/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.dto;

import com.legendshop.common.core.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * (Advertise)DTO
 *
 * @author legendshop
 * @since 2022-04-27 15:23:37
 */
@Data
@Schema(description = "DTO")
public class AdvertiseDTO extends BaseDTO implements Serializable {

	private static final long serialVersionUID = 431798277615052114L;

	private Long id;

	/**
	 * 广告标题
	 */
	@Schema(description = "广告标题")
	private String title;

	/**
	 * 用户id
	 */
	@Schema(description = "用户id")
	private Long userId;

	/**
	 * 投放广告用户类型
	 */
	@Schema(description = "投放广告用户类型:1全部用户")
	private String advertiseUesrType;

	/**
	 * 渠道：pc端,移动端
	 */
	@Schema(description = "渠道:PC,APP")
	private String source;

	/**
	 * 渠道：pc端,移动端
	 */
	@Schema(description = "渠道:PC,APP")
	private List sourceList;

	/**
	 * 投放页面:首页,个人中心
	 */
	@Schema(description = "投放页面:首页,个人中心")
	private String advertisePage;

	/**
	 * 投放频率
	 */
	@Schema(description = "投放频率类型:有效期内显示,每天显示,每次打开页面都显示弹窗")
	private String advertiseFrequency;

	/**
	 * 链接
	 */
	@Schema(description = "链接")
	private HashMap<String, Object> url;

	/**
	 * 广告图片
	 */
	@Schema(description = "广告图片")
	private String photos;

	/**
	 * 权重
	 */
	@Schema(description = "权重")
	private Integer seq;

	/**
	 * 广告状态:0未开始,1开始,2暂停,3结束
	 */
	@Schema(description = "广告状态:0未开始,1开始,2暂停,3结束")
	private Integer status;

	/**
	 * 投放开始时间
	 */
	@Schema(description = "投放开始时间")
	private Date startTime;

	/**
	 * 投放结束时间
	 */
	@Schema(description = "投放结束时间")
	private Date endTime;

	/**
	 * 点击限制:1点击弹窗后不显示(已登陆),2点击弹窗后仍会显示
	 */
	@Schema(description = "点击限制:1点击弹窗后不显示(已登陆),2点击弹窗后仍会显示")
	private Integer clickLimit;

	/**
	 * 创建时间
	 */
	@Schema(description = "创建时间")
	private Date createTime;

	/**
	 * 更新时间
	 */
	@Schema(description = "更新时间")
	private Date updateTime;

	/**
	 * 有效期内显示广告次数
	 */
	@Schema(description = "有效期内显示广告次数")
	private Integer count;

	/**
	 * 有效期内显示广告次数
	 */
	@Schema(description = "每天显示广告次数")
	private Integer everyDayCount;


}
