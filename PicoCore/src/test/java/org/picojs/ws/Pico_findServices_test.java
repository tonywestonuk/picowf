package org.picojs.ws;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.picojs.PacketReceiver;
import org.picojs.nanojson.JsonObject;
import org.picojs.nanojson.JsonParser;
import org.powermock.modules.junit4.PowerMockRunner;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;
import java.util.concurrent.ConcurrentSkipListMap;

import javax.json.JsonArray;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RunWith(PowerMockRunner.class)
public class Pico_findServices_test {

	@Mock
	HttpServletRequest rqs;
	
	@Mock
	HttpServletResponse rsp;
	
	@Mock
	ServletContext ctx;
	
	@Mock
	PrintWriter outputWriter;
	
	@Mock
	ServletConfig sc;
	
	@Mock
	ServletContext sctx;
	
	@Mock
	PacketReceiver packetReceiver;
	
	@Captor
	ArgumentCaptor<String> jsonOutput;
	
	Pico_findServices pfs;
	
	@Before
	public void init() throws IOException, ServletException{
			pfs = new Pico_findServices();
			when(sc.getServletContext()).thenReturn(sctx);
			when(sctx.getAttribute("packetReceiver")).thenReturn(packetReceiver);
			pfs.init(sc);
			
			ConcurrentSkipListMap<String, Map<String, String>> map = new ConcurrentSkipListMap<>();
			
			HashMap srv1 = new HashMap<>();
			srv1.put("srv1_a", "a");
			srv1.put("srv1_b", "b");
			map.put("srv1", srv1);
			
			srv1 = new HashMap<>();
			srv1.put("srv2_a", "a");
			srv1.put("srv2_b", "b");
			map.put("srv2", srv1);
			
			
			when(packetReceiver.getPacketCache()).thenReturn(map);
			when(rsp.getWriter()).thenReturn(outputWriter);
	}
	
	@Test
	public void testResponseIsJson() throws Exception {
		pfs.doGet(rqs, rsp);
		verify(outputWriter).println(jsonOutput.capture());
		
		JsonObject jobj= JsonParser.object().from(jsonOutput.getValue());
		org.picojs.nanojson.JsonArray ja= jobj.getArray("services");
		String s=ja.getObject(0).getString("srv1_a");
		assertEquals("a", s);
	}
	
	
}
