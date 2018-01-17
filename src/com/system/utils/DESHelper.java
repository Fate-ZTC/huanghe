package com.system.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;


public class DESHelper {
	private static byte[] iv = { 1, 2, 3, 4, 5, 6, 7, 8 };
	/**
	 * 加密
	 * @param encryptString
	 * @param encryptKey
	 * @return
	 */
    public static String encryptDES(String encryptString, String encryptKey){
    	byte[] encryptedData = null;
        IvParameterSpec zeroIv = new IvParameterSpec(iv);
        SecretKeySpec key = new SecretKeySpec(encryptKey.getBytes(), "DES");
        try {
			Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, key, zeroIv);
			encryptedData = cipher.doFinal(encryptString.getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return Base64.encodeBase64String(encryptedData);
    }
    /**
     * 解密
     * @param decryptString
     * @param decryptKey
     * @return
     */
    public static String decryptDES(String decryptString, String decryptKey){
    	byte decryptedData[] = null;
		byte[] byteMi = Base64.decodeBase64(decryptString);
		IvParameterSpec zeroIv = new IvParameterSpec(iv);
		SecretKeySpec key = new SecretKeySpec(decryptKey.getBytes(), "DES");
		try {
			Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, key, zeroIv);
			decryptedData = cipher.doFinal(byteMi);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return new String(decryptedData);
	}
    /**
	 * 将二进制转换成16进制
	 * @method parseByte2HexStr
	 * @param buf
	 * @return
	 */
	public static String parseByte2HexStr(byte buf[]){
		StringBuffer sb = new StringBuffer();
		for(int i = 0; i < buf.length; i++){
			String hex = Integer.toHexString(buf[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			sb.append(hex.toUpperCase());
		}
		return sb.toString();
	}
	
	/**
	 * 将16进制转换为二进制
	 * @method parseHexStr2Byte
	 * @param hexStr
	 * @return
	 */
	public static byte[] parseHexStr2Byte(String hexStr){
		if(hexStr.length() < 1)
			return null;
		byte[] result = new byte[hexStr.length()/2];
		for (int i = 0;i< hexStr.length()/2; i++) {
			int high = Integer.parseInt(hexStr.substring(i*2, i*2+1), 16);
			int low = Integer.parseInt(hexStr.substring(i*2+1, i*2+2), 16);
			result[i] = (byte) (high * 16 + low);
		}
		return result;
	}
	public static void main(String[] args) {
		String content = "{\"content\":\"" + DigestUtils.md5Hex("175lqkjhhxy20070527")+ "\",\"userid\":\"20070527\",\"timestamp\":\"" + System.currentTimeMillis()/1000 + "\"}";
		String password = "lqkjhhxy";
		System.out.println(System.currentTimeMillis()/1000);
		String ciphertext = DESHelper.encryptDES(content, password);
		System.out.println("明文：" + content);
		System.out.println("密钥：" + password);
		System.out.println("密文：" + ciphertext);
		System.out.println("解密后：" + DESHelper.decryptDES("/7ucqhNfG1ns8kTmRVzuFg1jS9VWPA0gIio0JrGYa0pk4JohocqQt6hjgZpn1dGX1FmNypfLfgQ5KvTPjkurqtnM9jnaX4zFTc1Ajy8sK+yoJ07e6lsJj2l79cvkItPJ", password));
		
	}
}
