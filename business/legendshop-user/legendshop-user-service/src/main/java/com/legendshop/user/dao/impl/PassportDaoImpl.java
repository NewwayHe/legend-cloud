/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.dao.impl;

import cn.hutool.core.collection.CollUtil;
import cn.legendshop.jpaplus.impl.GenericDaoImpl;
import cn.legendshop.jpaplus.util.StringUtils;
import com.legendshop.user.dao.PassportDao;
import com.legendshop.user.entity.Passport;
import com.legendshop.user.entity.PassportItem;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

/**
 * @author legendshop
 */
@Repository
public class PassportDaoImpl extends GenericDaoImpl<Passport, Long> implements PassportDao {


	@Deprecated
	@Override
	public Passport getByUserId(Long userId) {
		return get("select * from ls_passport where user_id = ? order by update_time desc limit 1", Passport.class, userId);
	}

	@Override
	public Passport getByUserIdAndType(Long userId, String type) {
		return get("select * from ls_passport where user_id = ? AND type = ? limit 1", Passport.class, userId, type);
	}

	@Override
	public Passport getPassByIdentifier(String identifier) {
		return super.get("SELECT p.* FROM ls_passport p LEFT JOIN ls_passport_item pi ON p.id = pi.pass_port_id WHERE pi.third_party_identifier = ?", Passport.class, identifier);
	}

	@Override
	public String getOpenIdByUserId(Long userId, String source) {
		return super.get("SELECT pi.third_party_identifier FROM ls_passport p LEFT JOIN ls_passport_item pi ON p.id = pi.pass_port_id WHERE p.user_id = ? AND pi.source = ?", String.class, userId, source);
	}

	@Override
	public boolean clearPassportItem(Long userId, String source) {
		return super.update("DELETE i.* FROM ls_passport p LEFT JOIN ls_passport_item i ON p.id = i.pass_port_id WHERE p.user_id = ? AND i.source = ?", userId, source) > 0;
	}

	@Override
	public PassportItem authInfo(Long userId, String type, String source) {
		return get("select p.user_id AS userId, p.type AS type,pi.source AS source,pi.third_party_identifier AS thirdPartyIdentifier from ls_passport p LEFT JOIN ls_passport_item pi ON p.id = pi.pass_port_id where user_id = ? AND type = ? AND pi.source = ? LIMIT 1", PassportItem.class, userId, type, source);
	}

	@Override
	public List<String> getOpensByUserIds(List<Long> userIds, String source) {
		if (CollUtil.isEmpty(userIds) || StringUtils.isBlank(source)) {
			return Collections.emptyList();
		}
		StringBuilder sb = new StringBuilder("SELECT pi.third_party_identifier FROM ls_passport p LEFT JOIN ls_passport_item pi ON p.id = pi.pass_port_id WHERE p.user_id in(");
		for (Long userId : userIds) {
			sb.append(userId);
			sb.append(",");
		}
		sb.setLength(sb.length() - 1);
		sb.append(")");
		sb.append(" AND pi.source = ?");
		return query(sb.toString(), String.class, source);
	}
}
