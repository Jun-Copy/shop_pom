package com.wtj.service;

import com.wtj.entity.OrderDetils;

public interface IOrderDetilsService {
	int insertOrderDetil(OrderDetils orderDetils);
	int deleteOrderDetilById(int id);
}
