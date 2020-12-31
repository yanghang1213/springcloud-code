package com.hangyang.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class MyLoadBalanceImpl implements MyLoadBalance{

    //定义一个原子操作类
    private AtomicInteger atomicInteger;
    public static final String SERVICE_ID = "SPRINGCLOUD-PAYMENT";
    @Autowired
    private DiscoveryClient discoveryClient;

    //初始化赋值
    public MyLoadBalanceImpl (){
        this.atomicInteger = new AtomicInteger(0);
    }


    @Override
    public URI getServiceInstance() {
        //1.获取所有的eureka存活的实例
        List<ServiceInstance> instances = discoveryClient.getInstances(SERVICE_ID);
        //2.获取到每一次请求的角标
        int instancesCount = instances.size();
        int nextValue = getAndSetNextValue(instancesCount);
        //3.获取当前角标的实例对象
        ServiceInstance serviceInstance = instances.get(nextValue);

        return serviceInstance.getUri();
    }

    //此方法是获取到下一个index,通过原子操作类+1 % 存活实例总数量(集群总数量) = 当前请求角标
    private int getAndSetNextValue(int instancesCount) {
        int current = this.atomicInteger.get();
        int nextValue;
        do{
            nextValue = (current + 1) % instancesCount;
        }while (!this.atomicInteger.compareAndSet(current,nextValue));
            return nextValue;
    }
}
