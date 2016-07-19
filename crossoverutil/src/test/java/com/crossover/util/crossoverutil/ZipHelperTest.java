package com.crossover.util.crossoverutil;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.crossover.util.PropertiesUtil;
import com.crossover.util.zip.ZipHelper;

public class ZipHelperTest {

	//File config
	static PropertiesUtil prop = null;
	static String os = null;
	static String fileName = null;

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
		fileName = prop.getValue("zip.file.test") + File.separator + "compress" + File.separator + System.currentTimeMillis() + ".zip";
	}
	
	@Test
	public void testZipFile() throws IOException {
		ZipHelper zip = new ZipHelper();
		String compressFile = zip.zipFile(prop.getValue("zip.file.test") + File.separator + "compress", "*", fileName);
		File file = new File(compressFile);
		Assert.assertTrue("Compress file " + file.getName(), file.isFile());
	}
	
	@Test
	public void testUnzipFile() throws IOException {
		ZipHelper zip = new ZipHelper();
		List<String> list = zip.unzipFile(fileName, prop.getValue("zip.file.test") + File.separator + "decompress");
		Assert.assertTrue("All files was decompress ", !list.isEmpty());
	}

}
