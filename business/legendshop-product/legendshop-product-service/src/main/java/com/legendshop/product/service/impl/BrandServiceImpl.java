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
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.basic.api.AuditApi;
import com.legendshop.basic.dto.AuditDTO;
import com.legendshop.basic.enums.AuditTypeEnum;
import com.legendshop.common.core.constant.CacheConstants;
import com.legendshop.common.core.constant.R;
import com.legendshop.product.bo.BrandBO;
import com.legendshop.product.bo.ProductBO;
import com.legendshop.product.dao.BrandDao;
import com.legendshop.product.dao.ProductDao;
import com.legendshop.product.dto.BrandDTO;
import com.legendshop.product.entity.Brand;
import com.legendshop.product.enums.BrandStatusEnum;
import com.legendshop.product.excel.BrandExportDTO;
import com.legendshop.product.query.BrandQuery;
import com.legendshop.product.query.ProductQuery;
import com.legendshop.product.service.BrandService;
import com.legendshop.product.service.ProductService;
import com.legendshop.product.service.convert.BrandConverter;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 品牌服务实现
 *
 * @author legendshop
 */
@Service
@AllArgsConstructor
public class BrandServiceImpl implements BrandService {

	final BrandDao brandDao;

	final ProductDao productDao;

	final BrandConverter brandConverter;

	final AuditApi auditApi;

	final ProductService productService;

	@Override
	public BrandBO getById(Long id) {
		BrandBO brandBO = brandConverter.convert2Bo(brandDao.getById(id));
		if (ObjectUtil.isEmpty(brandBO)) {
			return brandBO;
		}
		brandBO.setBrandCount(productService.getBrandId(brandBO.getId()));
		return brandBO;
	}


	@Override
	@CacheEvict(value = CacheConstants.BRAND_DETAILS, allEntries = true)
	public R deleteById(Long brandId) {
		ProductQuery productQuery = new ProductQuery();
		productQuery.setBrandId(brandId);
		PageSupport<ProductBO> page = productService.getPage(productQuery);
		if (page.getTotal() > 0) {
			return R.fail("品牌删除失败，已存在该品牌的商品");
		}
		return R.ok(brandDao.updateDeleteFlag(brandId));
	}


	@Override
	@CacheEvict(value = CacheConstants.BRAND_DETAILS, allEntries = true)
	public R save(BrandDTO brandDTO) {
		if (ObjectUtil.isNotNull(brandDTO.getId())) {
			update(brandDTO);
			return R.ok(brandDTO.getId());
		}
		if (checkBrandByName(brandDTO.getBrandName(), brandDTO.getId())) {
			return R.fail("已有同名品牌存在");
		}
		brandDTO.setCreateTime(new Date());
		brandDTO.setDeleteFlag(false);
		return R.ok(brandDao.save(brandConverter.from(brandDTO)));
	}


	@Override
	@CacheEvict(value = CacheConstants.BRAND_DETAILS, allEntries = true)
	public R update(BrandDTO brandDTO) {
		if (checkBrandByName(brandDTO.getBrandName(), brandDTO.getId())) {
			return R.fail("品牌名称已存在");
		}
		Brand brand = brandConverter.from(brandDTO);
		brand.setUpdateTime(new Date());
		return R.ok(brandDao.update(brand));
	}

	@Override
	public List<BrandBO> getBrandByIds(List<Long> brandIds) {
		return brandConverter.convert2BoList(brandDao.queryAllByIds(brandIds));
	}

	@Override
	public List<BrandBO> getAllOnline(Long shopId, String brandName) {
		List<BrandBO> brandBOList = brandConverter.convert2BoList(brandDao.getAllBrand(shopId, brandName));
		if (CollUtil.isEmpty(brandBOList)) {
			return brandBOList;
		}
		brandBOList.forEach(e -> {
			e.setBrandCount(productService.getBrandId(e.getId()));
		});
		return brandBOList;
	}


	@Override
	public PageSupport<BrandBO> queryPage(BrandQuery brandQuery) {
		PageSupport<BrandBO> page = brandConverter.convert2BoPage(brandDao.queryPage(brandQuery));
		return page;
	}

	@Override
	public boolean checkBrandByName(String brandName, Long brandId) {
		return brandDao.checkBrandByNameByShopId(brandName, brandId);
	}

	@Override
	public List<BrandExportDTO> getExportBrands(BrandQuery brandQuery) {
		List<BrandBO> brandBOList = brandDao.getExportBrands(brandQuery);
		List<BrandExportDTO> dataset = new ArrayList<>();
		//给导出的列表增加序号列
		int sequence = 1;
		for (BrandBO brand1 : brandBOList) {
			BrandExportDTO exportDTO = new BrandExportDTO();
			exportDTO.setId(brand1.getId());
			exportDTO.setCreateTime(brand1.getCreateTime());
			exportDTO.setBrandName(brand1.getBrandName());
			exportDTO.setBrandPic(brand1.getBrandPic());
			if (BrandStatusEnum.BRAND_ONLINE.value().equals(brand1.getStatus())) {
				exportDTO.setStatusStr("上线中");
			}
			if (BrandStatusEnum.BRAND_OFFLINE.value().equals(brand1.getStatus())) {
				exportDTO.setStatusStr("下线中");
			}
			dataset.add(exportDTO);
		}
		return dataset;
	}

	@Override
	@CacheEvict(value = CacheConstants.BRAND_DETAILS, allEntries = true)
	public R updateStatus(Integer status, Long id) {
		ProductQuery productQuery = new ProductQuery();
		productQuery.setBrandId(id);
		PageSupport<ProductBO> page = productService.getPage(productQuery);
		if (page.getTotal() > 0) {
			return R.fail("品牌下架失败，已存在该品牌的商品,不能下架");
		}
		return R.ok(brandDao.updateStatus(status, id));
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	@CacheEvict(value = CacheConstants.BRAND_DETAILS, allEntries = true)
	public R audit(AuditDTO auditDTO) {
		//如果审核通过，品牌马上上线
		int result = brandDao.updateOpStatus(auditDTO.getCommonId(), auditDTO.getOpStatus());
		if (result == 0) {
			return R.fail("审核失败，未找到该品牌");
		}
		auditDTO.setAuditTime(DateUtil.date());
		auditDTO.setAuditType(AuditTypeEnum.BRAND.getValue());
		auditApi.audit(auditDTO);
		return R.ok("审核成功");
	}

}
