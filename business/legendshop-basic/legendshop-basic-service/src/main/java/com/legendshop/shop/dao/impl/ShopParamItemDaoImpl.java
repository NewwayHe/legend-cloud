/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.dao.impl;

import cn.legendshop.jpaplus.impl.GenericDaoImpl;
import com.legendshop.basic.dto.SysParamValueItemDTO;
import com.legendshop.shop.dao.ShopParamItemDao;
import com.legendshop.shop.dto.ShopParamItemDTO;
import com.legendshop.shop.entity.ShopParamItem;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 商家配置项(ShopParamItem)表数据库访问层实现类
 *
 * @author legendshop
 * @since 2020-11-03 11:03:08
 */
@Repository
public class ShopParamItemDaoImpl extends GenericDaoImpl<ShopParamItem, Long> implements ShopParamItemDao {

	@Override
	public void deleteByShopId(List<Long> parentId) {
		if (CollectionUtils.isEmpty(parentId)) {
			return;
		}
		StringBuffer sql = new StringBuffer("delete from ls_shop_param_item where parent_id in(");
		parentId.forEach(e -> {
			sql.append("?,");
		});
		sql.setLength(sql.length() - 1);
		sql.append(")");
		update(sql.toString(), parentId.toArray());
	}

	@Override
	public void updateValueOnlyById(List<SysParamValueItemDTO> sysParamValueItemDTOList) {
		String sql = "update ls_shop_param_item set value = ? where id = ?";
		List<Object[]> list = new ArrayList<>();
		sysParamValueItemDTOList.forEach(sysParamValueItemDTO -> {
			list.add(new Object[]{sysParamValueItemDTO.getValue(), sysParamValueItemDTO.getId()});
		});
		batchUpdate(sql, list);
	}

	@Override
	public List<ShopParamItemDTO> queryByParentParamName(String shopParamName, Long shopId) {
		return query("select pi.* from ls_shop_params p ,ls_shop_param_item pi where pi.parent_id=p.id " +
				"and p.name=? and shop_id=?", ShopParamItemDTO.class, shopParamName, shopId);
	}

	@Override
	public List<ShopParamItem> getListByParentId(Long parentId) {
		return query("select * from ls_shop_param_item where parent_id = ?", ShopParamItem.class, parentId);
	}
}
