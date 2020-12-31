package com.hangyang.controller;

import com.hangyang.service.PaymentFeignService;
import com.hangyang.utils.R;
import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
@DefaultProperties(defaultFallback = "paymentDefaultFallback")
public class OrderController {

    @Autowired
    private PaymentFeignService paymentFeignService;

    @RequestMapping("/payment/infoOk/{id}")
    public R infoOk(@PathVariable Long id){
        R r = paymentFeignService.infoOk(id);
        return r;
    }

    @RequestMapping("/payment/infoError/{id}")
//    @HystrixCommand(fallbackMethod = "infoErrorHandler",commandProperties = {@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds",value = "2000")})
    @HystrixCommand
//    execution.isolation.thread.timeoutInMilliseconds设置方法执行超时时间，让超时时间小于线程时间测试服务降级方法会不会执行
    public R infoError(@PathVariable Long id){
        R r = paymentFeignService.infoError(id);
        return r;
    }

    public R infoErrorHandler(Long id){
        return R.error().put("info","服务繁忙，请稍后重试");
    }

    public R paymentDefaultFallback(){
        return R.error().put("info","global全局异常处理");
    }
}
