package com.wtj.shop_sso;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EmailRabbitConfig {
	@Bean
	public Queue getQueue(){
		return new Queue("email_queue");
	}
}
