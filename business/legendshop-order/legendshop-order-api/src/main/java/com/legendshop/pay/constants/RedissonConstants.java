/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.constants;

/**
 * @author legendshop
 */
public interface RedissonConstants {

	// ----------------------  分销钱包

	/**
	 * 钱包操作锁常量
	 */
	String DISTRIBUTION_WALLET_LOCK_KEY = "redisson:distribution_wallet:";

	/**
	 * 钱包中间表锁常量
	 */
	String DISTRIBUTION_WALLET_CENTRE_LOCK_KEY = "redisson:distribution_wallet_centre:";

	/**
	 * 钱包提现操作锁常量
	 */
	String DISTRIBUTION_WALLET_WITHDRAW_LOCK_KEY = "redisson:distribution_wallet_withdraw:";

	/**
	 * 钱包提现操作锁常量
	 */
	String DISTRIBUTION_WALLET_WITHDRAW_DETAIL_LOCK_KEY = "redisson:distribution_wallet_detail_withdraw:";


	// ----------------------  用户钱包

	/**
	 * 钱包操作锁常量
	 */
	String USER_WALLET_LOCK_KEY = "redisson:user_wallet:";

	/**
	 * 钱包中间表锁常量
	 */
	String USER_WALLET_CENTRE_LOCK_KEY = "redisson:user_wallet_centre:";

	/**
	 * 钱包提现操作锁常量
	 */
	String USER_WALLET_WITHDRAW_DETAIL_LOCK_KEY = "redisson:user_wallet_detail_withdraw:";
}
