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
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.expetion.BusinessException;
import com.legendshop.common.security.utils.UserTokenUtil;
import com.legendshop.product.bo.ProductPropertyBO;
import com.legendshop.product.bo.ProductPropertyValueBO;
import com.legendshop.product.dao.BrandDao;
import com.legendshop.product.dao.ProductDao;
import com.legendshop.product.dao.ProductSnapshotDao;
import com.legendshop.product.dto.ProductSnapshotDTO;
import com.legendshop.product.dto.SaveProductSnapshotDTO;
import com.legendshop.product.entity.Brand;
import com.legendshop.product.entity.Product;
import com.legendshop.product.entity.ProductSnapshot;
import com.legendshop.product.service.ProductSnapshotService;
import com.legendshop.product.service.convert.ProductSnapshotConverter;
import com.legendshop.shop.api.ShopDetailApi;
import com.legendshop.shop.bo.ShopDetailBO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 商品快照 service impl
 *
 * @author legendshop
 */
@Service
public class ProductSnapshotServiceImpl implements ProductSnapshotService {

	@Autowired
	private ProductSnapshotDao productSnapshotDao;

	@Autowired
	private ProductSnapshotConverter productSnapshotConverter;

	@Autowired
	private BrandDao brandDao;

	@Autowired
	private ProductDao productDao;

	@Autowired
	private ShopDetailApi shopDetailApi;

	@Autowired
	private UserTokenUtil userTokenUtil;

	@Autowired
	private HttpServletRequest request;

	@Override
	@Cacheable(value = "productSnapshot", key = "#snapshotId")
	public R<ProductSnapshotDTO> getProductSnapshot(Long snapshotId) {
		ProductSnapshotDTO snapshotDTO = productSnapshotConverter.to(productSnapshotDao.getProductSnapshot(snapshotId));
		Long userId = userTokenUtil.getUserId(request);
		if (null == snapshotDTO) {
			return R.fail("商品快照不存在！");
		}

		//解析参数
		ProductPropertyBO otherParamGroup = null;
		List<ProductPropertyBO> otherParamGroupList = filterParameter(snapshotDTO.getParameter());
		//创建其他参数组对象
		if (CollUtil.isNotEmpty(otherParamGroupList)) {
			otherParamGroup = new ProductPropertyBO();
			otherParamGroup.setGroupName("其他参数");
			otherParamGroup.setProductPropertyBOList(otherParamGroupList);
		}

		//解析参数组
		List<ProductPropertyBO> paramGroupList = filterParameter(snapshotDTO.getParamGroup());
		//创建其他参数组对象
		if (CollUtil.isNotEmpty(paramGroupList)) {
			paramGroupList.forEach(productPropertyBO -> {
				ProductPropertyBO propertyBO = new ProductPropertyBO();
				paramGroupList.add(productPropertyBO);
				propertyBO.setGroupName(productPropertyBO.getGroupName());
				propertyBO.setId(productPropertyBO.getGroupId());
				propertyBO.setProductPropertyBOList(paramGroupList);
				paramGroupList.add(propertyBO);
			});
		}
		//合并参数组和其他参数组
		if (ObjectUtil.isNotEmpty(otherParamGroup)) {
			paramGroupList.add(otherParamGroup);
		}
		snapshotDTO.setParamGroupBOList(paramGroupList);
		//设置店铺信息
		R<ShopDetailBO> shopDetail = shopDetailApi.getUserShop(userId, snapshotDTO.getShopId());
		if (!shopDetail.success()) {
			throw new BusinessException(shopDetail.getMsg());
		}
		snapshotDTO.setShopDetailBO(shopDetail.getData());
		return R.ok(snapshotDTO);
	}

