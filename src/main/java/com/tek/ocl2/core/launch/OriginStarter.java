package com.tek.ocl2.core.launch;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import com.tek.ocl2.core.ApplicationWindow;
import com.tek.ocl2.core.detection.OriginVersion;

public class OriginStarter {
	
	Process process;
	Thread t;
	
	public void startOrigin(final LaunchMode mode, final boolean full, final OriginVersion version) {
		Runnable r = new Runnable() { public void run() {
			if(process == null) {
				if(version != null) {
					if(version.file.exists()) {
						Runtime runtime = Runtime.getRuntime();
					
						ArrayList<String> arguments = new ArrayList<String>();
					
						arguments.add("java");
						arguments.add("-jar");
						arguments.add(version.file.getName());
						if(mode != LaunchMode.REGULAR) arguments.add(mode.getArgument());
					
						if(full) arguments.add("-full");
					
						try {
							process = runtime.exec(arguments(arguments));
						} catch (IOException e) { }
						
						System.gc();
						
						if(process != null) {
							BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
							
							String line = null;
							
							while(true) {
								try {
									line = reader.readLine();
								} catch (IOException e) { }
								
								if(line != null) ApplicationWindow.inst().print(line);
							}
						}
					}
				}
			}
		}};
		
		t = new Thread(r);
		
		t.start();
	}
	
	public void endOrigin() {
		try{
			process.destroy();
			process.destroyForcibly();
			process = null;
			
			t.interrupt();
			t = null;
			
			System.gc();
		}catch(Exception e) { }
	}
	
	public enum LaunchMode{
		REGULAR(""),
		POOL("-pool"),
		POOLHOST("-pr");
		
		public String arg;
		
		LaunchMode(String arg) {
			this.arg = arg;
		}
		
		public String getArgument() {
			return arg;
		}
	}
	
	private static String[] arguments(ArrayList<String> arguments) {
		String[] array = new String[arguments.size()];
		
		for(int i = 0; i < arguments.size(); i++) {
			array[i] = arguments.get(i);
		}
		
		return array;
	}
	
}
