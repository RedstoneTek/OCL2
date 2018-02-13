package com.tek.ocl2;

import com.tek.ocl2.core.ApplicationWindow;
import com.tek.ocl2.core.lib.ErrorReporter;

public class Start {
	
	public static void main(String[] args) {
		Thread.setDefaultUncaughtExceptionHandler(new ErrorReporter());
		
		new ApplicationWindow();
	}
	
}
