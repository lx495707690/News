package com.topnews.helper;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import javax.net.ssl.HttpsURLConnection;

import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import com.topnews.bean.Response;


public abstract class HTTPAsyncTask extends AsyncTask<String, Void, Response> {
	private static final String KEY = "key";
	private static final String NAME = "name";
	private static final String FILEPATH = "path";
	private URL url;
	private String method = Constants.GET;
	private boolean httpsEnabled = false;
	private boolean isMultipart = false;
	private LinkedHashMap<String,String> params = new LinkedHashMap<String, String>();
	private ArrayList<LinkedHashMap<String,String>> fileParams = new ArrayList<LinkedHashMap<String, String>>();
	private LinkedHashMap<String,byte[]> bytesParams = new LinkedHashMap<String,byte[]>();
//	private String encoding = "ISO-8859-1";

	public String token;

	protected abstract void onPreExecute();
	protected abstract void onPostExecute(Response response);
	
	public URL getUrl() {
		return url;
	}

	public void setUrl(String url) {

		try {
			url = URLUtil.encodeURL(url);
			this.url = new URL(url);
			if(url.startsWith("https")){
				this.httpsEnabled = true;
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
	
	public void setGetParams(String key, String value){
		if(key != null && value != null && key.trim().length() > 0 && value.trim().length() > 0){
			String currentUrl = url.toString();
			if(currentUrl.contains("?") && currentUrl.indexOf("?") <= currentUrl.length()){
				try {
					
					currentUrl += "&" + key +"="+ Helper.escapeURL(value);
					this.url = new URL(currentUrl);
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
			}else{
				try {
					currentUrl += "?" + key +"="+ Helper.escapeURL(value);
					this.url = new URL(currentUrl);
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void setGetParams(String key, int value){
		String val = String.valueOf(value);
		setGetParams(key, val);
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public boolean isHttpsEnabled() {
		return httpsEnabled;
	}

	public void setHttpsEnabled(boolean httpsEnabled) {
		this.httpsEnabled = httpsEnabled;
	}

	public void setPostParams(String key, String value) {
		if(key==null || value==null) return;
		if(key.trim().length() <= 0){
			return;
		}
		this.params.put(key, value);

	}
	
	public void setPostParams(String key, double value) {
		String d = String.valueOf(value);
		this.setPostParams(key, d);
	}
	
	public void setImageParams(String key, String path){
		if(path.length() <=0 || key.trim().length() <= 0){
			return;
		}
		this.isMultipart = true;
		String[] q = path.split("/");
        int idx = q.length - 1;
		LinkedHashMap<String, String> file = new LinkedHashMap<String,String>();
		file.put(KEY, key);
		file.put(NAME, q[idx]);
		file.put(FILEPATH, path);
		this.fileParams.add(file);
	}
	
	public void setByteParams(String key, byte[] bytes){
		if(key.trim().length() <= 0 || bytes == null || bytes.length <=0){
			return;
		}
		this.isMultipart = true;
		this.bytesParams.put(key, bytes);
		
	}
	
	public void execute(){
    	super.execute();
    }
	

	@Override
	protected Response doInBackground(String... urls) {
		//init
		HttpURLConnection 	conn 			= null;
		InputStream 		in 				= null;
		int 				http_status 	= Constants.STATUS_BAD_REQUEST;
		String 				responseString 	= null;
		Response 			response 		= null;
		int 				maxBufferSize 	= 2*1024*1024;
		
		try {
			
			if(httpsEnabled){
				conn = (HttpsURLConnection) url.openConnection();
			}else{
				conn = (HttpURLConnection) url.openConnection();
			}
			
			Log.e("Test", url.toString());
			
			
			conn.setConnectTimeout(Constants.TIMEOUT);
			conn.setReadTimeout(Constants.TIMEOUT);

			conn.setRequestProperty("token",token);
			
			if(this.method.equalsIgnoreCase(Constants.POST)){
				//post data to server
				conn.setConnectTimeout(Constants.TIMEOUT_UPLOAD_FILE);
//				conn.setRequestProperty( "Accept-Encoding", "" );
				conn.setDoOutput(true);
				conn.setRequestMethod("POST");
				if(!isMultipart){
					String paramsStr = "";
					for(String key : params.keySet()){
						paramsStr += key +"="+ URLEncoder.encode(params.get(key), "utf-8") + "&";
					}
					paramsStr = paramsStr.substring(0, paramsStr.length() - 1);
					conn.setFixedLengthStreamingMode(paramsStr.getBytes().length);
					conn.setRequestProperty("Connection", "Keep-Alive");
					conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
					PrintWriter out = new PrintWriter(conn.getOutputStream());
					Log.e("POST", paramsStr);
					out.print(paramsStr);
					out.close();
				}else{
					String twoHyphens 	= "--";
					String boundary 	=  "*****"+Long.toString(System.currentTimeMillis())+"*****";
					String lineEnd 		= "\r\n";
					conn.setRequestProperty("Connection", "Keep-Alive");
					conn.setRequestProperty("Content-Type", "multipart/form-data; boundary="+boundary);
					DataOutputStream outputStream = new DataOutputStream(conn.getOutputStream());
					for(LinkedHashMap<String,String> map : fileParams){
						int bytesRead, bytesAvailable, bufferSize;

						outputStream.writeBytes(twoHyphens + boundary + lineEnd);
						outputStream.writeBytes("Content-Disposition: form-data; name=\""+map.get(KEY)+"\"; filename=\""+map.get(NAME)+"\"");
						outputStream.writeBytes(lineEnd);
						outputStream.writeBytes("Content-Type: image/png" + lineEnd);
		                outputStream.writeBytes("Content-Transfer-Encoding: binary" + lineEnd);
		                outputStream.writeBytes(lineEnd);
		                
//		                Log.d("value", map.get(Keys.KEY) +":"+map.get(Keys.NAME)+":"+map.get(Keys.FILEPATH));
		                File file = new File(map.get(FILEPATH));
		                FileInputStream fileInputStream = new FileInputStream(file);
		                bytesAvailable = fileInputStream.available();
		                bufferSize = Math.min(bytesAvailable, maxBufferSize);
		                byte[] buffer = new byte[bufferSize];
		                
		                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
		                while(bytesRead > 0) {
		                        outputStream.write(buffer, 0, bufferSize);
		                        bytesAvailable = fileInputStream.available();
		                        bufferSize = Math.min(bytesAvailable, maxBufferSize);
		                        bytesRead = fileInputStream.read(buffer, 0, bufferSize);
		                }
		                
		                outputStream.writeBytes(lineEnd);
		                
						fileInputStream.close();
					}
					
					for(String key : this.bytesParams.keySet()){
						outputStream.writeBytes(twoHyphens + boundary + lineEnd);
						outputStream.writeBytes("Content-Disposition: form-data; name=\""+key+"\"; filename=\"uploads.jpg\"");
						outputStream.writeBytes(lineEnd);
						outputStream.writeBytes("Content-Type: image/jpeg" + lineEnd);
		                outputStream.writeBytes("Content-Transfer-Encoding: binary" + lineEnd);
		                outputStream.writeBytes(lineEnd);
		                
						outputStream.write(bytesParams.get(key));
						Log.d("value", key +":"+params.get(key));
						outputStream.writeBytes(lineEnd);
						
					}
					
					for(String key : params.keySet()){
						outputStream.writeBytes(twoHyphens + boundary + lineEnd);
						outputStream.writeBytes("Content-Disposition: form-data; name=\""+key+"\"");
						outputStream.writeBytes(lineEnd);
						outputStream.writeBytes("Content-Type: text/plain"+lineEnd);
						outputStream.writeBytes(lineEnd);
						outputStream.writeBytes(params.get(key));
						Log.d("value", key +":"+params.get(key));
						outputStream.writeBytes(lineEnd);
						
					}
					
					outputStream.writeBytes(twoHyphens + boundary + twoHyphens +lineEnd);
					outputStream.flush();
					outputStream.close();
				}
				
			}

			conn.connect();

			 http_status = conn.getResponseCode();
			 
			 if(http_status == Constants.STATUS_SUCCESS){
				 in = conn.getInputStream();
			 }else{
				 in = conn.getErrorStream();
			 }


			if (in != null) {
				//read input from server
				BufferedReader br = new BufferedReader(new InputStreamReader(in));

				StringBuilder sb = new StringBuilder();

				String line;
				while ((line = br.readLine()) != null) {
					sb.append(line);
				}
				
				responseString = sb.toString().replace("\\'", "'");
 				response = new Response(http_status, responseString);
			}

		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
			response = new Response(HttpURLConnection.HTTP_INTERNAL_ERROR, "");
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			response = null;
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}
		
		return response;
		

	}
}
