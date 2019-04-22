package com.wtj.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
public class ShopCart implements Serializable{
	@TableId(type = IdType.AUTO)
	private int id;
	private int gId;
	private int uId;
	private int gNumber;
	private String gName;
	private BigDecimal gPrice;
	private BigDecimal allPrice;
	private int status;
	private Date createTime;
	private int flag;

	@TableField(exist = false)
	private Goods goods;

}
