package com.crossover.web.listener;

import java.util.List;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.crossover.bo.CrossoverBO;
import com.crossover.entity.User;

/**
 * Application Lifecycle Listener implementation class CrossoverContextListener
 *
 */
@WebListener
public class CrossoverContextListener implements ServletContextListener {

    /**
     * Default constructor. 
     */
    public CrossoverContextListener() {
        // TODO Auto-generated constructor stub
    }

    /**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent servletContextEvent)  { 
    	com.objectdb.Enhancer.enhance("com.crossover.entity.*");
    	EntityManagerFactory emf =
    			Persistence.createEntityManagerFactory("$objectdb/db/guest.odb");
    	servletContextEvent.getServletContext().setAttribute("emf", emf);
    	CrossoverBO crossoverBO = new CrossoverBO(emf);
    	//crossoverBO.deleteAllUsers();
    	List<User> users = crossoverBO.getAllUser();
	    	if(users.isEmpty()){
	    	User user = null;
	    	for(int i = 1; i <= 10; i++){
	    		user = new User("user" + i, "abc" + i);
	    		crossoverBO.saveUser(user);
	    	}
    	}
    	crossoverBO.close();
    }
    
	/**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent servletContextEvent)  { 
    	EntityManagerFactory emf =
                (EntityManagerFactory)servletContextEvent.getServletContext().getAttribute("emf");
            emf.close();
    }

	
}
