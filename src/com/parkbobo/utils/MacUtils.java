package com.parkbobo.utils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;


public class MacUtils {
	private static MacUtils macUtils;
	private MacUtils(){}
	public synchronized static MacUtils getInstance(){
		if(macUtils==null){
			macUtils = new MacUtils();
		}
		return macUtils;
	}
	/**
	 * 不带冒号的MAC地址
	 * @return
	 */
	public long noColonMacToLong(String macStr)
	{
		macStr = macStr.toUpperCase();
		String macStrs[] = new String[6] ;
		for(int i = 0 ; i < 6; i++)
		{
			macStrs[i] = macStr.substring(i * 2, (i * 2) + 2 );
		}
		byte macs[] = new byte[6];
		for(int i=0;i<6;i++)
		{
			int m = Integer.parseInt(macStrs[i], 16);
			macs[i]=(byte) (m&0xff);
		}
		ByteBuffer bb=ByteBuffer.allocate(8);
		bb.order(ByteOrder.nativeOrder());
		bb.position(0);
		bb.put(macs, 0, 6);
		bb.put((byte) 0);
		bb.put((byte) 0);
		bb.position(0);
		return bb.getLong();
	}
	public String longToMac(long mac)
	{
		StringBuffer macBuf = new StringBuffer();
		byte macs[] = new byte[6];
		macs = macToBytes(mac);
		for (byte b : macs) {
			int s = b & 0xff;
			macBuf.append(String.format("%02X", s) + ":");
		}
		return macBuf.substring(0, macBuf.length() -1);
	}
	public long macToLong(String macStr)
	{
		
		String macStrs[] = macStr.toUpperCase().split(":");
		byte macs[] = new byte[6];
		for(int i=0;i<6;i++)
		{
			int m = Integer.parseInt(macStrs[i], 16);
			macs[i]=(byte) (m&0xff);
		}
		ByteBuffer bb=ByteBuffer.allocate(8);
		bb.order(ByteOrder.nativeOrder());
		bb.position(0);
		bb.put(macs, 0, 6);
		bb.put((byte) 0);
		bb.put((byte) 0);
		bb.position(0);
		return bb.getLong();
	}
	private static byte[] macToBytes(long mac)
	{
		ByteBuffer bb=ByteBuffer.allocate(8);
		bb.order(ByteOrder.nativeOrder());
		bb.position(0);
		bb.putLong(mac);
		byte ret[]=new byte[6];
		bb.position(0);
		bb.get(ret);
		return ret;
	}
	public static void main(String[] args) {
		System.out.println(MacUtils.getInstance().macToLong("00:1B:35:0E:CA:C4"));
	}
}
