package com.chencoder.rpc.core;

import com.chencoder.rpc.common.bean.Message;
import com.chencoder.rpc.common.config.RpcRuntimeConfig;

public interface RpcInvoker {
	
	Object invoke(Message<?> message, RpcRuntimeConfig invokeConfig);
	
}
