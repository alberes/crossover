package com.crossover.util.zip;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ZipHelper {

	public static final int BUFFER_SIZE = 1024;
	
	/**
	 * Unzip files
	 * 
	 * @param inputZipFile
	 * @param outputZipFile
	 * @return List with names of files
	 * @throws IOException
	 */
	public List<String> unzipFile(String inputZipFile, String outputZipFile) throws IOException{
		byte[] buffer = new byte[ZipHelper.BUFFER_SIZE];
		List<String> fileList = new ArrayList<String>();
		String fileName = null;
		File folder = new File(inputZipFile);
		File newFile = null;
		FileOutputStream fos = null;
		
		//Create folder if not exists
		if(!folder.exists()){
			folder.mkdir();
		}
				
		ZipInputStream zis = new ZipInputStream(new FileInputStream(folder));
		
		ZipEntry zipEntry = zis.getNextEntry();
		
		while(zipEntry != null){
			fileName = zipEntry.getName();
			newFile = new File(outputZipFile + File.separator + fileName);
			fileList.add(outputZipFile + File.separator + fileName);
			//System.out.println("Unzip file: " + newFile.getAbsolutePath());
			
			//Create all folders else we can have FileNotFoundException
			if(zipEntry.isDirectory()){
				newFile.mkdirs();
				zipEntry = zis.getNextEntry();
				continue;
			}else{
				newFile.getParentFile().mkdirs();
			}
			fos = new FileOutputStream(newFile);
			
			//Write files on disk
			int len = 0;
			while((len = zis.read(buffer)) > 0){
				fos.write(buffer, 0, len);
			}
			
			fos.close();
			zipEntry = zis.getNextEntry();
		}
		
		zis.closeEntry();
		zis.close();
		
		//System.out.println("Decompress all files");
		
		return fileList;
	}
	
	/**
	 * Compress all files
	 * 
	 * @param basePath
	 * @param ext
	 * @param outputZipFile
	 * @return List with names of files
	 * @throws IOException
	 */
	public String zipFile(String basePath, String ext, String outputZipFile) throws IOException{
		byte[] buffer = new byte[ZipHelper.BUFFER_SIZE];
		List<File> listFiles = new ArrayList<File>();
		String fullFileName = null;
		ZipEntry zipEntry = null;
		FileInputStream fis = null;
		
		File baseFile = new File(basePath);
		
		findFiles(listFiles, baseFile, ext);
		
		FileOutputStream fos = new FileOutputStream(outputZipFile);
		ZipOutputStream zos = new ZipOutputStream(fos);
		
		
		for(File file : listFiles){
			//Keep the qualify name
			fullFileName = file.getAbsolutePath().substring(baseFile.getAbsolutePath().length() + 1);
			zipEntry = new ZipEntry(fullFileName);
			zos.putNextEntry(zipEntry);
			
			fis = new FileInputStream(file.getPath());
			int len = 0;
			while((len = fis.read(buffer)) > 0){
				zos.write(buffer, 0, len);
			}
		}
		fis.close();
		zos.close();
		
		return outputZipFile;
	}
	
	public void findFiles(List<File> files, File folder, String ext){
		
		for(File f : folder.listFiles()){
			if(f.isDirectory()){
				findFiles(files, f, ext);
			}else if(f.getName().endsWith(ext)){
				files.add(f);
			}else if("*".equals(ext)){
				files.add(f);
			}
		}
		
	}
}
