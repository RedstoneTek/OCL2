package com.tek.ocl2.core.lib;

import org.jsoup.nodes.Element;

public class FAQ {
	
	public String question;
	public String answer;
	
	public FAQ(Element element) {
		String content = element.text();
		
		if(content.startsWith("#")) return;
		
		content = content.substring(3);
		
		String[] qa = content.split(" A: ");
	
		if(qa.length == 2) {
			this.question = qa[0];
			this.answer = qa[1];
		}
	}
	
	public boolean valid() {
		if(question == null || answer == null) return false;
		
		if(question.isEmpty() || answer.isEmpty()) return false;
		
		return true;
	}
	
}
