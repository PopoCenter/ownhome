package com.tencent.wxcloudrun.enums;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 自定义返回枚举
 *
 * @author dongdongxie
 * @date 2024/05/17
 */
public enum ResponseEnum {

    /**
     * 10000+
     */
    SYSTEM_CODE(9999, "服务异常，请联系管理员"),

    /**
     * 0
     */
    SUCCESS(0, "操作成功"),


    CODE_NOT_EXIST(101, "验证码失效"),
    CODE_WRONG(102, "验证码不正确"),


    USER_NOT_EXIST(103, "用户不存在"),

    USER_IS_EXIST(104, "用户已注册"),


    FAILURE(999, "操作失败"),

    ;

    private static Logger logger = LoggerFactory.getLogger(ResponseEnum.class);
    private static final Object _LOCK = new Object();
    private static Map<Integer, ResponseEnum> _MAP;
    private static List<ResponseEnum> _LIST;
    private static List<ResponseEnum> _ALL_LIST;

    static {
        synchronized (_LOCK) {
            Map<Integer, ResponseEnum> map = new HashMap<>();
            List<ResponseEnum> list = new ArrayList<>();
            List<ResponseEnum> listAll = new ArrayList<>();
            for (ResponseEnum OperateType : ResponseEnum.values()) {
                map.put(OperateType.getValue(), OperateType);
                listAll.add(OperateType);
            }

            _MAP = ImmutableMap.copyOf(map);
            _LIST = ImmutableList.copyOf(list);
            _ALL_LIST = ImmutableList.copyOf(listAll);
        }
    }

    private int value;
    private String name;

    ResponseEnum(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }

    public static ResponseEnum get(int value) {
        try {
            return _MAP.get(value);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    public static List<ResponseEnum> list() {
        return _LIST;
    }

    public static List<ResponseEnum> listAll() {
        return _ALL_LIST;
    }
}
