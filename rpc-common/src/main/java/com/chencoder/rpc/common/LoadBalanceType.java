package com.chencoder.rpc.common;

import com.chencoder.rpc.common.cluster.lb.LoadBalance;
import com.chencoder.rpc.common.cluster.lb.RandomLoadBalance;
import com.chencoder.rpc.common.cluster.lb.RoundRobinLoadBalance;

/**
 */
public enum LoadBalanceType {
    RANDOM, ROBBIN, HASH, SAME_ROOM_FIST;

	public static LoadBalance getLoadBalance(String loadBalance) {
		if("ROBBIN".equalsIgnoreCase(loadBalance)){
			return new RoundRobinLoadBalance();
		}
		return new RandomLoadBalance();
	}
}
