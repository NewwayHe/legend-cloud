/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.service;

import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.basic.dto.SmsLogDTO;
import com.legendshop.basic.query.SmsLogQuery;

/**
 * 短信记录服务
 *
 * @author legendshop
 */
public interface SmsLogService {

	/**
	 * 保存短信发送历史
	 *
	 * @param smsLog
	 * @return
	 */
	Long save(SmsLogDTO smsLog);

	/**
	 * 获取短信发送历史分页列表
	 *
	 * @param smsLogQuery
	 * @return
	 */
	PageSupport<SmsLogDTO> getSmsLogPage(SmsLogQuery smsLogQuery);

}
