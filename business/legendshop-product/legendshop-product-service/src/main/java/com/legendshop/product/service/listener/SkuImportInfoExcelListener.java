/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.service.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson.JSON;
import com.legendshop.product.dto.SkuImportDTO;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author legendshop
 */
@Slf4j
public class SkuImportInfoExcelListener extends AnalysisEventListener<SkuImportDTO> {

	/**
	 * 创建list集合封装最终的数据
	 */
	public List<SkuImportDTO> list = new ArrayList<>();

	/**
	 * 一行一行去读取excel内容
	 */
	@Override
	public void invoke(SkuImportDTO dto, AnalysisContext analysisContext) {
		list.add(dto);
	}

	/**
	 * 读取excel表头信息
	 */
	@Override
	public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
		log.info("表头信息：{}", JSON.toJSONString(headMap));
	}


	/**
	 * 读取完成后执行
	 */
	@Override
	public void doAfterAllAnalysed(AnalysisContext analysisContext) {
	}

	public List<SkuImportDTO> getList() {
		return list;
	}
}
