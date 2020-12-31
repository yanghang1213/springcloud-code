package com.hangyang.service;

import com.hangyang.entity.PaymentEntity;

public interface PaymentService {
    void save(PaymentEntity paymentEntity);

    PaymentEntity info(Long id);
}
