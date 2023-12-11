/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.activity.handle;

import com.legendshop.product.bo.SkuBO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 营销活动sku校验器
 * 用于校验同一个时间段sku有且仅能参与一个活动【秒杀、团购、拼团】
 *
 * @author legendshop
 */
@RequiredArgsConstructor
@Component
public class ActivitySkuValidators {


	private Map<Long, List<SkuBO>> ListConvertToMap(List<SkuBO> skuBOS, List<SkuBO> illegalSku) {
		List<SkuBO> newSkuList = new ArrayList<>();
		Map<Long, List<SkuBO>> collect = illegalSku.stream().collect(Collectors.groupingBy(SkuBO::getId));
		for (SkuBO skuBO : skuBOS) {
			if (!collect.containsKey(skuBO.getId())) {
				newSkuList.add(skuBO);
			}
		}
		illegalSku.addAll(newSkuList);
		return skuBOS.stream().collect(Collectors.groupingBy(SkuBO::getId));
	}

}
