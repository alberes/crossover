package com.crossover.command;

import javax.persistence.EntityManagerFactory;

import org.apache.log4j.Logger;

import com.crossover.bo.CrossoverBO;
import com.crossover.entity.User;

public class LoginAction extends ActionCommand {

	Logger logger = Logger.getLogger(LoginAction.class);
	
	@Override
	public void execute() {
		String name = this.getRequest().getParameter("name");
		String password = this.getRequest().getParameter("password");
		
		EntityManagerFactory emf = (EntityManagerFactory)this.getRequest().getServletContext().getAttribute("emf");
		CrossoverBO crossoverBO = new CrossoverBO(emf);
		try {
			User userVO = crossoverBO.getUser(name, password);
			if(userVO != null){
				this.getRequest().getSession().setAttribute("user", userVO);
				logger.info("User authenticated ID" + userVO.getId()  + " Name " + userVO.getName());
				this.forward("/WEB-INF/jsp/upload.jsp");
				logger.info("Login with success");
			}else{
				logger.info("User not found.");
				this.getRequest().setAttribute("messagesError", "User not found.");
				this.forward("/index.jsp");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Exception" + e.getMessage());
			//this.getRequest().setAttribute("messagesError", e.getMessage());
			this.getRequest().setAttribute("messagesError", "User not found.");
			this.forward("/index.jsp");
		}

	}

}
