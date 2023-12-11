/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.dao.impl;

import cn.hutool.core.util.StrUtil;
import cn.legendshop.jpaplus.criterion.MatchMode;
import cn.legendshop.jpaplus.impl.GenericDaoImpl;
import cn.legendshop.jpaplus.support.CriteriaQuery;
import cn.legendshop.jpaplus.support.PageSupport;
import cn.legendshop.jpaplus.support.lambda.LambdaEntityCriterion;
import com.legendshop.user.dao.ShopSubUserDao;
import com.legendshop.user.entity.ShopSubUser;
import com.legendshop.user.query.ShopSubUserQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

/**
 * 商家子账号
 *
 * @author legendshop
 */
@Repository
public class ShopSubUserDaoImpl extends GenericDaoImpl<ShopSubUser, Long> implements ShopSubUserDao {
	@Override
	public PageSupport<ShopSubUser> queryUserPage(ShopSubUserQuery query) {
		CriteriaQuery criteriaQuery = new CriteriaQuery(ShopSubUser.class, query.getCurPage());
		criteriaQuery.setPageSize(query.getPageSize());
		if (StringUtils.isNotBlank(query.getUserAccount())) {
			criteriaQuery.like("userAccount", query.getUserAccount(), MatchMode.ANYWHERE);
		}

		if (null != query.getShopUserId()) {
			criteriaQuery.eq("shopUserId", query.getShopUserId());
		}

		if (null != query.getDelFlag()) {
			criteriaQuery.eq("delFlag", query.getDelFlag());
		}

		if (!CollectionUtils.isEmpty(query.getSubUserIds())) {
			criteriaQuery.in("id", query.getSubUserIds());
		}
		if (StrUtil.isNotBlank(query.getUsername())) {
			criteriaQuery.like("username", query.getUsername(), MatchMode.ANYWHERE);
		}
		criteriaQuery.addDescOrder("updateTime");
		criteriaQuery.addDescOrder("createTime");
		return this.queryPage(criteriaQuery);
	}

	@Override
	public ShopSubUser getUserAccount(String userAccount, Long shopUserId) {
		LambdaEntityCriterion<ShopSubUser> criterion = new LambdaEntityCriterion<>(ShopSubUser.class);
		criterion.eq(ShopSubUser::getUserAccount, userAccount);
		criterion.eq(ShopSubUser::getShopUserId, shopUserId);
		return getByProperties(criterion);
	}

	@Override
	public int updateStatusByUserName(Long userId, Boolean status) {
		return super.update("UPDATE ls_shop_sub_user SET lock_flag = ? WHERE id = ? ", status, userId);
	}

}
