package com.tweetapp.service;

import java.util.List;

import com.tweetapp.model.Login;
import com.tweetapp.model.Tweet;
import com.tweetapp.model.TweetInfo;
import com.tweetapp.model.UserDetail;

public interface TweetAppService{
	UserDetail SaveUser(UserDetail userDetail);
	List<UserDetail> getAllUser();
	List<Tweet> getAllTweet();
	UserDetail userLogin(Login login);
	List<UserDetail> getAllUserByName(String name);
	List<TweetInfo> getTweetByUserName(String userid);
	UserDetail getUserById(String userId);
}
