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
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author legendshop
 */
@Data
@Component
public class LoginHistoryQuery extends PageParams {

	private Date startTime;

	private Date endTime;
}
