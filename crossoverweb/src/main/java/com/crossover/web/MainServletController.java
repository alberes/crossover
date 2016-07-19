package com.crossover.web;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.crossover.command.ActionCommand;

/**
 * Servlet implementation class MainServletController
 */
@WebServlet(
		urlPatterns = { "/controller" }, 
		initParams = { 
				@WebInitParam(name = "mapping", value = "/WEB-INF/mappings.properties"), 
				@WebInitParam(name = "log4j-properties-location", value = "WEB-INF/log4j.properties")
		})
public class MainServletController extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
    
	Logger logger = Logger.getLogger(MainServletController.class);
	
	private Properties mappings;
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		InputStream is = null;
		try {
			String location = config.getInitParameter("mapping");
			is = this.getServletContext().getResourceAsStream(location);
			mappings = new Properties();
			mappings.load(is);
			configLog(config);
			logger.info("Controller load with success");
		} catch (IOException e) {
			e.printStackTrace();
			logger.fatal(e.getMessage());
		}finally{
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MainServletController() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String command = request.getParameter("cmd");
		if(command == null){
			command = "entryAction";
		}
		String actionClass = mappings.getProperty(command);
		ActionCommand action = null;
		
		try {
			action = (ActionCommand)Class.forName(actionClass).newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		action.setRequest(request);
		action.setResponse(response);
		action.setContext(this.getServletContext());
		action.setResource(this.mappings);
		action.execute();
	}
	
	public void configLog(ServletConfig config){
		System.out.println("Log4JInitServlet is initializing log4j");
		String log4jLocation = config.getInitParameter("log4j-properties-location");

		ServletContext sc = config.getServletContext();

		if (log4jLocation == null) {
			System.err.println("*** No log4j-properties-location init param, so initializing log4j with BasicConfigurator");
			BasicConfigurator.configure();
		} else {
			String webAppPath = sc.getRealPath("/");
			String log4jProp = webAppPath + log4jLocation;
			File fileLog = new File(log4jProp);
			if (fileLog.exists()) {
				System.out.println("Initializing log4j with: " + log4jProp);
				PropertyConfigurator.configure(log4jProp);
			} else {
				System.err.println("*** " + log4jProp + " file not found, so initializing log4j with BasicConfigurator");
				BasicConfigurator.configure();
			}
		}
	}


}
