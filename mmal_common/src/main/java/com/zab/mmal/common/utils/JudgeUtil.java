package com.zab.mmal.common.utils;

import org.apache.commons.lang.StringUtils;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 判断工具
 *
 * @author user
 * @date 2016年8月22日
 */
public class JudgeUtil {

    public static boolean isEmpty(Collection<?> collection) {
        return null == collection || collection.isEmpty();
    }

    public static boolean allEmpty(Collection<?> collection) {
        if (null == collection || collection.isEmpty()) {
            return true;
        }
        for (Object obj : collection) {
            if (!JudgeUtil.isNullOr(obj)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isEmpty(Object[] arr) {
        return null == arr || 0 == arr.length;
    }

    public static boolean allEmpty(Object[] arr) {
        if (null == arr || 0 == arr.length) {
            return true;
        }
        for (Object obj : arr) {
            if (!JudgeUtil.isNullOr(obj)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isEmpty(Map<?, ?> map) {
        return null == map || map.isEmpty();
    }

    public static boolean isDBNull(Number number) {
        return null == number || 0 == number.intValue();
    }

    public static int add(Integer... integers) {
        int i = 0;
        if (null != integers) {
            for (Integer integer : integers) {
                if (null != integer) {
                    i += integer;
                }
            }
        }

        return i;
    }

    public static int size(Collection<?> collection) {
        return null == collection ? 0 : collection.size();
    }

    public static boolean isDBEq(Long l1, Long l2) {
        if (null == l1) {
            return null == l2 || l2.equals(0l);
        } else if (null == l2) {
            return l1.equals(0l);
        } else {
            return l1.equals(l2);
        }
    }

    public static boolean isDBEq(Integer i1, Integer i2) {
        if (null == i1) {
            return null == i1 || i2.equals(0);
        } else if (null == i2) {
            return i1.equals(0);
        } else {
            return i1.equals(i2);
        }
    }

    public static int length(Object[] arr) {
        if (null == arr) {
            return 0;
        }
        return arr.length;
    }

    /**
     * 其中一个为空，都直接返回null
     *
     * @param objs
     * @return
     */
    public static boolean isNullOr(Object... objs) {
        if (!JudgeUtil.isEmpty(objs)) {
            for (Object obj : objs) {
                if (null != obj) {
                    boolean hasNull;
                    if (String.class.isInstance(obj)) {
                        hasNull = StringUtils.isBlank((String) obj);
                    } else if (Number.class.isInstance(obj)) {
                        hasNull = JudgeUtil.isDBNull((Number) obj);
                    } else if (obj.getClass().isArray()) {
                        hasNull = 0 == Array.getLength(obj);
                    } else if (Map.class.isInstance(obj)) {
                        hasNull = JudgeUtil.isEmpty((Map<?, ?>) obj);
                    } else if (Collection.class.isInstance(obj)) {
                        hasNull = JudgeUtil.isEmpty((Collection<?>) obj);
                    } else {
                        // 非字符串和数字的，直接判断是否为null
                        hasNull = false;
                    }
                    // 如果存在为空的，直接返回
                    if (hasNull) {
                        return true;
                    }
                } else {
                    return true;
                }
            }
            return false;
        } else {
            return true;
        }
    }

    public static int compareTo(Integer i1, Integer i2) {
        if (null == i1 && null == i2) {
            return 0;
        } else if (null != i1 && null != i2) {
            return i1.compareTo(i2);
        } else if (null != i1) {
            return i1.compareTo(0);
        } else {
            return -1 * i2.compareTo(0);
        }
    }

    public static int compareTo(Long i1, Long i2) {
        if (null == i1 && null == i2) {
            return 0;
        } else if (null != i1 && null != i2) {
            return i1.compareTo(i2);
        } else if (null != i1) {
            return i1.compareTo(0l);
        } else {
            return -1 * i2.compareTo(0l);
        }
    }

    public static int compareTo(Double i1, Double i2) {
        if (null == i1 && null == i2) {
            return 0;
        } else if (null != i1 && null != i2) {
            return i1.compareTo(i2);
        } else if (null != i1) {
            return i1.compareTo(0d);
        } else {
            return -1 * i2.compareTo(0d);
        }
    }

    public static int plus(Integer i1, Integer i2) {
        if (null == i1) {
            i1 = 0;
        }
        if (null == i2) {
            i2 = 0;
        }
        return i1 + i2;
    }

    public static boolean same(Map<String, Object> map1,
                               Map<String, Object> map2) {
        if (JudgeUtil.isEmpty(map1)) {
            return JudgeUtil.isEmpty(map2);
        }
        if (JudgeUtil.isEmpty(map2)) {
            return JudgeUtil.isEmpty(map1);
        }

        for (Entry<String, Object> entry : map1.entrySet()) {
            Object v = map2.get(entry.getValue());
            if (null == entry.getValue()) {
                return null == v;
            }

            // 忽略集合和数组
            if (!v.equals(entry.getValue())) {
                return false;
            }
        }

        for (Entry<String, Object> entry : map2.entrySet()) {
            Object v = map1.get(entry.getValue());
            if (null == entry.getValue()) {
                return null == v;
            }

            if (!v.equals(entry.getValue())) {
                return false;
            }
        }

        return true;
    }

}