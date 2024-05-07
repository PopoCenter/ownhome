package com.tencent.wxcloudrun.util;

import com.google.common.base.Charsets;
import com.google.common.io.BaseEncoding;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.codec.CodecException;

import java.io.UnsupportedEncodingException;


/**
 * 安全工具集合类
 *
 * @author dongdongxie
 */
public class CoreSecurityUtils {

    private final transient static Logger logger = LoggerFactory.getLogger(CoreSecurityUtils.class);

    /**
     * base64编码
     *
     * @param input 输入数据
     * @return 编码结果
     */
    public static String encodeBase64(byte[] input) {
        return BaseEncoding.base64().encode(input);
    }

    /**
     * base64解码
     *
     * @param encodeString 已编码字符串
     * @return 解码结果
     */
    public static byte[] decodeBase64(String encodeString) {
        return BaseEncoding.base64().decode(encodeString);
    }

    /**
     * MD5加密
     *
     * @param source   源数据
     * @param encoding 编码
     * @return md5摘要
     */
    public static String encryptMD5(String source, String encoding) throws CodecException, UnsupportedEncodingException {
        return DigestUtils.md5Hex(source.getBytes(encoding));
    }


    /**
     * MD5加密
     *
     * @param source 源数据
     * @return 加密结果
     */
    public static String encryptMD5(String source) throws CodecException, UnsupportedEncodingException {
        return CoreSecurityUtils.encryptMD5(source, Charsets.UTF_8.name());
    }
}
