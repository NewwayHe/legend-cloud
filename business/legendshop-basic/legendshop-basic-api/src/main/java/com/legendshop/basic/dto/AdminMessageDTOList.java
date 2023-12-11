/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 后台消息实体实体列表.
 *
 * @author legendshop
 */
@Data
public class AdminMessageDTOList implements Serializable {

	private static final long serialVersionUID = 4503049789126128241L;


	private Integer totalMessage;


	private List<AdminMessageDTO> list;


}
