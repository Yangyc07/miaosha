package com.yang.miaoshaproject.dao;

import com.yang.miaoshaproject.dataobject.ItemStockDO;

public interface ItemStockDOMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table item_stock
     *
     * @mbg.generated Tue Feb 19 20:49:57 CST 2019
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table item_stock
     *
     * @mbg.generated Tue Feb 19 20:49:57 CST 2019
     */
    int insert(ItemStockDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table item_stock
     *
     * @mbg.generated Tue Feb 19 20:49:57 CST 2019
     */
    int insertSelective(ItemStockDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table item_stock
     *
     * @mbg.generated Tue Feb 19 20:49:57 CST 2019
     */
    ItemStockDO selectByPrimaryKey(Integer id);

    ItemStockDO selectByItemId(Integer itemId);
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table item_stock
     *
     * @mbg.generated Tue Feb 19 20:49:57 CST 2019
     */
    int updateByPrimaryKeySelective(ItemStockDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table item_stock
     *
     * @mbg.generated Tue Feb 19 20:49:57 CST 2019
     */
    int updateByPrimaryKey(ItemStockDO record);
}