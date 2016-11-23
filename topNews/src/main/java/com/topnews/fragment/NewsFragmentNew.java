package com.topnews.fragment;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import com.topnews.R;
import com.topnews.adapter.NewsAdapterNew;
import com.topnews.bean.NewsEntityNew;
import com.topnews.helper.Api;
import com.topnews.helper.ApiService;
import com.topnews.helper.Constants;
import com.topnews.helper.Convert;
import com.topnews.helper.Keys;
import com.topnews.view.NewsListView;
import com.yalantis.pulltorefresh.library.PullToRefreshView;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

import io.saeid.fabloading.LoadingView;

public class NewsFragmentNew extends Fragment implements AbsListView.OnScrollListener{

	private View v;
	private NewsListView lvNews;
	private View footer;
	private NewsAdapterNew adapterNew;
	private ArrayList<NewsEntityNew> newsList = new ArrayList<NewsEntityNew>();

	private PullToRefreshView mPullToRefreshView;

	private LoadingView mLoadingView;

	private String newsType = Constants.NEWS_TYPE_TEXT;

	private int page = 1;
	private int visibleLastIndex;
	private boolean loadFinished = false;
	private boolean isLoading = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Bundle args = getArguments();
		newsType = args != null ? args.getString(Keys.NEWS_TYPE) : Constants.NEWS_TYPE_TEXT;
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		v = LayoutInflater.from(getActivity()).inflate(R.layout.news_fragment_new, null);
		initView();
		getNews();
		return v;
	}

	private void initView(){

		mLoadingView = (LoadingView) v.findViewById(R.id.loading_view);

//		boolean isLollipop = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
		boolean isLollipop = false;
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

		lvNews = (NewsListView) v.findViewById(R.id.lvNews);
		lvNews.setOnScrollListener(this);

		footer = LayoutInflater.from(getActivity()).inflate(R.layout.load_more,null);

		mPullToRefreshView = (PullToRefreshView) v.findViewById(R.id.pullToRefresh);
		mPullToRefreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
			@Override
			public void onRefresh() {
				refreshNews();
			}
		});
	}

	private void refreshNews(){
		loadFinished = false;
		page = 1;
		newsList.clear();
		getNews();
	}

	private void getNews(){
		ApiService as = null;

		if(newsType.equals(Constants.NEWS_TYPE_TEXT)){
			as = new ApiService(Api.API_TEXT_NEWS);
		}else if(newsType.equals(Constants.NEWS_TYPE_IMAGE)){
			as = new ApiService(Api.API_IMAGE_NEWS);
		}

		as.setMethod(Constants.GET);
		as.setGetParams(Keys.PAGE,page);
		as.execute(new ApiService.OnServiceListener() {

			@Override
			public void onReceivedData(JSONObject json) {
				// TODO Auto-generated method stub

				mPullToRefreshView.setRefreshing(false);
				lvNews.removeFooterView(footer);
				mLoadingView.pauseAnimation();
				mLoadingView.setVisibility(View.GONE);
				try {
					if (json.getInt(Keys.CODE) == 1) {

						ArrayList<NewsEntityNew> newsListTemp = new ArrayList<NewsEntityNew>();

						newsListTemp = Convert.convertToNewsList(json.getJSONObject(Keys.DATA).getJSONArray(Keys.DATA),newsType);

						if(newsListTemp.size() > 0){
							newsList.addAll(newsListTemp);

							if(adapterNew == null){
								adapterNew = new NewsAdapterNew(getActivity(),newsList);
								lvNews.setAdapter(adapterNew);
							}else{
								adapterNew.notifyDataSetChanged();
							}
						}

						int perPage = Integer.parseInt(json.getJSONObject(Keys.DATA).getString(Keys.PER_PAGE));

						if(newsListTemp.size() < perPage){
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
	public void onScrollStateChanged(AbsListView view, int scrollState) {

		int itemsLastIndex = adapterNew.getCount() - 1; // 数据集最后一项的索引
		if(newsList.size() > 0 && !isLoading && !loadFinished && itemsLastIndex == visibleLastIndex && scrollState == SCROLL_STATE_IDLE){
			//show footview and get news
			isLoading = true;
			lvNews.addFooterView(footer);
			page++;
			getNews();
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		visibleLastIndex = firstVisibleItem + visibleItemCount - 1;
	}
}
