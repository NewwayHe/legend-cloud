/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.product.bo.CategoryBO;
import com.legendshop.product.dao.ProductCountDao;
import com.legendshop.product.dto.*;
import com.legendshop.product.query.*;
import com.legendshop.product.service.ProductCountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * 商品统计服务
 *
 * @author legendshop
 */
@Slf4j
@Service
public class ProductCountServiceImpl implements ProductCountService {

	@Autowired
	private ProductCountDao productCountDao;

	@Cacheable(value = "ProductDataSpuDTO")
	@Override
	public ProductDataSpuDTO getProductDataSpuCount() {

		//获取时间范围 1-一天内 7-一周内 30-一月内
		Date[] date1 = getDate(1);
		Date[] date2 = getDate(7);
		Date[] date3 = getDate(30);

		Integer count1 = productCountDao.queryProdSpuCount(null, date1[1], null);
		Integer count2 = productCountDao.queryProdSpuCount(date1[0], date1[1], null);
		Integer count3 = productCountDao.queryProdSpuCount(date2[0], date2[1], null);
		Integer count4 = productCountDao.queryProdSpuCount(date3[0], date3[1], null);

		return ProductDataSpuDTO
				.builder()
				.spuTotalNum(count1)
				.spuTotalNumByDay(count2)
				.spuTotalNumByWeek(count3)
				.spuTotalNumByMonth(count4)
				.build();

	}

	@Cacheable(value = "ProductDataSkuDTO")
	@Override
	public ProductDataSkuDTO getProductDataSkuCount() {

		//获取时间范围 1-一天内 7-一周内 30-一月内
		Date[] date1 = getDate(1);
		Date[] date2 = getDate(7);
		Date[] date3 = getDate(30);

		Integer count1 = productCountDao.queryProdSkuCount(null, date1[1], null);
		Integer count2 = productCountDao.queryProdSkuCount(date1[0], date1[1], null);
		Integer count3 = productCountDao.queryProdSkuCount(date2[0], date2[1], null);
		Integer count4 = productCountDao.queryProdSkuCount(date3[0], date3[1], null);

		return ProductDataSkuDTO
				.builder()
				.skuTotalNum(count1)
				.skuTotalNumByDay(count2)
				.skuTotalNumByWeek(count3)
				.skuTotalNumByMonth(count4)
				.build();

	}

	@Cacheable(value = "ProductDataSkuOnSaleDTO")
	@Override
	public ProductDataSkuOnSaleDTO getProductDataSkuOnSaleCount() {

		//获取时间范围 1-一天内 7-一周内 30-一月内
		Date[] date1 = getDate(1);
		Date[] date2 = getDate(7);
		Date[] date3 = getDate(30);

		Integer count1 = productCountDao.queryProdSkuSaleCount(null, date1[1], null);
		Integer count2 = productCountDao.queryProdSkuSaleCount(date1[0], date1[1], null);
		Integer count3 = productCountDao.queryProdSkuSaleCount(date2[0], date2[1], null);
		Integer count4 = productCountDao.queryProdSkuSaleCount(date3[0], date3[1], null);

		return ProductDataSkuOnSaleDTO
				.builder()
				.skuTotalNumOnSale(count1)
				.skuTotalNumByDayOnSale(count2)
				.skuTotalNumByWeekOnSale(count3)
				.skuTotalNumByMonthOnSale(count4)
				.build();
	}

	@Cacheable(value = "ProductDataSpuClickDTO")
	@Override
	public ProductDataSpuClickDTO getProductDataSpuClickCount() {

		//获取时间范围 1-一天内 7-一周内 30-一月内
		Date[] date1 = getDate(1);
		Date[] date2 = getDate(7);
		Date[] date3 = getDate(30);

		Integer count1 = productCountDao.queryProdViews(null, date1[1], null);
		Integer count2 = productCountDao.queryProdViews(date1[0], date1[1], null);
		Integer count3 = productCountDao.queryProdViews(date2[0], date2[1], null);
		Integer count4 = productCountDao.queryProdViews(date3[0], date3[1], null);

		return ProductDataSpuClickDTO
				.builder()
				.spuClickTotalNum(count1)
				.spuClickNumByDay(count2)
				.spuClickNumByWeek(count3)
				.spuClickNumByMonth(count4)
				.build();
	}

	@Override
	public List<ProductDataSkuByCategoryDTO> getProductSkuByCategory() {

		String now = DateUtil.now();
		Date date = DateUtil.parse(now);
		Date beginOfDay = DateUtil.beginOfDay(date);
		Date endDate = DateUtil.offset(beginOfDay, DateField.SECOND, -1);

		List<ProductDataSkuByCategoryDTO> list = productCountDao.queryProdSkuByCategory(endDate, null);

		AtomicInteger num = new AtomicInteger(1);
		list.forEach(a -> a.setOrderNum(num.getAndIncrement()));

		return list;
	}

	@Override
	public List<ProductDataSearchDTO> getProductSearchData(String resource) {
		return productCountDao.querySearchData(resource);
	}

	@Cacheable(value = "list.ProductDataShopDTO")
	@Override
	public List<ProductDataShopDTO> getShopList() {
		return productCountDao.queryShopList();
	}

	@Override
	public List<ProductDataGoodDTO> getGoodList(Long shopId) {
		return productCountDao.queryGoodList(shopId);
	}

	@Cacheable(value = "getShopGoodsCount")
	@Override
	public ProductDataGoodsByShopDTO getShopGoodsCount(Long id, String startDate, String endDate) {
		Date s = null;
		if (StrUtil.isNotBlank(startDate)) {
			s = DateUtil.parseDate(startDate);
		}
		Date e = null;
		Date end = null;
		if (StrUtil.isNotBlank(endDate)) {
			e = DateUtil.parseDate(endDate);
			end = DateUtil.endOfDay(e);
		}
		return productCountDao.queryShopGoodsCount(id, s, end);
	}

	@Override
	public List<ProductDataViewLineDTO> getProductViewLine(ProductDataViewQuery viewQuery) {
		Date s;
		Date e;
		List<Date> dateAvg;
		if ((viewQuery.getEndDate() == null && viewQuery.getStartDate() == null) || (viewQuery.getEndDate().isEmpty() && viewQuery.getStartDate().isEmpty())) {
			//未设置日期范围，默认七天
			Integer num = 7;
			Date[] date = getDate(num);
			s = date[0];
			e = date[1];
			dateAvg = getDateAvg(s, e);
			viewQuery.setStartDate(DateUtil.formatDateTime(s));
			viewQuery.setEndDate(DateUtil.formatDateTime(e));
		} else {
			s = DateUtil.parseDateTime(viewQuery.getStartDate());
			e = DateUtil.parseDateTime(viewQuery.getEndDate());
			dateAvg = getDateAvg(s, e);
		}
		List<ProductDataViewLineDTO> list = new ArrayList<>();
		for (int x = 0; x < dateAvg.size() - 1; x++) {
			viewQuery.setStartDate(DateUtil.formatDateTime(dateAvg.get(x)));
			viewQuery.setEndDate(DateUtil.formatDateTime(DateUtil.offset(dateAvg.get(x + 1), DateField.SECOND, -1)));
			ProductDataViewLineDTO dto = productCountDao.queryProductViewLineAll(viewQuery);
			if (dto == null || dto.getFrequency() == null) {
				dto = new ProductDataViewLineDTO(0);
			}
			dto.setTime(dateAvg.get(x));
			list.add(dto);
		}
		return list;
	}

	@Override
	public PageSupport<ProductDataViewPageDTO> getProductViewPage(ProductDataViewQuery viewQuery) {

		viewQuery.setProp(checkString(viewQuery.getProp()));
		if ((viewQuery.getEndDate() == null && viewQuery.getStartDate() == null) || (viewQuery.getEndDate().isEmpty() && viewQuery.getStartDate().isEmpty())) {
			//未设置日期范围，默认七天
			Integer num = 7;
			Date[] date = getDate(num);
			Date s = date[0];
			Date e = date[1];
			viewQuery.setStartDate(DateUtil.formatDateTime(s));
			viewQuery.setEndDate(DateUtil.formatDateTime(e));
			return productCountDao.getProductViewPage(viewQuery);
		}
		//设置了日期范围
		Date e = DateUtil.parseDate(viewQuery.getEndDate());
		Date end = DateUtil.endOfDay(e);
		viewQuery.setEndDate(DateUtil.formatDateTime(end));

		return productCountDao.getProductViewPage(viewQuery);
	}

