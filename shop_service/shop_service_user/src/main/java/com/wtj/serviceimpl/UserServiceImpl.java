package com.wtj.serviceimpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wtj.entity.User;
import com.wtj.mapper.IUserMapper;
import com.wtj.service.IUserService;
import com.wtj.utils.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class UserServiceImpl implements IUserService {

	@Autowired
	private IUserMapper userMapper;

	@Override
	public User insertUser(User user) {
		//MD5处理密码
		String pwd = MD5Util.md5(user.getPassword());
		user.setPassword(pwd);
		userMapper.insert(user);
		return user;
	}

	@Override
	public User getUser(String userName, String password) {
		QueryWrapper<User> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("user_name",userName);
		queryWrapper.eq("password",MD5Util.md5(password));
		User user = userMapper.selectOne(queryWrapper);
		return user;
	}

	@Override
	public int updateStatus(String userName) {
		QueryWrapper<User> queryWrapper = new QueryWrapper();
		queryWrapper.eq("user_name",userName);
		User user = userMapper.selectOne(queryWrapper);
		user.setStatus(1);
		return userMapper.updateById(user);
	}
}
