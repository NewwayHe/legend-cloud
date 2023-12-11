/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.wechat.model.enums;

import lombok.Getter;

/**
 * 直播间状态。101：直播中，102：未开始，103已结束，104禁播，105：暂停，106：异常，107：已过期
 *
 * @author legendshop
 */
@Getter
public enum WeChatLiveRoomStatusEnum {

	/**
	 * 直播中
	 */
	LIVE_PROCESSING(101, "直播中"),

	/**
	 * 未开始
	 */
	LIVE_HAS_NOT_STARTED(102, "未开始"),

	/**
	 * 103已结束
	 */
	LIVE_OVER(103, "已结束"),

	/**
	 * 104禁播
	 */
	LIVE_BANNED(104, "禁播"),

	/**
	 * 暂停
	 */
	LIVE_TIME_OUT(105, "暂停"),

	/**
	 * 异常
	 */
	live_error(106, "异常"),

	/**
	 * 已过期
	 */
	LIVE_EXPIRED(107, "已过期");

	private Integer code;

	private String desc;

	WeChatLiveRoomStatusEnum(Integer code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public static WeChatLiveRoomStatusEnum fromCode(Integer code) {
		if (code != null) {
			for (WeChatLiveRoomStatusEnum statusEnum : WeChatLiveRoomStatusEnum.values()) {
				if (statusEnum.code.equals(code)) {
					return statusEnum;
				}
			}
		}
		return null;
	}
}
