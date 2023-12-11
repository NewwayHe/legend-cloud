/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.entity;


import cn.legendshop.jpaplus.persistence.*;
import cn.legendshop.jpaplus.support.GenericEntity;
import com.legendshop.common.core.dto.BaseEntity;
import lombok.Data;

import java.util.Date;

/**
 * 导出详情记录表(ExeclDetail)实体类
 *
 * @author legendshop
 * @since 2021-12-14 19:05:28
 */
@Data
@Entity
@Table(name = "ls_export_excel_task")
public class ExportExcelTask extends BaseEntity implements GenericEntity<Long> {

	private static final long serialVersionUID = -30488327250109846L;

	/**
	 * 主键
	 */
	@Column(name = "id")
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
	@TableGenerator(name = "generator", pkColumnValue = "EXECLDETAIL_SEQ")
	private Long id;

	/**
	 * 文件名称
	 */
	@Column(name = "file_name")
	private String fileName;

	/**
	 * 用户类型(U:普通用户 S:商家 A:平台)
	 */
	@Column(name = "user_type")
	private String userType;

	/**
	 * 用户id
	 */
	@Column(name = "user_id")
	private Long userId;

	/**
	 * 操作用户名字
	 */
	@Column(name = "user_name")
	private String userName;

	/**
	 * 状态(-1:导出失败, 10:正在导出 20:导出成功)
	 * ExportExcelStatusEnum
	 */
	@Column(name = "status")
	private Integer status;

	/**
	 * 业务名称
	 */
	@Column(name = "business_name")
	private String businessName;

	/**
	 * 业务类型
	 */
	@Column(name = "business_type")
	private String businessType;

	/**
	 * 下载链接
	 */
	@Column(name = "url")
	private String url;

	/**
	 * 备注
	 */
	@Column(name = "remark")
	private String remark;

	/**
	 * 创建时间
	 */
	@Column(name = "create_time")
	private Date createTime;

	/**
	 * 更新时间
	 */
	@Column(name = "update_time")
	private Date updateTime;

}
