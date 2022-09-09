package com.tweetapp.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tweetapp.kafka.Producer;
import com.tweetapp.model.Comment;
import com.tweetapp.model.Login;
import com.tweetapp.model.Tweet;
import com.tweetapp.model.TweetInfo;
import com.tweetapp.model.UserDetail;
import com.tweetapp.service.TweetAppServiceImpl;


@RestController
@RequestMapping("/api/v1.0/tweets")
public class TweetAppController {
	Logger log=LoggerFactory.getLogger(TweetAppController.class);
	@Autowired
	private TweetAppServiceImpl service;
	
	@Autowired
	private Producer producer;

	@PostMapping("/register")
	public ResponseEntity<?> resgisterUser(@RequestBody UserDetail userDetail) {
		log.info("User registration Started");
		producer.publishToTopic("User Registration Started Now");
		UserDetail use=null;
		try {
		 use=service.SaveUser(userDetail);
		 if(use==null) {
			 producer.publishToTopic("User Already Register with same email id or mobile number");
			 return new ResponseEntity<>("User Already Register with same email id or mobile number",HttpStatus.BAD_REQUEST);
		 }
		}catch(Exception e) {
			e.getStackTrace();
			log.error("User Registration failed");
			 producer.publishToTopic("User Registration failed" +e.getMessage());
			return new ResponseEntity<>(new UserDetail(),HttpStatus.BAD_REQUEST);
		}
		log.error("User Registration Successfully");
		 producer.publishToTopic("User Registred Successfully");
		return new ResponseEntity<>(use,HttpStatus.OK);
	}

	@GetMapping("/get")
	public List<UserDetail> getAllUser() {
		log.info("Requested for User Details");
		producer.publishToTopic("Requesting data for all User");
		List<UserDetail> list=service.getAllUser();
		if(list.isEmpty()) {
			log.error("No User Found");
			producer.publishToTopic("No User Found");
			return list;
		}
	log.info("Found all the User");
	producer.publishToTopic("Requested All User Data Found");
		return list;
	}

	@GetMapping("/user/search/{name}")
	public List<UserDetail> getUserByName(@PathVariable("name") String name) {
		return service.getAllUserByName(name);
	}
	
	@PostMapping("/{userId}/add")
	public ResponseEntity<?> postTweet(@PathVariable("userId") String userId, @RequestBody Tweet post) {

		if (service.postTweet(userId, post) != null) {

			return new ResponseEntity<>("Tweet post successfully", HttpStatus.OK);
		}
		return new ResponseEntity<>("No User Found", HttpStatus.BAD_REQUEST);
	}

	@GetMapping("/all")
	public ResponseEntity<List<Tweet>> getAllTweet() {
		if (service.getAllTweet().isEmpty()) {
			return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<List<Tweet>>(service.getAllTweet(), HttpStatus.OK);
	}

	@PostMapping("/login")
	public ResponseEntity<?> logInUser(@RequestBody Login login) {
		UserDetail userDetail = service.userLogin(login);
		if (userDetail == null) {
			return new ResponseEntity<>("Email Id or Password is incorrect", HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(userDetail, HttpStatus.OK);
	}
	@GetMapping("/{userid}")
	public ResponseEntity<List<TweetInfo>> getAllTweetbyUserName(@PathVariable("userid") String userid){
		List<TweetInfo> tweet=service.getTweetByUserName(userid);
		if(tweet!=null) {
			return new ResponseEntity<List<TweetInfo>>(tweet,HttpStatus.OK);
		}
		return new ResponseEntity<List<TweetInfo>>(new ArrayList<>(),HttpStatus.BAD_REQUEST);
	}
	
    @PutMapping("/{userid}/like/{tweetid}")
	public ResponseEntity<?> giveLikeToPost(@PathVariable("userid") String userid,@PathVariable("tweetid") String tweetid){
		Tweet tweet=service.giveLikeToPost(userid, tweetid);
		if(tweet==null) {
			return new ResponseEntity<>("UserId or Tweet not found",HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(tweet.getLike().size(),HttpStatus.OK);
	}
	
	@DeleteMapping("/{userid}/delete/{tweetid}")
	public ResponseEntity<?> deleteTweetByUserIdAndTweetId(@PathVariable("userid") String userid,@PathVariable("tweetid") String tweetid){
		Tweet tweet=service.deleteTweetByTweetIdAndUserId(userid, tweetid);
		if(tweet!=null) {
			return new ResponseEntity<>(tweet,HttpStatus.OK);
		}
		return new ResponseEntity<>("No Tweet Found for delete",HttpStatus.BAD_REQUEST);
	}
	
	@PostMapping("/{userid}/reply/{tweetid}")
	public ResponseEntity<?> commentOnTweet(@PathVariable("userid") String userid,@PathVariable("tweetid") String tweetid,@RequestBody Comment comment){
		Comment comm=service.postComment(userid, tweetid, comment);
		if(comm==null) {
			return new ResponseEntity<>("Something went wrong",HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(comm,HttpStatus.OK);
	}
}
