package com.changgou.order.consumer;

import com.alibaba.fastjson.JSON;
import com.changgou.order.dao.OrderMapper;
import com.changgou.order.pojo.Order;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * @author ljh
 * @version 1.0
 * @date 2020/11/25 15:01
 * @description 标题
 * @package com.changgou.order.listener
 */
@Component
@RabbitListener(queues = "queue.order")
public class OrderPayMessageListener {

    @Autowired
    private OrderMapper orderMapper;

    /**
     * 接收消息 获取消息 之后 进行更新订单的状态
     *
     * @param msg
     */
    @RabbitHandler
    public void updateMsg(String msg) {
        //1.获取消息转成MAP
        Map<String, String> map = JSON.parseObject(msg, Map.class);
        if (map != null) {
            //通信成功
            if (map.get("return_code").equals("SUCCESS")) {
                Order order = orderMapper.selectByPrimaryKey(map.get("out_trade_no"));
                if (map.get("result_code").equals("SUCCESS")) {
                    //2.获取map中的数据 判断 如果支付成功 修改状态

                    order.setUpdateTime(new Date());
                    String time_end = map.get("time_end");
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
                    Date end_time=new Date();
                    try {
                        end_time= simpleDateFormat.parse(time_end);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    order.setPayTime(end_time);
                    order.setTransactionId(map.get("transaction_id"));
                    order.setPayStatus("1");//已经支付
                    orderMapper.updateByPrimaryKeySelective(order);
                } else {
                    //3.获取map中的数据 判断 如果支付失败 （删除掉订单） --->关闭交易订单--》恢复库存--》业务的订单的删除
                    order.setIsDelete("1");//删除
                    orderMapper.updateByPrimaryKeySelective(order);

                }
            } else {
                System.out.println("微信没有东西返回");
            }

        }
    }


}
