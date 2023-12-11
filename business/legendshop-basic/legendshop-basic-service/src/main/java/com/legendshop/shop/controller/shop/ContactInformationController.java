/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.controller.shop;

import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.enums.UserTypeEnum;
import com.legendshop.common.log.annotation.SystemLog;
import com.legendshop.common.security.utils.SecurityUtils;
import com.legendshop.shop.dto.ContactInformationDTO;
import com.legendshop.shop.service.ContactInformationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * 微信联系方式存储表(ContactInformation)表控制层
 *
 * @author legendshop
 * @since 2021-12-27 09:30:28
 */
@Tag(name = "商家、平台微信联系方式存储表管理")
@RestController
@RequestMapping(value = "/contactInformation", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class ContactInformationController {

	/**
	 * 商家、平台微信联系方式(ContactInformation)服务对象
	 */
	private final ContactInformationService contactInformationService;


	/**
	 * 根据店铺id查询商家、平台微信联系方式存储表
	 *
	 * @return
	 */
	@GetMapping("/info")
	@Operation(summary = "【用户】根据店铺id查询商家、平台微信联系方式存储表")
	public R<ContactInformationDTO> getByShopId(Long shopId) {
		return R.ok(contactInformationService.getByShopId(shopId));
	}


	/**
	 * 根据当前登录用户id查询商家、平台微信联系方式存储表
	 *
	 * @return
	 */
	@GetMapping("/infoByShopId")
	@Operation(summary = "【商家，平台】根据当前登录用户id查询商家或平台微信联系方式存储表")
	public R<ContactInformationDTO> getById() {
		Long shopId = 0L;
		if (SecurityUtils.getBaseUser().getUserType().equals(UserTypeEnum.SHOP.getLoginType())) {
			shopId = SecurityUtils.getShopUser().getShopId();
		}
		return R.ok(contactInformationService.getByShopId(shopId));
	}

	/**
	 * 保存商家微信联系方式存储表
	 *
	 * @param contactInformationDTO
	 * @return
	 */
	@PostMapping("/s/save")
	@SystemLog("保存商家微信联系方式存储表")
	@Operation(summary = "【商家】保存商家微信联系方式")
	public R save(@Valid @RequestBody ContactInformationDTO contactInformationDTO) {
		return contactInformationService.save(contactInformationDTO);
	}

	/**
	 * 保存平台微信联系方式存储表
	 *
	 * @param contactInformationDTO
	 * @return
	 */
	@PostMapping("/admin/save")
	@SystemLog("保存平台微信联系方式存储表")
	@Operation(summary = "【平台】保存平台微信联系方式")
	public R adminSave(@Valid @RequestBody ContactInformationDTO contactInformationDTO) {
		return contactInformationService.save(contactInformationDTO);
	}
}
