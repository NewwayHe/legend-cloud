/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.spider.core;

import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.lang.Nullable;

/**
 * @author legendshop
 */
public class SpiderConversionService extends DefaultFormattingConversionService {
	@Nullable
	private static volatile SpiderConversionService SHARED_INSTANCE;

	public SpiderConversionService() {
		super();
		super.addConverter(new EnumToStringConverter());
		super.addConverter(new StringToEnumConverter());
	}

	/**
	 * Return a shared default application {@code ConversionService} instance, lazily
	 * building it once needed.
	 * <p>
	 * Note: This method actually returns an {@link SpiderConversionService}
	 * instance. However, the {@code ConversionService} signature has been preserved for
	 * binary compatibility.
	 *
	 * @return the shared {@code MicaConversionService} instance (never{@code null})
	 */
	public static GenericConversionService getInstance() {
		SpiderConversionService sharedInstance = SpiderConversionService.SHARED_INSTANCE;
		if (sharedInstance == null) {
			synchronized (SpiderConversionService.class) {
				sharedInstance = SpiderConversionService.SHARED_INSTANCE;
				if (sharedInstance == null) {
					sharedInstance = new SpiderConversionService();
					SpiderConversionService.SHARED_INSTANCE = sharedInstance;
				}
			}
		}
		return sharedInstance;
	}

}
