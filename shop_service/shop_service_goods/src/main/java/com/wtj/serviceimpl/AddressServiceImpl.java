package com.wtj.serviceimpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wtj.entity.Address;
import com.wtj.mapper.AddressMapper;
import com.wtj.service.IAddressService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class AddressServiceImpl implements IAddressService {

	@Autowired
	private AddressMapper addressMapper;

	@Override
	public int insertAddress(Address address) {
		return addressMapper.insertAddress(address);
	}

	@Override
	public List<Address> getAddressByUId(int uId) {
		QueryWrapper queryWrapper = new QueryWrapper();
		queryWrapper.eq("u_id",uId);
		return addressMapper.selectList(queryWrapper);
	}

	@Override
	public Address getAddressById(int id) {
		return addressMapper.selectById(id);
	}
}
