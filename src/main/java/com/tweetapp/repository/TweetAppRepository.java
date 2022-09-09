package com.tweetapp.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.tweetapp.model.UserDetail;

public interface TweetAppRepository extends MongoRepository<UserDetail,Long>{

}
