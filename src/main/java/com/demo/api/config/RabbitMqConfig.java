package com.demo.api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;

@Configuration
public class RabbitMqConfig {
	
	@Value("${rabbitmq.queue.name}")
	private String queueName;
	
	@Value("${rabbitmq.queue.json.name}")
	private String jsonQueue;
	
	@Value("${rabbitmq.exchange.name}")
	private String exchangeName;
	
	@Value("${rabbitmq.routing.key}")
	private String routingKey;
	
	@Value("${rabbitmq.routing.json.key}")
	private String routingjsonKey;
	  
	//spring bean for rabbitmq queue
	@Bean
	public Queue queue() {
		return new Queue(queueName);
	}
	
	// spring bean for queue to store json messages.
	@Bean
	public Queue jsonqueue() {
		return new Queue(jsonQueue);
	}
	
	// spring bean for rabbitmq exchange
	@Bean
	public TopicExchange excahange() {
		return new TopicExchange(exchangeName);
	}
	
	//Binding between queue and exchange using routing key
	@Bean
	public Binding binding() {
		return BindingBuilder
				.bind(queue())
				.to(excahange())
				.with(routingKey);				
	}
	
	//Binding between json queue and exchange using json routing key
	@Bean
	public Binding jsonbinding() {
		return BindingBuilder
				.bind(jsonqueue())
				.to(excahange())
				.with(routingjsonKey);
	}
	
	@Bean
	public MessageConverter convertor() {
		return new Jackson2JsonMessageConverter();
	}
	
	@Bean
	public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory) {
		RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
		rabbitTemplate.setMessageConverter(convertor());
		return rabbitTemplate;
	}
	// ConnectionFactory
	// RabbitTemplate
	// RabbitAdmin  // the spring boot will atuomatically initialize  this Infrastructure spring beans

}
