package com.crossover.command;

import java.io.IOException;
import java.util.Properties;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class ActionCommand {

	private HttpServletRequest request;
	
	private HttpServletResponse response;
	
	private ServletContext context;
	
	private Properties resource;
	
	public abstract void execute();
	
	public void forward(String forwardResponse){
		try {
			RequestDispatcher rd = this.getRequest().getRequestDispatcher(forwardResponse);
			rd.forward(this.getRequest(), this.getResponse());
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

	public ServletContext getContext() {
		return context;
	}

	public void setContext(ServletContext context) {
		this.context = context;
	}

	public Properties getResource() {
		return resource;
	}

	public void setResource(Properties resource) {
		this.resource = resource;
	}

}
