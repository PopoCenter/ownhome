package com.tencent.wxcloudrun.util;


import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.concurrent.TimeUnit;

/**
 * id生成工具
 *
 * @author dongdongxie
 * @date 2022/12/12
 */
public class UniqueIdUtils {
    /**
     * 按秒生成
     * 模仿twitter的SnowFlake（雪花）
     * 1. 40bit：6位自增，4位节点，29位时间秒数，1位符号位
     * 2. 支持每个节点（node）每秒下发64个id
     * 3. 时间起点 2019-07-01 00:00:00，29位时间秒数，可以用到2036-07-04 18:48:32
     * 4. 支持16个节点
     * 5, 可能产生10-12位的长整型数字，满足带时间格式的19位编码需求
     */
    public static class Seconds {
        private static final int SERIAL_BIT = 6;
        private static final int NODE_BIT = 4;
        private static final int TIME_STAMP_BIT = 29;

        /**
         * 对比时间起点 2019-07-01 00:00:00 时间戳
         */
        private static long START_TIME_POINT = LocalDateTime.of(2019, 7, 1, 0, 0, 0).atZone(ZoneId.systemDefault()).toEpochSecond();

        /**
         * 1 bit: 符号位
         */
        private static long FLAG = 0;

        /**
         * 29 bit: 时间（秒）
         */
        private static long LAST_TIME_STAMP = -1L;

        /**
         * 4 bit: 节点号
         */
        private static long NODE_ID = 0;

        /**
         * 6 bit: 序列号
         */
        private static long SERIAL = 0;

        /**
         * 序列号 mask，防止溢出
         */
        private static final long SERIAL_MASK = ~(-1L << SERIAL_BIT);

        public static synchronized long uniqueId(long nodeId) {
            long nowTimePoint = LocalDateTime.now().atZone(ZoneId.systemDefault()).toEpochSecond();

            long currentTimeStamp = nowTimePoint - START_TIME_POINT;
            if (LAST_TIME_STAMP == currentTimeStamp) {
                // 同一秒，序列号递增
                long temp = (SERIAL + 1) & SERIAL_MASK;
                if (temp == 0) {
                    // 当前秒内序列已满，重新获取
                    sleep();
                    return uniqueId(nodeId);
                }
                SERIAL = temp;
            } else {
                LAST_TIME_STAMP = currentTimeStamp;
                SERIAL = 0L;
            }
            return SERIAL
                    | (nodeId << SERIAL_BIT)
                    | (LAST_TIME_STAMP << (SERIAL_BIT + NODE_BIT))
                    | (FLAG << (SERIAL_BIT + NODE_BIT + TIME_STAMP_BIT));
        }

        public static long uniqueId() {
            return uniqueId(NODE_ID);
        }

        private static void sleep() {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException();
            }
        }
    }

    /**
     * 按毫秒生成
     * 模仿twitter的SnowFlake（雪花）
     * 1. 50bit：4位自增，4位节点，41位时间秒数，1位符号位
     * 2. 支持每个节点（node）每毫秒下发16个id
     * 3. 时间起点 2019-07-01 00:00:00，41位时间毫秒数，可以用到2089-03-06T15:47:35.552
     * 4. 支持16个节点
     */
    public static class Millis {
        private static final int SERIAL_BIT = 4;
        private static final int NODE_BIT = 4;
        private static final int TIME_STAMP_BIT = 41;

        /**
         * 对比时间起点 2019-07-01 00:00:00 时间戳
         */
        private static long START_TIME_POINT = LocalDateTime.of(2019, 7, 1, 0, 0, 0).toInstant(ZoneOffset.of("+8")).toEpochMilli();

        /**
         * 1 bit: 符号位
         */
        private static long FLAG = 0;

        /**
         * 41 bit: 时间（毫秒）
         */
        private static long LAST_TIME_STAMP = -1L;

        /**
         * 4 bit: 节点号
         */
        private static long NODE_ID = 0;

        /**
         * 4 bit: 序列号
         */
        private static long SERIAL = 0;

        /**
         * 序列号 mask，防止溢出
         */
        private static final long SERIAL_MASK = ~(-1L << SERIAL_BIT);

        public static synchronized long uniqueId(long nodeId) {
            long currentTimeStamp = System.currentTimeMillis() - START_TIME_POINT;
            if (LAST_TIME_STAMP == currentTimeStamp) {
                // 同一毫秒，序列号递增
                long temp = (SERIAL + 1) & SERIAL_MASK;
                if (temp == 0) {
                    // 当前秒内序列已满，重新获取
                    sleep();
                    return uniqueId(nodeId);
                }
                SERIAL = temp;
            } else {
                LAST_TIME_STAMP = currentTimeStamp;
                SERIAL = 0L;
            }
            return SERIAL
                    | (nodeId << SERIAL_BIT)
                    | (LAST_TIME_STAMP << (SERIAL_BIT + NODE_BIT))
                    | (FLAG << (SERIAL_BIT + NODE_BIT + TIME_STAMP_BIT));
        }

        public static long uniqueId() {
            return uniqueId(NODE_ID);
        }

        private static void sleep() {
            try {
                TimeUnit.MILLISECONDS.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException();
            }
        }
    }
}
