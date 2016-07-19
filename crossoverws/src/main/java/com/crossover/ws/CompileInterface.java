package com.crossover.ws;

import javax.jws.WebService;

import com.crossover.ws.vo.CompilerResultVO;


@WebService(
		targetNamespace = "http://crossoverws.com")
public interface CompileInterface {
	

	public CompilerResultVO execute(String path, String fileName) throws Exception;

}
