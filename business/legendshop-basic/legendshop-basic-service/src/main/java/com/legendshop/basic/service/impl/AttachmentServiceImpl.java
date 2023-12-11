/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileTypeUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.*;
import com.legendshop.basic.config.PropertiesUtil;
import com.legendshop.basic.dao.AttachmentDao;
import com.legendshop.basic.dao.AttachmentFileFolderDao;
import com.legendshop.basic.dao.AttachmentFolderDao;
import com.legendshop.basic.dto.UpLoadFileNewDTO;
import com.legendshop.basic.dto.UpLoadVideoFileNewDTO;
import com.legendshop.basic.dto.UpdateAttachmentFlagDTO;
import com.legendshop.basic.entity.Attachment;
import com.legendshop.basic.entity.AttachmentFolder;
import com.legendshop.basic.enums.FileScopeEnum;
import com.legendshop.basic.enums.FileSourceEnum;
import com.legendshop.basic.properties.AllowedFileTypesProperties;
import com.legendshop.basic.service.AttachmentService;
import com.legendshop.common.core.constant.CommonConstants;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.expetion.BusinessException;
import com.legendshop.common.oss.http.OssService;
import com.legendshop.common.oss.properties.OssProperties;
import com.legendshop.common.security.utils.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 附件服务.
 *
 * @author legendshop
 */
@Service
@Slf4j
public class AttachmentServiceImpl implements AttachmentService {

	@Autowired
	private OssService ossService;

	@Autowired
	private AttachmentDao attachmentDao;

	@Autowired
	private OssProperties ossProperties;

	@Autowired
	private AllowedFileTypesProperties allowedFileTypesProperties;

	@Autowired
	private WxServiceImpl wxService;

	@Autowired
	private AttachmentFolderDao attachmentFolderDao;

	@Autowired
	private AttachmentFileFolderDao attachmentFileFolderDao;
	@Autowired
	private PropertiesUtil propertiesUtil;

	@Autowired
	private ThreadPoolTaskExecutor threadPool;

	@Override
	public R<Map<String, String>> uploadFile(Long userId, MultipartFile file, String fileSource, String scope, Long fileType) throws IOException {
		// 文件校验
		String fileName = file.getOriginalFilename();
		if (StringUtils.isBlank(fileName)) {
			return R.fail("错误的文件类型！");
		}
		String fileContentType;
		String fileSuffixType;
		try {
			fileContentType = FileTypeUtil.getType(file.getInputStream());
			fileSuffixType = fileName.substring(fileName.lastIndexOf(".") + 1);
			log.info("上件图片中，图片类型为：{}", fileContentType);
		} catch (IOException e) {
			log.error("上传失败", e);
			return R.fail("文件类型错误！");
		}
		//视频格式限制
		//视频格式是否满足 mp4  webm ogg
		// fileType 0:图片 1:视频
//		if (fileType==1){
//			if ("mp4".equalsIgnoreCase(fileSuffixType)?Boolean.FALSE:("webm".equalsIgnoreCase(fileSuffixType)?Boolean.FALSE:("ogg".equalsIgnoreCase(fileSuffixType)?Boolean.FALSE:Boolean.TRUE))){
//				throw new BusinessException("不支持"+fileSuffixType+"视频格式！！");
//			}
//		}
		if (fileType == 1) {
			if (!"mp4".equalsIgnoreCase(fileSuffixType)) {
				throw new BusinessException("不支持" + fileSuffixType + "视频格式！！");
			}
		}


		if (CollectionUtils.isEmpty(this.allowedFileTypesProperties.getAllowedFileTypes())) {
			return R.fail("禁止任何文件类型上传！");
		}
		if (null == fileContentType) {
			fileContentType = "";
			// TODO mp4文件无法识别，只能先依照后缀进行保存
			if (StrUtil.isNotBlank(fileSuffixType)) {
				log.info("当前文件类型无法识别，使用文件名后缀作为文件类型，{}", fileSuffixType);
				fileContentType = fileSuffixType;
			}
		}
		String s = fileContentType.toUpperCase();
		log.info("文件类型{}", s);
		if (!this.allowedFileTypesProperties.getAllowedFileTypes().contains(fileContentType.toUpperCase()) &&
				!this.allowedFileTypesProperties.getAllowedFileTypes().contains(fileSuffixType.toUpperCase())) {
			return R.fail("禁止的文件类型！");
		}
		// 敏感图片审核
		R<Void> result = wxService.imgSecCheck(file);
		if (!result.success()) {
			return R.fail(result.getMsg());
		}

		// 图片上传
		String bucketName = this.ossProperties.getBucketName();
		if (FileScopeEnum.PRIVATE.equals(FileScopeEnum.valueOf(scope.toUpperCase()))) {
			bucketName = this.ossProperties.getPrivateBucketName();
		}
		fileName = IdUtil.simpleUUID() + StrUtil.DOT + fileContentType;
		Map<String, String> resultMap = new HashMap<>(4);
		resultMap.put("fileName", fileName);
		String dataFormat = DateUtil.format(DateUtil.date(), "yyyy/MM/dd/");
		String url = dataFormat + fileName;
		resultMap.put("bucketName", bucketName);
		resultMap.put("url", url);
		try {
			ossService.putObject(bucketName, url, file.getInputStream());
			saveAttachment(userId, file, url, fileSource, scope, null);
		} catch (Exception e) {
			log.error("上传失败", e);
			return R.fail(e.getLocalizedMessage());
		}

		return R.ok(resultMap);
	}

