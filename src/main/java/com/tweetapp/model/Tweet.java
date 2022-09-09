package com.tweetapp.model;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Tweet {
	@Id
	private String tweetId;
	private String userName;
	private String userId;
	private String tweet;
	private List<Comment> comment;
	private Date dateAndTimeOfTweet;
	private List<UserDetailDto> like;
	
	public Tweet() {
		super();
	}

	public Tweet(String tweetId, String userName, String tweet, List<Comment> comment, Date dateAndTimeOfTweet,
			List<UserDetailDto> like) {
		super();
		this.tweetId = tweetId;
		this.userName = userName;
		this.tweet = tweet;
		this.comment = comment;
		this.dateAndTimeOfTweet = dateAndTimeOfTweet;
		this.like = like;
	}

	public String getTweetId() {
		return tweetId;
	}

	public void setTweetId(String tweetId) {
		this.tweetId = tweetId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getTweet() {
		return tweet;
	}

	public void setTweet(String tweet) {
		this.tweet = tweet;
	}

	public List<Comment> getComment() {
		return comment;
	}

	public void setComment(List<Comment> comment) {
		this.comment = comment;
	}

	public Date getDateAndTimeOfTweet() {
		return dateAndTimeOfTweet;
	}

	public void setDateAndTimeOfTweet(Date dateAndTimeOfTweet) {
		this.dateAndTimeOfTweet = dateAndTimeOfTweet;
	}

	public List<UserDetailDto> getLike() {
		return like;
	}

	public void setLike(List<UserDetailDto> like) {
		this.like = like;
	}
		
}
