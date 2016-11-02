package com.topnews;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.dd.processbutton.iml.ActionProcessButton;
import com.topnews.helper.Api;
import com.topnews.helper.ApiService;
import com.topnews.helper.Constants;
import com.topnews.helper.Keys;
import com.topnews.helper.UserInfoManager;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends Activity {

	private EditText etAccount;
	private EditText etPWD;
	private ActionProcessButton btnLogin;
	private TextView tvSignUp;

	View v;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		v = View.inflate(this, R.layout.login, null);
		setContentView(v);
		initView();
		setListener();
	}

	
	private void initView() {
		etAccount = (EditText) v.findViewById(R.id.etAccount);
		etPWD     = (EditText) v.findViewById(R.id.etPWD);
		btnLogin  = (ActionProcessButton) v.findViewById(R.id.btnLogin);

		tvSignUp  = (TextView) v.findViewById(R.id.tvSignUp);
		tvSignUp.setText(Html.fromHtml("<u>"+getString(R.string.news_signup)+"</u>"));

	}

	private void setListener(){
		btnLogin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				login();
			}
		});

		tvSignUp.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				startActivity(new Intent(LoginActivity.this,SignUpActivity.class));
			}
		});
	}

	private void login(){
		ApiService as = new ApiService(Api.API_LOGIN);

		as.setPostParams(Keys.MOBILE, etAccount.getText().toString().trim());
		as.setPostParams(Keys.PASSWORD, etPWD.getText().toString().trim());
		as.setMethod(Constants.POST);
		as.execute(new ApiService.OnServiceListener() {

			@Override
			public void onReceivedData(JSONObject json) {
				// TODO Auto-generated method stub

				try {
					if (json.getInt(Keys.CODE) == 1) {
						btnLogin.setProgress(100);
						JSONObject data = json.getJSONObject(Keys.DATA);
						String mobile = data.getString(Keys.MOBILE);
						String fullName = data.getString(Keys.FULL_NAME);
						String role = data.getString(Keys.ROLE);
						String token = data.getString(Keys.TOKEN);

						UserInfoManager.getInstance(LoginActivity.this).saveUserInfo(mobile,fullName,role,token);
						startActivity(new Intent(LoginActivity.this,MainActivity.class));
						finish();
					}else{
						btnLogin.setProgress(-1);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			@Override
			public void OnPreExecute() {
				// TODO Auto-generated method stub

				btnLogin.setMode(ActionProcessButton.Mode.ENDLESS);
				btnLogin.setProgress(1);
			}

			@Override
			public void OnFailed() {
				// TODO Auto-generated method stub
				btnLogin.setProgress(-1);
			}

		});
	}

}
