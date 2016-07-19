package com.crossover.util.crossoverutil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.maven.shared.invoker.MavenInvocationException;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.crossover.util.PropertiesUtil;
import com.crossover.util.compile.CompilerHelper;

public class CompilerHelperTest {

	//File config
	static PropertiesUtil prop = null;
	static String os = null;

	@BeforeClass
	public static void setUp(){
		String workPath = "/opt/dev/projects/crossover/work";
		os = System.getProperty("os.name").toLowerCase();
		if(os.indexOf("win") >= 0){
			workPath = "c:\\opt\\dev\\projects\\crossover\\work";
			os = "windows";
		}else{
			os = "";			
		}

		prop = new PropertiesUtil(workPath + File.separator + "config", "config" + os + ".properties");
	}

	@Test
	public void testCompile() {
		File f = new File(prop.getValue("compile.file.test") + File.separator + "java" + File.separator + "classes");
		f.mkdir();

		CompilerHelper compiler = new CompilerHelper(prop.getValue("maven.home"));
		List<String> errorList = compiler.compile(prop.getValue("compile.file.test") + File.separator + "java" + File.separator + "src",
				f.getAbsolutePath());
		Assert.assertTrue("All files was compiled ", errorList.isEmpty());
	}
	
	@Test
	public void testcompileMavenProject() throws MavenInvocationException, Exception{
		CompilerHelper compiler = new CompilerHelper(prop.getValue("maven.home"));
		
		List<String> goals = new ArrayList<String>();
		goals.add("clean");
		goals.add("install");

		int code = compiler.compileMavenProject(prop.getValue("compile.file.test") + File.separator + "maven" + File.separator + "pom.xml", goals,
				new File(prop.getValue("compile.file.test") + File.separator + "maven" + File.separator + "log.txt"));
		
		Assert.assertEquals("Maven project was compiled with success", 0, code);
	}

}
