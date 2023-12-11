/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.dao.impl;

import cn.legendshop.jpaplus.impl.GenericDaoImpl;
import com.legendshop.basic.dao.ExceptionLogDao;
import com.legendshop.basic.entity.ExceptionLog;
import org.springframework.stereotype.Repository;

/**
 * (ExceptionLog)表数据库访问层实现类
 *
 * @author legendshop
 * @since 2020-09-25 10:20:08
 */
@Repository
public class ExceptionLogDaoImpl extends GenericDaoImpl<ExceptionLog, Long> implements ExceptionLogDao {

}
