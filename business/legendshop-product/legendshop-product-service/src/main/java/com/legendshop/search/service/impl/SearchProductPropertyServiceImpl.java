/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.search.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.legendshop.product.bo.SkuBO;
import com.legendshop.product.dto.ActivityProductDTO;
import com.legendshop.product.dto.ProductPropertyDTO;
import com.legendshop.product.dto.ProductPropertyValueDTO;
import com.legendshop.search.service.SearchProductPropertyService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author legendshop
 */
@Service
@Slf4j
@AllArgsConstructor
public class SearchProductPropertyServiceImpl implements SearchProductPropertyService {


	@Override
	public List<ProductPropertyDTO> getPropValListByProd(ActivityProductDTO activityProductDTO) {
		List<SkuBO> skuBOS = activityProductDTO.getSkuBOS();
		if (CollUtil.isEmpty(skuBOS)) {
			return null;
		} else {
			String skuType = activityProductDTO.getSkuType();
			Long activityId = activityProductDTO.getActivityId();
			SkuBO defaultSku = null;

			//普通商品则取得列表中第一个有库存的sku作为默认选中的sku
			if (ObjectUtil.isNull(skuType)) {
				if (ObjectUtil.isNotEmpty(activityProductDTO.getSkuId())) {
					defaultSku = skuBOS.stream().filter(e -> e.getId().equals(activityProductDTO.getSkuId())).findFirst().orElse(null);
				} else {
					for (SkuBO skuBO : skuBOS) {
						if (skuBO.getStocks() > 0) {
							defaultSku = skuBO;
							break;
						}
					}
				}
			}

			// 如果前端选中了sku，例如订单详情点击商品时跳转，只要存在就选中这个sku
			else if (ObjectUtil.isNotEmpty(activityProductDTO.getSkuId())) {
				defaultSku = skuBOS.stream().filter(e -> e.getId().equals(activityProductDTO.getSkuId()) && e.getSkuType().equals(skuType)).findFirst().orElse(null);
			}

			// 辉哥：如果为空，则选择第一个为默认选中的sku
			if (null == defaultSku) {
				defaultSku = skuBOS.stream().filter(e -> e.getStocks() > 0).findFirst().orElse(skuBOS.get(0));
			}
			//解析商品规格
			List<ProductPropertyDTO> productPropertyDTOList = JSONUtil.toList(JSONUtil.parseArray(activityProductDTO.getSpecification()), ProductPropertyDTO.class);
			//过滤掉selectFlag=false的属性值
			productPropertyDTOList.forEach(productPropertyDTO -> {
				productPropertyDTO.setProdPropList(productPropertyDTO.getProdPropList().stream().filter(ProductPropertyValueDTO::getSelectFlag).collect(Collectors.toList()));
			});
			//过滤掉prodPropList为空的规格对象
			productPropertyDTOList = productPropertyDTOList.stream().filter(productPropertyDTO -> {
				return CollUtil.isNotEmpty(productPropertyDTO.getProdPropList());
			}).collect(Collectors.toList());
			//全部规格改为false,设为不选中状态
			productPropertyDTOList.forEach(productPropertyDTO -> {
				productPropertyDTO.setProdPropList(productPropertyDTO.getProdPropList().stream()
						.map(productPropertyValueDTO -> {
							productPropertyValueDTO.setSelectFlag(false);
							return productPropertyValueDTO;
						}).collect(Collectors.toList()));
			});
			//将默认sku下的规格设为选中状态
			if (ObjectUtil.isNotEmpty(defaultSku)) {
				//取得sku规格字符串(k1:v1;k2:v2;k3:v3)中的每对key:value,并将属于默认sku规格的value存储起来
				List<Long> selectedVals = new ArrayList<Long>();
				for (SkuBO skuBO : skuBOS) {
					String properties = skuBO.getProperties();
					if (StrUtil.isNotBlank(properties)) {
						String[] skuStrs = properties.split(";");
						for (String skuStr : skuStrs) {
							String[] skuItems = skuStr.split(":");
							Long valueId = Long.valueOf(skuItems[1]);
							if (skuBO.getId().equals(defaultSku.getId())) {
								selectedVals.add(valueId);
							}
						}
					}
				}
				//存储起来的规格改为选中状态
				productPropertyDTOList.forEach(productPropertyDTO -> {
					productPropertyDTO.setProdPropList(productPropertyDTO.getProdPropList().stream()
							.peek(productPropertyValueDTO -> {
								if (selectedVals.contains(productPropertyValueDTO.getId())) {
									productPropertyValueDTO.setSelectFlag(true);
								}
							}).collect(Collectors.toList()));
				});
			}
			return productPropertyDTOList;
		}
	}
}
