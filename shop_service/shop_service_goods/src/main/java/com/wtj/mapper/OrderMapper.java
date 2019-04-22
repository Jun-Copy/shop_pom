package com.wtj.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wtj.entity.Orders;

import java.util.List;

public interface OrderMapper extends BaseMapper<Orders> {
	public List<Orders> getOrderListByUId(int uId);
}
