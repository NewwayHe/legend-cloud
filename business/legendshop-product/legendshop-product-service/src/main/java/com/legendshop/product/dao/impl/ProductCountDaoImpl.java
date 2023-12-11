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
import cn.legendshop.jpaplus.SQLOperation;
import cn.legendshop.jpaplus.criterion.MatchMode;
import cn.legendshop.jpaplus.impl.GenericDaoImpl;
import cn.legendshop.jpaplus.support.PageSupport;
import cn.legendshop.jpaplus.support.QueryMap;
import cn.legendshop.jpaplus.support.SimpleSqlQuery;
import com.legendshop.common.datasource.NonTable;
import com.legendshop.product.bo.CategoryBO;
import com.legendshop.product.dao.ProductCountDao;
import com.legendshop.product.dto.*;
import com.legendshop.product.query.*;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * 商品数量统计报表
 *
 * @author legendshop
 */
@Repository
public class ProductCountDaoImpl extends GenericDaoImpl<NonTable, Long> implements ProductCountDao {

	@Override
	public Integer queryProdSpuCount(Date startDate, Date endDate, Long shopId) {
		QueryMap map = new QueryMap();
		map.put("startDate", startDate);
		map.put("endDate", endDate);
		map.put("shopId", shopId);

		SQLOperation operation = this.getSQLAndParams("ProductCount.queryProdSpuCount", map);
		return get(operation.getSql(), operation.getParams(), new SingleColumnRowMapper<>(Integer.class));

	}

	@Override
	public Integer queryProdSkuCount(Date startDate, Date endDate, Long shopId) {
		QueryMap map = new QueryMap();
		map.put("startDate", startDate);
		map.put("endDate", endDate);
		map.put("shopId", shopId);

		SQLOperation operation = this.getSQLAndParams("ProductCount.queryProdSkuCount", map);
		return get(operation.getSql(), operation.getParams(), new SingleColumnRowMapper<>(Integer.class));

	}

	@Override
	public Integer queryProdSkuSaleCount(Date startDate, Date endDate, Long shopId) {
		QueryMap map = new QueryMap();
		map.put("startDate", startDate);
		map.put("endDate", endDate);
		map.put("shopId", shopId);

		SQLOperation operation = this.getSQLAndParams("ProductCount.queryProdSkuSaleCount", map);
		return get(operation.getSql(), operation.getParams(), new SingleColumnRowMapper<>(Integer.class));

	}

	@Override
	public Integer queryProdViews(Date startDate, Date endDate, Long shopId) {
		QueryMap map = new QueryMap();
		map.put("startDate", startDate);
		map.put("endDate", endDate);
		map.put("shopId", shopId);

		SQLOperation operation = this.getSQLAndParams("ProductCount.queryProdViews", map);
		return get(operation.getSql(), operation.getParams(), new SingleColumnRowMapper<>(Integer.class));

	}

	@Override
	public List<ProductDataSkuByCategoryDTO> queryProdSkuByCategory(Date endDate, Long shopId) {
		QueryMap map = new QueryMap();
		map.put("endDate", endDate);
		map.put("shopId", shopId);

		SQLOperation operation = this.getSQLAndParams("ProductCount.querySkuByCategory", map);

		return query(operation.getSql(), operation.getParams(), new BeanPropertyRowMapper<>(ProductDataSkuByCategoryDTO.class));

	}

	@Override
	public List<ProductDataSkuByCategoryDTO> queryShopProdSkuByCategory(Date endDate, Long shopId) {
		QueryMap map = new QueryMap();
		map.put("endDate", endDate);
		map.put("shopId", shopId);

		SQLOperation operation = this.getSQLAndParams("ProductCount.queryShopProdSkuByCategory", map);

		return query(operation.getSql(), operation.getParams(), new BeanPropertyRowMapper<>(ProductDataSkuByCategoryDTO.class));

	}

	@Override
	public List<ProductDataShopDTO> queryShopList() {
		return query(getSQL("ProductCount.queryAllShop"), ProductDataShopDTO.class);
	}

	@Override
	public List<ProductDataGoodDTO> queryGoodList(Long shopId) {
		QueryMap map = new QueryMap();
		map.put("shopId", shopId);

		SQLOperation operation = this.getSQLAndParams("ProductCount.queryAllGood", map);
		return query(operation.getSql(), operation.getParams(), new BeanPropertyRowMapper<>(ProductDataGoodDTO.class));
	}

	@Override
	public ProductDataGoodsByShopDTO queryShopGoodsCount(Long id, Date startDate, Date endDate) {

		QueryMap map = new QueryMap();
		map.put("shopId", id);
		map.put("startDate", startDate);
		map.put("endDate", endDate);
		SQLOperation operationSpu = this.getSQLAndParams("ProductCount.queryProdSpuCount", map);
		SQLOperation operationSku = this.getSQLAndParams("ProductCount.queryProdSkuCount", map);
		SQLOperation operationSale = this.getSQLAndParams("ProductCount.queryProdSkuSaleCount", map);
		Integer spu = get(operationSpu.getSql(), operationSpu.getParams(), new SingleColumnRowMapper<>(Integer.class));
		Integer sku = get(operationSku.getSql(), operationSku.getParams(), new SingleColumnRowMapper<>(Integer.class));
		Integer sale = get(operationSale.getSql(), operationSale.getParams(), new SingleColumnRowMapper<>(Integer.class));

		ProductDataGoodsByShopDTO dto = new ProductDataGoodsByShopDTO();
		dto.setSpuNum(spu);
		dto.setSkuNum(sku);
		dto.setSkuNumSale(sale);

		return dto;
	}

