package com.tweetapp.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.tweetapp.model.Comment;

public interface commentRepository extends MongoRepository<Comment, String>{

}