	@Override
	public List<ProductDataViewPageDTO> getProductViewPageExcel(ProductDataViewQuery viewQuery) {
		viewQuery.setProp(checkString(viewQuery.getProp()));
		if ((viewQuery.getEndDate() == null && viewQuery.getStartDate() == null) || (viewQuery.getEndDate().isEmpty() && viewQuery.getStartDate().isEmpty())) {
			//未设置日期范围，默认七天
			Integer num = 7;
			Date[] date = getDate(num);
			Date s = date[0];
			Date e = date[1];
			viewQuery.setStartDate(DateUtil.formatDateTime(s));
			viewQuery.setEndDate(DateUtil.formatDateTime(e));
			return productCountDao.getProductViewPageExcel(viewQuery);
		}
		//设置了日期范围
		Date e = DateUtil.parseDate(viewQuery.getEndDate());
		Date end = DateUtil.endOfDay(e);
		viewQuery.setEndDate(DateUtil.formatDateTime(end));

		return productCountDao.getProductViewPageExcel(viewQuery);
	}

	@Override
	public PageSupport<AdminProductDataViewPageDTO> getAdminProductViewPage(ProductDataViewQuery viewQuery) {
		viewQuery.setProp(checkString(viewQuery.getProp()));
		if ((viewQuery.getEndDate() == null && viewQuery.getStartDate() == null) || (viewQuery.getEndDate().isEmpty() && viewQuery.getStartDate().isEmpty())) {
			//未设置日期范围，默认七天
			Integer num = 7;
			Date[] date = getDate(num);
			Date s = date[0];
			Date e = date[1];
			viewQuery.setStartDate(DateUtil.formatDateTime(s));
			viewQuery.setEndDate(DateUtil.formatDateTime(e));
			return productCountDao.getAdminProductViewPage(viewQuery);
		}
		//设置了日期范围
		Date e = DateUtil.parseDate(viewQuery.getEndDate());
		Date end = DateUtil.endOfDay(e);
		viewQuery.setEndDate(DateUtil.formatDateTime(end));

		return productCountDao.getAdminProductViewPage(viewQuery);
	}

	@Override
	public List<AdminProductDataViewPageDTO> getAdminProductViewPageExcel(ProductDataViewQuery viewQuery) {
		viewQuery.setProp(checkString(viewQuery.getProp()));
		if ((viewQuery.getEndDate() == null && viewQuery.getStartDate() == null) || (viewQuery.getEndDate().isEmpty() && viewQuery.getStartDate().isEmpty())) {
			//未设置日期范围，默认七天
			Integer num = 7;
			Date[] date = getDate(num);
			Date s = date[0];
			Date e = date[1];
			viewQuery.setStartDate(DateUtil.formatDateTime(s));
			viewQuery.setEndDate(DateUtil.formatDateTime(e));
			return productCountDao.getAdminProductViewPageExcel(viewQuery);
		}
		//设置了日期范围
		Date e = DateUtil.parseDate(viewQuery.getEndDate());
		Date end = DateUtil.endOfDay(e);
		viewQuery.setEndDate(DateUtil.formatDateTime(end));

		return productCountDao.getAdminProductViewPageExcel(viewQuery);
	}

	@Override
	public List<ProductDataCategoryDTO> getCategoryTree(ProductDataCategoryQuery query) {

		query.setEndDate(null);
		query.setStartDate(null);

		//模糊查询类目名的顶层类目id的list
		String categoryName = query.getCategoryName();
		List<Long> integers = new ArrayList<>();
		List<CategoryBO> list = productCountDao.queryCategoryByName(categoryName);
		list.forEach(a -> {
			if (a.getParentId() == -1) {
				integers.add(a.getId());
			} else if (a.getGrade() == 2) {
				integers.add(a.getParentId());
			} else {
				List<CategoryBO> list1 = productCountDao.queryCategoryById(a.getParentId());
				list1.forEach(b -> {
					integers.add(b.getParentId());
				});
			}
		});
		List<Long> collect = integers.stream().distinct().collect(Collectors.toList());
		//根据类目id获得类目树
		List<ProductDataCategoryDTO> topList = new ArrayList<>();
		collect.forEach(c -> {
			ProductDataCategoryDTO productDataCategoryDTO = productCountDao.queryTopCategory(c, query);
			if (productDataCategoryDTO != null) {
				topList.add(productDataCategoryDTO);
			}
		});
		Integer sum = getSkuSumByCategory(topList);
		BigDecimal hundred = new BigDecimal(100);
		topList.forEach(a -> {
			a.setPercentage(BigDecimal.ZERO);
			if (sum > 0) {
				a.setPercentage(BigDecimal.valueOf(a.getSkuNum()).multiply(hundred).divide(BigDecimal.valueOf(sum), 2, RoundingMode.HALF_DOWN));
			}
			List<ProductDataCategoryDTO> secondList = getSecondCategoryList(a.getId(), query);
			Integer secondSum = getSkuSumByCategory(secondList);
			secondList.forEach(b -> {
				b.setPercentage(BigDecimal.ZERO);
				if (secondSum > 0) {
					b.setPercentage(BigDecimal.valueOf(b.getSkuNum()).multiply(hundred).divide(BigDecimal.valueOf(secondSum), 2, RoundingMode.HALF_DOWN));
				}
				List<ProductDataCategoryDTO> thirdList = getThirdCategoryList(b.getId(), query);
				Integer thirdSum = getSkuSumByCategory(thirdList);
				thirdList.forEach(c -> {
					c.setPercentage(BigDecimal.ZERO);
					if (thirdSum > 0) {
						c.setPercentage(BigDecimal.valueOf(c.getSkuNum()).multiply(hundred).divide(BigDecimal.valueOf(thirdSum), 2, RoundingMode.HALF_DOWN));
					}
				});
				b.setCategoryDTOList(thirdList);
			});
			a.setCategoryDTOList(secondList);
		});
		return topList;
	}

	@Override
	public List<ProductDataCategoryDTO> getCategory(ProductDataCategoryQuery query) {
		if (query.getGrade() == null) {
			return Collections.emptyList();
		}

		if ((query.getEndDate() == null && query.getStartDate() == null) || (query.getEndDate().isEmpty() && query.getStartDate().isEmpty())) {
			//未设置日期范围，默认七天
			Integer num = 7;
			Date[] date = getDate(num);
			Date e = date[1];
			query.setEndDate(DateUtil.formatDateTime(e));
		} else {
			//设置了日期范围
			Date e = DateUtil.parseDateTime(query.getEndDate());
			e = DateUtil.endOfDay(e);
			query.setEndDate(DateUtil.formatDateTime(e));
		}

		List<ProductDataCategoryDTO> topList = new ArrayList<>();
		if (query.getGrade() == 1) {
			topList = productCountDao.queryTopCategory(query);
		} else if (query.getGrade() == 2) {
			topList = productCountDao.querySecondCategory(null, query);
		} else if (query.getGrade() == 3) {
			topList = productCountDao.queryThirdCategory(null, query);
		}

		Integer sum = getSkuSumByCategory(topList);
		BigDecimal hundred = new BigDecimal(100);
		topList.forEach(a -> {
			a.setPercentage(BigDecimal.valueOf(Optional.ofNullable(a.getSkuNum()).orElse(0)).multiply(hundred).divide(BigDecimal.valueOf(sum), 2, RoundingMode.HALF_DOWN));
		});

		return topList;
	}

	@Override
	public PageSupport<ProductDataCategoryPageDTO> getCategoryDetailPage(ProductDataCategoryQuery categoryQuery) {

		categoryQuery.setProp(checkString(categoryQuery.getProp()));
		if ((categoryQuery.getEndDate() == null && categoryQuery.getStartDate() == null) || (categoryQuery.getEndDate().isEmpty() && categoryQuery.getStartDate().isEmpty())) {
			//未设置日期范围，默认七天
			Integer num = 7;
			Date[] date = getDate(num);
			Date s = date[0];
			Date e = date[1];
			categoryQuery.setStartDate(DateUtil.formatDateTime(s));
			categoryQuery.setEndDate(DateUtil.formatDateTime(e));

			return productCountDao.queryCategoryDetailPage(categoryQuery);
		}
		//设置了日期范围
		Date e = DateUtil.parseDate(categoryQuery.getEndDate());
		e = DateUtil.endOfDay(e);
		categoryQuery.setEndDate(DateUtil.formatDateTime(e));

		return productCountDao.queryCategoryDetailPage(categoryQuery);
	}

