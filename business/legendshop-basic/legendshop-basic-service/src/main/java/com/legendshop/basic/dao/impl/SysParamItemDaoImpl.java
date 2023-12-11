/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.dao.impl;

import cn.legendshop.jpaplus.SQLOperation;
import cn.legendshop.jpaplus.criterion.MatchMode;
import cn.legendshop.jpaplus.impl.GenericDaoImpl;
import cn.legendshop.jpaplus.support.CriteriaQuery;
import cn.legendshop.jpaplus.support.PageSupport;
import cn.legendshop.jpaplus.support.QueryMap;
import com.legendshop.basic.dao.SysParamItemDao;
import com.legendshop.basic.dto.SysParamItemDTO;
import com.legendshop.basic.dto.SysParamValueItemDTO;
import com.legendshop.basic.entity.SysParamItem;
import com.legendshop.basic.query.SysParamItemQuery;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * (SysParamItem)表数据库访问层实现类
 *
 * @author legendshop
 * @since 2020-08-28 14:17:39
 */
@Repository
public class SysParamItemDaoImpl extends GenericDaoImpl<SysParamItem, Long> implements SysParamItemDao {


	@Override
	public PageSupport<SysParamItem> queryPageList(SysParamItemQuery sysParamItemQuery) {
		CriteriaQuery cq = new CriteriaQuery(SysParamItem.class, sysParamItemQuery.getPageSize(), sysParamItemQuery.getCurPage());
		cq.eq("parentId", sysParamItemQuery.getParentId());
		cq.eq("group", sysParamItemQuery.getGroup());
		cq.eq("enabled", sysParamItemQuery.getEnabled());
		cq.like("desc", sysParamItemQuery.getDesc(), MatchMode.ANYWHERE);
		// 排序
		cq.addDescOrder("sort");
		return queryPage(cq);
	}


	@Override
	public List<SysParamItem> getListByParentId(Long parentId) {
		return query("select * from ls_sys_param_item where parent_id = ?", SysParamItem.class, parentId);
	}

	@Override
	public void updateValueOnlyById(List<SysParamValueItemDTO> sysParamValueItemDTOS) {

		String sql = "update ls_sys_param_item set value = ?, update_time = NOW() where id = ?";
		List<Object[]> list = new ArrayList<>();
		sysParamValueItemDTOS.forEach(sysParamValueItemDTO -> {
			list.add(new Object[]{sysParamValueItemDTO.getValue(), sysParamValueItemDTO.getId()});
		});
		batchUpdate(sql, list);
	}


	@Override
	public Boolean getEnabledByParentId(Long parentId) {
		String sql = "select value from ls_sys_param_item where parent_id=? and key_word = 'enabled'";
		String s = get(sql, String.class, parentId);
		return Boolean.parseBoolean(s);
	}

	@Override
	public List<SysParamItemDTO> getEnabledByParentIds(List<Long> parentIds) {
		QueryMap map = new QueryMap();
		map.in("parentId", parentIds);

		SQLOperation operation = this.getSQLAndParams("SystemParamItem.getEnabledByParentIds", map);
		return query(operation.getSql(), operation.getParams(), new BeanPropertyRowMapper<>(SysParamItemDTO.class));
	}

	@Override
	public void batchAddItems(List<SysParamItemDTO> itemDTOList) {
		String sql = "insert into ls_sys_param_item(id,parent_id,des,key_word,value,data_type,update_time) values (?,?,?,?,?,?,?)";
		List<Object[]> list = new ArrayList<>();
		itemDTOList.forEach(t -> {
			list.add(new Object[]{this.createId(), t.getParentId(), t.getDes(), t.getKeyWord(), t.getValue(), t.getDataType(), t.getUpdateTime()});
		});
		batchUpdate(sql, list);
	}
}
