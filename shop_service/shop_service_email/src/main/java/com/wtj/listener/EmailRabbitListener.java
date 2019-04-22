package com.wtj.listener;

import com.wtj.entity.Email;
import com.wtj.util.EmailUtil;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EmailRabbitListener {
	@Autowired
	private EmailUtil emailUtil;
	@RabbitListener(queues = "email_queue")
	public void getEmail(Email email){
		emailUtil.send(email);
	}
}