	@Override
	public List<ProductDataCategoryPageDTO> getCategoryDetailPageExcel(ProductDataCategoryQuery categoryQuery) {
		categoryQuery.setProp(checkString(categoryQuery.getProp()));
		if ((categoryQuery.getEndDate() == null && categoryQuery.getStartDate() == null) || (categoryQuery.getEndDate().isEmpty() && categoryQuery.getStartDate().isEmpty())) {
			//未设置日期范围，默认七天
			Integer num = 7;
			Date[] date = getDate(num);
			Date s = date[0];
			Date e = date[1];
			categoryQuery.setStartDate(DateUtil.formatDateTime(s));
			categoryQuery.setEndDate(DateUtil.formatDateTime(e));

			return productCountDao.queryCategoryDetailPageExcel(categoryQuery);
		}
		//设置了日期范围
		Date e = DateUtil.parseDate(categoryQuery.getEndDate());
		e = DateUtil.endOfDay(e);
		categoryQuery.setEndDate(DateUtil.formatDateTime(e));

		return productCountDao.queryCategoryDetailPageExcel(categoryQuery);
	}

	@Override
	public List<ProductDataSearchPicDTO> getSearchLine(ProductDataSearchPicQuery searchPicQuery) {

		if ((searchPicQuery.getEndDate() == null && searchPicQuery.getStartDate() == null) || (searchPicQuery.getEndDate().isEmpty() && searchPicQuery.getStartDate().isEmpty())) {
			//未设置日期范围，默认七天
			Integer num = 7;
			Date[] date = getDate(num);
			Date s = date[0];
			Date e = date[1];
			searchPicQuery.setStartDate(DateUtil.formatDateTime(s));
			searchPicQuery.setEndDate(DateUtil.formatDateTime(e));
		} else {
			//设置了日期范围
			Date e = DateUtil.parseDateTime(searchPicQuery.getEndDate());
			e = DateUtil.endOfDay(e);
			searchPicQuery.setEndDate(DateUtil.formatDateTime(e));
		}
		List<ProductDataSearchPicDTO> searchLine = productCountDao.getSearchLine(searchPicQuery);
		searchLine.forEach(a -> a.setPeople(a.getH5People() + a.getMiniPeople()));
		return searchLine;
	}

	@Override
	public PageSupport<ProductDataSearchPicDTO> getSearchDetailPage(ProductDataSearchPicQuery searchPicQuery) {

		searchPicQuery.setProp(checkString(searchPicQuery.getProp()));
		int num;
		Date s;
		Date e;
		if ((searchPicQuery.getEndDate() == null && searchPicQuery.getStartDate() == null) || (searchPicQuery.getEndDate().isEmpty() && searchPicQuery.getStartDate().isEmpty())) {
			//未设置日期范围，默认七天
			num = 7;
			Date[] date = getDate(num);
			s = date[0];
			e = date[1];
			searchPicQuery.setStartDate(DateUtil.formatDateTime(s));
			searchPicQuery.setEndDate(DateUtil.formatDateTime(e));
		} else {
			//设置了日期范围
			s = DateUtil.parseDateTime(searchPicQuery.getStartDate());
			e = DateUtil.parseDateTime(searchPicQuery.getEndDate());
			e = DateUtil.endOfDay(e);
			searchPicQuery.setStartDate(DateUtil.formatDateTime(s));
			searchPicQuery.setEndDate(DateUtil.formatDateTime(e));
			num = Math.toIntExact(DateUtil.between(s, DateUtil.offsetSecond(e, 1), DateUnit.DAY));
		}

		//当前数据
		PageSupport<ProductDataSearchPicDTO> searchDetailPicture = productCountDao.getSearchDetailPicture(searchPicQuery);
		List<ProductDataSearchPicDTO> resultList = searchDetailPicture.getResultList();

		//同比数据时间范围
		Date start = DateUtil.offset(s, DateField.YEAR, -1);
		Date end = DateUtil.offset(e, DateField.YEAR, -1);
		log.info("同比时间范围, 开始时间: {}, 结束时间: {}", start, end);

		//环比数据时间范围
		Date start1 = DateUtil.offset(s, DateField.DAY_OF_MONTH, -num);
		Date end1 = DateUtil.offset(e, DateField.DAY_OF_MONTH, -num);
		log.info("环比时间范围, 开始时间: {}, 结束时间: {}", start1, end1);

		//数据查询拼接
		ProductDataSearchPicQuery query = new ProductDataSearchPicQuery();
		if (resultList != null) {
			BigDecimal hundred = new BigDecimal(100);
			resultList.forEach(a -> {
				query.setWord(a.getWord());
				query.setStartDate(DateUtil.formatDateTime(start));
				query.setEndDate(DateUtil.formatDateTime(end));
				Integer tongBi = productCountDao.getSearchDetailLastYear(query);
				if (tongBi == null) {
					a.setFigureLastYear(BigDecimal.ZERO);
				} else {
					BigDecimal count = (BigDecimal.valueOf(a.getFrequency()).subtract(BigDecimal.valueOf(tongBi))).divide(BigDecimal.valueOf(tongBi), 4, RoundingMode.HALF_UP);
					a.setFigureLastYear(count);
				}
				query.setStartDate(DateUtil.formatDateTime(start1));
				query.setEndDate(DateUtil.formatDateTime(end1));
				Integer huanBi = productCountDao.getSearchDetailLastMonth(query);
				if (huanBi == null) {
					a.setFigureLastMonth(BigDecimal.ZERO);
				} else {
					BigDecimal count = (BigDecimal.valueOf(a.getFrequency()).subtract(BigDecimal.valueOf(huanBi))).divide(BigDecimal.valueOf(huanBi), 4, RoundingMode.HALF_UP);
					a.setFigureLastMonth(count);
				}
				a.setPeople(a.getH5People() + a.getMiniPeople());
				a.setFigureLastMonth(Optional.ofNullable(a.getFigureLastMonth()).orElse(BigDecimal.ZERO).multiply(hundred).setScale(2, RoundingMode.HALF_UP));
				a.setFigureLastYear(Optional.ofNullable(a.getFigureLastYear()).orElse(BigDecimal.ZERO).multiply(hundred).setScale(2, RoundingMode.HALF_UP));
			});
		}

		return searchDetailPicture;

	}

