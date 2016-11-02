package com.topnews.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class UserInfoManager {

	static final String KEY_MOBILE = "mobile";
	static final String KEY_FULL_NAME = "full_name";
	static final String KEY_ROLE = "role";
	static final String KEY_TOKEN = "token";
	private static UserInfoManager _userInfo = null;
	private static String FILE_NAME = "lx_news";
	private String username;
	private SharedPreferences prefs;


	private UserInfoManager(){
		super();
	}
	
	public static UserInfoManager getInstance(Context c) {
		if(_userInfo == null){
			_userInfo = new UserInfoManager();
			_userInfo.openPrefs(c.getApplicationContext());
		}
		return _userInfo;
	}
	
	private void openPrefs(Context c){
		this.prefs = c.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
	}
	
	public void saveUserInfo(String mobile, String fullName, String role, String token){
		Editor editor = this.prefs.edit();
		editor.putString(KEY_MOBILE, mobile);
		editor.putString(KEY_FULL_NAME, fullName);
		editor.putString(KEY_ROLE, role);
		editor.putString(KEY_TOKEN, token);
		editor.commit();
	}

	public String getToken(){
		return this.prefs.getString(KEY_TOKEN, null);
	}

	public void logout(){
		prefs.edit().clear().commit();
		_userInfo = null;
	}
}
