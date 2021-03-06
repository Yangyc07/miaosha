package com.yang.miaoshaproject.controller;

import com.yang.miaoshaproject.controller.viewobject.ItemVO;
import com.yang.miaoshaproject.dao.ItemDOMapper;
import com.yang.miaoshaproject.error.BusinessException;
import com.yang.miaoshaproject.response.CommonReturnType;
import com.yang.miaoshaproject.service.ItemService;
import com.yang.miaoshaproject.service.model.ItemModel;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Controller("/item ")
@RequestMapping("/item")
@CrossOrigin(origins = {"*"},allowCredentials = "true")
public class ItemController extends BaseController {

    @Autowired
    private ItemService itemService;

    //创建商品
    @RequestMapping(value = "/create",method = {RequestMethod.POST},consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType createItem(@RequestParam(name = "title")String title,
                                       @RequestParam(name = "description")String description,
                                       @RequestParam(name = "price")BigDecimal price,
                                       @RequestParam(name = "stock")Integer stock,
                                       @RequestParam(name = "imgUrl")String imgUrl) throws BusinessException {
        //封装service请求用来创建商品
        ItemModel itemModel = new ItemModel();
        itemModel.setTitle(title);
        itemModel.setDescription(description);
        itemModel.setPrice(price);
        itemModel.setStock(stock);
        itemModel.setImgUrl(imgUrl);

        ItemModel itemMode1ForReturn = itemService.creatItem(itemModel);
        ItemVO itemVO = convertVOFromModel(itemMode1ForReturn);

        return  CommonReturnType.create(itemVO);

    }

    //商品详情页浏览
    @RequestMapping(value = "/get",method = {RequestMethod.GET})//Content type 'null' not supported
    @ResponseBody
    public CommonReturnType getItem(@RequestParam(name = "id") Integer id){
         ItemModel itemModel = itemService.getItemById(id);
         ItemVO itemVO = convertVOFromModel(itemModel);
         System.out.print(itemVO.toString());
         return CommonReturnType.create(itemVO);
    }

    //商品页表浏览
    @RequestMapping(value = "/list",method = {RequestMethod.GET})//Content type 'null' not supported
    @ResponseBody
    public CommonReturnType listItem(){
        List<ItemModel> itemModelList = itemService.listItem();
        List<ItemVO> itemVOList = itemModelList.stream().map(itemModel -> {
            ItemVO itemVO = this.convertVOFromModel(itemModel);
            return itemVO;
        }).collect(Collectors.toList());
        return CommonReturnType.create(itemVOList);
    }

    private ItemVO convertVOFromModel(ItemModel itemModel){
        if(itemModel == null){
            return null;
        }
        ItemVO itemVO = new ItemVO();
        BeanUtils.copyProperties(itemModel,itemVO);
        if(itemModel.getPromoModel()!=null){
            //有正在进行或者即将开始的秒杀活动
            itemVO.setPromoStatus(itemModel.getPromoModel().getStatus());
            itemVO.setPromoId(itemModel.getPromoModel().getId());
            itemVO.setStartDate(itemModel.getPromoModel().getStartDate().toString(DateTimeFormat.forPattern("yyyy-mm-dd HH:mm:ss")));
            itemVO.setPromoPirce(itemModel.getPromoModel().getPromoItemPrice());

        }else{
            itemVO.setPromoStatus(0);
        }
        return itemVO;
    }
}
