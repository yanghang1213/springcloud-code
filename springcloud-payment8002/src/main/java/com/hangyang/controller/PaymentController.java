package com.hangyang.controller;

import com.hangyang.entity.PaymentEntity;
import com.hangyang.service.PaymentService;
import com.hangyang.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Value("${server.port}")
    private String serverPort;

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
}
