package com.wtj.serviceimpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.wtj.entity.Goods;
import com.wtj.service.IGoodsSearchService;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class GoodsSearchServiceImpl implements IGoodsSearchService {

	@Autowired
	private SolrClient solrClient;


	@Override
	public List<Goods> querySearchGooodsList(String queryStr) {
		System.out.println(queryStr);
		SolrQuery solrQuery = new SolrQuery();
		System.out.println("queryStr:"+queryStr);
		if (queryStr!=null && !"".equals(queryStr)){//设置搜索的关键字
			//标题和内容
			solrQuery.setQuery("gdname:"+queryStr+" || gdinfo:"+queryStr+"");
		}else {
			//全部
			solrQuery.setQuery("*:*");
		}
		//搜索的结果放在list里返回
		List<Goods> goodsList = new ArrayList<>();
		try {
			/*搜索结果的高亮显示*/
			//开启高亮
			solrQuery.setHighlight(true);
			//设置高亮的部分的前后缀
			solrQuery.setHighlightSimplePre("<font color='red'>");
			solrQuery.setHighlightSimplePost("</font>");
			//添加需要高亮的字段
			solrQuery.addHighlightField("gdname");

			//solr搜索
			QueryResponse queryResponse = solrClient.query(solrQuery);
			//获取高亮的结果
			//string为高亮内容的id，string为高亮的字段，list里为高亮的关键词
			Map<String, Map<String, List<String>>> highlighting = queryResponse.getHighlighting();
			System.out.println(this.getClass().getName()+":"+highlighting);
			//搜索的结果，该集合继承了arrayList，并且泛型是SolrDocument
			SolrDocumentList results = queryResponse.getResults();

			Goods goods = null;
			for (SolrDocument result : results) {
				goods = new Goods();
				goods.setId(Integer.parseInt(result.get("id")+""));
				goods.setGdName(result.get("gdname")+"");
				goods.setGdInfo(result.get("goinfo")+"");
				goods.setGdPrice(BigDecimal.valueOf(Double.valueOf((result.get("gdprice")+""))));
				goods.setGdImage(result.get("gdimage")+"");
				//高亮的字段和字段内容
				Map<String, List<String>> stringListMap = highlighting.get(goods.getId()+"");
				//System.out.println("for里面："+stringListMap);
				//System.out.println("for里面的name："+stringListMap.get("gdname"));
				if(stringListMap.get("gdname")!=null){ //判断当前商品是否有高亮，如果搜索内容得到的结果，标题是没有高亮的
					//将高亮的内容替换
					goods.setGdName(stringListMap.get("gdname").get(0));
				}
				goodsList.add(goods);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return goodsList;
	}

	@Override
	public int insertGoodsToSolr(Goods goods) {
		//创建一个输入文档
		SolrInputDocument solrDocument = new SolrInputDocument();
		solrDocument.addField("id",goods.getId());
		solrDocument.addField("gdname",goods.getGdName());
		solrDocument.addField("gdprice",goods.getGdPrice().doubleValue());
		solrDocument.addField("gdimage",goods.getGdImage());
		solrDocument.addField("gdinfo",goods.getGdInfo());
		try {
			//向solr服务器添加数据
			solrClient.add(solrDocument);
			//要提交
			solrClient.commit();
			return 1;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
}
