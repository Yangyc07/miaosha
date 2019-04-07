package com.yang.miaoshaproject.service.Impl;

import com.yang.miaoshaproject.dao.PromoDOMapper;
import com.yang.miaoshaproject.dataobject.PromoDO;
import com.yang.miaoshaproject.service.PromoService;
import com.yang.miaoshaproject.service.model.PromoModel;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PromoServiceImpl implements PromoService {

    @Autowired
    private PromoDOMapper promoDOMapper;

    @Override
    public PromoModel getPromoByItemId(Integer itemId) {
        //获取对应商品的秒杀活动信息
        PromoDO promoDO = promoDOMapper.selectByItemId(itemId);

        //dataobject->model
        PromoModel promoModel = convertFromDataObject(promoDO);
        if(promoModel == null){
            return null;
        }
        //判断当前时间时候秒杀活动即将开始或是正在进行
        DateTime now = new DateTime();
        if(promoModel.getStartDate().isAfter(now)){
            promoModel.setStatus(1);
        }else if(promoModel.getEndDate().isBefore(now)){
            promoModel.setStatus(3);
        }else{
            promoModel.setStatus(2);
        }
        System.out.println("-------------");
        System.out.println(promoModel.getStatus());
        return promoModel;
    }

    private PromoModel convertFromDataObject(PromoDO promoDO){
        if(promoDO == null) {
            return null;
        }
        PromoModel promoModel = new PromoModel();
        BeanUtils.copyProperties(promoDO,promoModel);
        promoModel.setPromoItemPrice(new BigDecimal(promoDO.getPromoItemPrice()));
        promoModel.setStartDate(new DateTime(promoDO.getStartDate()));
        promoModel.setEndDate(new DateTime(promoDO.getEndDate()));
        return  promoModel;
    }


}
