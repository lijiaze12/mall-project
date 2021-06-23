package com.changgou.order.controller;


import com.changgou.core.AbstractCoreController;
import com.changgou.order.pojo.Address;
import com.changgou.order.pojo.OrderItem;
import com.changgou.order.pojo.OrderLog;
import com.changgou.order.service.AddressService;
import com.changgou.order.service.OrderItemService;
import com.changgou.order.service.OrderLogService;
import entity.Result;
import entity.StatusCode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/address")
@CrossOrigin
public class AddressController extends AbstractCoreController<Address> {

    private AddressService addressService;

    @Autowired
    public AddressController(AddressService addressService) {
        super(addressService, Address.class);
        this.addressService = addressService;
    }

    /****
     * 用户收件地址
     */
    @GetMapping(value = "/user/list")
    public Result<List<Address>> list(){
        //获取用户登录信息
       // Map<String, String> userMap = TokenDecode.getUserInfo();
        //String username = userMap.get("username");
        String username ="lijiaze";
        //查询用户收件地址
        List<Address> addressList = addressService.list(username);
        return new Result(true, StatusCode.OK,"查询成功！",addressList);
    }



}
