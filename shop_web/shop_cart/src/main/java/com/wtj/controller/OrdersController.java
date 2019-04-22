package com.wtj.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.wtj.aop.IsLogin;
import com.wtj.entity.Address;
import com.wtj.entity.Orders;
import com.wtj.entity.ShopCart;
import com.wtj.entity.User;
import com.wtj.service.IAddressService;
import com.wtj.service.IOrdersService;
import com.wtj.service.IShopCartService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/order")
public class OrdersController {
	@Reference
	private IOrdersService ordersService;
	@Reference
	private IAddressService addressService;
	@Reference
	private IShopCartService shopCartService;


	/**
	 * 前往订单编辑页面
	 * @param user
	 * @param model
	 * @return
	 */
	@RequestMapping("/toAdd")
	@IsLogin(mustLogin = true)
	public String addOrder(User user, Model model){
		List<Address> addressList = addressService.getAddressByUId(user.getId());
		List<ShopCart> cartList = shopCartService.queryShopList(null, user);

		BigDecimal priceSave=new BigDecimal(0.00);
		for (ShopCart shopCart : cartList) {
			priceSave=priceSave.add(shopCart.getAllPrice());
		}
		model.addAttribute("cartList",cartList);
		model.addAttribute("priceSave",priceSave);
		model.addAttribute("addressList",addressList);
		return "order_edit";
	}

	/**
	 * 提交订单
	 * @param user
	 * @return
	 */
	@RequestMapping("/add")
	@IsLogin(mustLogin = true)
	@ResponseBody
	public String add(User user, int addId){
		String orderId = ordersService.insertOrder(addId,user);
		shopCartService.delShopCartAllByUId(user.getId());
		return orderId;
	}


	@RequestMapping("/orderList")
	@IsLogin(mustLogin = true)
	public String toOrderList(User user,Model model){
		List<Orders> orderList = ordersService.getOrderListByUId(user.getId());
		System.out.println("下单后的用户订单信息："+orderList);
		model.addAttribute("orderList",orderList);
		return "order_list";
	}
}
