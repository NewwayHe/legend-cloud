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
import com.legendshop.product.dto.TransFeeCalProductDetailDTO;
import com.legendshop.product.service.TransFeeCalculateService;
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
 * @author legendshop
 */
@Tag(name = "运费模板")
@RestController
@RequestMapping(value = "/p/transport", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserTransportController {

	@Autowired
	private TransFeeCalculateService transFeeCalculateService;

	@Operation(summary = "【用户】商品区域限售，运费返回")
	@PostMapping("/calculate/fee")
	public R calculateTransFee(@Valid @RequestBody TransFeeCalProductDetailDTO transFeeCalculateDTO) {
		return transFeeCalculateService.calTransFeeForUser(transFeeCalculateDTO);
	}

}
