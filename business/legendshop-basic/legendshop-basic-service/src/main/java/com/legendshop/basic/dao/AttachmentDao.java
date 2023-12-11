/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.dao;


import cn.legendshop.jpaplus.GenericDao;
import cn.legendshop.jpaplus.PageSupportHandler;
import com.legendshop.basic.dto.AttachmentDTO;
import com.legendshop.basic.entity.Attachment;
import com.legendshop.basic.query.AttachmentQuery;

import java.util.List;

/**
 * 附件的Dao.
 *
 * @author legendshop
 */
public interface AttachmentDao extends GenericDao<Attachment, Long> {

	/**
	 * 根据路径获取Id
	 *
	 * @param url
	 * @return
	 */
	Long getIdByPath(String url);

	/**
	 * 分页查询附件表
	 *
	 * @param attachmentQuery
	 * @return
	 */
	PageSupportHandler<AttachmentDTO> queryPage(AttachmentQuery attachmentQuery);

	/**
	 * 更新图片状态为图片空间可用
	 *
	 * @param filePath
	 * @return
	 */
	Integer updateManagedFlage(String filePath);

	/**
	 * 根据id更新附件名字
	 *
	 * @param fileName
	 * @param id
	 * @return
	 */
	Integer updateName(String fileName, Long id);

	/**
	 * 根据文件名查询, 判断文件名是否已经存在
	 *
	 * @param fileName
	 * @return
	 */
	List<Long> getIdByName(String fileName);

	Attachment getAttachmentByPath(String filePath);

	String getNameByUrl(String path);

	/**
	 * 根据路径更新名字
	 *
	 * @param fileName
	 * @param filePath
	 * @return
	 */
	Integer updateNameByPath(String fileName, List<String> filePath);

	/**
	 * 查询当前文件夹下, 该名字图片个数
	 *
	 * @param fileName
	 * @return
	 */
	List<Long> getCountByName(String fileName);

	List<Long> getIdsByName(String fileName);

	/**
	 * 上传图片时, 判断当前文件夹是否有该图片
	 *
	 * @param s
	 * @param fileFolderId
	 * @return
	 */
	Long getAttIdByName(String s, Long fileFolderId);

	/**
	 * 根据ID删除文件
	 *
	 * @param attIds
	 * @return
	 */
	Integer deleteByAttId(List<Long> attIds);

	/**
	 * 根据url 查询附件表id
	 *
	 * @param url
	 * @param uploadType
	 * @return
	 */
	Long getIdByFilePath(String url, Boolean uploadType);

	/**
	 * 更新视频状态为图片空间可用
	 *
	 * @param path
	 * @return
	 */
	Integer updateVideoManagedFlage(String path);

	int deleteAttachmentByFilePath(String filePath);

	List<Attachment> getAttachmentByShortPath(Long userId, List<String> shortPath);
}
