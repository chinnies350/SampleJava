package com.demo.api.controller1;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.demo.api.publisher.RabbitMqProducer;

@RestController
@RequestMapping("/api/v1")
public class MessageController {
	
	private RabbitMqProducer producer;

	public MessageController(RabbitMqProducer producer) {
		super();
		this.producer = producer;
	}
	
	@GetMapping("/publish")
	public ResponseEntity<String> sendMessage(@RequestParam("message") String message){
		producer.SendMessage(message);
		return ResponseEntity.ok("Message sent to RabbitMq....");
	}
	
	

}
