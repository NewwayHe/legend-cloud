/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.excel.converters;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.property.ExcelContentProperty;

/**
 * @author legendshop
 * @version 1.0.0
 * @title DefaultStringConverter
 * @date 2022/4/16 14:10
 * @description：
 */
public class NullableDefaultConverter implements Converter<Object> {

	@Override
	public Class supportJavaTypeKey() {
		return Object.class;
	}

	@Override
	public CellDataTypeEnum supportExcelTypeKey() {
		return CellDataTypeEnum.STRING;
	}

	@Override
	public Object convertToJavaData(CellData cellData, ExcelContentProperty excelContentProperty, GlobalConfiguration globalConfiguration) throws Exception {
		return cellData.getData();
	}

	@Override
	public CellData convertToExcelData(Object o, ExcelContentProperty excelContentProperty, GlobalConfiguration globalConfiguration) throws Exception {
		if (o == null) {
			return new CellData("-");
		} else {
			CellData cellData = new CellData(o);
			if (o instanceof Number) {
				cellData.setType(CellDataTypeEnum.NUMBER);
			} else {
				cellData.setType(CellDataTypeEnum.STRING);
			}
			return cellData;
		}
	}
}
