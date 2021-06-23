package com.changgou.order.service.impl;

import com.changgou.core.service.impl.CoreServiceImpl;
import com.changgou.order.dao.AddressMapper;
import com.changgou.order.pojo.Address;
import com.changgou.order.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Service
public class AddressServiceImpl extends CoreServiceImpl<Address> implements AddressService {

    private AddressMapper addressMapper;

    @Autowired
    public AddressServiceImpl(AddressMapper addressMapper) {
        super(addressMapper, Address.class);
        this.addressMapper = addressMapper;
    }

    /***
     * 根据用户名username 查询用户地址信息
     * @param username
     * @return
     */
    @Override
    public List<Address> list(String username) {
        Address address = new Address();
        address.setUsername(username);
        return addressMapper.select(address);
    }

}
