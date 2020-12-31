package com.hangyang.service;

import com.hangyang.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Component
@FeignClient(value = "SPRINGCLOUD-PAYMENT-HYSTRIX",fallback = PaymentFeignServiceImpl.class)
public interface PaymentFeignService {
    @RequestMapping("/payment/infoOk/{id}")
    public R infoOk(@PathVariable("id") Long id);

    @RequestMapping("/payment/infoError/{id}")
    public R infoError(@PathVariable("id") Long id);
}
