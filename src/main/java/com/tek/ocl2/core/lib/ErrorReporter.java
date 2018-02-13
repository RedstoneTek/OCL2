package com.tek.ocl2.core.lib;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.Socket;

import com.tek.ocl2.core.ApplicationWindow;

public class ErrorReporter implements Thread.UncaughtExceptionHandler{
	
	final String host = "julienchampoux.com";
	final long port   = 9337;
	
	public boolean shouldReport() {
		return ApplicationWindow.inst().isAnonymousDataChecked();
	}
	
	public void uncaughtException(Thread t, Throwable e) {
	    if(shouldReport()) {
	    	final String stackTrace = getStackTrace(e);
	    	
	    	Runnable r = new Runnable() { public void run() {
	    		try {
	    			Socket socket = new Socket(host, (int) port);
	    			PrintStream output = new PrintStream(socket.getOutputStream());
	    		
	    			output.print(stackTrace);
	    		
	    			output.close();
	    			socket.close();
	    		}catch(Exception e1) { }
	    	
	    		System.gc();
	    	}};
	    	
	    	new Thread(r).start();
	    	
	    	System.out.println("[REPORTER]: Reported a " + e.getClass().getSimpleName() + " to the developer, thanks for your help!");
	    }
	}
	
	private String getStackTrace(final Throwable throwable) {
	     final StringWriter sw = new StringWriter();
	     final PrintWriter pw = new PrintWriter(sw, true);
	     throwable.printStackTrace(pw);
	     return sw.getBuffer().toString();
	}
	
}
