package org.picojs.ws;

import java.io.IOException;
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

@WebServlet("/WEB-INF/pico/redirect")
public class Pico_redirect extends HttpServlet{

private PacketReceiver pr;
	@Override
	public void init(ServletConfig config) throws ServletException {		
		pr=(PacketReceiver) config.getServletContext().getAttribute("packetReceiver");
	}
	
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String serviceType=req.getAttribute("javax.servlet.forward.request_uri").toString();
		
		serviceType=serviceType.substring(serviceType.lastIndexOf("/")+1,serviceType.length()-14);
		
		ConcurrentSkipListMap<String, Map<String, String>> map = pr.getPacketCache();	
		for (Entry<String, Map<String, String>> e:map.entrySet()){
			if (serviceType.equals(e.getValue().get("serviceType"))){
				String rootUrl=e.getValue().get("rootUrl");
				if (req.getParameter("path")!=null){
					rootUrl+="/"+req.getParameter("path");
				}
				
				resp.sendRedirect(rootUrl);
				return;
			}	
		}
	}
}
