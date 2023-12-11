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
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * @author legendshop
 */
@Schema(description = "收货地址参数")
@Data
@Component
public class UserAddressQuery extends PageParams {

	@Schema(description = "用户Id")
	private Long userId;
}
