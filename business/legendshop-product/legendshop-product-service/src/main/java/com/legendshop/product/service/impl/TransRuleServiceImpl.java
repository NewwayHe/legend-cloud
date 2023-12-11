/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.service.impl;

import cn.hutool.core.date.DateUtil;
import com.legendshop.common.core.service.impl.BaseServiceImpl;
import com.legendshop.product.dao.TransRuleDao;
import com.legendshop.product.dto.TransRuleDTO;
import com.legendshop.product.entity.TransRule;
import com.legendshop.product.service.TransRuleService;
import com.legendshop.product.service.convert.TransRuleConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 店铺运费规则(TransRule)表服务实现类
 *
 * @author legendshop
 * @since 2020-09-08 17:00:50
 */
@Service
public class TransRuleServiceImpl extends BaseServiceImpl<TransRuleDTO, TransRuleDao, TransRuleConverter> implements TransRuleService {

	@Autowired
	private TransRuleDao transRuleDao;

	@Autowired
	private TransRuleConverter transRuleConverter;

	@Override
	public TransRuleDTO getByShopId(Long shopId) {
		TransRule rule = transRuleDao.getByShopId(shopId);
		if (rule == null) {
			rule = new TransRule();
			rule.setShopId(shopId);
			rule.setType(1);
			rule.setRecDate(DateUtil.date());
			Long id = transRuleDao.save(rule);
			rule.setId(id);
		}
		return transRuleConverter.to(rule);
	}
}
