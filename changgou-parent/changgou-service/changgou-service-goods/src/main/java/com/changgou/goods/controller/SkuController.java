package com.changgou.goods.controller;

import com.changgou.core.AbstractCoreController;
import com.changgou.goods.pojo.Sku;
import com.changgou.goods.service.SkuService;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.List;

/****
 * @Author:admin
 * @Description:
 * @Date 2019/6/14 0:18
 *****/

@RestController
@RequestMapping("/sku")
@CrossOrigin
public class SkuController extends AbstractCoreController<Sku>{

    private SkuService  skuService;

    @Autowired
    public SkuController(SkuService  skuService) {
        super(skuService, Sku.class);
        this.skuService = skuService;
    }

    //需要实现接口
    @GetMapping("/status/{status}")
    Result<List<Sku>> findByStatus(@PathVariable(name="status") String status){
            //根据状态获取sku的列表数据
        List<Sku> skuList = skuService.findByStatus(status);
        //return new Result<List<Sku>>()
        return Result.ok(skuList);
    }

    //根据spu_id和规格(比如灰色长型)查询出对应的sku数据
    @GetMapping("/getBySpec/{spuid}/{spec}")
    Result<Sku> findBySpuidAndSpec(@PathVariable(name="spuid") Long spuid,@PathVariable(name="spec") String spec){

        Sku sku = new Sku();
        sku.setSpuId(spuid);
        sku.setSpec(spec);


        List<Sku> skus = skuService.select(sku);
        if(skus.size()==0||skus==null){
            return new Result<>(false, StatusCode.REPERROR,"没有查到数据");
        }

        return  Result.ok(skus.get(0));


    }

    /**
     *  给指定的商品的ID 扣库存
     * @param id  要扣库存的商品的ID skuid
     * @param num  要扣的数量
     * @return
     */
    @GetMapping("/decCount")
    public Result decCount(@RequestParam(name="id") Long id, @RequestParam(name="num") Integer num){
        int count =  skuService.decCount(id,num);
        if (count > 0) {
            return new Result<List<Sku>>(true, StatusCode.OK,"扣库存成功") ;
        }
        return new Result<List<Sku>>(false, StatusCode.ERROR,"扣库存失败") ;
    }

    /**
     *  给指定的商品的ID 增加销量
     * @param id  要扣库存的商品的ID skuid
     * @param saleNum  要扣的数量
     * @return
     */
    @GetMapping("/addSaleNum")
    public Result addSaleNum(@RequestParam(name="id") Long id, @RequestParam(name="saleNum") Integer saleNum){
        int count =  skuService.addSaleNum(id,saleNum);
        if (count > 0) {
            return new Result<>(true, StatusCode.OK,"增加销量成功") ;
        }
        return new Result<>(false, StatusCode.ERROR,"增加销量失败失败") ;
    }




}
