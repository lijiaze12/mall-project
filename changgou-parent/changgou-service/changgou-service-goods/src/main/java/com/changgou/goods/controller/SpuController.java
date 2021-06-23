package com.changgou.goods.controller;

import com.changgou.core.AbstractCoreController;
import com.changgou.goods.pojo.Goods;
import com.changgou.goods.pojo.Sku;
import com.changgou.goods.pojo.Spu;
import com.changgou.goods.service.SpuService;
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
@RequestMapping("/spu")
@CrossOrigin
public class SpuController extends AbstractCoreController<Spu>{

    private SpuService  spuService;

    @Autowired
    public SpuController(SpuService  spuService) {
        super(spuService, Spu.class);
        this.spuService = spuService;
    }


    /**
     * 添加、更新商品
     * @param goods 表单数据
     * @return
     */
    @PostMapping("/save")
    public Result save(@RequestBody Goods goods){
        spuService.saveGoods(goods);
        return new Result(true, StatusCode.OK,"添加成功");
    }

    /**
     * 根据商品的SPU的ID获取spu和sku的列表数据
     * @param id
     * @return
     */
    @GetMapping("/goods/{id}")
    public Result<Goods> findGoodsById(@PathVariable(name="id") Long id){
        Goods goods  = spuService.findGoodsById(id);
        return new Result(true, StatusCode.OK,"查询成功",goods);
    }

    /**
     * 审核
     * @param id
     * @return
     */
    @PutMapping("/audit/{id}")
    public Result audit(@PathVariable Long id){
        spuService.audit(id);
        return new Result(true,StatusCode.OK,"审核成功");
    }

    /**
     * 下架
     * @param id
     * @return
     */
    @PutMapping("/pull/{id}")
    public Result pull(@PathVariable Long id){
        spuService.pull(id);
        return new Result(true,StatusCode.OK,"下架成功");
    }

    /**
     * 商品上架
     * @param id
     * @return
     */
    @PutMapping("/put/{id}")
    public Result put(@PathVariable Long id){
        spuService.put(id);
        return new Result(true,StatusCode.OK,"上架成功");
    }

    /**
     *  批量上架
     * @param ids
     * @return
     */
    @PutMapping("/put/many")
    public Result putMany(@RequestBody Long[] ids){
        int count = spuService.putMany(ids);
        return new Result(true,StatusCode.OK,"上架"+count+"个商品");
    }

    /***
     * 根据审核状态查询Sku
     * @param status
     * @return
     */
    @GetMapping("/status/{status}")
    public Result<List<Spu>> findByStatus(@PathVariable String status){
        List<Spu> list = spuService.findByStatus(status);
        return new Result<List<Spu>>(true,StatusCode.OK,"查询成功",list);
    }

    /**
     *  给指定的商品的ID 增加销量
     * @param id  要扣库存的商品的ID skuid
     * @param saleNum  要扣的数量
     * @return
     */
    @GetMapping("/addSaleNum")
    public Result addSaleNum(@RequestParam(name="id") Long id, @RequestParam(name="saleNum") Integer saleNum){
        int count =  spuService.addSaleNum(id,saleNum);
        if (count > 0) {
            return new Result<>(true, StatusCode.OK,"增加销量成功") ;
        }
        return new Result<>(false, StatusCode.ERROR,"增加销量失败失败") ;
    }







}
