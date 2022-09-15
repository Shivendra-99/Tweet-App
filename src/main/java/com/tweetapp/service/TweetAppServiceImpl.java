package com.tweetapp.service;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tweetapp.model.Comment;
import com.tweetapp.model.Login;
import com.tweetapp.model.Tweet;
import com.tweetapp.model.TweetInfo;
import com.tweetapp.model.UserDetail;
import com.tweetapp.model.UserDetailDto;
import com.tweetapp.repository.TweetAppRepository;
import com.tweetapp.repository.commentRepository;
import com.tweetapp.repository.tweetRepository;

@Service
public class TweetAppServiceImpl implements TweetAppService{
	Logger log=LoggerFactory.getLogger(TweetAppServiceImpl.class);
	
	
	@Autowired
	private TweetAppRepository tweetApp;
	
	@Autowired
	private tweetRepository tweetrepo;
	
	@Autowired
	private commentRepository commentRepo;
	
	@Autowired
	private ModelMapper mapper;
	
	@Override
	public UserDetail SaveUser(UserDetail userDetail) {
		List<UserDetail> list=tweetApp.findAll();
		for(UserDetail user1:list) {
			if(user1.getEmail().equals(userDetail.getEmail())) {
				log.error("User email id already register");
				return new UserDetail();
			}
		}
		List<TweetInfo> twe=new ArrayList<>();
		userDetail.setTweet(twe);
		return tweetApp.save(userDetail);
	}

	@Override
	public List<UserDetail> getAllUser() {
		return tweetApp.findAll();
	}

	@Override
	public List<Tweet> getAllTweet() {
		return tweetrepo.findAll();
	}

	@Override
	public UserDetail userLogin(Login login) {
		List<UserDetail>list=tweetApp.findAll();
		for(UserDetail userDetail:list) {
			if(userDetail.getPassword().equals(login.getPassword()) && userDetail.getEmail().equals(login.getEmail())) {
				return userDetail;
			}
		}
		return null;
	}
	@Override
	public List<String> getAllUserByName(String name) {
        List<String> list=new ArrayList<>();
        List<UserDetail> userDetails=getAllUser();
        for(UserDetail userDetail:userDetails) {
        	if(userDetail.getFirstName().toLowerCase().contains(name.toLowerCase()) || userDetail.getLastName().toLowerCase().contains(name.toLowerCase())) {
        		list.add(userDetail.getFirstName()+" "+userDetail.getLastName());
        	}
        }
		return list;
	}
	@Override
	public List<TweetInfo> getTweetByUserName(String userId) {
		List<UserDetail> list=getAllUser();
		for(UserDetail userDetail:list) {
			if(userDetail.getUserId().equals(userId)) {
				return userDetail.getTweet();
			}
		}
		return new ArrayList<>();
	}

	@Override
	public UserDetail getUserById(String userId) {
		List<UserDetail> list=getAllUser();
		for(UserDetail userDetail:list) {
			if(userDetail.getUserId().equals(userId)) {
				return userDetail;
			}
		}
		return null;
	}
	
