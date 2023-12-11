/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.basic.dao.ExportExcelTaskDao;
import com.legendshop.basic.dto.ExportExcelTaskDTO;
import com.legendshop.basic.entity.ExportExcelTask;
import com.legendshop.basic.query.ExportExcelTaskQuery;
import com.legendshop.basic.service.ExportExcelTaskService;
import com.legendshop.basic.service.convert.ExportExcelTaskConverter;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.expetion.BusinessException;
import com.legendshop.common.excel.enums.ExportExcelStatusEnum;
import com.legendshop.common.oss.http.OssService;
import com.legendshop.common.oss.properties.OssProperties;
import com.legendshop.common.security.dto.BaseUserDetail;
import com.legendshop.common.security.utils.SecurityUtils;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @author legendshop
 * @version 1.0.0
 * @title ExportExcelTaskServiceImpl
 * @date 2022/4/26 12:00
 * @description：
 */
@Slf4j
@Service
public class ExportExcelTaskServiceImpl implements ExportExcelTaskService {

	@Autowired
	private OssService ossService;

	@Autowired
	private OssProperties ossProperties;

	@Autowired
	private ExportExcelTaskDao exportExcelTaskDao;

	@Autowired
	private ExportExcelTaskConverter exportExcelTaskConverter;


	@Override
	public R<String> save(ExportExcelTaskDTO exportExcelTaskDTO) {
		//1、查询当前用户导出的业务名称的数据，判断是否有频繁导出。有则不允许操作

		//2、生成Task数据记录
		//生成随机文件名。
		String fileName = IdUtil.simpleUUID() + StrUtil.DOT + "xls";
		exportExcelTaskDTO.setFileName(fileName);
		exportExcelTaskDTO.setCreateTime(DateUtil.date());
		exportExcelTaskDTO.setStatus(ExportExcelStatusEnum.IN_PROGRESS.getValue());
		exportExcelTaskDTO.setCreateTime(DateUtil.date());
		exportExcelTaskDTO.setUpdateTime(DateUtil.date());
		exportExcelTaskDao.save(exportExcelTaskConverter.from(exportExcelTaskDTO));

		return R.ok(fileName);
	}

	@Override
	public R<PageSupport<ExportExcelTaskDTO>> page(ExportExcelTaskQuery query) {
		BaseUserDetail baseUser = SecurityUtils.getBaseUser();
		query.setUserId(baseUser.getUserId());
		query.setUserType(baseUser.getUserType());
		return R.ok(exportExcelTaskDao.page(query));
	}

	@Override
	public void download(HttpServletResponse response, Long exportId) {
		BaseUserDetail baseUser = SecurityUtils.getBaseUser();
		ExportExcelTask exportExcelTask = exportExcelTaskDao.getById(exportId, baseUser.getUserId(), baseUser.getUserType());
		if (null == exportExcelTask) {
			throw new BusinessException("文件下载失败，当前文件找不到！");
		}

		if (!ExportExcelStatusEnum.SUCCESS.getValue().equals(exportExcelTask.getStatus()) || StrUtil.isBlank(exportExcelTask.getUrl())) {
			throw new BusinessException("文件下载失败，当前文件找不到！");
		}

		try {
			String bucketName = this.ossProperties.getPrivateBucketName();
			String fileName = exportExcelTask.getBusinessName();
			@Cleanup InputStream inputStream = this.ossService.getObject(bucketName, exportExcelTask.getUrl());
			@Cleanup OutputStream outputStream = response.getOutputStream();

			response.setContentType("application/vnd.ms-excel");
			response.setCharacterEncoding("utf-8");
			// 这里URLEncoder.encode可以防止中文乱码
			try {
				fileName = URLEncoder.encode(fileName, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				log.error("编码异常");
			}
			response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");

			int length;
			byte[] temp = new byte[1024 * 10];
			while ((length = inputStream.read(temp)) != -1) {
				outputStream.write(temp, 0, length);
			}
			outputStream.flush();

		} catch (Exception e) {
			log.error("文件下载失败！", e);
			throw new BusinessException("系统异常，文件下载失败！");
		}
	}
}
