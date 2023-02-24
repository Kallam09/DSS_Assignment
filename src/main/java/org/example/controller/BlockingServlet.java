package org.example.controller;


import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;


@WebServlet(name = "BlockingServlet", value = "BlockingServlet")
public class BlockingServlet extends HttpServlet {


	private static final long serialVersionUID = 1L;

	  @Override
	  protected void doGet(HttpServletRequest request, 
	      HttpServletResponse response) throws ServletException, IOException {

	    final long startTime = System.nanoTime();
	    try {
	      Thread.sleep(2000);
	    } catch (InterruptedException e) {
	      throw new RuntimeException(e);
	    }
	    
	    response.setContentType("application/json");
	    response.setStatus(HttpServletResponse.SC_OK);
	    
	    PrintWriter out = response.getWriter();
	    out.print("BlockingServlet Work completed. Time elapsed: " + (System.nanoTime() - startTime));
	    out.flush();

	  }
	
}