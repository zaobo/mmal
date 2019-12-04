package com.zab.mmal.common.utils;

import org.apache.commons.lang.StringUtils;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 判断对象属性是否是null
 */
public class ObjectIsEmptytils {

    public static Map<String, Object> getNotEmptyField(Object object) {
        if (null == object) {
            return null;
        }
        // 获取类对象
        Class<?> clazz = object.getClass();
        Field[] fields = clazz.getDeclaredFields();
        Map<String, Object> map = new LinkedHashMap<>();
        for (Field field : fields) {
            field.setAccessible(true);
            if (StringUtils.equalsIgnoreCase("serialVersionUID", field.getName())) {
                continue;
            }
            try {

                if (field.getType().equals(String.class) && StringUtils.isNotBlank((String) field.get(object))) {
                    map.put(field.getName(), field.get(object));
                }

                if (!JudgeUtil.isNullOr(field.get(object))) {
                    map.put(field.getName(), field.get(object));
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return map;
    }

}
