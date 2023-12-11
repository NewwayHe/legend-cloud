/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.service;

import com.legendshop.basic.dto.ExceptionLogDTO;

/**
 * (ExceptionLog)表服务接口
 *
 * @author legendshop
 * @since 2020-09-25 10:20:15
 */
public interface ExceptionLogService {
	Long save(ExceptionLogDTO dto);
}
