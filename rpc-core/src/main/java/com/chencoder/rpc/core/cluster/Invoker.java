package com.chencoder.rpc.core.cluster;

import com.chencoder.rpc.common.bean.Message;
import com.chencoder.rpc.core.transport.ResponseFuture;

public interface Invoker {
	ResponseFuture<?> request(Message message, long timeout);
}
