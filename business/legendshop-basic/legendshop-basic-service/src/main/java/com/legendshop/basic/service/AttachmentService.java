/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.service;

import com.legendshop.basic.dto.UpLoadFileNewDTO;
import com.legendshop.basic.dto.UpLoadVideoFileNewDTO;
import com.legendshop.basic.dto.UpdateAttachmentFlagDTO;
import com.legendshop.common.core.constant.R;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * 附件Service.
 *
 * @author legendshop
 */
public interface AttachmentService {


	/**
	 * 上传文件。
	 *
	 * @param userId     用户ID
	 * @param file       待上传的文件
	 * @param fileSource 文件来源
	 * @param scope      文件范围
	 * @param fileType   文件类型 (0: 图片, 1: 视频)
	 * @return 包含文件信息的 R<Map<String, String>> 对象
	 * @throws IOException IO异常
	 */
	R<Map<String, String>> uploadFile(Long userId, MultipartFile file, String fileSource, String scope, Long fileType) throws IOException;

	/**
	 * 下载文件。
	 *
	 * @param fileName 文件名
	 * @param scope    文件范围
	 * @return 包含文件流的 InputStream 对象
	 */
	InputStream download(String fileName, String scope);

	/**
	 * 上传文件，用于图片空间。
	 *
	 * @param upLoadFileNewDTO 包含上传文件信息的 UpLoadFileNewDTO 对象
	 * @return 包含文件信息的 R<Map<String, String>> 对象
	 * @throws IOException IO异常
	 */
	R<Map<String, String>> uploadFileNew(UpLoadFileNewDTO upLoadFileNewDTO) throws IOException;

	/**
	 * 修改附件表中图片空间的可用状态列。
	 *
	 * @param updateAttachmentFlagDTO 包含更新附件标志信息的 UpdateAttachmentFlagDTO 对象
	 * @return 包含更新结果的 R 对象
	 */
	R updateManagedFlage(UpdateAttachmentFlagDTO updateAttachmentFlagDTO);

	/**
	 * 修改文件名。
	 *
	 * @param fileName 文件名
	 * @param id       文件ID
	 * @return 包含更新结果的 R 对象
	 */
	R updateName(String fileName, Long id);

	/**
	 * 上传文件，用于图片和视频空间。
	 *
	 * @param upLoadFileNewDTO 包含上传文件信息的 UpLoadVideoFileNewDTO 对象
	 * @return 包含文件信息的 R<Map<String, String>> 对象
	 * @throws IOException IO异常
	 */
	R<Map<String, String>> uploadFileVideoNew(UpLoadVideoFileNewDTO upLoadFileNewDTO) throws IOException;

	/**
	 * 上传压缩文件。
	 *
	 * @param userId     用户ID
	 * @param file       压缩文件
	 * @param fileSource 文件来源
	 * @param scope      文件范围
	 * @param fileType   文件类型
	 * @return 包含文件信息的 R<Map<String, String>> 对象
	 * @throws IOException IO异常
	 */
	R<Map<String, String>> uploadFileZip(Long userId, MultipartFile file, String fileSource, String scope, Long fileType) throws IOException;

	/**
	 * 根据文件路径删除附件。
	 *
	 * @param filePath 文件路径
	 * @return 操作影响的行数
	 */
	int deleteAttachmentByPath(String filePath);
}
