/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.service.impl;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.expetion.BusinessException;
import com.legendshop.product.bo.SkuBO;
import com.legendshop.product.bo.TransportBO;
import com.legendshop.product.dto.*;
import com.legendshop.product.enums.TransCalFeeResultEnum;
import com.legendshop.product.enums.TransRuleTypeEnum;
import com.legendshop.product.service.*;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 运费计算
 *
 * @author legendshop
 */
@Service
public class TransFeeCalculateServiceImpl implements TransFeeCalculateService {

	@Autowired
	private TransRuleService transRuleService;

	@Autowired
	private TransportService transportService;

	@Autowired
	private ProductService productService;

	@Autowired
	private SkuService skuService;

	@Resource(name = "transportFreePostageHandler")
	private AbstractTransportHandler transportHandler;


	@Override
	public R calTransFee(TransFeeCalculateDTO calculateDTO) {
		if (calculateDTO == null) {
			return R.fail(TransCalFeeResultEnum.NULL_DTO.getCode(), TransCalFeeResultEnum.NULL_DTO.getMsg());
		}
		BigDecimal freight = BigDecimal.ZERO;
		//获取店铺设置的规则
		TransRuleDTO ruleDTO = transRuleService.getByShopId(calculateDTO.getShopId());
		//叠加 or 最高
		Integer type = ruleDTO.getType();
		List<TransFeeCalProductDTO> calProductDTOS = calculateDTO.getCalProductDTOS();
		//根据模板id将商品进行分组，再根据运费模板的不同分别进行计算
		Map<Long, List<TransFeeCalProductDTO>> map = calProductDTOS.stream().collect(Collectors.groupingBy(TransFeeCalProductDTO::getTransId));
		//Map<Long, BigDecimal> feeMap = new HashMap<>(16);
		List<BigDecimal> feeList = new ArrayList<>();
		for (Map.Entry<Long, List<TransFeeCalProductDTO>> entry : map.entrySet()) {
			Long transId = entry.getKey();
			List<TransFeeCalProductDTO> calProductDTOList = entry.getValue();
			TransportBO transport = transportService.getById(transId);
			if (null == transport) {
				return R.fail(TransCalFeeResultEnum.NOT_EXIT_TRANS_PORT.getCode(), TransCalFeeResultEnum.NOT_EXIT_TRANS_PORT.getMsg());
			}
			if (ObjectUtil.isNull(calculateDTO.getCityId())) {
				return R.fail(TransCalFeeResultEnum.NULL_CITY.getCode(), TransCalFeeResultEnum.NULL_CITY.getMsg());
			}
			//TransFeeCalculateResultBO transFeeCalculateResultBO = new TransFeeCalculateResultBO();
			//包邮处理类为责任链的首节点
			R r = transportHandler.calculateTransFee(calProductDTOList, transport, calculateDTO.getCityId());
			if (r.getCode() == 1) {
				BigDecimal transFee = (BigDecimal) r.getData();
				freight = freight.add(transFee);
				feeList.add(transFee);
			} else {
				return r;
			}
		}
		BigDecimal max = feeList.stream().max(BigDecimal::compareTo).get();
		if (type.equals(TransRuleTypeEnum.CAL_ADD.value())) {
			return R.ok(freight);
		}
		return R.ok(max);
	}


	@Override
	public R calTransFeeForUser(TransFeeCalProductDetailDTO calProductDetailDTO) {
		TransFeeCalculateDTO calculateDTO = new TransFeeCalculateDTO();
		List<TransFeeCalProductDTO> calProductDTOS = new ArrayList<>();
		TransFeeCalProductDTO calProductDTO = new TransFeeCalProductDTO();
		SkuBO sku = skuService.getSkuById(calProductDetailDTO.getSkuId());
		if (ObjectUtil.isNull(sku)) {
			throw new BusinessException("未找到该规格！！！");
		}
		calProductDTO.setProductId(sku.getProductId());
		ProductDTO product = productService.getDtoByProductId(sku.getProductId());
		if (ObjectUtil.isNull(product)) {
			throw new BusinessException("未找到该商品！！！");
		}
		calProductDTO.setTransId(product.getTransId());
		calculateDTO.setCityId(calProductDetailDTO.getCityId());
		calculateDTO.setShopId(product.getShopId());
		calProductDTO.setTotalPrice(NumberUtil.mul(sku.getPrice(), calProductDetailDTO.getProductCount()));
		calProductDTO.setTotalCount(calProductDetailDTO.getProductCount());

		BigDecimal volume = NumberUtil.mul(sku.getVolume(), calProductDetailDTO.getProductCount());
		calProductDTO.setTotalVolume(volume.doubleValue());
		BigDecimal weight = NumberUtil.mul(sku.getWeight(), calProductDetailDTO.getProductCount());
		calProductDTO.setTotalWeight(weight.doubleValue());
		calProductDTOS.add(calProductDTO);
		calculateDTO.setCalProductDTOS(calProductDTOS);
		return calTransFee(calculateDTO);
	}
}
