package com.topnews.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.topnews.LoginActivity;
import com.topnews.MainActivityNew;
import com.topnews.R;
import com.topnews.TestActivity;
import com.topnews.bean.NewsEntityNew;
import com.topnews.helper.Api;
import com.topnews.helper.ApiService;
import com.topnews.helper.Convert;
import com.topnews.helper.Helper;
import com.topnews.helper.Keys;
import com.topnews.helper.UserInfoManager;
import com.topnews.tool.Constants;
import com.topnews.tool.Options;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.common.util.DensityUtil;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

public class NewsAdapterNew extends BaseAdapter {

	protected ImageLoader imageLoader = ImageLoader.getInstance();
	private Context ctx;
	private ArrayList<NewsEntityNew> newsList = new ArrayList<NewsEntityNew>();
	/** 弹出的更多选择框  */
	private PopupWindow popupWindow;
	DisplayImageOptions options;
	private ImageOptions imageOptions;

	private int currentPostion;

	public NewsAdapterNew(Context ctx, ArrayList<NewsEntityNew> newsList) {
		this.ctx = ctx;
		this.newsList = newsList;
		options = Options.getListOptions();
		initPopWindow();

		imageOptions = new ImageOptions.Builder()
//				.setRadius(DensityUtil.dip2px(5))
				// 如果ImageView的大小不是定义为wrap_content, 不要crop.
//				.setCrop(true) // 很多时候设置了合适的scaleType也不需要它.
				// 加载中或错误图片的ScaleType
				//.setPlaceholderScaleType(ImageView.ScaleType.MATRIX)
				.setLoadingDrawableId(R.drawable.ic_launcher)
				.setFailureDrawableId(R.drawable.ic_launcher)
				.setUseMemCache(true)
				.build();
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return newsList == null ? 0 : newsList.size();
	}

