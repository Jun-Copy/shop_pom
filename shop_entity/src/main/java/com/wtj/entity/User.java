package com.wtj.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_user")
public class User implements Serializable{
	@TableId(type = IdType.AUTO)
	private int id;
	private String userName;
	private String password;
	private String nickName;
	private String email;
	private String phone;
	private int status;
	private int flag;
}
