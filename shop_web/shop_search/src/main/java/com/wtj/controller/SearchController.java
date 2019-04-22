package com.wtj.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.wtj.entity.Goods;
import com.wtj.service.IGoodsSearchService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/search")
public class SearchController {

	@Reference
	private IGoodsSearchService goodsSearchService;

	//商品搜索
	@RequestMapping("byGoods")
	public String queryGoodsList(String queryStr, ModelMap modelMap){
		List<Goods> goodsList = goodsSearchService.querySearchGooodsList(queryStr);
		modelMap.put("goodsList",goodsList);
		modelMap.put("queryStr",queryStr);
		System.out.println(goodsList);
		return "goods/goods_list";
	}
}
