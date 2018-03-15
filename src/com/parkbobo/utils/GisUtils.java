package com.parkbobo.utils;

import org.geotools.geometry.jts.JTSFactoryFinder;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

public class GisUtils {
	private static GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory( null );
	
	public static Point createPoint(Coordinate coord){
		Point point = geometryFactory.createPoint( coord );
		return point;
	}
	public static Point createPoint(double lon, double lat){
		Coordinate coord = new Coordinate(lon, lat);
		return createPoint(coord);
	}
	/**
	 * 
	 * @param pointWkt POINT (109.013388 32.715519)
	 * @return
	 */
	public static Point createPointByWKT(String pointWkt){
		WKTReader reader = new WKTReader(geometryFactory);
		Point point = null;
		try {
			point = (Point) reader.read(pointWkt);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return point;
	}
	public static LineString createLineString(Coordinate[] coords){
		LineString lineString = geometryFactory.createLineString(coords);
		return lineString;
	}
	/**
	 * 
	 * @param lineStringWkt "LINESTRING(0 0, 2 0)"
	 * @return
	 */
	public static LineString createLineStringByWKT(String lineStringWkt){
		WKTReader reader = new WKTReader(geometryFactory);
		LineString lineString = null;
		try {
			lineString = (LineString) reader.read(lineStringWkt);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return lineString;
	}
	public static MultiLineString createMultiLineString(LineString[] lineStrings){
		MultiLineString multiLineString = geometryFactory.createMultiLineString(lineStrings);
		return multiLineString;
	}
	/**
	 * MULTILINESTRING((0 0, 2 0),(1 1,2 2))
	 * @param lineStringsWkt
	 * @return
	 */
	public static MultiLineString createMultiLineStringByWKT(String lineStringsWkt){
		WKTReader reader = new WKTReader(geometryFactory);
		MultiLineString multiLineString = null;
		try {
			multiLineString = (MultiLineString) reader.read(lineStringsWkt);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return multiLineString;
	}
	/**
	 * 
	 * @param polygonWkt POLYGON((20 10, 30 0, 40 10, 30 20, 20 10))
	 * @return
	 */
	public static Polygon createPolygonByWKT(String polygonWkt){
		WKTReader reader = new WKTReader(geometryFactory);
		Polygon polygon = null;
		try {
			polygon = (Polygon) reader.read(polygonWkt);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return polygon;
	}
	/**
	 * 
	 * @param multiPolygonWkt MULTIPOLYGON(((40 10, 30 0, 40 10, 30 20, 40 10),(30 10, 30 0, 40 10, 30 20, 30 10)))
	 * @return
	 */
	public static MultiPolygon createMultiPolygonByWKT(String multiPolygonWkt) {
		WKTReader reader = new WKTReader(geometryFactory);
		MultiPolygon multiPolygon = null;
		try {
			multiPolygon = (MultiPolygon) reader.read("");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return multiPolygon;
	}
//	/**
//	 * 格式化面
//	 * @param polygon
//	 * @return
//	 */
//	public static String formatPolygon(String polygon){
//		StringBuffer result = new StringBuffer();
//		StringBuffer tempBuf = new StringBuffer();
//		String coordinates[] = polygon.replace("MULTIPOLYGON", "").replace("(((", "").replace(")))", "").split(",");
//		for (String coordinate : coordinates) {
//			coordinate = coordinate.trim();
//			String lonlat[] = coordinate.split(" ");
//			tempBuf.append("[")
//				.append(lonlat[0])
//				.append(",")
//				.append(lonlat[1])
//				.append("],");
//		}
//		return result.append("[")
//				.append(StringUtil.deleteLastStr(tempBuf.toString()))
//				.append("]").toString();
//	}
	/**
	 * 格式化点
	 * @param point
	 * @return
	 */
	public static String formatPoint(String point){
		String coordinates[] = point.replace("POINT", "")
									.replace("(", "")
									.replace(")", "")
									.trim()
									.split(" ");
		return "[" + coordinates[0] + "," + coordinates[1] + "]";
	}
//	/**
//	 * 格式化线
//	 * @param polyline
//	 * @return
//	 */
//	public static String formatPolyline(String polyline){
//		StringBuffer result = new StringBuffer();
//		StringBuffer tempBuf = new StringBuffer();
//		String coordinates[] = polyline.replace("MULTILINESTRING", "").replace("((", "").replace("))", "").split(",");
//		for (String coordinate : coordinates) {
//			coordinate = coordinate.trim();
//			String lonlat[] = coordinate.split(" ");
//			tempBuf.append("[")
//				.append(lonlat[0])
//				.append(",")
//				.append(lonlat[1])
//				.append("],");
//		}
//		return result.append("[")
//				.append(StringUtil.deleteLastStr(tempBuf.toString()))
//				.append("]").toString();
//	}
	/**
	 * 计算两点之间的距离
	 * @param p1 线段起始端点  [113.3,33.2]
	 * @param p2 线段结束端点   [114.5,33.8]
	 * @return 距离(米)
	 */
	public static double getDistance(double[]p1,double[] p2) {
        double radLat1 = p1[1] * Math.PI / 180;
        double radLat2 = p2[1] * Math.PI / 180;
        double a = radLat1 - radLat2;
        double b = p1[0] * Math.PI / 180 - p2[0] * Math.PI / 180;
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1)
                * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * 6378137.0;
        s = Math.round(s * 10000d) / 10000d;
        return s;
    }
	/**
	 * 计算几何线段的距离
	 * @param multiLineString
	 * @return 距离(米)
	 */
	public static double getLineStringDistance(double[] multiLineString){
		if(multiLineString.length < 4){
			return 0d;
		}else{
			double distance = 0;
			for(int i = 0; (i+2) < multiLineString.length; i= i+2){
				double[] start = {multiLineString[i], multiLineString[i+1]};
				double[] end = {multiLineString[i+2], multiLineString[i+3]};	
				distance += getDistance(start, end);
			}
			return distance;
		}
	}
	/**
	 * 求点到直线的垂直交点坐标
	 * @param startPoint 线起始点坐标
	 * @param endPoint 先结束点坐标
	 * @param point 点
	 * @return 垂直交点坐标
	 */
	public static double[] getCrossPoint(double[] startPoint, double[] endPoint, double[] point){
		double  A = (startPoint[1] - endPoint[1])/(startPoint[0] - endPoint[0]);
		double B = startPoint[1] - A * startPoint[0];
		double M = point[0] + A * point[1];
		double p0 = (M- A*B)/(A * A +1);
		double p1 = A * p0 + B;
		double[] crossPoint = {p0, p1};
		return crossPoint;
	}
	/**
	 * 
	 * @param x 圆心经度
	 * @param y 圆心纬度
	 * @param r 半径(单位米)
	 * @param sides 圆上点个数，最少20个点
	 * @return 
	 */
	public static Polygon createCircle(double x, double y, double r, int sides){
		if(sides < 20){
			sides = 20;
		}
	    Coordinate coords[] = new Coordinate[sides+1];
	    for( int i = 0; i < sides; i++){
	        double angle = ((double) i / (double) sides) * Math.PI * 2.0;
	        double dx = Math.cos( angle ) * r * 0.000010444641; //0.000010444641 一米经度误差
	        double dy = Math.sin( angle ) * r * 0.000010444641;//0.008991241 一米纬度误差。如此处采用纬度误差，则为椭圆，圆上各点到圆心的真实距离为半径值
	        coords[i] = new Coordinate( x + dx, y + dy );
	    }
	    coords[sides] = coords[0];
	    LinearRing ring = geometryFactory.createLinearRing(coords);
	    return geometryFactory.createPolygon(ring, null );
	}
	/**
	 * 
	 * @param x 圆心经度
	 * @param y 圆心纬度
	 * @param r 半径(单位米)
	 * @return
	 */
	public static Polygon createCircle(double x, double y, double r){
		return createCircle(x, y, r, 64);
	}
	public static void main(String[] args) {
		String polygon = GisUtils.createCircle(104.060815013281,30.598310651617, 2.25, 20).toText();
		System.out.println(polygon);
		MultiPolygon regionLocation = null;
		Point point = GisUtils.createPoint(12.3, 22.3);
		regionLocation.contains(point);
	}
}
