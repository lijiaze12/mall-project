package com.changgou.goods.service;

import com.changgou.core.service.CoreService;
import com.changgou.goods.pojo.Goods;
import com.changgou.goods.pojo.Sku;
import com.changgou.goods.pojo.Spu;

import java.util.List;

/****
 * @Author:admin
 * @Description:Spu业务层接口
 * @Date 2019/6/14 0:16
 *****/
public interface SpuService extends CoreService<Spu> {

    void saveGoods(Goods goods);

    Goods findGoodsById(Long id);

    /***
     * 商品审核
     * @param spuId
     */
    void audit(Long spuId);

    /***
     * 商品下架
     * @param spuId
     */
    void pull(Long spuId);

    /***
     * 商品上架
     * @param spuId
     */
    void put(Long spuId);

    /*
       批量上架   接受前端传递的一组id的集合
     */
    int putMany(Long[] ids);

    /**
     * 根据状态查询SKU列表
     */
    List<Spu> findByStatus(String status);

    //增加销量
    int addSaleNum(Long id, Integer saleNum);



}
