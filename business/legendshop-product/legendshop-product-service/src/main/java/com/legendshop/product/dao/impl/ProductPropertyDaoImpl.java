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
import cn.hutool.core.util.StrUtil;
import cn.legendshop.jpaplus.criterion.MatchMode;
import cn.legendshop.jpaplus.impl.GenericDaoImpl;
import cn.legendshop.jpaplus.support.EntityCriterion;
import cn.legendshop.jpaplus.support.PageSupport;
import cn.legendshop.jpaplus.support.QueryMap;
import cn.legendshop.jpaplus.support.SimpleSqlQuery;
import com.legendshop.product.dao.ProductPropertyDao;
import com.legendshop.product.dao.ProductPropertyValueDao;
import com.legendshop.product.dto.ProductPropertyDTO;
import com.legendshop.product.entity.ProductProperty;
import com.legendshop.product.entity.ProductPropertyValue;
import com.legendshop.product.query.ProductPropertyQuery;
import com.legendshop.product.service.convert.ProductPropertyConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * 商品属性Dao.
 *
 * @author legendshop
 */
@Repository
public class ProductPropertyDaoImpl extends GenericDaoImpl<ProductProperty, Long> implements ProductPropertyDao {


	@Autowired
	private ProductPropertyValueDao productPropertyValueDao;

	@Autowired
	private ProductPropertyConverter productPropertyConverter;

	private final String prodPropListquery = "select lpp.id as propId, lpp.prop_name as propName, lpp.memo as memo, lpp.required_flag as requiredFlag, lpp.multi_flag as MultiFlag," +
			"    lpp.sequence as sequence, lpp.status as status, lpp.type as type, lptp.seq as seq from ls_product_property lpp,ls_product_property_agg_specification lptp,ls_product_property_agg lpt " +
			"    where lpp.id = lptp.prop_id and lptp.type_id = lpt.id and lpt.id = ? order by lptp.seq";

	private final String parameterPropertyListquery = "SELECT DISTINCT prop.* from ls_product_property prop,ls_product_property_agg_param aggParam where prop.id =aggParam.prop_id " +
			"and prop.attribute_type ='P' and prop.source=\"SYS\" and prop.delete_flag=0 and aggParam.agg_id in( ### ) ORDER BY prop.id";

	private final String specificationPropertyListquery = "SELECT DISTINCT prop.id,prop.prop_name,prop.memo,prop.type,prop.attribute_type,prop.source from ls_product_property prop,ls_product_property_agg_specification" +
			" aggSpecification where aggSpecification.prop_id=prop.id and prop.attribute_type ='S' and prop.source=\"SYS\" and prop.delete_flag=0 and aggSpecification.agg_id in( ### ) ORDER BY prop.id";

	private final String sqlForGetProductProperty1 = "select distinct pp.* from ls_product_property pp left join ls_product_property_agg_specification ptp on pp.id = ptp.prop_id left join ls_product_property_agg pt on pt.id = ptp.type_id " +
			"left join ls_category c on c.type_id = ptp.type_id and c.id = ? where pp.id in (";


	private final String sqlForGetProductProperty2 = ") order by ptp.seq";

	@Override
	public List<ProductProperty> getProductProperty(String propName) {
		return this.queryByProperties(new EntityCriterion().eq("propName", propName));

	}

	@Override
	public ProductProperty getProperty(String propName, String type, Long shopId) {
		return get("select * from ls_product_property where delete_flag=0 and shop_id=? and prop_name=? and attribute_type=?", ProductProperty.class, shopId, propName, type);
	}

	@Override
	public List<ProductProperty> getProductPropertyList(Long proTypeId) {
		return query(prodPropListquery, ProductProperty.class, proTypeId);
	}

	@Override
	public List<ProductProperty> getParameterPropertyList(List<Long> aggId) {
		StringBuffer sb = new StringBuffer("");
		for (Long id : aggId) {
			sb.append("?,");
		}
		sb.setLength(sb.length() - 1);
		return query(StrUtil.replace(parameterPropertyListquery, "###", sb.toString()), ProductProperty.class, aggId.toArray());
	}

	@Override
	public List<ProductProperty> getSpecificationPropertyList(List<Long> aggId) {
		StringBuffer sb = new StringBuffer("");
		for (Long id : aggId) {
			sb.append("?,");
		}
		sb.setLength(sb.length() - 1);
		return query(StrUtil.replace(specificationPropertyListquery, "###", sb.toString()), ProductProperty.class, aggId.toArray());
	}

	/**
	 * 只是获取ID值,并没有保存到数据库
	 */
	@Override
	public Long saveCustomProperty(ProductProperty newProperty) {
		return createId();
	}