	@Override
	public ProductDataViewLineDTO queryProductViewLineByShopId(ProductDataViewQuery viewQuery) {
		QueryMap map = new QueryMap();
		map.put("shopId", viewQuery.getShopId());
		map.put("startDate", viewQuery.getStartDate());
		map.put("endDate", viewQuery.getEndDate());
		SQLOperation operation = this.getSQLAndParams("ProductCount.queryProductViewLineByShopId", map);
		ProductDataViewLineDTO lineDTO = get(operation.getSql(), operation.getParams(), new BeanPropertyRowMapper<>(ProductDataViewLineDTO.class));
		if (lineDTO == null) {
			lineDTO = new ProductDataViewLineDTO(0);
		}
		lineDTO.setPeople(Optional.ofNullable(lineDTO.getH5People()).orElse(0) + Optional.ofNullable(lineDTO.getMiniPeople()).orElse(0));
		lineDTO.setFrequency(Optional.ofNullable(lineDTO.getH5Frequency()).orElse(0) + Optional.ofNullable(lineDTO.getMiniFrequency()).orElse(0));
		return lineDTO;
	}

	@Override
	public ProductDataViewLineDTO queryProductViewLineAll(ProductDataViewQuery viewQuery) {
		QueryMap map = new QueryMap();
		map.put("shopId", viewQuery.getShopId());
		map.put("goodId", viewQuery.getGoodId());
		map.put("startDate", viewQuery.getStartDate());
		map.put("endDate", viewQuery.getEndDate());
		SQLOperation operation = this.getSQLAndParams("ProductCount.queryProductViewLineAll", map);
		ProductDataViewLineDTO lineDTO = get(operation.getSql(), operation.getParams(), new BeanPropertyRowMapper<>(ProductDataViewLineDTO.class));
		if (lineDTO == null) {
			lineDTO = new ProductDataViewLineDTO(0);
		}
		lineDTO.setH5People(Optional.ofNullable(lineDTO.getH5People()).orElse(0)
				+ Optional.ofNullable(lineDTO.getMpPeople()).orElse(0)
		);
		lineDTO.setH5Frequency(Optional.ofNullable(lineDTO.getH5Frequency()).orElse(0)
				+ Optional.ofNullable(lineDTO.getMpFrequency()).orElse(0)
		);
		return lineDTO;
	}

	@Override
	public ProductDataViewLineDTO queryProductViewLineByProdId(ProductDataViewQuery viewQuery) {
		QueryMap map = new QueryMap();
		map.put("goodId", viewQuery.getGoodId());
		map.put("startDate", viewQuery.getStartDate());
		map.put("endDate", viewQuery.getEndDate());
		SQLOperation operation = this.getSQLAndParams("ProductCount.queryProductViewLineByProdId", map);
		ProductDataViewLineDTO lineDTO = get(operation.getSql(), operation.getParams(), new BeanPropertyRowMapper<>(ProductDataViewLineDTO.class));
		if (lineDTO == null) {
			lineDTO = new ProductDataViewLineDTO(0);
		}
		lineDTO.setPeople(Optional.ofNullable(lineDTO.getH5People()).orElse(0) + Optional.ofNullable(lineDTO.getMiniPeople()).orElse(0));
		lineDTO.setFrequency(Optional.ofNullable(lineDTO.getH5Frequency()).orElse(0) + Optional.ofNullable(lineDTO.getMiniFrequency()).orElse(0));
		return lineDTO;
	}

	@Override
	public PageSupport<ProductDataViewPageDTO> getProductViewPage(ProductDataViewQuery viewQuery) {
		SimpleSqlQuery query = new SimpleSqlQuery(ProductDataViewPageDTO.class, viewQuery.getPageSize(), viewQuery.getCurPage());
		QueryMap map = new QueryMap();
		map.put("shopId", viewQuery.getShopId());
		map.like("shopName", viewQuery.getShopName(), MatchMode.ANYWHERE);
		map.put("goodId", viewQuery.getGoodId());
		map.like("goodName", viewQuery.getGoodName(), MatchMode.ANYWHERE);
		map.put("startDate", viewQuery.getStartDate());
		map.put("endDate", viewQuery.getEndDate());
		if (ObjectUtil.isNotEmpty(viewQuery.getProp()) && ObjectUtil.isNotEmpty(viewQuery.getOrder())) {
			map.put("orderBy", " order by " + viewQuery.getProp() + " " + viewQuery.getOrder());
		}
//		OrderBy orderBy = new OrderBy(viewQuery.getProp(), viewQuery.getOrder());
//		map.addOrder(orderBy, Arrays.asList("people","frequency", "mpPeople","mpFrequency","miniPeople","miniFrequency","appPeople","appFrequency","h5People","h5Frequency"));
		query.setSqlAndParameter("ProductCount.queryProdViewDetailPage", map);
		PageSupport<ProductDataViewPageDTO> page = querySimplePage(query);
		if (CollUtil.isNotEmpty(page.getResultList())) {
			for (ProductDataViewPageDTO viewPageDTO : page.getResultList()) {
				viewPageDTO.setPeople(Optional.ofNullable(viewPageDTO.getH5People()).orElse(0) + Optional.ofNullable(viewPageDTO.getMiniPeople()).orElse(0));
				viewPageDTO.setFrequency(Optional.ofNullable(viewPageDTO.getH5Frequency()).orElse(0) + Optional.ofNullable(viewPageDTO.getMiniFrequency()).orElse(0));
			}
		}
		return page;
	}

