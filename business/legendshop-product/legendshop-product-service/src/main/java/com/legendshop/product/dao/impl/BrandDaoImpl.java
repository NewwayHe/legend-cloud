/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.dao.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.legendshop.jpaplus.impl.GenericDaoImpl;
import cn.legendshop.jpaplus.support.EntityCriterion;
import cn.legendshop.jpaplus.support.PageSupport;
import cn.legendshop.jpaplus.support.QueryMap;
import cn.legendshop.jpaplus.support.SimpleSqlQuery;
import com.legendshop.basic.enums.OpStatusEnum;
import com.legendshop.product.bo.BrandBO;
import com.legendshop.product.dao.BrandDao;
import com.legendshop.product.entity.Brand;
import com.legendshop.product.query.BrandQuery;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import static cn.legendshop.jpaplus.criterion.MatchMode.ANYWHERE;

/**
 * 品牌Dao
 *
 * @author legendshop
 */
@Repository
public class BrandDaoImpl extends GenericDaoImpl<Brand, Long> implements BrandDao {


	private final String sqlForQueryBrandListByProdTypeId = "select lb.id as id, lb.brand_name as brandName, lb.brand_pic as brandPic  " +
			"from ls_brand lb, ls_product_property_agg_brand lptb, ls_product_property_agg lpt where lb.id = lptb.brand_id and lptb.type_id = lpt.id and lb.delete_flag=0  and lptb.type_id = ? order by lptb.id";

	@Override
	public List<Brand> getAllBrand(Long shopId, String brandName) {
		EntityCriterion entityCriterion = new EntityCriterion(true).eq("status", "1").eq("deleteFlag", false);
		if (shopId.compareTo(0L) > 0) {
			entityCriterion.eq("shopId", shopId);
		}
		entityCriterion.like("brandName", brandName, ANYWHERE);
		entityCriterion.addAscOrder("seq");
		return queryByProperties(entityCriterion);
	}

	@Override
	public Brand getBrand(Long shopId, String brandName) {

		return get("select * from ls_brand where delete_flag=0 and status=1 and shop_id=? and brand_name=?", Brand.class, shopId, brandName);
	}

	@Override
	public PageSupport<Brand> queryPage(BrandQuery brandQuery) {
		SimpleSqlQuery query = new SimpleSqlQuery(Brand.class, brandQuery.getPageSize(), brandQuery.getCurPage());
		QueryMap map = new QueryMap();
		map.like("brandName", brandQuery.getBrandName(), ANYWHERE);
		map.put("status", brandQuery.getStatus());
		map.put("opStatus", brandQuery.getOpStatus());
		map.put("userName", brandQuery.getUserName());
		map.put("commend", brandQuery.getCommend());
		map.put("shopId", brandQuery.getShopId());
		map.like("shopName", brandQuery.getShopName(), ANYWHERE);
		query.setSqlAndParameter("Brand.queryBrandList", map);
		return querySimplePage(query);
	}

	@Override
	public boolean checkBrandByNameByShopId(String brandName, Long brandId) {
		List<String> list = null;
		if (ObjectUtil.isNotNull(brandId)) {
			list = query("select id from ls_brand where  brand_name = ? and delete_flag=0 and op_status=1 and id <> ? ", String.class, brandName, brandId);
		} else {
			list = query("select id from ls_brand where brand_name = ? and delete_flag=0 and op_status=1 ", String.class, brandName);
		}
		return CollUtil.isNotEmpty(list) && list.size() > 0;
	}

	@Override
	public List<BrandBO> getExportBrands(BrandQuery query) {
		if (ObjectUtil.isNotNull(query)) {
			StringBuilder sb = new StringBuilder();
			List<Object> obj = new ArrayList<Object>();
			sb.append("SELECT b.*,d.shop_name AS siteName,b.status FROM ls_brand b LEFT JOIN ls_shop_detail d ON b.shop_id=d.id  where b.delete_flag=0 ");
			if (CollUtil.isNotEmpty(query.getIdList())) {
				sb.append(" AND b.id in(");
				for (Long brandId : query.getIdList()) {
					sb.append("?,");
					obj.add(brandId);
				}
				sb.setLength(sb.length() - 1);
				sb.append(")");
			} else {
				if (ObjectUtil.isNotNull(query.getStatus())) {
					sb.append(" AND b.status=?");
					obj.add(query.getStatus());
				}
				if (StrUtil.isNotBlank(query.getBrandName())) {
					sb.append(" AND b.brand_name LIKE ?");
					obj.add("%" + query.getBrandName().trim() + "%");
				}
				if (ObjectUtil.isNotNull(query.getCommend())) {
					sb.append(" AND b.commend_flag = ?");
					obj.add(query.getCommend());
				}
			}
			return this.query(sb.toString(), BrandBO.class, obj.toArray());
		}
		return null;
	}

	@Override
	public int updateStatus(Integer status, Long id) {
		return update("update ls_brand set status=?, update_time= NOW()  where id =? ", status, id);
	}

	@Override
	public int updateOpStatus(Long commonId, Integer opStatus) {
		String sql = "update ls_brand set op_status=?, ";
		if (OpStatusEnum.PASS.getValue().equals(opStatus)) {
			sql += "status=1,";
		}
		sql += "update_time= NOW()  where id =? ";
		return update(sql, opStatus, commonId);
	}

	@Override
	public int updateDeleteFlag(Long brandId) {
		return update("update ls_brand set delete_flag=1, update_time= NOW()  where id =? ", brandId);
	}

}
