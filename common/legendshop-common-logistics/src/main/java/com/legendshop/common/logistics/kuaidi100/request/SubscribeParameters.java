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
 * @Date: 2020-07-16 13:42
 */
@Data
public class SubscribeParameters {
	/**
	 * 回调接口的地址。如果需要在推送信息回传自己业务参数，可以在回调地址URL后面拼接上去，如示例中的orderId
	 * http://www.xxxxx.com/callback?orderId=123
	 */
	private String callbackurl;
	/**
	 * 签名用随机字符串。32位自定义字符串。添加该参数，则推送的时候会增加sign给贵司校验消息的可靠性
	 */
	private String salt;
	/**
	 * 添加此字段表示开通行政区域解析功能。0：关闭（默认），1：开通行政区域解析功能
	 */
	private String resultv2;
	/**
	 * 添加此字段且将此值设为1，则表示开始智能判断单号所属公司的功能，
	 * 开启后，company字段可为空,即只传运单号（number字段），我方收到后会根据单号判断出其所属的快递公司（即company字段）。
	 * 建议只有在无法知道单号对应的快递公司（即company的值）的情况下才开启此功能。
	 */
	private String autoCom;
	/**
	 * 添加此字段表示开启国际版
	 * 开启后，若订阅的单号（即number字段）属于国际单号，会返回出发国与目的国两个国家的跟踪信息;
	 * 本功能暂时只支持邮政体系（国际类的邮政小包、EMS）内的快递公司;
	 * 若单号我方识别为非国际单，即使添加本字段，也不会返回destResult元素组.
	 */
	private String interCom;
	/**
	 * 出发国家编码
	 */
	private String departureCountry;
	/**
	 * 出发国家快递公司的编码
	 */
	private String departureCom;
	/**
	 * 目的国家编码
	 */
	private String destinationCountry;
	/**
	 * 目的国家快递公司的编码
	 */
	private String destinationCom;
	/**
	 * 收件人或寄件人的手机号或固话（顺丰单号必填，也可以填写后四位，如果是固话，请不要上传分机号）
	 */
	private String phone;
}
