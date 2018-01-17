package com.parkbobo.utils;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
 
public class MD532 {
	private static MD532 md532;
	/**
	 * 
	 * 私有构造函数  ，类不能用new关键字实例
	 *
	 */
	private MD532(){};
	/**
	 * 
	 * 获得一个单一实例
	 * @return MD5   
	 */
	public synchronized static MD532 getDefaultInstance(){
		if(md532==null){
			md532 = new MD532();
		}
		return md532;
	}
	
	
    public static void main(String[] args) {
    	String encryption = MD532.getDefaultInstance().encryption("parkboboweibokeji");
    	System.out.println(encryption);
    }
    
    /**
     * 
     * @param plainText
     *            明文
     * @return 32位密文
     */
    public String encryption(String plainText) {
        String re_md5 = new String();
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plainText.getBytes("UTF-8"));
            byte b[] = md.digest();
 
            int i;
 
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
 
            re_md5 = buf.toString();
 
        } catch (Exception e) {
            e.printStackTrace();
        }
        return re_md5;
    }
}