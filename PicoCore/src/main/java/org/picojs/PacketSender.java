package org.picojs;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.Inet4Address;
import java.net.MulticastSocket;
import java.util.Enumeration;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.picojs.nanojson.JsonStringWriter;
import org.picojs.nanojson.JsonWriter;

@WebListener
public class PacketSender implements ServletContextListener{


	private boolean shutdownFlag=false;

	private ServletContext servletContext;
	private final UUID uniqueKey;
	private String serverUrl="http://localhost:8080";

	private MulticastSocket mcastSocket;
	private ScheduledExecutorService senderExecutor;
	{
		uniqueKey = UUID.randomUUID();
		try {
			mcastSocket = new MulticastSocket(MulticastEnabler.port);
		} catch (IOException err) {
			throw new RuntimeException(err);
		}
	
	}

	

	
	public void sendPacket() {
		try {		
			Enumeration<String> en =  servletContext.getInitParameterNames();
			JsonStringWriter jbld= JsonWriter.string();
			jbld.object();
			jbld.value("uuid", uniqueKey.toString());
			
			if (shutdownFlag==true){
				jbld.value("shutdown", "true");
			} else {
			
			
			jbld.value("rootUrl", serverUrl+ servletContext.getContextPath());			
			while (en.hasMoreElements()) {
				String parm = (String) en.nextElement();
				if (parm.startsWith("pico_"))
					jbld.value(parm.substring(5), servletContext.getInitParameter(parm));			
				}
			}
			
			jbld.end();
			byte[] bytes= jbld.done().getBytes("utf-8");
			
			DatagramPacket pkt = new DatagramPacket(bytes, bytes.length);
			pkt.setAddress(MulticastEnabler.mCastAddress);
			pkt.setPort(MulticastEnabler.port);
			mcastSocket.send(pkt);

		} catch (Exception err) {
			err.printStackTrace();
		}
	}




	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		
		senderExecutor.shutdown();
		try {
			senderExecutor.awaitTermination(10, TimeUnit.SECONDS);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		// Send shutdown packets.
		shutdownFlag=true;
		for (int i = 0; i < 3; i++) {
			sendPacket();

			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		//close the socket.
		mcastSocket.close();

	}


	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		servletContext=arg0.getServletContext();
		servletContext.setAttribute("packetSender", this);	

		if (servletContext.getInitParameter("serverUrl")!=null){
			serverUrl=servletContext.getInitParameter("serverUrl");
		}
			
	
		if (servletContext.getInitParameter("pico_serviceGroup") == null) {
			try {
				mcastSocket.setTimeToLive(0);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		senderExecutor=Executors.newSingleThreadScheduledExecutor();
		
		
		senderExecutor.scheduleAtFixedRate(			
				()->{sendPacket();}, 1, 10, TimeUnit.SECONDS);
		
	}

}