/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.service;


import com.legendshop.basic.dto.ReceiverDTO;
import com.legendshop.basic.enums.ReceiverBusinessTypeEnum;
import com.legendshop.common.core.service.BaseService;

/**
 * 消息已读服务
 *
 * @author legendshop
 */
public interface ReceiverService extends BaseService<ReceiverDTO> {
	/**
	 * @param userId 用户ID
	 * @param msgId  业务ID
	 * @param type   {@link ReceiverBusinessTypeEnum} 业务类型
	 * @return
	 */
	boolean getByUserIdAndMsgId(Long userId, Long msgId, Integer type);

	ReceiverDTO getBusinessId(Long msgId);
}
