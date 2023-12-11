/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.service;

import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.basic.dto.AttFileIdListDTO;
import com.legendshop.basic.dto.AttachmentFileFolderDTO;
import com.legendshop.basic.dto.FileFolderUpdateDTO;
import com.legendshop.basic.entity.AttachmentFileFolder;
import com.legendshop.basic.query.AttachmentFileFolderQuery;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.security.dto.ShopUserDetail;

import java.util.List;

/**
 * (AttachmentFileFolder)表服务接口
 *
 * @author legendshop
 * @since 2021-07-06 14:19:23
 */
public interface AttachmentFileFolderService {


	/**
	 * 第一次访问查找平台根目录所有文件
	 *
	 * @param parentId
	 * @return
	 */
	AttachmentFileFolderDTO getByParentId(Long parentId);

	/**
	 * 根据文件id查找子文件夹和图片
	 *
	 * @param fileId
	 * @return
	 */
	AttachmentFileFolderDTO getFolderByFileId(Long fileId, Long shopId, Long userType);

	/**
	 * 新建文件夹
	 *
	 * @param attachmentFileFolder
	 * @return
	 */
	R saveFileFolder(AttachmentFileFolder attachmentFileFolder);

	/**
	 * 判断文件夹或者文件是否存在
	 *
	 * @param id
	 * @param ext
	 * @param fileName
	 * @return
	 */
	R checkAttachment(Long id, String ext, String fileName);

	/**
	 * 更新文件夹名字
	 *
	 * @param fileName
	 * @param id
	 * @return
	 */
	R updateFileFolderName(String fileName, Long id);


	/**
	 * 更新文件路径
	 *
	 * @param fileFolderUpdateDTO 更新路径对象
	 * @return
	 */
	R updateAttPathByFileFolderId(FileFolderUpdateDTO fileFolderUpdateDTO);

	/**
	 * 删除文件
	 *
	 * @param id
	 * @param ext
	 * @return
	 */
	Integer delFile(Long id, String ext);

	/**
	 * 分页查询
	 *
	 * @param attachmentFileFolderQuery
	 * @return
	 */
	R<PageSupport<AttachmentFileFolderDTO>> queryPage(AttachmentFileFolderQuery attachmentFileFolderQuery);

	/**
	 * 根据ids查询文件夹下是否有图片列表
	 *
	 * @param attFileIdListDTO
	 * @return
	 */
	R checkAttachmentInFile(AttFileIdListDTO attFileIdListDTO);

	/**
	 * 批量删除
	 *
	 * @param attFileIdListDTOList
	 * @param shopUserDetail
	 * @return
	 */
	R delFileList(List<AttFileIdListDTO> attFileIdListDTOList, ShopUserDetail shopUserDetail);

	/**
	 * [平台]获取导航id集合
	 *
	 * @param id
	 * @return
	 */
	List<AttachmentFileFolder> getIdList(Long id);

	/**
	 * [商家]获取导航id集合
	 *
	 * @param id
	 * @return
	 */
	List<AttachmentFileFolder> getIdList(Long id, Long shopId);

	/**
	 * 初始化商家根目录
	 *
	 * @param attachmentFileFolderDTO
	 * @return
	 */
	R save(AttachmentFileFolderDTO attachmentFileFolderDTO);

	/**
	 * 获取品台根目录文件夹
	 *
	 * @param parentId
	 * @return
	 */
	Long getAdminByParentId(Long parentId);

	List<Long> queryIds();

	Long getByShopId(Long id);

	/**
	 * [商家] 查询本商家所有文件夹
	 *
	 * @param shopId
	 * @return
	 */
	List<AttachmentFileFolderDTO> queryShopFolder(Long shopId);


	/**
	 * 查询文件夹
	 *
	 * @param id
	 * @param shopId
	 * @return
	 */
	List<AttachmentFileFolderDTO> queryFileFolderList(Long id, Long shopId);

	/**
	 * 更新文件夹路径
	 *
	 * @param fileFolderUpdateDTO
	 * @return
	 */
	R updateFilePath(FileFolderUpdateDTO fileFolderUpdateDTO);

	/**
	 * 【平台】分页显示所有文件夹
	 *
	 * @param id       文件夹ID
	 * @param userType 用户类型
	 * @return
	 */
	List<AttachmentFileFolderDTO> queryPageAllFolder(Long id, Integer userType);

	/**
	 * 保存文件访问历史记录，并返回最新5条
	 *
	 * @param userId
	 * @param id
	 * @return
	 */
	R<List<List<AttachmentFileFolderDTO>>> saveHistory(Long userId, String userType, List<Long> id);


	AttachmentFileFolder getByShopIdFirst(Long shopId);


	void update(AttachmentFileFolder byShopIdFirst);
}
