package com.crossover.ws.vo;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "list", namespace = "http://crossoverws.com")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "list", namespace = "http://crossoverws.com")
public class ListFilesVO {
	
	@XmlElement(name = "file", namespace = "")
	private List<String> files;

	public ListFilesVO() {
		super();
		this.files = new ArrayList<String>();
	}

	public List<String> getFiles() {
		return files;
	}

	public void setFiles(List<String> files) {
		this.files = files;
	}

}
