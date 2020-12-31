package com.hangyang.dao;

import com.hangyang.entity.PaymentEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PaymentDao {
    void save(PaymentEntity paymentEntity);


    PaymentEntity info(@Param("id") Long id);
}
