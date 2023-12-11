/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.service;

import com.legendshop.basic.dto.MsgDTO;
import com.legendshop.basic.enums.MsgReceiverTypeEnum;

/**
 * 站内信服务.
 *
 * @author legendshop
 */
public interface MessageBizService {


	/**
	 * 保存系统通知
	 *
	 * @param msgDTO
	 * @param receiverIds
	 * @param type        {@link MsgReceiverTypeEnum}
	 * @return
	 */
	Long saveSystemMessages(MsgDTO msgDTO, Long[] receiverIds, Integer type);

}
