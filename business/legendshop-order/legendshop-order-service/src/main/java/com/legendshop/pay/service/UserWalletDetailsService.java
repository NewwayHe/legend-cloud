/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.service;

import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.pay.dto.UserWalletDetailsDTO;
import com.legendshop.pay.enums.UserWalletAmountTypeEnum;
import com.legendshop.pay.enums.WalletBusinessTypeEnum;
import com.legendshop.pay.enums.WalletDetailsStateEnum;
import com.legendshop.pay.excel.UserWalletDetailsExcelDTO;
import com.legendshop.pay.query.UserWalletDetailsQuery;

import java.util.Date;
import java.util.List;

/**
 * 用户钱包收支记录详情(UserWalletDetails)表服务接口
 *
 * @author legendshop
 * @since 2021-03-13 14:44:00
 */
public interface UserWalletDetailsService {

	/**
	 * 保存记录
	 */
	@Deprecated
	List<Long> record(List<UserWalletDetailsDTO> userWalletDetailsList);

	/**
	 * 根据serialNo修改记录状态
	 */
	int updateStateByNo(Long serialNo, WalletDetailsStateEnum state);

	/**
	 * 根据Id修改记录状态
	 */
	@Deprecated
	int updateState(Long id, WalletDetailsStateEnum state, String remarks);

	@Deprecated
	int update(List<UserWalletDetailsDTO> userWalletDetailsList);

	/**
	 * 根据用户Id查询记录
	 */
	List<UserWalletDetailsDTO> queryByUserId(Long userId);

	List<UserWalletDetailsDTO> queryByUserId(Long userId, Date startDate, Date endDate);

	/**
	 * 分页查询
	 */
	PageSupport<UserWalletDetailsDTO> associatePage(UserWalletDetailsQuery query);

	/**
	 * 分页查询
	 */
	PageSupport<UserWalletDetailsDTO> pageList(UserWalletDetailsQuery query);


	/**
	 * 根据业务Id查询记录
	 */
	@Deprecated
	UserWalletDetailsDTO findUserPayDetails(Long businessId);

	/**
	 * 根据业务Id查询记录
	 *
	 * @param businessId
	 * @param type
	 * @return
	 */
	List<UserWalletDetailsDTO> findDetailsByBusinessId(Long businessId, WalletBusinessTypeEnum type);

	/**
	 * 根据业务Ids查询记录
	 */
	List<UserWalletDetailsDTO> findDetailsByBusinessIds(List<Long> businessIds, WalletBusinessTypeEnum type);

	/**
	 * 根据业务Ids查询记录
	 */
	List<UserWalletDetailsDTO> findDetailsByBusinessIds(List<Long> businessIds, WalletBusinessTypeEnum type, UserWalletAmountTypeEnum amountType);

	/**
	 * 根据业务Ids查询记录
	 *
	 * @param businessIds
	 * @param type
	 * @param state
	 * @return
	 */
	List<UserWalletDetailsDTO> findDetailsByBusinessIds(List<Long> businessIds, WalletBusinessTypeEnum type, WalletDetailsStateEnum state);

	UserWalletDetailsDTO getBySerialNo(Long serialNo);

	UserWalletDetailsDTO getById(Long id);

	/**
	 * 添加异常描述
	 */
	@Deprecated
	int abnormalMsg(Long id, String errorMsg);

	List<UserWalletDetailsDTO> findOrderPayDetails(List<Long> orderIds);

	Integer updateUserWalletDetail(UserWalletDetailsDTO details);

	/**
	 * 提现导出
	 *
	 * @param query
	 * @return
	 */
	List<UserWalletDetailsExcelDTO> walletExcel(UserWalletDetailsQuery query);

	/**
	 * 根据id获取提现明细详情
	 *
	 * @param id
	 * @return
	 */
	UserWalletDetailsDTO getDetailById(Long id);
}
