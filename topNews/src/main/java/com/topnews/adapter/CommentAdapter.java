package com.topnews.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.topnews.LoginActivity;
import com.topnews.R;
import com.topnews.bean.CommentEntity;
import com.topnews.helper.Api;
import com.topnews.helper.ApiService;
import com.topnews.helper.Helper;
import com.topnews.helper.Keys;
import com.topnews.helper.UserInfoManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CommentAdapter extends BaseAdapter {
	private Context context;
	public List<CommentEntity> commentList = new ArrayList<>();

	private int currentPostion;

	public CommentAdapter(Context context, List<CommentEntity> commentList) {
		this.context = context;
		this.commentList = commentList;
	}

	@Override
	public int getCount() {
		return commentList == null ? 0 : commentList.size();
	}

	@Override
	public CommentEntity getItem(int position) {
		if (commentList != null && commentList.size() != 0) {
			return commentList.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder mHolder;
		View view = convertView;
		if (view == null) {
			mHolder = new ViewHolder();
			view = LayoutInflater.from(context).inflate(R.layout.list_comment_item, null);
			mHolder.tvContent = (TextView) view.findViewById(R.id.tvContent);
			mHolder.tvDate    = (TextView) view.findViewById(R.id.tvDate);
			mHolder.tvZan     = (TextView) view.findViewById(R.id.item_zan);
			mHolder.tvCai     = (TextView) view.findViewById(R.id.item_cai);

			view.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) view.getTag();
		}
		CommentEntity comment = getItem(position);

		if(!Helper.isEmpty(comment.getContent())){
			mHolder.tvContent.setText(comment.getContent());
		}

		if(!Helper.isEmpty(comment.getDate())){
			java.util.Date d = new Date(Long.parseLong(comment.getDate()) * 1000);
			mHolder.tvDate.setText(Helper.calcTimeDiff(d));
		}

		mHolder.tvZan.setText(context.getString(R.string.news_zan) + "(" + comment.getZan() + ")");
		mHolder.tvCai.setText(context.getString(R.string.news_cai) + "(" + comment.getCai() + ")");

		mHolder.tvZan.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				if(UserInfoManager.getInstance(context).getToken() == null){
					context.startActivity(new Intent(context,LoginActivity.class));
				}

				currentPostion = position;
				like(1);
			}
		});


		mHolder.tvCai.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				if(UserInfoManager.getInstance(context).getToken() == null){
					context.startActivity(new Intent(context,LoginActivity.class));
				}

				currentPostion = position;
				like(2);
			}
		});

		return view;
	}

	private void like(int type){  //zan :1  ,  cai : 2

		ApiService as = new ApiService(Api.API_COMMENT_ZAN);
		as.setMethod(com.topnews.helper.Constants.POST);
		as.setToken(UserInfoManager.getInstance(context).getToken());
		as.setPostParams(Keys.COMMENT_ID,commentList.get(currentPostion).getId());
		as.setPostParams(Keys.CLICK_TYPE,type + "");
		as.setPostParams(Keys.CLICK_VALUE,1 + "");
		as.execute(new ApiService.OnServiceListener() {

			@Override
			public void onReceivedData(JSONObject json) {
				// TODO Auto-generated method stub
				try {
					if (json.getInt(Keys.CODE) == 1) {

						if(json.getJSONObject(Keys.DATA).getString(Keys.CLICK_TYPE).equals("1")){
							commentList.get(currentPostion).setZan(commentList.get(currentPostion).getZan() + 1);
						}else{
							commentList.get(currentPostion).setCai(commentList.get(currentPostion).getCai() + 1);
						}

						notifyDataSetChanged();
					}else{
						Toast.makeText(context,json.getString(Keys.MSG),Toast.LENGTH_SHORT).show();
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

	static class ViewHolder {
		TextView tvContent,tvDate,tvZan,tvCai;
	}
}