	@Override
	public List<ProductDataViewPageDTO> getProductViewPageExcel(ProductDataViewQuery viewQuery) {

		QueryMap map = new QueryMap();
		map.put("shopId", viewQuery.getShopId());
		map.put("goodId", viewQuery.getGoodId());
		map.put("startDate", viewQuery.getStartDate());
		map.put("endDate", viewQuery.getEndDate());
		if (ObjectUtil.isNotEmpty(viewQuery.getProp()) && ObjectUtil.isNotEmpty(viewQuery.getOrder())) {
			map.put("orderBy", " order by " + viewQuery.getProp() + " " + viewQuery.getOrder());
		}

		SQLOperation operation = this.getSQLAndParams("ProductCount.queryProdViewDetailPageExcel", map);
		List<ProductDataViewPageDTO> dtoList = query(operation.getSql(), operation.getParams(), new BeanPropertyRowMapper<>(ProductDataViewPageDTO.class));
		for (ProductDataViewPageDTO viewPageDTO : dtoList) {
			viewPageDTO.setPeople(Optional.ofNullable(viewPageDTO.getH5People()).orElse(0) + Optional.ofNullable(viewPageDTO.getMiniPeople()).orElse(0));
			viewPageDTO.setFrequency(Optional.ofNullable(viewPageDTO.getH5Frequency()).orElse(0) + Optional.ofNullable(viewPageDTO.getMiniFrequency()).orElse(0));
		}
		return dtoList;
	}

	@Override
	public PageSupport<AdminProductDataViewPageDTO> getAdminProductViewPage(ProductDataViewQuery viewQuery) {
		SimpleSqlQuery query = new SimpleSqlQuery(AdminProductDataViewPageDTO.class, viewQuery.getPageSize(), viewQuery.getCurPage());
		QueryMap map = new QueryMap();
		map.put("shopId", viewQuery.getShopId());
		map.like("shopName", viewQuery.getShopName(), MatchMode.ANYWHERE);
		map.put("goodId", viewQuery.getGoodId());
		map.like("goodName", viewQuery.getGoodName(), MatchMode.ANYWHERE);
		map.put("startDate", viewQuery.getStartDate());
		map.put("endDate", viewQuery.getEndDate());
		if (ObjectUtil.isNotEmpty(viewQuery.getProp()) && ObjectUtil.isNotEmpty(viewQuery.getOrder())) {
			map.put("orderBy", " order by " + viewQuery.getProp() + " " + viewQuery.getOrder());
		}
//		OrderBy orderBy = new OrderBy(viewQuery.getProp(), viewQuery.getOrder());
//		map.addOrder(orderBy, Arrays.asList("people","frequency", "mpPeople","mpFrequency","miniPeople","miniFrequency","appPeople","appFrequency","h5People","h5Frequency"));
		query.setSqlAndParameter("ProductCount.queryProdViewDetailPage", map);
		PageSupport<AdminProductDataViewPageDTO> page = querySimplePage(query);
		if (CollUtil.isNotEmpty(page.getResultList())) {
			for (AdminProductDataViewPageDTO viewPageDTO : page.getResultList()) {
				viewPageDTO.setPeople(Optional.ofNullable(viewPageDTO.getH5People()).orElse(0) + Optional.ofNullable(viewPageDTO.getMiniPeople()).orElse(0));
				viewPageDTO.setFrequency(Optional.ofNullable(viewPageDTO.getH5Frequency()).orElse(0) + Optional.ofNullable(viewPageDTO.getMiniFrequency()).orElse(0));
			}
		}
		return page;
	}

	@Override
	public List<AdminProductDataViewPageDTO> getAdminProductViewPageExcel(ProductDataViewQuery viewQuery) {
		QueryMap map = new QueryMap();
		map.put("shopId", viewQuery.getShopId());
		map.put("goodId", viewQuery.getGoodId());
		map.put("startDate", viewQuery.getStartDate());
		map.put("endDate", viewQuery.getEndDate());
		if (ObjectUtil.isNotEmpty(viewQuery.getProp()) && ObjectUtil.isNotEmpty(viewQuery.getOrder())) {
			map.put("orderBy", " order by " + viewQuery.getProp() + " " + viewQuery.getOrder());
		}

		SQLOperation operation = this.getSQLAndParams("ProductCount.queryProdViewDetailPageExcel", map);
		List<AdminProductDataViewPageDTO> dtoList = query(operation.getSql(), operation.getParams(), new BeanPropertyRowMapper<>(AdminProductDataViewPageDTO.class));
		for (AdminProductDataViewPageDTO viewPageDTO : dtoList) {
			viewPageDTO.setPeople(Optional.ofNullable(viewPageDTO.getH5People()).orElse(0) + Optional.ofNullable(viewPageDTO.getMiniPeople()).orElse(0));
			viewPageDTO.setFrequency(Optional.ofNullable(viewPageDTO.getH5Frequency()).orElse(0) + Optional.ofNullable(viewPageDTO.getMiniFrequency()).orElse(0));
		}
		return dtoList;
	}

