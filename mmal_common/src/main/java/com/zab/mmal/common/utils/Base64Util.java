package com.zab.mmal.common.utils;

import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;

import java.io.UnsupportedEncodingException;

/**
 * author：YZH
 * time: 2019/10/30 11:52
 * company: 重庆知行宏图科技有限公司
 * description: base64加解密工具
 **/
@Slf4j
public class Base64Util {

    private static final String WEB_BASE64_CHARSET = "UTF-8";

    /**
     * 将tableName进行base64解密
     *
     * @param str
     * @return
     */
    public static String strDecode(String str) {
        //将tableName的base64解密
        byte[] arr;
        try {
            arr = str.getBytes(WEB_BASE64_CHARSET);
            if (!Base64.isBase64(arr)) {
                return null;
            }
            byte[] brr = Base64.decodeBase64(arr);
            String reallyName = new String(brr, WEB_BASE64_CHARSET);
            return reallyName;
        } catch (UnsupportedEncodingException e) {
            log.error("base64解析失败", e);
        }
        return null;
    }

    public static String strEncode(String data) throws UnsupportedEncodingException {
        byte[] bytes = Base64.encodeBase64(data.getBytes(WEB_BASE64_CHARSET));
        return new String(bytes, WEB_BASE64_CHARSET);
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        System.out.println(strEncode("lisi"));
    }
}