	@Override
	public InputStream download(String fileName, String scope) {
		String bucketName = this.ossProperties.getBucketName();
		if (FileScopeEnum.PRIVATE.equals(FileScopeEnum.valueOf(scope))) {
			bucketName = this.ossProperties.getPrivateBucketName();
		}
		return this.ossService.getObject(bucketName, fileName);
	}

	@Override
	public R<Map<String, String>> uploadFileNew(UpLoadFileNewDTO upLoadFileNewDTO) throws IOException {
		MultipartFile file = upLoadFileNewDTO.getFile();
		Long userId = upLoadFileNewDTO.getUserId();
		String fileSource = upLoadFileNewDTO.getFileSource();
		String scope = upLoadFileNewDTO.getScope();
		Long shopId = upLoadFileNewDTO.getShopId();

		// 文件校验
		String fileName = file.getOriginalFilename();
		if (StringUtils.isBlank(fileName)) {
			return R.fail("错误的文件类型！");
		}
		String fileContentType;
		String fileSuffixType;
		try {
			fileContentType = FileTypeUtil.getType(file.getInputStream());
			fileSuffixType = fileName.substring(fileName.lastIndexOf(".") + 1);
		} catch (IOException e) {
			log.error("上传失败", e);
			return R.fail("文件类型错误！");
		}
		if (CollectionUtils.isEmpty(this.allowedFileTypesProperties.getAllowedFileTypes())) {
			return R.fail("禁止任何文件类型上传！");
		}
		if (null == fileContentType) {
			fileContentType = "";
		}
		String dataFormat = DateUtil.format(DateUtil.date(), "yyyy/MM/dd/");
		String url = dataFormat + fileName;
		log.info("文件类型{}", fileContentType.toUpperCase());
		if ("ZIP".equals(fileContentType.toUpperCase())) {
			HashMap<String, String> map = new HashMap<>(4);
			StringBuffer stringBuffer = new StringBuffer();
			String pathList = null;
			File zip = FileUtil.file("d:" + "/" + fileName);
			int fileSize = 0;
			try {
				fileSize = file.getInputStream().available();
				FileUtil.writeFromStream(file.getInputStream(), zip);
				log.info("upLoadFileNewDTO{}", upLoadFileNewDTO);
				R r = unZip(fileName, upLoadFileNewDTO);
			} catch (Exception e) {
				log.error("压缩包上传到本地异常！", e);
			} finally {
				IoUtil.close(file.getInputStream());
			}

			return R.ok();
		}
		if (!this.allowedFileTypesProperties.getAllowedFileTypes().contains(fileContentType.toUpperCase()) ||
				!this.allowedFileTypesProperties.getAllowedFileTypes().contains(fileSuffixType.toUpperCase())) {
			return R.fail("禁止的文件类型！");
		}

		// 敏感图片审核
		R<Void> result = wxService.imgSecCheck(file);
		if (!result.success()) {
			return R.fail(result.getMsg());
		}

		// 图片上传
		String bucketName = this.ossProperties.getBucketName();
		if (FileScopeEnum.PRIVATE.equals(FileScopeEnum.valueOf(scope))) {
			bucketName = this.ossProperties.getPrivateBucketName();
		}
		fileName = IdUtil.simpleUUID() + StrUtil.DOT + FileUtil.extName(file.getOriginalFilename());
		Map<String, String> resultMap = new HashMap<>(4);
		resultMap.put("fileName", fileName);

		resultMap.put("bucketName", bucketName);
		resultMap.put("url", url);
		if (ObjectUtil.isNotEmpty(upLoadFileNewDTO.getPicPath())) {
			log.info("覆盖图片{}", upLoadFileNewDTO.getPicPath());
			resultMap.put("url", upLoadFileNewDTO.getPicPath());
		}
		log.info("resultMap{}", resultMap);
		try {
			ossService.putObject(bucketName, url, file.getInputStream());
			//原来的上传图片
			if (ObjectUtil.isEmpty(upLoadFileNewDTO.getShortPath()) && ObjectUtil.isEmpty(upLoadFileNewDTO.getPicPath())) {
				saveAttachment(userId, file, url, fileSource, scope, null);
			}
			//上传zip解压保存图片短链
			if (ObjectUtil.isNotEmpty(upLoadFileNewDTO.getShortPath())) {
				saveAttachment(userId, file, url, fileSource, scope, upLoadFileNewDTO.getShortPath());
			}

		} catch (Exception e) {
			log.error("上传失败", e);
			return R.fail(e.getLocalizedMessage());
		}
		Long fileFolderId = null;
		//根据url获取图片路径
		Long idByPath = attachmentDao.getIdByPath(url);
		if (ObjectUtil.isNotEmpty(upLoadFileNewDTO.getFileFolderId())) {
			fileFolderId = upLoadFileNewDTO.getFileFolderId();
			String attachmentFolderStr = fileFolderId + fileName;

			//判断是否是
			if (ObjectUtil.isNull(fileFolderId) && ObjectUtil.isNotNull(shopId)) {
				Long byShopId = attachmentFileFolderDao.getByShopId(shopId);
				fileFolderId = byShopId;

			} else if (ObjectUtil.isNull(fileFolderId)) {
				fileFolderId = attachmentFileFolderDao.getIdByParentId(-1L);

			} else if (fileFolderId == -1 && ObjectUtil.isNull(shopId)) {
				fileFolderId = attachmentFileFolderDao.getIdByParentId(fileFolderId);

			}
			//保存数据到中间表
			if (ObjectUtil.isEmpty(upLoadFileNewDTO.getPicPath()) || ObjectUtil.isNotEmpty(upLoadFileNewDTO.getShortPath())) {
				AttachmentFolder attachmentFolder = new AttachmentFolder();
				attachmentFolder.setAttachmentFolder(attachmentFolderStr);
				attachmentFolder.setAttachmentId(idByPath);
				attachmentFolder.setFileFolderId(fileFolderId);
				attachmentFolderDao.save(attachmentFolder);
			}
			if (ObjectUtil.isNotEmpty(upLoadFileNewDTO.getPicPath()) || ObjectUtil.isNotEmpty(upLoadFileNewDTO.getShortPath())) {
				AttachmentFolder attachmentFolder = new AttachmentFolder();
				attachmentFolder.setAttachmentFolder(attachmentFolderStr);
				attachmentFolder.setAttachmentId(idByPath);
				attachmentFolder.setFileFolderId(fileFolderId);
				attachmentFolderDao.save(attachmentFolder);
			}
		}

		String name = file.getOriginalFilename();
		List<Long> idsByName = attachmentDao.getIdsByName(name);
		int count = 0;
		if (ObjectUtil.isNotNull(idsByName)) {
			for (Long attId : idsByName) {
				if (attachmentFolderDao.getFolderIdByAttId(attId) != null && attachmentFolderDao.getFolderIdByAttId(attId).equals(fileFolderId)) {
					count++;
				} else {
					continue;
				}
			}
			if (count > 0) {
				while (attachmentDao.getAttIdByName((file.getOriginalFilename() + "(" + count + ")"), fileFolderId) != null) {
					count++;
				}
				attachmentDao.updateName(file.getOriginalFilename() + "(" + count + ")", idByPath);
			}

		}

		return R.ok(resultMap);

	}


