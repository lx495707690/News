package com.topnews.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.topnews.R;
import com.topnews.bean.CommentEntity;
import com.topnews.helper.Helper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CommentAdapter extends BaseAdapter {
	private Context context;
	public List<CommentEntity> commentList = new ArrayList<>();


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
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder mHolder;
		View view = convertView;
		if (view == null) {
			mHolder = new ViewHolder();
			view = LayoutInflater.from(context).inflate(R.layout.list_comment_item, null);
			mHolder.tvContent = (TextView) view.findViewById(R.id.tvContent);
			mHolder.tvDate    = (TextView) view.findViewById(R.id.tvDate);

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

		return view;
	}

	static class ViewHolder {
		TextView tvContent,tvDate;
	}
}