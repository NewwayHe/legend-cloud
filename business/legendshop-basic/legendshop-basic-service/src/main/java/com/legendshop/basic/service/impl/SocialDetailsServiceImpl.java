/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.service.impl;

import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.basic.dao.SocialDetailsDao;
import com.legendshop.basic.dto.SocialDetailsDTO;
import com.legendshop.basic.query.SocialDetailsQuery;
import com.legendshop.basic.service.SocialDetailsService;
import com.legendshop.basic.service.convert.SociaDetailsConverter;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 社交登录配置接口
 *
 * @author legendshop
 */
@Service
@AllArgsConstructor
public class SocialDetailsServiceImpl implements SocialDetailsService {

	private final SocialDetailsDao socialDetailsDao;

	private final SociaDetailsConverter converter;

	@Override
	public PageSupport<SocialDetailsDTO> page(SocialDetailsQuery socialDetailsQuery) {
		return converter.page(socialDetailsDao.page(socialDetailsQuery));
	}
}
