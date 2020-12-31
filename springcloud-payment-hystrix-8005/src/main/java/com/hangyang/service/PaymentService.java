package com.hangyang.service;

import com.hangyang.utils.R;

public interface PaymentService {
    R infoOk(Long id);

    R infoError(Long id);

    R info(Long id);
}
