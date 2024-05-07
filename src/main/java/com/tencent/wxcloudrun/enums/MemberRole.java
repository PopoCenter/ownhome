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
 * 团队成员角色
 *
 * @author dongdongxie
 * @date 2024/05/12
 */
public enum MemberRole {
    //    DEFAULT(0, "默认"),
    COMMON(10, "普通"),
    MANAGER(20, "管理员");

    private static Logger logger = LoggerFactory.getLogger(MemberRole.class);

    private static final Object _LOCK = new Object();

    private static Map<Integer, MemberRole> _MAP;
    private static List<MemberRole> _LIST;
    private static List<MemberRole> _ALL_LIST;

    static {
        synchronized (_LOCK) {
            Map<Integer, MemberRole> map = new HashMap<>();
            List<MemberRole> list = new ArrayList<>();
            List<MemberRole> listAll = new ArrayList<>();
            for (MemberRole yesNoStatus : MemberRole.values()) {
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

    MemberRole(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }

    public static MemberRole get(int value) {
        try {
            return _MAP.get(value);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    public static List<MemberRole> list() {
        return _LIST;
    }

    public static List<MemberRole> listAll() {
        return _ALL_LIST;
    }
}
