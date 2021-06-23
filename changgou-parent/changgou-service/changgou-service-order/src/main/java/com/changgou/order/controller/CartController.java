package com.changgou.order.controller;

import com.alibaba.fastjson.JSON;
import com.changgou.order.pojo.OrderItem;
import com.changgou.order.service.CartService;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author ljh
 * @version 1.0
 * @date 2020/11/22 11:46
 * @description 标题
 * @package com.changgou.order.controller
 */

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    //@Autowired
    //private TokenDecode tokenDecode;

    /**
     * 添加购物车
     *
     * @param num 购买的数量
     * @param id  购买的商品的SKU的ID
     * @return
     */
    @GetMapping("/add")
    public Result addCart(Integer num, Long id) {

        //String username = tokenDecode.getUserName();
        String username ="lijiaze";
        cartService.add(num,id,username);
        return new Result(true, StatusCode.OK,"添加成功");
    }

    //获取当前登录的用户的购物车的商品列表
    @GetMapping("/list")
    public Result<List<OrderItem>> list(){
        //String username = tokenDecode.getUserName();
        String username="lijiaze";
        List<OrderItem> orderItemList = cartService.list(username);
        return new Result<List<OrderItem>>(true,StatusCode.OK,"查询成功",orderItemList);
    }
    //@GetMapping("/category/{id}")
    //public Result<List<Brand>> findBrandByCategory(@PathVariable(name="id")Integer id){

    @GetMapping("/findById/{id}")
    public  Result<OrderItem> findById(@PathVariable(name="id")Long id){
        String username = "lijiaze";
        OrderItem orderItem = cartService.findByKey(username, id);
        return new Result<OrderItem>(true,StatusCode.OK,"查询成功",orderItem);
    }

    @PostMapping("/findByIds")
    //根据选中的id的集合查询购物车
    public Result<List<OrderItem>> findByIds(@RequestBody Map jsonMap){

        String username="lijiaze";
        List result = (List<Long>)jsonMap.get("skuids");


        List<OrderItem> lists = cartService.findByIds(username, result);


        return new Result<List<OrderItem>>(true,StatusCode.OK,"查询成功",lists);
    }
    /***********************************************
      ******************** 商品详情。立即结算*****************************
      ******************************************************/
    //从商品详情页面跳转到结算页面  页面加载请求这个方法 封装订单明细对象 保存到redis中 并返回对象给前端展示
    @GetMapping("/findSkuSaveRedis")
    public Result<OrderItem> findSkuSaveRedis(Integer num, Long id){
        //String username = tokenDecode.getUserName();
        String username ="lijiaze";
        OrderItem orderItem = cartService.findSkuAndSaveRedis(num, id, username);
        return new Result<OrderItem>(true, StatusCode.OK,"添加成功",orderItem);
    }
    //根据skuid  作为redis中的key  查询对应的订单明细数据
    @GetMapping("/findBySkuRedis/{id}")
    public  OrderItem findRedis(@PathVariable(name="id")Long id){
        String username ="lijiaze";
        OrderItem orderItem = cartService.findByKeyWithSku(username, id);
        return  orderItem ;
    }
     //查询redis中的订单明细列表
    @GetMapping("/skuReidslist")
    public Result<List<OrderItem>> skuReidslist(){
        //String username = tokenDecode.getUserName();
        String username="lijiaze";
        List<OrderItem> orderItemList = cartService.skuReidslist(username);
        return new Result<List<OrderItem>>(true,StatusCode.OK,"查询成功",orderItemList);
    }
}
