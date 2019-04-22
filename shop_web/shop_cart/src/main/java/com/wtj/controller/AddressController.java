package com.wtj.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.wtj.aop.IsLogin;
import com.wtj.entity.Address;
import com.wtj.entity.User;
import com.wtj.service.IAddressService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/address")
public class AddressController {

	@Reference
	private IAddressService addressService;

	@RequestMapping("/add")
	@ResponseBody
	@IsLogin(mustLogin = true)
	public int insertAddress(Address address, User user){
		address.setUId(user.getId());
		return addressService.insertAddress(address);
	}
}
