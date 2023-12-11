/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.activity.dto;

import com.legendshop.common.core.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 积分设置(LsIntegralSetting)DTO
 *
 * @author legendshop
 * @since 2021-03-08 14:49:27
 */
@Data
@Schema(description = "积分设置DTO")
public class IntegralConfigDTO extends BaseDTO implements Serializable {

	private static final long serialVersionUID = 631718464645368234L;

	@NotNull(message = "id不能为空")
	private Long id;

	/**
	 * 开关
	 */
	@Schema(description = "开关")
	@NotNull(message = "开关不能为空")
	private Boolean enabled;

	/**
	 * 兑换积分,比例 x:1
	 */
	@Schema(description = "兑换积分,比例 x:1")
	@NotNull(message = "兑换积分不能为空")
	@Min(value = 1L, message = "积分兑换比例值不能小于1")
	@Max(value = 999999L, message = "积分兑换比例值不能超过999999")
	private BigDecimal proportion;

	/**
	 * 签到送积分
	 */
	@Schema(description = "签到送积分")
	@NotNull(message = "签到送积分不能为空")
	private Boolean signInFlag;

	/**
	 * 评论送积分规则
	 */
	@Schema(description = "评论送积分规则")
	@NotNull(message = "评论送积分规则不能为空")
	private Boolean commentFlag;

	@Schema(description = "文章送积分")
	private Boolean articleFlag;

	/**
	 * 积分优惠券
	 */
	@Schema(description = "积分优惠券")
	@NotNull(message = "积分优惠券不能为空")
	private Boolean couponFlag;

	/**
	 * 积分商品
	 */
	@Schema(description = "积分商品")
	@NotNull(message = "积分商品不能为空")
	private Boolean integralProductFlag;

	/**
	 * 积分商品比例
	 */
	@Schema(description = "积分商品比例")
	@Max(value = 100, message = "积分商品比例不能超过100")
	@Min(value = 0, message = "积分商品比例不能小于0")
	@NotNull(message = "积分商品比例不能为空")
	private BigDecimal integralProductProportion;

	/**
	 * 积分抵扣
	 */
	@Schema(description = "积分抵扣")
	@NotNull(message = "积分抵扣不能为空")
	private Boolean deductionFlag;

	/**
	 * 积分抵扣比例
	 */
	@Schema(description = "积分抵扣比例")
	@Max(value = 100, message = "积分抵扣比例比例不能超过100")
	@Min(value = 0, message = "积分抵扣比例比例不能小于0")
	@NotNull(message = "积分抵扣比例不能为空")
	private BigDecimal deductionProportion;

	/**
	 * 用户端积分规则
	 */
	@Schema(description = "用户端积分规则")
	private String userRule;

	/**
	 * 商家端积分规则
	 */
	@Schema(description = "商家端积分规则")
	private String shopRule;

	/**
	 * 签到规则1:连续 2：周期SignTypeEnum
	 */
	@Schema(description = "签到规则1:连续 2：周期SignTypeEnum")
	@NotNull(message = "签到规则不能为空")
	private Integer signInRule;


	/**
	 * 文章发布获得积分
	 */
	@Schema(description = "文章发布获得积分")
	@Max(value = 999999L, message = "文章发布获得积分不能超过999999")
	private Integer articleIntegral;

	/**
	 * 评论内容得积分开关
	 */
	@Schema(description = "评论内容得积分开关")
	private Boolean commentContentFlag;

	/**
	 * 评论字数
	 */
	@Schema(description = "评论字数")
	private Integer contentNumber;

	/**
	 * 可得积分
	 */
	@Schema(description = "可得积分")
	private Integer contentIntegral;

	/**
	 * 评论图片得积分开关
	 */
	@Schema(description = "评论图片得积分开关")
	private Boolean picFlag;

	/**
	 * 图片数量
	 */
	@Schema(description = "图片数量")
	private Integer picNumber;

	/**
	 * 可得积分
	 */
	@Schema(description = "可得积分")
	private Integer picIntegral;

	/**
	 * 签到周期
	 */
	@Schema(description = "积分签到周期")
	@Max(value = 999999, message = "积分签到周期不能超过999999")
	@Min(value = 0, message = "积分签到周期不能小于0")
	@NotNull(message = "积分签到周期不能为空")
	private Integer period;

	/**
	 * 初始积分值
	 */
	@Schema(description = "初始积分值")
	@Min(value = 0, message = "初始积分值不能小于0")
	@NotNull(message = "初始积分值不能为空")
	private Integer originalIntegral;

	/**
	 * 阶梯叠加值
	 */
	@Schema(description = "阶梯叠加值")
	@Min(value = 0, message = "阶梯叠加值比例不能小于0")
	@NotNull(message = "阶梯叠加值不能为空")
	private Integer addIntegral;

	/**
	 * 封顶值
	 */
	@Min(value = 0, message = "阶封顶值不能小于0")
	@Schema(description = "封顶值")
	@NotNull(message = "封顶值不能为空")
	private Integer topIntegral;

	/**
	 * 创建时间
	 */
	@Schema(description = "创建时间")
	private Date createTime;

}
