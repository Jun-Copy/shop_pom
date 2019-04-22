package com.wtj.controller;

import freemarker.template.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/item")
public class ItemController {


	@Autowired
	private Configuration configuration;

	/**
	 * 生成商品的静态页面
	 * @return
	 */
	@RequestMapping("/createHtml")
	public String createHtml(){
		/*//获得商品对象
		Goods goods = new Goods(1,"长生不老药", BigDecimal.valueOf(19999.99),100,"不可能","xxx|yyy",0,new Date(),0);
		String[] images = goods.getGdImage().split("\\|");
		String htmlPath = this.getClass().getResource("/static/goods/").getPath()+goods.getId()+".html";
		try {//生成静态html页面
			Template template = configuration.getTemplate("goods_info_item.ftl");
			//准备数据
			Map<String,Object> map = new HashMap<>();
			map.put("goods", goods);
			map.put("images",images);
			template.process(map,new FileWriter(htmlPath));
		} catch (Exception e) {
			e.printStackTrace();
		}*/
		return "";
	}

}
