package com.crossover.ws.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "status", namespace = "http://crossoverws.com")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "status", namespace = "http://crossoverws.com")
public class StatusVO {
	
	@XmlElement(name = "code", namespace = "")
	private int code;
	
	@XmlElement(name = "message", namespace = "")
	private String message;
	
	@XmlElement(name = "erros", namespace = "")
	private ListFilesVO erros;
	

	public StatusVO() {
		super();
		this.erros = new ListFilesVO();
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public ListFilesVO getErros() {
		return erros;
	}

	public void setErros(ListFilesVO erros) {
		this.erros = erros;
	}

}
