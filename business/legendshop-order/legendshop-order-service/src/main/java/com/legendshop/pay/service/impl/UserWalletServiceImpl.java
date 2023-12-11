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
import cn.hutool.core.util.ObjectUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.legendshop.basic.api.AuditApi;
import com.legendshop.basic.api.SysParamsApi;
import com.legendshop.basic.dto.AuditDTO;
import com.legendshop.basic.dto.WalletSettingDTO;
import com.legendshop.basic.enums.AuditTypeEnum;
import com.legendshop.basic.enums.OpStatusEnum;
import com.legendshop.basic.enums.SysParamNameEnum;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.enums.VisitSourceEnum;
import com.legendshop.common.core.expetion.BusinessException;
import com.legendshop.common.rabbitmq.constants.PayAmqpConst;
import com.legendshop.common.rabbitmq.util.AmqpSendMsgUtil;
import com.legendshop.common.security.utils.CryptographicSignatureUtil;
import com.legendshop.common.security.utils.SecurityUtils;
import com.legendshop.pay.bo.UserWalletBO;
import com.legendshop.pay.dao.UserWalletDao;
import com.legendshop.pay.dao.UserWalletDetailsDao;
import com.legendshop.pay.dto.*;
import com.legendshop.pay.entity.UserWallet;
import com.legendshop.pay.entity.UserWalletDetails;
import com.legendshop.pay.enums.*;
import com.legendshop.pay.service.UserWalletBusinessService;
import com.legendshop.pay.service.UserWalletDetailsService;
import com.legendshop.pay.service.UserWalletService;
import com.legendshop.pay.service.convert.UserWalletConverter;
import com.legendshop.pay.utils.WalletUtil;
import com.legendshop.user.api.OrdinaryUserApi;
import com.legendshop.user.api.PassportApi;
import com.legendshop.user.dto.OrdinaryUserDTO;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * (UserWallet)表服务实现类
 *
 * @author legendshop
 * @since 2021-03-13 14:09:48
 */
@Slf4j
@Component("USER_WALLET")
@RequiredArgsConstructor
public class UserWalletServiceImpl implements UserWalletService {

	private final ObjectMapper mapper = new ObjectMapper();

	private static final PasswordEncoder ENCODER = new BCryptPasswordEncoder();

	private final UserWalletDao userWalletDao;

	private final OrdinaryUserApi userClient;

	private final PassportApi passportApi;

	private final UserWalletConverter converter;

	private final AmqpSendMsgUtil amqpSendMsgUtil;

	private final SysParamsApi sysParamsApi;

	private final UserWalletDetailsService userWalletDetailsService;

	private final UserWalletDetailsDao userWalletDetailsDao;

	private final AuditApi auditApi;

	private final UserWalletBusinessService userWalletBusinessService;

	@Override
	public R<Void> initUserWallet(Long userId) {
		return initUserWalletByIds(Collections.singletonList(userId));
	}

	/**
	 * 初始化用户钱包
	 */
	@Override
	public R<Void> initUserWallet(Integer offset, Integer size) {
		R<List<Long>> userIdsResult = this.userClient.getUserIds(offset, size);
		if (userIdsResult.success()) {
			return R.fail("查询用户失败！");
		}
		List<Long> ids = userIdsResult.getData();
		return initUserWalletByIds(ids);
	}

	private R<Void> initUserWalletByIds(List<Long> ids) {
		if (CollectionUtils.isEmpty(ids)) {
			return R.fail("查询用户为空！");
		}
		List<UserWallet> walletList = this.userWalletDao.queryByUserIds(ids);
		if (!CollectionUtils.isEmpty(walletList)) {
			List<Long> userIdList = walletList.stream().map(UserWallet::getUserId).collect(Collectors.toList());
			ids.removeIf(userIdList::contains);
		}
		if (CollectionUtils.isEmpty(ids)) {
			return R.fail("需要添加的用户为空！");
		}
		Date now = new Date();
		List<UserWallet> newWalletList = new ArrayList<>(ids.size());
		for (Long id : ids) {
			UserWallet wallet = new UserWallet();
			newWalletList.add(wallet);
			wallet.setUserId(id);
			wallet.setAvailableAmount(BigDecimal.ZERO);
			wallet.setCumulativeAmount(BigDecimal.ZERO);
			wallet.setFrozenAmount(BigDecimal.ZERO);
			wallet.setCumulativeExpenditure(BigDecimal.ZERO);
			wallet.setState(WalletStateEnum.DEFAULT.getCode());
			wallet.setCreateTime(now);
			wallet.setUpdateTime(now);
			wallet.setPayPassword(null);
		}
		this.userWalletDao.save(newWalletList);
		return R.ok();
	}

