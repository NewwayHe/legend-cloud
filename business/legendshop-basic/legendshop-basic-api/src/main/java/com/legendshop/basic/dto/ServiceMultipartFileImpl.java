/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.dto;

import java.io.Serializable;

/**
 * 传递到后台的文件，不再跟MultipartFile挂钩。
 * byte[] 数据流不需要传递到服务层， 由controller直接调用file-service-api, 有以下2种情况：
 * 1. 先把文件上传到文件服务器或者OSS， 把上传结果传给后台服务
 * 2. 先做业务逻辑，等待业务逻辑成功之后再上传文件
 *
 * @author legendshop
 */
public class ServiceMultipartFileImpl implements ServiceMultipartFile, Serializable {

	private static final long serialVersionUID = 2523790857854535713L;

	/**
	 * 文件路径
	 */
	private String path;
	/**
	 * 上传后的文件名
	 */
	private String name;

	/**
	 * 文件原名
	 */
	private String originalFilename;

	/**
	 * 文件大小
	 */
	private long size;

	/**
	 * 附件Id
	 */
	private Long attachmentId;


	public ServiceMultipartFileImpl(String path, String name, String originalFilename, long size) {
		this.path = path;
		this.name = name;
		this.originalFilename = originalFilename;
		this.size = size;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getOriginalFilename() {
		return originalFilename;
	}

	@Override
	public boolean isEmpty() {
		return size <= 0L;
	}

	@Override
	public long getSize() {
		return size;
	}

	@Override
	public String getPath() {
		return path;
	}

	@Override
	public Long getAttachmentId() {
		return attachmentId;
	}

	@Override
	public void setAttachmentId(Long attachmentId) {
		this.attachmentId = attachmentId;
	}
}
