/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.entity;


import cn.legendshop.jpaplus.persistence.*;
import cn.legendshop.jpaplus.support.GenericEntity;
import com.legendshop.common.core.dto.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 用户详细表(UserDetail)实体类
 *
 * @author legendshop
 */
@Data
@Entity
@Table(name = "ls_user_detail")
public class UserDetail extends BaseEntity implements GenericEntity<Long> {

	private static final long serialVersionUID = -52346287425113089L;

	/**
	 * ID
	 */
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
	@TableGenerator(name = "generator", pkColumnValue = "USER_DETAIL_SEQ")
	private Long id;


	/**
	 * 用户id
	 */
	@Column(name = "user_id")
	private Long userId;

	/**
	 * 手机号
	 */
	@Column(name = "mobile")
	private String mobile;

	/**
	 * 用户昵称
	 */
	@Column(name = "nick_name")
	private String nickName;

	/**
	 * 支付密码
	 */
	@Column(name = "pay_password")
	private String payPassword;


	/**
	 * 注册IP
	 */
	@Column(name = "reg_ip")
	private String regIp;

	/**
	 * 注册来源
	 */
	@Column(name = "reg_source")
	private String regSource;


	/**
	 * 最后登录时间
	 */
	@Column(name = "last_login_time")
	private Date lastLoginTime;


	/**
	 * 最后登录IP
	 */
	@Column(name = "last_login_ip")
	private String lastLoginIp;


	/**
	 * M(男) or F(女)
	 */
	@Column(name = "sex")
	private String sex;

	/**
	 * 注册时生成的注册码
	 */
	@Column(name = "reg_code")
	private String regCode;


	/**
	 * 积分
	 */
	@Column(name = "score")
	private Integer score;


	/**
	 * 省份
	 */
	@Column(name = "province_id")
	private Long provinceId;


	/**
	 * 城市
	 */
	@Column(name = "city_id")
	private Long cityId;


	/**
	 * 地级市
	 */
	@Column(name = "area_id")
	private Long areaId;


	/**
	 * 分销上级用户
	 */
	@Column(name = "parent_user_id")
	private Long parentUserId;

	/**
	 * 绑定上级时间
	 */
	@Column(name = "parent_binding_time")
	private Date parentBindingTime;

	/**
	 * 等级
	 */
	@Column(name = "grade_id")
	private String gradeId;

	/**
	 * 消费金额
	 */
	@Column(name = "consumption_amount")
	private BigDecimal consumptionAmount;

	/**
	 * 消费订单数
	 */
	@Column(name = "consumption_order_count")
	private Integer consumptionOrderCount;

	/**
	 * 最近消费时间
	 */
	@Column(name = "recent_consumption_time")
	private Date recentConsumptionTime;

	/**
	 * 可用积分
	 */
	@Column(name = "available_integral")
	private BigDecimal availableIntegral;

	/**
	 * 累计积分
	 */
	@Column(name = "cumulative_integral")
	private BigDecimal cumulativeIntegral;

	/**
	 * 微信号
	 */
	@Column(name = "we_chat_number")
	private String weChatNumber;

	/**
	 * 电子邮箱
	 */
	@Column(name = "email")
	private String email;

	/**
	 * 个性化开关
	 */
	@Column(name = "close_recommend")
	private Boolean closeRecommend;
}