	@Override
	public UserWalletDTO getByUserId(Long userId) {
		UserWallet userWallet = this.userWalletDao.getByUserId(userId);
		if (userWallet == null) {
			this.initUserWallet(userId);
			userWallet = this.userWalletDao.getByUserId(userId);
		}
		userWallet.setPayPassword(null);
		userWallet.setCumulativeAmount(Optional.ofNullable(userWallet.getAvailableAmount()).orElse(BigDecimal.ZERO).add(Optional.ofNullable(userWallet.getFrozenAmount()).orElse(BigDecimal.ZERO)));
		return this.converter.to(userWallet);
	}


	/**
	 * 支付抵扣成功
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public R<Void> payDeduction(UserWalletDetailsDTO details) {
		Long userId = details.getUserId();
		BigDecimal amount = details.getAmount();
		// 抵扣金额
		int result = this.userWalletDao.deductionAmount(userId, amount);
		if (result <= 0) {
			return R.fail("金额抵扣失败！");
		}
		result = this.userWalletDetailsService.updateState(details.getId(), WalletDetailsStateEnum.FULFILL, "提现成功!");
		if (result <= 0) {
			return R.fail("无效的金额抵扣记录！");
		}
		return R.ok();
	}

	/**
	 * 支付抵扣成功
	 */
	@Override
	@Deprecated
	@Transactional(rollbackFor = Exception.class)
	public R<Void> payDeduction(List<UserWalletDetailsDTO> details) {
		List<Long> userIds = details.stream().map(UserWalletDetailsDTO::getUserId).collect(Collectors.toList());
		List<UserWallet> walletByUserId = userWalletDao.getWalletByUserId(userIds);
		walletByUserId.stream().forEach(v -> {
			details.stream().forEach(detail -> {
				if (v.getUserId().equals(detail.getUserId())) {
					v.setFrozenAmount(v.getFrozenAmount().subtract(detail.getAmount()));
					v.setCumulativeExpenditure(v.getCumulativeExpenditure().subtract(detail.getAmount()));
				}
			});

		});
		int update = userWalletDao.update(walletByUserId);
		if (update != walletByUserId.size()) {
			throw new BusinessException("金额抵扣失败!");
		}
		List<Long> ids = details.stream().map(UserWalletDetailsDTO::getId).collect(Collectors.toList());
		List<UserWalletDetails> byIds = userWalletDetailsDao.getByIds(ids);
		byIds.stream().forEach(v -> {
			v.setState(WalletDetailsStateEnum.FULFILL.getCode());
		});
		userWalletDetailsDao.update(byIds);
		return R.ok();


	}

	/**
	 * 平台补偿用户
	 *
	 * @param identifier the 用户标识（Id，手机号，用户名）
	 * @param amount     the 补偿金额
	 */
	@Override
	public R<Void> platformCompensation(String identifier, BigDecimal amount) {
		R<OrdinaryUserDTO> clientResult = this.userClient.getUser(identifier);
		if (!clientResult.success()) {
			return R.fail("查询用户失败！");
		}
		OrdinaryUserDTO user = clientResult.getData();
		if (null == user) {
			return R.fail("用户不存在！");
		}

		UserWalletOperationDTO userWalletOperationDTO = new UserWalletOperationDTO();
		userWalletOperationDTO.setUserId(user.getId());
		userWalletOperationDTO.setAmount(amount);
		userWalletOperationDTO.setBusinessId(WalletUtil.snowflakeId());
		userWalletOperationDTO.setBusinessType(WalletBusinessTypeEnum.PLATFORM_COMPENSATION);
		userWalletOperationDTO.setOperationType(WalletOperationTypeEnum.ADDITION);
		userWalletOperationDTO.setRemarks("平台补偿");
		userWalletBusinessService.synchronizeNotify(userWalletBusinessService.synchronizeAvailableRecordUpdate(userWalletOperationDTO).getData());
		return R.ok();
	}

