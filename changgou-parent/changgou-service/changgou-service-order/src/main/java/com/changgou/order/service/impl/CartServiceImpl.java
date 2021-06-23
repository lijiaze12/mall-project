package com.changgou.order.service.impl;

import com.changgou.goods.feign.SkuFeign;
import com.changgou.goods.feign.SpuFeign;
import com.changgou.goods.pojo.Sku;
import com.changgou.goods.pojo.Spu;
import com.changgou.order.pojo.OrderItem;
import com.changgou.order.service.CartService;
import entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ljh
 * @version 1.0
 * @date 2020/11/22 11:50
 * @description 标题
 * @package com.changgou.order.service.impl
 */
@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private SkuFeign skuFeign;

    @Autowired
    private SpuFeign spuFeign;

    @Override
    public void add(Integer num, Long id, String username) {
        //接收的参数 是 sku的id  数量  和 用户名
        if(num<=0){
            //不买了 删除购物车的数据
            redisTemplate.boundHashOps("Cart_"+username).delete(id);
            //如果此时购物车数据量为空  则连购物车一起移除
            Long size  = redisTemplate.boundHashOps("Cart_"+username).size();
            if(size==null||size<0){
                redisTemplate.delete("Cart_"+username);
            }

            return;
        }
        //判断重复添加购物车行为
        OrderItem orderItem1 = (OrderItem)redisTemplate.boundHashOps("Cart_" + username).get(id);
        if(orderItem1!=null){
            //重复添加
              System.out.println("重复添加购物车");
             Integer maxNum= orderItem1.getNum()+num;
             orderItem1.setNum(maxNum);
            redisTemplate.boundHashOps("Cart_" + username).put(id, orderItem1);



        }else {
            //1.根据商品的ID 获取SKU数据（1.创建一个接口 2.添加一个方法 3.被调用的微服务实现接口4.用的地方引入依赖，开启feignclients 注入）
            //获取SKU数据
            Result<Sku> result = skuFeign.findById(id);
            Sku sku = result.getData();
            //2.根据spu_id 获取SPU的数据
            Result<Spu> spuResult = spuFeign.findById(sku.getSpuId());
            Spu spu = spuResult.getData();
            //3.将数据存储到redis中的购物车中   string  hash   set list zset
            // key? value? String?--->key:用户名 value: List<OrderItem>
            // key? value? hash?[采用]--->
            // key:用户名      field   value
            // key:zhansan   skuID1   pojo1
            // key:zhansan   skuID2   pojo2
            // key:zhanwu    skuID1   pojo1

            // hset key field1 value1
            OrderItem orderItem = new OrderItem();
            //3.1 设置分类的ID 1,2,3级
            orderItem.setCategoryId1(spu.getCategory1Id());
            orderItem.setCategoryId2(spu.getCategory2Id());
            orderItem.setCategoryId3(spu.getCategory3Id());
            orderItem.setSpuId(spu.getId());
            orderItem.setSkuId(sku.getId());
            orderItem.setName(sku.getName());
            orderItem.setPrice(sku.getPrice());
            orderItem.setNum(num);//
            orderItem.setMoney(num * sku.getPrice());
            orderItem.setPayMoney(num * sku.getPrice());//优惠券
            orderItem.setImage(sku.getImage());
            //redis中存储购物车数据  名称空间是用户名   key是skuid  value是orderItem对象
            redisTemplate.boundHashOps("Cart_" + username).put(id, orderItem);
        }
    }
    /*
     结算页 钩子函数需要获取上一级的url  判断 如果上一级是购物车 则根据ids查购物车
     如果上一级来自商品详情 则获取skuid和数量  分装成orderitem对象 返回orderitem 并保存到reids中
     */
    @Override
    public OrderItem findSkuAndSaveRedis(Integer num, Long id, String username) { //商品数量  skuid   用户名
        //1.根据商品的ID 获取SKU数据（1.创建一个接口 2.添加一个方法 3.被调用的微服务实现接口4.用的地方引入依赖，开启feignclients 注入）
        //获取SKU数据
        Result<Sku> result = skuFeign.findById(id);
        Sku sku = result.getData();
        //2.根据spu_id 获取SPU的数据
        Result<Spu> spuResult = spuFeign.findById(sku.getSpuId());
        Spu spu = spuResult.getData();
        //3.将数据存储到redis中的购物车中   string  hash   set list zset
        // key? value? String?--->key:用户名 value: List<OrderItem>
        // key? value? hash?[采用]--->
        // key:用户名      field   value
        // key:zhansan   skuID1   pojo1
        // key:zhansan   skuID2   pojo2
        // key:zhanwu    skuID1   pojo1

        // hset key field1 value1
        OrderItem orderItem = new OrderItem();
        //3.1 设置分类的ID 1,2,3级
        orderItem.setCategoryId1(spu.getCategory1Id());
        orderItem.setCategoryId2(spu.getCategory2Id());
        orderItem.setCategoryId3(spu.getCategory3Id());
        orderItem.setSpuId(spu.getId());
        orderItem.setSkuId(sku.getId());
        orderItem.setName(sku.getName());
        orderItem.setPrice(sku.getPrice());
        orderItem.setNum(num);//
        orderItem.setMoney(num*sku.getPrice());
        orderItem.setPayMoney(num*sku.getPrice());//优惠券
        orderItem.setImage(sku.getImage());


        //redis中存储购物车数据  名称空间是用户名   key是skuid  value是orderItem对象

        redisTemplate.boundHashOps("Sku_"+username).put(id,orderItem);


        return  orderItem;

    }



    @Override
    public List<OrderItem> list(String username) {
        //hgetall key
        return redisTemplate.boundHashOps("Cart_"+username).values();
    }


    @Override
    //提供一个方法  根据名称空间和  key   查对应的 orderItems
    public OrderItem findByKey(String username,Long id){
        OrderItem orderItem = (OrderItem)redisTemplate.boundHashOps("Cart_" + username).get(id);
        return orderItem;
    }

    @Override
    public OrderItem findByKeyWithSku(String username,Long id){
        OrderItem orderItem = (OrderItem)redisTemplate.boundHashOps("Sku_" + username).get(id);
        return orderItem;
    }

    @Override
    public List<OrderItem> skuReidslist(String username) {

            //hgetall key
            return redisTemplate.boundHashOps("Sku_"+username).values();

    }


    //根据id的集合返回购物车集合数据
    @Override
    public List<OrderItem> findByIds(String username, List<Long> ids){

         List<OrderItem> list = new ArrayList<>();

        for (Long skuid : ids) {
            OrderItem orderItem = (OrderItem)redisTemplate.boundHashOps("Cart_" + username).get(skuid);
            list.add(orderItem);
        }

        return list;
    }



}
