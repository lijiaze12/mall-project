package com.changgou.order.service.impl;

import com.changgou.core.service.impl.CoreServiceImpl;
import com.changgou.goods.feign.SkuFeign;
import com.changgou.goods.feign.SpuFeign;
import com.changgou.order.dao.OrderItemMapper;
import com.changgou.order.dao.OrderMapper;
import com.changgou.order.pojo.Order;
import com.changgou.order.pojo.OrderItem;
import com.changgou.order.service.CartService;
import com.changgou.order.service.OrderService;
import com.changgou.order.util.PageUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import entity.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.awt.print.Book;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/****
 * @Author:admin
 * @Description:Order业务层接口实现类
 * @Date 2019/6/14 0:16
 *****/
@Service
@Transactional
public class OrderServiceImpl extends CoreServiceImpl<Order> implements OrderService {

    private OrderMapper orderMapper;

    @Autowired
    public OrderServiceImpl(OrderMapper orderMapper) {
        super(orderMapper, Order.class);
        this.orderMapper = orderMapper;
    }


    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private RedisTemplate redisTemplate;


    @Autowired
    private CartService cartService;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private SkuFeign skuFeign;

    @Autowired
    private SpuFeign spuFeign;



    /***
     * 添加订单
     * @param order
     * @return
     */
    @Override
    public int add(Order order) {
        List<OrderItem> orderItems = new ArrayList<>();

        //接收order对象  并获取order对象中的 ids，是选中了购物车的哪些商品
        //遍历ids  获取每一个sku的id  根据用户名和sku的id  从redis获取购物车的数据
        for (Long skuid : order.getSkuids()) {  //商品数据来自订单详情页  那么ids集合中只有一个id
            // 获取username
            String username = order.getUsername();
            //从购物车的redsi中查  如果数据来自详情页  那么购物车中查不到
            OrderItem orderItem = cartService.findByKey(username, skuid);
            //向容器中添加orderItem对象
            orderItems.add(orderItem);
            //从redis中删除对应的购物车数据
            redisTemplate.boundHashOps("Cart_" + username).delete(skuid);

        }
        //根据用户名查询出用户购物车所有的商品明细
       // List<OrderItem> orderItems = cartService.list(order.getUsername());

        //统计计算
        int totalMoney = 0;
        int totalPayMoney=0;
        int num = 0;
        //遍历购物车中的数据  计算出整个订单的应付总金额 和实付总金额
        for (OrderItem orderItem : orderItems) {
            // 计算出总金额
            totalMoney+=orderItem.getMoney();

            //实际支付金额
            totalPayMoney+=orderItem.getPayMoney();
            //总数量
            num+=orderItem.getNum();
        }
        order.setTotalNum(num);
        order.setTotalMoney(totalMoney);
        order.setPayMoney(totalPayMoney);
        order.setPreMoney(totalMoney-totalPayMoney);

        //其他数据完善
        order.setCreateTime(new Date());    //订单创建时间
        order.setUpdateTime(order.getCreateTime());
        order.setBuyerRate("0");        //0:未评价，1：已评价
        order.setSourceType("1");       //来源，1：WEB
        order.setOrderStatus("0");      //0:未完成,1:已完成，2：已退货
        order.setPayStatus("0");        //0:未支付，1：已支付，2：支付失败
        order.setConsignStatus("0");    //0:未发货，1：已发货，2：已收货
        order.setId(idWorker.nextId()+"");  //订单的id
        int count = orderMapper.insertSelective(order);





        //添加订单明细
        for (OrderItem orderItem : orderItems) {
            orderItem.setId("NO."+idWorker.nextId());
            orderItem.setIsReturn("0");
            orderItem.setOrderId(order.getId());
            orderItemMapper.insertSelective(orderItem);

            Long skuId = orderItem.getSkuId();

            //库存减库存
            skuFeign.decCount(skuId, orderItem.getNum());
            //销量加1  调用sku
            skuFeign.addSaleNum(skuId,orderItem.getNum());

            //获取订单明细中的spuid
            Long spuId = orderItem.getSpuId();
            //为spu表中的数据增加销量
            spuFeign.addSaleNum(spuId,orderItem.getNum());


        }
        return count;
    }

