package com.topnews.helper;

import com.topnews.bean.CommentEntity;
import com.topnews.bean.NewsEntityNew;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by iapps on 11/9/16.
 */

public class Convert {

    public static ArrayList<CommentEntity> convertToCommentList(JSONArray data){
        ArrayList<CommentEntity> commentList = new ArrayList<CommentEntity>();

        for (int i = 0; i < data.length(); i ++){
            try {
                JSONObject commentJson = data.getJSONObject(i);
                CommentEntity comment = new CommentEntity();

                if(!commentJson.isNull(Keys.ID)){
                    comment.setId(commentJson.getString(Keys.ID));
                }

                if(!commentJson.isNull(Keys.CONTENT)){
                    comment.setContent(commentJson.getString(Keys.CONTENT));
                }

                if(!commentJson.isNull(Keys.IS_GOOD)){
                    comment.setIsGood(commentJson.getInt(Keys.IS_GOOD));
                }

                if(!commentJson.isNull(Keys.CREATED_AT)){
                    comment.setDate(commentJson.getString(Keys.CREATED_AT));
                }

                if(!commentJson.isNull(Keys.STATUS)){
                    comment.setStatus(commentJson.getString(Keys.STATUS));
                }

                commentList.add(comment);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return commentList;
    }

    public static CommentEntity convertToComment(JSONObject data){

            try {

                CommentEntity comment = new CommentEntity();

                if(!data.isNull(Keys.ID)){
                    comment.setId(data.getString(Keys.ID));
                }

                if(!data.isNull(Keys.CONTENT)){
                    comment.setContent(data.getString(Keys.CONTENT));
                }

                if(!data.isNull(Keys.IS_GOOD)){
                    comment.setIsGood(data.getInt(Keys.IS_GOOD));
                }

                if(!data.isNull(Keys.CREATED_AT)){
                    comment.setDate(data.getString(Keys.CREATED_AT));
                }

                if(!data.isNull(Keys.STATUS)){
                    comment.setStatus(data.getString(Keys.STATUS));
                }

                return comment;
            } catch (JSONException e) {
                e.printStackTrace();
            }

        return null;
    }

    public static ArrayList<NewsEntityNew> convertToNewsList(JSONArray data,String newsType){
        ArrayList<NewsEntityNew> newsList = new ArrayList<NewsEntityNew>();

        for (int i = 0; i < data.length(); i ++){
            try {
                JSONObject newsJson = data.getJSONObject(i);
                NewsEntityNew news = new NewsEntityNew();

                news.setType(newsType);

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

                if(newsType.equals(Constants.NEWS_TYPE_IMAGE)){

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
                }

                newsList.add(news);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return newsList;
    }
}
