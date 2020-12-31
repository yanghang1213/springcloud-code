package com.hangyang.controller;

import com.hangyang.service.PaymentService;
import com.hangyang.utils.R;
import com.netflix.discovery.converters.Auto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @RequestMapping("/infoOk/{id}")
    public R infoOk(@PathVariable Long id){
        R r = paymentService.infoOk(id);

        return r;
    }

    @RequestMapping("/infoError/{id}")
    public R infoError(@PathVariable Long id){
        R r = paymentService.infoError(id);

        return r;
    }

    @RequestMapping("/info/{id}")
    public R info(@PathVariable Long id){
        R r = paymentService.info(id);

        return r;
    }
}
