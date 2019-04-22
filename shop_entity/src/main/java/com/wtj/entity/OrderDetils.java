package com.wtj.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetils implements Serializable{
	@TableId(type = IdType.AUTO)
	private int id;
	private int gId;
	private String gImage;
	private String gName;
	private BigDecimal gPrice;
	private int gNumber;
	private int oId;

}
