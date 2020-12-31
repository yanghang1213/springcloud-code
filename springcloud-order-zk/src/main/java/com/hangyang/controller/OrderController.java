package com.hangyang.controller;

import com.hangyang.entity.PaymentEntity;
import com.hangyang.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@Slf4j
@RequestMapping("/order")
public class OrderController {

    public static final String PAYMENT_URL = "http://springcloud-payment";
    public static final String INFO_URL = "/payment/zookeeper";
    
    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/payment/zk")
    public R info(){
        R r = restTemplate.getForObject(PAYMENT_URL + INFO_URL, R.class);
        return r;
    }
}
