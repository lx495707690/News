package com.topnews.helper;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.MessageFormat;

public class URLUtil {
	public static final String UTF8Charset = "UTF-8";
	public static String encodeURLFormat(String input) {
		String ret = null;
		if (input != null) {
			try {
				ret = URLEncoder.encode(input, UTF8Charset);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return ret;
	}
	
	public static String decodeURLFormat(String input) {
		String ret = null;
		if (input != null) {
			try {
				ret = URLDecoder.decode(input, UTF8Charset);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return ret;
	}
	/**
	 * use {@link MessageFormat} to add arg to url<br>
	 * encode each argument with @link {@link URLEncoder} <br>
	 * <b>maximum total length of String is 255 characters</b>
	 * @author MinhTDH
	 * @param input
	 * @param args
	 * @return String
	 *********************************************************
	 */
	public static String encodURLWithArgs(String input, Object... args) {
		String ret = null;
		if (args != null) {
			for (int i=0; i <args.length; i++) {
				
				if(args[i] != null){
					args[i] = encodeURLFormat(args[i].toString());
				}
			}
		}
		ret = MessageFormat.format(input, args);
		return ret;
	}
	
	public static String encodeURL(String URL)
	{
		URL url = null;
		try {
			url = new URL(URL);
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		URI uri;
		try {
			uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
			try {
				url = uri.toURL();
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (URISyntaxException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		return url.toString();
	}
}
