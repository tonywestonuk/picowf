package org.picojs;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class MulticastEnabler {

	public static InetAddress mCastAddress =init();
	public static final int port = 9223;

	
	private static InetAddress init(){
		try {
			Runtime.getRuntime().exec("route add -net 224.0.0.0/24 -interface lo0".split(" "));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			return  Inet4Address.getByAddress(new byte[]{(byte)224,1,2,(byte)251});
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	
	
	
	
}
