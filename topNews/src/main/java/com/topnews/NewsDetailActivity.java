package com.topnews;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.topnews.adapter.CommentAdapter;
import com.topnews.adapter.NewsAdapterNew;
import com.topnews.base.BaseActivity;
import com.topnews.bean.CommentEntity;
import com.topnews.bean.NewsEntityNew;
import com.topnews.helper.Api;
import com.topnews.helper.ApiService;
import com.topnews.helper.Constants;
import com.topnews.helper.Convert;
import com.topnews.helper.Helper;
import com.topnews.helper.Keys;
import com.topnews.helper.UserInfoManager;
import com.topnews.tool.Options;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import io.saeid.fabloading.LoadingView;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

public class NewsDetailActivity extends BaseActivity implements AbsListView.OnScrollListener{

	private TextView title,tvTitle,tvContent,tvComment;
	private EditText etComment;
	private ListView lvComment;
	private CommentAdapter commentAdapter;
	private View header;
	private View footer;
	private LinearLayout llImage;

	private NewsEntityNew newsEntityNew;
	private String newsTitle;
	private String newsContent;

	private int page = 1;
	private int visibleLastIndex;
	private boolean loadFinished = false;
	private boolean isLoading = false;

	private LoadingView mLoadingView;

	private ArrayList<CommentEntity> commentList = new ArrayList<CommentEntity>();
	private int commentNum = 0;

