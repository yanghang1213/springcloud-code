package com.hangyang.controller;

import com.hangyang.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@Slf4j
@RequestMapping("/order")
public class OrderController {

    public static final String PAYMENT_URL = "http://springcloud-payment-consul";
    public static final String INFO_URL = "/payment/consul";
    
    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/payment/consule")
    public R info(){
        R r = restTemplate.getForObject(PAYMENT_URL + INFO_URL, R.class);
        return r;
    }
}
