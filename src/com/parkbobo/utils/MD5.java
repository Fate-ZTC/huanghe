package com.parkbobo.utils;



import java.security.MessageDigest;

/**
 * 
 * <p>MD5加密辅助类</p>
 * @author LH
 * 
 */
public class MD5 {
	private final static String[] hexDigits = { "0", "1", "2", "3", "4", "5",
		"6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };
	private static MD5 md5;
	/**
	 * 
	 * 私有构造函数  ，类不能用new关键字实例
	 *
	 */
	private MD5(){};
	/**
	 * 
	 * 获得一个单一实例
	 * @return MD5   
	 */
	public synchronized static MD5 getDefaultInstance(){
		if(md5==null){
			md5 = new MD5();
		}
		return md5;
	}
	/**
	 * 转换字节数组为16进制字串
	 * @param b 字节数组
	 * @return 16进制字串
	 */
	private static String byteArrayToHexString(byte[] b) {
		StringBuffer resultSb = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			resultSb.append(byteToHexString(b[i]));
		}
		return resultSb.toString();
	}
	private static String byteToHexString(byte b) {
		int n = b;
		if (n < 0)
			n = 256 + n;
		int d1 = n / 16;
		int d2 = n % 16;
		return hexDigits[d1] + hexDigits[d2];
	}
	/**
	 * 
	 * MD5加密
	 * @param  plainText 需要加密字符串
	 * @return String  MD5加密后字符串
	 */
	public  String MD5Encode(String plainText) {
		String resultString = null;
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			resultString = byteArrayToHexString(md.digest(plainText
					.getBytes()));
		} catch (Exception ex) {
			System.out.println("MD5加密异常");
		}
		return resultString;
	}
	

	public  String md5(String string) 
	{
		char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',
		'e', 'f' };
		try {
			byte[] bytes = string.getBytes();
			MessageDigest messageDigest = MessageDigest.getInstance("MD5");
			messageDigest.update(bytes);
			byte[] updateBytes = messageDigest.digest();
			int len = updateBytes.length;
			char myChar[] = new char[len * 2];
			int k = 0;
			for (int i = 0; i < len; i++) {
				byte byte0 = updateBytes[i];
				myChar[k++] = hexDigits[byte0 >>> 4 & 0x0f];
				myChar[k++] = hexDigits[byte0 & 0x0f];
			}
			return new String(myChar);
		} 
		catch (Exception e) 
		{
			return null;
		}
	}
	
	public static void main(String[] args) {
		System.out.println(MD5.getDefaultInstance().MD5Encode("a4bf5363f7bd76c5a4975b75f19f63caldingadmin"));
	}
}
