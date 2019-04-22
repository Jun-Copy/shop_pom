package com.wtj.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;
import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Email implements Serializable {
	//发件人
	private String from;
	//收件人
	private String to;
	//标题
	private String subject;
	//内容
	private String content;
	//发件时间
	private Date createTime;
	//附件
	private File file;
}
