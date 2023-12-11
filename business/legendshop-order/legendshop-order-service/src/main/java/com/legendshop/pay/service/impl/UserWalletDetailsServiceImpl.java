/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.service.impl;

import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.pay.dao.UserWalletDetailsDao;
import com.legendshop.pay.dto.UserWalletDetailsDTO;
import com.legendshop.pay.entity.UserWalletDetails;
import com.legendshop.pay.enums.UserWalletAmountTypeEnum;
import com.legendshop.pay.enums.WalletBusinessTypeEnum;
import com.legendshop.pay.enums.WalletDetailsStateEnum;
import com.legendshop.pay.excel.UserWalletDetailsExcelDTO;
import com.legendshop.pay.query.UserWalletDetailsQuery;
import com.legendshop.pay.service.UserWalletDetailsService;
import com.legendshop.pay.service.convert.UserWalletDetailsConverter;
import com.legendshop.pay.utils.WalletUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 用户钱包收支记录详情(UserWalletDetails)表服务实现类
 *
 * @author legendshop
 * @since 2021-03-13 14:44:01
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserWalletDetailsServiceImpl implements UserWalletDetailsService {

	private final UserWalletDetailsConverter converter;

	private final UserWalletDetailsDao userWalletDetailsDao;


	@Override
	public List<Long> record(List<UserWalletDetailsDTO> userWalletDetailsList) {
		if (CollectionUtils.isEmpty(userWalletDetailsList)) {
			return new ArrayList<>();
		}
		Date now = new Date();
		userWalletDetailsList.forEach(userWalletDetailsDTO -> {
			userWalletDetailsDTO.setSerialNo(WalletUtil.snowflakeId());
			userWalletDetailsDTO.setState(WalletDetailsStateEnum.DEFAULT.getCode());
			userWalletDetailsDTO.setCreateTime(now);
			userWalletDetailsDTO.setUpdateTime(now);
		});
		return this.userWalletDetailsDao.save(this.converter.from(userWalletDetailsList));
	}

	@Override
	public int updateStateByNo(Long serialNo, WalletDetailsStateEnum state) {
		UserWalletDetails userWalletDetails = this.userWalletDetailsDao.getBySerialNo(serialNo);
		userWalletDetails.setState(state.getCode());
		userWalletDetails.setUpdateTime(new Date());
		return this.userWalletDetailsDao.update(userWalletDetails);
	}

	/**
	 * 失效记录
	 */
	@Override
	public int updateState(Long id, WalletDetailsStateEnum state, String remarks) {
		UserWalletDetails userWalletDetails = this.userWalletDetailsDao.getById(id);
		userWalletDetails.setState(state.getCode());
		userWalletDetails.setUpdateTime(new Date());
		userWalletDetails.setRemarks(remarks);
		return this.userWalletDetailsDao.update(userWalletDetails);
	}

	@Override
	public int update(List<UserWalletDetailsDTO> userWalletDetailsList) {
		if (CollectionUtils.isEmpty(userWalletDetailsList)) {
			return 0;
		}
		userWalletDetailsList.forEach(e -> e.setUpdateTime(new Date()));
		return this.userWalletDetailsDao.update(this.converter.from(userWalletDetailsList));
	}

	/**
	 * 查询用户钱包收支记录
	 */
	@Override
	public List<UserWalletDetailsDTO> queryByUserId(Long userId) {
		return this.converter.to(this.userWalletDetailsDao.queryByUserId(userId));
	}

	@Override
	public List<UserWalletDetailsDTO> queryByUserId(Long userId, Date startDate, Date endDate) {
		return this.converter.to(this.userWalletDetailsDao.queryByUserId(userId, startDate, endDate));
	}

	@Override
	public PageSupport<UserWalletDetailsDTO> associatePage(UserWalletDetailsQuery query) {
		PageSupport<UserWalletDetailsDTO> page = this.userWalletDetailsDao.associatePage(query);
		List<UserWalletDetailsDTO> resultList = page.getResultList();
		if (CollectionUtils.isEmpty(resultList)) {
			return page;
		}
		resultList.forEach(e -> {
			e.setBusinessTypeName(e.getBusinessType() == null ? "未知" : e.getBusinessType().getDesc());
			e.setRemarks(e.getBusinessType() == null ? "未知" : e.getBusinessType().getDesc());
		});
		return page;
	}

	/**
	 * 分页查询
	 */
	@Override
	public PageSupport<UserWalletDetailsDTO> pageList(UserWalletDetailsQuery query) {
		PageSupport<UserWalletDetailsDTO> page = this.converter.page(this.userWalletDetailsDao.pageList(query));
		List<UserWalletDetailsDTO> resultList = page.getResultList();
		if (CollectionUtils.isEmpty(resultList)) {
			return page;
		}
		resultList.forEach(e -> e.setBusinessTypeName(e.getBusinessType() == null ? "未知" : e.getBusinessType().getDesc()));
		return page;
	}


	@Override
	public UserWalletDetailsDTO findUserPayDetails(Long businessId) {
		return this.converter.to(this.userWalletDetailsDao.findUserPayDetails(businessId));
	}

	@Override
	public List<UserWalletDetailsDTO> findDetailsByBusinessId(Long businessId, WalletBusinessTypeEnum type) {
		return this.converter.to(this.userWalletDetailsDao.findDetailsByBusinessId(businessId, type));
	}

	@Override
	public List<UserWalletDetailsDTO> findDetailsByBusinessIds(List<Long> businessIds, WalletBusinessTypeEnum type) {
		return this.converter.to(this.userWalletDetailsDao.findDetailsByBusinessIds(businessIds, type));
	}

	@Override
	public List<UserWalletDetailsDTO> findDetailsByBusinessIds(List<Long> businessIds, WalletBusinessTypeEnum type, UserWalletAmountTypeEnum amountType) {
		return this.converter.to(this.userWalletDetailsDao.findDetailsByBusinessIds(businessIds, type, amountType));
	}

	@Override
	public List<UserWalletDetailsDTO> findDetailsByBusinessIds(List<Long> businessIds, WalletBusinessTypeEnum type, WalletDetailsStateEnum state) {
		return this.converter.to(this.userWalletDetailsDao.findDetailsByBusinessIds(businessIds, type, state));
	}

	@Override
	public UserWalletDetailsDTO getBySerialNo(Long serialNo) {
		return this.converter.to(this.userWalletDetailsDao.getBySerialNo(serialNo));
	}

	@Override
	public UserWalletDetailsDTO getById(Long id) {
		return this.converter.to(this.userWalletDetailsDao.getById(id));
	}

	@Override
	public int abnormalMsg(Long id, String errorMsg) {
		UserWalletDetails userWalletDetails = this.userWalletDetailsDao.getById(id);
		userWalletDetails.setRemarks(errorMsg);
		userWalletDetails.setState(WalletDetailsStateEnum.ABNORMAL.getCode());
		userWalletDetails.setUpdateTime(new Date());
		return this.userWalletDetailsDao.update(userWalletDetails);
	}

	@Override
	public List<UserWalletDetailsDTO> findOrderPayDetails(List<Long> businessIds) {
		return this.converter.to(this.userWalletDetailsDao.findOrderPayDetails(businessIds));
	}

	@Override
	public Integer updateUserWalletDetail(UserWalletDetailsDTO details) {
		details.setUpdateTime(new Date());
		return this.userWalletDetailsDao.update(converter.from(details));
	}

	@Override
	public List<UserWalletDetailsExcelDTO> walletExcel(UserWalletDetailsQuery query) {
		List<UserWalletDetailsExcelDTO> resultList = this.userWalletDetailsDao.walletExcel(query);
		if (CollectionUtils.isEmpty(resultList)) {
			return resultList;
		}
		for (UserWalletDetailsExcelDTO userWalletDetailsExcelDTO : resultList) {
			switch (userWalletDetailsExcelDTO.getOpStatus()) {
				case 0:
					userWalletDetailsExcelDTO.setOpStatusType("待审核");
					break;
				case 1:
					userWalletDetailsExcelDTO.setOpStatusType("审核通过");
					break;
				case -1:
					userWalletDetailsExcelDTO.setOpStatusType("审核拒绝");
					break;
				default:
					break;
			}

			switch (userWalletDetailsExcelDTO.getState()) {
				case 0:
					userWalletDetailsExcelDTO.setStateType("-");
					break;
				case 1:
					userWalletDetailsExcelDTO.setStateType("已支付");
					break;
				default:
					break;
			}
		}
		return resultList;
	}

	@Override
	public UserWalletDetailsDTO getDetailById(Long id) {
		UserWalletDetails userWalletDetails = userWalletDetailsDao.getById(id);
		UserWalletDetailsDTO walletDetailsDTO = converter.to(userWalletDetails);
		return walletDetailsDTO;
	}
}
