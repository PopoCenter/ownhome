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
 * 订单状态
 *
 * @author dongdongxie
 * @date 2024/05/12
 */
public enum OrderOperateType {
    CREATE(10, "创建订单"),
    EDIT(20, "编辑订单"),
    CANCEL(30, "取消订单"),
    INSTALL(40, "安装订单完成"),

    AFTER_SALES_CREATE(50, "创建售后订单"),
    AFTER_SALES_EDIT(60, "编辑售后订单"),
    AFTER_SALES_FINISH(70, "已完成售后订单"),
    AFTER_SALES_CANCEL(80, "已完成售后订单"),


    ;


    private static Logger logger = LoggerFactory.getLogger(OrderOperateType.class);

    private static final Object _LOCK = new Object();

    private static Map<Integer, OrderOperateType> _MAP;
    private static List<OrderOperateType> _LIST;
    private static List<OrderOperateType> _ALL_LIST;

    static {
        synchronized (_LOCK) {
            Map<Integer, OrderOperateType> map = new HashMap<>();
            List<OrderOperateType> list = new ArrayList<>();
            List<OrderOperateType> listAll = new ArrayList<>();
            for (OrderOperateType yesNoStatus : OrderOperateType.values()) {
                map.put(yesNoStatus.getValue(), yesNoStatus);
                listAll.add(yesNoStatus);
            }

            _MAP = ImmutableMap.copyOf(map);
            _LIST = ImmutableList.copyOf(list);
            _ALL_LIST = ImmutableList.copyOf(listAll);
        }
    }

    private int value;
    private String name;

    OrderOperateType(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }

    public static OrderOperateType get(int value) {
        try {
            return _MAP.get(value);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    public static List<OrderOperateType> list() {
        return _LIST;
    }

    public static List<OrderOperateType> listAll() {
        return _ALL_LIST;
    }
}