	@Override
	public R updateManagedFlage(UpdateAttachmentFlagDTO updateAttachmentFlagDTO) {
		List<String> filePath = updateAttachmentFlagDTO.getFilePath();
		for (String path : filePath) {
			if (updateAttachmentFlagDTO.getTabType() == 1) {
				attachmentDao.updateManagedFlage(path);
			} else {
				attachmentDao.updateVideoManagedFlage(path);
			}
		}

		return R.ok();
	}

	private R unZip(String fileName, UpLoadFileNewDTO upLoadFileNewDTO) {
		File unzip = null;
		try {
			// 解压
			unzip = ZipUtil.unzip("d:" + "/" + fileName, CharsetUtil.CHARSET_UTF_8);
		} catch (IllegalArgumentException e) {
			try {
				// 编码问题，换一种解压方式
				unzip = ZipUtil.unzip("d:" + "/" + fileName, CharsetUtil.CHARSET_GBK);
			} catch (Exception exception) {
				// 不存在，则返回错误信息 =》 redis


			}
		} catch (Exception e) {
			// 不存在，则返回错误信息 =》 redis

		}
		log.info("解压文件{}", unzip);
		// 直接获取文件夹下所有文件
		List<File> imgFileList = FileUtil.loopFiles(unzip);
		log.info("直接获取文件夹下所有文件{}", imgFileList);
		// 保存上传结果
		int failNum = 0;
		int successNum = 0;

		if (CollUtil.isEmpty(imgFileList)) {
			FileUtil.del(unzip);

		}

		// 文件短链Map
		File finalUnzip = unzip;
		Map<String, String> fileShortMap = imgFileList.stream().collect(Collectors.toMap(File::getPath, e -> {
			return Optional.ofNullable(StrUtil.sub(FileUtil.subPath(finalUnzip.getPath(), e.getPath()), 0, 200)).orElse("").toLowerCase();
		}, (a, b) -> a));

		// 获取短链对应的附件，用于文件覆盖
		int pageSize = 1000;
		int curPageNo = 0;
		int totalPage = new BigDecimal(fileShortMap.values().size()).divide(new BigDecimal(pageSize), 0, RoundingMode.UP).intValue();
		List<Attachment> shortChainList = new ArrayList<>();
		List<String> fileShortPage = new ArrayList<>(fileShortMap.values());
		while (curPageNo < totalPage) {
			List<Attachment> attachmentList = attachmentDao.getAttachmentByShortPath(upLoadFileNewDTO.getUserId(), CollUtil.page(curPageNo, pageSize, fileShortPage));
			if (CollUtil.isNotEmpty(attachmentList)) {
				shortChainList.addAll(attachmentList);
			}
			curPageNo++;
		}
		Map<String, Attachment> shortPathMap = shortChainList.stream().collect(Collectors.toMap(Attachment::getShortPath, e -> e));


		for (File files : imgFileList) {

			try (FileInputStream in = IoUtil.toStream(files)) {
				MockMultipartFile mockMultipartFile = new MockMultipartFile(files.getName(), files.getName(), null, in);

				upLoadFileNewDTO.setFile(mockMultipartFile);
				upLoadFileNewDTO.setShopId(SecurityUtils.getShopUser().getShopId());
				upLoadFileNewDTO.setUserId(SecurityUtils.getShopUser().getUserId());
				upLoadFileNewDTO.setFileSource(FileSourceEnum.SHOP.getValue());
				upLoadFileNewDTO.setFileFolderId(upLoadFileNewDTO.getFileFolderId());
				upLoadFileNewDTO.setScope("PUBLIC");
				log.info("upLoadFileNewDTO{}", upLoadFileNewDTO);
				// 如果数据库存在这个短链，则直接覆盖oss上的图片
				String[] split = fileShortMap.get(files.getPath()).split("\"");
				String path = split[0];
				if (shortPathMap.containsKey(path)) {
					Attachment attachment = shortPathMap.get(fileShortMap.get(files.getPath()));
					upLoadFileNewDTO.setPicPath(attachment.getFilePath());
					log.info("覆盖图片");
					R<Map<String, String>> mapR = this.uploadFileNew(upLoadFileNewDTO);


				} else {
					// 如果不存在，则正常上传图片
					upLoadFileNewDTO.setFile(mockMultipartFile);
					log.info("第一次上传图片,图片短链: {}", fileShortMap.get(files.getPath()));
					upLoadFileNewDTO.setShortPath(path);
					upLoadFileNewDTO.setFileSource(FileSourceEnum.SHOP.getValue());
					R<Map<String, String>> mapR = this.uploadFileNew(upLoadFileNewDTO);

				}

			} catch (Exception e) {
				log.error("图片压缩包解压上传异常！", e);
				continue;
			}


		}


		// 完成后删除缓存数据
		boolean del = FileUtil.del(unzip);
		log.info("完成后删除缓存数据{}", del);
		return R.ok();
	}


