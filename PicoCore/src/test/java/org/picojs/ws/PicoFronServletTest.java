package org.picojs.ws;

import static org.junit.Assert.*;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.modules.junit4.PowerMockRunner;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
public class PicoFronServletTest {

	@Mock
	HttpServletRequest rqs;
	
	@Mock
	HttpServletResponse rsp;
	
	@Mock
	RequestDispatcher rd;
	
	@Captor
	ArgumentCaptor<String> requestDispatcherArg;
	
	
	@Test
	public void testService() throws ServletException, IOException {
		PicoFrontServlet pfs = new PicoFrontServlet();
		
		when(rqs.getRequestURI()).thenReturn("bull/crap/testCommand.pico");
		
		
		
		
		when(rqs.getRequestDispatcher(anyString())).thenReturn(rd);
		pfs.service(rqs,rsp);
		verify(rqs).getRequestDispatcher(requestDispatcherArg.capture());
		
		assertEquals("/WEB-INF/pico/testCommand",requestDispatcherArg.getValue());
	}

}
