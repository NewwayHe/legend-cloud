/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.dao.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.legendshop.jpaplus.SQLOperation;
import cn.legendshop.jpaplus.criterion.MatchMode;
import cn.legendshop.jpaplus.impl.GenericDaoImpl;
import cn.legendshop.jpaplus.support.EntityCriterion;
import cn.legendshop.jpaplus.support.PageSupport;
import cn.legendshop.jpaplus.support.QueryMap;
import cn.legendshop.jpaplus.support.SimpleSqlQuery;
import com.legendshop.product.bo.CategoryBO;
import com.legendshop.product.bo.ProductCategoryRelationBO;
import com.legendshop.product.dao.CategoryDao;
import com.legendshop.product.entity.Category;
import com.legendshop.product.enums.ProductDelStatusEnum;
import com.legendshop.product.query.ProductCategoryRelationQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 分类Dao实现类, category内存计算参考CategoryManagerUtil
 *
 * @author legendshop
 */
@Repository
public class CategoryDaoImpl extends GenericDaoImpl<Category, Long> implements CategoryDao {

	/**
	 * 基础的商品分类SQL.
	 */
	private static String BASE_SQL_FOR_CATEGORY = "select n.id as id, n.parent_id as parentId, n.name as name ,n.status  as status,n.type, " +
			"n.agg_id as typeId,n.keyword as keyword,n.cat_desc as catDesc,n.title as title,n.grade as grade,n.seq as seq,n.recomm_con as recommCon,n.icon as icon from ls_category n ";

	/**
	 * The sql for get navigation nsort.
	 */
	private static String sqlForGetNavigationNsort = BASE_SQL_FOR_CATEGORY + "where  n.status = 1 and  n.type= ?   and  n.parent_id=? order by n.seq";

	/**
	 * The sql for get navigation nsort.
	 */
	private static String sqlGetProductCategory = BASE_SQL_FOR_CATEGORY + "where  n.status = 1 and  n.type= ? order by n.seq";

	private static String sqlGetAllProductCategory = BASE_SQL_FOR_CATEGORY + "where n.type= ? order by n.seq";

	/**
	 * The sql for get sort.
	 **/
	private static String sqlForFindCategory = BASE_SQL_FOR_CATEGORY + "where  n.status = 1 and  n.type= ? and n.parent_id = ? order by n.seq";


	/**
	 * The sql for get navigation nsort.
	 */
	private static String sqlGetCategory = "select n.id as id, n.parent_id as parentId, n.name as name ,n.pic as pic ,n.status  as status, n.type_id as typeId,n.keyword as keyword,n.cat_desc as catDesc,n.title as title,n.grade as grade,n.seq as seq from ls_category n  where  n.status = 1 and  n.type= ? order by n.grade,n.seq";

	@Override
	public String getCategoryNameById(Long id) {
		return get("select name from ls_category where id=? ", String.class, id);
	}

	@Override
	public int updateStatus(int status, Long id) {
		return update("update ls_category c set status=?,update_time=NOW() where id= ?", status, id);
	}

	@Override
	public List<CategoryBO> queryByParentId(Long parentId, int status) {
		return query(getSQL("Category.queryByParentId"), CategoryBO.class, parentId, status, status);
	}

	@Override
	public List<Category> queryByParentIdAndName(Long parentId, String name, int status) {
		return queryByProperties(new EntityCriterion(true).eq("parentId", parentId).eq("status", status).like("name", name, MatchMode.ANYWHERE));
	}

	@Override
	public CategoryBO getCategoryBOById(Long aLong) {
		return get(getSQL("Category.queryById"), CategoryBO.class, aLong);
	}

	@Override
	public CategoryBO queryByIdName(String name) {
		return get(getSQL("Category.queryByIdName"), CategoryBO.class, name);
	}

	@Override
	public List<CategoryBO> queryAllOnline() {
		return query(getSQL("Category.queryBriefAllOnline"), CategoryBO.class);
	}

	@Override
	public PageSupport<ProductCategoryRelationBO> queryProductList(ProductCategoryRelationQuery relationQuery) {
		SimpleSqlQuery query = new SimpleSqlQuery(ProductCategoryRelationBO.class, relationQuery.getPageSize(), relationQuery.getCurPage());
		QueryMap map = new QueryMap();
		if (relationQuery.getShopId() == null) {
			//【平台】查询类目下的商品
			map.put("categoryId1", relationQuery.getCategoryId());
			map.put("categoryId2", relationQuery.getCategoryId());
			map.put("categoryId3", relationQuery.getCategoryId());
		} else {
			//【商家】查询类目下的商品
			map.put("shopCategoryId1", relationQuery.getCategoryId());
			map.put("shopCategoryId2", relationQuery.getCategoryId());
			map.put("shopCategoryId3", relationQuery.getCategoryId());
			map.put("shopId", relationQuery.getShopId());
		}

		map.like("productName", relationQuery.getProductName(), MatchMode.ANYWHERE);
		map.like("shopName", relationQuery.getShopName(), MatchMode.ANYWHERE);

		map.put("delStatus", ProductDelStatusEnum.PROD_SHOP_DELETE.getValue());
		if (ObjectUtil.isNotEmpty(relationQuery.getSort())) {
			StringBuffer buffer = new StringBuffer();
			buffer.append("p.").append(relationQuery.getSort());
			map.put("orderBy", "order by " + buffer + " desc");
		}
		String sqlId = relationQuery.getShopId() == null ? "Category.queryProductList" : "Category.queryProductListInShop";
		query.setSqlAndParameter(sqlId, map);
		return querySimplePage(query);
	}

	@Override
	public List<CategoryBO> queryAllOnlineByProductPropertyAggId() {
		return query(getSQL("Category.queryAllOnlineByProductPropertyAggId"), CategoryBO.class);
	}

	@Override
	public List<Long> queryProductIdsListById(Long id) {
		QueryMap map = new QueryMap();
		map.put("categoryId1", id);
		map.put("delStatus", ProductDelStatusEnum.PROD_SHOP_DELETE.getValue());
		SQLOperation operation = this.getSQLAndParams("Category.queryProductListById", map);
		return this.query(operation.getSql(), Long.class, operation.getParams());
	}

}
