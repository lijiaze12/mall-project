package com.changgou.search.controller;


import com.changgou.search.service.SpuService;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/search")
@CrossOrigin
public class SpuController {

    @Autowired
    private SpuService spuService;

    /**
     * 导入数据
     * @return
     */
    @GetMapping("/importSpu")
    public Result importData(){
        spuService.importSpu();
        return new Result(true, StatusCode.OK,"导入数据到索引库中成功！");
    }

    /**
     * 搜索
     * @param
     * @return
     */
    @PostMapping("/searchMap")
    public Map<String,Object> search(@RequestBody(required = false) Map<String,String> searchMap){
        if(searchMap==null){
            searchMap = new HashMap<>();
        }
        return  spuService.search(searchMap);
    }
}