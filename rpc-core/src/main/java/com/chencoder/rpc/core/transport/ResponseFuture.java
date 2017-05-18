package com.chencoder.rpc.core.transport;

import com.chencoder.rpc.common.bean.Message;
import com.chencoder.rpc.core.transport.client.Promise;

/**
 */
public class ResponseFuture<T> {
    private long createTime;
    private long timeOut;
    private Message request;
    private Promise<T> promise;

    public ResponseFuture(long createTime, long timeOut, Message request, Promise<T> promise) {
        this.createTime = createTime;
        this.timeOut = timeOut;
        this.request = request;
        this.promise = promise;
    }

    public long getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(long timeOut) {
        this.timeOut = timeOut;
    }

    public Message getRequest() {
        return request;
    }

    public void setRequest(Message request) {
        this.request = request;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public Promise<T> getPromise() {
        return promise;
    }

    public void setPromise(Promise<T> promise) {
        this.promise = promise;
    }
}
