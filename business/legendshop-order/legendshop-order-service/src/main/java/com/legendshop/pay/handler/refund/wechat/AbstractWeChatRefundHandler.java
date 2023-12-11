/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.handler.refund.wechat;

import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ijpay.core.enums.SignType;
import com.ijpay.core.kit.WxPayKit;
import com.ijpay.wxpay.WxPayApi;
import com.ijpay.wxpay.model.RefundModel;
import com.ijpay.wxpay.model.RefundQueryModel;
import com.legendshop.basic.api.FileApi;
import com.legendshop.basic.api.SysParamsApi;
import com.legendshop.basic.api.WxApi;
import com.legendshop.basic.enums.FileScopeEnum;
import com.legendshop.basic.enums.SysParamNameEnum;
import com.legendshop.common.core.config.sys.params.WxPayConfig;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.oss.http.OssService;
import com.legendshop.common.oss.properties.OssProperties;
import com.legendshop.pay.dto.RefundRequestDTO;
import com.legendshop.pay.dto.RefundResultItemDTO;
import com.legendshop.pay.handler.refund.RefundHandler;
import com.legendshop.pay.service.PaySettlementItemService;
import feign.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

/**
 * @author legendshop
 */
@Slf4j
public abstract class AbstractWeChatRefundHandler implements RefundHandler {

	@Autowired
	protected WxApi wxApi;

	protected WxPayConfig config;

	@Autowired
	protected ObjectMapper mapper;

	@Autowired
	private FileApi fileApi;

	@Autowired
	private OssService ossService;

	@Autowired
	private OssProperties ossProperties;

	@Autowired
	protected SysParamsApi sysParamsApi;

	@Autowired
	protected PaySettlementItemService paySettlementItemService;

	@Override
	public R<RefundResultItemDTO> refund(RefundRequestDTO refundItem) {
		log.info(" [ Refund WeChat ] 进入微信退款, param={}", JSONUtil.toJsonStr(refundItem));

		R<WxPayConfig> configR = wxApi.getPayConfig();
		config = configR.getData();
		if (null == config) {
			log.error(" [ Refund WeChat ] 微信支付配置错误！");
			return R.fail("配置错误！");
		}

		if (null == config.getEnabled() || !config.getEnabled()) {
			log.error(" [ Refund WeChat ] 微信支付尚未启用！");
			return R.fail("微信支付尚未启用！");
		}
		this.paySettlementItemService.queryBySn(refundItem.getNumber());

		String appId = getAppId();
		String mchId = config.getMchId();
		String partnerKey = config.getPartnerKey();
		String refundPath = config.getRefundFile();
		SignType signType = getSignType();
		String nonceStr = RandomStringUtils.randomNumeric(8);
		String outRefundNo = refundItem.getRefundSn();
		String settlementSn = refundItem.getNumber();

		String totalFee = String.valueOf(refundItem.getOrderAmount().setScale(2, RoundingMode.HALF_UP)
				.multiply(new BigDecimal(100)).intValue());


		String refundFee = String.valueOf(refundItem.getRefundAmount().setScale(2, RoundingMode.HALF_UP)
				.multiply(new BigDecimal(100)).intValue());

		if (StringUtils.isBlank(refundPath)) {
			log.error(" [ Refund WeChat ] 微信支付配置错误，未上传退款文件");
			return R.fail("配置错误！");
		}

		// 拼接文件全路劲
//		refundPath = ossProperties.getEndpoint() + "/" + ossProperties.getBucketName() + "/" + refundPath;


		// REFUND_SOURCE_UNSETTLED_FUNDS---未结算资金退款（默认使用未结算资金退款）
		// REFUND_SOURCE_RECHARGE_FUNDS---可用余额退款
		String refundAccount = "REFUND_SOURCE_UNSETTLED_FUNDS";
		Map<String, String> params = RefundModel.builder()
				.appid(appId)
				.mch_id(mchId)
				.nonce_str(nonceStr)
				.out_trade_no(settlementSn)
				.out_refund_no(outRefundNo)
				.total_fee(totalFee)
				.refund_fee(refundFee)
				.refund_desc("微信支付退款！")
				.sign_type(signType.getType())
				.refund_account(refundAccount).build()
				.createSign(partnerKey, signType);

		try (
				// 获取私有证书
				Response refundFileResponse = fileApi.download(refundPath, FileScopeEnum.PRIVATE.name());
				Response.Body body = refundFileResponse.body();
				InputStream inputStream = body.asInputStream();

				// 获取公共读证书
				// InputStream inputStream = download(refundPath, FileScopeEnum.PUBLIC.name())
		) {

			// 发送请求
			String xmlResult = WxPayApi.orderRefund(config.getEnabledSandbox() != null && config.getEnabledSandbox(), params, inputStream, mchId);
			// 将请求返回的 xml 数据转为 Map，方便后面逻辑获取数据
			Map<String, String> resultMap = WxPayKit.xmlToMap(xmlResult);
			// 判断返回的结果
			String returnCode = resultMap.get("return_code");
			String returnMsg = resultMap.get("return_msg");
			if (!WxPayKit.codeIsOk(returnCode)) {
				log.error(" [ Refund WeChat ] 微信退款失败 returnCode：{}, returnMsg: {}", returnCode, returnMsg);
				return R.fail(returnMsg);
			}
			String resultCode = resultMap.get("result_code");
			String errCodeDes = resultMap.get("err_code_des");
			if (!WxPayKit.codeIsOk(resultCode)) {
				log.error(" [ Refund WeChat ] 微信退款失败 returnCode：{}, returnMsg: {}, resultCode: {}, errCodeDes: {}", returnCode, returnMsg, resultCode, errCodeDes);
				return R.fail(errCodeDes);
			}

			// 微信退款订单号
			String refundNo = resultMap.get("refund_id");

			RefundResultItemDTO refundResultItem = new RefundResultItemDTO();
			refundResultItem.setExternalRefundSn(refundNo);
			refundResultItem.setPayTypeId(SysParamNameEnum.WX_PAY.name());
			refundResultItem.setAmount(refundItem.getRefundAmount());

			return R.ok(refundResultItem);
		} catch (Exception e) {
			e.printStackTrace();
			if (e instanceof IOException) {
				log.error(" [ Refund WeChat ] 微信退款失败,请检查api证书文件~！");
			}
			return R.fail("退款失败");
		}
	}

