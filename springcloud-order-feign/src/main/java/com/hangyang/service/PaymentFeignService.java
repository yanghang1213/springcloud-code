package com.hangyang.service;

import com.hangyang.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Component
@FeignClient(value = "SPRINGCLOUD-PAYMENT")
public interface PaymentFeignService {

    @GetMapping("/payment/info/{id}")
    public R info(@PathVariable("id") Long id);
}