	/**
	 * 过滤参数和参数组
	 */
	private List<ProductPropertyBO> filterParameter(String paramter) {
		List<ProductPropertyBO> parameterList = new ArrayList<>();
		if (StrUtil.isNotBlank(paramter)) {
			JSONUtil.toList(JSONUtil.parseArray(paramter), ProductPropertyBO.class);
			//过滤掉selectFlag=false的参数值
			parameterList.forEach(productPropertyBO -> {
				productPropertyBO.setProdPropList(productPropertyBO.getProdPropList().stream()
						.filter(ProductPropertyValueBO::getSelectFlag).collect(Collectors.toList()));
			});
			//过滤掉prodPropList为空的参数对象
			parameterList = parameterList.stream().filter(productPropertyBO -> {
				return CollUtil.isNotEmpty(productPropertyBO.getProdPropList());
			}).collect(Collectors.toList());
		}
		return parameterList;
	}

	@Override
	public R<Long> saveProdSnapshot(SaveProductSnapshotDTO saveProductSnapshotDTO) {

		Long productId = saveProductSnapshotDTO.getProductId();
		Long skuId = saveProductSnapshotDTO.getSkuId();
		//查询商品详细信息
		Product product = productDao.getById(productId);
		if (ObjectUtil.isNull(product)) {
			return R.fail();
		}
		//根据productId, skuId 和 version 先从快照表里找
		ProductSnapshot productSnapshot = productSnapshotDao.getProductSnapshot(productId, skuId, product.getVersion());

		//快照表里没有找到时，保存到快照表
		if (ObjectUtil.isNull(productSnapshot)) {
			productSnapshot = new ProductSnapshot();
			productSnapshot.setProductId(product.getId());
			productSnapshot.setSkuId(skuId);
			productSnapshot.setVersion(product.getVersion());
			productSnapshot.setShopId(product.getShopId());
			productSnapshot.setName(product.getName());
			productSnapshot.setBrief(product.getBrief());
			productSnapshot.setProductType(product.getProdType());
			productSnapshot.setProductCount(saveProductSnapshotDTO.getBasketCount());
			productSnapshot.setCreateTime(new Date());
			productSnapshot.setContent(product.getContent());
			productSnapshot.setPic(saveProductSnapshotDTO.getPic());
			productSnapshot.setPrice(saveProductSnapshotDTO.getPrice());
			productSnapshot.setOriginalPrice(saveProductSnapshotDTO.getOriginalPrice());
			productSnapshot.setAttribute(saveProductSnapshotDTO.getAttribute());
			productSnapshot.setOriginalPrice(saveProductSnapshotDTO.getOriginalPrice());
			//品牌
			productSnapshot.setBrandName(getBrandName(product.getBrandId()));
			//获取参数
			List<ProductPropertyBO> parameterList = JSONUtil.toList(JSONUtil.parseArray(product.getParameter()), ProductPropertyBO.class);
			List<ProductPropertyBO> userParameterList = JSONUtil.toList(JSONUtil.parseArray(product.getUserParameter()), ProductPropertyBO.class);
			parameterList.addAll(userParameterList);
			if (CollUtil.isNotEmpty(parameterList)) {
				productSnapshot.setParameter(JSONUtil.toJsonStr(parameterList));
			}
			//获取参数组
			List<ProductPropertyBO> paramGroupList = JSONUtil.toList(JSONUtil.parseArray(product.getParamGroup()), ProductPropertyBO.class);
			List<ProductPropertyBO> userParamGroupList = JSONUtil.toList(JSONUtil.parseArray(product.getUserParameter()), ProductPropertyBO.class);
			paramGroupList.addAll(userParamGroupList);
			if (CollUtil.isNotEmpty(paramGroupList)) {
				productSnapshot.setParamGroup(JSONUtil.toJsonStr(paramGroupList));
			}
			//保存
			Long snapshotId = productSnapshotDao.saveProductSnapshot(productSnapshot);
			if (snapshotId <= 0) {
				return R.fail("商品快照生成失败");
			}
			productSnapshot.setId(snapshotId);
		}

		return R.ok(productSnapshot.getId());
	}

	/**
	 * 根据品牌id 获取品牌名称
	 *
	 * @param brandId
	 * @return
	 */
	private String getBrandName(Long brandId) {
		if (ObjectUtil.isNotNull(brandId)) {
			Brand brand = brandDao.getById(brandId);
			if (brand != null) {
				return brand.getBrandName();
			}
		}
		return null;
	}
}
