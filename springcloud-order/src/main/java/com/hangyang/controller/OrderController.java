package com.hangyang.controller;

import com.hangyang.common.MyLoadBalance;
import com.hangyang.entity.PaymentEntity;
import com.hangyang.utils.R;
import com.netflix.discovery.converters.Auto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/order")
public class OrderController {

    public static final String PAYMENT_URL = "http://SPRINGCLOUD-PAYMENT";
    public static final String SAVE_URL = "/payment/save/";
    public static final String INFO_URL = "/payment/info/";
    
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private MyLoadBalance myLoadBalance;

    @Autowired
    private DiscoveryClient discoveryClient;

    @GetMapping("/my/loadbalance/{id}")
    public R loadbalance(@PathVariable Long id){
        URI uri = myLoadBalance.getServiceInstance();

        return restTemplate.getForObject(uri + INFO_URL + id,R.class);
    }

    @PostMapping("/save")
    public R save(@RequestBody PaymentEntity paymentEntity){
        R r = restTemplate.postForObject(PAYMENT_URL + SAVE_URL, paymentEntity, R.class);
        return r;
    }

    @GetMapping("/info/{id}")
    public R info(@PathVariable Long id){
        R r = restTemplate.getForObject(PAYMENT_URL + INFO_URL + id, R.class);
        return r;
    }

}
