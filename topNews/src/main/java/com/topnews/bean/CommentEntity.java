package com.topnews.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class CommentEntity implements Serializable {


	private static final long serialVersionUID = 8951570597303830846L;

	private String id;
	private String content;
	private int isGood;
	private String date;
	private String status;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getIsGood() {
		return isGood;
	}

	public void setIsGood(int isGood) {
		this.isGood = isGood;
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
}

