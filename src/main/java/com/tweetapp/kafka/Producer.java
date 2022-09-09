package com.tweetapp.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
@Service
public class Producer {
	public static final String topic="myTopic";
	private final org.slf4j.Logger logger = LoggerFactory.getLogger(Producer.class);
	@Autowired
	private KafkaTemplate<String,String> kafkaTemp;
	
	public void publishToTopic(String message) {
		logger.info("Kafka Start listening");
		this.kafkaTemp.send(topic,message);
	}
	
	@Bean
	public NewTopic createTopic() {
		return new NewTopic(topic,3,(short)1);
	} 

}
