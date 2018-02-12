package com.tek.ocl2.core.lib;

import org.json.JSONException;
import org.json.JSONObject;

public class OriginPool {
	
	String ip;
	State state;
	
	public OriginPool(JSONObject response, String host) {
		this.ip = host;
		
		String base = host.replaceAll("\\.", "") + "-pool";
		
		State state = null;
		try {
			state = State.byTranslate(response.getString(base));
		} catch (JSONException e) { }
		
		if(state != null) {
			this.state = state;
		}
	}
	
	@Override
	public String toString() {
		return ip + " > " + state.translate.toUpperCase();
	}
	
	public enum State {
		UP("up"),
		DOWN("down");
		
		public String translate;
		
		State(String translate){
			this.translate = translate;
		}
		
		public static State byTranslate(String translate) {
			for(State state : State.values()) {
				if(state.translate.equals(translate)) {
					return state;
				}
			}
			
			return null;
		}
	}
	
}