	@Override
	public List<ProductDataSearchPicDTO> getSearchDetailPageExcel(ProductDataSearchPicQuery searchPicQuery) {
		searchPicQuery.setProp(checkString(searchPicQuery.getProp()));
		int num;
		Date s;
		Date e;
		if ((searchPicQuery.getEndDate() == null && searchPicQuery.getStartDate() == null) || (searchPicQuery.getEndDate().isEmpty() && searchPicQuery.getStartDate().isEmpty())) {
			//未设置日期范围，默认七天
			num = 7;
			Date[] date = getDate(num);
			s = date[0];
			e = date[1];
			searchPicQuery.setStartDate(DateUtil.formatDateTime(s));
			searchPicQuery.setEndDate(DateUtil.formatDateTime(e));
		} else {
			//设置了日期范围
			s = DateUtil.parseDateTime(searchPicQuery.getStartDate());
			e = DateUtil.parseDateTime(searchPicQuery.getEndDate());
			e = DateUtil.endOfDay(e);
			searchPicQuery.setStartDate(DateUtil.formatDateTime(s));
			searchPicQuery.setEndDate(DateUtil.formatDateTime(e));
			num = Math.toIntExact(DateUtil.between(s, e, DateUnit.DAY));
		}

		//当前数据
		List<ProductDataSearchPicDTO> resultList = productCountDao.getSearchDetailPictureExcel(searchPicQuery);

		//同比数据时间范围
		Date start = DateUtil.offset(s, DateField.YEAR, -1);
		Date end = DateUtil.offset(e, DateField.YEAR, -1);

		//环比数据时间范围
		Date start1 = DateUtil.offset(s, DateField.DAY_OF_MONTH, -num);
		Date end1 = DateUtil.offset(e, DateField.DAY_OF_MONTH, -num);

		//数据查询拼接
		ProductDataSearchPicQuery query = new ProductDataSearchPicQuery();
		if (resultList != null) {
			BigDecimal hundred = new BigDecimal(100);
			resultList.forEach(a -> {
				query.setWord(a.getWord());
				query.setStartDate(DateUtil.formatDateTime(start));
				query.setEndDate(DateUtil.formatDateTime(end));
				Integer tongBi = productCountDao.getSearchDetailLastYear(query);
				if (tongBi == null) {
					a.setFigureLastYear(BigDecimal.ZERO);
				} else {
					BigDecimal count = (BigDecimal.valueOf(a.getFrequency()).subtract(BigDecimal.valueOf(tongBi))).divide(BigDecimal.valueOf(tongBi), 2, RoundingMode.DOWN);
					a.setFigureLastYear(count);
				}
				query.setStartDate(DateUtil.formatDateTime(start1));
				query.setEndDate(DateUtil.formatDateTime(end1));
				Integer huanBi = productCountDao.getSearchDetailLastMonth(query);
				if (huanBi == null) {
					a.setFigureLastMonth(BigDecimal.ZERO);
				} else {
					BigDecimal count = (BigDecimal.valueOf(a.getFrequency()).subtract(BigDecimal.valueOf(huanBi))).divide(BigDecimal.valueOf(huanBi), 2, RoundingMode.DOWN);
					a.setFigureLastMonth(count);
				}
				a.setPeople(a.getH5People() + a.getMiniPeople());
				a.setFigureLastMonth(Optional.ofNullable(a.getFigureLastMonth()).orElse(BigDecimal.ZERO).multiply(hundred));
				a.setFigureLastYear(Optional.ofNullable(a.getFigureLastYear()).orElse(BigDecimal.ZERO).multiply(hundred));
			});
		}

		return resultList;
	}

	@Override
	public ProductDataSpuDTO getShopSpuCount(Long shopId) {
		//获取时间范围 1-一天内 7-一周内 30-一月内
		Date[] date1 = getDate(1);
		Date[] date2 = getDate(7);
		Date[] date3 = getDate(30);

		Integer count1 = productCountDao.queryProdSpuCount(null, date1[1], shopId);
		Integer count2 = productCountDao.queryProdSpuCount(date1[0], date1[1], shopId);
		Integer count3 = productCountDao.queryProdSpuCount(date2[0], date2[1], shopId);
		Integer count4 = productCountDao.queryProdSpuCount(date3[0], date3[1], shopId);

		return ProductDataSpuDTO
				.builder()
				.spuTotalNum(count1)
				.spuTotalNumByDay(count2)
				.spuTotalNumByWeek(count3)
				.spuTotalNumByMonth(count4)
				.build();
	}

	@Override
	public ProductDataSkuDTO getShopSkuCount(Long shopId) {
		//获取时间范围 1-一天内 7-一周内 30-一月内
		Date[] date1 = getDate(1);
		Date[] date2 = getDate(7);
		Date[] date3 = getDate(30);

		Integer count1 = productCountDao.queryProdSkuCount(null, date1[1], shopId);
		Integer count2 = productCountDao.queryProdSkuCount(date1[0], date1[1], shopId);
		Integer count3 = productCountDao.queryProdSkuCount(date2[0], date2[1], shopId);
		Integer count4 = productCountDao.queryProdSkuCount(date3[0], date3[1], shopId);

		return ProductDataSkuDTO
				.builder()
				.skuTotalNum(count1)
				.skuTotalNumByDay(count2)
				.skuTotalNumByWeek(count3)
				.skuTotalNumByMonth(count4)
				.build();
	}

	@Override
	public ProductDataSkuOnSaleDTO getShopSkuOnSaleCount(Long shopId) {

		//获取时间范围 1-一天内 7-一周内 30-一月内
		Date[] date1 = getDate(1);
		Date[] date2 = getDate(7);
		Date[] date3 = getDate(30);

		Integer count1 = productCountDao.queryProdSkuSaleCount(null, date1[1], shopId);
		Integer count2 = productCountDao.queryProdSkuSaleCount(date1[0], date1[1], shopId);
		Integer count3 = productCountDao.queryProdSkuSaleCount(date2[0], date2[1], shopId);
		Integer count4 = productCountDao.queryProdSkuSaleCount(date3[0], date3[1], shopId);

		return ProductDataSkuOnSaleDTO
				.builder()
				.skuTotalNumOnSale(count1)
				.skuTotalNumByDayOnSale(count2)
				.skuTotalNumByWeekOnSale(count3)
				.skuTotalNumByMonthOnSale(count4)
				.build();
	}

	@Override
	public ProductDataSpuClickDTO getShopSpuClickCount(Long shopId) {
		//获取时间范围 1-一天内 7-一周内 30-一月内
		Date[] date1 = getDate(1);
		Date[] date2 = getDate(7);
		Date[] date3 = getDate(30);

		Integer count1 = productCountDao.queryProdViews(null, date1[1], shopId);
		Integer count2 = productCountDao.queryProdViews(date1[0], date1[1], shopId);
		Integer count3 = productCountDao.queryProdViews(date2[0], date2[1], shopId);
		Integer count4 = productCountDao.queryProdViews(date3[0], date3[1], shopId);

		return ProductDataSpuClickDTO
				.builder()
				.spuClickTotalNum(Optional.ofNullable(count1).orElse(0))
				.spuClickNumByDay(Optional.ofNullable(count2).orElse(0))
				.spuClickNumByWeek(Optional.ofNullable(count3).orElse(0))
				.spuClickNumByMonth(Optional.ofNullable(count4).orElse(0))
				.build();
	}

	@Override
	public List<ProductDataSkuByCategoryDTO> getShopSkuByCategory(Long shopId) {
		String now = DateUtil.now();
		Date date = DateUtil.parse(now);
		Date beginOfDay = DateUtil.beginOfDay(date);
		Date endDate = DateUtil.offset(beginOfDay, DateField.SECOND, -1);

		List<ProductDataSkuByCategoryDTO> list = productCountDao.queryShopProdSkuByCategory(endDate, shopId);

		AtomicInteger num = new AtomicInteger(1);
		list.forEach(a -> a.setOrderNum(num.getAndIncrement()));

		return list;
	}

	@Override
	public List<ProductDataViewLineDTO> getShopViewLine(ProductDataViewQuery viewQuery) {

		Long shopId = viewQuery.getShopId();
		Long goodId = viewQuery.getGoodId();
		List<Date> dateAvg;
		if ((viewQuery.getEndDate() == null && viewQuery.getStartDate() == null) || (viewQuery.getEndDate().isEmpty() && viewQuery.getStartDate().isEmpty())) {
			//未设置日期范围，默认七天
			Integer num = 7;
			Date[] date = getDate(num);
			Date s = date[0];
			Date e = date[1];
			dateAvg = getDateAvg(s, e);
			viewQuery.setStartDate(DateUtil.formatDateTime(s));
			viewQuery.setEndDate(DateUtil.formatDateTime(e));
		} else {
			Date s = DateUtil.parseDateTime(viewQuery.getStartDate());
			Date e = DateUtil.parseDateTime(viewQuery.getEndDate());
			dateAvg = getDateAvg(s, e);
		}
		List<ProductDataViewLineDTO> list = new ArrayList<>();
		if (goodId != null) {
			for (int x = 0; x < dateAvg.size() - 1; x++) {
				viewQuery.setStartDate(DateUtil.formatDateTime(dateAvg.get(x)));
				viewQuery.setEndDate(DateUtil.formatDateTime(DateUtil.offset(dateAvg.get(x + 1), DateField.SECOND, -1)));
				ProductDataViewLineDTO dto = productCountDao.queryProductViewLineByProdId(viewQuery);
				if (dto == null || dto.getFrequency() == null) {
					dto = new ProductDataViewLineDTO(0);
				}
				dto.setTime(dateAvg.get(x));
				list.add(dto);
			}
			return list;
		} else if (shopId != null) {
			for (int x = 0; x < dateAvg.size() - 1; x++) {
				viewQuery.setStartDate(DateUtil.formatDateTime(dateAvg.get(x)));
				viewQuery.setEndDate(DateUtil.formatDateTime(DateUtil.offset(dateAvg.get(x + 1), DateField.SECOND, -1)));
				ProductDataViewLineDTO dto = productCountDao.queryProductViewLineByShopId(viewQuery);
				if (dto == null || dto.getFrequency() == null) {
					dto = new ProductDataViewLineDTO(0);
				}
				dto.setTime(dateAvg.get(x));
				list.add(dto);
			}
			return list;
		}
		return list;
	}