	private void fileNew(UpLoadFileNewDTO upLoadFileNewDTO) throws IOException {
		this.uploadFileNew(upLoadFileNewDTO);
	}

	@Override
	public R updateName(String fileName, Long id) {
		List<Long> idByNames = attachmentDao.getIdByName(fileName);
		for (Long idByName : idByNames) {
			Long fileIdByAttId = attachmentFolderDao.getFileIdByAttId(idByName);
			List<Long> attIds = attachmentFolderDao.queryFileById(fileIdByAttId);
			for (Long attId : attIds) {
				if (attId.equals(id)) {
					return R.fail("文件名已存在，请重新命名");
				}
			}
		}


		return R.ok(attachmentDao.updateName(fileName, id));
	}

	@Override
	public R<Map<String, String>> uploadFileVideoNew(UpLoadVideoFileNewDTO upLoadFileNewDTO) throws IOException {
		MultipartFile file = upLoadFileNewDTO.getFile();
		Long fileFolderId = upLoadFileNewDTO.getFileFolderId();
		Long userId = upLoadFileNewDTO.getUserId();
		String fileSource = upLoadFileNewDTO.getFileSource();
		String scope = upLoadFileNewDTO.getScope();
		Long shopId = upLoadFileNewDTO.getShopId();
		// 文件校验
		String fileName = file.getOriginalFilename();
		if (StringUtils.isBlank(fileName)) {
			return R.fail("错误的文件类型！");
		}

		String fileContentType;
		String fileSuffixType;
		try {
			fileContentType = FileTypeUtil.getType(file.getInputStream());
			fileSuffixType = fileName.substring(fileName.lastIndexOf(".") + 1);
		} catch (IOException e) {
			log.error("上传失败", e);
			return R.fail("文件类型错误！");
		}
		//如果为视频格式是否满足 mp4  webm ogg
//		if (!upLoadFileNewDTO.getUploadType()){
//			if ("mp4".equalsIgnoreCase(fileSuffixType)?Boolean.FALSE:("webm".equalsIgnoreCase(fileSuffixType)?Boolean.FALSE:("ogg".equalsIgnoreCase(fileSuffixType)?Boolean.FALSE:Boolean.TRUE))){
//				throw new BusinessException("不支持"+fileSuffixType+"视频格式！！");
//			}
//		}

		if (!upLoadFileNewDTO.getUploadType()) {
			if (!"mp4".equalsIgnoreCase(fileSuffixType)) {
				throw new BusinessException("不支持" + fileSuffixType + "视频格式！！");
			}
		}

		if (CollectionUtils.isEmpty(this.allowedFileTypesProperties.getAllowedFileTypes())) {
			return R.fail("禁止任何文件类型上传！");
		}
		if (null == fileContentType) {
			fileContentType = "";
		}
		String s = fileContentType.toUpperCase();
		log.info("文件类型{}", s);
		if (!this.allowedFileTypesProperties.getAllowedFileTypes().contains(fileContentType.toUpperCase()) &&
				!this.allowedFileTypesProperties.getAllowedFileTypes().contains(fileSuffixType.toUpperCase())) {
			return R.fail("禁止的文件类型！");
		}
		//保存数据到中间表
		AttachmentFolder attachmentFolder = new AttachmentFolder();
		//根据url获取视频路径
		Long idByPath;
		Map<String, String> resultMap = new HashMap<>(4);


		// 敏感图片审核
		R<Void> result = wxService.imgSecCheck(file);
		if (!result.success()) {
			return R.fail(result.getMsg());
		}

		// 文件上传
		String bucketName = this.ossProperties.getBucketName();
		if (FileScopeEnum.PRIVATE.equals(FileScopeEnum.valueOf(scope))) {
			bucketName = this.ossProperties.getPrivateBucketName();
		}
		fileName = IdUtil.simpleUUID() + StrUtil.DOT + FileUtil.extName(file.getOriginalFilename());
		resultMap.put("fileName", fileName);
		String dataFormat = DateUtil.format(DateUtil.date(), "yyyy/MM/dd/");
		String url = dataFormat + fileName;
		resultMap.put("bucketName", bucketName);
		resultMap.put("url", url);

		try {
			ossService.putObject(bucketName, url, file.getInputStream());
			saveVideAttachmentNew(userId, file, url, fileSource, scope, upLoadFileNewDTO.getUploadType(), upLoadFileNewDTO.getAttachmentId());
		} catch (Exception e) {
			log.error("上传失败", e);
			return R.fail(e.getLocalizedMessage());
		}

		//根据url获取id
		idByPath = attachmentDao.getIdByFilePath(url, upLoadFileNewDTO.getUploadType());
		//附件表的id
		resultMap.put("attachmentId", idByPath.toString());


		String attachmentFolderStr = fileFolderId + fileName;

		//判断是否是
		if (ObjectUtil.isNull(fileFolderId) && ObjectUtil.isNotNull(shopId)) {
			Long byShopId = attachmentFileFolderDao.getByShopId(shopId);
			fileFolderId = byShopId;

		} else if (ObjectUtil.isNull(fileFolderId)) {
			fileFolderId = attachmentFileFolderDao.getIdByParentId(-1L);

		} else if (fileFolderId == -1 && ObjectUtil.isNull(shopId)) {
			fileFolderId = attachmentFileFolderDao.getIdByParentId(fileFolderId);

		}
		//保存数据到中间表
		attachmentFolder.setAttachmentFolder(attachmentFolderStr);
		attachmentFolder.setAttachmentId(idByPath);
		attachmentFolder.setFileFolderId(fileFolderId);
		attachmentFolderDao.save(attachmentFolder);
		String name = file.getOriginalFilename();
		List<Long> idsByName = attachmentDao.getIdsByName(name);
		int count = 0;
		if (ObjectUtil.isNotNull(idsByName)) {
			for (Long attId : idsByName) {
				if (attachmentFolderDao.getFolderIdByAttId(attId) != null && attachmentFolderDao.getFolderIdByAttId(attId).equals(fileFolderId)) {
					count++;
				} else {
					continue;
				}
			}
			if (!upLoadFileNewDTO.getUploadType()) {
				if (count > 0) {
					while (attachmentDao.getAttIdByName((file.getOriginalFilename() + "(" + count + ")"), fileFolderId) != null) {
						count++;
					}
					attachmentDao.updateName(file.getOriginalFilename() + "(" + count + ")", idByPath);
				}
			}
		}

		return R.ok(resultMap);

	}

