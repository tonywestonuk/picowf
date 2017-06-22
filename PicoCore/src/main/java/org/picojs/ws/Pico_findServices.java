package org.picojs.ws;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentSkipListMap;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.picojs.PacketReceiver;
import org.picojs.nanojson.JsonStringWriter;
import org.picojs.nanojson.JsonWriter;


@WebServlet("/WEB-INF/pico/findServices")
public class Pico_findServices extends HttpServlet{

private PacketReceiver pr;
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		pr=(PacketReceiver) config.getServletContext().getAttribute("packetReceiver");
	}
	
	
	@Override
		protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		JsonStringWriter jbld= JsonWriter.string();
		
		jbld.object().array("services");
		
		Map<String, Map<String, String>> map = pr.getPacketCache();	
		
		HashSet<String> serviceTypes = new HashSet<>();
		
		
		for (Entry<String, Map<String, String>> e:map.entrySet()){
			String serviceType=e.getValue().get("serviceType");
			
			if (serviceTypes.contains(serviceType)) continue;
			serviceTypes.add(serviceType);
			
			
			jbld.object();
			for (Entry<String,String> ee:e.getValue().entrySet()){
				jbld.value(ee.getKey(), ee.getValue());
			}

			jbld.end();
		}
		
		jbld.end();
		jbld.end();
		
		resp.setContentType("application/json");

		resp.getWriter().println(jbld.done());
		}
	
}
