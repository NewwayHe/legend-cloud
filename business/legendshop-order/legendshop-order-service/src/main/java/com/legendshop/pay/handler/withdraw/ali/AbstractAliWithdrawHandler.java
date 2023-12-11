/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.handler.withdraw.ali;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alipay.api.AlipayClient;
import com.alipay.api.CertAlipayRequest;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayFundTransUniTransferRequest;
import com.alipay.api.response.AlipayFundTransUniTransferResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.legendshop.basic.enums.SysParamNameEnum;
import com.legendshop.common.core.config.sys.params.AliPayConfig;
import com.legendshop.common.core.constant.R;
import com.legendshop.pay.dto.AliWithdrawDTO;
import com.legendshop.pay.dto.UserWithdrawDTO;
import com.legendshop.pay.handler.withdraw.AbstractWithdrawHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * 支付宝提现抽象
 *
 * @author legendshop
 */
@Slf4j
public abstract class AbstractAliWithdrawHandler extends AbstractWithdrawHandler {

	@Override
	protected R<Void> userWithdrawHandler(UserWithdrawDTO dto) {
		try {
			//设置支付宝提现参数
			AliWithdrawDTO aliWithdrawDTO = new AliWithdrawDTO();
			setAliWithdrawDTO(aliWithdrawDTO, dto);

			R<AliPayConfig> payConfigR = this.sysParamsApi.getConfigDtoByParamName(SysParamNameEnum.ALI_PAY_TRANSFER.name(), AliPayConfig.class);

			log.info("支付宝提现配置参数， {} ", JSONUtil.parse(payConfigR));

			ObjectMapper mapper = new ObjectMapper();
			AliPayConfig payConfig = mapper.convertValue(payConfigR.getData(), AliPayConfig.class);
			//构造client
			CertAlipayRequest certAlipayRequest = new CertAlipayRequest();
			//设置网关地址
			certAlipayRequest.setServerUrl("https://openapi.alipay.com/gateway.do");
			//设置应用Id
			certAlipayRequest.setAppId(payConfig.getAppId());
			//设置应用私钥
			certAlipayRequest.setPrivateKey(payConfig.getRsaPrivate());
			//设置请求格式，固定值json
			certAlipayRequest.setFormat("json");
			//设置字符集
			certAlipayRequest.setCharset("GBK");
			//设置签名类型
			certAlipayRequest.setSignType("RSA2");
			//设置应用公钥证书路径
			certAlipayRequest.setCertPath(payConfig.getMerCertPath());
			//设置支付宝公钥证书路径
			certAlipayRequest.setAlipayPublicCertPath(payConfig.getAliCertPath());
			//设置支付宝根证书路径
			certAlipayRequest.setRootCertPath(payConfig.getAliRootCertPath());
			//构造client
			AlipayClient alipayClient = new DefaultAlipayClient(certAlipayRequest);

			// 新接口
			AlipayFundTransUniTransferRequest request = new AlipayFundTransUniTransferRequest();
			request.setBizContent("{" +
					"    \"out_biz_no\":\"" + aliWithdrawDTO.getOut_biz_no() + "\"," +
					"    \"trans_amount\":\"" + aliWithdrawDTO.getAmount() + "\"," +
					"    \"product_code\":\"TRANS_ACCOUNT_NO_PWD\"," +
					"    \"biz_scene\":\"DIRECT_TRANSFER\"," +
					"    \"order_title\":\"" + aliWithdrawDTO.getPayer_show_name() + "\"," +
					"  \"payee_info\":{" +
					"    \"identity\":\"" + aliWithdrawDTO.getPayee_account() + "\"," +
					"    \"identity_type\":\"ALIPAY_LOGON_ID\"," +
					"    \"name\":\"" + aliWithdrawDTO.getPayee_real_name() + "\"" +
					"  }," +
					"    \"remark\":\"" + aliWithdrawDTO.getRemark() + "\"," +
					"}");
			log.info("支付宝提现前参数， {} | {}", JSONUtil.parse(aliWithdrawDTO), JSONUtil.parse(request));
			AlipayFundTransUniTransferResponse response = alipayClient.certificateExecute(request);
			log.info("支付宝提现后响应参数， {}", JSONUtil.parse(response));
			if (response.isSuccess()) {
				return R.ok();
			} else {
				return R.fail(StrUtil.emptyToDefault(response.getSubMsg(), "支付宝提现失败！"));
			}

		} catch (Exception e) {
			log.info("支付宝提现失败！{}", e.getMessage());
			return R.fail("支付宝提现失败！" + StrUtil.emptyToDefault(e.getMessage(), ""));
		}
	}

	/**
	 * 设置提现DTO
	 *
	 * @param aliWithdrawDTO
	 * @param dto
	 * @return
	 */
	private void setAliWithdrawDTO(AliWithdrawDTO aliWithdrawDTO, UserWithdrawDTO dto) {

		//获取支付宝配置
		R<AliPayConfig> aliPayConfigR = this.sysParamsApi.getConfigDtoByParamName(SysParamNameEnum.ALI_PAY_TRANSFER.name(), AliPayConfig.class);
		AliPayConfig config = this.mapper.convertValue(aliPayConfigR.getData(), AliPayConfig.class);
		aliWithdrawDTO.setApp_id(config.getAppId());
		aliWithdrawDTO.setPrivate_key(config.getRsaPrivate());
		aliWithdrawDTO.setAlipay_public_key(config.getRsaPublish());

		aliWithdrawDTO.setAmount(dto.getAmount().doubleValue());
		aliWithdrawDTO.setPayee_account(dto.getAccount());
		aliWithdrawDTO.setPayee_real_name(dto.getRealName());
		aliWithdrawDTO.setPayee_type(dto.getAccount());
		aliWithdrawDTO.setRemark("支付宝提现");
		aliWithdrawDTO.setPayer_show_name(config.getPayerShowName());
		aliWithdrawDTO.setOut_biz_no(dto.getWithdrawSerialNo().toString());
	}
}
