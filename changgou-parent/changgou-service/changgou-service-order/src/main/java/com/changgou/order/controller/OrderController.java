package com.changgou.order.controller;

import com.changgou.core.AbstractCoreController;
import com.changgou.order.pojo.Order;
import com.changgou.order.pojo.OrderItem;
import com.changgou.order.service.OrderService;
import com.github.pagehelper.PageInfo;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/****
 * @Author:admin
 * @Description:
 * @Date 2019/6/14 0:18
 *****/

@RestController
@RequestMapping("/order")
@CrossOrigin
public class OrderController extends AbstractCoreController<Order>{

    private OrderService  orderService;

    @Autowired
    public OrderController(OrderService  orderService) {
        super(orderService, Order.class);
        this.orderService = orderService;
    }
    //@Autowired
  //  private TokenDecode tokenDecode;
    /**
     * 添加订单 1.添加订单 2.更新库存 3 添加积分 4.清空购物车
     * @param
     * @return
     */
    @PostMapping("/addByCart")
    public Result add(@RequestBody Order order) {
        //String username = tokenDecode.getUsername();
        String username="lijiaze";
        order.setUsername(username);
        orderService.add(order);
        return new Result(true, StatusCode.OK,"添加订单成功");
    }

    //不是购物车的商品下单   从商品详情页进行下单 的添加订单方法
    @PostMapping("/addBySku")
    public Result addBySku(@RequestBody Order order){
        //String username = tokenDecode.getUsername();
        String username="lijiaze";
        order.setUsername(username);
        orderService.addBySku(order);
        return new Result(true, StatusCode.OK,"添加订单成功");
    }

    //*********根据username查询其所有的订单数据和订单明细数据**********
    @GetMapping(value = "/searchItem/{page}/{size}")
    public Result<PageInfo<OrderItem>> findOrderAndItem(@PathVariable(name = "page") Integer pageNo,
                                                        @PathVariable(name = "size") Integer pageSize) {
        //获取当前用户名
        String username="lijiaze";
        List list = orderService.findOrderAndItem(pageNo,pageSize,username);
        return new Result<PageInfo<OrderItem>>(true, StatusCode.OK, "分页查询订单成功", list);
    }

    //查询未付款订单接口
    @GetMapping(value = "/noPay/{page}/{size}")
    public Result<PageInfo<OrderItem>> findNoPay(@PathVariable(name = "page") Integer pageNo,
                                                        @PathVariable(name = "size") Integer pageSize) {
        //获取当前用户名
        String username="lijiaze";
        List list = orderService.findNoPay(pageNo,pageSize,username);
        return new Result<PageInfo<OrderItem>>(true, StatusCode.OK, "分页查询未支付订单成功", list);
    }















}
