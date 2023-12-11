/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.logistics.kuaidi100.contant;

import lombok.Getter;

/**
 * 签收状态
 * 0在途，1揽收，2疑难，3签收，4退签，5派件，6退回，7转投，10待清关，11清关中，12已清关，13清关异常，14拒签 等13个状态
 *
 * @author legendshop
 * @create: 2021/8/20 18:11
 */
@Getter
public enum QueryTrackStatusEnum {

	/**
	 *
	 */
	ON_THE_WAY("0", "在途"),

	COLLECT("1", "揽收"),

	DIFFICULT("2", "疑难"),

	SIGN_FOR("3", "签收"),

	WITHDRAW("4", "退签"),

	DISPATCH("5", "派件"),

	RETURN("6", "退回"),

	SWITCH("7", "转投"),

	PENDING_CUSTOMS_CLEARANCE("10", "待清关"),

	DURING_CUSTOMS_CLEARANCE("11", "清关中"),

	CLEARED("12", "已清关"),

	CUSTOMS_CLEARANCE_EXCEPTION("13", "清关异常"),

	REFUSAL("14", "拒签");


	private String value;

	private String desc;

	QueryTrackStatusEnum(String value, String desc) {
		this.value = value;
		this.desc = desc;
	}
}
