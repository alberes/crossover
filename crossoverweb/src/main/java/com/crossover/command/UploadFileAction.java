package com.crossover.command;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManagerFactory;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;

import com.compile.service.CompileInterface;
import com.compile.service.CompilerService;
import com.crossover.bo.CrossoverBO;
import com.crossover.entity.Artifact;
import com.crossover.entity.User;
import com.crossover.util.PropertiesUtil;

public class UploadFileAction extends ActionCommand {

	Logger logger = Logger.getLogger(DownloadFileAction.class);
	
	@Override
	public void execute() {
		
		//Authenticated user
		User user = (User)this.getRequest().getSession().getAttribute("user");
		if(user == null){
			this.getRequest().setAttribute("messagesError", "Access denied.");
			logger.warn("User tried access without log in");
			this.forward("/index.jsp");
		}
		
		//Find path file config
		//We can see in /crossoverweb/src/main/webapp/WEB-INF/mappings.properties
		String os = System.getProperty("os.name").toLowerCase();
		String workPath = "";
		if(os.indexOf("win") >= 0){
			workPath = this.getResource().getProperty("work.config." + "windows");
			os = "windows";
		}else{
			workPath = this.getResource().getProperty("work.config." + "linux");
			os = "";
		}

		//File config
		PropertiesUtil prop = new PropertiesUtil(workPath + File.separator + "config", "config" + os + ".properties");
		
		//Persist object
		EntityManagerFactory emf = (EntityManagerFactory)this.getRequest().getServletContext().getAttribute("emf");
		CrossoverBO crossoverBO = new CrossoverBO(emf);
		Artifact artifact = new Artifact();
		artifact.setStartDate(new Date());
		
		//Save file
		DiskFileItemFactory factory = new DiskFileItemFactory();
		String path = prop.getValue("upload.file");
		File repository = new File(path + File.separator + "tmp");
		factory.setRepository(repository);		
		// Create a new file upload handler
		ServletFileUpload upload = new ServletFileUpload(factory);		
		// Parse the request
		List<FileItem> items;
		
		//Folder and file
		String fileName = null;
		String filePath = null;
		String filePathWork = null;
		File storeFile = null;
		
		//Call service
		CompilerService service = new CompilerService();
		CompileInterface compiler = service.getCompilerPort();
		//Set endpoint
		javax.xml.ws.BindingProvider bp = (javax.xml.ws.BindingProvider) compiler;
		Map<String, Object> c = bp.getRequestContext();
		c.put(javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY, prop.getValue("url.service.compile"));
		com.compile.service.CompileResult result = null;
		
		try {
			items = upload.parseRequest(this.getRequest());
			for(FileItem item : items){
				fileName = new File(item.getName()).getName();
				filePathWork = path + File.separator + "files" + File.separator + System.currentTimeMillis(); 
				filePath = filePathWork + File.separator + fileName;
				storeFile = new File(filePath);
				storeFile.getParentFile().mkdirs();
				// saves the file on disk
				item.write(storeFile);
				logger.info("Write file " + storeFile.getAbsolutePath());
				logger.info("Call service " + new Date());
				result = compiler.execute(filePathWork, fileName);
				logger.info("Finish call service " + new Date());
				logger.info("Status result " + result.getStatus().getCode() + result.getStatus().getMessage());
				artifact.setFinishDate(new Date());
				artifact.setProjectType(result.getProjectType());
				artifact.setPath(result.getPath());
				artifact.setFile(result.getNameFile());
				artifact.setStatus(result.getStatus().getCode());
				artifact.setMessage(result.getStatus().getMessage().replaceAll(System.getProperty("line.separator"), "\n"));
				artifact = crossoverBO.saveArtifact(artifact);
				logger.info("Saved artifact " + artifact.getPath() + File.separator +  artifact.getFile());
				this.getRequest().setAttribute("artifactList", crossoverBO.getAllArtifact());
				crossoverBO.close();
			}
		} catch (FileUploadException e) {
			e.printStackTrace();
			logger.error("FileUploadException " + e.getMessage());
			this.getRequest().setAttribute("messagesError", e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			StringBuilder sb = new StringBuilder();
			sb.append(e.getMessage() + "\n");
			sb.append("Perhaps the services is not online: " + prop.getValue("url.service.compile") + "\n");
			sb.append("http://localhost:8080/crossoverws/service/compile?wsdl\n");
			sb.append("See more in file config: " + workPath + File.separator + "config" + "config.properties");
			logger.error("FileUploadException " + sb.toString());
			this.getRequest().setAttribute("messagesError", sb.toString());
		}
		this.forward("/WEB-INF/jsp/upload.jsp");

	}

}
