package com.tencent.wxcloudrun.util;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author dongdongxie
 */
public class CoreMathUtils {

    /**
     * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指
     * 定精度，以后的数字四舍五入。
     *
     * @param v1    被除数
     * @param v2    除数
     * @param scale 表示表示需要精确到小数点以后几位。
     * @return 两个参数的商
     */
    public static double div(double v1, double v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 提供精确的乘法运算。
     *
     * @param v 乘数
     * @return 两个参数的积
     */
    public static double mul(double... v) {
        double result = v[0];
        BigDecimal r = new BigDecimal(Double.toString(result));
        for (int i = 1; i < v.length; i ++) {
            BigDecimal b = new BigDecimal(Double.toString(v[i]));
            result =  r.multiply(b).doubleValue();
        }
        return result;
    }


    /**
     * 提供精确的加法运算。
     *
     * @param v 加数
     * @return 两个参数的和
     */
    public static double add(double... v) {
        double result = v[0];
        for (int i = 1; i < v.length; i ++) {
            BigDecimal r = new BigDecimal(Double.toString(result));
            BigDecimal b = new BigDecimal(Double.toString(v[i]));
            result =  r.add(b).doubleValue();
        }
        return result;
    }

    /**
     * 提供精确的加法运算。
     *
     * @param v 加数
     * @return 两个参数的和
     */
    public static long add(long... v) {
        long result = v[0];
        for (int i = 1; i < v.length; i ++) {
            BigInteger r = new BigInteger(Long.toString(result));
            BigInteger b = new BigInteger(Long.toString(v[i]));
            result =  r.add(b).longValue();
        }
        return result;
    }

    /**
     * 提供精确的减法运算。
     *
     * @param v1 被减数
     * @param v2 减数
     * @return 两个参数的差
     */
    public static double sub(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.subtract(b2).doubleValue();
    }

    /**
     * 提供精确的减法运算。
     *
     * @param v1 被减数
     * @param v2 减数
     * @return 两个参数的差
     */
    public static long sub(long v1, long v2) {
        BigInteger b1 = new BigInteger(Long.toString(v1));
        BigInteger b2 = new BigInteger(Long.toString(v2));
        return b1.subtract(b2).longValue();
    }

}
