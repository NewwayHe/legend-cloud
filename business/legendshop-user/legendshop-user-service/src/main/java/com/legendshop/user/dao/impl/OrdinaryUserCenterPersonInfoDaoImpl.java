/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.dao.impl;

import cn.legendshop.jpaplus.GenericJdbcDao;
import cn.legendshop.jpaplus.impl.GenericDaoImpl;
import com.legendshop.common.datasource.NonTable;
import com.legendshop.user.bo.UserSecureBO;
import com.legendshop.user.dao.UserCenterPersonInfoDao;
import com.legendshop.user.dto.MyPersonInfoDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * @author legendshop
 */
@Repository
@Slf4j
public class OrdinaryUserCenterPersonInfoDaoImpl extends GenericDaoImpl<NonTable, Long> implements UserCenterPersonInfoDao {

	@Autowired
	private GenericJdbcDao genericJdbcDao;

	private String sqlForGetPersonInfo = " select ls.id,lud.nick_name as nickName ,ls.username as userName, lud.email, " +
			" lud.sex ,lud.province_id, lud.city_id, lud.area_id, lud.we_chat_number, ls.mobile, ls.avatar as portraitPic,lud.we_chat_number," +
			" lud.score as score, lud.available_predeposit as availablePredeposit, lud.available_integral, lud.cumulative_integral, " +
			" if(ls.password = null, 0, 1) as setUpLoginPassword, if(lud.pay_password = null, 0, 1) as setUpPaymentPassword, lud.close_recommend as closeRecommend " +
			" from ls_ordinary_user ls,ls_user_detail lud where ls.id=lud.user_id and lud.user_id=? ";

	@Override
	public MyPersonInfoDTO getPersonInfo(Long userId) {
		return genericJdbcDao.get(sqlForGetPersonInfo, MyPersonInfoDTO.class, userId);
	}

	@Override
	public void updatePersonInfo(MyPersonInfoDTO myPersonInfo) {

	}


	@Override
	public int updateNickNameByUserId(Long userId, String nickName) {
		String sql = "update ls_user_detail set nick_name = ? where user_id = ?";
		int update = genericJdbcDao.update(sql, nickName, userId);
		if (update > 0) {
			genericJdbcDao.update("update ls_ordinary_user set nick_name = ? where id = ?", nickName, userId);
		}
		return update;
	}

	@Override
	public int updateSexByUserId(Long userId, String sex) {
		String sql = "update ls_user_detail set sex = ? where user_id = ?";
		return genericJdbcDao.update(sql, sex, userId);
	}

	@Override
	public UserSecureBO getUserSecureByUserId(Long userId) {
		String sql = "SELECT o.id,o.`password`,o.mobile,d.pay_password FROM ls_ordinary_user o LEFT JOIN ls_user_detail d ON o.id = d.user_id WHERE o.id= ? limit 1";
		return genericJdbcDao.get(sql, UserSecureBO.class, userId);
	}


}
