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
 * @Date: 2020-07-17 17:05
 */
@Data
public class PrintCloudParam {

	/**
	 * 业务类型，默认为10
	 */
	private String type;
	/**
	 * 电子面单客户账户或月结账号，需贵司向当地快递公司网点申请
	 */
	private String partnerId;
	/**
	 * 电子面单密码，需贵司向当地快递公司网点申请
	 */
	private String partnerKey;
	/**
	 * 收件网点名称,由快递公司当地网点分配，
	 * 若使用淘宝授权填入（taobao），使用菜鸟授权填入（cainiao）
	 */
	private String net;
	/**
	 * 快递公司的编码，一律用小写字母
	 */
	private String kuaidicom;
	/**
	 * 收件人信息
	 */
	private ManInfo recMan;
	/**
	 * 收件人信息
	 */
	private ManInfo sendMan;
	/**
	 * 物品名称(部分快递公司必填)
	 */
	private String cargo;
	/**
	 * 物品总数量。
	 * 另外该属性与子单有关，如果需要子单（指同一个订单打印出多张电子面单，即同一个订单返回多个面单号），
	 * needChild = 1、count 需要大于1，如count = 2 则一个主单 一个子单，
	 * count = 3则一个主单 二个子单；返回的子单号码见返回结果的childNum字段
	 */
	private String count;
	/**
	 * 物品总重量，单位：KG （例子：0.5）
	 */
	private String weight;
	/**
	 * 支付方式：
	 * SHIPPER:寄方付（默认）
	 * CONSIGNEE:到付
	 * MONTHLY:月结
	 * THIRDPARTY:第三方支付
	 */
	private String payType;
	/**
	 * 快递类型：
	 * 标准快递（默认）
	 * 顺丰特惠
	 * EMS经济
	 */
	private String expType;
	/**
	 * 备注
	 */
	private String remark;
	/**
	 * 电子面单模板编码
	 */
	private String tempid;
	/**
	 * 打印设备编码。通过打印机输出的设备码进行获取
	 */
	private String siid;
	/**
	 * 保价额度
	 */
	private String valinsPay;
	/**
	 * 代收货款额度
	 */
	private String collection;
	/**
	 * 是否需要子单(支持子单的快递公司才可以用，是否支持可以参考参数字典)
	 * 1:需要
	 * 0:不需要(默认)
	 * 如果需要子单（指同一个订单打印出多张电子面单，即同一个订单返回多个面单号）；
	 * needChild = 1、count 需要大于1，如count = 2 一个主单 一个子单，
	 * count = 3 一个主单 二个子单，返回的子单号码见返回结果的childNum字段
	 */
	private String needChild;
	/**
	 * 是否需要回单(支持回单的快递公司才可以用，是否支持可以参考参数字典)
	 * 1:需要
	 * 0:不需要(默认)
	 * 返回的回单号见返回结果的returnNum字段
	 */
	private String needBack;
	/**
	 * 贵司内部自定义的订单编号,需要保证唯一性
	 */
	private String orderId;
	/**
	 * 生成图片的高，以mm为单位(默认100)
	 */
	private String height;
	/**
	 * 生成图片的宽，以mm为单位（默认180）
	 */
	private String width;
	/**
	 * 签名用随机字符串
	 */
	private String salt;
	/**
	 * 是否开启订阅功能：
	 * 0：不开启(默认)
	 * 1：开启
	 * 说明开启订阅功能时：pollCallBackUrl必须填入
	 * 此功能只针对有快递单号的单
	 */
	private String op;
	/**
	 * 如果op设置为1时，pollCallBackUrl必须填入，用于跟踪回调
	 */
	private String pollCallBackUrl;
	/**
	 * 添加此字段表示开通行政区域解析功能：0：关闭（默认）；1：开通行政区域解析功能
	 */
	private String resultv2;
	/**
	 * 扩展字段，不同快递公司具体详情看参数字典
	 * https://api.kuaidi100.com/help/doc/?code=5f0ff6e82977d50a94e10237&openKey=%E7%94%B5%E5%AD%90%E9%9D%A2%E5%8D%95
	 */
	private String code;
	/**
	 * 扩展字段，具体详情看参数字典
	 * https://api.kuaidi100.com/help/doc/?code=5f0ff6e82977d50a94e10237&openKey=%E7%94%B5%E5%AD%90%E9%9D%A2%E5%8D%95
	 */
	private String partnerSecret;
	/**
	 * 扩展字段，具体详情看参数字典
	 * https://api.kuaidi100.com/help/doc/?code=5f0ff6e82977d50a94e10237&openKey=%E7%94%B5%E5%AD%90%E9%9D%A2%E5%8D%95
	 */
	private String partnerName;
	/**
	 * 扩展字段，具体详情看参数字典
	 * https://api.kuaidi100.com/help/doc/?code=5f0ff6e82977d50a94e10237&openKey=%E7%94%B5%E5%AD%90%E9%9D%A2%E5%8D%95
	 */
	private String checkMan;

	/**
	 * 打印方向（默认0） 0-正方向 1-反方向
	 */
	private String direction;
}
