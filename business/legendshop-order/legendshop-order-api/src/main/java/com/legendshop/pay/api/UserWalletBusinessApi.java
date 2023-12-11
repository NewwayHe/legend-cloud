/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.api;

import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.constant.ServiceNameConstants;
import com.legendshop.pay.dto.UserWalletOperationDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author legendshop
 * @version 1.0.0
 * @title DistributionWalletBusinessClient
 * @date 2022/4/1 13:39
 * @description：
 */
@FeignClient(contextId = "UserWalletBusinessApi", value = ServiceNameConstants.ORDER_SERVICE)
public interface UserWalletBusinessApi {

	String PREFIX = ServiceNameConstants.ORDER_SERVICE_RPC_PREFIX;

	/**
	 * 同步冻结金额，可用转冻结，注：需要全局事务！！！
	 *
	 * @param walletOperationDTO
	 * @return
	 */
	@PostMapping(value = PREFIX + "/user/wallet/business/synchronizeFrozen")
	R<Void> synchronizeFrozen(@RequestBody UserWalletOperationDTO walletOperationDTO);

	/**
	 * 同步冻结金额，可用转冻结，注：需要全局事务！！！
	 *
	 * @param walletOperationList
	 * @return
	 */
	@PostMapping(value = PREFIX + "/user/wallet/business/batch/synchronizeFrozen")
	R<Void> synchronizeFrozen(@RequestBody List<UserWalletOperationDTO> walletOperationList);

	/**
	 * 新增可用金额记录，需要搭配 synchronizeNotify 一起使用
	 *
	 * @param walletOperationDTO
	 * @return 操作中间表ID
	 */
	@PostMapping(value = PREFIX + "/user/wallet/business/addAvailableRecordUpdate")
	R<Long> addAvailableRecordUpdate(@RequestBody UserWalletOperationDTO walletOperationDTO);

	/**
	 * 扣减冻结金额记录，需要搭配 synchronizeNotify 一起使用
	 *
	 * @param walletOperationDTO
	 * @return 操作中间表ID
	 */
	@PostMapping(value = PREFIX + "/user/wallet/business/deductionFrozenRecordUpdate")
	R<Long> deductionFrozenRecordUpdate(@RequestBody UserWalletOperationDTO walletOperationDTO);

	/**
	 * 通知业务已处理完成，可以进行钱包操作
	 *
	 * @param walletCentreId 操作中间表ID
	 * @return
	 */
	@PostMapping(value = PREFIX + "/user/wallet/business/synchronizeNotify")
	R<Void> synchronizeNotify(@RequestParam("walletCentreId") Long walletCentreId);

	/**
	 * 用户钱包中间表状态为待处理定时任务处理
	 *
	 * @return
	 */
	@GetMapping(value = PREFIX + "/user/wallet/dealWithUserWalletCentreJobHandle")
	R<Void> dealWithUserWalletCentreJobHandle();


}
