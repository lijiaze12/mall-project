package com.changgou.order.service;

import com.changgou.core.service.CoreService;
import com.changgou.order.pojo.Address;

import java.util.List;

public interface AddressService  extends CoreService<Address> {
    /***
     * 收件地址查询
     * @param username
     * @return
     */
    List<Address> list(String username);
}
