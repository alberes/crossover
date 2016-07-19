package com.crossover.ws;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import com.crossover.util.PropertiesUtil;
import com.crossover.util.compile.CompilerHelper;
import com.crossover.util.zip.ZipHelper;
import com.crossover.ws.vo.CompilerResultVO;
import com.crossover.ws.vo.StatusVO;

@WebService(
		portName = "CompilerPort",
		serviceName = "CompilerService",
		targetNamespace = "http://crossoverws.com", name="Compiler",
		endpointInterface = "com.crossover.ws.CompileInterface")
public class Compiler implements CompileInterface{
	
	@WebMethod(operationName="execute", action="cs:executeProject")
    @WebResult(name="result")
	public CompilerResultVO execute(@WebParam(name="path")String path, @WebParam(name="fileName")String fileName) throws Exception{
		CompilerResultVO result = new CompilerResultVO();
		StatusVO status = new StatusVO();
		String workPath = "/opt/dev/projects/crossover/work";
		String os = System.getProperty("os.name").toLowerCase();
		if(os.indexOf("win") >= 0){
			workPath = "c:\\opt\\dev\\projects\\crossover\\work";
			os = "windows";
		}else{
			os = "";			
		}
		
		PropertiesUtil prop = new PropertiesUtil(workPath + File.separator + "config", "config" + os + ".properties");
		
		ZipHelper zip = new ZipHelper();
		CompilerHelper compiler = new CompilerHelper(prop.getValue("maven.home"));
		String pom = null;
		String pathOut = path + File.separator + "out";
		
		File fileOut = new File(pathOut);
		
		if(!fileOut.exists()){
			fileOut.mkdir();
		}
		
		List<String> listFiles = zip.unzipFile(path + File.separator + fileName, fileOut.getAbsolutePath());
		
		result.getListFiles().setFiles(listFiles);
		
		for(String s: fileOut.list()){
			if("pom.xml".equals(s)){
				pom = s;
				break;
			}
		}
		
		result.setStatus(status);
		
		if(pom == null){
			File fileclasses = new File(pathOut + File.separator + "classes");
			fileclasses.mkdir();
			List<String> erros = compiler.compile(pathOut + File.separator + "src", fileclasses.getAbsolutePath());
			if(erros.isEmpty()){
				zip.zipFile(pathOut + File.separator + "classes", "class", pathOut + File.separator + fileName);
				status.setCode(0);
				status.setMessage("Project was compiled with sucess.");
				result.setPath(pathOut);
				result.setNameFile(fileName);
			}else{
				StringBuffer error = new StringBuffer();
				error.append("Project failure");
				for(String s : erros){
					error.append(s + System.getProperty("line.separator"));
				}
				status.setCode(1);
				status.setMessage(error.toString());
				result.getStatus().getErros().setFiles(erros);
			}
			result.setProjectType("Java");
		}else{
			List<String> goals = new ArrayList<String>();
			goals.add("clean");
			goals.add("install");
			File log = new File(pathOut + File.separator + "log.txt");
			try{
				int code = compiler.compileMavenProject(pathOut + File.separator + pom, goals, log);
				if(code == 0){
					zip.zipFile(pathOut + File.separator + "target", "*", pathOut + File.separator + fileName);
					status.setCode(0);
					status.setMessage("Project was compiled with sucess.");
					result.setPath(pathOut);
					result.setNameFile(fileName);
				}else{
					status.setCode(code);
					status.setMessage("Project failure");
				}
			}catch(Exception e){
				status.setCode(1);
				status.setMessage("Project failure: " + e.getMessage());
				result.setStatus(status);
				result.setPath(pathOut);
			}
			status.setMessage(status.getMessage() + "\n" + compiler.getLog());
			result.setProjectType("Maven");
		}
		
		return result;
	}

}