	@Override
	public List<ProductDataCategoryDTO> getShopCategoryTree(ProductDataCategoryQuery query) {

		query.setEndDate(null);
		query.setStartDate(null);

		//模糊查询类目名的顶层类目id的list
		List<Long> integers = new ArrayList<>();
		List<CategoryBO> list = productCountDao.queryShopCategoryByName(query);
		list.forEach(a -> {
			if (a.getParentId() == -1) {
				integers.add(a.getId());
			} else if (a.getGrade() == 2) {
				integers.add(a.getParentId());
			} else {
				List<CategoryBO> list1 = productCountDao.queryShopCategoryById(a.getParentId(), query.getShopId());
				list1.forEach(b -> {
					integers.add(b.getParentId());
				});
			}
		});
		List<Long> collect = integers.stream().distinct().collect(Collectors.toList());
		//根据类目id获得类目树
		List<ProductDataCategoryDTO> topList = new ArrayList<>();
		collect.forEach(c -> {
			ProductDataCategoryDTO productDataCategoryDTO = productCountDao.queryShopTopCategory(c, query);
			if (productDataCategoryDTO != null) {
				topList.add(productDataCategoryDTO);
			}
		});
		Integer sum = getSkuSumByCategory(topList);
		BigDecimal hundred = new BigDecimal(100);
		topList.forEach(a -> {
			a.setPercentage(BigDecimal.ZERO);
			if (sum > 0) {
				a.setPercentage(BigDecimal.valueOf(a.getSkuNum()).multiply(hundred).divide(BigDecimal.valueOf(sum), 2, RoundingMode.HALF_DOWN));
			}
			List<ProductDataCategoryDTO> secondList = productCountDao.queryShopSecondCategory(a.getId(), query);
			Integer secondSum = getSkuSumByCategory(secondList);
			secondList.forEach(b -> {
				b.setPercentage(BigDecimal.ZERO);
				if (secondSum > 0) {
					b.setPercentage(BigDecimal.valueOf(b.getSkuNum()).multiply(hundred).divide(BigDecimal.valueOf(secondSum), 2, RoundingMode.HALF_DOWN));
				}
				List<ProductDataCategoryDTO> thirdList = productCountDao.queryShopThirdCategory(b.getId(), query);
				Integer thirdSum = getSkuSumByCategory(thirdList);
				thirdList.forEach(c -> {
					c.setPercentage(BigDecimal.ZERO);
					if (thirdSum > 0) {
						c.setPercentage(BigDecimal.valueOf(c.getSkuNum()).multiply(hundred).divide(BigDecimal.valueOf(thirdSum), 2, RoundingMode.HALF_DOWN));
					}
				});
				b.setCategoryDTOList(thirdList);
			});
			a.setCategoryDTOList(secondList);
		});
		return topList;
	}

	@Override
	public List<ProductDataCategoryDTO> getShopCategory(ProductDataCategoryQuery query) {
		if (query.getGrade() == null) {
			return Collections.emptyList();
		}

		if ((query.getEndDate() == null && query.getStartDate() == null) || (query.getEndDate().isEmpty() && query.getStartDate().isEmpty())) {
			//未设置日期范围，默认七天
			Integer num = 7;
			Date[] date = getDate(num);
			Date e = date[1];
			query.setEndDate(DateUtil.formatDateTime(e));
		} else {
			//设置了日期范围
			Date e = DateUtil.parseDateTime(query.getEndDate());
			e = DateUtil.endOfDay(e);
			query.setEndDate(DateUtil.formatDateTime(e));
		}

		List<ProductDataCategoryDTO> topList = new ArrayList<>();
		if (query.getGrade() == 1) {
			topList = productCountDao.queryShopTopCategory(query);
		} else if (query.getGrade() == 2) {
			topList = productCountDao.queryShopSecondCategory(null, query);
		} else if (query.getGrade() == 3) {
			topList = productCountDao.queryShopThirdCategory(null, query);
		}

		Integer sum = getSkuSumByCategory(topList);
		BigDecimal hundred = new BigDecimal(100);
		topList.forEach(a -> {
			a.setPercentage(BigDecimal.valueOf(Optional.ofNullable(a.getSkuNum()).orElse(0)).multiply(hundred).divide(BigDecimal.valueOf(sum), 2, RoundingMode.HALF_DOWN));
		});

		return topList;
	}

	@Override
	public PageSupport<ProductDataCategoryPageDTO> getShopCategoryDetailPage(ProductDataCategoryQuery categoryQuery) {

		categoryQuery.setProp(checkString(categoryQuery.getProp()));
		if ((categoryQuery.getEndDate() == null && categoryQuery.getStartDate() == null) || (categoryQuery.getEndDate().isEmpty() && categoryQuery.getStartDate().isEmpty())) {
			//未设置日期范围，截至到前一天最后一秒
			Integer num = 7;
			Date[] date = getDate(num);
			Date e = date[1];
			categoryQuery.setEndDate(DateUtil.formatDateTime(e));
			categoryQuery.setStartDate(DateUtil.formatDateTime(date[0]));

			return productCountDao.queryShopCategoryDetailPage(categoryQuery);
		}
		//设置了日期范围
		Date e = DateUtil.parseDate(categoryQuery.getEndDate());
		e = DateUtil.endOfDay(e);
		categoryQuery.setEndDate(DateUtil.formatDateTime(e));
		categoryQuery.setStartDate(DateUtil.formatDateTime(DateUtil.beginOfDay(DateUtil.parseDate(categoryQuery.getStartDate()))));

		return productCountDao.queryShopCategoryDetailPage(categoryQuery);
	}

	@Override
	public List<ProductDataCategoryPageDTO> getShopCategoryDetailPageExcel(ProductDataCategoryQuery categoryQuery) {
		categoryQuery.setProp(checkString(categoryQuery.getProp()));
		if ((categoryQuery.getEndDate() == null && categoryQuery.getStartDate() == null) || (categoryQuery.getEndDate().isEmpty() && categoryQuery.getStartDate().isEmpty())) {
			//未设置日期范围，截至到前一天最后一秒
			Integer num = 7;
			Date[] date = getDate(num);
			Date e = date[1];
			categoryQuery.setEndDate(DateUtil.formatDateTime(e));

			return productCountDao.queryShopCategoryDetailPageExcel(categoryQuery);
		}
		//设置了日期范围
		Date e = DateUtil.parseDate(categoryQuery.getEndDate());
		e = DateUtil.endOfDay(e);
		categoryQuery.setEndDate(DateUtil.formatDateTime(e));

		return productCountDao.queryShopCategoryDetailPageExcel(categoryQuery);
	}

	@Override
	public PageSupport<ProductDataShopSaleDTO> getShopSaleDetailPage(ProductDataSaleQuery productDataSaleQuery) {
		productDataSaleQuery.setProp(checkString(productDataSaleQuery.getProp()));
		if ((productDataSaleQuery.getEndDate() == null && productDataSaleQuery.getStartDate() == null) || (productDataSaleQuery.getEndDate().isEmpty() && productDataSaleQuery.getStartDate().isEmpty())) {
			//未设置日期范围，默认七天
			Integer num = 7;
			Date[] date = getDate(num);
			Date s = date[0];
			Date e = date[1];
			productDataSaleQuery.setStartDate(DateUtil.formatDateTime(s));
			productDataSaleQuery.setEndDate(DateUtil.formatDateTime(e));
			return productCountDao.getShopSaleDetailPage(productDataSaleQuery);
		}
		//设置了日期范围
		Date e = DateUtil.parseDate(productDataSaleQuery.getEndDate());
		Date end = DateUtil.endOfDay(e);
		productDataSaleQuery.setEndDate(DateUtil.formatDateTime(end));
		PageSupport<ProductDataShopSaleDTO> list = productCountDao.getShopSaleDetailPage(productDataSaleQuery);
		return list;
	}