	private ImageOptions imageOptions;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.details);
		setNeedBackGesture(true);//设置需要手势监听

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

		getData();
		initView();
		getComments();
	}

	/* 获取传递过来的数据 */
	private void getData() {
		newsEntityNew = (NewsEntityNew) getIntent().getSerializableExtra(Keys.NEWS);
		newsTitle     = newsEntityNew.getTitle();
		newsContent   = newsEntityNew.getBody();
	}

	private void initView() {

		initLoadingView();

		title     = (TextView) findViewById(R.id.title);
		title.setText(getString(R.string.news_detail));

		etComment   = (EditText) findViewById(R.id.etComment);

		lvComment   = (ListView) findViewById(R.id.lvComment);
		header      = LayoutInflater.from(this).inflate(R.layout.details_header,null);
		footer      = LayoutInflater.from(this).inflate(R.layout.load_more,null);
		tvTitle     = (TextView) header.findViewById(R.id.tvTitle);
		tvContent   = (TextView) header.findViewById(R.id.tvContent);
		tvComment   = (TextView) header.findViewById(R.id.tvComment);
		llImage     = (LinearLayout) header.findViewById(R.id.llImage);
		lvComment.addHeaderView(header);

		if(newsEntityNew.getType().equals(Constants.NEWS_TYPE_IMAGE) && newsEntityNew.getImageUrls().size() > 0){
			llImage.setVisibility(View.VISIBLE);
			ArrayList<String> imgUrlList = newsEntityNew.getImageUrls();
			for (int i = 0; i < imgUrlList.size(); i++){

//				ImageView imageView = new ImageView(this);
//				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//				params.setMargins(30,20,30,20);
//				imageView.setLayoutParams(params);
//				imageView.setImageResource(R.drawable.ic_launcher);
//				imageView.setScaleType(ImageView.ScaleType.FIT_XY);
//
//				imageLoader.displayImage(imgUrlList.get(i), imageView, Options.getListOptions());

				GifImageView gifImageView = new GifImageView(this);

				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
				params.setMargins(30,20,30,20);
				gifImageView.setLayoutParams(params);
				gifImageView.setImageResource(R.drawable.ic_launcher);
				gifImageView.setScaleType(ImageView.ScaleType.FIT_XY);
				gifImageView.setAdjustViewBounds(true);

				llImage.addView(gifImageView);

				showGif(gifImageView,imgUrlList.get(i));
			}
		}

		if(!Helper.isEmpty(newsTitle)){
			tvTitle.setText(newsTitle);
		}else{
			tvTitle.setVisibility(View.GONE);
		}

		if(!Helper.isEmpty(newsContent) && !newsEntityNew.getType().equals(Constants.NEWS_TYPE_IMAGE)){
			tvContent.setText(newsContent);
		}else{
			tvContent.setVisibility(View.GONE);
		}

		commentNum = newsEntityNew.getCommentNum();

		tvComment.setText(getString(R.string.news_comment) + "(" + commentNum + ")");

		etComment.setOnEditorActionListener(new TextView.OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

				if (actionId == EditorInfo.IME_ACTION_SEND) {
					if(!Helper.isEmpty(etComment.getText().toString().trim())){
						sendComment();
					}
				}

				return false;
			}
		});
	}

	private void sendComment(){
		if(Helper.isEmpty(UserInfoManager.getInstance(this).getToken())){
			startActivity(new Intent(this,LoginActivity.class));
		}

		ApiService as = new ApiService(Api.API_COMMENT_ADD);
		as.setMethod(Constants.POST);
		as.setPostParams(Keys.ARTICLE_ID,newsEntityNew.getId());
		as.setPostParams(Keys.ARTICLE_TYPE,newsEntityNew.getType());
		as.setPostParams(Keys.CONTENT,etComment.getText().toString().trim());
		as.setToken(UserInfoManager.getInstance(this).getToken());

		as.execute(new ApiService.OnServiceListener() {

			@Override
			public void onReceivedData(JSONObject json) {
				// TODO Auto-generated method stub

				mLoadingView.pauseAnimation();
				mLoadingView.setVisibility(View.GONE);
				try {
					if (json.getInt(Keys.CODE) == 1) {

						CommentEntity commentEntity = Convert.convertToComment(json.getJSONObject(Keys.DATA));

						if(commentEntity != null){
							commentList.add(0,commentEntity);
							commentAdapter.notifyDataSetChanged();
							commentNum++;
							tvComment.setText(getString(R.string.news_comment) + "(" + commentNum + ")");
							etComment.setText("");
						}
					}else{

					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			@Override
			public void OnPreExecute() {
				// TODO Auto-generated method stub
				mLoadingView.setVisibility(View.VISIBLE);
				mLoadingView.startAnimation();
			}

			@Override
			public void OnFailed() {
				// TODO Auto-generated method stub
			}

		});
	}

	private void getComments(){

		ApiService as = new ApiService(Api.API_COMMENT_LIST);
		as.setMethod(Constants.POST);
		as.setGetParams(Keys.PAGE,page);
		as.setPostParams(Keys.ARTICLE_ID,newsEntityNew.getId());
		as.setPostParams(Keys.ARTICLE_TYPE,newsEntityNew.getType());
		as.setPostParams(Keys.STATUS,"approved");
		as.execute(new ApiService.OnServiceListener() {

			@Override
			public void onReceivedData(JSONObject json) {
				// TODO Auto-generated method stub


				lvComment.removeFooterView(footer);
				mLoadingView.pauseAnimation();
				mLoadingView.setVisibility(View.GONE);
				try {
					if (json.getInt(Keys.CODE) == 1) {

						ArrayList<CommentEntity> commentListTemp = Convert.convertToCommentList(json.getJSONObject(Keys.DATA).getJSONArray(Keys.DATA));

						commentList.addAll(commentListTemp);

						if(commentAdapter == null){
							commentAdapter = new CommentAdapter(NewsDetailActivity.this,commentList);
							lvComment.setAdapter(commentAdapter);
						}else{
							commentAdapter.notifyDataSetChanged();
						}

						int perPage = Integer.parseInt(json.getJSONObject(Keys.DATA).getString(Keys.PER_PAGE));

						if(commentListTemp.size() < perPage){
							//loadfinished
							loadFinished = true;
						}

					}else{

					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				finally {
					isLoading = false;
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

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {

		int itemsLastIndex = commentAdapter.getCount() - 1; // 数据集最后一项的索引
		if(commentList.size() > 0 && !isLoading && !loadFinished && itemsLastIndex == visibleLastIndex && scrollState == SCROLL_STATE_IDLE){
			//show footview and get news
			isLoading = true;
			lvComment.addFooterView(footer);
			page++;
			getComments();
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		visibleLastIndex = firstVisibleItem + visibleItemCount - 1;
	}

	private void initLoadingView(){
		mLoadingView = (LoadingView) findViewById(R.id.loading_view);

		boolean isLollipop = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
		int marvel_1 = isLollipop ? R.drawable.marvel_1_lollipop : R.drawable.marvel_1;
		int marvel_2 = isLollipop ? R.drawable.marvel_2_lollipop : R.drawable.marvel_2;
		int marvel_3 = isLollipop ? R.drawable.marvel_3_lollipop : R.drawable.marvel_3;
		int marvel_4 = isLollipop ? R.drawable.marvel_4_lollipop : R.drawable.marvel_4;
		mLoadingView.addAnimation(Color.parseColor("#FFD200"), marvel_1,
				LoadingView.FROM_LEFT);
		mLoadingView.addAnimation(Color.parseColor("#2F5DA9"), marvel_2,
				LoadingView.FROM_TOP);
		mLoadingView.addAnimation(Color.parseColor("#FF4218"), marvel_3,
				LoadingView.FROM_RIGHT);
		mLoadingView.addAnimation(Color.parseColor("#C7E7FB"), marvel_4,
				LoadingView.FROM_BOTTOM);

		mLoadingView.startAnimation();
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
