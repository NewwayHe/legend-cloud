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
import com.legendshop.product.enums.VitLogPageEnum;
import com.legendshop.product.enums.VitLogSourceEnum;
import lombok.Data;

import java.util.Date;

/**
 * 客户浏览历史(VitLog)实体类
 *
 * @author legendshop
 */
@Data
@Entity
@Table(name = "ls_vit_log")
public class VitLog implements GenericEntity<Long> {

	private static final long serialVersionUID = -7269310223088879889L;
	/**
	 * 主键
	 */
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
	@TableGenerator(name = "generator", pkColumnValue = "VIT_LOG_SEQ")
	private Long id;


	/**
	 * 店铺ID
	 */
	@Column(name = "shop_id")
	private Long shopId;

	/**
	 * 用户ID
	 */
	@Column(name = "user_id")
	private Long userId;


	/**
	 * 商城名称
	 */
	@Column(name = "shop_name")
	private String shopName;


	/**
	 * 商品ID
	 */
	@Column(name = "product_id")
	private Long productId;


	/**
	 * 产品名称
	 */
	@Column(name = "product_name")
	private String productName;


	/**
	 * IP
	 */
	@Column(name = "ip")
	private String ip;


	/**
	 * 获得IP所在国家，如果在中国，直接显示省市
	 */
	@Column(name = "country")
	private String country;


	/**
	 * 获得IP所在区域
	 */
	@Column(name = "area")
	private String area;


	/**
	 * 产品图片
	 */
	@Column(name = "pic")
	private String pic;


	/**
	 * 产品售价范围
	 */
	@Column(name = "price")
	private String price;


	/**
	 * 访问页面
	 * {@link VitLogPageEnum}
	 */
	@Column(name = "page")
	private Integer page;


	/**
	 * 访问次数
	 */
	@Column(name = "visit_num")
	private Integer visitNum;

	/**
	 * 用户唯一标识，用于登录前识别身份
	 */
	@Column(name = "user_key")
	private String userKey;


	/**
	 * 来源
	 * {@link VitLogSourceEnum}
	 */
	@Column(name = "source")
	private String source;


	/**
	 * 用户是否已删除
	 */
	@Column(name = "user_del_flag")
	private Boolean userDelFlag;

	/**
	 * 创建时间
	 */
	@Column(name = "create_time")
	private Date createTime;

	/**
	 * 更新时间
	 */
	@Column(name = "update_time")
	private Date updateTime;

}