	@Override
	public List<CategoryBO> queryCategoryByName(String name) {
		QueryMap map = new QueryMap();
		map.like("name", name, MatchMode.ANYWHERE);
		SQLOperation operation = this.getSQLAndParams("ProductCount.queryCategoryByName", map);
		return query(operation.getSql(), operation.getParams(), new BeanPropertyRowMapper<>(CategoryBO.class));
	}

	@Override
	public List<CategoryBO> queryCategory(ProductDataCategoryQuery query) {
		QueryMap map = new QueryMap();
		map.like("name", query.getCategoryName(), MatchMode.ANYWHERE);
		map.put("grade", query.getGrade());
		SQLOperation operation = this.getSQLAndParams("ProductCount.queryCategoryList", map);
		return query(operation.getSql(), operation.getParams(), new BeanPropertyRowMapper<>(CategoryBO.class));
	}

	@Override
	public List<CategoryBO> queryCategoryById(Long id) {
		QueryMap map = new QueryMap();
		map.put("id", id);
		SQLOperation operation = this.getSQLAndParams("ProductCount.queryCategoryById", map);
		return query(operation.getSql(), operation.getParams(), new BeanPropertyRowMapper<>(CategoryBO.class));
	}

	@Override
	public ProductDataCategoryDTO queryTopCategory(Long num, ProductDataCategoryQuery query) {
		QueryMap map = new QueryMap();
		map.put("startDate", query.getStartDate());
		map.put("endDate", query.getEndDate());
		map.put("id", num);
		map.put("shopId", query.getShopId());
		SQLOperation operation = this.getSQLAndParams("ProductCount.queryCategory", map);
		return get(operation.getSql(), operation.getParams(), new BeanPropertyRowMapper<>(ProductDataCategoryDTO.class));
	}

	@Override
	public List<ProductDataCategoryDTO> queryTopCategory(ProductDataCategoryQuery query) {
		QueryMap map = new QueryMap();
		map.put("startDate", query.getStartDate());
		map.put("endDate", query.getEndDate());
		map.put("shopId", query.getShopId());
		SQLOperation operation = this.getSQLAndParams("ProductCount.queryCategory", map);
		return query(operation.getSql(), operation.getParams(), new BeanPropertyRowMapper<>(ProductDataCategoryDTO.class));
	}

	@Override
	public List<ProductDataCategoryDTO> querySecondCategory(Long num, ProductDataCategoryQuery query) {
		QueryMap map = new QueryMap();
		map.put("startDate", query.getStartDate());
		map.put("endDate", query.getEndDate());
		map.put("id", num);
		map.put("shopId", query.getShopId());
		SQLOperation operation = this.getSQLAndParams("ProductCount.querySecondCategory", map);
		return query(operation.getSql(), operation.getParams(), new BeanPropertyRowMapper<>(ProductDataCategoryDTO.class));
	}

	@Override
	public ProductDataCategoryDTO getSecondCategory(Long num, ProductDataCategoryQuery query) {
		QueryMap map = new QueryMap();
		map.put("startDate", query.getStartDate());
		map.put("endDate", query.getEndDate());
		map.put("categoryId", num);
		map.put("shopId", query.getShopId());
		SQLOperation operation = this.getSQLAndParams("ProductCount.querySecondCategory", map);
		return get(operation.getSql(), ProductDataCategoryDTO.class, operation.getParams());
	}

	@Override
	public List<ProductDataCategoryDTO> queryThirdCategory(Long num, ProductDataCategoryQuery query) {
		QueryMap map = new QueryMap();
		map.put("startDate", query.getStartDate());
		map.put("endDate", query.getEndDate());
		map.put("id", num);
		map.put("shopId", query.getShopId());
		SQLOperation operation = this.getSQLAndParams("ProductCount.queryThirdCategory", map);
		return query(operation.getSql(), operation.getParams(), new BeanPropertyRowMapper<>(ProductDataCategoryDTO.class));
	}

	@Override
	public ProductDataCategoryDTO getThirdCategory(Long num, ProductDataCategoryQuery query) {
		QueryMap map = new QueryMap();
		map.put("startDate", query.getStartDate());
		map.put("endDate", query.getEndDate());
		map.put("categoryId", num);
		map.put("shopId", query.getShopId());
		SQLOperation operation = this.getSQLAndParams("ProductCount.queryThirdCategory", map);
		return get(operation.getSql(), operation.getParams(), new BeanPropertyRowMapper<>(ProductDataCategoryDTO.class));
	}

	@Override
	public PageSupport<ProductDataCategoryPageDTO> queryCategoryDetailPage(ProductDataCategoryQuery categoryQuery) {
		SimpleSqlQuery query = new SimpleSqlQuery(ProductDataCategoryPageDTO.class, categoryQuery.getPageSize(), categoryQuery.getCurPage());
		QueryMap map = new QueryMap();
		map.like("firstName", categoryQuery.getCategoryName(), MatchMode.ANYWHERE);
		map.like("secondName", categoryQuery.getCategoryName(), MatchMode.ANYWHERE);
		map.like("thirdName", categoryQuery.getCategoryName(), MatchMode.ANYWHERE);
		map.put("startDate", categoryQuery.getStartDate());
		map.put("endDate", categoryQuery.getEndDate());
		map.put("shopId", categoryQuery.getShopId());

		if (ObjectUtil.isNotEmpty(categoryQuery.getProp()) && ObjectUtil.isNotEmpty(categoryQuery.getOrder())) {
			map.put("orderBy", " order by " + categoryQuery.getProp() + " " + categoryQuery.getOrder());
		}
		query.setSqlAndParameter("ProductCount.queryCategoryPage", map);
		PageSupport<ProductDataCategoryPageDTO> page = querySimplePage(query);
		if (CollUtil.isNotEmpty(page.getResultList())) {
			BigDecimal hundred = BigDecimal.valueOf(100);
			for (ProductDataCategoryPageDTO dto : page.getResultList()) {
				dto.setBuyRate(Optional.ofNullable(dto.getBuyRate()).orElse(BigDecimal.ZERO).multiply(hundred).setScale(2, RoundingMode.HALF_DOWN));
			}
		}

		return page;
	}