	@Override
	public List<ProductDataShopSaleDTO> getShopSaleDetailPageExcel(ProductDataSaleQuery productDataSaleQuery) {
		productDataSaleQuery.setProp(checkString(productDataSaleQuery.getProp()));
		if ((productDataSaleQuery.getEndDate() == null && productDataSaleQuery.getStartDate() == null) || (productDataSaleQuery.getEndDate().isEmpty() && productDataSaleQuery.getStartDate().isEmpty())) {
			//未设置日期范围，默认七天
			Integer num = 7;
			Date[] date = getDate(num);
			Date s = date[0];
			Date e = date[1];
			productDataSaleQuery.setStartDate(DateUtil.formatDateTime(s));
			productDataSaleQuery.setEndDate(DateUtil.formatDateTime(e));
			return productCountDao.getShopSaleDetailPageExcel(productDataSaleQuery);
		}
		//设置了日期范围
		Date e = DateUtil.parseDate(productDataSaleQuery.getEndDate());
		Date end = DateUtil.endOfDay(e);
		productDataSaleQuery.setEndDate(DateUtil.formatDateTime(end));
		List<ProductDataShopSaleDTO> list = productCountDao.getShopSaleDetailPageExcel(productDataSaleQuery);
		return list;
	}

	@Override
	public PageSupport<AdminProductDataShopSaleDTO> getAdminShopSaleDetailPage(ProductDataSaleQuery productDataSaleQuery) {
		productDataSaleQuery.setProp(checkString(productDataSaleQuery.getProp()));
		if ((productDataSaleQuery.getEndDate() == null && productDataSaleQuery.getStartDate() == null) || (productDataSaleQuery.getEndDate().isEmpty() && productDataSaleQuery.getStartDate().isEmpty())) {
			//未设置日期范围，默认七天
			Integer num = 7;
			Date[] date = getDate(num);
			Date s = date[0];
			Date e = date[1];
			productDataSaleQuery.setStartDate(DateUtil.formatDateTime(s));
			productDataSaleQuery.setEndDate(DateUtil.formatDateTime(e));
			return productCountDao.getAdminShopSaleDetailPage(productDataSaleQuery);
		}
		//设置了日期范围
		Date e = DateUtil.parseDate(productDataSaleQuery.getEndDate());
		Date end = DateUtil.endOfDay(e);
		productDataSaleQuery.setEndDate(DateUtil.formatDateTime(end));
		return productCountDao.getAdminShopSaleDetailPage(productDataSaleQuery);
	}

	@Override
	public List<AdminProductDataShopSaleDTO> getAdminShopSaleDetailPageExcel(ProductDataSaleQuery productDataSaleQuery) {
		productDataSaleQuery.setProp(checkString(productDataSaleQuery.getProp()));
		if ((productDataSaleQuery.getEndDate() == null && productDataSaleQuery.getStartDate() == null) || (productDataSaleQuery.getEndDate().isEmpty() && productDataSaleQuery.getStartDate().isEmpty())) {
			//未设置日期范围，默认七天
			Integer num = 7;
			Date[] date = getDate(num);
			Date s = date[0];
			Date e = date[1];
			productDataSaleQuery.setStartDate(DateUtil.formatDateTime(s));
			productDataSaleQuery.setEndDate(DateUtil.formatDateTime(e));
			return productCountDao.getAdminShopSaleDetailPageExcel(productDataSaleQuery);
		}
		//设置了日期范围
		Date e = DateUtil.parseDate(productDataSaleQuery.getEndDate());
		Date end = DateUtil.endOfDay(e);
		productDataSaleQuery.setEndDate(DateUtil.formatDateTime(end));
		return productCountDao.getAdminShopSaleDetailPageExcel(productDataSaleQuery);
	}

	@Override
	public List<ProductDataGoodSaleDTO> getShopSaleTrendLine(ProductDataSaleTrendQuery trendQuery) {
		List<Date> dateAvg;
		if ((trendQuery.getEndDate() == null && trendQuery.getStartDate() == null) || (trendQuery.getEndDate().isEmpty() && trendQuery.getStartDate().isEmpty())) {
			//未设置日期范围，默认七天
			Integer num = 7;
			Date[] date = getDate(num);
			Date s = date[0];
			Date e = date[1];
			dateAvg = getDateAvg(s, e);
			trendQuery.setStartDate(DateUtil.formatDateTime(s));
			trendQuery.setEndDate(DateUtil.formatDateTime(e));
		} else {
			Date s = DateUtil.parseDateTime(trendQuery.getStartDate());
			Date e = DateUtil.parseDateTime(trendQuery.getEndDate());
			dateAvg = getDateAvg(s, e);
		}

		List<ProductDataGoodSaleDTO> list = new ArrayList<>();
		for (int x = 0; x < dateAvg.size() - 1; x++) {
			trendQuery.setSDate(dateAvg.get(x));
			trendQuery.setEDate(DateUtil.offset(dateAvg.get(x + 1), DateField.SECOND, -1));
			ProductDataGoodSaleDTO dto = productCountDao.getShopSaleTrendLine(trendQuery);
			if (dto == null) {
				dto = new ProductDataGoodSaleDTO();
			}
			dto.setTime(dateAvg.get(x));
			dto.setDealAmount(Optional.ofNullable(dto.getDealAmount()).orElse(BigDecimal.ZERO));
			dto.setDealGoodNum(Optional.ofNullable(dto.getDealGoodNum()).orElse(0));
			dto.setTotalDealAmount(dto.getDealAmount());
			dto.setTotalDealGoodNum(dto.getDealGoodNum());
			if (x > 0) {
				ProductDataGoodSaleDTO old = list.get(x - 1);
				dto.setTotalDealGoodNum(dto.getTotalDealGoodNum() + old.getTotalDealGoodNum());
				dto.setTotalDealAmount(dto.getTotalDealAmount().add(old.getTotalDealAmount()));
			}
			list.add(dto);
		}

		return list;
	}