	@Override
	public UserWalletPayDTO payInfo(Long userId) {
		UserWallet wallet = this.userWalletDao.getByUserId(userId);
		if (null == wallet) {
			return null;
		}
		R<WalletSettingDTO> sysParamResult = this.sysParamsApi.getConfigDtoByParamName(SysParamNameEnum.WALLET_SETTING.name(), WalletSettingDTO.class);
		WalletSettingDTO settingDTO = this.mapper.convertValue(sysParamResult.getData(), WalletSettingDTO.class);
		return new UserWalletPayDTO(settingDTO.getEnabled(), wallet.getAvailableAmount(), wallet.getPayPassword());
	}

//	@Override
//	@Transactional
//	public R<Void> completeWithdrawal(Long withdrawSerialNo) {
//		log.info("提现完成后处理~");
//		UserWalletDetailsDTO detailsDTO = this.userWalletDetailsService.getBySerialNo(withdrawSerialNo);
//		if (null == detailsDTO) {
//			log.info("无效的提现记录");
//			return R.fail("无效的提现记录！");
//		}
//		if (!WalletBusinessTypeEnum.CASH_WITHDRAWAL.equals(detailsDTO.getBusinessType())
//				|| (!WalletDetailsStateEnum.DEFAULT.getCode().equals(detailsDTO.getState()) && !WalletDetailsStateEnum.PROCESSING.getCode().equals(detailsDTO.getState()))) {
//			log.info("失效的提现记录");
//			return R.fail("失效的提现记录！");
//		}
//		int result = this.userWalletDetailsService.updateState(detailsDTO.getId(), WalletDetailsStateEnum.FULFILL, "提现成功!");
//		if (result <= 0) {
//			log.error("用户提现记录变更失败！");
//			return R.fail("提现变更失败，请联系平台客服！");
//		}
//		result = this.userWalletDao.deductionAmount(detailsDTO.getUserId(), detailsDTO.getAmount());
//		if (result <= 0) {
//			log.error("用户冻结金额扣除失败！");
//			return R.fail("提现变更失败，请联系平台客服！");
//		}
//		log.info("提现成功！");
//		return R.ok();
//	}

	@Override
	public R<UserWalletBO> getCommissionByUserId(Long id) {
		UserWalletBO userWalletBO = userWalletDao.getCommissionByUserId(id);
		if (ObjectUtil.isEmpty(userWalletBO)) {
			userWalletBO = new UserWalletBO();
		}
		return R.ok(userWalletBO);
	}


