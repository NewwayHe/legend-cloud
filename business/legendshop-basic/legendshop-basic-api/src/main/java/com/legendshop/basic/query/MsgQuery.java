/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.query;

import cn.legendshop.jpaplus.support.PageParams;
import lombok.Data;

/**
 * 系统通知
 *
 * @author legendshop
 */
@Data
public class MsgQuery extends PageParams {

	private Long receiverId;

	/**
	 * {@link com.legendshop.basic.enums.MsgReceiverTypeEnum}
	 */
	private Integer userType;

}
