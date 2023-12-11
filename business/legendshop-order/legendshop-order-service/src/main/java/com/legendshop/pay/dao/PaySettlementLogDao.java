/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.dao;

import cn.legendshop.jpaplus.GenericDao;
import com.legendshop.pay.entity.PaySettlementLog;

/**
 * 支付单日志表【主要记录支付单异常记录，协助分析用户支付行为，以防支异常或攻击】(PaySettlementLog)表数据库访问层
 *
 * @author legendshop
 * @since 2022-04-28 11:48:11
 */
public interface PaySettlementLogDao extends GenericDao<PaySettlementLog, Long> {


}
