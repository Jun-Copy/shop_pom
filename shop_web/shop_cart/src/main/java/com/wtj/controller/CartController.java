package com.wtj.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.wtj.aop.IsLogin;
import com.wtj.entity.ShopCart;
import com.wtj.entity.User;
import com.wtj.service.IShopCartService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/cart")
public class CartController {

	@Reference
	private IShopCartService shopCartService;

	/**
	 * 跳转到购物车添加成功页面
	 * @return
	 */
	@RequestMapping("/add_success")
	public String toAddSuccess(){
		return "add_success";
	}

	/**
	 * 添加商品到购物车
	 * @param cartToken
	 * @param shopCart
	 * @param user
	 * @param response
	 * @return
	 */
	@RequestMapping("/add")
	@IsLogin
	@ResponseBody
	public String add(@CookieValue(name = "cart_token",required = false)String cartToken,
					  ShopCart shopCart, User user,
					  HttpServletResponse response){
		if (cartToken==null){
			cartToken = UUID.randomUUID().toString();
			Cookie cookie = new Cookie("cart_token",cartToken);
			cookie.setPath("/");
			cookie.setMaxAge(60*60*24*7);
			response.addCookie(cookie);
		}
		shopCartService.add(cartToken,shopCart,user);
		return "callBack('add_success')";
	}

	/**
	 * ajax请求购物车列表,返回json对象
	 * @param user
	 * @param cartToken
	 * @return
	 */
	@RequestMapping("/ajaxList")
	@ResponseBody
	@IsLogin
	public String getAjaxCartList(User user,
								  @CookieValue(name = "cart_token",required = false)String cartToken){
		System.out.println(user);
		System.out.println(cartToken);
		List<ShopCart> shopCarts = shopCartService.queryShopList(cartToken, user);
		return "json("+ JSON.toJSONString(shopCarts)+")";
	}


	/**
	 * 跳转到购物车列表页面
	 * @return
	 */
	@RequestMapping("/list")
	@IsLogin
	public String getCartList(@CookieValue(name = "cart_token",required = false)String cartToken,
									  User user, Model model){
		//cookie	user
		List<ShopCart> shopCarts = shopCartService.queryShopList(cartToken, user);
		BigDecimal priceSave=new BigDecimal(0.00);
		for (ShopCart shopCart : shopCarts) {
			priceSave=priceSave.add(shopCart.getAllPrice());
		}
		model.addAttribute("cartList",shopCarts);
		model.addAttribute("priceSave",priceSave);
		return "cart_list";
	}
}
