/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.oss.endpoint;

import com.legendshop.common.oss.http.OssService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 扩展图片服务器的controller
 *
 * @author legendshop
 */
@RestController
@AllArgsConstructor
@RequestMapping("/oss")
public class OssEndpoint {

	private final OssService ossService;

}
