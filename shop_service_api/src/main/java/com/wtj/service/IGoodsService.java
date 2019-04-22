package com.wtj.service;

import com.wtj.entity.Goods;

import java.util.List;

public interface IGoodsService {
	public List<Goods> getList();
	public int addGoods(Goods goods);
}
