package org.picojs;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.picojs.nanojson.JsonObject;
import org.picojs.nanojson.JsonParser;
import org.picojs.nanojson.JsonParserException;

@WebListener
public class PacketReceiver implements ServletContextListener {

	private MulticastSocket mcastSocket;
	
	private String picoServiceGroup;
	private boolean debugMode=false;
	
	

	public ConcurrentSkipListMap<String, Map<String, String>> getPacketCache() {
		return packetCache;
	}

	
	private ConcurrentSkipListMap<String, Map<String,String>> packetCache = new ConcurrentSkipListMap<>();
	private ScheduledExecutorService timeOutExecutor;
	
	

	public void receivePacket() {
		byte[] buf = new byte[1500];
		DatagramPacket recv = new DatagramPacket(buf, buf.length);

		try {	
				mcastSocket.receive(recv); // if this times out, the loop exits.
						
				JsonObject jp=null;
				try {
					if (debugMode)
						System.out.println(new String(recv.getData(), 0, recv.getLength()));
					
					jp = JsonParser.object().from(new ByteArrayInputStream(recv.getData(), 0, recv.getLength()));
				} catch (JsonParserException e) {
					throw new RuntimeException(e);
				}
			
				ConcurrentHashMap<String, String> packet = new ConcurrentHashMap<>();
				
				for (String key:jp.keySet()){		
						packet.put(key, jp.getString(key));
				}
				
				packet.put("lastSeen", Long.toString(System.currentTimeMillis()));
				String packetServiceGroup = packet.get("serviceGroup");
				
				if ((packetServiceGroup==null && picoServiceGroup==null)
					|| picoServiceGroup.equals(packet.get("serviceGroup"))){	
					if ("true".equals(packet.get("shutdown"))){
						packetCache.remove(packet.get("uuid"));
					} else{
						packetCache.put(packet.get("uuid"), packet);
					}
				}
			
				

		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SocketTimeoutException e) {
			//**** at this point.... the while true loop ends...
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	// Removed old services who've we've not heard from in 30 seconds.
	public void timeOut(){
		Long timeOut=System.currentTimeMillis()-30000; // 30 seconds time out.
		Iterator<Entry<String, Map<String,String>>> it= packetCache.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String,Map<String,String>> entry=it.next();
			long lastSeen = Long.parseLong(entry.getValue().get("lastSeen"));
			if (lastSeen<timeOut){
				System.out.println("removed "+entry.getValue().get("uuid"));
				it.remove();
		}
	}
	}



	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		timeOutExecutor.shutdown();
		try {
			timeOutExecutor.awaitTermination(10,TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mcastSocket.close();
		
	}



	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		
		arg0.getServletContext().setAttribute("packetReceiver", this);	
		ServletContext servletContext = arg0.getServletContext();
		
		debugMode=servletContext.getInitParameter("pico_debug")!=null;
		
		this.picoServiceGroup = servletContext.getInitParameter("pico_serviceGroup");
		
		
		try {
			mcastSocket = new MulticastSocket(MulticastEnabler.port);
			//mcastSocket.setInterface(InetAddress.getLocalHost());
			System.out.println(MulticastEnabler.mCastAddress);
			mcastSocket.joinGroup(MulticastEnabler.mCastAddress);
			mcastSocket.setSoTimeout(100); 
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		timeOutExecutor = Executors.newSingleThreadScheduledExecutor();
		timeOutExecutor.scheduleWithFixedDelay(()->{timeOut();}, 1, 1, TimeUnit.SECONDS);
		timeOutExecutor.scheduleWithFixedDelay(()->{receivePacket();}, 1, 1, TimeUnit.MILLISECONDS);

		
	}

}