	@Override
	public List<ProductDataSaleDealDTO> getShopSaleDealPage(ProductDataSaleTrendQuery trendQuery) {
		//将排序字段转化成数据库识别字段
		trendQuery.setProp(checkString(trendQuery.getProp()));

		List<Date> dateList;
		if ((trendQuery.getEndDate() == null && trendQuery.getStartDate() == null) || (trendQuery.getEndDate().isEmpty() && trendQuery.getStartDate().isEmpty())) {
			//未设置日期范围，默认七天
			//时间列表，间隔单位天
			Integer num = 7;
			Date[] date = getDate(num);
			Date s = date[0];
			Date e = date[1];
			dateList = getDateSplit(s, e);
			trendQuery.setStartDate(DateUtil.formatDateTime(s));
			trendQuery.setEndDate(DateUtil.formatDateTime(e));
		} else {
			Date s = DateUtil.parseDateTime(trendQuery.getStartDate());
			Date e = DateUtil.parseDateTime(trendQuery.getEndDate());
			dateList = getDateSplit(s, e);
		}
		//获取结果集
		List<ProductDataSaleDealDTO> list = productCountDao.getShopSaleDealList(trendQuery);
		if (CollUtil.isNotEmpty(list)) {
			for (int i = 0; i < list.size(); i++) {
				ProductDataSaleDealDTO dto = list.get(i);
				dto.setDealAmount(Optional.ofNullable(dto.getDealAmount()).orElse(BigDecimal.ZERO));
				dto.setDealGoodNum(Optional.ofNullable(dto.getDealGoodNum()).orElse(0));
				dto.setTotalDealAmount(dto.getDealAmount());
				dto.setTotalDealGoodNum(dto.getDealGoodNum());
				if (i > 0) {
					ProductDataSaleDealDTO old = list.get(i - 1);
					dto.setTotalDealGoodNum(dto.getTotalDealGoodNum() + old.getTotalDealGoodNum());
					dto.setTotalDealAmount(dto.getTotalDealAmount().add(old.getTotalDealAmount()));
				}
			}
		}

		AtomicReference<Integer> num = new AtomicReference<>(0);
		ProductDataSaleDealDTO dto = null;
		if (list.size() > 0) {
			dto = list.get(num.get());
		}
		List<ProductDataSaleDealDTO> resultList = new ArrayList<>();
		//根据排序条件对结果集进行日期连续填充
		//不排序时
		if (trendQuery.getOrder() == null || StrUtil.isBlank(trendQuery.getOrder())) {
			ProductDataSaleDealDTO finalDto = dto;
			dateList.forEach(a -> {
				//空日期填充新空对象
				ProductDataSaleDealDTO dto1 = new ProductDataSaleDealDTO();
				dto1.setDealAmount(new BigDecimal(0));
				dto1.setDealGoodNum(0);
				if (num.get() < list.size() && DateUtil.compare(a, list.get(num.get()).getTime()) == 0) {
					ProductDataSaleDealDTO dealDTO = list.get(num.get());
					if (resultList.size() - 1 >= 0) {
						ProductDataSaleDealDTO old = resultList.get(resultList.size() - 1);
						dealDTO.setTotalDealAmount(dealDTO.getDealAmount().add(old.getTotalDealAmount()));
						dealDTO.setTotalDealGoodNum(dealDTO.getDealGoodNum() + old.getTotalDealGoodNum());
					} else {
						dealDTO.setTotalDealAmount(dealDTO.getDealAmount());
						dealDTO.setTotalDealGoodNum(dealDTO.getDealGoodNum());
					}
					resultList.add(dealDTO);
					num.getAndSet(num.get() + 1);
				} else {

					if (resultList.size() - 1 >= 0) {
						ProductDataSaleDealDTO dealDTO = resultList.get(resultList.size() - 1);
						dto1.setTotalDealAmount(dealDTO.getTotalDealAmount());
						dto1.setTotalDealGoodNum(dealDTO.getTotalDealGoodNum());
					}
					dto1.setTime(a);
					resultList.add(dto1);
				}
			});
		} else {
			//降序时
			if (Objects.equals(trendQuery.getOrder(), "desc")) {
				resultList.addAll(list);
				ProductDataSaleDealDTO finalDto1 = dto;
				dateList.removeIf(date -> {
					AtomicBoolean flag = new AtomicBoolean();
					for (ProductDataSaleDealDTO a : list) {
						if (DateUtil.compare(a.getTime(), date) == 0) {
							flag.set(true);
							break;
						} else {
							flag.set(false);
						}
					}
					return flag.get();
				});
				dateList.forEach(a -> {
					ProductDataSaleDealDTO dto1 = new ProductDataSaleDealDTO();
					dto1.setDealAmount(new BigDecimal(0));
					dto1.setDealGoodNum(0);
					if (finalDto1 != null) {
						dto1.setTotalDealAmount(finalDto1.getTotalDealAmount());
						dto1.setTotalDealGoodNum(finalDto1.getTotalDealGoodNum());
					}
					for (int x = 0; x < list.size(); x++) {
						if (num.get() < list.size() && DateUtil.compare(a, list.get(num.get()).getTime()) == 0) {
							num.getAndSet(num.get() + 1);
							break;
						}
						dto1.setTime(a);
						resultList.add(dto1);
						break;
					}
				});
			} else {
				//升序时
				dateList.removeIf(date -> {
					AtomicBoolean flag = new AtomicBoolean();
					for (ProductDataSaleDealDTO a : list) {
						if (DateUtil.compare(a.getTime(), date) == 0) {
							flag.set(true);
							break;
						} else {
							flag.set(false);
						}
					}
					return flag.get();
				});
				ProductDataSaleDealDTO finalDto2 = dto;
				dateList.forEach(a -> {
					ProductDataSaleDealDTO dto1 = new ProductDataSaleDealDTO();
					dto1.setDealAmount(new BigDecimal(0));
					dto1.setDealGoodNum(0);
					if (finalDto2 != null) {
						dto1.setTotalDealAmount(finalDto2.getTotalDealAmount());
						dto1.setTotalDealGoodNum(finalDto2.getTotalDealGoodNum());
					}
					for (int x = 0; x < list.size(); x++) {
						if (num.get() < list.size() && DateUtil.compare(a, list.get(num.get()).getTime()) == 0) {
							num.getAndSet(num.get() + 1);
							break;
						}
						dto1.setTime(a);
						resultList.add(dto1);
						break;
					}
				});
				resultList.addAll(list);
			}
		}

		return resultList;
	}

	@Override
	public List<ProductDataSaleViewDTO> getShopSaleViewPage(ProductDataSaleTrendQuery trendQuery) {

		//将排序字段转化成数据库识别字段
		trendQuery.setProp(checkString(trendQuery.getProp()));

		List<Date> dateList;
		if ((trendQuery.getEndDate() == null && trendQuery.getStartDate() == null) || (trendQuery.getEndDate().isEmpty() && trendQuery.getStartDate().isEmpty())) {
			//未设置日期范围，默认七天
			//时间列表，间隔单位天
			Integer num = 7;
			Date[] date = getDate(num);
			Date s = date[0];
			Date e = date[1];
			dateList = getDateSplit(s, e);
			trendQuery.setStartDate(DateUtil.formatDateTime(s));
			trendQuery.setEndDate(DateUtil.formatDateTime(e));
		} else {
			Date s = DateUtil.parseDateTime(trendQuery.getStartDate());
			Date e = DateUtil.parseDateTime(trendQuery.getEndDate());
			dateList = getDateSplit(s, e);
		}
		//获取结果集
		List<ProductDataSaleViewDTO> list = productCountDao.getShopSaleViewPage(trendQuery);

		AtomicReference<Integer> num = new AtomicReference<>(0);
		ProductDataSaleViewDTO dto = null;
		if (list.size() > 0) {
			dto = list.get(num.get());
		}
		List<ProductDataSaleViewDTO> resultList = new ArrayList<>();
		//根据排序条件对结果集进行日期连续填充
		//不排序时
		if (trendQuery.getOrder() == null) {
			ProductDataSaleViewDTO finalDto = dto;
			dateList.forEach(a -> {
				//空日期填充新空对象
				ProductDataSaleViewDTO dto1 = new ProductDataSaleViewDTO();
				dto1.setViewNum(0);
				if (finalDto != null) {
					dto1.setTotalViewNum(finalDto.getTotalViewNum());
				}
				if (num.get() < list.size() && DateUtil.compare(a, list.get(num.get()).getTime()) == 0) {
					resultList.add(list.get(num.get()));
					num.getAndSet(num.get() + 1);
				} else {
					dto1.setTime(a);
					resultList.add(dto1);
				}
			});
		} else {
			//降序时
			if (Objects.equals(trendQuery.getOrder(), "desc")) {
				resultList.addAll(list);
				ProductDataSaleViewDTO finalDto1 = dto;
				dateList.forEach(a -> {
					ProductDataSaleViewDTO dto1 = new ProductDataSaleViewDTO();
					dto1.setViewNum(0);
					if (finalDto1 != null) {
						dto1.setTotalViewNum(finalDto1.getTotalViewNum());
					}
					for (int x = 0; x < list.size(); x++) {
						if (num.get() < list.size() && DateUtil.compare(a, list.get(num.get()).getTime()) == 0) {
							num.getAndSet(num.get() + 1);
							continue;
						}
						dto1.setTime(a);
						resultList.add(dto1);
						break;
					}
				});
			} else {
				//升序时
				ProductDataSaleViewDTO finalDto2 = dto;
				dateList.forEach(a -> {
					ProductDataSaleViewDTO dto1 = new ProductDataSaleViewDTO();
					dto1.setViewNum(0);
					if (finalDto2 != null) {
						dto1.setTotalViewNum(finalDto2.getTotalViewNum());
					}
					for (int x = 0; x < list.size(); x++) {
						if (num.get() < list.size() && DateUtil.compare(a, list.get(num.get()).getTime()) == 0) {
							num.getAndSet(num.get() + 1);
							continue;
						}
						dto1.setTime(a);
						resultList.add(dto1);
						break;
					}
				});
				resultList.addAll(list);
			}
		}

		return resultList;
	}

