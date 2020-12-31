package com.hangyang.service;

import com.hangyang.utils.R;
import org.springframework.stereotype.Component;

@Component
public class PaymentFeignServiceImpl implements PaymentFeignService{
    @Override
    public R infoOk(Long id) {
        return R.error().put("infoOk","80客户端自己处理降级");
    }

    @Override
    public R infoError(Long id) {
        return R.error().put("infoError","80客户端自己处理降级");
    }
}
