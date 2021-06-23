package com.changgou.order.service;

import com.changgou.order.pojo.OrderItem;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ljh
 * @version 1.0
 * @date 2020/11/22 11:50
 * @description 标题
 * @package com.changgou.order.service
 */
public interface CartService {
    /**
     * 给指定的用户 添加购物车的商品
     * @param num  数量
     * @param id 商品的ID
     * @param username 购买商品的用户
     */
    void add(Integer num, Long id, String username);

    /**
     * 根据用户名获取该用户下的购物车的商品的列表
     * @param username
     * @return
     */
    List<OrderItem> list(String username);

    OrderItem findByKey(String username,Long id);

    List<OrderItem> findByIds(String username, List<Long> ids);

    OrderItem findSkuAndSaveRedis(Integer num, Long id, String username);

    OrderItem findByKeyWithSku(String username,Long id);

    List<OrderItem> skuReidslist(String username);

}
