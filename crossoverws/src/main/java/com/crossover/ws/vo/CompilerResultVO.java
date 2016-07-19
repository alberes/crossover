package com.crossover.ws.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "compileResult", namespace = "http://crossoverws.com")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "compileResult", namespace = "http://crossoverws.com")
public class CompilerResultVO {
	
	@XmlElement(name = "projectType", namespace = "")
	private String projectType;
	
	@XmlElement(name = "path", namespace = "")
	private String path;
	
	@XmlElement(name = "nameFile", namespace = "")
	private String nameFile;
	
	@XmlElement(name = "status", namespace = "")
	private StatusVO status;
	
	@XmlElement(name = "listFiles", namespace = "")
	private ListFilesVO listFiles;
	
	public CompilerResultVO() {
		super();
		this.listFiles = new ListFilesVO();
		
	}

	public String getProjectType() {
		return projectType;
	}

	public void setProjectType(String projectType) {
		this.projectType = projectType;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getNameFile() {
		return nameFile;
	}

	public void setNameFile(String nameFile) {
		this.nameFile = nameFile;
	}

	public StatusVO getStatus() {
		return status;
	}

	public void setStatus(StatusVO status) {
		this.status = status;
	}

	public ListFilesVO getListFiles() {
		return listFiles;
	}

	public void setListFiles(ListFilesVO listFiles) {
		this.listFiles = listFiles;
	}
	
}
