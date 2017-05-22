package com.chencoder.rpc.common.interceptor;

import java.lang.reflect.Method;

/**
 */
public interface InvokerInterceptor {

    boolean beforeInvoke(Object target, Method method, Object... args);

    Object processInvoke(Object target, Method method, Object... args);

    boolean afterInvoke(Object target, Method method, Object result);
}
