//package com.demo.api.publisher;
//
//import java.util.List;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.amqp.core.AmqpTemplate;
//import org.springframework.amqp.rabbit.core.RabbitTemplate;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//
//import com.demo.api.dto.User;
//import com.demo.api.City;
//
//@Service
//public class RabbitMqJsonProducer {
//	
//	@Value("${rabbitmq.exchange.name}")
//	private String exchange;
//	
//	@Value("${rabbitmq.routing.json.key}")
//	private String routingJsonKey;
//	
//	@Autowired
//    private AmqpTemplate amqpTemplate;
//	
//	private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMqJsonProducer.class);
//	
//	private RabbitTemplate rabbitTemplate;
//
//	public RabbitMqJsonProducer(RabbitTemplate rabbitTemplate) {
//		super();
//		this.rabbitTemplate = rabbitTemplate;
//	}
//	
//	public void sendJsonMessage(User user) {
//		LOGGER.info(String.format("json message sent -> %s", user.toString()));
//		rabbitTemplate.convertAndSend(exchange, routingJsonKey, user);
//	}
//	
////	public void sendCityData(List<City> cityList) {
////        rabbitTemplate.convertAndSend(exchange, routingJsonKey, cityList);
////    }
//	
//	public void sendCities(List<City> cityList) {
//        for (City city : cityList) {
//        	LOGGER.info("Sending city to RabbitMQ: {}", city);
//            amqpTemplate.convertAndSend(exchange, routingJsonKey, city);
//        }
//    }
//
//}
