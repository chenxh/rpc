package com.chencoder.rpc.core.transport;

public interface Callback<T> {

    void onReceive(T message);
}
