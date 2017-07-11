package com.chencoder.rpc.core.registry;

import org.apache.curator.x.discovery.ServiceInstance;

import java.util.Collection;

/**
 * Created by Dempe on 2016/12/8.
 */
public interface RpcServiceDiscovery<T> {


    /**
     * Register/re-register a service
     *
     * @param service service to add
     * @throws Exception errors
     */
    void registerService(ServiceInstance<T> service) throws Exception;

    /**
     * Unregister/remove a service instance
     *
     * @param service the service
     * @throws Exception errors
     */
    void unregisterService(ServiceInstance<T> service) throws Exception;


    /**
     * Return the names of all known services
     *
     * @return list of service names
     * @throws Exception errors
     */
    Collection<String> queryForNames() throws Exception;

    /**
     * Return all known instances for the given service
     *
     * @param name name of the service
     * @return list of instances (or an empty list)
     * @throws Exception errors
     */
    Collection<ServiceInstance<T>> queryForInstances(String name) throws Exception;

    /**
     * Return a service instance POJO
     *
     * @param name name of the service
     * @param id   ID of the instance
     * @return the instance or <code>null</code> if not found
     * @throws Exception errors
     */
    ServiceInstance<T> queryForInstance(String name, String id) throws Exception;


}
