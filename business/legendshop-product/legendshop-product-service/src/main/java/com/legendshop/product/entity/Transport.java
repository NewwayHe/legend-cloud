/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.entity;


import cn.legendshop.jpaplus.persistence.*;
import cn.legendshop.jpaplus.support.GenericEntity;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 运费模板(Transport)实体类
 *
 * @author legendshop
 */
@Data
@Entity
@Table(name = "ls_transport")
public class Transport implements GenericEntity<Long> {


	private static final long serialVersionUID = 6798887255623153225L;
	/**
	 * ID
	 */
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
	@TableGenerator(name = "generator", pkColumnValue = "TRANSPORT_SEQ")
	private Long id;


	/**
	 * 商家ID
	 */
	@Column(name = "shop_id")
	private Long shopId;


	/**
	 * 运费名称
	 */
	@Column(name = "trans_name")
	private String transName;


	/**
	 * 计费方式
	 */
	@Column(name = "trans_type")
	private String transType;


	/**
	 * 是否包邮
	 */
	@Column(name = "free_flag")
	private Boolean freeFlag;


	/**
	 * 条件包邮 0：关闭 1：开启
	 */
	@Column(name = "condition_free_flag")
	private Boolean conditionFreeFlag;

	/**
	 * 状态
	 */
	@Column(name = "status")
	private Integer status;


	/**
	 * 配送时间
	 */
	@Column(name = "trans_time")
	private Integer transTime;

	/**
	 * 记录时间
	 */
	@Column(name = "rec_date")
	private Date recDate;

	@Transient
	private Boolean transMail;

	@Transient
	private Boolean transExpress;

	@Transient
	private Boolean transEms;

	@Transient
	private List<TransFee> feeList = new ArrayList<TransFee>();

	@Transient
	private List<TransFee> mailList = new ArrayList<TransFee>();

	@Transient
	private List<TransFee> expressList = new ArrayList<TransFee>();

	@Transient
	private List<TransFee> emsList = new ArrayList<TransFee>();

	public void addFeeList(TransFee transfeet) {
		if (feeList == null) {
			feeList = new ArrayList<TransFee>();
		}
		feeList.add(transfeet);
	}

	public boolean geTransfeeIsCity(long cityId) {
//		if(transMail){
//			for (TransFee transfee : mailList) {
//				if(transfee.getCityIdList().contains(cityId)){
//					return true;
//				}
//			}
//		}
//		if(transExpress){
//			for (TransFee transfee : expressList) {
//				if(transfee.getCityIdList().contains(cityId)){
//					return true;
//				}
//			}
//		}
//		if(transEms){
//			for (TransFee transfee : emsList) {
//				if(transfee.getCityIdList().contains(cityId)){
//					return true;
//				}
//			}
//		}
//		return false;
		return false;
	}

}