	@Override
	public R<Void> passwordValidation(Long userId, String payPassword) {
		return this.verifyPayPassword(payPassword, this.userWalletDao.getByUserId(userId).getPayPassword());
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public R<Void> userWithdraw(UserWithdrawApplyDTO apply) {
		Long userId = apply.getUserId();
		BigDecimal amount = apply.getAmount();
		String payPassword = apply.getPayPassword();

		if (StringUtils.isBlank(payPassword)) {
			return R.fail("请输入正确的支付密码！");
		}

		// 微信提现限制
		if (BigDecimal.ONE.compareTo(amount) > 0) {
			return R.fail("提现金额不能小于1元！");
		}
		if (BigDecimal.valueOf(20000).compareTo(amount) < 0) {
			return R.fail("提现金额不能大于2万元！");
		}

		R<WalletSettingDTO> settingR = this.sysParamsApi.getConfigDtoByParamName(SysParamNameEnum.WALLET_SETTING.name(), WalletSettingDTO.class);
		WalletSettingDTO setting = this.mapper.convertValue(settingR.getData(), WalletSettingDTO.class);
		if (null == setting.getWithdraw() || !setting.getWithdraw()) {
			return R.fail("平台尚未开启钱包提现！");
		}

		UserWallet wallet = this.userWalletDao.getByUserId(userId);
		// 验证支付密码
		if (!this.verifyPayPassword(payPassword, wallet.getPayPassword()).success()) {
			return R.fail("支付密码错误");
		}

		// 验证用户是否有绑定微信
		R<String> openIdResult = this.passportApi.getOpenIdByUserId(userId, VisitSourceEnum.getByName(apply.getSource()).name());
		if (!openIdResult.success() || StringUtils.isBlank(openIdResult.getData())) {
			return R.fail("用户尚未进行微信授权，无法进行微信提现！");
		}


		UserWalletOperationDTO operationDTO = new UserWalletOperationDTO();
		operationDTO.setUserId(userId);
		operationDTO.setAmount(amount);
		// TODO 需要另创一张提现业务表
		operationDTO.setBusinessId(null);
		operationDTO.setBusinessType(WalletBusinessTypeEnum.CASH_WITHDRAWAL);
		StringBuilder remarks = new StringBuilder();
		remarks.append("用户申请提现：用户【")
				.append(userId)
				.append("】，申请提现￥")
				.append(amount)
				.append("，待平台审核");
		operationDTO.setRemarks(remarks.toString());
		userWalletBusinessService.synchronizeFrozen(operationDTO);

		// 默认需要平台提现
		if (null == setting.getWithdrawAudit() || setting.getWithdrawAudit()) {
			return R.ok();
		}
		// 不需要审核直接发起
		return userWithdrawAudit(0L);
	}

	@Override
	public R<Void> userWithdrawAudit(Long id) {
		UserWalletDetailsDTO details = this.userWalletDetailsService.getById(id);
		log.info("提现记录：{}", details);
		if (null == details || !WalletBusinessTypeEnum.CASH_WITHDRAWAL.equals(details.getBusinessType()) || WalletDetailsStateEnum.FULFILL.getCode().equals(details.getState())) {
			log.info("无效的提现记录");
			return R.fail("无效的提现记录！");
		}

		details.setState(WalletDetailsStateEnum.PROCESSING.getCode());
		details.setRemarks(WalletDetailsStateEnum.PROCESSING.getDesc());
		this.userWalletDetailsService.updateUserWalletDetail(details);
		this.amqpSendMsgUtil.convertAndSend(PayAmqpConst.PAY_EXCHANGE, PayAmqpConst.USER_WITHDRAW_ROUTING_KEY, id, true);

		return R.ok();
	}

	@Override
	public R<Void> compensate(Long id) {
		UserWalletDetailsDTO details = this.userWalletDetailsService.getById(id);
		log.info("提现记录：{}", details);
		if (null == details) {
			return R.fail("无效的提现记录");
		}

		if (WalletDetailsStateEnum.FULFILL.getCode().equals(details.getState())) {
			return R.fail("无效的提现记录");
		}

		if (WalletDetailsStateEnum.PROCESSING.getCode().equals(details.getState())) {
			return R.fail("正在处理中，请忽重复提交~");
		}

		R<List<AuditDTO>> walletDetails = auditApi.checkWalletDetails(Collections.singletonList(id));
		List<AuditDTO> auditList = walletDetails.getData();
		if (CollUtil.isEmpty(auditList)) {
			return R.fail("无效的接口调用");
		}

		AuditDTO auditDTO = auditList.get(0);
		if (!OpStatusEnum.PASS.getValue().equals(auditDTO.getOpStatus())) {
			return R.fail("该提现记录还未审核~");
		}

		details.setState(WalletDetailsStateEnum.PROCESSING.getCode());
		details.setRemarks(WalletDetailsStateEnum.PROCESSING.getDesc());
		this.userWalletDetailsService.updateUserWalletDetail(details);

		this.amqpSendMsgUtil.convertAndSend(PayAmqpConst.PAY_EXCHANGE, PayAmqpConst.USER_WITHDRAW_ROUTING_KEY, id);

		return R.ok();
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	@GlobalTransactional(rollbackFor = Exception.class)
	public R<Void> userWithdrawAudit(WithdrawalAuditDTO dtos) {
		if (ObjectUtil.isEmpty(dtos.getId())) {
			return R.fail("id不能为空");
		}

		R<List<AuditDTO>> auditDTOSR = auditApi.checkWalletDetails(dtos.getId());
		if (!CollectionUtils.isEmpty(auditDTOSR.getData())) {
			return R.fail("存在重复审核");
		}
		dtos.getId().forEach(id -> {
			if (dtos.getOpinion() == 1) {
				R<Void> result = this.userWithdrawAudit(id);
				if (!result.getSuccess()) {
					throw new BusinessException(result.getMsg());
				}
				auditApi.audit(this.getAudit(id, dtos.getOpinion(), dtos.getRefuseReason()));
			} else {
				int num = userWalletDetailsService.updateState(id, WalletDetailsStateEnum.INVALID, "提现成功!");
				if (num != 1) {
					throw new BusinessException("审核异常");
				}

				UserWalletDetailsDTO detailsDTO = this.userWalletDetailsService.getById(id);
				int result = this.userWalletDao.returnedFrozenAmount(detailsDTO.getUserId(), detailsDTO.getAmount());
				if (result <= 0) {
					throw new BusinessException("用户冻结金额不足！");
				}

				auditApi.audit(this.getAudit(id, dtos.getOpinion(), dtos.getRefuseReason()));
			}
		});
		return R.ok();
	}


	/**
	 * 验证支付密码
	 *
	 * @param reqPassword     the 前端传输密码
	 * @param userPayPassword the 用户数据库支付密码
	 */
	private R<Void> verifyPayPassword(String reqPassword, String userPayPassword) {
		if (StringUtils.isBlank(reqPassword)) {
			return R.fail("密码不能为空！");
		}
		if (StringUtils.isBlank(userPayPassword)) {
			return R.fail("请先设置用户支付密码！");
		}
		reqPassword = CryptographicSignatureUtil.decrypt(reqPassword);
		return R.process(ENCODER.matches(reqPassword, userPayPassword), "密码错误！");
	}

	/**
	 * 审核记录
	 */
	private AuditDTO getAudit(Long id, int opinion, String refuseReason) {
		//审核记录
		AuditDTO auditDTO = new AuditDTO();
		auditDTO.setCommonId(id);
		auditDTO.setAuditType(AuditTypeEnum.WITHDRAWAL.getValue());
		auditDTO.setAuditTime(DateUtil.date());
		auditDTO.setAuditUsername(SecurityUtils.getAdminUser().getUsername());
		auditDTO.setAuditOpinion(refuseReason);
		//审核记录 成功
		if (opinion == 1) {
			auditDTO.setOpStatus(WalletDetailsAuditEnum.PASS.getCode());
		} else {
			auditDTO.setOpStatus(WalletDetailsAuditEnum.REFUSED.getCode());
		}
		return auditDTO;
	}

	/**
	 * 判断日累计金额是否达到
	 *
	 * @return false 没有达到
	 */
	private Boolean checkDailyCumulativeLimit(Long userId, BigDecimal dailyCumulativeLimit) {
		// 日累计金额 小于等于 0，则直接返回
		if (dailyCumulativeLimit.compareTo(BigDecimal.ZERO) <= 0) {
			log.info("日累计金额小于等于0 ~");
			return Boolean.FALSE;
		}

		Date nowTime = new Date();
		Date start = DateUtil.beginOfDay(nowTime);
		Date end = DateUtil.endOfDay(nowTime);
		List<UserWalletDetailsDTO> userWalletDetailsDTOS = userWalletDetailsService.queryByUserId(userId, start, end);

		// 日累计金额的单位是万，需要乘以 10000L
		dailyCumulativeLimit = dailyCumulativeLimit.multiply(BigDecimal.valueOf(10000L));

		// 如果当天一条提现记录都没有
		if (CollectionUtils.isEmpty(userWalletDetailsDTOS)) {
			return Boolean.TRUE;
		}
		BigDecimal dailyCumulative = BigDecimal.valueOf(0);

		for (UserWalletDetailsDTO userWalletDetailsDTO : userWalletDetailsDTOS) {
			//今天的提现
			Integer state = userWalletDetailsDTO.getState();
			//待审批体现金额+已完成提现金额=今日提现金额
			if (WalletDetailsStateEnum.DEFAULT.getCode().equals(state) || WalletDetailsStateEnum.FULFILL.getCode().equals(state)) {
				dailyCumulative = dailyCumulative.add(userWalletDetailsDTO.getAmount());
			}
		}

		if (dailyCumulative.compareTo(dailyCumulativeLimit) <= 0) {
			return Boolean.TRUE;
		}

		log.info("日累计金额大于日限制金额~");
		return Boolean.FALSE;
	}

}
