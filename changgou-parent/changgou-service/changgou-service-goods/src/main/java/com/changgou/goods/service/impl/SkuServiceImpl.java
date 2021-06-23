package com.changgou.goods.service.impl;

import com.changgou.core.service.impl.CoreServiceImpl;
import com.changgou.goods.dao.SkuMapper;
import com.changgou.goods.pojo.Sku;
import com.changgou.goods.service.SkuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/****
 * @Author:admin
 * @Description:Sku业务层接口实现类
 * @Date 2019/6/14 0:16
 *****/
@Service
public class SkuServiceImpl extends CoreServiceImpl<Sku> implements SkuService {

    private SkuMapper skuMapper;

    @Autowired
    public SkuServiceImpl(SkuMapper skuMapper) {
        super(skuMapper, Sku.class);
        this.skuMapper = skuMapper;
    }

    @Override
    public List<Sku> findByStatus(String status) {
        //select * from tb_sku where stauts=?


        Sku condition = new Sku();//where
        condition.setStatus(status);// status = ?
        // condition.setStatus(status);// and status = ?
        // condition.setStatus(status);// and status = ?
        return skuMapper.select(condition);//select * from tb_sku
    }

    @Override
    public List<Sku>  select(Sku sku){
        return skuMapper.select(sku);
    }

    @Override
    public int  decCount(Long id, Integer num) {
    /* Sku sku = skuMapper.selectByPrimaryKey(id);
        sku.setNum(sku.getNum()-num);
        skuMapper.updateByPrimaryKeySelective(sku);*/

        return skuMapper.decCount(id,num);
    }
    @Override
    public int  addSaleNum(Long id, Integer saleNum) {

        return skuMapper.addSaleNum(id,saleNum);
    }



}
