package com.wtj.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.wtj.entity.Goods;
import com.wtj.service.IGoodsService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/goods")
public class GoodsController {
	@Reference
	private IGoodsService goodsService;
	@Value("${pageContext.imgPath}")
	private String imgPath;

	@RequestMapping("/list")
	public String getGoodsList(ModelMap modelMap){
		List<Goods> list = goodsService.getList();
		modelMap.put("goodsList",list);
		modelMap.put("imgPath",imgPath);
		return "goods/goods_list";
	}

	@RequestMapping("/add")
	public String add(Goods goods){
		System.out.println("goodsController："+goods);
		goodsService.addGoods(goods);
		return "redirect:/goods/list";
	}
}
