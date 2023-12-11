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
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author legendshop
 */
@Schema(description = "用户反馈查询参数")
@Data
@Component
public class UserFeedBackQuery extends PageParams {

	@Schema(description = "反馈内容")
	private String content;

	@Schema(description = "状态")
	private Integer status;

	@Schema(description = "反馈时间")
	private Date createTime;

	@Schema(description = "反馈来源 [pc：pc端 mobile：移动端]参考枚举FeedbackSourceEnum)")
	private String feedbackSource;

	@Schema(description = "开始时间")
	private String beginTime;

	@Schema(description = "结束时间")
	private String endTime;
}
