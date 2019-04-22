package com.wtj.service;

import com.wtj.entity.ShopCart;
import com.wtj.entity.User;

import java.util.List;

public interface IShopCartService {
	public int add(String cartToken,ShopCart shopCart, User user);
	public List<ShopCart> queryShopList(String cartToken,User user);
	public int mergeShopCart(String cartToken,User user);
	int delShopCartAllByUId(int uId);
}
