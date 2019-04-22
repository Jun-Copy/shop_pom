package com.wtj.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Goods implements Serializable{
	@TableId(type = IdType.AUTO)
	private int id;
	private String gdName;
	private BigDecimal gdPrice;
	private int gdSave;
	private String gdInfo;
	private String gdImage;
	private int status;
	private Date createTime = new Date();
	private int typeId;
}
