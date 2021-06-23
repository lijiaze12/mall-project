package com.changgou.goods.service;

import com.changgou.core.service.CoreService;
import com.changgou.goods.pojo.Sku;

import java.util.List;

/****
 * @Author:admin
 * @Description:Sku业务层接口
 * @Date 2019/6/14 0:16
 *****/
public interface SkuService extends CoreService<Sku> {

    List<Sku> findByStatus(String status);


    List<Sku>  select(Sku sku);

    //扣减库存
    int decCount(Long id, Integer num);

    //增加销量
    int addSaleNum(Long id, Integer saleNum);

}
