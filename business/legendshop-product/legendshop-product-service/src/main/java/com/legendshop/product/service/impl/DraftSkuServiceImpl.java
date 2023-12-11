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
import com.legendshop.common.core.constant.R;
import com.legendshop.product.bo.SkuBO;
import com.legendshop.product.dao.DraftSkuDao;
import com.legendshop.product.dao.SkuDao;
import com.legendshop.product.dto.DraftSkuDTO;
import com.legendshop.product.dto.ProductDTO;
import com.legendshop.product.dto.SkuDTO;
import com.legendshop.product.entity.DraftSku;
import com.legendshop.product.entity.Product;
import com.legendshop.product.entity.Sku;
import com.legendshop.product.service.DraftSkuService;
import com.legendshop.product.service.convert.DraftSkuConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 单品SKU草稿表(DraftSku)表服务实现类
 *
 * @author legendshop
 * @since 2022-05-08 09:37:06
 */
@Service
public class DraftSkuServiceImpl implements DraftSkuService {

	@Autowired
	private SkuDao skuDao;

	@Autowired
	private DraftSkuDao draftSkuDao;

	@Autowired
	private DraftSkuConverter draftSkuConverter;

	@Override
	public R<Void> save(List<SkuDTO> skuList) {
		draftSkuDao.save(draftSkuConverter.convert2DraftSku(skuList));
		return R.ok();
	}

	@Override
	public List<DraftSkuDTO> updateSku(ProductDTO productDTO, Product originProduct, Date date) {
		List<DraftSku> skuList = draftSkuConverter.convert2DraftSku(productDTO.getSku());


		Long productId = productDTO.getId();

		// 获取原sku列表
		List<DraftSku> originSkuList = draftSkuDao.getSkuByProductId(productId);

		// 如果原sku为空，则不需要处理
		if (CollUtil.isNotEmpty(originSkuList)) {
			if (!productDTO.getMultipleSpecificationFlag() && !originProduct.getMultipleSpecificationFlag()) {
				// 如果原商品和新商品都是单sku，则直接获取原skuId
				skuList.get(0).setId(originSkuList.get(0).getId());
				skuList.get(0).setSkuId(originSkuList.get(0).getSkuId());
			} else {
				// 否则，需要匹配规格属性ID，获取原skuId
				for (DraftSku skuDTO : skuList) {
					// 清除skuId，方便后续更新
					skuDTO.setId(null);

					// 按;分隔规格
					List<String> newProperties = StrUtil.split(skuDTO.getProperties(), ';');

					// 排序，方便后续比较
					Collections.sort(newProperties);

					// 获取与上面新的sku规格相同的旧sku
					Optional<DraftSku> optional = originSkuList.stream().filter(e -> {
						List<String> originProperties = StrUtil.split(e.getProperties(), ';');
						Collections.sort(originProperties);
						return newProperties.equals(originProperties);
					}).findFirst();

					// 如果存在，则将旧skuId赋值到新sku上
					optional.ifPresent(dto -> {
						skuDTO.setId(dto.getId());
						skuDTO.setSkuId(dto.getSkuId());
					});
				}
			}
		}

		// 获取需要删除的sku
		List<Long> draftSkuIds = skuList.stream().map(DraftSku::getId).filter(ObjectUtil::isNotEmpty).collect(Collectors.toList());
		List<DraftSku> deleteList = originSkuList.stream().filter(e -> !draftSkuIds.contains(e.getId())).collect(Collectors.toList());
		draftSkuDao.delete(deleteList);

		List<DraftSku> updateList = new ArrayList<>();
		List<DraftSku> saveList = new ArrayList<>();

		for (DraftSku draftSku : skuList) {
			draftSku.setProductId(productId);
			draftSku.setCreateTime(date);
			if (draftSku.getId() != null) {
				updateList.add(draftSku);
			} else {
				draftSku.setSkuId(skuDao.createId());
				saveList.add(draftSku);
			}

			// 设置图片切割规格
			if (ObjectUtil.isNotEmpty(draftSku.getProperties()) && ObjectUtil.isNotEmpty(productDTO.getMainSpecificationId())) {
				String[] specificationMap = draftSku.getProperties().split(";");
				/*在规格列表里找出主规格对应的规格id：规格值id*/
				List<String> value = Arrays.stream(specificationMap).filter(s -> {
					String[] strings = s.split(":");
					return strings[0].compareTo(productDTO.getMainSpecificationId().toString()) == 0;
				}).collect(Collectors.toList());
				/*根据规格值id、规格Id、查找图片，并设置进去*/
				if (CollUtil.isNotEmpty(value)) {
					List<String> imageSrc = productDTO.getImageList().stream().filter(productPropertyImageDTO -> {
						String[] split = value.get(0).split(":");
						return productPropertyImageDTO.getPropId().compareTo(productDTO.getMainSpecificationId()) == 0
								&& productPropertyImageDTO.getValueId().compareTo(Long.parseLong(split[1])) == 0;
					}).map(productPropertyImageDTO -> productPropertyImageDTO.getImgList().get(0))
							.collect(Collectors.toList());
					if (CollUtil.isNotEmpty(imageSrc)) {
						draftSku.setPic(imageSrc.get(0));
					}
				}
			} else {
				draftSku.setPic(productDTO.getPic());
			}
		}

		draftSkuDao.save(saveList);
		draftSkuDao.update(updateList);
		return draftSkuConverter.to(skuList);
	}

	@Override
	public Integer deleteByProductId(Long productId) {
		return draftSkuDao.deleteByProductId(productId);
	}

	@Override
	public List<SkuBO> getByProductId(Long productId) {
		List<SkuBO> skuBOS = draftSkuConverter.convert2SkuBO(draftSkuDao.getSkuByProductId(productId));

		List<Long> skuIds = skuBOS.stream().map(SkuBO::getId).collect(Collectors.toList());
		List<Sku> originalSkus = skuDao.queryAllByIds(skuIds);
		Map<Long, Sku> skuMap = originalSkus.stream().collect(Collectors.toMap(Sku::getId, e -> e));
		for (SkuBO skuBO : skuBOS) {
			if (skuMap.containsKey(skuBO.getId())) {
				Sku sku = skuMap.get(skuBO.getId());
				skuBO.setActualStocks(sku.getActualStocks());
			}
		}

		return skuBOS;
	}

	@Override
	public List<DraftSku> getSkuByProductId(Long productId) {
		return draftSkuDao.getSkuByProductId(productId);
	}
}
