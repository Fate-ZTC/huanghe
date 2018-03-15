package com.parkbobo.utils;

import java.io.ByteArrayInputStream;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class AESOperator {

    /*
     * 加密用的Key
     */
    private static final String sKey = Configuration.getInstance().getValue("aes_key");//key
    private static AESOperator instance = null;

    private AESOperator() {

    }

    public static AESOperator getInstance() {
        if (instance == null)
            instance = new AESOperator();
        return instance;
    }
    
public static String Encrypt(String encData ,String secretKey,String vector) throws Exception {
        
        if(secretKey == null) {
            return null;
        }
        if(secretKey.length() != 16) {
            return null;
        }
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        byte[] raw = secretKey.getBytes();
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encrypted = cipher.doFinal(encData.getBytes("utf-8"));
        return new BASE64Encoder().encode(encrypted);// 此处使用BASE64做转码。
    }


    /**
     * 加密
     * @param sSrc 需要加密的字符串
     * @return 加密返回字符串
     * */
    public String encrypt(String sSrc) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        byte[] raw = sKey.getBytes();
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encrypted = cipher.doFinal(sSrc.getBytes("gbk"));
        return new BASE64Encoder().encode(encrypted).replaceAll("\\r\\n", "");// 此处使用BASE64做转码。
    }

    /**
     * 解密
     * @param sSrc 需要解密的字符串
     * @return 解密返回字符串
     * */
    public String decrypt(String sSrc) throws Exception {
        try {
            byte[] raw = sKey.getBytes("ASCII");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            byte[] encrypted1 = new BASE64Decoder().decodeBuffer(sSrc);// 先用base64解密
            byte[] original = cipher.doFinal(encrypted1);
            String originalString = new String(original, "gbk");
            return originalString;
        } catch (Exception ex) {
            return null;
        }
    }
    /**
     * 加密
     * @param sSrc 需要加密的字符串
     *        key 密匙
     * 
     * */
    public String decrypt(String sSrc,String key) throws Exception {
        try {
            byte[] raw = key.getBytes("ASCII");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            byte[] encrypted1 = new BASE64Decoder().decodeBuffer(new ByteArrayInputStream(sSrc.getBytes("UTF-8")));// 先用base64解密
            byte[] original = cipher.doFinal(encrypted1);
            String originalString = new String(original, "UTF-8");
            return originalString;
        } catch (Exception ex) {
            return null;
        }
    }
    
    public static String encodeBytes(byte[] bytes) {
        StringBuffer strBuf = new StringBuffer();

        for (int i = 0; i < bytes.length; i++) {
            strBuf.append((char) (((bytes[i] >> 4) & 0xF) + ((int) 'a')));
            strBuf.append((char) (((bytes[i]) & 0xF) + ((int) 'a')));
        }

        return strBuf.toString();
    }

    public static void main(String[] args) throws Exception {
        // 需要加密的字串
        //String cSrc = "[{\"request_no\":\"1001\",\"service_code\":\"FS0001\",\"contract_id\":\"100002\",\"order_id\":\"0\",\"phone_id\":\"13913996922\",\"plat_offer_id\":\"100094\",\"channel_id\":\"1\",\"activity_id\":\"100045\"}]";
    	String cSrc = "{\"message\": \"请求参数不正确\",\"errorCode\": 300009,\"token\": \"\"}";
        // 加密
        long lStart = System.currentTimeMillis();
        String enString = AESOperator.getInstance().encrypt(cSrc);
        System.out.println("加密后的字串是：" + enString);

        long lUseTime = System.currentTimeMillis() - lStart;
        System.out.println("加密耗时：" + lUseTime + "毫秒");
        // 解密
        lStart = System.currentTimeMillis();
        String d = "";
        String DeString = AESOperator.getInstance().decrypt("XoBVjMHKTzDkBTB0fDD4f+UGbyUG1O3ZuhL47kQ4mt6z6pbyZHxJwVgAXjndED9gw3DWkkwPNx0i/6jKJoOxjg==");
        System.out.println("解密后的字串是：" + DeString);
        lUseTime = System.currentTimeMillis() - lStart;
        System.out.println("解密耗时：" + lUseTime + "毫秒");
    }

}
