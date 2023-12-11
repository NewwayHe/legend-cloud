/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.dto;


import com.legendshop.basic.enums.MsgSendTypeEnum;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 站内信(Msg)实体类
 *
 * @author legendshop
 */
@Data
public class MsgDTO implements Serializable {

	private static final long serialVersionUID = 5718311401741756020L;

	/**
	 * ID
	 */
	private Long id;


	/**
	 * 发送人用户id
	 */
	private Long sendUserId;

	/**
	 * 内容
	 * 注：这里只需要替换的内容，比如商品到货通知，只需要传该商品的名称即可
	 */
	/**
	 * 模板替换的内容
	 */
	private List<MsgSendParamDTO> dataDTOList;

	/**
	 * 接受人
	 */
	Long[] receiveUserIds;

	/**
	 * 系统通知消息类型
	 */
	private MsgSendTypeEnum msgSendTypeEnum;

	/**
	 * 详情id
	 */
	private Long detailId;

	/**
	 * 消息ID
	 */
	private Long textId;


	/**
	 * 状态，已读1， 未读0
	 */
	private Integer status;


	/**
	 * 用户级别
	 */
	private String userLevel;


	/**
	 * 是否全局消息
	 */
	private Boolean globalFlag;


	/**
	 * 发件人 删除状态
	 */
	private Integer deleteStatus;


	/**
	 * 收件人删除状态
	 */
	private Integer deleteStatus2;


	/**
	 * 标题
	 */
	private String title;

	/**
	 * 内容
	 */
	private String content;

	private Date recDate;
}
