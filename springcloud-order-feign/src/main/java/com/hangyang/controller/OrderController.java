package com.hangyang.controller;

import com.hangyang.entity.PaymentEntity;
import com.hangyang.service.PaymentFeignService;
import com.hangyang.utils.R;
import com.netflix.discovery.converters.Auto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private PaymentFeignService paymentFeignService;

    @RequestMapping("/feign/payment/info/{id}")
    public R info(@PathVariable Long id){
        R r = paymentFeignService.info(id);

        return r;
    }
}
