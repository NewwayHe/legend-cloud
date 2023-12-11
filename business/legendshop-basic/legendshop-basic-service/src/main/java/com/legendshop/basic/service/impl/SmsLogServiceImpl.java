/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.basic.dao.SmsLogDao;
import com.legendshop.basic.dto.SmsLogDTO;
import com.legendshop.basic.enums.MsgSendTypeEnum;
import com.legendshop.basic.query.SmsLogQuery;
import com.legendshop.basic.service.SmsLogService;
import com.legendshop.basic.service.convert.SmsLogConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 短信记录服务
 *
 * @author legendshop
 */
@Service
@RequiredArgsConstructor
public class SmsLogServiceImpl implements SmsLogService {

	final SmsLogDao smsLogDao;

	final SmsLogConverter converter;

	@Override
	public Long save(SmsLogDTO smsLog) {
		return this.smsLogDao.save(converter.from(smsLog));
	}

	@Override
	public PageSupport<SmsLogDTO> getSmsLogPage(SmsLogQuery smsLogQuery) {
		PageSupport<SmsLogDTO> page = converter.page(smsLogDao.getSmsLogPage(smsLogQuery));
		if (ObjectUtil.isNotEmpty(page.getResultList())) {
			page.getResultList().forEach(sms -> {
				sms.setType(MsgSendTypeEnum.getTypeName(Integer.valueOf(sms.getType())));
			});
		}
		return page;
	}

}
