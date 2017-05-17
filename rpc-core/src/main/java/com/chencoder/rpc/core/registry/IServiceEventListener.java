package com.chencoder.rpc.core.registry;

import org.apache.curator.x.discovery.ServiceInstance;

/**
 */
public interface IServiceEventListener<T> {

    void onRegister(ServiceInstance<T> serviceInstance);

    void onRemove(ServiceInstance<T> serviceInstance);

    void onUpdate(ServiceInstance<T> serviceInstance);

    enum ServiceEvent {
        ON_REGISTER,
        ON_UPDATE,
        ON_REMOVE
    }

}
