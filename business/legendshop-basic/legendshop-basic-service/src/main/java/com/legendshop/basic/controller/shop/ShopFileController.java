/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.controller.shop;

import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.basic.dto.*;
import com.legendshop.basic.entity.AttachmentFileFolder;
import com.legendshop.basic.enums.FileSourceEnum;
import com.legendshop.basic.query.AttachmentFileFolderQuery;
import com.legendshop.basic.service.AttachmentFileFolderService;
import com.legendshop.basic.service.AttachmentService;
import com.legendshop.basic.service.convert.AttachmentFileFolderConverter;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.security.utils.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 图片处理
 *
 * @author legendshop
 */
@Slf4j
@RestController
@AllArgsConstructor
@Tag(name = "图片上传")
@RequestMapping(value = "/s/file", produces = MediaType.APPLICATION_JSON_VALUE)
public class ShopFileController {

	private final AttachmentService attachmentService;

	private final AttachmentFileFolderService attachmentFileFolderService;

	private final AttachmentFileFolderConverter attachmentFileFolderConverter;

	/**
	 * 上传文件 文件名采用uuid,避免原始文件名中带"-"符号导致下载的时候解析出现异
	 *
	 * @param file 图片
	 */
	@PostMapping("/uploadNew")
	@PreAuthorize("@pms.hasPermission('s_basic_uploadNew')")
	@Operation(summary = "【商家】文件上传（预上传）.新 s_basic_uploadNew")
	public R<Map<String, String>> uploadNew(@RequestParam("file") MultipartFile file, @RequestParam(value = "scope", defaultValue = "PUBLIC") String scope, @RequestParam(required = false) Long fileFolderId) throws IOException {
		UpLoadFileNewDTO upLoadFileNewDTO = new UpLoadFileNewDTO();
		upLoadFileNewDTO.setFile(file);
		upLoadFileNewDTO.setFileFolderId(fileFolderId);
		upLoadFileNewDTO.setShopId(SecurityUtils.getShopUser().getShopId());
		upLoadFileNewDTO.setUserId(SecurityUtils.getShopUser().getUserId());
		upLoadFileNewDTO.setFileSource(FileSourceEnum.SHOP.getValue());
		upLoadFileNewDTO.setScope(scope);
		return this.attachmentService.uploadFileNew(upLoadFileNewDTO);
	}


	/**
	 * 上传文件 文件名采用uuid,避免原始文件名中带"-"符号导致下载的时候解析出现异
	 *
	 * @param file 图片及视频
	 */
	@PostMapping("/uploadFileVideoNew")
	@Operation(summary = "【商家】文件上传（预上传）.新 s_basic_uploadNew")
	public R<Map<String, String>> uploadFileVideoNew(@RequestParam("file") MultipartFile file, @RequestParam(value = "scope", defaultValue = "PUBLIC") String scope, @RequestParam(required = false) Long fileFolderId, @RequestParam(value = "uploadType") Boolean uploadType, @RequestParam(value = "attachmentId") Long attachmentId) throws IOException {
		UpLoadVideoFileNewDTO upLoadFileNewDTO = new UpLoadVideoFileNewDTO();
		upLoadFileNewDTO.setFile(file);
		upLoadFileNewDTO.setFileFolderId(fileFolderId);
		upLoadFileNewDTO.setShopId(SecurityUtils.getShopUser().getShopId());
		upLoadFileNewDTO.setUserId(SecurityUtils.getShopUser().getUserId());
		upLoadFileNewDTO.setFileSource(FileSourceEnum.SHOP.getValue());
		upLoadFileNewDTO.setUploadType(uploadType);
		upLoadFileNewDTO.setAttachmentId(attachmentId);
		upLoadFileNewDTO.setScope(scope);
		return this.attachmentService.uploadFileVideoNew(upLoadFileNewDTO);
	}


	@PostMapping(value = "/saveFileFolder")
	@PreAuthorize("@pms.hasPermission('s_basic_saveNameByFileId')")
	@Operation(summary = "【商家】添加新文件夹 s_basic_saveNameByFileId")
	public R<Long> saveNameByFileId(@RequestBody AttachmentFileFolder attachmentFileFolder) {
		attachmentFileFolder.setShopId(SecurityUtils.getShopUser().getShopId());
		attachmentFileFolder.setUserType(1);
		return attachmentFileFolderService.saveFileFolder(attachmentFileFolder);
	}

	@GetMapping(value = "/updateFileFolderName")
	@PreAuthorize("@pms.hasPermission('s_basic_updateFileFolderName')")
	@Operation(summary = "【商家】更新文件夹/文件名字 s_basic_updateFileFolderName")
	public R updateFileFolderName(@RequestParam String fileName, @RequestParam Long id, @RequestParam(required = false) String ext) {
		if (ext != null) {
			return attachmentService.updateName(fileName, id);
		}
		return attachmentFileFolderService.updateFileFolderName(fileName, id);
	}

