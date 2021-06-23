package com.changgou.order.controller;

import com.changgou.core.AbstractCoreController;
import com.changgou.order.pojo.CartItem;
import com.changgou.order.service.CartItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/****
 * @Author:admin
 * @Description:
 * @Date 2019/6/14 0:18
 *****/

@RestController
@RequestMapping("/cartItem")
@CrossOrigin
public class CartItemController extends AbstractCoreController<CartItem>{

    private CartItemService  cartItemService;

    @Autowired
    public CartItemController(CartItemService  cartItemService) {
        super(cartItemService, CartItem.class);
        this.cartItemService = cartItemService;
    }
}
