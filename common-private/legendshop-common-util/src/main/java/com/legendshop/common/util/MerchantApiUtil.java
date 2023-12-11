/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.util;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * 用于支付网关加密
 *
 * @author legendshop
 * @创建者: Tony哥
 */
@Slf4j
public class MerchantApiUtil {

	//私盐
	private static final String _MD5 = "@$%ETYDTY^#$@##%$L";


	/**
	 * 获取参数签名
	 *
	 * @param paramMap  签名参数
	 * @param paySecret 签名密钥
	 * @return
	 */
	public static String getSign(Map<String, Object> paramMap, String paySecret) {
		SortedMap<String, Object> smap = new TreeMap<String, Object>(paramMap);
		StringBuffer stringBuffer = new StringBuffer();
		for (Map.Entry<String, Object> m : smap.entrySet()) {
			Object value = m.getValue();
			if (value != null && StrUtil.isNotBlank(String.valueOf(value))) {
				stringBuffer.append(m.getKey()).append("=").append(value).append("&");
			}
		}
		stringBuffer.delete(stringBuffer.length() - 1, stringBuffer.length());

		String argPreSign = stringBuffer.append("&paySecret=").append(paySecret).toString();
		String signStr = SecureUtil.md5(argPreSign + _MD5).toUpperCase();

		return signStr;
	}


	/**
	 * 获取参数拼接串
	 *
	 * @param paramMap
	 * @return
	 */
	public static String getParamStr(Map<String, Object> paramMap) {
		SortedMap<String, Object> smap = new TreeMap<String, Object>(paramMap);
		StringBuffer stringBuffer = new StringBuffer();
		for (Map.Entry<String, Object> m : smap.entrySet()) {
			Object value = m.getValue();
			if (value != null && StrUtil.isNotBlank(String.valueOf(value))) {
				stringBuffer.append(m.getKey()).append("=").append(value).append("&");
			}
		}
		stringBuffer.delete(stringBuffer.length() - 1, stringBuffer.length());

		return stringBuffer.toString();
	}

	public static void main(String[] args) {

		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("xxxx", "dasfdsaf");
		paramMap.put("121", "dasf1212dsaf");

		String ssss = getParamStr(paramMap);
		System.out.println(ssss);
	}

	/**
	 * 验证商户签名
	 *
	 * @param paramMap  签名参数
	 * @param paySecret 签名私钥
	 * @param signStr   原始签名密文
	 * @return
	 */
	public static boolean isRightSign(Map<String, Object> paramMap, String paySecret, String signStr) {
		if (StrUtil.isBlank(signStr)) {
			return false;
		}
		String sign = getSign(paramMap, paySecret);
		log.info("===>订单签名校验:sign={},signStr={}", sign, signStr);
		if (signStr.equals(sign)) {
			return true;
		} else {
			return false;
		}
	}


	/**
	 * 建立请求，以表单HTML形式构造（默认）
	 *
	 * @param sParaTemp     请求参数数组
	 * @param action        方法
	 * @param strMethod     提交方式。两个值可选：post、get
	 * @param strButtonName 确认按钮显示文字
	 * @return 提交表单HTML文本
	 */
	public static String buildRequest(Map<String, Object> sParaTemp, String action, String strMethod, String strButtonName) {
		//待请求参数数组
		List<String> keys = new ArrayList<String>(sParaTemp.keySet());
		StringBuffer sbHtml = new StringBuffer();

		sbHtml.append("<form id=\"rppaysubmit\" name=\"rppaysubmit\" action=\"" + action + "\" method=\"" + strMethod
				+ "\">");

		for (int i = 0; i < keys.size(); i++) {
			String name = (String) keys.get(i);
			Object object = sParaTemp.get(name);
			String value = "";

			if (object != null) {
				value = String.valueOf(sParaTemp.get(name));
			}

			sbHtml.append("<input type=\"hidden\" name=\"" + name + "\" value=\"" + value + "\"/>");
		}

		//submit按钮控件请不要含有name属性
		sbHtml.append("<input type=\"submit\" value=\"" + strButtonName + "\" style=\"display:none;\"></form>");
		sbHtml.append("<script>document.forms['rppaysubmit'].submit();</script>");

		return sbHtml.toString();
	}


	/**
	 * 建立请求，以表单key=value形式构造（用于app余额支付）
	 *
	 * @param sParaTemp 请求参数数组
	 * @param action    方法
	 * @return
	 */
	public static String buildRequestForApp(Map<String, Object> sParaTemp, String action) {
		//待请求参数数组
		List<String> keys = new ArrayList<String>(sParaTemp.keySet());
		StringBuffer sbHtml = new StringBuffer();

		for (int i = 0; i < keys.size(); i++) {
			String name = (String) keys.get(i);
			Object object = sParaTemp.get(name);
			String value = "";

			if (object != null) {
				value = String.valueOf(sParaTemp.get(name));
			}

			if (i == (keys.size() - 1)) {//如果是最后一个
				sbHtml.append(name).append("=").append(value);
			} else {
				sbHtml.append(name).append("=").append(value).append("&");
			}
		}

		return action + "?" + sbHtml.toString();
	}


}
