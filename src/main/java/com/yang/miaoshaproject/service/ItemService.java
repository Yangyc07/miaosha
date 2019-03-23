package com.yang.miaoshaproject.service;

import com.yang.miaoshaproject.error.BusinessException;
import com.yang.miaoshaproject.service.model.ItemModel;

import java.util.List;

public interface ItemService {

    //创建商品
    ItemModel creatItem(ItemModel itemModel) throws BusinessException;
    //商品列表浏览
    List<ItemModel> listItem();

    //商品详情浏览
    ItemModel getItemById(Integer id);
}
