package com.tek.ocl2.core.lib;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.tek.ocl2.core.lib.OriginPool.State;

public class OriginApi {
	
	private static final String apiUrl = "https://origin-server-checker.firebaseio.com/.json";
	
	public static ArrayList<OriginPool> getPools(){
		ArrayList<OriginPool> pools = new ArrayList<OriginPool>();
		
		try{
			JSONObject json = readJsonFromUrl(apiUrl);
			JSONObject hosts = json.getJSONObject("hosts");
			
			int i = 1;
			String pool;
			
			while((pool = hosts.getString("pool-" + i)) != null) {
				pools.add(new OriginPool(json, pool));
				
				i++;
			}
		}catch(Exception e) {}
		
		return pools;
	}
	
	public static ArrayList<FAQ> getFaqs(){
		ArrayList<FAQ> faqs = new ArrayList<FAQ>();
		
		String url = "https://bitbucket.org/backspace119/ki-project-origin/wiki/FAQ";
		
		try {
			Document doc = Jsoup.connect(url).timeout(2500).get();
			
			Element content = doc.getElementById("wiki-content");
			
			for(Element p : content.getElementsByTag("p")) {
				FAQ faq = new FAQ(p);
				
				if(faq.valid()) {
					faqs.add(faq);
				}
			}
		}catch(Exception e) {}
		
		return faqs;
	}
	
	public static String getLatestVersion() {
		try {
			Document doc = Jsoup.connect("https://bitbucket.org/backspace119/ki-project-origin/downloads/").timeout(1000).get();
			
			for(Element element : doc.getElementsByTag("a")) {
				if(element.className().equals("execute")) {
					String href = element.attr("href");
					
					if(href != null && !href.isEmpty()) {
						if(href.endsWith(".jar")) {
							String[] tokens = href.split("/");
							String version = tokens[tokens.length - 1];
							
							return version.substring(7, version.length() - 11);
						}
					}
				}
			}
		} catch (IOException e) {
			return "NA";
		}
		
		return "NA";
	}
	
	public static long getHeight(){
		try{
			JSONObject json = readJsonFromUrl(apiUrl);
			
			long height = Long.parseLong(json.getString("mimpvehost_height"));
			
			return height;
		}catch(Exception e) {}
		
		return -1;
	}
	
	public static State getRelayStatus(){
		try{
			JSONObject json = readJsonFromUrl(apiUrl);
			
			String jstate = json.getString("mimpvehost");
			State state = State.byTranslate(jstate);
			
			if(state != null) {
				return state;
			}
		}catch(Exception e) {}
		
		return State.DOWN;
	}
	
	private static String readAll(Reader rd) throws IOException {
	    StringBuilder sb = new StringBuilder();
	    int cp;
	    while ((cp = rd.read()) != -1) {
	        sb.append((char) cp);
	    }
	    return sb.toString();
	}
	
	private static JSONObject readJsonFromUrl(String url) throws Exception{
	    InputStream is = null;
	    
		is = new URL(url).openStream();
		
	    try {
	        BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
	        String jsonText = readAll(rd);
	        JSONObject json = new JSONObject(jsonText);
	        return json;
	    } finally {
		    is.close();
	    }
    }
	
}
