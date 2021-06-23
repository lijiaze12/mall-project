package com.changgou.search.controller;

import com.changgou.search.service.SkuService;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ljh
 * @version 1.0
 * @date 2020/11/14 11:21
 * @description 标题
 * @package com.changgou.search.controller
 */
@RestController
@RequestMapping("/search")
public class SkuController {
    @Autowired
    private SkuService skuService;

    /**
     * 从数据库获取数据再导入数据到es服务器中
     * @return
     */
    @GetMapping("/importSku")
    public Result importData(){

        skuService.importSku();
        return new Result(true, StatusCode.OK,"导入成功");
    }

    /**
     * 搜索
     * @param searchMap 搜索的条件对象JSON     map={"keywords":"手机"}
     * @return map 包含了当前页的记录 总记录数 总页数 品牌列表 规格列表 分类列表。。。
     */
    @PostMapping
    public Map<String,Object> search(@RequestBody(required = false) Map<String,String> searchMap){
        if(searchMap==null){
            searchMap = new HashMap<>();
        }
        return  skuService.search(searchMap);
    }
}