    /***
     * 添加订单  商品数据来自详情页  不是来自 购物车
     * @param order
     * @return
     */
    @Override
    public int addBySku(Order order) {
        List<OrderItem> orderItems = new ArrayList<>();


        for (Long skuid : order.getSkuids()) {
            // 获取username
            String username = order.getUsername();

            OrderItem orderItem = cartService.findByKeyWithSku(username, skuid);
            //向容器中添加orderItem对象
            orderItems.add(orderItem);
            //从redis中删除对应的购物车数据
            redisTemplate.boundHashOps("Sku_" + username).delete(skuid);

        }
        //根据用户名查询出用户购物车所有的商品明细
        // List<OrderItem> orderItems = cartService.list(order.getUsername());

        //统计计算
        int totalMoney = 0;
        int totalPayMoney=0;
        int num = 0;
        //遍历购物车中的数据  计算出整个订单的应付总金额 和实付总金额
        for (OrderItem orderItem : orderItems) {
            // 计算出总金额
            totalMoney+=orderItem.getMoney();

            //实际支付金额
            totalPayMoney+=orderItem.getPayMoney();
            //总数量
            num+=orderItem.getNum();
        }
        order.setTotalNum(num);
        order.setTotalMoney(totalMoney);
        order.setPayMoney(totalPayMoney);
        order.setPreMoney(totalMoney-totalPayMoney);

        //其他数据完善
        order.setCreateTime(new Date());    //订单创建时间
        order.setUpdateTime(order.getCreateTime());
        order.setBuyerRate("0");        //0:未评价，1：已评价
        order.setSourceType("1");       //来源，1：WEB
        order.setOrderStatus("0");      //0:未完成,1:已完成，2：已退货
        order.setPayStatus("0");        //0:未支付，1：已支付，2：支付失败
        order.setConsignStatus("0");    //0:未发货，1：已发货，2：已收货
        order.setId(idWorker.nextId()+"");  //订单的id
        int count = orderMapper.insertSelective(order);

        //添加订单明细
        for (OrderItem orderItem : orderItems) {
            orderItem.setId("NO."+idWorker.nextId());
            orderItem.setIsReturn("0");
            orderItem.setOrderId(order.getId());
            orderItemMapper.insertSelective(orderItem);

            Long skuId = orderItem.getSkuId();
            //库存减库存
            skuFeign.decCount(skuId, orderItem.getNum());
            //todo销量加
            skuFeign.addSaleNum(skuId,orderItem.getNum());


            //获取订单明细中的spuid
            Long spuId = orderItem.getSpuId();
            //为spu表中的数据增加销量
            spuFeign.addSaleNum(spuId,orderItem.getNum());
        }
        return count;
    }


    @Override
    public PageInfo<Order> findByPageExam(int pageNo,int pageSize,String username){
        PageHelper.startPage(pageNo, pageSize);
        Order order = new Order();
        order.setUsername(username);
        List<Order> list = orderMapper.select(order);
        PageInfo<Order> info = new PageInfo<Order>(list);
        return info;
    }

    /*
    根据用户  ****  查询全部订单及其   *****   订单明细及其状态
     */
    @Override
    public List<OrderItem> findOrderAndItem(int pageNo, int pageSize, String username) {
        Example example = new Example(Order.class);
        // 排序
        example.orderBy("createTime").desc();
        // 条件查询
        example.createCriteria()
                .andEqualTo("username", username);

        List<Order> list = orderMapper.selectByExample(example);
        //创建一个list容器  装载orderItem
        List<OrderItem> orderItemList = new ArrayList<>();


        for (Order orderr : list) {
            String id = orderr.getId();  //订单id
            Date createTime = orderr.getCreateTime();//订单创建时间
            String payStatus = orderr.getPayStatus(); //支付状态
            String consignStatus = orderr.getConsignStatus();//物流状态
            //查询出这个订单id下的全部订单明细数据
            OrderItem condition = new OrderItem();
            condition.setOrderId(id);//设置订单id
            //根据订单id查询订单明细数据
            List<OrderItem> orderItems = orderItemMapper.select(condition);
            //判断状态
            if(!payStatus.equals("1")){
                //没有支付
                for (OrderItem orderItem : orderItems) {
                    orderItem.setCreateTime(createTime); //设置订单创建时间
                    orderItem.setStatus("未支付");
                    orderItemList.add(orderItem);
                }
            }else {
                //支付完成  订单明细的状态设置为  待发货  已发货  已收货
                for (OrderItem orderItem : orderItems) {
                    orderItem.setCreateTime(createTime);//设置订单创建时间
                    //判断
                    if(consignStatus.equals("0")){
                        orderItem.setStatus("待发货");
                    }else if(consignStatus.equals("1")){
                        orderItem.setStatus("已发货");
                    }else if(consignStatus.equals("2")){
                        orderItem.setStatus("已收货");
                    }
                    orderItemList.add(orderItem);
                }
            }
        }

        List list1 = PageUtil.startPage(orderItemList, pageNo, pageSize);
        return list1;
    }
    //根据username 和未付款的条件  查询用户订单和订单明细数据  分页展示
    @Override
    public List findNoPay(int pageNo, int pageSize, String username) {
        Example example = new Example(Order.class);
        // 排序
        example.orderBy("createTime").desc();
        // 条件查询
        example.createCriteria()
                .andEqualTo("username", username)
                .andNotEqualTo("payStatus","1");

        List<Order> list = orderMapper.selectByExample(example);
        //创建一个list容器  装载orderItem
        List<OrderItem> orderItemList = new ArrayList<>();


        for (Order orderr : list) {
            String id = orderr.getId();  //订单id
            Date createTime = orderr.getCreateTime();//订单创建时间
            String payStatus = orderr.getPayStatus(); //支付状态
            String consignStatus = orderr.getConsignStatus();//物流状态
            //查询出这个订单id下的全部订单明细数据
            OrderItem condition = new OrderItem();
            condition.setOrderId(id);//设置订单id
            //根据订单id查询订单明细数据
            List<OrderItem> orderItems = orderItemMapper.select(condition);
            //判断状态

                //没有支付
            for (OrderItem orderItem : orderItems) {
                    orderItem.setCreateTime(createTime); //设置订单创建时间
                    orderItem.setStatus("未支付");
                    orderItemList.add(orderItem);
            }

        }

        List list1 = PageUtil.startPage(orderItemList, pageNo, pageSize);
        return list1;
    }


}
