/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.core.sensitive;


import cn.hutool.core.util.StrUtil;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

/**
 * 脱敏字段处理的工具类
 *
 * @author legendshop
 */
@UtilityClass
public class SensitiveInfoUtil {


	/**
	 * 【处理手机号】 138*****000
	 *
	 * @param mobile
	 * @return
	 */
	public String mobilePhone(final String mobile) {
		if (StrUtil.isBlank(mobile)) {
			return "";
		}
		return StringUtils.
				left(mobile, 3).
				concat(StringUtils.removeStart(StringUtils.leftPad(StringUtils.right(mobile, 3), StringUtils.length(mobile), "*"), "***"));
	}

	/**
	 * 【身份证号】显示前六位和最后四位
	 *
	 * @param idCardNum
	 * @return
	 */
	public String idCardNum(final String idCardNum) {
		if (StringUtils.isBlank(idCardNum)) {
			return "";
		}
		return StringUtils.left(idCardNum, 6).concat(StringUtils.removeStart(StringUtils.leftPad(StringUtils.right(idCardNum, 4), StringUtils.length(idCardNum), "*"), "*"));
	}

	/**
	 * 【银行卡号】前4位，后3位，其他用星号隐藏每位1个星号，比如：6217 **** **** **** 567>
	 *
	 * @param bankCardNum
	 * @return
	 */
	public String bankCard(String bankCardNum) {
		if (StringUtils.isBlank(bankCardNum)) {
			return "";
		}
		return StringUtils.left(bankCardNum, 4).concat(StringUtils.removeStart(StringUtils.leftPad(StringUtils.right(bankCardNum, 3), StringUtils.length(bankCardNum), "*"), "****"));
	}

	/**
	 * 【密码】密码的全部字符都用*代替，比如：******
	 *
	 * @param password
	 * @return
	 */
	public String password(String password) {
		if (StringUtils.isBlank(password)) {
			return "";
		}
		String pwd = StringUtils.left(password, 0);
		return StringUtils.rightPad(pwd, StringUtils.length(password), "*");
	}
}
