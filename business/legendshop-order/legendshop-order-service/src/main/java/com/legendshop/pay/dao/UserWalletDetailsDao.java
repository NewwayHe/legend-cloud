/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.dao;

import cn.legendshop.jpaplus.GenericDao;
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.pay.dto.UserWalletDetailsDTO;
import com.legendshop.pay.entity.UserWalletDetails;
import com.legendshop.pay.enums.UserWalletAmountTypeEnum;
import com.legendshop.pay.enums.WalletBusinessTypeEnum;
import com.legendshop.pay.enums.WalletDetailsStateEnum;
import com.legendshop.pay.excel.UserWalletDetailsExcelDTO;
import com.legendshop.pay.query.UserWalletDetailsQuery;

import java.util.Date;
import java.util.List;

/**
 * 用户钱包收支记录详情(UserWalletDetails)表数据库访问层
 *
 * @author legendshop
 * @since 2021-03-13 14:44:00
 */
public interface UserWalletDetailsDao extends GenericDao<UserWalletDetails, Long> {

	@Override
	Long save(UserWalletDetails entity);

	//List<Long> save(List<UserWalletDetails> entityList);

	@Override
	int update(UserWalletDetails entity);

	UserWalletDetails getBySerialNo(Long serialNo);

	List<UserWalletDetails> queryByUserId(Long userId);

	List<UserWalletDetails> queryByUserId(Long userId, Date startDate, Date endDate);

	PageSupport<UserWalletDetailsDTO> associatePage(UserWalletDetailsQuery query);

	PageSupport<UserWalletDetails> pageList(UserWalletDetailsQuery query);

	UserWalletDetails findUserPayDetails(Long businessId);

	List<UserWalletDetails> findOrderPayDetails(List<Long> businessIds);

	List<UserWalletDetails> findDetailsByBusinessIds(List<Long> businessIds, WalletBusinessTypeEnum type);

	List<UserWalletDetails> findDetailsByBusinessIds(List<Long> businessIds, WalletBusinessTypeEnum type, UserWalletAmountTypeEnum amountType);

	List<UserWalletDetails> findDetailsByBusinessIds(List<Long> businessIds, WalletBusinessTypeEnum type, WalletDetailsStateEnum state);

	List<UserWalletDetails> getByIds(List<Long> ids);

	/**
	 * 提现导出
	 *
	 * @param query
	 * @return
	 */
	List<UserWalletDetailsExcelDTO> walletExcel(UserWalletDetailsQuery query);

	/**
	 * 根据业务ID获取详情
	 *
	 * @param businessId
	 * @param type
	 * @return
	 */
	List<UserWalletDetails> findDetailsByBusinessId(Long businessId, WalletBusinessTypeEnum type);
}
