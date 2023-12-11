/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.expetion.BusinessException;
import com.legendshop.common.rabbitmq.util.AmqpSendMsgUtil;
import com.legendshop.pay.constants.RabbitConstants;
import com.legendshop.pay.constants.RedissonConstants;
import com.legendshop.pay.dao.UserWalletDao;
import com.legendshop.pay.dao.UserWalletDetailedCentreDao;
import com.legendshop.pay.dao.UserWalletDetailsDao;
import com.legendshop.pay.dto.UserWalletDetailedCentreDTO;
import com.legendshop.pay.dto.UserWalletOperationDTO;
import com.legendshop.pay.entity.UserWallet;
import com.legendshop.pay.entity.UserWalletDetailedCentre;
import com.legendshop.pay.entity.UserWalletDetails;
import com.legendshop.pay.enums.*;
import com.legendshop.pay.service.UserWalletBusinessService;
import com.legendshop.pay.service.UserWalletDetailedCentreService;
import com.legendshop.pay.utils.WalletUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author legendshop
 * @version 1.0.0
 * @title UserWalletBusinessServiceImpl
 * @date 2022/4/27 14:20
 * @description：
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserWalletBusinessServiceImpl implements UserWalletBusinessService {

	private final UserWalletDao userWalletDao;
	@Getter
	@Setter
	private RedissonClient redissonClient;
	private final AmqpSendMsgUtil amqpSendMsgUtil;
	private final UserWalletDetailsDao userWalletDetailsDao;
	private final UserWalletDetailedCentreDao userWalletDetailedCentreDao;
	final UserWalletDetailedCentreService userWalletDetailedCentreService;

	@Override
	public R<Void> synchronizeFrozen(UserWalletOperationDTO walletOperationDTO) {
		log.info("synchronizeFrozen 开始同步更新用户钱包，操作内容：{}", JSONUtil.toJsonStr(walletOperationDTO));

		// 抢锁，如果失败了，抛异常中断
		RLock lock = redissonClient.getLock(RedissonConstants.USER_WALLET_LOCK_KEY + walletOperationDTO.getUserId());
		try {
			if (!lock.tryLock(5L, 10L, TimeUnit.SECONDS)) {
				log.info("synchronizeFrozen 更新钱包失败，抢锁失败，操作内容：{}", JSONUtil.toJsonStr(walletOperationDTO));
				throw new BusinessException("金额扣减失败，服务异常！");
			}
		} catch (Exception e) {
			log.error("synchronizeFrozen 更新钱包失败，抢锁异常，操作内容：" + JSONUtil.toJsonStr(walletOperationDTO), e);
			throw new BusinessException("金额扣减失败，服务异常！");
		}

		try {

			// 获取用户分销钱包
			UserWallet walletDTO = getUserWallet(walletOperationDTO.getUserId());
			log.info("synchronizeFrozen 当前用户钱包数据，{}", JSONUtil.toJsonStr(walletDTO));

			// 结算转冻结
			// 需要操作两个字段，所以要生成两个中间表。
			// 一个是冻结金额增加，一个是结算金额扣减
			UserWalletDetailedCentre reduceSettleCentre = convert2UserWalletDetailedCentre(walletOperationDTO, UserWalletAmountTypeEnum.AVAILABLE_AMOUNT, WalletOperationTypeEnum.DEDUCTION);
			reduceSettleCentre.setStatus(WalletCentreStatusEnum.SUCCESS.getValue());
			UserWalletDetailedCentre addFrozenCentre = convert2UserWalletDetailedCentre(walletOperationDTO, UserWalletAmountTypeEnum.FROZEN_AMOUNT, WalletOperationTypeEnum.ADDITION);
			addFrozenCentre.setStatus(WalletCentreStatusEnum.SUCCESS.getValue());

			// 同理，需要生成两个钱包明细
			UserWalletDetails reduceSettleDetail = convert2UserWalletDetails(walletOperationDTO, UserWalletAmountTypeEnum.AVAILABLE_AMOUNT, WalletOperationTypeEnum.DEDUCTION);
			reduceSettleDetail.setBeforeAmount(walletDTO.getAvailableAmount());
			reduceSettleDetail.setAfterAmount(walletDTO.getAvailableAmount().subtract(walletOperationDTO.getAmount()));
			reduceSettleDetail.setUserId(walletOperationDTO.getUserId());

			UserWalletDetails addFrozenDetail = convert2UserWalletDetails(walletOperationDTO, UserWalletAmountTypeEnum.FROZEN_AMOUNT, WalletOperationTypeEnum.ADDITION);
			addFrozenDetail.setBeforeAmount(walletDTO.getFrozenAmount());
			addFrozenDetail.setAfterAmount(walletDTO.getFrozenAmount().add(walletOperationDTO.getAmount()));
			addFrozenDetail.setUserId(walletOperationDTO.getUserId());
			// 对钱包进行操作
			Integer update = userWalletDao.frozenAmount(walletOperationDTO.getUserId(), walletOperationDTO.getAmount());
			if (update <= 0) {
				log.info("synchronizeFrozen 金额扣减失败，钱包可用金额不足，用户ID：{}", walletOperationDTO.getUserId());
				throw new BusinessException("金额扣减失败，钱包可用金额不足！");
			}

			// 保存中间表和钱包明细
			List<Long> walletCentreIdList = userWalletDetailedCentreDao.save(Arrays.asList(addFrozenCentre, reduceSettleCentre));
			addFrozenDetail.setWalletCentreId(walletCentreIdList.get(0));
			reduceSettleDetail.setWalletCentreId(walletCentreIdList.get(1));
			userWalletDetailsDao.save(Arrays.asList(addFrozenDetail, reduceSettleDetail));
		} finally {
			lock.unlock();
		}
		log.info("synchronizeFrozen 用户钱包同步冻结金额成功，用户ID：{}", walletOperationDTO.getUserId());
		return R.ok();
	}

	@Override
	public R<Void> synchronizeFrozen(List<UserWalletOperationDTO> walletOperationList) {

		log.info("synchronizeFrozen 开始批量同步更新用户钱包，操作内容：{}", JSONUtil.toJsonStr(walletOperationList));
		List<Long> userIds = walletOperationList.stream().map(UserWalletOperationDTO::getUserId).distinct().collect(Collectors.toList());

		RLock[] lockList = new RLock[userIds.size()];
		for (int i = 0; i < userIds.size(); i++) {
			Long userId = userIds.get(i);
			RLock lock = redissonClient.getLock(RedissonConstants.USER_WALLET_LOCK_KEY + userId);
			lockList[i] = lock;
		}

		RLock multiLock = redissonClient.getMultiLock(lockList);

		try {
			// 抢锁，如果失败了，抛异常中断
			if (!multiLock.tryLock(5L, 10L, TimeUnit.SECONDS)) {
				log.info("synchronizeFrozen 更新钱包失败，抢锁失败，操作内容：{}", JSONUtil.toJsonStr(walletOperationList));
				throw new BusinessException("金额扣减失败，服务异常！");
			}
		} catch (Exception e) {
			log.error("synchronizeFrozen 更新钱包失败，抢锁异常，操作内容：" + JSONUtil.toJsonStr(walletOperationList), e);
			throw new BusinessException("金额扣减失败，服务异常！");
		}

		try {
			// 批量保存
			List<UserWalletDetails> detailsList = new ArrayList<>();
			List<UserWalletDetailedCentre> centreList = new ArrayList<>();

			// 将操作列表按用户分组汇总
			Map<Long, List<UserWalletOperationDTO>> operationMap = walletOperationList.stream().collect(Collectors.groupingBy(UserWalletOperationDTO::getUserId));
			for (Long userId : operationMap.keySet()) {
				// 每个用户会生成多条操作记录，但金额操作只会操作一次，防止seata回滚异常
				List<UserWalletOperationDTO> operationList = operationMap.get(userId);

				// 获取用户分销钱包
				UserWallet walletDTO = getUserWallet(userId);
				log.info("synchronizeFrozen 当前用户钱包数据，{}", JSONUtil.toJsonStr(walletDTO));

				// 生成多条操作记录
				for (UserWalletOperationDTO walletOperationDTO : operationList) {
					// 结算转冻结
					// 需要操作两个字段，所以要生成两个中间表。
					// 一个是冻结金额增加，一个是结算金额扣减
					UserWalletDetailedCentre reduceSettleCentre = convert2UserWalletDetailedCentre(walletOperationDTO, UserWalletAmountTypeEnum.AVAILABLE_AMOUNT, WalletOperationTypeEnum.DEDUCTION);
					reduceSettleCentre.setId(userWalletDetailedCentreDao.createId());
					reduceSettleCentre.setStatus(WalletCentreStatusEnum.SUCCESS.getValue());
					centreList.add(reduceSettleCentre);

					UserWalletDetailedCentre addFrozenCentre = convert2UserWalletDetailedCentre(walletOperationDTO, UserWalletAmountTypeEnum.FROZEN_AMOUNT, WalletOperationTypeEnum.ADDITION);
					addFrozenCentre.setId(userWalletDetailedCentreDao.createId());
					addFrozenCentre.setStatus(WalletCentreStatusEnum.SUCCESS.getValue());
					centreList.add(addFrozenCentre);

					// 同理，需要生成两个钱包明细
					UserWalletDetails reduceSettleDetail = convert2UserWalletDetails(walletOperationDTO, UserWalletAmountTypeEnum.AVAILABLE_AMOUNT, WalletOperationTypeEnum.DEDUCTION);
					reduceSettleDetail.setBeforeAmount(walletDTO.getAvailableAmount());
					reduceSettleDetail.setAfterAmount(walletDTO.getAvailableAmount().subtract(walletOperationDTO.getAmount()));
					reduceSettleDetail.setWalletCentreId(reduceSettleCentre.getId());
					detailsList.add(reduceSettleDetail);

					UserWalletDetails addFrozenDetail = convert2UserWalletDetails(walletOperationDTO, UserWalletAmountTypeEnum.FROZEN_AMOUNT, WalletOperationTypeEnum.ADDITION);
					addFrozenDetail.setBeforeAmount(walletDTO.getFrozenAmount());
					addFrozenDetail.setAfterAmount(walletDTO.getFrozenAmount().add(walletOperationDTO.getAmount()));
					addFrozenDetail.setWalletCentreId(addFrozenCentre.getId());
					detailsList.add(addFrozenDetail);

				}

				// 操作总金额
				BigDecimal totalAmount = operationList.stream().map(UserWalletOperationDTO::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);

				// 对钱包进行操作
				Integer update = userWalletDao.frozenAmount(userId, totalAmount);
				if (update <= 0) {
					log.info("synchronizeFrozen 金额扣减失败，钱包可用金额不足，用户ID：{}", userId);
					throw new BusinessException("金额扣减失败，钱包可用金额不足！");
				}
			}

			// 保存中间表和钱包明细
			userWalletDetailedCentreDao.saveWithId(centreList);
			userWalletDetailsDao.save(detailsList);
		} finally {
			multiLock.unlock();
		}
		log.info("synchronizeFrozen 钱包同步冻结金额成功，用户ID：{}", userIds);
		return R.ok();
	}

	@Override
	public R<Void> synchronizeFrozenReturn(UserWalletOperationDTO walletOperationDTO) {
		log.info("synchronizeFrozenReturn 开始同步更新钱包，操作内容：{}", JSONUtil.toJsonStr(walletOperationDTO));

		// 抢锁，如果失败了，抛异常中断
		RLock lock = redissonClient.getLock(RedissonConstants.USER_WALLET_LOCK_KEY + walletOperationDTO.getUserId());
		try {
			if (!lock.tryLock(5L, 10L, TimeUnit.SECONDS)) {
				log.info("synchronizeFrozenReturn 更新钱包失败，抢锁失败，操作内容：{}", JSONUtil.toJsonStr(walletOperationDTO));
				throw new BusinessException("更新钱包失败，抢锁失败！");
			}
		} catch (Exception e) {
			log.error("synchronizeFrozenReturn 更新钱包失败，抢锁异常，操作内容：" + JSONUtil.toJsonStr(walletOperationDTO), e);
			throw new BusinessException("更新钱包失败，抢锁异常");
		}

		try {

			// 获取用户分销钱包
			UserWallet walletDTO = getUserWallet(walletOperationDTO.getUserId());
			log.info("synchronizeFrozenReturn 当前用户钱包数据，{}", JSONUtil.toJsonStr(walletDTO));

			// 结算转冻结
			// 需要操作两个字段，所以要生成两个中间表。
			// 一个是冻结金额扣减，一个是结算金额增加
			UserWalletDetailedCentre addFrozenCentre = convert2UserWalletDetailedCentre(walletOperationDTO, UserWalletAmountTypeEnum.FROZEN_AMOUNT, WalletOperationTypeEnum.DEDUCTION);
			addFrozenCentre.setStatus(WalletCentreStatusEnum.SUCCESS.getValue());
			UserWalletDetailedCentre reduceSettleCentre = convert2UserWalletDetailedCentre(walletOperationDTO, UserWalletAmountTypeEnum.AVAILABLE_AMOUNT, WalletOperationTypeEnum.ADDITION);
			reduceSettleCentre.setStatus(WalletCentreStatusEnum.SUCCESS.getValue());

			// 同理，需要生成两个钱包明细
			UserWalletDetails addFrozenDetail = convert2UserWalletDetails(walletOperationDTO, UserWalletAmountTypeEnum.FROZEN_AMOUNT, WalletOperationTypeEnum.DEDUCTION);
			addFrozenDetail.setBeforeAmount(walletDTO.getFrozenAmount());
			addFrozenDetail.setAfterAmount(walletDTO.getFrozenAmount().subtract(walletOperationDTO.getAmount()));

			UserWalletDetails reduceSettleDetail = convert2UserWalletDetails(walletOperationDTO, UserWalletAmountTypeEnum.AVAILABLE_AMOUNT, WalletOperationTypeEnum.ADDITION);
			reduceSettleDetail.setBeforeAmount(walletDTO.getAvailableAmount());
			reduceSettleDetail.setAfterAmount(walletDTO.getAvailableAmount().add(walletOperationDTO.getAmount()));


			// 对钱包进行操作
			Integer update = userWalletDao.returnFrozenAmount(walletOperationDTO.getUserId(), walletOperationDTO.getAmount());
			if (update <= 0) {
				log.info("synchronizeFrozenReturn 冻结金额返还失败，钱包冻结金额异常，用户ID：{}", walletOperationDTO.getUserId());
				throw new BusinessException("金额扣减失败，钱包冻结金额异常！");
			}

			// 保存中间表和钱包明细
			userWalletDetailsDao.save(Arrays.asList(addFrozenDetail, reduceSettleDetail));
			userWalletDetailedCentreDao.save(Arrays.asList(addFrozenCentre, reduceSettleCentre));

		} finally {
			lock.unlock();
		}
		log.info("synchronizeFrozenReturn 钱包同步冻结金额返还成功，用户ID：{}", walletOperationDTO.getUserId());
		return R.ok();
	}

	@Override
	public R<Void> synchronizeFrozenReturn(List<UserWalletOperationDTO> walletOperationList) {

		log.info("synchronizeFrozenReturn 开始同步更新钱包，操作内容：{}", JSONUtil.toJsonStr(walletOperationList));
		List<Long> userIds = walletOperationList.stream().map(UserWalletOperationDTO::getUserId).distinct().collect(Collectors.toList());

		RLock[] lockList = new RLock[userIds.size()];
		for (int i = 0; i < userIds.size(); i++) {
			Long userId = userIds.get(i);
			RLock lock = redissonClient.getLock(RedissonConstants.USER_WALLET_LOCK_KEY + userId);
			lockList[i] = lock;
		}

		RLock multiLock = redissonClient.getMultiLock(lockList);

		try {
			// 抢锁，如果失败了，抛异常中断
			if (!multiLock.tryLock(0L, 10L, TimeUnit.SECONDS)) {
				log.info("synchronizeFrozen 更新钱包失败，抢锁失败，操作内容：{}", JSONUtil.toJsonStr(walletOperationList));
				throw new BusinessException("金额扣减失败，服务异常！");
			}
		} catch (Exception e) {
			log.error("synchronizeFrozen 更新钱包失败，抢锁异常，操作内容：" + JSONUtil.toJsonStr(walletOperationList), e);
			throw new BusinessException("金额扣减失败，服务异常！");
		}

		try {

			// 批量保存
			List<UserWalletDetails> detailsList = new ArrayList<>();
			List<UserWalletDetailedCentre> centreList = new ArrayList<>();

			// 将操作列表按用户分组汇总
			Map<Long, List<UserWalletOperationDTO>> operationMap = walletOperationList.stream().collect(Collectors.groupingBy(UserWalletOperationDTO::getUserId));
			for (Long userId : operationMap.keySet()) {
				// 每个用户会生成多条操作记录，但金额操作只会操作一次，防止seata回滚异常
				List<UserWalletOperationDTO> operationList = operationMap.get(userId);

				// 获取用户分销钱包
				UserWallet walletDTO = getUserWallet(userId);
				log.info("synchronizeFrozen 当前用户钱包数据，{}", JSONUtil.toJsonStr(walletDTO));

				// 生成多条操作记录
				for (UserWalletOperationDTO walletOperationDTO : operationList) {
					// 冻结转结算
					// 需要操作两个字段，所以要生成两个中间表。
					// 一个是冻结金额扣减，一个是结算金额增加
					UserWalletDetailedCentre addFrozenCentre = convert2UserWalletDetailedCentre(walletOperationDTO, UserWalletAmountTypeEnum.FROZEN_AMOUNT, WalletOperationTypeEnum.DEDUCTION);
					addFrozenCentre.setId(userWalletDetailedCentreDao.createId());
					addFrozenCentre.setStatus(WalletCentreStatusEnum.SUCCESS.getValue());
					centreList.add(addFrozenCentre);

					UserWalletDetailedCentre reduceSettleCentre = convert2UserWalletDetailedCentre(walletOperationDTO, UserWalletAmountTypeEnum.AVAILABLE_AMOUNT, WalletOperationTypeEnum.ADDITION);
					reduceSettleCentre.setId(userWalletDetailedCentreDao.createId());
					reduceSettleCentre.setStatus(WalletCentreStatusEnum.SUCCESS.getValue());
					centreList.add(reduceSettleCentre);

					// 同理，需要生成两个钱包明细
					UserWalletDetails addFrozenDetail = convert2UserWalletDetails(walletOperationDTO, UserWalletAmountTypeEnum.FROZEN_AMOUNT, WalletOperationTypeEnum.DEDUCTION);
					addFrozenDetail.setBeforeAmount(walletDTO.getFrozenAmount());
					addFrozenDetail.setAfterAmount(walletDTO.getFrozenAmount().subtract(walletOperationDTO.getAmount()));
					addFrozenDetail.setWalletCentreId(addFrozenCentre.getId());
					detailsList.add(addFrozenDetail);

					UserWalletDetails reduceSettleDetail = convert2UserWalletDetails(walletOperationDTO, UserWalletAmountTypeEnum.AVAILABLE_AMOUNT, WalletOperationTypeEnum.ADDITION);
					reduceSettleDetail.setBeforeAmount(walletDTO.getAvailableAmount());
					reduceSettleDetail.setAfterAmount(walletDTO.getAvailableAmount().add(walletOperationDTO.getAmount()));
					reduceSettleDetail.setWalletCentreId(reduceSettleCentre.getId());
					detailsList.add(reduceSettleDetail);

				}

				// 操作总金额
				BigDecimal totalAmount = operationList.stream().map(UserWalletOperationDTO::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);

				// 对钱包进行操作
				Integer update = userWalletDao.frozenAmount(userId, totalAmount);
				if (update <= 0) {
					log.info("synchronizeFrozen 金额扣减失败，钱包可用金额不足，用户ID：{}", userId);
					throw new BusinessException("金额扣减失败，钱包可用金额不足！");
				}
			}

			// 保存中间表和钱包明细
			userWalletDetailedCentreDao.saveWithId(centreList);
			userWalletDetailsDao.saveWithId(detailsList);

		} finally {
			multiLock.unlock();
		}

		return R.ok();
	}

	@Override
	public R<Long> synchronizeFrozenRecordUpdate(UserWalletOperationDTO walletOperationDTO) {
		// 生成中间表
		UserWalletDetailedCentre detailedCentre = convert2UserWalletDetailedCentre(walletOperationDTO, UserWalletAmountTypeEnum.FROZEN_AMOUNT, null);

		// 保存中间表并获取ID
		Long id = userWalletDetailedCentreDao.save(detailedCentre);

		// 返回中间表ID
		return R.ok(id);
	}

	@Override
	public R<Long> synchronizeAvailableRecordUpdate(UserWalletOperationDTO walletOperationDTO) {
		// 生成中间表
		UserWalletDetailedCentre detailedCentre = convert2UserWalletDetailedCentre(walletOperationDTO, UserWalletAmountTypeEnum.AVAILABLE_AMOUNT, null);

		// 保存中间表并获取ID
		Long id = userWalletDetailedCentreDao.save(detailedCentre);

		// 返回中间表ID
		return R.ok(id);
	}

	@Override
	public R<Void> synchronizeNotify(Long walletCentreId) {

		// 更新中间表状态
		Integer update = userWalletDetailedCentreDao.updateStatus(walletCentreId, WalletCentreStatusEnum.NOT_NOTIFIED.getValue(), WalletCentreStatusEnum.NOT_PROCESSED.getValue());
		if (update <= 0) {
			UserWalletDetailedCentre detailedCentre = userWalletDetailedCentreDao.getById(walletCentreId);
			if (null == detailedCentre || !WalletCentreStatusEnum.NOT_PROCESSED.getValue().equals(detailedCentre.getStatus())) {
				log.info("asyncNotify 修改状态失败，当前操作钱包ID：{}", walletCentreId);
				return R.fail("修改状态失败");
			}
		}

		// 发送异步处理MQ
		amqpSendMsgUtil.convertAndSend(RabbitConstants.USER_WALLET_EXCHANGE,
				RabbitConstants.USER_WALLET_CENTRE, walletCentreId);
		return R.ok();
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public R<Void> consumptionCentre(Long walletCentreId) {
		log.info("consumptionCentre 开始消费钱包中间表，操作ID：{}", walletCentreId);

		UserWalletDetailedCentre detailedCentre = userWalletDetailedCentreDao.getById(walletCentreId);
		if (detailedCentre == null) {
			log.info("consumptionCentre 消费钱包中间表数据失败，当前ID找不到数据，ID：{}", walletCentreId);
			return R.fail("消费钱包中间表数据失败，当前ID找不到数据");
		}

		// 抢锁，如果失败了，抛异常中断
		RLock walletLock = redissonClient.getLock(RedissonConstants.USER_WALLET_LOCK_KEY + detailedCentre.getUserId());
		RLock centreLock = redissonClient.getLock(RedissonConstants.USER_WALLET_CENTRE_LOCK_KEY + detailedCentre.getId());

		RLock multiLock = redissonClient.getMultiLock(walletLock, centreLock);
		try {
			if (!multiLock.tryLock(5L, -1L, TimeUnit.SECONDS)) {
				log.info("consumptionCentre 更新钱包失败，抢锁失败，操作内容：{}", JSONUtil.toJsonStr(detailedCentre));
				throw new BusinessException("更新钱包失败，抢锁失败！");
			}
		} catch (Exception e) {
			log.error("consumptionCentre 更新钱包失败，抢锁异常，操作内容：" + JSONUtil.toJsonStr(detailedCentre), e);
			throw new BusinessException("更新钱包失败，抢锁异常");
		}

		try {

			// 更新中间表状态
			Integer updateStatus = userWalletDetailedCentreDao.updateStatus(detailedCentre.getId(), WalletCentreStatusEnum.NOT_PROCESSED.getValue(), WalletCentreStatusEnum.SUCCESS.getValue());
			if (updateStatus <= 0) {
				log.info("consumptionCentre 消费失败，当前中间表状态修改失败，用户ID：{}，中间表ID：{}", detailedCentre.getUserId(), detailedCentre.getId());
				throw new BusinessException("消费失败，当前中间表状态修改失败");
			}

			// 获取用户分销钱包
			UserWallet walletDTO = getUserWallet(detailedCentre.getUserId());
			log.info("consumptionCentre 当前用户钱包数据，{}", JSONUtil.toJsonStr(walletDTO));

			UserWalletDetails walletDetail = new UserWalletDetails();
			walletDetail.setUserId(detailedCentre.getUserId());
			walletDetail.setWalletCentreId(walletCentreId);
			walletDetail.setSerialNo(WalletUtil.snowflakeId());
			walletDetail.setBusinessId(detailedCentre.getBusinessId());
			walletDetail.setBusinessType(detailedCentre.getBusinessType());
			walletDetail.setAmount(detailedCentre.getAmount());
			walletDetail.setAmountType(detailedCentre.getAmountType());
			walletDetail.setOperationType(detailedCentre.getOperationType());
			walletDetail.setRemarks(detailedCentre.getRemarks());
			walletDetail.setState(WalletDetailsStateEnum.FULFILL.getCode());
			walletDetail.setCreateTime(DateUtil.date());
			walletDetail.setUpdateTime(DateUtil.date());

			// 根据佣金类型，获取操作前后的金额
			if (UserWalletAmountTypeEnum.AVAILABLE_AMOUNT.equals(detailedCentre.getAmountType())) {
				walletDetail.setBeforeAmount(walletDTO.getAvailableAmount());

				// 对钱包进行操作
				if (WalletOperationTypeEnum.ADDITION.equals(detailedCentre.getOperationType())) {
					walletDetail.setAfterAmount(walletDTO.getAvailableAmount().add(detailedCentre.getAmount()));
					userWalletDao.addAvailableAmount(detailedCentre.getUserId(), detailedCentre.getAmount());
				} else {
					walletDetail.setAfterAmount(walletDTO.getAvailableAmount().subtract(detailedCentre.getAmount()));
					Integer update = userWalletDao.reduceAvailableAmount(detailedCentre.getUserId(), detailedCentre.getAmount());
					if (update <= 0) {
						log.info("consumptionCentre 分销钱包可用金额不足，用户ID：{}", detailedCentre.getUserId());
						throw new BusinessException("分销钱包可用金额不足！");
					}
				}
			} else if (UserWalletAmountTypeEnum.FROZEN_AMOUNT.equals(detailedCentre.getAmountType())) {
				walletDetail.setBeforeAmount(walletDTO.getFrozenAmount());

				// 对钱包进行操作
				if (WalletOperationTypeEnum.ADDITION.equals(detailedCentre.getOperationType())) {
					walletDetail.setAfterAmount(walletDTO.getFrozenAmount().add(detailedCentre.getAmount()));
					userWalletDao.addFrozenAmount(detailedCentre.getUserId(), detailedCentre.getAmount());
				} else {
					walletDetail.setAfterAmount(walletDTO.getFrozenAmount().subtract(detailedCentre.getAmount()));
					Integer update = userWalletDao.reduceFrozenAmount(detailedCentre.getUserId(), detailedCentre.getAmount());
					if (update <= 0) {
						log.info("consumptionCentre 分销钱包冻结金额不足，用户ID：{}", detailedCentre.getUserId());
						throw new BusinessException("分销钱包冻结金额不足！");
					}
				}
			}

			userWalletDetailsDao.save(walletDetail);

		} finally {
			multiLock.unlock();
		}
		log.info("consumptionCentre 消费钱包中间表成功，用户ID：{}", detailedCentre.getUserId());
		return R.ok();
	}

	@Override
	public R<Void> dealWithUserWalletCentreJobHandle() {

		List<UserWalletDetailedCentreDTO> detailedCentreList = userWalletDetailedCentreService.queryByStatus(WalletCentreStatusEnum.NOT_PROCESSED);
		if (CollUtil.isEmpty(detailedCentreList)) {
			log.info("dealWithUserWalletCentreJob - 当前找不到可处理的中间表，定时任务完成~");
			return R.ok();
		}

		List<Long> ids = detailedCentreList.stream().map(UserWalletDetailedCentreDTO::getId).collect(Collectors.toList());
		log.info("dealWithUserWalletCentreJob - 找到需要处理的数据，ID：{}", ids);

		for (UserWalletDetailedCentreDTO userWalletDetailedCentreDTO : detailedCentreList) {
			log.info("dealWithUserWalletCentreJob - 开始处理，ID：{}", userWalletDetailedCentreDTO.getId());

			R<Void> result = this.synchronizeNotify(userWalletDetailedCentreDTO.getId());
			if (result.getSuccess()) {
				log.info("dealWithUserWalletCentreJob - 处理成功，ID：{}", userWalletDetailedCentreDTO.getId());
			} else {
				log.error("dealWithUserWalletCentreJob - 处理失败，ID：{}，失败原因：{}", userWalletDetailedCentreDTO.getId(), result.getMsg());
				return R.fail(result.getMsg());
			}
		}
		log.info("dealWithUserWalletCentreJob - 定时任务完成~");
		return R.ok();
	}


	/**
	 * 转换成钱包中间表
	 *
	 * @param walletOperationDTO 钱包操作对象
	 * @param amountType         替换佣金操作类型
	 * @param operationType      替换收支类型
	 * @return
	 */
	private UserWalletDetailedCentre convert2UserWalletDetailedCentre(UserWalletOperationDTO walletOperationDTO, UserWalletAmountTypeEnum amountType, WalletOperationTypeEnum operationType) {
		UserWalletDetailedCentre detailedCentre = new UserWalletDetailedCentre();
		detailedCentre.setUserId(walletOperationDTO.getUserId());
		detailedCentre.setBusinessId(walletOperationDTO.getBusinessId());
		detailedCentre.setBusinessType(walletOperationDTO.getBusinessType());
		detailedCentre.setOperationType(operationType == null ? walletOperationDTO.getOperationType() : operationType);
		detailedCentre.setAmountType(amountType);
		detailedCentre.setAmount(walletOperationDTO.getAmount());
		detailedCentre.setStatus(WalletCentreStatusEnum.NOT_NOTIFIED.getValue());
		detailedCentre.setRemarks(walletOperationDTO.getRemarks());
		detailedCentre.setCreateTime(DateUtil.date());
		detailedCentre.setUpdateTime(DateUtil.date());
		return detailedCentre;
	}

	/**
	 * 转换成钱包明细表
	 *
	 * @param walletOperationDTO 钱包操作对象
	 * @param amountType         替换金额类型
	 * @param operationType      替换收支类型
	 * @return
	 */
	private UserWalletDetails convert2UserWalletDetails(UserWalletOperationDTO walletOperationDTO, UserWalletAmountTypeEnum amountType, WalletOperationTypeEnum operationType) {
		UserWalletDetails walletDetail = new UserWalletDetails();
		walletDetail.setUserId(walletOperationDTO.getUserId());
		walletDetail.setSerialNo(WalletUtil.snowflakeId());
		walletDetail.setBusinessId(walletOperationDTO.getBusinessId());
		walletDetail.setBusinessType(walletOperationDTO.getBusinessType());
		walletDetail.setOperationType(operationType == null ? walletOperationDTO.getOperationType() : operationType);
		walletDetail.setAmountType(amountType);
		walletDetail.setAmount(walletOperationDTO.getAmount());
		walletDetail.setState(WalletDetailsStateEnum.FULFILL.getCode());
		walletDetail.setRemarks(walletOperationDTO.getRemarks());
		walletDetail.setCreateTime(DateUtil.date());
		walletDetail.setUpdateTime(DateUtil.date());
		return walletDetail;
	}

	/**
	 * 初始化用户钱包
	 *
	 * @return
	 */
	private UserWallet getUserWallet(Long userId) {

		UserWallet userWallet = userWalletDao.getByUserId(userId);
		if (null != userWallet) {
			return userWallet;
		}


		userWallet = new UserWallet();
		userWallet.setUserId(userId);
		userWallet.setAvailableAmount(BigDecimal.ZERO);
		userWallet.setFrozenAmount(BigDecimal.ZERO);
		userWallet.setCumulativeAmount(BigDecimal.ZERO);
		userWallet.setCumulativeExpenditure(BigDecimal.ZERO);
		userWallet.setState(WalletStateEnum.DEFAULT.getCode());
		userWallet.setCreateTime(DateUtil.date());
		userWallet.setUpdateTime(DateUtil.date());
		userWallet.setPayPassword(null);
		userWalletDao.save(userWallet);
		return userWallet;
	}
}
