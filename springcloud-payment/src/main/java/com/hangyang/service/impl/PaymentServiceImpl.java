package com.hangyang.service.impl;

import com.hangyang.dao.PaymentDao;
import com.hangyang.entity.PaymentEntity;
import com.hangyang.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentDao paymentDao;

    @Override
    public void save(PaymentEntity paymentEntity) {
        paymentDao.save(paymentEntity);
    }

    @Override
    public PaymentEntity info(Long id) {
        return paymentDao.info(id);
    }
}
