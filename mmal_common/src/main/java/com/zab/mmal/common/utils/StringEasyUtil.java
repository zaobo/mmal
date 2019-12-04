package com.zab.mmal.common.utils;

import com.zab.mmal.common.exceptions.WrongArgumentException;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringEasyUtil {

    public static final Pattern bracketsRowPattern = Pattern.compile("\\{\\}");
    public static final Pattern mapRowPattern = Pattern.compile("\\$\\{\\w+\\}");
    public static final Pattern ifRowPattern = Pattern.compile(">>>\\w+<<.+?>>>/\\w+<<");
    public static final char[] lowerChars = new char[26];

    static {
        for (char i = 'a'; i <= 'z'; i++) {
            lowerChars[i - 'a'] = i;
        }
    }

    /**
     * map中有值且不为空则保留，否则去掉
     *
     * @param org
     * @param map
     * @return
     */
    public static final String subStrByIfNullWithMap(String org, Map<String, Object> map) {
        if (null == org) {
            return null;
        }
        Matcher matcher = ifRowPattern.matcher(org);
        StringBuffer sb = new StringBuffer();

        while (matcher.find()) {
            String s = matcher.group();
            String sub = s.substring(3, s.length() - 2);
            int firstEnd = sub.indexOf("<<"), lastStart = sub.lastIndexOf(">>>");
            String key1 = sub.substring(0, firstEnd);
            String key2 = sub.substring(lastStart + 4);

            String str;
            if (!key1.equals(key2)) {
                // 前后key值不相等，跳过
                str = s;
            } else {
                boolean isNull = JudgeUtil.isEmpty(map) || JudgeUtil.isNullOr(map.get(key1));
                if (isNull) {
                    // 没有值
                    str = "";
                } else {
                    str = sub.substring(firstEnd + 2, lastStart);
                }
            }

            matcher.appendReplacement(sb, str);
        }
        matcher.appendTail(sb);

        return sb.toString();
    }

    /**
     * injectWithMap: 通过map,向字符串中注入预订的值
     *
     * @param org 为空时返回null.字符串格式为:${map的主键}
     * @param map 预订值映射.为空时,如果org中没找到预订值,不会报错,否则会报错
     * @return String org为空时返回null 创建人：Administrator 创建时间：2015年6月30日 下午4:45:23
     */
    public static final String injectWithMap(String org, Map<String, Object> map) {
        if (null == org) {
            return null;
        }
        Matcher matcher = mapRowPattern.matcher(org);
        StringBuffer sb = new StringBuffer();

        while (matcher.find()) {
            String s = matcher.group();
            String key = s.substring(2, s.length() - 1);
            if (null == map) {
                throw new WrongArgumentException("org string find '" + s + "',but map is null");
            }
            if (!map.containsKey(key)) {
                throw new WrongArgumentException(
                        "org string find '" + s + "',need key '" + key + "',but nothing in map");
            }
            Object obj = map.get(key);
            matcher.appendReplacement(sb, null == obj ? "" : obj.toString());
        }
        matcher.appendTail(sb);

        return sb.toString();
    }

    /**
     * injectByBrackets: 通过括号向字符串注入值
     *
     * @param org        为空时返回null.eg:injectByBrackets("你好,{}","tomcat");
     * @param parameters 预订值 为空时,如果org中没找到预订值,不会报错,否则会报错
     *                   不为空时,parameters的长度必须不小于org中预订位个数
     * @return org为空时返回null
     * @throws WrongArgumentException String 创建人：Administrator 创建时间：2015年6月30日
     *                                下午4:46:28
     */
    public static String injectByBrackets(String org, Object... parameters) throws WrongArgumentException {
        if (null == org) {
            return null;
        }
        Matcher matcher = bracketsRowPattern.matcher(org);
        StringBuffer sb = new StringBuffer();

        int index = 0;
        while (matcher.find()) {
            if (null == parameters) {
                throw new WrongArgumentException("org string find '{}',but no any parameters in",
                        bracketsRowPattern.pattern());
            }
            if (index >= parameters.length) {
                throw new WrongArgumentException("org string find '{}',but parameters doesn`t enough",
                        bracketsRowPattern.pattern());
            }
            Object v = parameters[index++];
            matcher.appendReplacement(sb, null == v ? "" : v.toString());
        }
        matcher.appendTail(sb);

        return sb.toString();
    }

    public static List<String> testInjectWithMap(String org, Map<String, Object> map) {
        if (null == org) {
            return Collections.emptyList();
        }
        Matcher matcher = mapRowPattern.matcher(org);
        List<String> list = new LinkedList<>();

        while (matcher.find()) {
            String s = matcher.group();
            String key = s.substring(2, s.length() - 1);
            if (null == map || !map.containsKey(key)) {
                list.add(key);
            }
        }

        return list;
    }

    /**
     * 原始字符串中是否含有所有预参数
     *
     * @param org
     * @param keys
     * @return
     */
    public static boolean hasInjectKeys(String org, Collection<String> keys) {
        if (null == org) {
            return true;
        }
        if (JudgeUtil.isEmpty(keys)) {
            return false;
        }
        Matcher matcher = mapRowPattern.matcher(org);

        Set<String> set = new HashSet<>();
        set.addAll(keys);
        while (matcher.find()) {
            String s = matcher.group();
            String key = s.substring(2, s.length() - 1);
            set.remove(key);

            if (set.isEmpty()) {
                return true;
            }
        }

        return set.isEmpty();
    }

    /**
     * 获取原始字符串中的预参数
     *
     * @param org
     * @return
     */
    public static List<String> injectKeys(String org) {
        if (null == org) {
            return Collections.emptyList();
        }
        Matcher matcher = mapRowPattern.matcher(org);
        List<String> list = new LinkedList<>();

        while (matcher.find()) {
            String s = matcher.group();
            String key = s.substring(2, s.length() - 1);

            if (!list.contains(key)) {
                list.add(key);
            }
        }

        return list;
    }

    /**
     * toFirstCaps: 首字母大写
     *
     * @param s 为空时返回null
     * @return String 创建人：Administrator 创建时间：2015年6月30日 下午4:51:09
     */
    public static String toFirstCaps(String s) {
        if (null == s || "".equals(s)) {
            return s;
        }
        if (1 == s.length()) {
            return s.toUpperCase();
        } else {
            return s.substring(0, 1).toUpperCase() + s.substring(1);
        }
    }

    /**
     * 首字母小写 toFirstLow:
     *
     * @param s 为空时返回null
     * @return String 创建人：Administrator 创建时间：2015年6月30日 下午4:51:27
     */
    public static String toFirstLow(String s) {
        if (null == s || "".equals(s)) {
            return s;
        }
        if (1 == s.length()) {
            return s.toLowerCase();
        } else {
            return s.substring(0, 1).toLowerCase() + s.substring(1);
        }
    }

    /**
     * appUniqueKey: 本次启动服务中,唯一标识的键
     *
     * @return String
     * @author Administrator
     * @date 2015年12月8日 上午10:25:45
     */
    public static String appUniqueKey() {
        return UUID.randomUUID().toString();
    }

    /**
     * 所有项目中都唯一的key
     *
     * @return
     */
    public static String allUniqueKey() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 获取一个尽量不重复的单词(大写+下划线)
     *
     * @param min
     * @param max
     * @return
     */
    public static String uniqueStr(int min, int max) {
        if (max < min) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        char before = '_';
        for (; sb.length() <= max; ) {
            if (sb.length() > min && 0 == random.nextInt(max - sb.length() + 1)) {
                break;
            }
            if ('_' != before && 0 == random.nextInt(3)) {
                before = '_';
            } else {
                before = (char) ('A' + random.nextInt(26));
            }
            sb.append(before);
        }
        return sb.toString();
    }

    public static String[] splitRemove(String str, String regex, boolean all) {
        if (null == str) {
            return null;
        }
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);

        List<String> list = new LinkedList<>();
        while (matcher.find()) {
            StringBuffer sb = new StringBuffer();
            matcher.appendReplacement(sb, "");

            String pre = sb.toString();
            if ("".equals(pre) || (all && "".equals(pre.trim()))) {
                continue;
            }
            list.add(pre);
        }
        StringBuffer sb = new StringBuffer();
        matcher.appendTail(sb);
        String pre = sb.toString();
        if (!"".equals(pre) && (!all || !"".equals(pre.trim()))) {
            list.add(pre);
        }

        String[] re = new String[list.size()];
        System.arraycopy(list.toArray(), 0, re, 0, list.size());
        return re;
    }

    public static String toFieldName(String colName) {
        String fieldName;
        String[] arr = colName.split("_");
        StringBuilder sb = new StringBuilder();
        boolean big = false;
        for (String s : arr) {
            String name = s.toLowerCase();
            if (name.length() > 0) {
                // 首字母大写
                if (big) {
                    sb.append(StringEasyUtil.toFirstCaps(name));
                } else {
                    sb.append(name);
                    big = true;
                }
            }
        }
        fieldName = sb.toString();
        return fieldName;
    }

    public static int hashCodeTrim(String str) {
        if (null == str) {
            return 0;
        }
        return str.trim().hashCode();
    }

    public static String toString(Collection<?> collection) {
        if (JudgeUtil.isEmpty(collection)) {
            return "";
        }
        return Arrays.toString(collection.toArray());
    }

    public static String link(Collection<?> collection, String link, String nullAs) {

        if (JudgeUtil.isEmpty(collection)) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        AtomicBoolean add = new AtomicBoolean();
        for (Object obj : collection) {
            if (JudgeUtil.isNullOr(obj)) {
                if (null == nullAs) {
                    continue;
                } else {
                    obj = nullAs;
                }
            }
            if (add.getAndSet(true)) {
                sb.append(link);
            }
            sb.append(obj);
        }

        return sb.toString();

    }

    public static String link(Collection<?> collection, String link) {
        return link(collection, link, null);
    }

    public static String safeRandomLower(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(lowerChars.length);
            sb.append(lowerChars[index]);
        }
        return sb.toString();
    }

    public static String exceptionInfo(Throwable e) throws IOException {
        try (StringWriter stringWriter = new StringWriter();
             PrintWriter printWriter = new PrintWriter(stringWriter, true);) {
            e.printStackTrace(printWriter);
            StringBuffer error = stringWriter.getBuffer();
            return error.toString();
        }
    }

    public static boolean equals(String str1, String str2) {
        String s1, s2;
        if (null == str1) {
            s1 = "";
        } else {
            s1 = str1.trim();
        }

        if (null == str2) {
            s2 = "";
        } else {
            s2 = str2.trim();
        }

        return s1.equals(s2);
    }

    public static int compare(String str1, String str2) {
        String s1, s2;
        if (null == str1) {
            s1 = "";
        } else {
            s1 = str1.trim();
        }

        if (null == str2) {
            s2 = "";
        } else {
            s2 = str2.trim();
        }

        return s1.compareTo(s2);
    }

    public static String upperChars(String str) {
        if (null == str) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        for (char c : str.toCharArray()) {
            if (c >= 'A' && c <= 'Z') {
                sb.append(c);
            }
        }

        return sb.toString();
    }

    public static String lowerChars(String str) {
        if (null == str) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        for (char c : str.toCharArray()) {
            if (c >= 'a' && c <= 'z') {
                sb.append(c);
            }
        }

        return sb.toString();
    }

    public static boolean equalsTrim(String str1, String str2) {
        if (StringUtils.isBlank(str1)) {
            str1 = "";
        }
        if (StringUtils.isBlank(str2)) {
            str2 = "";
        }
        return StringUtils.equals(StringUtils.trim(str1), StringUtils.trim(str2));
    }

    public static boolean equalsTrimIgnoreCase(String str1, String str2) {
        if (StringUtils.isBlank(str1)) {
            str1 = "";
        }
        if (StringUtils.isBlank(str2)) {
            str2 = "";
        }
        return StringUtils.equalsIgnoreCase(StringUtils.trim(str1), StringUtils.trim(str2));
    }

    public static String rTrim(String str) {
        if (StringUtils.isBlank(str)) {
            return "";
        }

        int end = str.length();
        for (; end > 0; end--) {
            char c = str.charAt(end - 1);
            if (c != ' ' && c != '\r' && c != '\n') {
                break;
            }
        }

        return str.substring(0, end);
    }
}