	@Override
	public List<ProductDataSaleFavoriteDTO> getShopSaleFavoritePage(ProductDataSaleTrendQuery trendQuery) {

		//将排序字段转化成数据库识别字段
		trendQuery.setProp(checkString(trendQuery.getProp()));

		List<Date> dateList;
		if ((trendQuery.getEndDate() == null && trendQuery.getStartDate() == null) || (trendQuery.getEndDate().isEmpty() && trendQuery.getStartDate().isEmpty())) {
			//未设置日期范围，默认七天
			//时间列表，间隔单位天
			Integer num = 7;
			Date[] date = getDate(num);
			Date s = date[0];
			Date e = date[1];
			dateList = getDateSplit(s, e);
			trendQuery.setStartDate(DateUtil.formatDateTime(s));
			trendQuery.setEndDate(DateUtil.formatDateTime(e));
		} else {
			Date s = DateUtil.parseDateTime(trendQuery.getStartDate());
			Date e = DateUtil.parseDateTime(trendQuery.getEndDate());
			dateList = getDateSplit(s, e);
		}
		//获取结果集
		List<ProductDataSaleFavoriteDTO> list = productCountDao.getShopSaleFavoritePage(trendQuery);

		AtomicReference<Integer> num = new AtomicReference<>(0);
		ProductDataSaleFavoriteDTO dto = null;
		if (list.size() > 0) {
			dto = list.get(num.get());
		}
		List<ProductDataSaleFavoriteDTO> resultList = new ArrayList<>();
		//根据排序条件对结果集进行日期连续填充
		//不排序时
		if (trendQuery.getOrder() == null) {
			ProductDataSaleFavoriteDTO finalDto = dto;
			dateList.forEach(a -> {
				//空日期填充新空对象
				ProductDataSaleFavoriteDTO dto1 = new ProductDataSaleFavoriteDTO();
				dto1.setFavoriteNum(0);
				if (finalDto != null) {
					dto1.setTotalFavoriteNum(finalDto.getTotalFavoriteNum());
				}
				if (num.get() < list.size() && DateUtil.compare(a, list.get(num.get()).getTime()) == 0) {
					resultList.add(list.get(num.get()));
					num.getAndSet(num.get() + 1);
				} else {
					dto1.setTime(a);
					resultList.add(dto1);
				}
			});
		} else {
			//降序时
			if (Objects.equals(trendQuery.getOrder(), "desc")) {
				resultList.addAll(list);
				ProductDataSaleFavoriteDTO finalDto1 = dto;
				dateList.forEach(a -> {
					ProductDataSaleFavoriteDTO dto1 = new ProductDataSaleFavoriteDTO();
					dto1.setFavoriteNum(0);
					if (finalDto1 != null) {
						dto1.setTotalFavoriteNum(finalDto1.getTotalFavoriteNum());
					}
					for (int x = 0; x < list.size(); x++) {
						if (num.get() < list.size() && DateUtil.compare(a, list.get(num.get()).getTime()) == 0) {
							num.getAndSet(num.get() + 1);
							continue;
						}
						dto1.setTime(a);
						resultList.add(dto1);
						break;
					}
				});
			} else {
				//升序时
				ProductDataSaleFavoriteDTO finalDto2 = dto;
				dateList.forEach(a -> {
					ProductDataSaleFavoriteDTO dto1 = new ProductDataSaleFavoriteDTO();
					dto1.setFavoriteNum(0);
					if (finalDto2 != null) {
						dto1.setTotalFavoriteNum(finalDto2.getTotalFavoriteNum());
					}
					for (int x = 0; x < list.size(); x++) {
						if (num.get() < list.size() && DateUtil.compare(a, list.get(num.get()).getTime()) == 0) {
							num.getAndSet(num.get() + 1);
							continue;
						}
						dto1.setTime(a);
						resultList.add(dto1);
						break;
					}
				});
				resultList.addAll(list);
			}
		}

		return resultList;
	}

	/**
	 * 获取二级分类节点集合
	 *
	 * @param num 父节点id
	 * @return
	 */
	private List<ProductDataCategoryDTO> getSecondCategoryList(Long num, ProductDataCategoryQuery query) {
		return productCountDao.querySecondCategory(num, query);
	}

	/**
	 * 获取三级分类节点集合
	 *
	 * @param num 父节点id
	 * @return
	 */
	private List<ProductDataCategoryDTO> getThirdCategoryList(Long num, ProductDataCategoryQuery query) {
		return productCountDao.queryThirdCategory(num, query);
	}

	private Integer getSkuSumByCategory(List<ProductDataCategoryDTO> list) {

		AtomicReference<Integer> sum = new AtomicReference<>(0);
		list.forEach(a -> {
			sum.updateAndGet(v -> v + a.getSkuNum());
		});

		return sum.get();
	}

	/**
	 * 将时间范围分成十份，不能均分时添加随机元素，不足十天时根据一天分割。
	 *
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	private List<Date> getDateAvg(Date startDate, Date endDate) {
		int between = Math.toIntExact(DateUtil.between(startDate, endDate, DateUnit.DAY));
		List<Integer> list = new ArrayList<>();
		int avg;
		if (between < 10) {
			for (int x = 1; x <= between; x++) {
				list.add(1);
			}
			avg = 1;
		} else {
			double v = between / 9.0;
			int i = between % 9;
			Integer floor = (int) Math.floor(v);
			for (int x = 1; x < 10; x++) {
				list.add(floor);
			}
			avg = floor;
			while (i > 0) {
				int random = (int) Math.floor(Math.random() * 9);
				Integer now = list.get(random);
				if (now > floor) {
					continue;
				}
				list.set(random, now + 1);
				i--;
			}
		}
		List<Date> dateList = new ArrayList<>();
		dateList.add(startDate);
		AtomicReference<Date> temp = new AtomicReference<>(startDate);
		list.forEach(a -> {
			Date offset = DateUtil.offset(temp.get(), DateField.DAY_OF_YEAR, a);
			temp.set(offset);
			dateList.add(offset);
		});
		dateList.add(DateUtil.offset(temp.get(), DateField.DAY_OF_YEAR, avg));
		return dateList;
	}

	/**
	 * 根据天数获取--前num天至昨天23点59分59秒的date数组。
	 *
	 * @param num 天数
	 * @return date[0]-开始时间  date[1]-结束时间
	 */
	private Date[] getDate(Integer num) {

		String now = DateUtil.now();
		Date date = DateUtil.parse(now);

		Date beginOfDay = DateUtil.beginOfDay(date);
		Date startDate = DateUtil.offset(beginOfDay, DateField.DAY_OF_MONTH, -num);
		Date endDate = DateUtil.offset(beginOfDay, DateField.SECOND, -1);

		Date[] array = new Date[2];
		array[0] = startDate;
		array[1] = endDate;

		return array;
	}

	/**
	 * prop转成数据库的字段
	 *
	 * @param str
	 * @return
	 */
	private String checkString(String str) {
		if (str == null || str.isEmpty()) {
			return null;
		}
		if (str.length() <= 1) {
			return str.toLowerCase(Locale.ROOT);
		}
		char ch;
		int num = 0;
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < str.length(); i++) {
			ch = str.charAt(i);
			if (Character.isUpperCase(ch)) {
				result.append(str, num, i).append("_").append((char) (ch + 32));
				num = i + 1;
			}
		}
		if (num < str.length()) {
			result.append(str.substring(num));
		}
		return result.toString();
	}

	/**
	 * 将日期分割成以天为单位的list
	 *
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	private List<Date> getDateSplit(Date startDate, Date endDate) {
		Integer between = Math.toIntExact(DateUtil.between(startDate, endDate, DateUnit.DAY));
		List<Integer> integerList = new ArrayList<>();
		for (int x = 1; x <= between; x++) {
			integerList.add(1);
		}
		List<Date> dateList = new ArrayList<>();
		dateList.add(startDate);
		AtomicReference<Date> temp = new AtomicReference<>(startDate);
		integerList.forEach(a -> {
			Date offset = DateUtil.offset(temp.get(), DateField.DAY_OF_YEAR, a);
			temp.set(offset);
			dateList.add(offset);
		});
		return dateList;
	}


}
