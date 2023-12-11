/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.service;

import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.basic.dto.SocialDetailsDTO;
import com.legendshop.basic.query.SocialDetailsQuery;

/**
 * 社交登录接口
 *
 * @author legendshop
 */
public interface SocialDetailsService {

	PageSupport<SocialDetailsDTO> page(SocialDetailsQuery socialDetailsQuery);
}
