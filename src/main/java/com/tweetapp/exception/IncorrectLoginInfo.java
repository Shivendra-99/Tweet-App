package com.tweetapp.exception;

public class IncorrectLoginInfo extends RuntimeException{
	
	public IncorrectLoginInfo(String message) {
		super("Incorrect Email id or Password");
	}

}
