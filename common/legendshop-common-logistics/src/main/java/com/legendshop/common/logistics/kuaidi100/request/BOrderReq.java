/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.logistics.kuaidi100.request;

import lombok.Data;

/**
 * @author legendshop
 * @Author: api.kuaidi100.com
 * @Date: 2020-09-17 14:33
 */
@Data
public class BOrderReq {

	/**
	 * 快递公司的编码，一律用小写字母，见《快递公司编码》
	 */
	private String kuaidicom;
	/**
	 * 收件人姓名
	 */
	private String recManName;
	/**
	 * 收件人的手机号，手机号和电话号二者其一必填
	 */
	private String recManMobile;
	/**
	 * 收件人所在完整地址，如广东深圳市深圳市南山区科技南十二路2号金蝶软件园
	 */
	private String recManPrintAddr;
	/**
	 * 寄件人姓名
	 */
	private String sendManName;
	/**
	 * 寄件人的手机号，手机号和电话号二者其一必填
	 */
	private String sendManMobile;
	/**
	 * 寄件人所在的完整地址，如广东深圳市深圳市南山区科技南十二路2号金蝶软件园B10
	 */
	private String sendManPrintAddr;
	/**
	 * 物品名称,例：文件
	 */
	private String cargo;
	/**
	 * 物品总重量KG，例：1.5，单位kg
	 */
	private String weight;
	/**
	 * 备注
	 */
	private String remark;
	/**
	 * 签名用随机字符串
	 */
	private String salt;
	/**
	 * callBackUrl订单信息回调
	 */
	private String callBackUrl;
	/**
	 * 快递业务服务类型，例：标准快递，默认为标准快递
	 */
	private String serviceType;
}
