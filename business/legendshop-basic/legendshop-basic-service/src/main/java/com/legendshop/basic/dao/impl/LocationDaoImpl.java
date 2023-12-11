/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.dao.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.legendshop.jpaplus.impl.GenericDaoImpl;
import cn.legendshop.jpaplus.support.CriteriaQuery;
import cn.legendshop.jpaplus.support.EntityCriterion;
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.basic.dao.LocationDao;
import com.legendshop.basic.dto.LocationDTO;
import com.legendshop.basic.dto.LocationDetailDTO;
import com.legendshop.basic.entity.Location;
import com.legendshop.basic.query.LocationQuery;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * 地区DAO实现
 *
 * @author legendshop
 */
@Repository
public class LocationDaoImpl extends GenericDaoImpl<Location, Long> implements LocationDao {


	@Override
	public List<Location> loadLocation(Integer grade) {
		if (null == grade) {
			return Collections.emptyList();
		}
		return queryByProperties(new EntityCriterion().eq("grade", grade));
	}

	@Override
	public List<Location> getChildrenList(Long parentId) {
		if (null == parentId) {
			return Collections.emptyList();
		}
		return queryByProperties(new EntityCriterion().eq("parentId", parentId));
	}

	@Override
	public Long getIdByName(String name, Integer grade) {
		return this.getLongResult("select id from ls_location where grade = ? and name = ?", grade, name);
	}

	@Override
	public Location getByName(String name, Integer grade) {
		return this.get("select * from ls_location where grade = ? and name = ?", Location.class, grade, name);
	}

	@Override
	public Long getIdByGradeAndNameAndParentId(String name, Integer grade, Long parentId) {
		Location location = this.get("select id from ls_location where grade = ? and name = ? and parent_id=? limit 1", Location.class, grade, name, parentId);
		if (null == location) {
			location = this.get("SELECT id FROM ls_location WHERE grade = ? AND parent_id=? and NAME LIKE ? limit 1", Location.class, grade, parentId, new StringBuilder().append(name.charAt(0)).append("%"));
		}
		return Optional.ofNullable(location).map(Location::getId).orElse(null);
	}

	@Override
	public LocationDTO getByGradeAndNameAndParentId(String name, Integer grade, Long parentId) {
		return this.get("select id, name from ls_location where grade = ? and parent_id=? order by name = ? desc limit 1", LocationDTO.class, grade, parentId, name);
	}


	@Override
	public PageSupport<Location> queryLocationByGrade(LocationQuery locationQuery) {
		CriteriaQuery query = new CriteriaQuery(LocationQuery.class, locationQuery.getPageSize(), locationQuery.getCurPage());

		query.eq("grade", locationQuery.getGrade());

		query.eq("parentId", locationQuery.getParentId());
		if (ObjectUtil.isEmpty(locationQuery.getParentId()) && ObjectUtil.isEmpty(locationQuery.getGrade())) {
			query.eq("grade", 1);
		}
		return queryPage(query);
	}

	@Override
	public LocationDetailDTO getDetailAddress(LocationDetailDTO locationDetailDTO) {
		String sql = getSQL("Location.getDetailAddress");
		return get(sql, LocationDetailDTO.class, locationDetailDTO.getProvinceId(), locationDetailDTO.getCityId(), locationDetailDTO.getAreaId(), locationDetailDTO.getStreetId());
	}

}
