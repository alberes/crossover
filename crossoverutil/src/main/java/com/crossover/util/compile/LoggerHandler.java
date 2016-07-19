package com.crossover.util.compile;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.maven.shared.invoker.InvocationOutputHandler;

public class LoggerHandler implements InvocationOutputHandler {

	private static final String LS = System.getProperty( "line.separator" ); 
	 
    private final File output; 

    private FileWriter writer;
    
    private StringBuffer log;

    LoggerHandler(File logFile){ 
        output = logFile;
        log = new StringBuffer();
    } 

    /** {@inheritDoc} */ 
    public void consumeLine(String line){ 
        if (writer == null){ 
            try{ 
                output.getParentFile().mkdirs(); 
                writer = new FileWriter( output ); 
            }catch(IOException e){ 
                throw new IllegalStateException( "Failed to open build log: " + output + "\n\nError: " 
                    + e.getMessage()); 
            } 
        } 

        try{ 
            writer.write(line + LS); 
            log.append(line + "\n");
            writer.flush(); 
        }catch (IOException e){ 
            throw new IllegalStateException( "Failed to write to build log: " + output + " output:\n\n\'" + line 
                + "\'\n\nError: " + e.getMessage()); 
        } 
    } 

    public void close(){ 
        try {
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		} 
    }
    
    public String getLog(){
    	return log.toString();
    }
}
