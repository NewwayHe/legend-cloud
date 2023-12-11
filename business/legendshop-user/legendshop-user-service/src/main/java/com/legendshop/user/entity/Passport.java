/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.entity;

import cn.legendshop.jpaplus.persistence.*;
import cn.legendshop.jpaplus.support.GenericEntity;
import com.legendshop.common.core.dto.BaseEntity;
import lombok.Data;

/**
 * 用户第三方通行证 （微信、QQ等）
 * 类型 + 来源 + passId 可以对应到唯一记录
 * user_id : (pass_id , type , source)
 * 例：微信 ： pass_id (openId) + type (weChat) + source (H5)
 *
 * @author legendshop
 */
@Data
@Entity
@Table(name = "ls_passport")
public class Passport extends BaseEntity implements GenericEntity<Long> {

	private static final long serialVersionUID = 7066679491899680228L;
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
	@TableGenerator(name = "generator", pkColumnValue = "PASSPORT_SEQ")
	private Long id;

	@Column(name = "user_id")
	private Long userId;

	@Column(name = "pass")
	private String pass;

	@Column(name = "type")
	private String type;

	@Column(name = "nick_name")
	private String nickName;

	@Column(name = "head_portrait_url")
	private String headPortraitUrl;

	@Column(name = "sex")
	private String sex;

	@Column(name = "city")
	private String city;

	@Column(name = "province")
	private String province;

	@Column(name = "country")
	private String country;


}
