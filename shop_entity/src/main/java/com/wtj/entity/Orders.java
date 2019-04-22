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
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Orders implements Serializable{
	@TableId(type = IdType.AUTO)
	private int id;
	private String orderId;
	private String person;
	private String address;
	private String phone;
	private String code;
	private BigDecimal allPrice;
	private Date createTime;
	private int uId;
	private int status;

	@TableField(exist = false)
	private List<OrderDetils> orderDetilsList;
}
