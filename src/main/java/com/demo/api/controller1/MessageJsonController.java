//package com.demo.api.controller1;
//
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.demo.api.dto.User;
//import com.demo.api.publisher.RabbitMqJsonProducer;
//
//@RestController
//@RequestMapping("/api/v1")
//public class MessageJsonController {
//	
//	private RabbitMqJsonProducer jsonProducer;
//
//	public MessageJsonController(RabbitMqJsonProducer jsonProducer) {
//		super();
//		this.jsonProducer = jsonProducer;
//	}
//	
//	@PostMapping("/publish")
//	public ResponseEntity<String> sendJsonMessage(@RequestBody User user){
//		
//		jsonProducer.sendJsonMessage(user);
//		
//		return ResponseEntity.ok("Json message sent to RabbitMq....");
//	}
//
//}
