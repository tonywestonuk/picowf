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
	
	
	/**
	 * Pico front servlet takes any request with .pico at the end.
	 * Its job is to forward to another servlet based on the command specified before
	 * the .pico. This should happen regardless of the path it is called.
	 * 
	 * This test verifies that the front servlet correctly looks up the request
	 * dispatcher that corrolates with the command specified before .pico
	 * 
	 * 
	 * 
	 * @throws ServletException
	 * @throws IOException
	 */
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