	@Override
	public List<ProductDataCategoryPageDTO> queryCategoryDetailPageExcel(ProductDataCategoryQuery categoryQuery) {
		QueryMap map = new QueryMap();
		map.like("firstName", categoryQuery.getCategoryName(), MatchMode.ANYWHERE);
		map.like("secondName", categoryQuery.getCategoryName(), MatchMode.ANYWHERE);
		map.like("thirdName", categoryQuery.getCategoryName(), MatchMode.ANYWHERE);
		map.put("startDate", categoryQuery.getStartDate());
		map.put("endDate", categoryQuery.getEndDate());
		map.put("shopId", categoryQuery.getShopId());

		if (ObjectUtil.isNotEmpty(categoryQuery.getProp()) && ObjectUtil.isNotEmpty(categoryQuery.getOrder())) {
			map.put("orderBy", " order by " + categoryQuery.getProp() + " " + categoryQuery.getOrder());
		}
		SQLOperation operation = this.getSQLAndParams("ProductCount.queryCategoryPageExcel", map);
		List<ProductDataCategoryPageDTO> query = query(operation.getSql(), operation.getParams(), new BeanPropertyRowMapper<>(ProductDataCategoryPageDTO.class));
		if (CollUtil.isNotEmpty(query)) {
			BigDecimal hundred = BigDecimal.valueOf(100);
			for (ProductDataCategoryPageDTO dto : query) {
				dto.setBuyRate(Optional.ofNullable(dto.getBuyRate()).orElse(BigDecimal.ZERO).multiply(hundred).setScale(2, RoundingMode.HALF_DOWN));
			}
		}
		return query;
	}

	@Override
	public List<ProductDataSearchDTO> querySearchData(String source) {

		QueryMap map = new QueryMap();
		map.put("source", source);

		SQLOperation operation = this.getSQLAndParams("ProductCount.querySearchData", map);
		return query(operation.getSql(), operation.getParams(), new BeanPropertyRowMapper<>(ProductDataSearchDTO.class));

	}

	@Override
	public List<ProductDataSearchPicDTO> getSearchLine(ProductDataSearchPicQuery searchPicQuery) {

		QueryMap map = new QueryMap();
		map.put("startDate", searchPicQuery.getStartDate());
		map.put("endDate", searchPicQuery.getEndDate());
		map.like("word", searchPicQuery.getWord(), MatchMode.ANYWHERE);

		SQLOperation operation = this.getSQLAndParams("ProductCount.querySearchLine", map);
		return query(operation.getSql(), operation.getParams(), new BeanPropertyRowMapper<>(ProductDataSearchPicDTO.class));
	}

	@Override
	public PageSupport<ProductDataSearchPicDTO> getSearchDetailPicture(ProductDataSearchPicQuery searchPicQuery) {
		SimpleSqlQuery query = new SimpleSqlQuery(ProductDataSearchPicDTO.class, searchPicQuery.getPageSize(), searchPicQuery.getCurPage());
		QueryMap map = new QueryMap();
		map.put("startDate", searchPicQuery.getStartDate());
		map.put("endDate", searchPicQuery.getEndDate());
		map.like("word", searchPicQuery.getWord(), MatchMode.ANYWHERE);
		if (ObjectUtil.isNotEmpty(searchPicQuery.getProp()) && ObjectUtil.isNotEmpty(searchPicQuery.getOrder())) {
			map.put("orderBy", " order by " + searchPicQuery.getProp() + " " + searchPicQuery.getOrder());
		}
		query.setSqlAndParameter("ProductCount.querySearchPage", map);
		PageSupport<ProductDataSearchPicDTO> page = querySimplePage(query);

		return page;
	}

	@Override
	public List<ProductDataSearchPicDTO> getSearchDetailPictureExcel(ProductDataSearchPicQuery searchPicQuery) {
		QueryMap map = new QueryMap();
		map.put("startDate", searchPicQuery.getStartDate());
		map.put("endDate", searchPicQuery.getEndDate());
		map.like("word", searchPicQuery.getWord(), MatchMode.ANYWHERE);
		if (ObjectUtil.isNotEmpty(searchPicQuery.getProp()) && ObjectUtil.isNotEmpty(searchPicQuery.getOrder())) {
			map.put("orderBy", " order by " + searchPicQuery.getProp() + " " + searchPicQuery.getOrder());
		}
		SQLOperation operation = this.getSQLAndParams("ProductCount.querySearchPageExcel", map);

		return query(operation.getSql(), operation.getParams(), new BeanPropertyRowMapper<>(ProductDataSearchPicDTO.class));
	}

	@Override
	public Integer
	getSearchDetailLastYear(ProductDataSearchPicQuery searchPicQuery) {
		return get(getSQL("ProductCount.querySearchFigureYear"), Integer.class, searchPicQuery.getWord(), searchPicQuery.getEndDate(), searchPicQuery.getStartDate());
	}

