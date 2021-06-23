package com.changgou.order.service.impl;

import com.changgou.core.service.impl.CoreServiceImpl;
import com.changgou.order.dao.CartItemMapper;
import com.changgou.order.pojo.CartItem;
import com.changgou.order.service.CartItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/****
 * @Author:admin
 * @Description:CartItem业务层接口实现类
 * @Date 2019/6/14 0:16
 *****/
@Service
public class CartItemServiceImpl extends CoreServiceImpl<CartItem> implements CartItemService {

    private CartItemMapper cartItemMapper;

    @Autowired
    public CartItemServiceImpl(CartItemMapper cartItemMapper) {
        super(cartItemMapper, CartItem.class);
        this.cartItemMapper = cartItemMapper;
    }
}
