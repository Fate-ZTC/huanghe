package com.parkbobo.utils;


public class GisUtil {

	private static GisUtil gisUtil;
	private GisUtil(){}
	/**
	 * 
	 * 获得一个单一实例
	 * @return MD5   
	 */
	public synchronized static GisUtil getInstance(){
		if(gisUtil==null){
			gisUtil = new GisUtil();
		}
		return gisUtil;
	}
	/**
	 * 计算两点之间距离
	 * @param lng1
	 * @param lat1
	 * @param lng2
	 * @param lat2
	 * @return
	 */
	public double distanceByLngLat(double lng1, double lat1, double lng2, double lat2) {
        double radLat1 = lat1 * Math.PI / 180;
        double radLat2 = lat2 * Math.PI / 180;
        double a = radLat1 - radLat2;
        double b = lng1 * Math.PI / 180 - lng2 * Math.PI / 180;
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1)
                * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * 6378137.0;// 取WGS84标准参考椭球中的地球长半径(单位:m)
        s = Math.round(s * 10000) / 10000;
        return s;
    }
	/**
	 * @param args
	 */
	public static void main(String[] args) {
//		Double d = 104.041085d;
//		Double dd = 0.008991241d;
//		Double ddd = 2.0d;
//		System.out.println(dd *ddd );
//		System.out.println(d+0.010444641);
//		System.out.println(distanceByLngLat(104.041085,30.621221,104.041085,30.612230));
//		System.out.println("一公里纬度系数差值：" + (30.621221f - 30.612230f) );//一公里纬度系数差值：0.008991241
//		System.out.println(30.621221 + (30.621221 - 30.612230));
//		System.out.println(distanceByLngLat(104.041085,30.621221,104.041085,30.621221 + (30.621221 - 30.612230)));
//		System.out.println(distanceByLngLat(104.041171,30.621221,104.051616,30.621221));
//		System.out.println("一公里经度系数差值：" + (104.051616f - 104.041171f));//一公里经度系数差值：0.010444641
//		System.out.println(104.051616f + 0.010444641);
//		System.out.println(distanceByLngLat(104.041171,30.621221,104.06205749500391,30.621221));
//		System.out.println(distanceByLngLat(104.082600139081, 30.6092969106826 ,104.088158, 30.658205));
//		System.out.println(GisUtil.getInstance().distanceByLngLat(104.078650571522 ,30.6121285657886, 104.078690384361, 30.6121207519257));
//		System.out.println(GisUtil.getInstance().distanceByLngLat(104.078690384361, 30.6121207519257,104.078686316477, 30.6120990317717));
	}	              
	              

}
