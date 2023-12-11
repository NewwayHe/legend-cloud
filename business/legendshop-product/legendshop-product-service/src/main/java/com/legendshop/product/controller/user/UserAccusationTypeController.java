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
import com.legendshop.product.dto.AccusationTypeDTO;
import com.legendshop.product.service.AccusationTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author legendshop
 */
@RestController
@RequestMapping(value = "/p/accusationType", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserAccusationTypeController {

	@Autowired
	private AccusationTypeService accusationTypeService;

	/**
	 * 查询所有上线的举报类型
	 *
	 * @return
	 */
	@GetMapping("/all")
	public R<List<AccusationTypeDTO>> queryAllOnLine() {
		return R.ok(accusationTypeService.queryAllOnLine());
	}

}
