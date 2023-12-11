/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.service.impl;

import com.legendshop.basic.api.FileApi;
import com.legendshop.basic.bo.LngAndLatBO;
import com.legendshop.basic.dto.SmsLogDTO;
import com.legendshop.basic.util.MapUtil;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.log.annotation.SystemLog;
import feign.Response;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.*;

/**
 * @author legendshop
 */
@Slf4j
@RestController
public class TestController {

	@Autowired
	private MapUtil mapUtil;

	private FileApi fileApi;

	@SystemLog(value = "日志测试")
	@PostMapping(value = "/test")
	public R<Void> test(@RequestBody SmsLogDTO smsLogDTO, @RequestParam(value = "name") String name) {
		log.info("basic test name :{} ", name);
		log.info("smsLog :{} ", smsLogDTO);
		return R.ok();
	}

	@GetMapping(value = "/test/a/map")
	public R<LngAndLatBO> testMap(@RequestParam(value = "address") String address) {
		log.info("basic test address :{} ", address);
		LngAndLatBO lngAndLatBO = mapUtil.getLngAndLatBO(address);
		return R.ok(lngAndLatBO);
	}


	@GetMapping(value = "/test/a/map/address")
	public R<LngAndLatBO> testMap(@RequestParam String longitude, @RequestParam String latitude) {
		LngAndLatBO address = mapUtil.getAddress(longitude, latitude);
		return R.ok(address);
	}

	@GetMapping(value = "/downloadtest")
	@PreAuthorize("@pms.hasPermission('admin_group_del')")
	@Operation(summary = "【公共】文件下载1")
	public void downloadtest(@RequestParam(value = "fileName") String fileName, @RequestParam(value = "scope", defaultValue = "PUBLIC") String scope, HttpServletResponse servletResponse) throws IOException {
		Response download = fileApi.download(fileName, scope);
		Response.Body body = download.body();

		InputStream is = body.asInputStream();
		byte[] bs = new byte[1024];
		// 读取到的数据长度
		int len;
		// 输出的文件流(后缀名)
		File sf = new File("D://aaa.p12");
		if (!sf.exists()) {
			sf.mkdirs();
		}
		// 新的图片文件名 = 编号 +"."图片扩展名
		OutputStream os = new FileOutputStream(sf.getPath() + "\\" + sf.getName());
		// 开始读取
		while ((len = is.read(bs)) != -1) {
			os.write(bs, 0, len);
		}
		// 完毕，关闭所有链接
		os.close();
		is.close();
		System.out.println("程序执行完毕");
	}
}
