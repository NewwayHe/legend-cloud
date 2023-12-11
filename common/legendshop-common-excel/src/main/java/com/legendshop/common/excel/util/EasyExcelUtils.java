/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.excel.util;

import cn.hutool.core.date.DateUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.builder.ExcelWriterSheetBuilder;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.legendshop.common.excel.dto.ExportExcelUpdateDTO;
import com.legendshop.common.excel.enums.ExportExcelStatusEnum;
import com.legendshop.common.oss.http.OssService;
import com.legendshop.common.oss.properties.OssProperties;
import com.legendshop.common.rabbitmq.constants.AmqpConst;
import com.legendshop.common.rabbitmq.util.AmqpSendMsgUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

/**
 * 描述:
 *
 * @author legendshop
 * @create 2022-04-19
 */
@Slf4j
public class EasyExcelUtils {

	/**
	 * 每个sheet的容量，即超过60000时就会把数据分sheet
	 */
	private static final int PAGE_SIZE = 60000;

	@Autowired
	private OssService ossService;

	@Autowired
	private OssProperties ossProperties;

	@Autowired
	private AmqpSendMsgUtil amqpSendMsgUtil;

	/**
	 * 导出报表(使用注解方式设置Excel头数据)
	 *
	 * @param response   响应请求
	 * @param data       报表数据
	 * @param fileName   文件名字
	 * @param excelClass 报表实体类的Class（根据该Class的属性来设置Excel的头属性）
	 */
	public void exportByExcel(HttpServletResponse response, List<?> data, String fileName, Class<?> excelClass) throws IOException {
		@Cleanup ByteArrayOutputStream os = new ByteArrayOutputStream();
		long exportStartTime = System.currentTimeMillis();
		log.info("报表导出Size: " + data.size() + "条。");

		// 把查询到的数据按设置的sheet的容量进行切割
		List<? extends List<?>> lists = SplitList.splitList(data, PAGE_SIZE);

		// 设置响应头
		EasyExcelExt.setHead(response, fileName);

		// 格式化Excel数据
		// EasyExcelBasic.formatExcel()：设置Excel的格式
		// EasyExcelBasic.ExcelWidthStyleStrategy():设置头部单元格宽度
		ExcelWriter excelWriter = EasyExcel.write(os, excelClass).registerWriteHandler(EasyExcelExt.formatExcel()).registerWriteHandler(new EasyExcelExt.ExcelWidthStyleStrategy()).build();

		ExcelWriterSheetBuilder excelWriterSheetBuilder;
		WriteSheet writeSheet;
		for (int i = 1; i <= lists.size(); ++i) {
			excelWriterSheetBuilder = new ExcelWriterSheetBuilder(excelWriter);
			excelWriterSheetBuilder.sheetNo(i).sheetName("sheet" + i);
			writeSheet = excelWriterSheetBuilder.build();
			excelWriter.write(lists.get(i - 1), writeSheet);
		}

		// 必须要finish才会写入，不finish只会创建empty的文件
		excelWriter.finish();

		byte[] content = os.toByteArray();
		@Cleanup InputStream is = new ByteArrayInputStream(content);

		// 文件上传到OSS
		uploadFile(is, fileName);

		System.out.println("报表导出结束时间:" + new Date() + ";导出耗时: " + (System.currentTimeMillis() - exportStartTime) + "ms");

	}

	/**
	 * 导出报表(使用注解方式设置Excel头数据)
	 *
	 * @param data       报表数据
	 * @param fileName   文件名字
	 * @param excelClass 报表实体类的Class（根据该Class的属性来设置Excel的头属性）
	 */
	public void exportByExcel(List<?> data, String fileName, Class<?> excelClass) {
		try {
			@Cleanup ByteArrayOutputStream os = new ByteArrayOutputStream();
			long exportStartTime = System.currentTimeMillis();
			log.info("报表导出Size: " + data.size() + "条。");

			// 把查询到的数据按设置的sheet的容量进行切割
			List<? extends List<?>> lists = SplitList.splitList(data, PAGE_SIZE);

			// 格式化Excel数据
			// EasyExcelBasic.formatExcel()：设置Excel的格式
			// EasyExcelBasic.ExcelWidthStyleStrategy():设置头部单元格宽度
			ExcelWriter excelWriter = EasyExcel.write(os, excelClass).registerWriteHandler(EasyExcelExt.formatExcel()).registerWriteHandler(new EasyExcelExt.ExcelWidthStyleStrategy()).build();

			ExcelWriterSheetBuilder excelWriterSheetBuilder;
			WriteSheet writeSheet;
			for (int i = 1; i <= lists.size(); ++i) {
				excelWriterSheetBuilder = new ExcelWriterSheetBuilder(excelWriter);
				excelWriterSheetBuilder.sheetNo(i).sheetName("sheet" + i);
				writeSheet = excelWriterSheetBuilder.build();
				excelWriter.write(lists.get(i - 1), writeSheet);
			}

			// 必须要finish才会写入，不finish只会创建empty的文件
			excelWriter.finish();

			byte[] content = os.toByteArray();
			@Cleanup InputStream is = new ByteArrayInputStream(content);

			// 文件上传到OSS
			Boolean result = uploadFile(is, fileName);
			log.info("报表导出结束时间: {}; 导出耗时: {}ms; 结果: {}", DateUtil.date(), System.currentTimeMillis() - exportStartTime, result);
		} catch (Exception e) {
			ExportExcelUpdateDTO updateDTO = new ExportExcelUpdateDTO();
			updateDTO.setFileName(fileName);
			updateDTO.setStatus(ExportExcelStatusEnum.FAIL);
			updateDTO.setRemark(e.getMessage());
			this.amqpSendMsgUtil.convertAndSend(AmqpConst.SYSTEM_EXPORT_EXCEL_EXCHANGE, AmqpConst.SYSTEM_EXPORT_EXCEL_UPDATE_ROUTING_KEY,
					updateDTO, -1L, ChronoUnit.MILLIS);
		}
	}

	public Boolean uploadFile(InputStream is, String fileName) throws IOException {
		String bucketName = this.ossProperties.getPrivateBucketName();
		String dataFormat = DateUtil.format(DateUtil.date(), "yyyy/MM/dd/");
		String url = dataFormat + fileName;

		ExportExcelUpdateDTO updateDTO = new ExportExcelUpdateDTO();
		updateDTO.setFileName(fileName);

		try {
			log.info("上传OSS,URL is{}", url);
			ossService.putObject(bucketName, url, is);
			log.info("下载完成，通知Basic服务完成，可以下载");
			updateDTO.setStatus(ExportExcelStatusEnum.SUCCESS);
			updateDTO.setUrl(url);

		} catch (Exception e) {
			log.error("上传失败", e);
			updateDTO.setStatus(ExportExcelStatusEnum.FAIL);
			updateDTO.setRemark(e.getMessage());
			return false;
		} finally {
			this.amqpSendMsgUtil.convertAndSend(AmqpConst.SYSTEM_EXPORT_EXCEL_EXCHANGE, AmqpConst.SYSTEM_EXPORT_EXCEL_UPDATE_ROUTING_KEY,
					updateDTO, -1L, ChronoUnit.MILLIS);
		}
		return true;
	}
}

