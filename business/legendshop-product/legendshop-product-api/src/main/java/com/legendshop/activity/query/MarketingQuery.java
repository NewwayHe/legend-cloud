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
import com.legendshop.activity.enums.MarketingStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author legendshop
 */
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class MarketingQuery extends PageParams {

	private static final long serialVersionUID = -7734118471169355313L;

	@Schema(description = "活动名称")
	private String marketName;

	/**
	 * {@link MarketingStatusEnum}
	 */
	@Schema(description = "活动状态(-1 未发布 0 未开始 1 进行中 2 已暂停 3 已结束 4 已失效 -2 已删除)")
	private Integer status;

	/**
	 * 活动类型 0:满减 1:满折
	 */
	@Schema(description = "活动类型 0:满减 1:满折")
	private Integer type;

	@Schema(description = "店铺编号")
	private Long shopId;

	@Schema(description = "店铺名称")
	private String shopName;

	@Schema(description = "活动Id")
	private Long id;

	@Schema(description = "是否全部商品[全店] 部分商品[需要添加活动商品]")
	private Boolean allProdsFlag;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Schema(description = "开始时间")
	private Date startTime;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Schema(description = "结束时间")
	private Date endTime;
}
