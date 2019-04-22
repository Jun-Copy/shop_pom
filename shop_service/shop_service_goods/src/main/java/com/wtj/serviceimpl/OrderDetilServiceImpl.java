package com.wtj.serviceimpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.wtj.entity.OrderDetils;
import com.wtj.mapper.OrderDetilMapper;
import com.wtj.service.IOrderDetilsService;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class OrderDetilServiceImpl implements IOrderDetilsService {

	@Autowired
	private OrderDetilMapper orderDetilMapper;

	@Override
	public int insertOrderDetil(OrderDetils orderDetils) {
		return orderDetilMapper.insert(orderDetils);
	}

	@Override
	public int deleteOrderDetilById(int id) {
		return orderDetilMapper.deleteById(id);
	}
}
