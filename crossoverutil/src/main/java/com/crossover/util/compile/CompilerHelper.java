package com.crossover.util.compile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import org.apache.maven.shared.invoker.DefaultInvocationRequest;
import org.apache.maven.shared.invoker.DefaultInvoker;
import org.apache.maven.shared.invoker.InvocationRequest;
import org.apache.maven.shared.invoker.InvocationResult;
import org.apache.maven.shared.invoker.Invoker;
import org.apache.maven.shared.invoker.MavenInvocationException;

import com.crossover.util.PropertiesUtil;

public class CompilerHelper {
	
	private String log;
	
	private String mavenHome;
	
	public CompilerHelper() {
		super();
	}

	public CompilerHelper(String mavenHome) {
		super();
		this.mavenHome = mavenHome;
	}

	public List<String> compile(String sourcePath, String classesPath){
		//http://bugs.java.com/bugdatabase/view_bug.do?bug_id=7181951
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler(); 
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>(); 
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnostics, Locale.getDefault(), null);
        File sourceDir  = new File(sourcePath);
        File classesDir = new File(classesPath);
        List<String> messages = new ArrayList<String>();
        
        List<JavaFileObject> javaObjects = scanJavaFile(sourceDir, fileManager); 
        //if (javaObjects.size() == 0) { 
            //throw new CompilationError("There are no source files to compile in " + sourceDir.getAbsolutePath()); 
        //} 
        String[] compileOptions = new String[]{"-d", classesDir.getAbsolutePath()} ; 
        Iterable<String> compilationOptions = Arrays.asList(compileOptions); 
         
        JavaCompiler.CompilationTask compilerTask = compiler.getTask(null, fileManager, diagnostics, compilationOptions, null, javaObjects) ; 
        try {
			fileManager.flush();
			fileManager.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
        if (!compilerTask.call()) { 
            for (Diagnostic<?> diagnostic : diagnostics.getDiagnostics()) { 
                messages.add("Error on line " + diagnostic.getLineNumber() + " in " + diagnostic);
            } 
            //throw new CompilationError("Could not compile project"); 
        }
        return messages;
	}

	private List<JavaFileObject> scanJavaFile(File dir, StandardJavaFileManager fileManager) { 
        List<JavaFileObject> javaObjects = new LinkedList<JavaFileObject>(); 
        File[] files = dir.listFiles(); 
        for (File file : files) { 
            if (file.isDirectory()) { 
                javaObjects.addAll(scanJavaFile(file, fileManager)); 
            } 
            else if (file.isFile() && file.getName().toLowerCase().endsWith(".java")) { 
                javaObjects.add(readJavaObject(file, fileManager)); 
            } 
        } 
        return javaObjects; 
    } 
	
	private JavaFileObject readJavaObject(File file, StandardJavaFileManager fileManager) { 
        Iterable<? extends JavaFileObject> javaFileObjects = fileManager.getJavaFileObjects(file); 
        Iterator<? extends JavaFileObject> it = javaFileObjects.iterator(); 
        if (it.hasNext()) { 
            return it.next(); 
        } 
        throw new RuntimeException("Could not load " + file.getAbsolutePath() + " java file object"); 
    }
	
	public int compileMavenProject(String pathPom, List<String> goals, File buildLogFile) throws MavenInvocationException, Exception{
		InvocationRequest request = new DefaultInvocationRequest();
		request.setPomFile(new File(pathPom));
		
		request.setGoals(goals);
		request.setShowErrors(true);
		Invoker invoker = new DefaultInvoker();
		LoggerHandler handler = new LoggerHandler(buildLogFile);
		request.setOutputHandler(handler);
		request.setErrorHandler(handler);
		if(this.mavenHome != null){
			invoker.setMavenHome(new File(this.mavenHome));
		}
		InvocationResult result = invoker.execute(request);

		this.log = handler.getLog();
		
		if(result.getExitCode() != 0){
			if(result.getExecutionException() != null){
				throw new Exception( "Failed to execute command " + goals,
                        result.getExecutionException() );
			}else{
				throw new Exception( "Failed to execute command " + goals + " Code error " + result.getExitCode());
			}
		}
		
		return 0;
	}

	public String getLog(){
		return log;
	}
	
}
