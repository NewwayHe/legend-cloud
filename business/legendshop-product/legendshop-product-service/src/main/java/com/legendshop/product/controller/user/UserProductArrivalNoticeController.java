/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.controller.user;

import com.legendshop.common.core.constant.R;
import com.legendshop.common.security.utils.SecurityUtils;
import com.legendshop.product.dto.ProductArrivalNoticeDTO;
import com.legendshop.product.service.ProductArrivalNoticeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 商品到货通知表(ProductArrivalNotice)表控制层
 *
 * @author legendshop
 * @since 2020-08-20 15:25:45
 */
@Tag(name = "商品到货通知")
@RestController
@RequestMapping(value = "/p/productArrivalNotice", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserProductArrivalNoticeController {

	/**
	 * 商品到货通知表(ProductArrivalNotice)服务对象
	 */
	@Autowired
	private ProductArrivalNoticeService productArrivalNoticeService;


	/**
	 * 用户添加到货通知
	 *
	 * @param productArrivalNoticeDTO
	 * @return
	 */
	@Operation(summary = "【用户】添加到货通知")
	@PostMapping("/save")
	public R<Long> save(@Valid @RequestBody ProductArrivalNoticeDTO productArrivalNoticeDTO) {
		String mobile = SecurityUtils.getUser().getMobile();
		productArrivalNoticeDTO.setUserId(SecurityUtils.getUserId());
		productArrivalNoticeDTO.setMobilePhone(mobile);
		return productArrivalNoticeService.saveProdArriInfo(productArrivalNoticeDTO);
	}

}
