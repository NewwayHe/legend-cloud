/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.spider.core;

import cn.hutool.http.HttpResponse;
import com.legendshop.common.core.util.ExceptionUtil;
import com.legendshop.common.spider.annotation.HtmlQuery;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.springframework.cglib.proxy.Enhancer;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * dom文档,爬虫 xml 转 bean 基于 jsoup
 *
 * @author legendshop
 */
public class DomMapper {
	/**
	 * Returns body to jsoup Document.
	 *
	 * @return Document
	 */
	public static Document asDocument(HttpResponse response) {
		return readDocument(response.toString());
	}

	/**
	 * 将流读取为 jsoup Document
	 *
	 * @param inputStream InputStream
	 * @return Document
	 */
	public static Document readDocument(InputStream inputStream) {
		try {
			return DataUtil.load(inputStream, StandardCharsets.UTF_8.name(), "");
		} catch (IOException e) {
			throw ExceptionUtil.unchecked(e);
		}
	}

	/**
	 * 将 html 字符串读取为 jsoup Document
	 *
	 * @param html String
	 * @return Document
	 */
	public static Document readDocument(String html) {
		return Parser.parse(html, "");
	}

	/**
	 * 读取 xml 信息为 java Bean
	 *
	 * @param response ResponseSpec
	 * @param clazz    bean Class
	 * @param <T>      泛型
	 * @return 对象
	 */
	public static <T> T readValue(HttpResponse response, final Class<T> clazz) {
		return readValue(response.bodyStream(), clazz);
	}

	/**
	 * 读取 xml 信息为 java Bean
	 *
	 * @param inputStream InputStream
	 * @param clazz       bean Class
	 * @param <T>         泛型
	 * @return 对象
	 */
	public static <T> T readValue(InputStream inputStream, final Class<T> clazz) {
		return readValue(readDocument(inputStream), clazz);
	}

	/**
	 * 读取 xml 信息为 java Bean
	 *
	 * @param html  html String
	 * @param clazz bean Class
	 * @param <T>   泛型
	 * @return 对象
	 */
	public static <T> T readValue(String html, final Class<T> clazz) {
		return readValue(readDocument(html), clazz);
	}

	/**
	 * 读取 xml 信息为 java Bean
	 *
	 * @param doc   xml element
	 * @param clazz bean Class
	 * @param <T>   泛型
	 * @return 对象
	 */
	@SuppressWarnings("unchecked")
	public static <T> T readValue(final Element doc, final Class<T> clazz) {
		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(clazz);
		enhancer.setUseCache(true);
		enhancer.setCallback(new HtmlQueryMethodInterceptor(clazz, doc));
		return (T) enhancer.create();
	}

	/**
	 * 读取 xml 信息为 java Bean
	 *
	 * @param <T>      泛型
	 * @param response ResponseSpec
	 * @param clazz    bean Class
	 * @return 对象
	 */
	public static <T> List<T> readList(HttpResponse response, final Class<T> clazz) {
		return readList(response.bodyStream(), clazz);
	}

	/**
	 * 读取 xml 信息为 java Bean
	 *
	 * @param <T>         泛型
	 * @param inputStream InputStream
	 * @param clazz       bean Class
	 * @return 对象
	 */
	public static <T> List<T> readList(InputStream inputStream, final Class<T> clazz) {
		return readList(readDocument(inputStream), clazz);
	}

	/**
	 * 读取 xml 信息为 java Bean
	 *
	 * @param <T>   泛型
	 * @param html  html String
	 * @param clazz bean Class
	 * @return 对象
	 */
	public static <T> List<T> readList(String html, final Class<T> clazz) {
		return readList(readDocument(html), clazz);
	}

	/**
	 * 读取 xml 信息为 java Bean
	 *
	 * @param doc   xml element
	 * @param clazz bean Class
	 * @param <T>   泛型
	 * @return 对象列表
	 */
	public static <T> List<T> readList(Element doc, Class<T> clazz) {
		HtmlQuery annotation = clazz.getAnnotation(HtmlQuery.class);
		if (annotation == null) {
			throw new IllegalArgumentException("DomMapper readList " + clazz + " mast has annotation @HtmlQuery.");
		}
		String cssQueryValue = annotation.value();
		Elements elements = doc.select(cssQueryValue);
		List<T> valueList = new ArrayList<>();
		for (Element element : elements) {
			valueList.add(readValue(element, clazz));
		}
		return valueList;
	}
}
