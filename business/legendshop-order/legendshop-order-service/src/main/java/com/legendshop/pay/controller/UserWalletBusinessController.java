/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.controller;

import com.legendshop.common.core.constant.R;
import com.legendshop.pay.dto.UserWalletOperationDTO;
import com.legendshop.pay.enums.WalletOperationTypeEnum;
import com.legendshop.pay.service.UserWalletBusinessService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author legendshop
 * @version 1.0.0
 * @title DistributionWalletBusinessController
 * @date 2022/4/1 11:13
 * @description：
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/user/wallet/business")
public class UserWalletBusinessController {

	final UserWalletBusinessService userWalletBusinessService;

	/**
	 * 同步冻结金额
	 *
	 * @param walletOperationDTO
	 * @return
	 */
	@PostMapping(value = "/synchronizeFrozen")
	public R<Void> synchronizeFrozen(@Validated @RequestBody UserWalletOperationDTO walletOperationDTO) {
		return userWalletBusinessService.synchronizeFrozen(walletOperationDTO);
	}

	@PostMapping(value = "/batch/synchronizeFrozen")
	public R<Void> synchronizeFrozen(@Validated @RequestBody List<UserWalletOperationDTO> walletOperationList) {
		return userWalletBusinessService.synchronizeFrozen(walletOperationList);
	}

	/**
	 * 新增结算金额记录
	 *
	 * @param walletOperationDTO
	 * @return
	 */
	@PostMapping(value = "/addAvailableRecordUpdate")
	public R<Long> addAvailableRecordUpdate(@Validated @RequestBody UserWalletOperationDTO walletOperationDTO) {
		walletOperationDTO.setOperationType(WalletOperationTypeEnum.ADDITION);
		return userWalletBusinessService.synchronizeAvailableRecordUpdate(walletOperationDTO);
	}

	/**
	 * 扣减冻结金额记录
	 *
	 * @param walletOperationDTO
	 * @return
	 */
	@PostMapping(value = "/deductionFrozenRecordUpdate")
	public R<Long> deductionFrozenRecordUpdate(@Validated @RequestBody UserWalletOperationDTO walletOperationDTO) {
		walletOperationDTO.setOperationType(WalletOperationTypeEnum.DEDUCTION);
		return userWalletBusinessService.synchronizeFrozenRecordUpdate(walletOperationDTO);
	}

	/**
	 * 异步更新后，不需要事务，建议事务后调用（最好再搭配对应的定时任务补偿），业务处理完成，通知钱包更新。要注意最好事务提交成功后再调该方法
	 *
	 * @param walletCentreId 钱包中间表ID
	 * @return
	 */
	@PostMapping(value = "/synchronizeNotify")
	public R<Void> synchronizeNotify(@RequestParam("walletCentreId") Long walletCentreId) {
		return userWalletBusinessService.synchronizeNotify(walletCentreId);
	}
}
