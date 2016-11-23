package com.topnews.helper;

import com.topnews.bean.NewsEntityNew;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by iapps on 11/9/16.
 */

public class Convert {

    public static ArrayList<NewsEntityNew> convertToNewsList(JSONArray data,String newsType){
        ArrayList<NewsEntityNew> newsList = new ArrayList<NewsEntityNew>();

        for (int i = 0; i < data.length(); i ++){
            try {
                JSONObject newsJson = data.getJSONObject(i);
                NewsEntityNew news = new NewsEntityNew();

                if(newsType.equals(Constants.NEWS_TYPE_TEXT)){
                    news.setType("text");
                }else{
                    news.setType("image");
                }


                if(!newsJson.isNull(Keys.ID)){
                    news.setId(newsJson.getString(Keys.ID));
                }

                if(!newsJson.isNull(Keys.BODY)){
                    news.setBody(newsJson.getString(Keys.BODY));
                }

                if(!newsJson.isNull(Keys.TITLE)){
                    news.setTitle(newsJson.getString(Keys.TITLE));
                }

                if(!newsJson.isNull(Keys.CREATED_AT)){
                    news.setDate(newsJson.getString(Keys.CREATED_AT));
                }

                if(!newsJson.isNull(Keys.STATUS)){
                    news.setStatus(newsJson.getString(Keys.STATUS));
                }

                if(!newsJson.isNull(Keys.CLICK_UP)){
                    news.setZan(newsJson.getInt(Keys.CLICK_UP));
                }

                if(!newsJson.isNull(Keys.CLICK_DOWN)){
                    news.setCai(newsJson.getInt(Keys.CLICK_DOWN));
                }

                if(!newsJson.isNull(Keys.COMMENT_NUM)){
                    news.setCommentNum(newsJson.getInt(Keys.COMMENT_NUM));
                }

//                if(!newsJson.isNull(Keys.NEWS_TYPE)){
                if(!newsJson.isNull(Keys.TITLE)){
//                    news.setType(newsJson.getString(Keys.NEWS_TYPE));

//                    if(news.getType().equals(Constants.NEWS_TYPE_IMAGE)){
                        //set image list
                        ArrayList<String> imageUrls = new ArrayList<String>();
                        JSONArray urls = new JSONArray(news.getBody());
                        for (int j = 0; j < urls.length(); j++){
                            imageUrls.add("http://top-news.oss-cn-shanghai.aliyuncs.com/" + urls.getString(j));
                        }
                        news.setImageUrls(imageUrls);

                        if(imageUrls.size() == 1){
                            news.setLarge(true);
                        }
//                    }
                }

                newsList.add(news);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return newsList;
    }
}
