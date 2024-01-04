package com.demo.api.publisher;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.demo.api.City;

@Service
public class RabbitMqProducer {
	
	@Value("${rabbitmq.exchange.name}")
	private String exchange;
	
	@Value("${rabbitmq.routing.key}")
	private String routingKey;
	
	
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMqProducer.class); 
	
	private RabbitTemplate rabbitTemplate;

	//parameterised constructor
	public RabbitMqProducer(RabbitTemplate rabbitTemplate) {
		super();
		this.rabbitTemplate = rabbitTemplate;
	}
	
	public void SendMessage(String message) {
		LOGGER.info(String.format("Message sent -> %s", message));
		rabbitTemplate.convertAndSend(exchange, routingKey, message);
	}
	
	
	
	

}
