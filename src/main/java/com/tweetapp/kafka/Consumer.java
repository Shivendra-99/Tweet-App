package com.tweetapp.kafka;

import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;


@Service
public class Consumer {
	 private final org.slf4j.Logger logger = LoggerFactory.getLogger(Producer.class);

	//spring.kafka.bootstrap-servers=127.0.0.1:29092
	@KafkaListener(topics ="myTopic",groupId = "myGroup")
	public void consumeFromTopic(String message) {
		logger.info(message);
	}
}
