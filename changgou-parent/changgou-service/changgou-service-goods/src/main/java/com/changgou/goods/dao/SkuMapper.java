package com.changgou.goods.dao;
import com.changgou.goods.pojo.Sku;
import feign.Param;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.common.Mapper;

/****
 * @Author:admin
 * @Description:SkuDao
 * @Date 2019/6/14 0:12
 *****/
public interface SkuMapper extends Mapper<Sku> {
    //sku的库存减少
    @Update(value="update tb_sku set num=num-#{num} where id=#{id} and num>=#{num}")
    int decCount(@Param(value="id") Long id, @Param(value="num") Integer num);

    //sku的销量增加
    @Update(value="UPDATE tb_sku SET sale_num=sale_num+#{saleNum} WHERE id =#{id}")
    int addSaleNum(@Param(value="id") Long id, @Param(value="saleNum") Integer saleNum);

}
