package com.wtj.service;

import com.wtj.entity.Goods;

import java.util.List;

public interface IGoodsSearchService {
	public List<Goods> querySearchGooodsList(String queryStr);
	public int insertGoodsToSolr(Goods goods);
}