	@Override
	public Integer getSearchDetailLastMonth(ProductDataSearchPicQuery searchPicQuery) {
		return get(getSQL("ProductCount.querySearchFigureMonth"), Integer.class, searchPicQuery.getWord(), searchPicQuery.getEndDate(), searchPicQuery.getStartDate());

	}

	@Override
	public PageSupport<ProductDataShopSaleDTO> getShopSaleDetailPage(ProductDataSaleQuery productDataSaleQuery) {
		SimpleSqlQuery query = new SimpleSqlQuery(ProductDataShopSaleDTO.class, productDataSaleQuery.getPageSize(), productDataSaleQuery.getCurPage());
		QueryMap map = new QueryMap();
		map.put("startDate", productDataSaleQuery.getStartDate());
		map.put("endDate", productDataSaleQuery.getEndDate());
		map.like("productName", productDataSaleQuery.getProductName(), MatchMode.ANYWHERE);
		map.put("shopId", productDataSaleQuery.getShopId());
		map.put("productId", productDataSaleQuery.getProductId());
		if (ObjectUtil.isNotEmpty(productDataSaleQuery.getProp()) && ObjectUtil.isNotEmpty(productDataSaleQuery.getOrder())) {
			map.put("orderBy", " order by " + productDataSaleQuery.getProp() + " " + productDataSaleQuery.getOrder());
		}
		query.setSqlAndParameter("ProductCount.getShopSaleDetailPage", map);
		PageSupport<ProductDataShopSaleDTO> page = querySimplePage(query);

		return page;
	}

	@Override
	public List<ProductDataShopSaleDTO> getShopSaleDetailPageExcel(ProductDataSaleQuery productDataSaleQuery) {
		QueryMap map = new QueryMap();
		map.put("startDate", productDataSaleQuery.getStartDate());
		map.put("endDate", productDataSaleQuery.getEndDate());
		map.like("productName", productDataSaleQuery.getProductName(), MatchMode.ANYWHERE);
		map.put("shopId", productDataSaleQuery.getShopId());
		map.put("productId", productDataSaleQuery.getProductId());
		if (ObjectUtil.isNotEmpty(productDataSaleQuery.getProp()) && ObjectUtil.isNotEmpty(productDataSaleQuery.getOrder())) {
			map.put("orderBy", " order by " + productDataSaleQuery.getProp() + " " + productDataSaleQuery.getOrder());
		}
		SQLOperation operation = this.getSQLAndParams("ProductCount.getShopSaleDetailPageExcel", map);

		List<ProductDataShopSaleDTO> dtoList = query(operation.getSql(), operation.getParams(), new BeanPropertyRowMapper<>(ProductDataShopSaleDTO.class));
		return dtoList;
	}

	@Override
	public PageSupport<AdminProductDataShopSaleDTO> getAdminShopSaleDetailPage(ProductDataSaleQuery productDataSaleQuery) {
		SimpleSqlQuery query = new SimpleSqlQuery(AdminProductDataShopSaleDTO.class, productDataSaleQuery.getPageSize(), productDataSaleQuery.getCurPage());
		QueryMap map = new QueryMap();
		map.put("startDate", productDataSaleQuery.getStartDate());
		map.put("endDate", productDataSaleQuery.getEndDate());
		map.like("shopName", productDataSaleQuery.getShopName(), MatchMode.ANYWHERE);
		map.like("productName", productDataSaleQuery.getProductName(), MatchMode.ANYWHERE);
		map.put("shopId", productDataSaleQuery.getShopId());
		map.put("productId", productDataSaleQuery.getProductId());
		if (ObjectUtil.isNotEmpty(productDataSaleQuery.getProp()) && ObjectUtil.isNotEmpty(productDataSaleQuery.getOrder())) {
			map.put("orderBy", " order by " + productDataSaleQuery.getProp() + " " + productDataSaleQuery.getOrder());
		}
		query.setSqlAndParameter("ProductCount.getShopSaleDetailPage", map);
		PageSupport<AdminProductDataShopSaleDTO> page = querySimplePage(query);

		return page;
	}

	@Override
	public List<AdminProductDataShopSaleDTO> getAdminShopSaleDetailPageExcel(ProductDataSaleQuery productDataSaleQuery) {
		QueryMap map = new QueryMap();
		map.put("startDate", productDataSaleQuery.getStartDate());
		map.put("endDate", productDataSaleQuery.getEndDate());
		map.like("productName", productDataSaleQuery.getProductName(), MatchMode.ANYWHERE);
		map.put("shopId", productDataSaleQuery.getShopId());
		map.put("productId", productDataSaleQuery.getProductId());
		if (ObjectUtil.isNotEmpty(productDataSaleQuery.getProp()) && ObjectUtil.isNotEmpty(productDataSaleQuery.getOrder())) {
			map.put("orderBy", " order by " + productDataSaleQuery.getProp() + " " + productDataSaleQuery.getOrder());
		}
		SQLOperation operation = this.getSQLAndParams("ProductCount.getShopSaleDetailPageExcel", map);

		List<AdminProductDataShopSaleDTO> dtoList = query(operation.getSql(), operation.getParams(), new BeanPropertyRowMapper<>(AdminProductDataShopSaleDTO.class));
		return dtoList;
	}

