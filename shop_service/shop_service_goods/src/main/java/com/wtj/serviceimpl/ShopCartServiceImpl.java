package com.wtj.serviceimpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wtj.entity.Goods;
import com.wtj.entity.ShopCart;
import com.wtj.entity.User;
import com.wtj.mapper.GoodsMapper;
import com.wtj.mapper.ShopCartMapper;
import com.wtj.service.IShopCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class ShopCartServiceImpl implements IShopCartService {

	@Autowired
	private ShopCartMapper shopCartMapper;
	@Autowired
	private GoodsMapper goodsMapper;
	@Autowired
	private RedisTemplate redisTemplate;

	@Override
	public int add(String cartToken,ShopCart shopCart, User user) {
		Goods goods = goodsMapper.selectById(shopCart.getGId());
		System.out.println("查询的商品信息："+goods);
		shopCart.setGName(goods.getGdName());
		shopCart.setGPrice(goods.getGdPrice());
		shopCart.setAllPrice(goods.getGdPrice().multiply(BigDecimal.valueOf(shopCart.getGNumber())));
		shopCart.setCreateTime(new Date());
		shopCart.setGoods(goods);
		System.out.println("添加的购物车的商品信息："+shopCart);
		if (user==null){
			redisTemplate.opsForList().leftPush(cartToken,shopCart);
			redisTemplate.expire(cartToken,7, TimeUnit.DAYS);
			return 1;
		}
		shopCart.setUId(user.getId());
		shopCartMapper.insert(shopCart);
		return 1;
	}

	/**
	 * 获取购物车信息列表
	 * @param cartToken
	 * @param user
	 * @return
	 */
	@Override
	public List<ShopCart> queryShopList(String cartToken, User user) {
		List<ShopCart> carts = null;
		if (user==null){	//用户为空，从缓存中取出游客的购物车信息
			Long size = redisTemplate.opsForList().size(cartToken);
			carts = redisTemplate.opsForList().range(cartToken, 0, size);
			System.out.println("carts"+carts);
		}else {	//用户不为空，从数据库中查询信息
			QueryWrapper queryWrapper = new QueryWrapper();
			queryWrapper.eq("u_id",user.getId());
			carts = shopCartMapper.selectList(queryWrapper);
			Goods goods = null;
			for (ShopCart cart : carts) {		//取出购物车信息后，将商品信息添加到购物车信息中
				 goods = goodsMapper.selectById(cart.getGId());
				System.out.println("goods"+goods);
				cart.setGoods(goods);
			}
		}
		return carts;
	}

	/**
	 * 用户登录时合并购物车
	 * @param cartToken
	 * @param user
	 * @return
	 */
	@Override
	public int mergeShopCart(String cartToken, User user) {
		Long size = redisTemplate.opsForList().size(cartToken);
		List<ShopCart> shopCartList = redisTemplate.opsForList().range(cartToken, 0, size);
		for (ShopCart shopCart : shopCartList) {
			shopCart.setUId(user.getId());
			shopCartMapper.insert(shopCart);
		}
		redisTemplate.delete(cartToken);
		return 1;
	}

	@Override
	public int delShopCartAllByUId(int uId) {
		QueryWrapper queryWrapper = new QueryWrapper();
		queryWrapper.eq("u_id",uId);
		return shopCartMapper.delete(queryWrapper);
	}

}