	@Override
	public R<Map<String, String>> uploadFileZip(Long userId, MultipartFile file, String fileSource, String scope, Long fileType) throws IOException {
		// 文件校验
		String fileName = file.getOriginalFilename();
		if (StringUtils.isBlank(fileName)) {
			return R.fail("错误的文件类型！");
		}
		String fileContentType;
		String fileSuffixType;
		try {
			fileContentType = FileTypeUtil.getType(file.getInputStream());
			fileSuffixType = fileName.substring(fileName.lastIndexOf(".") + 1);
			log.info("上件图片中，图片类型为：{}", fileContentType);
		} catch (IOException e) {
			log.error("上传失败", e);
			return R.fail("文件类型错误！");
		}

//		if (fileType==1){
//			if (!"mp4".equalsIgnoreCase(fileSuffixType)){
//				throw new BusinessException("不支持"+fileSuffixType+"视频格式！！");
//			}
//		}


		if (null == fileContentType) {
			fileContentType = "";
			// TODO mp4文件无法识别，只能先依照后缀进行保存
			if (StrUtil.isNotBlank(fileSuffixType)) {
				log.info("当前文件类型无法识别，使用文件名后缀作为文件类型，{}", fileSuffixType);
				fileContentType = fileSuffixType;
			}
		}

		// 敏感图片审核xx
		R<Void> result = wxService.imgSecCheck(file);
		if (!result.success()) {
			return R.fail(result.getMsg());
		}

		// 图片上传
		String bucketName = this.ossProperties.getBucketName();
		if (FileScopeEnum.PRIVATE.equals(FileScopeEnum.valueOf(scope.toUpperCase()))) {
			bucketName = this.ossProperties.getPrivateBucketName();
		}
		fileName = IdUtil.simpleUUID() + StrUtil.DOT + fileContentType;
		Map<String, String> resultMap = new HashMap<>(4);
		resultMap.put("fileName", fileName);
		String dataFormat = DateUtil.format(DateUtil.date(), "yyyy/MM/dd/");
		String url = dataFormat + fileName;
		resultMap.put("bucketName", bucketName);
		resultMap.put("url", url);
		try {
			ossService.putObject(bucketName, url, file.getInputStream());
			saveAttachment(userId, file, url, fileSource, scope, null);
		} catch (Exception e) {
			log.error("上传失败", e);
			return R.fail(e.getLocalizedMessage());
		}

		return R.ok(resultMap);
	}

