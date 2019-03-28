package com.yang.miaoshaproject.service;

import com.yang.miaoshaproject.error.BusinessException;
import com.yang.miaoshaproject.service.model.OrderModel;

public interface OrderService {
    OrderModel create(Integer userId,Integer itemId, Integer amount) throws BusinessException;
}
