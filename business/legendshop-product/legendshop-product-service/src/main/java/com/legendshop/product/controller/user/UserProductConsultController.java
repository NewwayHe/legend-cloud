/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.controller.user;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.basic.api.WxApi;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.security.utils.SecurityUtils;
import com.legendshop.common.security.utils.UserTokenUtil;
import com.legendshop.product.dto.ProductConsultDTO;
import com.legendshop.product.query.ProductConsultQuery;
import com.legendshop.product.service.ProductConsultService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 用户端商品咨询控制器
 *
 * @author legendshop
 */
@Tag(name = "用户端商品咨询")
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/p/product/consult", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserProductConsultController {

	final WxApi wxApi;
	final UserTokenUtil userTokenUtil;
	final ProductConsultService productConsultService;


	@GetMapping("/addConsult")
	@Operation(summary = "【用户】新增商品咨询")
	public R<Long> addConsult(@Valid ProductConsultDTO productConsultDTO) {
		//审核敏感字(内容)
		R<Void> result = wxApi.checkContent(productConsultDTO.getContent());
		if (!result.success()) {
			return R.fail(result.getMsg());
		}
		//获取登录用户Id
		Long userId = SecurityUtils.getUserId();
		if (ObjectUtil.isNotNull(userId)) {
			productConsultDTO.setAskUserId(userId);
			return productConsultService.addConsult(productConsultDTO);
		}
		return R.fail("用户没有登陆");
	}


	@GetMapping("/page")
	@Operation(summary = "【用户】商品咨询列表查询（已回复）")
	public R<PageSupport<ProductConsultDTO>> queryUserProductConsultList(ProductConsultQuery query, HttpServletRequest request) {
		PageSupport<ProductConsultDTO> favorProd = productConsultService.getUserProductConsultPage(query);
		return R.ok(favorProd);
	}

	@GetMapping("/userCenter")
	@Operation(summary = "【用户】个人中心商品咨询")
	public R<PageSupport<ProductConsultDTO>> queryUserCenter(ProductConsultQuery query) {
		query.setAskUserId(SecurityUtils.getUserId());
		PageSupport<ProductConsultDTO> favorProd = productConsultService.getProductConsultCenterPage(query);
		if (CollUtil.isNotEmpty(favorProd.getResultList())) {
			List<ProductConsultDTO> resultList = favorProd.getResultList();
			for (ProductConsultDTO productConsultDTO : resultList) {
				//手机号码脱敏
				String desensitization = getDesensitization(productConsultDTO.getAskUserIphone());
				productConsultDTO.setAskUserIphone(desensitization);
			}
		}
		return R.ok(favorProd);
	}

	/**
	 * 手机号码脱敏
	 *
	 * @param mobile
	 * @return
	 */
	private String getDesensitization(String mobile) {
		if (StrUtil.isBlank(mobile)) {
			return "";
		}
		return mobile.substring(0, 3) + "****" + mobile.substring(7, 11);
	}

}
