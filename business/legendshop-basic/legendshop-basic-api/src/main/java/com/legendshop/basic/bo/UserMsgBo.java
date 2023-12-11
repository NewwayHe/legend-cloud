/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.bo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author legendshop
 */
@Data
@Schema(description = "PC端用户消息")
public class UserMsgBo {
	/**
	 * 我的公告未读数
	 */
	@Schema(description = "我的公告未读数")
	private Integer noticeUnreadMsgCount;

	/**
	 * 系统通知未读数
	 */
	@Schema(description = "系统通知未读数")
	private Integer systemUnreadMsgCount;

	/**
	 * 点赞收藏未读数
	 */
	@Schema(description = "点赞收藏未读数")
	private Integer thumbUpToCollectCount;

	/**
	 * 文章评论未读数
	 */
	@Schema(description = "文章评论未读数")
	private Integer articleCommentUnReadCount;

	@Schema(description = "总未读数")
	private Integer totalUnreadMsgCount;
}