	public Tweet postTweet(String userID,Tweet tweet) {
		UserDetail userDetail=getUserById(userID);
		if(userDetail==null) {
			return null;
		}
			tweet.setUserId(userDetail.getUserId());
			tweet.setUserName(userDetail.getFirstName()+" "+userDetail.getLastName());
			List<UserDetailDto> list=new ArrayList<>();
			List<Comment> com=new ArrayList<>();
			tweet.setLike(list);
			tweet.setComment(com);
			log.info(" userid and name added sccessfully in tweet ");
			Tweet tw=tweetrepo.save(tweet);
			TweetInfo info=mapper.map(tw, TweetInfo.class);  //mapping from one class object to other class object 
			userDetail.getTweet().add(info);
           tweetApp.save(userDetail);
           log.info("Tweet posted sccessfully");
			return tw;
	}
	public Tweet deleteTweetByTweetIdAndUserId(String userId,String tweetId) {
		UserDetail userDetail= getUserById(userId);
		List<TweetInfo> tweetInf=getTweetByUserName(userId);
		TweetInfo info=new TweetInfo();
		for(TweetInfo tweetInfo:tweetInf) {
			if(tweetId.equals(tweetInfo.getTweetId())) {
				info=tweetInfo;
				break;
			}
		}
		if(info!=null) {
		  tweetInf.remove(info);
		  userDetail.setTweet(tweetInf);
		  tweetApp.save(userDetail);
		}
		List<Tweet> list=getAllTweet();
		Tweet twe=new Tweet();
		for(Tweet tw:list) {
			if(tw.getTweetId().equals(tweetId)) {
				twe=tw;
			    tweetrepo.delete(tw);
				break;
			}
		}
		return twe;
	}
	public Tweet updateTweetByTweetIdAndUserId(String userId,String tweetId,Tweet post) {
		UserDetail userDetail= getUserById(userId);
		List<TweetInfo> tweetInf=getTweetByUserName(userId);
		TweetInfo info=new TweetInfo();
		for(TweetInfo tweetInfo:tweetInf) {
			if(tweetId.equals(tweetInfo.getTweetId())) {
				info=tweetInfo;
				log.info("We got the tweet");
				break;
			}
		}
		if(info!=null) {
		  info.setTweet(post.getTweet());
		  log.info("We got the tweet");
		  userDetail.setTweet(tweetInf);
		  tweetApp.save(userDetail);
		}
		List<Tweet> list=getAllTweet();
		Tweet twe=new Tweet();
		for(Tweet tw:list) {
			if(tw.getTweetId().equals(tweetId)) {
				twe=tw;
				log.info("We got the tweet");
				tw.setTweet(post.getTweet());
			    tweetrepo.save(tw);
				break;
			}
		}
		return twe;
	}
	
	
	public Tweet getTweetById(String tweetid) {
		List<Tweet> twe=getAllTweet();
		log.info("Got All Tweet");
		for(Tweet tweet:twe) {
			if(tweet.getTweetId().equals(tweetid)) {
				log.info("Got tweet by tweetId");
				return tweet;
			}
		}
		log.info("No Tweet found with this tweetId");
		return null;
	}
	
	public Tweet giveLikeToPost(String userid,String tweetid) {
		UserDetail userDetail=getUserById(userid);
		Tweet tweet=getTweetById(tweetid);
		if(userDetail==null || tweet==null){
			log.info("User or Tweet not found");
			return null;
		}
		for(UserDetailDto dto:tweet.getLike()) {
			if(dto.getUserId().equals(userDetail.getUserId())) {
				tweet.getLike().remove(dto);
				return null;
			}
		}
		UserDetailDto dto=mapper.map(userDetail,UserDetailDto.class);	//mapping from userDetails class to UserDetailsDto	
		tweet.getLike().add(dto);
		tweetrepo.save(tweet);
		log.info("Found the Tweet Info Updated");
		UserDetail tweetUser=getUserById(tweet.getUserId());
				List<TweetInfo> twe=tweetUser.getTweet();
		for(TweetInfo in:twe) {
			if(in.getTweetId().equals(tweetid)) {
				in.getLike().add(dto);
				tweetApp.save(tweetUser);
				log.info("Found the user Info Updated");
				return tweet;
			}
		}
		return null;
	}
	public Comment postComment(String userid,String tweetid,Comment comment) {
		UserDetail user=getUserById(userid);
		Tweet tweet=getTweetById(tweetid);
		if(user==null || tweet==null) {
			return new Comment();
		}
		UserDetailDto dto=mapper.map(user,UserDetailDto.class); //mapping from userDetails class to UserDetailsDto
		log.info("Comment method started");
		Comment comm =new Comment();
		comm.setComment(comment.getComment());
		comm.setUserDetailDto(dto);
		commentRepo.save(comm);
		log.info(comment.getComment());
		tweet.getComment().add(comm);
		Tweet twee=tweetrepo.save(tweet);
		TweetInfo info=mapper.map(twee,TweetInfo.class); //mapping from Tweet to TweetInfo
		user.getTweet().add(info);
		tweetApp.save(user);
		log.info("Data saved in tweet");
		return comm;
	}
}
