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
import com.legendshop.pay.dto.UserWalletDetailedCentreDTO;
import com.legendshop.pay.entity.UserWalletDetailedCentre;
import com.legendshop.pay.enums.WalletCentreStatusEnum;

import java.util.List;

/**
 * 用户钱包详情中间表(UserWalletDetailedCentre)表数据库访问层
 *
 * @author legendshop
 * @since 2022-04-27 13:56:51
 */
public interface UserWalletDetailedCentreDao extends GenericDao<UserWalletDetailedCentre, Long> {

	/**
	 * 更新中间表状态
	 *
	 * @param id
	 * @param oldStatus
	 * @param newStatus
	 * @return
	 */
	Integer updateStatus(Long id, Integer oldStatus, Integer newStatus);

	/**
	 * 根据状态查询中间表
	 *
	 * @param status
	 * @return
	 */
	List<UserWalletDetailedCentreDTO> queryByStatus(WalletCentreStatusEnum status);
}
