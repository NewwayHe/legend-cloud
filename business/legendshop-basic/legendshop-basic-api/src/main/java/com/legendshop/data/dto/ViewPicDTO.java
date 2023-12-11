/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.data.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author legendshop
 */
@Data
@Schema(description = "运营概况访问统计")
public class ViewPicDTO implements Serializable {

	private static final long serialVersionUID = -9155481134399156243L;

	@Schema(description = "访问商城次数")
	private Integer pv;

	@Schema(description = "访问商城用户数")
	private Integer uv;

	@Schema(description = "时间")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date createTime;

}