	@Override
	public ProductDataGoodSaleDTO getShopSaleTrendLine(ProductDataSaleTrendQuery trendQuery) {

		QueryMap map = new QueryMap();
		map.put("startDate", trendQuery.getStartDate());
		map.put("endDate", trendQuery.getEndDate());
		map.put("prodId", trendQuery.getProductId());
		map.put("skuId", trendQuery.getSkuId());
		map.put("shopId", trendQuery.getShopId());
		map.put("sDate", trendQuery.getSDate());
		map.put("eDate", trendQuery.getEDate());

		SQLOperation operation = this.getSQLAndParams("ProductCount.getShopSaleTrendLine", map);
		return get(operation.getSql(), operation.getParams(), new BeanPropertyRowMapper<>(ProductDataGoodSaleDTO.class));
	}

	@Override
	public PageSupport<ProductDataSaleDealDTO> getShopSaleDealPage(ProductDataSaleTrendQuery trendQuery) {
		SimpleSqlQuery query = new SimpleSqlQuery(ProductDataSaleDealDTO.class, trendQuery.getPageSize(), trendQuery.getCurPage());
		QueryMap map = new QueryMap();
		map.put("startDate", trendQuery.getStartDate());
		map.put("endDate", trendQuery.getEndDate());
		map.put("prodId", trendQuery.getProductId());
		map.put("skuId", trendQuery.getSkuId());
		if (ObjectUtil.isNotEmpty(trendQuery.getProp()) && ObjectUtil.isNotEmpty(trendQuery.getOrder())) {
			map.put("orderBy", " order by " + trendQuery.getProp() + " " + trendQuery.getOrder());
		}
		query.setSqlAndParameter("ProductCount.getShopSaleDealPage", map);
		PageSupport<ProductDataSaleDealDTO> page = querySimplePage(query);

		return page;
	}

	@Override
	public List<ProductDataSaleDealDTO> getShopSaleDealList(ProductDataSaleTrendQuery trendQuery) {
		QueryMap map = new QueryMap();
		map.put("startDate", trendQuery.getStartDate());
		map.put("endDate", trendQuery.getEndDate());
		map.put("prodId", trendQuery.getProductId());
		map.put("skuId", trendQuery.getSkuId());
		if (ObjectUtil.isNotEmpty(trendQuery.getProp()) && ObjectUtil.isNotEmpty(trendQuery.getOrder())) {
			map.put("orderBy", " order by " + trendQuery.getProp() + " " + trendQuery.getOrder());
		}
		SQLOperation operation = this.getSQLAndParams("ProductCount.getShopSaleDealPage", map);
		return query(operation.getSql(), ProductDataSaleDealDTO.class, operation.getParams());
	}

	@Override
	public List<ProductDataSaleViewDTO> getShopSaleViewPage(ProductDataSaleTrendQuery trendQuery) {
		QueryMap map = new QueryMap();
		map.put("startDate", trendQuery.getStartDate());
		map.put("endDate", trendQuery.getEndDate());
		map.put("prodId", trendQuery.getProductId());
		if (ObjectUtil.isNotEmpty(trendQuery.getProp()) && ObjectUtil.isNotEmpty(trendQuery.getOrder())) {
			map.put("orderBy", " order by " + trendQuery.getProp() + " " + trendQuery.getOrder());
		}
		SQLOperation operation = this.getSQLAndParams("ProductCount.getShopSaleViewPage", map);
		return query(operation.getSql(), operation.getParams(), new BeanPropertyRowMapper<>(ProductDataSaleViewDTO.class));
	}

	@Override
	public List<ProductDataSaleFavoriteDTO> getShopSaleFavoritePage(ProductDataSaleTrendQuery trendQuery) {
		QueryMap map = new QueryMap();
		map.put("startDate", trendQuery.getStartDate());
		map.put("endDate", trendQuery.getEndDate());
		map.put("prodId", trendQuery.getProductId());
		if (ObjectUtil.isNotEmpty(trendQuery.getProp()) && ObjectUtil.isNotEmpty(trendQuery.getOrder())) {
			map.put("orderBy", " order by " + trendQuery.getProp() + " " + trendQuery.getOrder());
		}
		SQLOperation operation = this.getSQLAndParams("ProductCount.getShopSaleFavoritePage", map);
		return query(operation.getSql(), operation.getParams(), new BeanPropertyRowMapper<>(ProductDataSaleFavoriteDTO.class));
	}

	@Override
	public List<ProductDataCategoryDTO> queryShopTopCategory(ProductDataCategoryQuery query) {
		QueryMap map = new QueryMap();
		map.put("startDate", query.getStartDate());
		map.put("endDate", query.getEndDate());
		map.put("shopId", query.getShopId());
		SQLOperation operation = this.getSQLAndParams("ProductCount.queryShopCategory", map);
		return query(operation.getSql(), operation.getParams(), new BeanPropertyRowMapper<>(ProductDataCategoryDTO.class));
	}

	@Override
	public ProductDataCategoryDTO queryShopTopCategory(Long num, ProductDataCategoryQuery query) {
		QueryMap map = new QueryMap();
		map.put("startDate", query.getStartDate());
		map.put("endDate", query.getEndDate());
		map.put("id", num);
		map.put("shopId", query.getShopId());
		SQLOperation operation = this.getSQLAndParams("ProductCount.queryShopTopCategory", map);
		return get(operation.getSql(), operation.getParams(), new BeanPropertyRowMapper<>(ProductDataCategoryDTO.class));
	}

