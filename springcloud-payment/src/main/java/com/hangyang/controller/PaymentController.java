package com.hangyang.controller;

import com.hangyang.entity.PaymentEntity;
import com.hangyang.service.PaymentService;
import com.hangyang.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/payment")
@Slf4j
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Value("${server.port}")
    private String serverPort;

    @Autowired
    private DiscoveryClient discoveryClient;

    @PostMapping("/save")
    public R save(@RequestBody PaymentEntity paymentEntity){
        paymentService.save(paymentEntity);
        return R.ok().put("serverPort",serverPort);
    }

    @GetMapping("/info/{id}")
    public R info(@PathVariable Long id) {
        PaymentEntity paymentEntity =paymentService.info(id);
        return R.ok().put("paymentEntity",paymentEntity).put("serverPort",serverPort);
    }

    @GetMapping("discoveryClient")
    public R getDiscoveryClient(){
        List<ServiceInstance> instances = discoveryClient.getInstances("SPRINGCLOUD-PAYMENT");
        for (ServiceInstance instance : instances) {
            log.info(instance.getServiceId() + "\t" + instance.getHost() + "\t" + instance.getPort() + "\t" + instance.getUri()+ "\t" +instance.getScheme()+ "\t" +instance.getMetadata());
        }
        return R.ok().put("discoveryClient",discoveryClient);
    }
}
