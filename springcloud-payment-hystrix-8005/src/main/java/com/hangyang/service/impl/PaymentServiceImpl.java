package com.hangyang.service.impl;

import com.hangyang.service.PaymentService;
import com.hangyang.utils.R;
import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@DefaultProperties(defaultFallback = "paymentDefaultFallback")
public class PaymentServiceImpl implements PaymentService {

    @Value("${server.port}")
    private String serverPort;

    @Override
    public R infoOk(Long id) {
        return R.ok().put("线程池",Thread.currentThread().getName()).put("ServerPort",serverPort);
    }

    @Override
//    @HystrixCommand(fallbackMethod = "infoErrorHandler",commandProperties = {@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds",value = "2000")})
    @HystrixCommand
//    execution.isolation.thread.timeoutInMilliseconds设置方法执行超时时间，让超时时间小于线程时间测试服务降级方法会不会执行
    public R infoError(Long id) {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        int a = 10 / 0;
        return R.ok().put("线程池",Thread.currentThread().getName()).put("ServerPort",serverPort);
    }

    @HystrixCommand(
            fallbackMethod = "idFallBack",
            commandProperties = {
            @HystrixProperty(name = "circuitBreaker.enabled",value = "true"),
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold",value = "10"),
            @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds",value = "10000"),
            @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage",value  = "50")})
    public R info(Long id) {
        if(id < 0) {
            throw new RuntimeException("id 不能为负数");
        }
        return R.ok().put("id",id);
    }

    public R idFallBack(Long id){
        return R.error().put("id",id);
    }

    public R infoErrorHandler(Long id){
        return R.error().put("info","服务繁忙，请稍后重试");
    }

    public R paymentDefaultFallback(){
        return R.error().put("info","global全局异常处理");
    }
}
