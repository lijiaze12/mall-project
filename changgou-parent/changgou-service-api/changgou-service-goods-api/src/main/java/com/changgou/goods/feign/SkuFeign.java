package com.changgou.goods.feign;

import com.changgou.goods.pojo.Sku;
import entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author ljh
 * @version 1.0
 * @date 2020/11/14 11:38
 * @description 标题
 * @package com.changgou.goods.feign
 */
@FeignClient(name="goods",path = "/sku")
public interface SkuFeign {

    /**
     * 根据状态值查询该状态值下的所有的sku的列表数据
     * @param status
     * @return
     */
    @GetMapping("/status/{status}")
    Result<List<Sku>> findByStatus(@PathVariable(name="status") String status);

    /***
     * 根据ID查询SKU信息
     * @param id : sku的ID
     */
    @GetMapping(value = "/{id}")
    public Result<Sku> findById(@PathVariable(value = "id", required = true) Long id);

    /***
     * 库存递减
     * @param
     * @return
     */
    @GetMapping("/decCount")
    public Result decCount(@RequestParam(name="id") Long id, @RequestParam(name="num") Integer num);

    /***
     * 增加sku的销量
     * @param
     * @return
     */
    @GetMapping("/addSaleNum")
    public Result addSaleNum(@RequestParam(name="id") Long id, @RequestParam(name="saleNum") Integer saleNum);
}
