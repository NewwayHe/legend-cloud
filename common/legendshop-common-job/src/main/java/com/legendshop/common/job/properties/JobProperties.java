/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.job.properties;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import static com.legendshop.common.job.properties.JobProperties.PREFIX;

/**
 * admin和executor的组合
 *
 * @author legendshop
 */
@Data
@ConfigurationProperties(prefix = PREFIX)
public class JobProperties {

	public static final String PREFIX = "legendshop.job";
	/**
	 * 是否启用分布式调度任务，默认：开启
	 */
	private boolean enabled = Boolean.TRUE;

	private XxlJobAdminProperties admin = new XxlJobAdminProperties();

	private XxlJobExecutorProperties executor = new XxlJobExecutorProperties();


	@Getter
	@Setter
	public static class XxlJobAdminProperties {
		/**
		 * 调度中心地址，如调度中心集群部署存在多个地址则用逗号分隔。执行器将会使用该地址进行"执行器心跳注册"和"任务结果回调"；为空则关闭自动注册；支持配置，{@code lb:// + ${service_name}} 从注册中心动态获取地址
		 */
		private String address;

		/**
		 * 与调度中心交互的accessToken
		 */
		private String accessToken;

		/**
		 * job admin 的 context-path
		 */
		private String contextPath;
	}


	@Getter
	@Setter
	public static class XxlJobExecutorProperties {
		/**
		 * 执行器名称，执行器心跳注册分组依据；为空则关闭自动注册
		 */
		private String appName;

		/**
		 * 执行器 IP，默认为空表示自动获取IP，多网卡时可手动设置指定IP，该IP不会绑定Host仅作为通讯实用；地址信息用于 "执行器注册" 和 "调度中心请求并触发任务"
		 */
		private String ip;

		/**
		 * 执行器端口，小于等于0则自动获取；默认端口为9999，单机部署多个执行器时，注意要配置不同执行器端口
		 */
		private int port = -1;

		/**
		 * 执行器日志位置
		 */
		private String logPath;

		/**
		 * 执行器日志保留天数，默认值：-1，值大于3时生效，启用执行器Log文件定期清理功能，否则不生效
		 */
		private int logRetentionDays = -1;
	}
}
