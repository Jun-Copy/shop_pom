package com.wtj.service;

import com.wtj.entity.Address;

import java.util.List;

public interface IAddressService {
	int insertAddress(Address address);
	List<Address> getAddressByUId(int uId);
	Address getAddressById(int id);
}
