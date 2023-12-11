/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.task.job.basic;

import cn.hutool.core.util.ObjectUtil;
import com.legendshop.basic.api.AdminFileApi;
import com.legendshop.basic.api.ShopFileApi;
import com.legendshop.basic.dto.AttachmentFileFolderDTO;
import com.legendshop.common.core.constant.R;
import com.legendshop.shop.api.ShopDetailApi;
import com.legendshop.shop.dto.ShopDetailDTO;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * 初始化商家图片空间定时任务
 *
 * @author legendshop
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AttachmentJob {

	private final ShopFileApi shopFileApi;
	private final AdminFileApi adminFileApi;
	private final ShopDetailApi shopDetailApi;

	@XxlJob("attachmentInit")
	public ReturnT<String> attachmentInit(String param) {

		//获取所有商家对象
		List<ShopDetailDTO> shopDetailDTOList = shopDetailApi.queryAll().getData();

		//初始化平台根目录
		R<Long> adminId = adminFileApi.checkFile();

		//添加对应商家文件夹
		for (ShopDetailDTO shopDetailDTO : shopDetailDTOList) {
			AttachmentFileFolderDTO attachmentFileFolderDTO = new AttachmentFileFolderDTO();
			attachmentFileFolderDTO.setUserType(1);
			attachmentFileFolderDTO.setFileName("商家[" + shopDetailDTO.getShopName() + "]根目录");
			attachmentFileFolderDTO.setCreateTime(new Date());
			attachmentFileFolderDTO.setTypeId(1);
			attachmentFileFolderDTO.setParentId(adminId.getData());
			attachmentFileFolderDTO.setShopId(shopDetailDTO.getId());

			//判断商家文件夹是否存在
			if (ObjectUtil.isNull(shopFileApi.getByShopId(shopDetailDTO.getId()).getData())) {
				shopFileApi.save(attachmentFileFolderDTO);
			}
		}
		return ReturnT.SUCCESS;
	}
}
