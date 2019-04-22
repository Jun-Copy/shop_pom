package com.wtj.serviceimpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wtj.entity.*;
import com.wtj.mapper.AddressMapper;
import com.wtj.mapper.OrderDetilMapper;
import com.wtj.mapper.OrderMapper;
import com.wtj.service.IOrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class OrderServiceImpl implements IOrdersService {
	@Autowired
	private OrderMapper orderMapper;
	@Autowired
	private AddressMapper addressMapper;
	@Autowired
	private ShopCartServiceImpl shopCartService;
	@Autowired
	private OrderDetilMapper orderDetilMapper;

	@Override
	@Transactional
	public String insertOrder(int addId,User user) {
		//获取收货地址信息
		Address address = addressMapper.selectById(addId);
		System.out.println(address);
		//获取商品信息列表
		List<ShopCart> shopCarts = shopCartService.queryShopList(null, user);

		String orderId = UUID.randomUUID().toString();

		OrderDetils orderDetils=null;
		BigDecimal priceAll=new BigDecimal(0.00);
		for (ShopCart shopCart : shopCarts) {
			priceAll = priceAll.add(shopCart.getAllPrice());
		}

		//创建订单
		Orders orders= new Orders(0,orderId,
				address.getPerson(),address.getAddress(),address.getPhone(),
				address.getCode(),priceAll,new Date(),user.getId(),0,null);
		//添加订单
		orderMapper.insert(orders);

		//创建订单详情
		for (ShopCart shopCart : shopCarts) {
			String imgPath = shopCart.getGoods().getGdImage().split("\\|")[0];
			orderDetils = new OrderDetils(0,shopCart.getGId(),
					imgPath,
					shopCart.getGName(),shopCart.getGPrice(),shopCart.getGNumber(),orders.getId());
			//添加订单详情到数据库
			orderDetilMapper.insert(orderDetils);
		}

		System.out.println("创建的orders："+orders);
		System.out.println("创建的orderDetils："+orderDetils);
		return orders.getOrderId();
	}

	@Override
	public List<Orders> getOrderListByUId(int uId) {
		return orderMapper.getOrderListByUId(uId);
	}

	@Override
	public Orders getOrderById(int id) {
		return null;
	}

	@Override
	public Orders getOrderByOId(String orderId) {
		QueryWrapper queryWrapper = new QueryWrapper();
		queryWrapper.eq("order_id",orderId);
		return orderMapper.selectOne(queryWrapper);
	}

	@Override
	public void updateOrder(Orders orders) {
		orderMapper.updateById(orders);
	}
}
