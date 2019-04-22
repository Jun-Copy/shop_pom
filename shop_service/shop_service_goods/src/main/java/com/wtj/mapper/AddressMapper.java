package com.wtj.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wtj.entity.Address;

public interface AddressMapper extends BaseMapper<Address> {
	int insertAddress(Address address);
}
