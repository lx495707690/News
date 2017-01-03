package com.topnews;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.dd.processbutton.iml.ActionProcessButton;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.topnews.app.AppApplication;
import com.topnews.helper.Api;
import com.topnews.helper.ApiService;
import com.topnews.helper.CircleTransform;
import com.topnews.helper.Constants;
import com.topnews.helper.Helper;
import com.topnews.helper.Keys;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

public class SignUpActivity extends Activity {

	private EditText etAccount;
	private EditText etPWD;
	private EditText etName;
	private ActionProcessButton btnSignUp;
	private ImageView imgAvatar;

	private String avatar;

	int IMAGE_PICKER = 99999;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.signup);
		initView();
		setListener();
	}

	
	private void initView() {
		etAccount = (EditText) findViewById(R.id.etAccount);
		etPWD     = (EditText) findViewById(R.id.etPWD);
		etName    = (EditText) findViewById(R.id.etName);
		btnSignUp = (ActionProcessButton) findViewById(R.id.btnSignUp);
		imgAvatar = (ImageView) findViewById(R.id.imgAvatar);
	}

	private void setListener(){
		btnSignUp.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				signup();
			}
		});

		imgAvatar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SignUpActivity.this, ImageGridActivity.class);
				startActivityForResult(intent, IMAGE_PICKER);
			}
		});
	}

	private void signup(){
		ApiService as = new ApiService(Api.API_SIGNUP);

		as.setPostParams(Keys.MOBILE, etAccount.getText().toString().trim());
		as.setPostParams(Keys.PASSWORD, etPWD.getText().toString().trim());
		as.setPostParams(Keys.NAME, etName.getText().toString().trim());
		as.setImageParams(Keys.PHOTO,avatar);
		as.setMethod(Constants.POST);
		as.execute(new ApiService.OnServiceListener() {

			@Override
			public void onReceivedData(JSONObject json) {
				// TODO Auto-generated method stub
				try {
					if (json.getInt(Keys.CODE) == 1) {
						btnSignUp.setProgress(100);
						finish();
					}else{
						btnSignUp.setProgress(-1);
						Toast.makeText(SignUpActivity.this,json.getString(Keys.MSG),Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			@Override
			public void OnPreExecute() {
				// TODO Auto-generated method stub
				btnSignUp.setMode(ActionProcessButton.Mode.ENDLESS);
				btnSignUp.setProgress(1);

			}

			@Override
			public void OnFailed() {
				// TODO Auto-generated method stub
				btnSignUp.setProgress(-1);
			}

		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
			if (data != null && requestCode == IMAGE_PICKER) {
				ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
				avatar = images.get(0).path;
				Helper.displayAvatar(this,avatar,imgAvatar);
			} else {
				Toast.makeText(this, "没有数据", Toast.LENGTH_SHORT).show();
			}
		}
	}

}
