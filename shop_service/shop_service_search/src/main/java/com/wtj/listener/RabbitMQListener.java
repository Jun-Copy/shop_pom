package com.wtj.listener;

import com.wtj.entity.Goods;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQListener {

	@Autowired
	private SolrClient solrClient;

	/**
	 * 监视该队列的消息，一但有消息就去消费获得消息 并添加到solr的索引库中
	 * @param goods
	 */
	@RabbitListener(queues = "goods_queue1")
	public void getMsg(Goods goods){
		System.out.println(this.getClass().getName()+":"+goods);
		//索引库支持该对像，将goods的内容根据设置的字段，赋给该对象
		SolrInputDocument document =  new SolrInputDocument();
		document.addField("id",goods.getId());
		document.addField("gdname",goods.getGdName());
		document.addField("gdprice",goods.getGdPrice().doubleValue());
		document.addField("gdinfo",goods.getGdInfo());
		document.addField("gdimage",goods.getGdImage());
		try {
			//添加到索引库，需要提交事务
			solrClient.add(document);
			solrClient.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
