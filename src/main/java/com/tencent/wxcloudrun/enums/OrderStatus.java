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
public enum OrderStatus {
    PENDING(10, "待安装"),
    INSTALL(20, "已安装完成"),
    CANCEL(30, "取消订单"),

    AFTER_SALES_PENDING(40, "售后中"),
    AFTER_SALES_INSTALL(50, "售后安装完成"),
    ;


    private static Logger logger = LoggerFactory.getLogger(OrderStatus.class);

    private static final Object _LOCK = new Object();

    private static Map<Integer, OrderStatus> _MAP;
    private static List<OrderStatus> _LIST;
    private static List<OrderStatus> _ALL_LIST;

    static {
        synchronized (_LOCK) {
            Map<Integer, OrderStatus> map = new HashMap<>();
            List<OrderStatus> list = new ArrayList<>();
            List<OrderStatus> listAll = new ArrayList<>();
            for (OrderStatus yesNoStatus : OrderStatus.values()) {
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

    OrderStatus(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }

    public static OrderStatus get(int value) {
        try {
            return _MAP.get(value);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    public static List<OrderStatus> list() {
        return _LIST;
    }

    public static List<OrderStatus> listAll() {
        return _ALL_LIST;
    }
}