	@Override
	public int deleteAttachmentByPath(String filePath) {
		return attachmentDao.deleteAttachmentByFilePath(filePath);
	}

	/**
	 * @param userId
	 * @param file         保存文件
	 * @param url          url路径
	 * @param fileSource   文件源
	 * @param scope
	 * @param uploadType   true代表上传封面图 false代表上传视频
	 * @param attachmentId 附件表id 第一次上传没有附件id默认为0 第二次带着附件id关联视频或封面图
	 */
	private void saveVideAttachmentNew(Long userId, MultipartFile file, String url, String fileSource, String scope, Boolean uploadType, Long attachmentId) {
		Attachment attachment = new Attachment();
		String original = file.getOriginalFilename();
		//如果有附件id 就直接修改
		if (attachmentId != 0) {
			if (uploadType) {
				Attachment pictureAttachment = attachmentDao.getById(attachmentId);
				pictureAttachment.setFilePath(url);
				attachmentDao.update(pictureAttachment);
				return;
			} else {
				Attachment videoAttachment = attachmentDao.getById(attachmentId);
				videoAttachment.setVidePath(url);
				videoAttachment.setFileName(original);
				videoAttachment.setFileSize(file.getSize());
				videoAttachment.setExt(FileUtil.extName(original));
				attachmentDao.update(videoAttachment);
				return;
			}
		}
		attachment.setUserId(userId);
		attachment.setFileName(original);
		if (uploadType) {
			//视频路径
			attachment.setFilePath(url);
		} else {
			//视频路径
			attachment.setVidePath(url);
		}
		attachment.setFileSource(fileSource);
		attachment.setStatus(CommonConstants.SUCCESS);
		attachment.setFileSize(file.getSize());
		attachment.setManagedFlag(0);
		attachment.setScope(scope);
		attachment.setExt(FileUtil.extName(original));
		attachment.setCreateTime(DateUtil.date());
		attachmentDao.save(attachment);
	}

