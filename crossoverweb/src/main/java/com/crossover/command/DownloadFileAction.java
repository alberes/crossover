package com.crossover.command;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

import javax.persistence.EntityManagerFactory;

import org.apache.log4j.Logger;

import com.crossover.bo.CrossoverBO;
import com.crossover.entity.Artifact;
import com.crossover.entity.User;

public class DownloadFileAction extends ActionCommand {
	
	Logger logger = Logger.getLogger(DownloadFileAction.class);

	@Override
	public void execute() {
		
		User user = (User)this.getRequest().getSession().getAttribute("user");
		if(user == null){
			this.getRequest().setAttribute("messagesError", "Access denied.");
			logger.warn("User tried access without log in");
			this.forward("/index.jsp");
		}

		EntityManagerFactory emf = (EntityManagerFactory)this.getRequest().getServletContext().getAttribute("emf");
		CrossoverBO crossoverBO = new CrossoverBO(emf);
		Artifact artifact = null;
		
		try{
			String id = this.getRequest().getParameter("id");
			artifact = crossoverBO.getArtifact(Long.valueOf(id));
			this.getResponse().setContentType("Application/octet-stream");
			this.getResponse().setHeader("Content-Disposition",
		                     "attachment;filename=" + artifact.getFile());
			int read=0;
			InputStream is = new FileInputStream(artifact.getPath() + File.separator + artifact.getFile());
			byte[] bytes = new byte[1024];
			OutputStream os = this.getResponse().getOutputStream();
			
			logger.info("Uploaded file " + artifact.getPath() + File.separator + artifact.getFile() + " " + new Date());
			
			while((read = is.read(bytes))!= -1){
				os.write(bytes, 0, read);
			}
			os.flush();
			os.close();
		}catch(Exception e){
			e.printStackTrace();
			logger.fatal(e.getMessage());
		}
		this.forward("/index.jsp");
	}

}
