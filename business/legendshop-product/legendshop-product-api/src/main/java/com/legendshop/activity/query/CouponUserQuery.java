/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.activity.query;

import cn.legendshop.jpaplus.support.PageParams;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * @author legendshop
 */
@Data
@Schema(description = "用户优惠券查询条件")
public class CouponUserQuery extends PageParams {

	private static final long serialVersionUID = -4201363471834565078L;

	/**
	 * 优惠券id
	 */
	@Schema(description = "优惠券id")
	private Long couponId;

	/**
	 * 排序条件（属性名/排序方式）
	 */
	private String orders;

	/**
	 * SQL语句
	 */
	private String sqlString;

	/**
	 * SQL计数
	 */
	private StringBuilder sqlCount;

	/**
	 * 对象列表
	 */
	private List<Object> objects;

}
