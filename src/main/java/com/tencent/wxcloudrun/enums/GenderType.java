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
 * 是否
 *
 * @author dongdongxie
 * @date 2022/12/12
 */
public enum GenderType {
    MAN(1, "男"),
    WOMEN(2, "女");

    private static Logger logger = LoggerFactory.getLogger(GenderType.class);

    private static final Object _LOCK = new Object();

    private static Map<Integer, GenderType> _MAP;
    private static List<GenderType> _LIST;
    private static List<GenderType> _ALL_LIST;

    static {
        synchronized (_LOCK) {
            Map<Integer, GenderType> map = new HashMap<>();
            List<GenderType> list = new ArrayList<>();
            List<GenderType> listAll = new ArrayList<>();
            for (GenderType yesNoStatus : GenderType.values()) {
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

    GenderType(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }

    public static GenderType get(int value) {
        try {
            return _MAP.get(value);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    public static List<GenderType> list() {
        return _LIST;
    }

    public static List<GenderType> listAll() {
        return _ALL_LIST;
    }
}
