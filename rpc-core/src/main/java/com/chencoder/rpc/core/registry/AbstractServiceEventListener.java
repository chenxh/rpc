package com.chencoder.rpc.core.registry;

import org.apache.curator.x.discovery.ServiceInstance;

/**
 */
public abstract class AbstractServiceEventListener<T> implements IServiceEventListener<T> {

    public void onFresh(ServiceInstance<T> serviceInstance, ServiceEvent event) {
        switch (event) {
            case ON_REGISTER:
                onRegister(serviceInstance);
                break;
            case ON_UPDATE:
                onUpdate(serviceInstance);
                break;
            case ON_REMOVE:
                onRemove(serviceInstance);
                break;
        }
    }

    @Override
    public void onUpdate(ServiceInstance<T> serviceInstance) {

    }

    @Override
    public void onRegister(ServiceInstance<T> serviceInstance) {

    }
}
