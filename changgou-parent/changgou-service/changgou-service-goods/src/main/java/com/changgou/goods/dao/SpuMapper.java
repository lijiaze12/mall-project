package com.changgou.goods.dao;
import com.changgou.goods.pojo.Spu;
import feign.Param;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.common.Mapper;

/****
 * @Author:admin
 * @Description:SpuDao
 * @Date 2019/6/14 0:12
 *****/
public interface SpuMapper extends Mapper<Spu> {

    //spu的销量增加
    @Update(value="UPDATE tb_spu SET sale_num=sale_num+#{saleNum} WHERE id =#{id}")
    int addSaleNum(@Param(value="id") Long id, @Param(value="saleNum") Integer saleNum);


}
