package com.wtj.listener;

import com.wtj.entity.Goods;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

@Component		//组件注解，将类加到spring周管理
public class RabbitMQListener {

	@Autowired
	private Configuration configuration;

	/**
	 * 监视goods_queue2该队列的消息，一但有消息就去消费获得消息，将消息注入到参数中
	 * 该方法为：当商品添加的时候去生成该商品的静态页面
	 * @param goods
	 */
	@RabbitListener(queues = "goods_queue2")
	public void getMsg(Goods goods){
		System.out.println(goods);
		/*注入商品静态页面*/
		String[] images = goods.getGdImage().split("\\|");  //将图片路径单独发送数据
		//静态页面的生成路径
		String filePath = this.getClass().getResource("/static/goods/").getPath()+goods.getId()+".html";
		try {
			//根据.ftl模板生成静态页面
			Template template = configuration.getTemplate("goods_info_item.ftl");
			Map<String,Object> map = new HashMap<>();
			map.put("goods",goods);
			map.put("images",images);
			//将页面存在指定路径
			template.process(map,new FileWriter(filePath));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
