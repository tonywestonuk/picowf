package org.picojs.ws;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
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

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RunWith(PowerMockRunner.class)
public class Pico_info_Test {

	@Mock
	HttpServletRequest rqs;
	
	@Mock
	HttpServletResponse rsp;
	
	@Mock
	ServletContext ctx;
	
	@Mock
	PrintWriter outputWriter;
	
	@Captor
	ArgumentCaptor<String> jsonOutput;
	
	@Before
	public void init() throws IOException{
		when(rqs.getServletContext()).thenReturn(ctx);	
		HashMap<String, String> initParameters = new HashMap<>();
		initParameters.put("pico_abc", "val_abc");
		initParameters.put("pico_serverUrl", "myServer");
		initParameters.put("notPicoParam", "Notvalid");


		
		
		when(ctx.getInitParameterNames()).thenReturn(new Vector(initParameters.keySet()).elements());
		
		for (Entry<String, String> parm:initParameters.entrySet()){
			when(ctx.getInitParameter(parm.getKey())).thenReturn(parm.getValue());

		}
		
		
		when(rqs.getContextPath()).thenReturn("/pants");
		
		when(rsp.getWriter()).thenReturn(outputWriter);
		
		
	}
	
	@Test
	public void testResponseIsJson() throws Exception {
		Pico_info picoInfo = new Pico_info();
		picoInfo.doGet(rqs, rsp);
		verify(rsp).setContentType("application/json");
		
	}
	
	
	@Test
	public void testJsonOutput() throws Exception {
		Pico_info picoInfo = new Pico_info();
		picoInfo.doGet(rqs, rsp);
		
		verify(outputWriter).println(jsonOutput.capture());
		
		JsonObject parsed = JsonParser.object().from(jsonOutput.getValue());
		
		assertEquals("myServer/pants", parsed.getString("pico_serverUrl"));
		assertEquals("val_abc", parsed.getString("pico_abc"));
	}
	
	@Test
	public void testNotPicoParmsAreNotOutput() throws Exception {
		Pico_info picoInfo = new Pico_info();
		picoInfo.doGet(rqs, rsp);
		
		verify(outputWriter).println(jsonOutput.capture());
		
		JsonObject parsed = JsonParser.object().from(jsonOutput.getValue());
		
		assertNull(parsed.getString("notPicoParam"));
	}
}
