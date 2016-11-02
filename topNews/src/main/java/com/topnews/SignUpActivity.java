package com.topnews;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.topnews.helper.Api;
import com.topnews.helper.ApiService;
import com.topnews.helper.Constants;
import com.topnews.helper.Keys;

import org.json.JSONException;
import org.json.JSONObject;

public class SignUpActivity extends Activity {

	private EditText etAccount;
	private EditText etPWD;
	private EditText etName;
	private Button btnSignUp;


	View v;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		v = View.inflate(this, R.layout.signup, null);
		setContentView(v);
		initView();
		setListener();
	}

	
	private void initView() {
		etAccount = (EditText) v.findViewById(R.id.etAccount);
		etPWD     = (EditText) v.findViewById(R.id.etPWD);
		etName    = (EditText) v.findViewById(R.id.etName);
		btnSignUp = (Button) v.findViewById(R.id.btnSignUp);
	}

	private void setListener(){
		btnSignUp.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				signup();
			}
		});
	}

	private void signup(){
		ApiService as = new ApiService(Api.API_SIGNUP);

		as.setPostParams(Keys.MOBILE, etAccount.getText().toString().trim());
		as.setPostParams(Keys.PASSWORD, etPWD.getText().toString().trim());
		as.setPostParams(Keys.FULL_NAME, etName.getText().toString().trim());
		as.setMethod(Constants.POST);
		as.execute(new ApiService.OnServiceListener() {

			@Override
			public void onReceivedData(JSONObject json) {
				// TODO Auto-generated method stub
				try {
					if (json.getInt(Keys.CODE) == 1) {
						finish();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			@Override
			public void OnPreExecute() {
				// TODO Auto-generated method stub

			}

			@Override
			public void OnFailed() {
				// TODO Auto-generated method stub

			}

		});
	}

}
