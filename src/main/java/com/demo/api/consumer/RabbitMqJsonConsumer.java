package com.demo.api.consumer;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.api.dto.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.demo.api.City;

@Service
public class RabbitMqJsonConsumer {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMqJsonConsumer.class);
	private final ObjectMapper objectMapper;
	
	@Autowired
    public RabbitMqJsonConsumer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }
	
//	@RabbitListener(queues = {"${rabbitmq.queue.json.name}"})
//	public void consumeJsonMessage(User user) throws JsonProcessingException {
//		
//		String jsonUser = objectMapper.writeValueAsString(user);
//        LOGGER.info(String.format("Received Json message -> %s", jsonUser));
//		
//	}
	
	@RabbitListener(queues = "${rabbitmq.queue.json.name}")
	public void processCity(City city) {
        // Process the received city data
        // Implement the logic to save to the database or perform other operations
		LOGGER.info("Received city from RabbitMQ: {}", city);
        
        // Add your processing logic here
    }
//	public void consumeCityData(List<City> cityList) {
//        LOGGER.info(String.format("Received City data from RabbitMQ -> %s", cityList));
//        // Process the received data as needed
//    }

}
