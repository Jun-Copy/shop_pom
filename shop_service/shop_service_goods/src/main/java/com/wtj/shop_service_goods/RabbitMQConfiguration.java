package com.wtj.shop_service_goods;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfiguration {

	public static final String FANOUT_NAME="goods_fanoutExchange";

	/**
	 * 声明两的队列
	 * @return
	 */
	@Bean
	public Queue getQueue1(){
		return new Queue("goods_queue1");
	}
	@Bean
	public Queue getQueue2(){
		return new Queue("goods_queue2");
	}

	/**
	 * 声明交换机
	 * @return
	 */
	@Bean
	public FanoutExchange getFanoutExchange(){
		return new FanoutExchange(FANOUT_NAME);
	}

	@Bean	//如果参数名跟当前类带bean的方法名相同，会自动注入方法返回值
	public Binding getBinding1(Queue getQueue1,FanoutExchange getFanoutExchange){
		return BindingBuilder.bind(getQueue1).to(getFanoutExchange);
	}

	@Bean
	public Binding getBinding2(Queue getQueue2,FanoutExchange getFanoutExchange){
		return BindingBuilder.bind(getQueue2).to(getFanoutExchange);
	}

}
