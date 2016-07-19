package com.crossover.ws.crossoverws;

import java.io.File;
import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.crossover.util.PropertiesUtil;

public class CompilerTest {

	//File config
	static PropertiesUtil prop = null;
	static String os = null;
	com.crossover.ws.CompileInterface compiler;

	@Before
	public void setUp() throws Exception {
		String workPath = "/opt/dev/projects/crossover/work";
		os = System.getProperty("os.name").toLowerCase();
		if(os.indexOf("win") >= 0){
			workPath = "c:\\opt\\dev\\projects\\crossover\\work";
			os = "windows";
		}else{
			os = "";			
		}

		prop = new PropertiesUtil(workPath + File.separator + "config", "config" + os + ".properties");

		Service compilerService = Service.create(
				new URL(prop.getValue("url.service.compile")),
				new QName("http://crossoverws.com", "CompilerService"));
		compiler = compilerService.getPort(com.crossover.ws.CompileInterface.class);
	}

	@Test
	public void testJavaExecute() throws Exception {
		com.crossover.ws.vo.CompilerResultVO result = compiler.execute(prop.getValue("ws.file.test") + File.separator + "java", prop.getValue("ws.file.java.test"));
		Assert.assertEquals("Compile java code", 0, result.getStatus().getCode());
	}

	@Test
	public void testMavenExecute() throws Exception {
		com.crossover.ws.vo.CompilerResultVO result = compiler.execute(prop.getValue("ws.file.test") + File.separator + "maven", prop.getValue("ws.file.maven.test"));
		Assert.assertEquals("Compile maven project", 0, result.getStatus().getCode());
	}

}
