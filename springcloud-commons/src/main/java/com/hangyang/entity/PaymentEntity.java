package com.hangyang.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String serial;
}
