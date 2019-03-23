package com.yang.miaoshaproject.service;

import com.yang.miaoshaproject.error.BusinessException;
import com.yang.miaoshaproject.service.model.UserModel;

public interface UserService {

    //通过id获取用户对象
    UserModel getUserById(Integer id);

    void register(UserModel userModel) throws BusinessException;

    UserModel validateLogin(String telphone,String encrpPassword) throws BusinessException;
}
