package com.wtj.serviceimpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.wtj.entity.Goods;
import com.wtj.mapper.GoodsMapper;
import com.wtj.service.IGoodsService;
import com.wtj.shop_service_goods.RabbitMQConfiguration;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class GoodsServiceImpl implements IGoodsService {

	@Autowired
	private GoodsMapper goodsMapper;
/*	@Reference
	private IGoodsSearchService goodsSearchService;*/
	@Autowired		//注入rabbit模板
	private RabbitTemplate rabbitTemplate;

	@Override
	public List<Goods> getList() {
		return goodsMapper.selectList(null);
	}
	@Override
	public int addGoods(Goods goods) {
		//添加商品
		int result = goodsMapper.insert(goods);
		//调用搜索服务同步索引库
		//goodsSearchService.insertGoodsToSolr(goods);

		//将添加的商品信息放入rabbitMQ中
		rabbitTemplate.convertAndSend(RabbitMQConfiguration.FANOUT_NAME,"",goods);
		return result;
	}
}
