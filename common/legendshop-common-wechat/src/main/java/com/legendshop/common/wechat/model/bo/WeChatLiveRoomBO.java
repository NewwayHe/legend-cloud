/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.wechat.model.bo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 微信小程序直播响应
 *
 * @author legendshop
 */
@Data
public class WeChatLiveRoomBO implements Serializable {

	private static final long serialVersionUID = 7377683083791293402L;

	/**
	 * 直接房间名
	 */
	private String name;

	/**
	 * 直播间ID
	 */
	private Long roomId;

	/**
	 * 直播间背景图链接
	 */
	private String coverImg;

	/**
	 * 直播间分享图链接
	 */
	private String shareImg;

	/**
	 * 直播间状态。101：直播中，102：未开始，103已结束，104禁播，105：暂停，106：异常，107：已过期
	 */
	private Integer liveStatus;

	/**
	 * 直播间状态详情
	 */
	private String liveStatusDesc;

	/**
	 * 直播间开始时间，列表按照start_time降序排列
	 */
	private Date startTime;

	/**
	 * 直播计划结束时间
	 */
	private Date endTime;

	/**
	 * 主播名
	 */
	private String anchorName;

	/**
	 * 商品列表
	 */
	private List<Goods> goods;

	/**
	 * 商品
	 */
	@Data
	public static class Goods implements Serializable {

		private static final long serialVersionUID = 7706136451877050865L;

		/**
		 * 商品封面图链接
		 */
		private String coverImg;

		/**
		 * 商品小程序路径
		 */
		private String url;

		/**
		 * 商品价格
		 */
		private BigDecimal price;

		/**
		 * 商品名称
		 */
		private String name;
	}
}
