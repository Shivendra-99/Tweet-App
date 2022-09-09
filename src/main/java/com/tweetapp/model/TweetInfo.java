package com.tweetapp.model;

import java.util.Date;
import java.util.List;

public class TweetInfo {
	private String tweetId;
	private String tweet;
	private List<Comment> comment;
	private Date dateAndTimeOfTweet;
	private List<UserDetailDto> like;
	
	public TweetInfo() {
		super();
	}
	public TweetInfo(String tweetId, String tweet, List<Comment> comment, Date dateAndTimeOfTweet, List<UserDetailDto> like) {
		super();
		this.tweetId = tweetId;
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