	@Override
	public NewsEntityNew getItem(int position) {
		// TODO Auto-generated method stub
		if (newsList != null && newsList.size() != 0) {
			return newsList.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder mHolder;
		View view = convertView;
		if (view == null) {
			view = LayoutInflater.from(ctx).inflate(R.layout.list_item_new, null);
			mHolder = new ViewHolder();
			mHolder.item_layout = (LinearLayout)view.findViewById(R.id.item_layout);
			mHolder.comment_layout = (RelativeLayout)view.findViewById(R.id.comment_layout);
			mHolder.item_title = (TextView)view.findViewById(R.id.item_title);
			mHolder.item_zan = (TextView)view.findViewById(R.id.item_zan);
			mHolder.item_cai = (TextView)view.findViewById(R.id.item_cai);
			mHolder.list_item_local = (TextView)view.findViewById(R.id.list_item_local);
			mHolder.comment_count = (TextView)view.findViewById(R.id.comment_count);
			mHolder.publish_time = (TextView)view.findViewById(R.id.publish_time);
			mHolder.item_abstract = (TextView)view.findViewById(R.id.item_abstract);
			mHolder.alt_mark = (ImageView)view.findViewById(R.id.alt_mark);
			mHolder.right_image = (ImageView)view.findViewById(R.id.right_image);
			mHolder.item_image_layout = (LinearLayout)view.findViewById(R.id.item_image_layout);
			mHolder.item_image_0 = (ImageView)view.findViewById(R.id.item_image_0);
			mHolder.item_image_1 = (ImageView)view.findViewById(R.id.item_image_1);
			mHolder.item_image_2 = (ImageView)view.findViewById(R.id.item_image_2);
			mHolder.large_image  = (GifImageView)view.findViewById(R.id.large_image);
			mHolder.popicon = (ImageView)view.findViewById(R.id.popicon);
			mHolder.comment_content = (TextView)view.findViewById(R.id.comment_content);
			mHolder.right_padding_view = (View)view.findViewById(R.id.right_padding_view);
			//头部的日期部分
			mHolder.layout_list_section = (LinearLayout)view.findViewById(R.id.layout_list_section);
			mHolder.section_text = (TextView)view.findViewById(R.id.section_text);
			mHolder.section_day = (TextView)view.findViewById(R.id.section_day);

			view.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) view.getTag();
		}
		//获取position对应的数据
		NewsEntityNew news = getItem(position);
		if(Helper.isEmpty(news.getTitle())){
			mHolder.item_title.setText(news.getBody());
		}else{
			mHolder.item_title.setText(news.getTitle());
		}

		mHolder.item_zan.setText(ctx.getString(R.string.news_zan) + " (" + news.getZan() + ")  ");
		mHolder.item_cai.setText(ctx.getString(R.string.news_cai) + " (" + news.getCai() + ")");
		mHolder.comment_count.setText(ctx.getString(R.string.news_comment) + " " + news.getCommentNum());

		java.util.Date d = new Date(Long.parseLong(news.getDate()) * 1000);
		mHolder.publish_time.setText(Helper.calcTimeDiff(d));

		List<String> imgUrlList = news.getImageUrls();
		mHolder.popicon.setVisibility(View.VISIBLE);
		mHolder.comment_count.setVisibility(View.VISIBLE);
		mHolder.right_padding_view.setVisibility(View.VISIBLE);
		if(imgUrlList !=null && imgUrlList.size() !=0){
			if(imgUrlList.size() == 1){
				mHolder.item_image_layout.setVisibility(View.GONE);
				//是否是大图
				if(news.isLarge()){
					mHolder.large_image.setVisibility(View.VISIBLE);
					mHolder.right_image.setVisibility(View.GONE);
//					imageLoader.displayImage(imgUrlList.get(0), mHolder.large_image, options);
//					mHolder.popicon.setVisibility(View.GONE);
//					mHolder.comment_count.setVisibility(View.GONE);

					//show gif
					showGif(mHolder.large_image,imgUrlList.get(0));

					mHolder.right_padding_view.setVisibility(View.GONE);
				}else{
					mHolder.large_image.setVisibility(View.GONE);
					mHolder.right_image.setVisibility(View.VISIBLE);
					imageLoader.displayImage(imgUrlList.get(0), mHolder.right_image, options);
				}
			}else{
				mHolder.large_image.setVisibility(View.GONE);
				mHolder.right_image.setVisibility(View.GONE);
				mHolder.item_image_layout.setVisibility(View.VISIBLE);
				imageLoader.displayImage(imgUrlList.get(0), mHolder.item_image_0, options);
				imageLoader.displayImage(imgUrlList.get(1), mHolder.item_image_1, options);
				imageLoader.displayImage(imgUrlList.get(2), mHolder.item_image_2, options);
			}
		}else{
			mHolder.large_image.setVisibility(View.GONE);
			mHolder.right_image.setVisibility(View.GONE);
			mHolder.item_image_layout.setVisibility(View.GONE);
		}
//		int markResID = getAltMarkResID(news.getMark(),news.getCollectStatus());
//		if(markResID != -1){
//			mHolder.alt_mark.setVisibility(View.VISIBLE);
//			mHolder.alt_mark.setImageResource(markResID);
//		}else{
//			mHolder.alt_mark.setVisibility(View.GONE);
//		}
		//判断该新闻概述是否为空
//		if (!TextUtils.isEmpty(news.getNewsAbstract())) {
//			mHolder.item_abstract.setVisibility(View.VISIBLE);
//			mHolder.item_abstract.setText(news.getNewsAbstract());
//		} else {
//			mHolder.item_abstract.setVisibility(View.GONE);
//		}
		//判断该新闻是否是特殊标记的，推广等，为空就是新闻
		if(news.getType() == com.topnews.helper.Constants.NEWS_TYPE_AD){
			//广告
			mHolder.list_item_local.setVisibility(View.VISIBLE);
			mHolder.list_item_local.setText(news.getBody());
		}else{
			//新闻
			mHolder.list_item_local.setVisibility(View.GONE);
		}
		//判断评论字段是否为空，不为空显示对应布局
		if(!TextUtils.isEmpty(news.getComment())){
			//news.getLocal() != null &&
			mHolder.comment_layout.setVisibility(View.VISIBLE);
			mHolder.comment_content.setText(news.getComment());
		}else{
			mHolder.comment_layout.setVisibility(View.GONE);
		}
		//判断该新闻是否已读
//		if(!news.getReadStatus()){
//			mHolder.item_layout.setSelected(true);
//		}else{
//			mHolder.item_layout.setSelected(false);
//		}
		//设置+按钮点击效果
		mHolder.popicon.setOnClickListener(new popAction(position));

		return view;
	}

	static class ViewHolder {
		LinearLayout item_layout;
		//title
		TextView item_title;
		//赞
		TextView item_zan;
		//踩
		TextView item_cai;
		//类似推广之类的标签
		TextView list_item_local;
		//评论数量
		TextView comment_count;
		//发布时间
		TextView publish_time;
		//新闻摘要
		TextView item_abstract;
		//右上方TAG标记图片
		ImageView alt_mark;
		//右边图片
		ImageView right_image;
		//3张图片布局
		LinearLayout item_image_layout; //3张图片时候的布局
		ImageView item_image_0;
		ImageView item_image_1;
		ImageView item_image_2;
		//大图的图片的话布局
		GifImageView large_image;
		//pop按钮
		ImageView popicon;
		//评论布局
		RelativeLayout comment_layout;
		TextView comment_content;
		//paddingview
		View right_padding_view;
		
