/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.excel;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author legendshop
 */
@Data
public class UserDetailExportDTO implements Serializable {

	private String nickName;

	private String realname;

	private String sex;

	private Date birthday;

	private String idCard;

	private String interest;

	private String email;

	private String addr;

	private String mobile;

	private String qq;

	private Long score;

	private Double coin;

	private Double totalCash;

	private Date modifyTime;

	private Date regTime;

	private Date lastTime;
}
