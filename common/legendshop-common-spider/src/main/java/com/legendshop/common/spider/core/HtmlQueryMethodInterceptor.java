/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.spider.core;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.legendshop.common.spider.annotation.HtmlQuery;
import com.legendshop.common.spider.utils.ConvertUtil;
import lombok.RequiredArgsConstructor;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.jsoup.select.Selector;
import org.springframework.beans.BeanUtils;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.core.ResolvableType;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.lang.Nullable;
import org.springframework.util.ReflectionUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * html代理解释器模型
 *
 * @author legendshop
 */
@RequiredArgsConstructor
public class HtmlQueryMethodInterceptor implements MethodInterceptor {
	private final Class<?> clazz;
	private final Element element;

	@Nullable
	@Override
	public Object intercept(Object object, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
		// 如果是 toString eq 等方法都不准确，故直接返回死值
		if (ReflectionUtils.isObjectMethod(method)) {
			return methodProxy.invokeSuper(object, args);
		}
		// 非 bean 方法
		PropertyDescriptor propertyDescriptor = BeanUtils.findPropertyForMethod(method, clazz);
		if (propertyDescriptor == null) {
			return methodProxy.invokeSuper(object, args);
		}
		// 非 read 的方法，只处理 get 方法 is
		if (!method.equals(propertyDescriptor.getReadMethod())) {
			return methodProxy.invokeSuper(object, args);
		}
		String fieldName = StrUtil.lowerFirst(propertyDescriptor.getDisplayName());
		Field field = clazz.getDeclaredField(fieldName);
		if (ObjectUtil.isNull(field)) {
			return methodProxy.invokeSuper(object, args);
		}
		HtmlQuery htmlQuery = field.getAnnotation(HtmlQuery.class);
		// 没有注解，不代理
		if (htmlQuery == null) {
			return methodProxy.invokeSuper(object, args);
		}
		Class<?> returnType = method.getReturnType();
		boolean isColl = Collection.class.isAssignableFrom(returnType);
		String cssQueryValue = htmlQuery.value();
		// 是否为 bean 中 bean
		boolean isInner = htmlQuery.inner();
		if (isInner) {
			return proxyInner(cssQueryValue, method, returnType, isColl);
		}
		Object proxyValue = proxyValue(cssQueryValue, htmlQuery, returnType, isColl);
		if (String.class.isAssignableFrom(returnType)) {
			return proxyValue;
		}
		// 用于读取 field 上的注解
		TypeDescriptor typeDescriptor = new TypeDescriptor(field);
		return ConvertUtil.convert(proxyValue, typeDescriptor);
	}

	@Nullable
	private Object proxyValue(String cssQueryValue, HtmlQuery htmlQuery, Class<?> returnType, boolean isColl) {
		if (isColl) {
			Elements elements = Selector.select(cssQueryValue, element);
			Collection<Object> valueList = newColl(returnType);
			if (elements.isEmpty()) {
				return valueList;
			}
			for (Element select : elements) {
				String value = getValue(select, htmlQuery);
				if (value != null) {
					valueList.add(value);
				}
			}
			return valueList;
		}
		Element select = Selector.selectFirst(cssQueryValue, element);
		return getValue(select, htmlQuery);
	}

	/**
	 * 代理内部嵌套对象
	 *
	 * @param cssQueryValue
	 * @param method
	 * @param returnType
	 * @param isColl
	 * @return
	 */
	private Object proxyInner(String cssQueryValue, Method method, Class<?> returnType, boolean isColl) {
		if (isColl) {
			Elements elements = Selector.select(cssQueryValue, element);
			Collection<Object> valueList = newColl(returnType);
			ResolvableType resolvableType = ResolvableType.forMethodReturnType(method);
			Class<?> innerType = resolvableType.getGeneric(0).resolve();
			if (innerType == null) {
				throw new IllegalArgumentException("Class " + returnType + " 读取泛型失败。");
			}
			for (Element select : elements) {
				valueList.add(DomMapper.readValue(select, innerType));
			}
			return valueList;
		}
		Element select = Selector.selectFirst(cssQueryValue, element);
		return DomMapper.readValue(select, returnType);
	}

	@Nullable
	private String getValue(@Nullable Element element, HtmlQuery htmlQuery) {
		if (element == null) {
			return null;
		}
		// 读取的属性名
		String attrName = htmlQuery.attr();
		// 读取的值
		String attrValue;
		if (StrUtil.isBlank(attrName)) {
			attrValue = element.outerHtml();
		} else if ("html".equalsIgnoreCase(attrName)) {
			attrValue = element.html();
		} else if ("text".equalsIgnoreCase(attrName)) {
			attrValue = getText(element);
		} else if ("allText".equalsIgnoreCase(attrName)) {
			attrValue = element.text();
		} else {
			attrValue = element.attr(attrName);
		}
		// 判断是否需要正则处理
		String regex = htmlQuery.regex();
		if (StrUtil.isBlank(attrValue) || StrUtil.isBlank(regex)) {
			return attrValue;
		}
		// 处理正则表达式
		return getRegexValue(regex, htmlQuery.regexGroup(), attrValue);
	}

	@Nullable
	private String getRegexValue(String regex, int regexGroup, String value) {
		// 处理正则表达式
		Matcher matcher = Pattern.compile(regex, Pattern.DOTALL | Pattern.CASE_INSENSITIVE).matcher(value);
		if (!matcher.find()) {
			return null;
		}
		// 正则 group
		if (regexGroup > HtmlQuery.DEFAULT_REGEX_GROUP) {
			return matcher.group(regexGroup);
		}
		return matcher.group();
	}

	private String getText(Element element) {
		return element.childNodes().stream()
				.filter(node -> node instanceof TextNode)
				.map(node -> (TextNode) node)
				.map(TextNode::text)
				.collect(Collectors.joining());
	}

	private Collection<Object> newColl(Class<?> returnType) {
		return Set.class.isAssignableFrom(returnType) ? new HashSet<>() : new ArrayList<>();
	}
}
