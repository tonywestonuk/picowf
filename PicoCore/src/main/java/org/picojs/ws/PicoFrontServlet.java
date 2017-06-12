package org.picojs.ws;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("*.pico")
public class PicoFrontServlet extends HttpServlet{

	
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String command = req.getRequestURI();
		command=command.substring(command.lastIndexOf("/")+1,command.length()-5);
		command=command.substring(command.lastIndexOf(".")+1);
		req.getRequestDispatcher("/WEB-INF/pico/"+command).forward(req, resp);	
	}
}