	@Override
	public R<Void> queryRefundResult(RefundRequestDTO refundItem) {

		String partnerKey = config.getPartnerKey();
		SignType signType = getSignType();
		String nonceStr = RandomStringUtils.randomNumeric(8);
		Map<String, String> params = RefundQueryModel
				.builder()
				.appid(getAppId())
				.mch_id(config.getMchId())
				.out_refund_no(refundItem.getRefundSn())
				.nonce_str(nonceStr)
				.sign_type(signType.getType())
				.build().createSign(partnerKey, signType);

		String xmlResult = WxPayApi.orderRefundQuery(config.getEnabledSandbox() != null && config.getEnabledSandbox(), params);
		// 将请求返回的 xml 数据转为 Map，方便后面逻辑获取数据
		Map<String, String> resultMap = WxPayKit.xmlToMap(xmlResult);
		// 判断返回的结果
		String returnCode = resultMap.get("return_code");
		String returnMsg = resultMap.get("return_msg");
		if (!WxPayKit.codeIsOk(returnCode)) {
			log.error(" [ Refund WeChat ] 微信退款查询失败 returnCode：{}, returnMsg: {}", returnCode, returnMsg);
			return R.fail(returnMsg);
		}
		String resultCode = resultMap.get("result_code");
		String errCodeDes = resultMap.get("err_code_des");
		if (!WxPayKit.codeIsOk(resultCode)) {
			log.error(" [ Refund WeChat ] 微信退款查询失败 returnCode：{}, returnMsg: {}, resultCode: {}, errCodeDes: {}", returnCode, returnMsg, resultCode, errCodeDes);
			return R.fail(errCodeDes);
		}

		// 获取退款次数
		String refundCount = resultMap.get("refund_count");

		// 获取退款状态
		String refundStatus = resultMap.get("refund_status_" + (Integer.parseInt(refundCount) - 1));
		if ("SUCCESS".equals(refundStatus)) {
			return R.ok();
		}
		String refundErrorMsg = "";
		switch (refundStatus) {
			case "REFUNDCLOSE":
				refundErrorMsg = "退款关闭！";
				break;
			case "PROCESSING":
				refundErrorMsg = "退款处理中";
				break;
			case "CHANGE":
				refundErrorMsg = "退款异常，请联系系统管理员";
				break;
			default:
				refundErrorMsg = "退款失败，请联系系统管理员";
		}
		return R.fail(refundErrorMsg);
	}

	/**
	 * AppId
	 */
	public abstract String getAppId();

	/**
	 * 获取加密类型
	 *
	 * @return SignType
	 */
	protected abstract SignType getSignType();

	private InputStream download(String fileName, String scope) {
		String bucketName = this.ossProperties.getBucketName();
		if (FileScopeEnum.PRIVATE.equals(FileScopeEnum.valueOf(scope))) {
			bucketName = this.ossProperties.getPrivateBucketName();
		}
		return this.ossService.getObject(bucketName, fileName);
	}
}
