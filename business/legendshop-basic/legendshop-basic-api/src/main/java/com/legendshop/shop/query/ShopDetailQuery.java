/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.query;

import cn.legendshop.jpaplus.support.PageParams;
import com.legendshop.basic.enums.OpStatusEnum;
import com.legendshop.common.core.annotation.EnumValid;
import com.legendshop.shop.enums.ApplyForTypeEnum;
import com.legendshop.shop.enums.ShopDetailStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author legendshop
 */
@Schema(description = "店铺列表搜参数")
@Data
public class ShopDetailQuery extends PageParams {

	private static final long serialVersionUID = -8511043294073022997L;

	@Schema(description = "商城名称")
	private String shopName;

	@Schema(description = "联系手机")
	private String contactPhone;

	@Schema(description = "联系人姓名")
	private String contactName;

	/**
	 * 店铺状态
	 * {@link com.legendshop.shop.enums.ShopDetailStatusEnum}
	 */
	@Schema(description = "店铺状态")
	@EnumValid(target = ShopDetailStatusEnum.class, message = "店铺状态异常")
	private Integer status;

	/**
	 * 店铺操作状态
	 * {@link com.legendshop.basic.enums.OpStatusEnum}
	 */
	@Schema(description = "店铺操作状态")
	@EnumValid(target = OpStatusEnum.class, message = "店铺操作状态异常")
	private Integer opStatus;

	@Schema(description = "店铺类型 0.专营店1.旗舰店2.自营店")
	private Integer shopType;

	/**
	 * 店铺类型
	 * {@link com.legendshop.shop.enums.ApplyForTypeEnum}
	 */
	@EnumValid(target = ApplyForTypeEnum.class, message = "店铺类型异常")
	@Schema(description = "店铺类型：2：供应商 3：分销商")
	private Integer applyForType;

	@EnumValid(target = ApplyForTypeEnum.class, message = "商家用户类型异常")
	@Schema(description = "需要排除的商家用户类型 1.个人用户  2.企业用户  3.分销员 ")
	private Integer excludeApplyForType;
}
