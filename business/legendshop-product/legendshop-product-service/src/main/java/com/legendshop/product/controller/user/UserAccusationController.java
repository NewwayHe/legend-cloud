/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.controller.user;

import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.basic.api.WxApi;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.security.dto.OrdinaryUserDetail;
import com.legendshop.common.security.utils.SecurityUtils;
import com.legendshop.product.bo.AccusationBO;
import com.legendshop.product.dto.AccusationDTO;
import com.legendshop.product.dto.AccusationUserTypeDTO;
import com.legendshop.product.enums.AccusationEnum;
import com.legendshop.product.enums.AccusationUserTypeEnum;
import com.legendshop.product.query.AccusationQuery;
import com.legendshop.product.service.AccusationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * @author legendshop
 */
@Tag(name = "商品举报")
@RestController
@RequestMapping(value = "/p/accusation", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class UserAccusationController {

	private final AccusationService accusationService;
	private final WxApi wxApi;

	/**
	 * 举报商品  保存
	 *
	 * @param accusationDTO
	 * @return
	 */
	@Operation(summary = "【用户】举报商品  保存")
	@PostMapping
	public R save(@Valid @RequestBody AccusationDTO accusationDTO) {
		//举报内容敏感字审核
		R<Void> result = wxApi.checkContent(accusationDTO.getContent());
		if (!result.success()) {
			return result;
		}

		OrdinaryUserDetail userDetail = SecurityUtils.getUser();

		AccusationUserTypeDTO userTypeDTO = new AccusationUserTypeDTO();

		userTypeDTO.setUserId(SecurityUtils.getUserId());
		userTypeDTO.setUserName(userDetail.getNickname());
		userTypeDTO.setUserType(AccusationUserTypeEnum.USER.value());

		accusationDTO.setStatus(AccusationEnum.STATUS_NO.value());
		accusationDTO.setUserDelStatus(AccusationEnum.DEL_STATUS_NO.value());
		return accusationService.saveAccusation(accusationDTO, userTypeDTO);
	}


	/**
	 * 用户端查看我的举报
	 *
	 * @param query
	 * @return
	 */
	@Operation(summary = "【用户】用户端查看我的举报")
	@GetMapping("/my/page")
	public R<PageSupport<AccusationBO>> queryVitListPage(AccusationQuery query) {
		Long userId = SecurityUtils.getUserId();
		query.setUserId(userId);
		PageSupport<AccusationBO> pageSupport = accusationService.queryMyAccusation(query);
		return R.ok(pageSupport);
	}


}
