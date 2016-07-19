package com.crossover.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class PropertiesUtil {
	
	private Properties properties = null;
		
	public PropertiesUtil(String path, String config){
		properties = new Properties();
		try {
			properties.load(new InputStreamReader(new FileInputStream(path + File.separator + config), "ISO-8859-1"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String getValue(String key){
		return properties.getProperty(key);
	}
	
}