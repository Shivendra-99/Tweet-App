package com.tweetapp.service;

import java.util.ArrayList;
import java.util.List;

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
	
	//@Autowired
	//private ModelMappermode
	
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
	public List<UserDetail> getAllUserByName(String name) {
        List<UserDetail> list=new ArrayList<>();
        List<UserDetail> userDetails=getAllUser();
        for(UserDetail userDetail:userDetails) {
        	if(userDetail.getFirstName().contains(name) || userDetail.getLastName().contains(name)) {
        		list.add(userDetail);
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
			TweetInfo info=new TweetInfo(tw.getTweetId(),tw.getTweet(),tw.getComment(),tw.getDateAndTimeOfTweet(),tw.getLike());
			userDetail.getTweet().add(info);
           tweetApp.save(userDetail);
           log.info("Tweet save sccessfully");
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
	public Tweet getTweetById(String tweetid) {
		List<Tweet> twe=getAllTweet();
		for(Tweet tweet:twe) {
			if(tweet.getTweetId().equals(tweetid)) {
				return tweet;
			}
		}
		return null;
	}
	
	public Tweet giveLikeToPost(String userid,String tweetid) {
		UserDetail userDetail=getUserById(userid);
		Tweet tweet=getTweetById(tweetid);
		log.info("Found the user Info "+userDetail);
		log.info("Found the Tweet Info "+tweet);
		if(userDetail==null || tweet==null){
			return null;
		}
		
		for(UserDetailDto dto:tweet.getLike()) {
			if(dto.getUserId().equals(userDetail.getUserId())) {
				tweet.getLike().remove(dto);
				return null;
			}
		}
		UserDetailDto dto=new UserDetailDto(userDetail.getUserId(),userDetail.getFirstName(),userDetail.getLastName(),userDetail.getMobileNumber(),userDetail.getEmail(),userDetail.getDateOfBirth());
		
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
		UserDetailDto dto=new UserDetailDto(user.getUserId(),user.getFirstName(),user.getLastName(),user.getMobileNumber(),user.getEmail(),user.getDateOfBirth());
		log.info("Comment method started");
		Comment comm =new Comment();
		comm.setComment(comment.getComment());
		comm.setUserDetailDto(dto);
		commentRepo.save(comm);
		log.info(comment.getComment());
		tweet.getComment().add(comm);
		Tweet twee=tweetrepo.save(tweet);
		TweetInfo info=new TweetInfo(twee.getTweetId(),twee.getTweet(),twee.getComment(),twee.getDateAndTimeOfTweet(),twee.getLike());
		user.getTweet().add(info);
		tweetApp.save(user);
		log.info("Data saved in tweet");
		return comm;
	}
}
