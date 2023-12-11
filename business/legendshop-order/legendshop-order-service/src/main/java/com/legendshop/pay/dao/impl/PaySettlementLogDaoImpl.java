/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.dao.impl;

import cn.legendshop.jpaplus.impl.GenericDaoImpl;
import com.legendshop.pay.dao.PaySettlementLogDao;
import com.legendshop.pay.entity.PaySettlementLog;
import org.springframework.stereotype.Repository;

/**
 * 支付单日志表【主要记录支付单异常记录，协助分析用户支付行为，以防支异常或攻击】(PaySettlementLog)表数据库访问层实现类
 *
 * @author legendshop
 * @since 2022-04-28 11:48:11
 */
@Repository
public class PaySettlementLogDaoImpl extends GenericDaoImpl<PaySettlementLog, Long> implements PaySettlementLogDao {

}
