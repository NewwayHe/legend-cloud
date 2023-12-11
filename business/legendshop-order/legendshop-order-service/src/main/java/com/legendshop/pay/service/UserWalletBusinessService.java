/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.service;

import com.legendshop.common.core.constant.R;
import com.legendshop.pay.dto.UserWalletOperationDTO;

import java.util.List;

/**
 * @author legendshop
 * @version 1.0.0
 * @title DistributionWalletBusinessService
 * @date 2022/3/30 11:24
 * @description： 分销钱包业务表
 */
public interface UserWalletBusinessService {

	/**
	 * 同步冻结金额，需要事务，金额会实时更新到用户钱包，不需要指定操作的收支类型
	 * 结算转冻结
	 */
	R<Void> synchronizeFrozen(UserWalletOperationDTO walletOperationDTO);

	/**
	 * 同步冻结金额，需要事务，金额会实时更新到用户钱包，不需要指定操作的收支类型
	 * 结算转冻结
	 */
	R<Void> synchronizeFrozen(List<UserWalletOperationDTO> walletOperationList);

	/**
	 * 同步返还冻结金额，需要事务，金额会实时更新到用户钱包，不需要指定操作的收支类型
	 * 冻结转结算
	 */
	R<Void> synchronizeFrozenReturn(UserWalletOperationDTO walletOperationDTO);

	/**
	 * 同步返还冻结金额，需要事务，金额会实时更新到用户钱包，不需要指定操作的收支类型
	 * 冻结转结算
	 */
	R<Void> synchronizeFrozenReturn(List<UserWalletOperationDTO> walletOperationList);

	/**
	 * 异步更新冻结金额，需要事务，只会保存中间记录表，会在队列异步进行金额操作，需要指定操作的收支类型
	 * 单冻结，需要搭配 synchronizeNotify
	 *
	 * @return 钱包中间表ID
	 */
	R<Long> synchronizeFrozenRecordUpdate(UserWalletOperationDTO walletOperationDTO);

	/**
	 * 异步更新可用金额，需要事务，只会保存中间记录表，会在队列异步进行金额操作，需要指定操作的收支类型
	 * 单结算，需要搭配 synchronizeNotify
	 *
	 * @return 钱包中间表ID
	 */
	R<Long> synchronizeAvailableRecordUpdate(UserWalletOperationDTO walletOperationDTO);

	/**
	 * 异步更新后，不需要事务，建议事务后调用（最好再搭配对应的定时任务补偿），业务处理完成，通知钱包更新。要注意最好事务提交成功后再调该方法
	 *
	 * @param walletCentreId 钱包中间表ID
	 * @return
	 */
	R<Void> synchronizeNotify(Long walletCentreId);

	/**
	 * 消费钱包中间表，不对业务开放，只用于钱包的消息队列自行使用
	 *
	 * @param walletCentreId 钱包中间表ID
	 * @return
	 */
	R<Void> consumptionCentre(Long walletCentreId);

	/**
	 * 用户钱包中间表状态为待处理定时任务处理
	 *
	 * @return
	 */
	R<Void> dealWithUserWalletCentreJobHandle();


}