	@PutMapping("/updateFileFolderList")
	@PreAuthorize("@pms.hasPermission('s_basic_updateAttPathByFileFolderId')")
	@Operation(summary = "【商家】批量更新文件路径 s_basic_updateAttPathByFileFolderId")
	public R updateAttPathByFileFolderId(@RequestBody FileFolderUpdateDTO fileFolderUpdateDTO) {
		/*attachmentFileFolder.setShopId(SecurityUtils.getShopUser().getShopId());*/
		attachmentFileFolderService.updateAttPathByFileFolderId(fileFolderUpdateDTO);
		return R.ok();
	}

	@DeleteMapping(value = "/delectFileByIdList")
	@PreAuthorize("@pms.hasPermission('s_basic_delectFileByIdList')")
	@Operation(summary = "【商家】批量删除文件或者文件夹 s_basic_delectFileByIdList")
	public R delectFileByIdList(@Validated @RequestBody List<AttFileIdListDTO> attFileIdListDTOList) {
		return attachmentFileFolderService.delFileList(attFileIdListDTOList, SecurityUtils.getShopUser());
	}

	@GetMapping(value = "/queryPageByShop")
	@PreAuthorize("@pms.hasPermission('s_basic_queryPageByShop')")
	@Operation(summary = "【商家】分页查询文件夹或者文件 s_basic_queryPageByShop")
	public R<PageSupport<AttachmentFileFolderDTO>> queryPageByShop(AttachmentFileFolderQuery attachmentFileFolderQuery) {
		//查询文件夹表计算总数
		//获取当前已查询的数据量（当前页 * 每页大小）
		//判断文件夹总数是否大于已查询数据量
		//不是，则分页查询附件表   是，则继续查询文件夹表
		attachmentFileFolderQuery.setShopId(SecurityUtils.getShopUser().getShopId());
		attachmentFileFolderQuery.setUserType(1);
		return attachmentFileFolderService.queryPage(attachmentFileFolderQuery);
	}

	@PutMapping(value = "/updateManagedFlage")
	@PreAuthorize("@pms.hasPermission('s_basic_updateManagedFlage')")
	@Operation(summary = "【商家】文件上传(可以上传图片或视频)（修改图片状态为图片管理可用） s_basic_updateManagedFlage")
	public R updateManagedFlage(@RequestBody UpdateAttachmentFlagDTO updateAttachmentFlagDTO) {
		updateAttachmentFlagDTO.setUserId(SecurityUtils.getUserId());
		return attachmentService.updateManagedFlage(updateAttachmentFlagDTO);
	}

	@GetMapping(value = "/getIdList")
	@PreAuthorize("@pms.hasPermission('s_basic_getIdList')")
	@Operation(summary = "【商家】路径导航 s_basic_getIdList")
	public R getIdList(@RequestParam(required = false) Long id) {
		Long shopId = SecurityUtils.getShopUser().getShopId();
		return R.ok(attachmentFileFolderService.getIdList(id, shopId));
	}

	@PostMapping(value = "/save")
	@Operation(summary = "【商家】初始化商家根目录")
	public R save(@RequestBody AttachmentFileFolderDTO attachmentFileFolderDTO) {
		return R.ok(attachmentFileFolderService.save(attachmentFileFolderDTO));
	}

	@GetMapping(value = "/getIds")
	@Operation(summary = "【商家】获取所有商家商铺id")
	public R<List<Long>> getIds() {
		return R.ok(attachmentFileFolderService.queryIds());
	}

	@GetMapping(value = "/getByShopId")
	@Operation(summary = "【商家】判断是否有该商家文件夹")
	public R<Long> getByShopId(@RequestParam(value = "id") Long id) {
		return R.ok(attachmentFileFolderService.getByShopId(id));
	}

	@GetMapping(value = "/updateFileName")
	public R<Long> updateFileName(@RequestParam(value = "fileName") String fileName, @RequestParam(value = "shopId") Long shopId) {
		AttachmentFileFolder byShopIdFirst = attachmentFileFolderService.getByShopIdFirst(shopId);
		byShopIdFirst.setFileName(fileName);
		byShopIdFirst.setUpdateTime(new Date());
		attachmentFileFolderService.update(byShopIdFirst);
		return null;
	}

	@GetMapping(value = "/queryFileFolderList")
	@PreAuthorize("@pms.hasPermission('s_basic_queryFileFolderList')")
	@Operation(summary = "【商家】修改文件路径展示列表 s_basic_queryFileFolderList")
	public R queryFileFolderList(@RequestParam(required = false) Long id) {
		Long shopId = SecurityUtils.getShopUser().getShopId();
		return R.ok(attachmentFileFolderService.queryFileFolderList(id, shopId));
	}


}
