package org.picojs.ws;

import java.io.IOException;
import java.util.Enumeration;
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


@WebServlet("/WEB-INF/pico/info")
public class Pico_info extends HttpServlet{

	
	@Override
		protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		
		JsonStringWriter jbld= JsonWriter.string();
		jbld.object();		
		
	
		Enumeration<String> en =  req.getServletContext().getInitParameterNames();
		while (en.hasMoreElements()) {	
			String parm = (String) en.nextElement();
			if (parm.startsWith("pico_")){
				jbld.value(parm, req.getServletContext().getInitParameter(parm));
			}
		}
		
		String serverURL = req.getServletContext().getInitParameter("serverUrl");
		
		jbld.value("pico_serverUrl",serverURL+req.getContextPath());
		
		
		
	
		jbld.end();
		
		resp.setContentType("application/json");

		resp.getWriter().println(jbld.done());
		}
	
}