	@Override
	public List<ProductPropertyDTO> queryUserPropByProductId(Long productId) {
		List<ProductProperty> propList = queryByProperties(new EntityCriterion().eq("productId", productId));
		List<ProductPropertyValue> valueList = productPropertyValueDao.getAllProductPropertyValue(productPropertyConverter.to(propList));
		return null;
	}

	@Override
	public List<ProductProperty> getProductProperty(List<Long> propIds, Long categoryId) {

		StringBuffer sb = new StringBuffer(sqlForGetProductProperty1);
		List<Long> params = new ArrayList<Long>(propIds);
		params.add(0, categoryId);
		for (int i = 0; i < propIds.size(); i++) {
			sb.append("?,");
		}
		sb.setLength(sb.length() - 1);
		sb.append(sqlForGetProductProperty2);
		return query(sb.toString(), ProductProperty.class, params.toArray());
	}

	@Override
	public PageSupport<ProductProperty> getProductPropertyPage(ProductPropertyQuery productPropertyQuery) {
		SimpleSqlQuery query = new SimpleSqlQuery(ProductProperty.class, productPropertyQuery.getPageSize(), productPropertyQuery.getCurPage());
		QueryMap map = new QueryMap();
		query.setPageSize(15);
		map.put("isRuleAttributes", productPropertyQuery.getAttributeType());
		map.like("propName", productPropertyQuery.getPropName(), MatchMode.ANYWHERE);
		map.like("memo", productPropertyQuery.getMemo(), MatchMode.ANYWHERE);
		map.put("proTypeId", productPropertyQuery.getProTypeId());

		query.setSqlAndParameter("ProductProperty.queryProdPropertyList", map);
		return querySimplePage(query);
	}

	@Override
	public PageSupport<ProductProperty> getProductPropertyParamPage(ProductPropertyQuery productPropertyQuery) {
		SimpleSqlQuery query = new SimpleSqlQuery(ProductProperty.class, productPropertyQuery.getPageSize(), productPropertyQuery.getCurPage());
		QueryMap map = new QueryMap();
		query.setPageSize(15);
		map.put("isRuleAttributes", productPropertyQuery.getAttributeType());
		map.like("propName", productPropertyQuery.getPropName(), MatchMode.ANYWHERE);
		map.like("memo", productPropertyQuery.getMemo(), MatchMode.ANYWHERE);
		map.put("proTypeId", productPropertyQuery.getProTypeId());

		query.setSqlAndParameter("ProductProperty.queryProdParamList", map);
		return querySimplePage(query);
	}

	@Override
	public PageSupport<ProductProperty> queryProductPropertyPage(ProductPropertyQuery productPropertyQuery) {
		SimpleSqlQuery query = new SimpleSqlQuery(ProductProperty.class, productPropertyQuery.getPageSize(), productPropertyQuery.getCurPage());
		QueryMap map = new QueryMap();
		map.put("attributeType", productPropertyQuery.getAttributeType());
		// 不是用户自定义的属性和参数,是系统统一定义的属性
		map.put("source", productPropertyQuery.getSource());
		map.put("shopId", productPropertyQuery.getShopId());
		map.put("paramsGroupId", ObjectUtil.isNotEmpty(productPropertyQuery.getParamsGroupId()) ? productPropertyQuery.getParamsGroupId() : 0);
		map.put("deleteFlag", false);
		map.like("propName", productPropertyQuery.getPropName(), MatchMode.ANYWHERE);
		map.like("memo", productPropertyQuery.getMemo(), MatchMode.ANYWHERE);
		map.put("type", productPropertyQuery.getType());
		query.setSqlAndParameter("ProductProperty.queryProductPropertyPage", map);
		return querySimplePage(query);
	}

	@Override
	public Integer findProductId(Long propId) {
		Integer productId = get("SELECT product_id FROM ls_product_property WHERE prop_id = ?", Integer.class, propId);
		return productId;
	}

	@Override
	public int updateDeleteFlag(Long id, Boolean deleteFlag) {
		return update("update ls_product_property set delete_flag= ? ,update_time= NOW() where id = ? ", deleteFlag, id);
	}


	@Override
	public List<String> getPropertyValueList(Long id) {
		return query("select name from ls_product_property_value  where prop_id=?", String.class, id);
	}

	@Override
	public List<String> queryAttachmentByUrl(String url) {
		String sql = "select name from ls_product_property_image where url like ?";
		List<Object> obj = new ArrayList<>();
		obj.add("%" + url + "%");
		return this.query(sql, String.class, obj.toArray());
	}

}
