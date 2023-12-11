/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.api;

import com.legendshop.basic.dto.AttachmentFileFolderDTO;
import com.legendshop.basic.entity.AttachmentFileFolder;
import com.legendshop.basic.service.AttachmentFileFolderService;
import com.legendshop.common.core.constant.R;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * 图片处理
 *
 * @author legendshop
 */
@Slf4j
@RestController
@AllArgsConstructor
public class ShopFileApiImpl implements ShopFileApi {

	private final AttachmentFileFolderService attachmentFileFolderService;

	@Operation(summary = "【商家】获取所有商家商铺id")
	@Override
	public R<List<Long>> getIds() {
		return R.ok(attachmentFileFolderService.queryIds());
	}

	@Operation(summary = "【商家】判断是否有该商家文件夹")
	@Override
	public R<Long> getByShopId(@RequestParam(value = "id") Long id) {
		return R.ok(attachmentFileFolderService.getByShopId(id));
	}

	@Override
	public R<Long> updateFileName(@RequestParam(value = "fileName") String fileName, @RequestParam(value = "shopId") Long shopId) {
		AttachmentFileFolder byShopIdFirst = attachmentFileFolderService.getByShopIdFirst(shopId);
		byShopIdFirst.setFileName(fileName);
		byShopIdFirst.setUpdateTime(new Date());
		attachmentFileFolderService.update(byShopIdFirst);
		return null;
	}

	@Operation(summary = "【商家】初始化商家根目录")
	@Override
	public R save(@RequestBody AttachmentFileFolderDTO attachmentFileFolderDTO) {
		return R.ok(attachmentFileFolderService.save(attachmentFileFolderDTO));
	}

}