	/**
	 * 保存附件
	 *
	 * @param file
	 * @param filePath
	 */
	private void saveAttachment(Long userId, MultipartFile file, String filePath, String fileSource, String scope, String shortPath) {
		Attachment attachment = new Attachment();
		String original = file.getOriginalFilename();
		attachment.setUserId(userId);
		attachment.setFileName(original);
		attachment.setFilePath(filePath);
		attachment.setFileSource(fileSource);
		attachment.setStatus(CommonConstants.SUCCESS);
		attachment.setFileSize(file.getSize());
		attachment.setScope(scope);
		attachment.setManagedFlag(0);
		attachment.setExt(FileUtil.extName(original));
		attachment.setCreateTime(DateUtil.date());
		if (StrUtil.isNotBlank(shortPath)) {
			attachment.setShortPath(shortPath);
			attachment.setManagedFlag(1);
		}

		attachmentDao.save(attachment);
	}

	/**
	 * 保存附件,图片管理
	 *
	 * @param file     the 保存文件
	 * @param filePath the 图片路径
	 */
	private Long saveAttachmentNew(Long userId, MultipartFile file, String filePath) {
		Attachment attachment = new Attachment();
		String original = file.getOriginalFilename();
		attachment.setUserId(userId);
		attachment.setFileName(original);
		attachment.setFilePath(filePath);
		attachment.setFileSource("S");
		attachment.setStatus(CommonConstants.SUCCESS);
		attachment.setFileSize(file.getSize());
		attachment.setManagedFlag(0);
		attachment.setScope("PUBLIC");
		attachment.setExt(FileUtil.extName(original));
		attachment.setCreateTime(DateUtil.date());
		Long id = attachmentDao.save(attachment);
		return id;
	}


}
