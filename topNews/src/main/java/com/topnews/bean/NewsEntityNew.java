package com.topnews.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class NewsEntityNew implements Serializable {

	private static final long serialVersionUID = 8263672832998693268L;

	private String id;
	private String title;
	private String body;
	private String date;
	private String status;
	private String comment;
	private String type;
	private ArrayList<String> imageUrls = new ArrayList<String>();
	private boolean isLarge = false;
	private int zan; //赞的数量
	private int cai;//踩的数量
	private int commentNum;//评论的数量

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public boolean isLarge() {
		return isLarge;
	}

	public void setLarge(boolean large) {
		isLarge = large;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public ArrayList<String> getImageUrls() {
		return imageUrls;
	}

	public void setImageUrls(ArrayList<String> imageUrls) {
		this.imageUrls = imageUrls;
	}

	public int getZan() {
		return zan;
	}

	public void setZan(int zan) {
		this.zan = zan;
	}

	public int getCai() {
		return cai;
	}

	public void setCai(int cai) {
		this.cai = cai;
	}

	public int getCommentNum() {
		return commentNum;
	}

	public void setCommentNum(int commentNum) {
		this.commentNum = commentNum;
	}
}

