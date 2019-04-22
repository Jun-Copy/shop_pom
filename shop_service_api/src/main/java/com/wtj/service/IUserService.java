package com.wtj.service;

import com.wtj.entity.User;

public interface IUserService {
	public User insertUser(User user);
	public User getUser(String userName,String password);
	public int updateStatus(String userName);
}
