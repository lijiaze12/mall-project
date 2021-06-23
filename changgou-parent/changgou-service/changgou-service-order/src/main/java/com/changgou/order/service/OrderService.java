package com.changgou.order.service;

import com.changgou.core.service.CoreService;
import com.changgou.order.pojo.Order;
import com.changgou.order.pojo.OrderItem;
import com.github.pagehelper.PageInfo;

import java.util.List;

/****
 * @Author:admin
 * @Description:Order业务层接口
 * @Date 2019/6/14 0:16
 *****/
public interface OrderService extends CoreService<Order> {


     int add(Order order);

     int addBySku(Order order);

    PageInfo<Order> findByPageExam(int pageNo, int pageSize, String username);

    List findOrderAndItem(int pageNo, int pageSize, String username);

    List findNoPay(int pageNo, int pageSize, String username);


}
