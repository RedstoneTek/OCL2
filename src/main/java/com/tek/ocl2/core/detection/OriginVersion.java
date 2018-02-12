package com.tek.ocl2.core.detection;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class OriginVersion {
	
	public File file;
	String version;
	
	public OriginVersion(File file) {
		this.file = file;
	}
	
	public boolean validVersion() {
		boolean valid = false;
		
		if(file.exists()) {
			try {
				ZipFile zipFile = new ZipFile(file);
				
				@SuppressWarnings("unchecked")
				Enumeration<ZipEntry> entries = (Enumeration<ZipEntry>) zipFile.entries();
				
				while(entries.hasMoreElements()) {
					ZipEntry entry = entries.nextElement();
					
					//Detect version of jar
					if(entry.getName().equals("META-INF/maven/com.lifeform/Origin/pom.xml")) {
						BufferedReader reader = new BufferedReader(new InputStreamReader(zipFile.getInputStream(entry)));
						String line;
						
						while((line = reader.readLine()) != null) {
							if(line.contains("<version>")) {
								String[] t1 = line.split(">");
							    String[] t2 = t1[1].split("<");
							    
							    String version = t2[0];
							    
							    this.version = version;
								
								break;
							}
						}
						
						reader.close();
					}
					
					//Validate origin version
					if(entry.getName().equals("com/lifeform/main/Main.class")) valid = true;
				}
				
				zipFile.close();
			} catch (Exception e) { }
		}
		
		return valid;
	}
	
	@Override
	public String toString() {
		return version;
	}
	
	public static ArrayList<OriginVersion> getVersions(File directory){
		ArrayList<OriginVersion> versions = new ArrayList<OriginVersion>();
		
		if(directory.isDirectory()) {
			for(File file : directory.listFiles()) {
				OriginVersion version = new OriginVersion(file);
				
				if(version.validVersion()) {
					versions.add(version);
				}
			}
		}else {
			return null;
		}
		
		return versions;
	}
	
}
