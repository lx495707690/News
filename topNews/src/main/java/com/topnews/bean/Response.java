package com.topnews.bean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.util.Log;

import com.topnews.helper.Keys;

public class Response {
	private int statusCode = 400;
	private JSONObject content;
	
	public Response(int statusCode, String content){
		this.statusCode = statusCode;
		try {
			this.content = new JSONObject(new JSONTokener(content));
		} catch (JSONException e) {
			Log.e("unknown json", content);
			e.printStackTrace();
			
			try{
				JSONArray jAr = new JSONArray(new JSONTokener(content));
				this.content = new JSONObject();
				this.content.put(Keys.RESULTS, jAr);
			}catch(JSONException ex){
				Log.e("unknown json", content);
				this.content = new JSONObject();
				
				ex.printStackTrace();
			}
			
		}
		
		if(!this.content.isNull(Keys.STATUS_CODE)){
			this.statusCode = this.content.optInt(Keys.STATUS_CODE, statusCode);
		}

	}

	public int getStatusCode() {
		return statusCode;
	}

	public JSONObject getContent() {
		return content;
	}

	
}
