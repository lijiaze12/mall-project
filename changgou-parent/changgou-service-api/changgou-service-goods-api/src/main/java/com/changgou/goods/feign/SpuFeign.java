package com.changgou.goods.feign;

import com.changgou.goods.pojo.Sku;
import com.changgou.goods.pojo.Spu;
import entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name="goods",path="/spu")
public interface SpuFeign {

    /***
     * 根据审核状态查询Spu
     * @param status
     * @return
     */
    @GetMapping("/status/{status}")
    Result<List<Spu>> findByStatus(@PathVariable(name = "status") String status);

    /***
     * 根据SpuID查询Spu信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<Spu> findById(@PathVariable(name = "id") Long id);

    /***
     * 增加spu的销量
     * @param
     * @return
     */
    @GetMapping("/addSaleNum")
    public Result addSaleNum(@RequestParam(name="id") Long id, @RequestParam(name="saleNum") Integer saleNum);


}