package com.wtj.service;

import com.wtj.entity.Orders;
import com.wtj.entity.User;

import java.util.List;

public interface IOrdersService {
	String insertOrder(int addId, User user);
	List<Orders> getOrderListByUId(int uId);
	Orders getOrderById(int id);
	Orders getOrderByOId(String orderId);
	void updateOrder(Orders orders);
}
