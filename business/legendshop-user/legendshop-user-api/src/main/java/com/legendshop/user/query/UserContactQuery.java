/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.query;

import cn.legendshop.jpaplus.support.PageParams;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * @author legendshop
 */
@ApiModel(value = "提货信息参数")
@Data
@Component
public class UserContactQuery extends PageParams {

	@ApiModelProperty(value = "用户Id", hidden = true)
	private Long userId;
}
