package org.picojs;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.HttpURLConnection;
import java.net.Inet4Address;
import java.net.MalformedURLException;
import java.net.MulticastSocket;
import java.net.URL;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.picojs.nanojson.JsonArray;
import org.picojs.nanojson.JsonObject;
import org.picojs.nanojson.JsonParser;
import org.picojs.nanojson.JsonStringWriter;
import org.picojs.nanojson.JsonWriter;

@WebListener
public class RemoteServiceRetriever implements ServletContextListener, Runnable{
	
	private ServletContext servletContext;
	private ScheduledExecutorService executor;


	private MulticastSocket mcastSocket;



	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		if (executor!=null){
		try {
			executor.shutdown();
			executor.awaitTermination(10, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mcastSocket.close();
		}
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		servletContext=arg0.getServletContext();
		
		if (servletContext.getInitParameter("pico_remoteServiceUrl")!=null){
			try {
				mcastSocket = new MulticastSocket(MulticastEnabler.port);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			executor=Executors.newSingleThreadScheduledExecutor();
			executor.scheduleWithFixedDelay(this, 1, 10, TimeUnit.SECONDS);
		}
	}
	

	@Override
	public void run() {
		try {
			URL url = new URL(servletContext.getInitParameter("pico_remoteServiceUrl")+"/findServices.pico");
			HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
			
			
			JsonObject jo = JsonParser.object().from(urlConnection.getInputStream());
			JsonArray ja= jo.getArray("services");
			Iterator it =  ja.iterator();
			while (it.hasNext()) {
				JsonObject object = (JsonObject) it.next();
				
				JsonStringWriter jsw = JsonWriter.string();
				jsw.value(object);
				
				byte[] bytes= jsw.done().getBytes("utf-8");
				
				DatagramPacket pkt = new DatagramPacket(bytes, bytes.length);
				pkt.setAddress(MulticastEnabler.mCastAddress);
				pkt.setPort(MulticastEnabler.port);
				mcastSocket.send(pkt);					
			}

			
			
		} catch (Exception e) {
			
		}
	}

	

}
