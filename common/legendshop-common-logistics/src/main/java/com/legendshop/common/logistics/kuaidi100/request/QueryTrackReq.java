/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.logistics.kuaidi100.request;

import com.google.gson.Gson;
import com.legendshop.common.logistics.kuaidi100.utils.SignUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author legendshop
 * @Author: api.kuaidi100.com
 * @Date: 2020-07-14 15:56
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueryTrackReq {
	/**
	 * 我方分配给贵司的的公司编号, 点击查看账号信息
	 */
	private String customer;
	/**
	 * 签名， 用于验证身份， 按param + key + customer 的顺序进行MD5加密（注意加密后字符串要转大写）， 不需要“+”号
	 */
	private String sign;
	/**
	 * 其他参数组合成的json对象
	 */
	private QueryTrackParam param;

	public void setSign(String appKey) {
		this.sign = SignUtils.sign(new Gson().toJson(this.param) + appKey + this.customer);
	}
}