		//头部的日期部分
		LinearLayout layout_list_section;
		TextView section_text;
		TextView section_day;
	}
	/** 根据属性获取对应的资源ID  */
	public int getAltMarkResID(int mark,boolean isfavor){
		if(isfavor){
			return R.drawable.ic_mark_favor;
		}
		switch (mark) {
		case Constants.mark_recom:
			return R.drawable.ic_mark_recommend;
		case Constants.mark_hot:
			return R.drawable.ic_mark_hot;
		case Constants.mark_frist:
			return R.drawable.ic_mark_first;
		case Constants.mark_exclusive:
			return R.drawable.ic_mark_exclusive;
		case Constants.mark_favor:
			return R.drawable.ic_mark_favor;
		default:
			break;
		}
		return -1;
	}
	
	/** popWindow 关闭按钮 */
	private ImageView btn_pop_close;
	
	/**
	 * 初始化弹出的pop
	 * */
	private void initPopWindow() {
		View popView = LayoutInflater.from(ctx).inflate(R.layout.list_item_pop, null);

		LinearLayout llLike = (LinearLayout) popView.findViewById(R.id.llLike);
		LinearLayout llDislike = (LinearLayout) popView.findViewById(R.id.llDislike);

		llLike.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				popupWindow.dismiss();
				newsList.get(currentPostion).setZan(newsList.get(currentPostion).getZan() + 1);
				like(1);
			}
		});

		llDislike.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				popupWindow.dismiss();
				newsList.get(currentPostion).setZan(newsList.get(currentPostion).getCai() + 1);
				like(2);
			}
		});

		popupWindow = new PopupWindow(popView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		popupWindow.setBackgroundDrawable(new ColorDrawable(0));
		//设置popwindow出现和消失动画
		popupWindow.setAnimationStyle(R.style.PopMenuAnimation);
		btn_pop_close = (ImageView) popView.findViewById(R.id.btn_pop_close);
	}
	
	/** 
	 * 显示popWindow
	 * */
	public void showPop(View parent, int x, int y,int postion) {
		//设置popwindow显示位置
		popupWindow.showAtLocation(parent, 0, x, y);
		//获取popwindow焦点
		popupWindow.setFocusable(true);
		//设置popwindow如果点击外面区域，便关闭。
		popupWindow.setOutsideTouchable(true);
		popupWindow.update();
		if (popupWindow.isShowing()) {
			
		}

		currentPostion = postion;

		btn_pop_close.setOnClickListener(new OnClickListener() {
			public void onClick(View paramView) {
				popupWindow.dismiss();
			}
		});
	}
	
	/** 
	 * 每个ITEM中more按钮对应的点击动作
	 * */
	public class popAction implements OnClickListener{
		int position;
		public popAction(int position){
			this.position = position;
		}
		@Override
		public void onClick(View v) {
			int[] arrayOfInt = new int[2];
			//获取点击按钮的坐标
			v.getLocationOnScreen(arrayOfInt);
	        int x = arrayOfInt[0];
	        int y = arrayOfInt[1];
	        showPop(v, x , y, position);
		}
	}

	private void like(int type){  //zan :1  ,  cai : 2

		if(UserInfoManager.getInstance(ctx).getToken() == null){
//			Toast.makeText(ctx,"请登录.",Toast.LENGTH_SHORT).show();
			ctx.startActivity(new Intent(ctx,LoginActivity.class));
		}

		ApiService as = new ApiService(Api.API_ZAN);
		as.setMethod(com.topnews.helper.Constants.POST);
		as.setToken(UserInfoManager.getInstance(ctx).getToken());
		as.setPostParams(Keys.ARTICLE_ID,newsList.get(currentPostion).getId());
		as.setPostParams(Keys.CLICK_TYPE,type + "");
		as.setPostParams(Keys.CLICK_VALUE,1 + "");
		as.setPostParams(Keys.ARTICLE_TYPE,newsList.get(currentPostion).getType());
		as.execute(new ApiService.OnServiceListener() {

			@Override
			public void onReceivedData(JSONObject json) {
				// TODO Auto-generated method stub
				try {
					if (json.getInt(Keys.CODE) == 1) {
						notifyDataSetChanged();
					}else{
						Toast.makeText(ctx,json.getString(Keys.MSG),Toast.LENGTH_SHORT).show();
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


	private void showGif(final GifImageView gifView,String url){
		x.image().loadFile(url,imageOptions, new Callback.CacheCallback<File>() {
			@Override
			public void onSuccess(File result) {

			}

			@Override
			public void onError(Throwable ex, boolean isOnCallback) {

			}

			@Override
			public void onCancelled(CancelledException cex) {

			}

			@Override
			public void onFinished() {

			}

			@Override
			public boolean onCache(File result) {

				GifDrawable gifFrom = null;
				try {
					gifFrom = new GifDrawable(result.getAbsolutePath());
					gifView.setImageDrawable(gifFrom);
				} catch (IOException e) {
					e.printStackTrace();
				}

				return false;
			}
		});
	}
}
