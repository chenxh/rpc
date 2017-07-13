package com.chencoder.rpc.common.interceptor;

public class InterceptorWrapper {
    public RpcInvokerInterceptor interceptor;
    public String autoMatch;
    public String excludes;

    public InterceptorWrapper(RpcInvokerInterceptor interceptor, String autoMatch, String excludes) {
        this.interceptor = interceptor;
        this.autoMatch = autoMatch;
        this.excludes = excludes;
    }
}
