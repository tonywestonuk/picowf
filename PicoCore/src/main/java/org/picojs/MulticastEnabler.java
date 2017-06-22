package org.picojs;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class MulticastEnabler {

	public static final InetAddress mCastAddress =init();
	public static final int port = 9223;

	
	private static InetAddress init(){

		try {
			
			
			
		
			return  Inet4Address.getByAddress(new byte[]{(byte)224,1,2,(byte)251});
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} finally{
		}
	}
	
	
	
	
	
}
