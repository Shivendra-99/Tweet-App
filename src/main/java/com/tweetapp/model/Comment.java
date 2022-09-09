package com.tweetapp.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Comment {
	@Id
	private String commentId;
	private UserDetailDto userDetailDto;
	private String comment;
	
	public Comment() {
		super();
	}
	
	public Comment(String commentId, UserDetailDto userDetailDto, String comment) {
		super();
		this.commentId = commentId;
		this.userDetailDto = userDetailDto;
		this.comment = comment;
	}


	public UserDetailDto getUserDetailDto() {
		return userDetailDto;
	}

	public void setUserDetailDto(UserDetailDto userDetailDto) {
		this.userDetailDto = userDetailDto;
	}

	public void setCommentId(String commentId) {
		this.commentId = commentId;
	}

	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getCommentId() {
		return commentId;
	}
}
