/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.query;

import cn.legendshop.jpaplus.support.PageParams;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * @author legendshop
 */
@Data
@Schema(description = "商家子账号搜索")
public class ShopSubUserQuery extends PageParams {


	@Schema(description = "商家Id")
	private Long shopUserId;

	@Schema(description = "员工账号")
	private String userAccount;


	@Schema(description = "是否可用")
	private Boolean delFlag;

	@Schema(description = "角色Id")
	private Long roleId;

	@Schema(description = "名称")
	private String username;


	private List<Long> subUserIds;

}
