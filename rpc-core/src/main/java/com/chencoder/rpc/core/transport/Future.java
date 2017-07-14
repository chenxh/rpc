package com.chencoder.rpc.core.transport;

import java.util.concurrent.TimeUnit;

public interface Future<T> {

    T await() throws Exception;

    T await(long amount, TimeUnit unit) throws Exception;
}