	@Override
	public List<ProductDataCategoryDTO> queryShopSecondCategory(Long num, ProductDataCategoryQuery query) {
		QueryMap map = new QueryMap();
		map.put("startDate", query.getStartDate());
		map.put("endDate", query.getEndDate());
		map.put("id", num);
		map.put("shopId", query.getShopId());
		SQLOperation operation = this.getSQLAndParams("ProductCount.queryShopSecondCategory", map);
		return query(operation.getSql(), operation.getParams(), new BeanPropertyRowMapper<>(ProductDataCategoryDTO.class));
	}

	@Override
	public List<ProductDataCategoryDTO> queryShopThirdCategory(Long num, ProductDataCategoryQuery query) {
		QueryMap map = new QueryMap();
		map.put("startDate", query.getStartDate());
		map.put("endDate", query.getEndDate());
		map.put("id", num);
		map.put("shopId", query.getShopId());
		SQLOperation operation = this.getSQLAndParams("ProductCount.queryShopThirdCategory", map);
		return query(operation.getSql(), operation.getParams(), new BeanPropertyRowMapper<>(ProductDataCategoryDTO.class));
	}

	@Override
	public PageSupport<ProductDataCategoryPageDTO> queryShopCategoryDetailPage(ProductDataCategoryQuery categoryQuery) {
		SimpleSqlQuery query = new SimpleSqlQuery(ProductDataCategoryPageDTO.class, categoryQuery.getPageSize(), categoryQuery.getCurPage());
		QueryMap map = new QueryMap();
		map.like("firstName", categoryQuery.getCategoryName(), MatchMode.ANYWHERE);
		map.like("secondName", categoryQuery.getCategoryName(), MatchMode.ANYWHERE);
		map.like("thirdName", categoryQuery.getCategoryName(), MatchMode.ANYWHERE);
		map.put("startDate", categoryQuery.getStartDate());
		map.put("endDate", categoryQuery.getEndDate());
		map.put("shopId", categoryQuery.getShopId());

		if (ObjectUtil.isNotEmpty(categoryQuery.getProp()) && ObjectUtil.isNotEmpty(categoryQuery.getOrder())) {
			map.put("orderBy", " order by " + categoryQuery.getProp() + " " + categoryQuery.getOrder());
		}
		query.setSqlAndParameter("ProductCount.queryShopCategoryPage", map);
		PageSupport<ProductDataCategoryPageDTO> page = querySimplePage(query);
		if (CollUtil.isNotEmpty(page.getResultList())) {
			BigDecimal hundred = BigDecimal.valueOf(100);
			for (ProductDataCategoryPageDTO dto : page.getResultList()) {
				dto.setBuyRate(Optional.ofNullable(dto.getBuyRate()).orElse(BigDecimal.ZERO).multiply(hundred).setScale(2, RoundingMode.HALF_DOWN));
			}
		}

		return page;
	}

	@Override
	public List<ProductDataCategoryPageDTO> queryShopCategoryDetailPageExcel(ProductDataCategoryQuery categoryQuery) {
		QueryMap map = new QueryMap();
		map.like("firstName", categoryQuery.getCategoryName(), MatchMode.ANYWHERE);
		map.like("secondName", categoryQuery.getCategoryName(), MatchMode.ANYWHERE);
		map.like("thirdName", categoryQuery.getCategoryName(), MatchMode.ANYWHERE);
		map.put("startDate", categoryQuery.getStartDate());
		map.put("endDate", categoryQuery.getEndDate());
		map.put("shopId", categoryQuery.getShopId());

		if (ObjectUtil.isNotEmpty(categoryQuery.getProp()) && ObjectUtil.isNotEmpty(categoryQuery.getOrder())) {
			map.put("orderBy", " order by " + categoryQuery.getProp() + " " + categoryQuery.getOrder());
		}
		SQLOperation operation = this.getSQLAndParams("ProductCount.queryShopCategoryPageExcel", map);
		List<ProductDataCategoryPageDTO> query = query(operation.getSql(), operation.getParams(), new BeanPropertyRowMapper<>(ProductDataCategoryPageDTO.class));
		if (CollUtil.isNotEmpty(query)) {
			BigDecimal hundred = BigDecimal.valueOf(100);
			for (ProductDataCategoryPageDTO dto : query) {
				dto.setBuyRate(Optional.ofNullable(dto.getBuyRate()).orElse(BigDecimal.ZERO).multiply(hundred).setScale(2, RoundingMode.HALF_DOWN));
			}
		}
		return query;
	}

	@Override
	public List<CategoryBO> queryShopCategoryByName(ProductDataCategoryQuery query) {
		QueryMap queryMap = new QueryMap();
		queryMap.put("shopId", query.getShopId());
		queryMap.like("categoryName", query.getCategoryName(), MatchMode.ANYWHERE);

		SQLOperation operation = this.getSQLAndParams("ProductCount.queryShopCategoryByName", queryMap);
		return this.query(operation.getSql(), CategoryBO.class, operation.getParams());
	}

	@Override
	public List<CategoryBO> queryShopCategoryById(Long id, Long shopId) {
		QueryMap map = new QueryMap();
		map.put("id", id);
		map.put("shopId", shopId);

		SQLOperation operation = this.getSQLAndParams("ProductCount.queryShopCategoryById", map);
		return this.query(operation.getSql(), CategoryBO.class, operation.getParams());
	}
}
