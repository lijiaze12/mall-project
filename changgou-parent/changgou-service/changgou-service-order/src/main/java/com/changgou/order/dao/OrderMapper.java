package com.changgou.order.dao;
import com.changgou.order.pojo.Order;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/****
 * @Author:admin
 * @Description:OrderDao
 * @Date 2019/6/14 0:12
 *****/
public interface OrderMapper extends Mapper<Order> {
    //根据用户username查询全部订单   按照订单的创建时间排序
    @Select(value="select * from  tb_order where username=#{username}  order by create_time desc")
    List<Order> findTimeOrder(@Param(value="username") String username);
}
