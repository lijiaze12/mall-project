package com.changgou.goods.controller;

import com.changgou.core.AbstractCoreController;
import com.changgou.goods.pojo.Template;
import com.changgou.goods.service.TemplateService;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/****
 * @Author:admin
 * @Description:
 * @Date 2019/6/14 0:18
 *****/

@RestController
@RequestMapping("/template")
@CrossOrigin
public class TemplateController extends AbstractCoreController<Template>{

    private TemplateService  templateService;

    @Autowired
    public TemplateController(TemplateService  templateService) {
        super(templateService, Template.class);
        this.templateService = templateService;
    }


    /***
     * 根据分类查询模板数据
     * @param id:分类ID
     */
    @GetMapping(value = "/category/{id}")
    public Result<Template> findByCategoryId(@PathVariable(value = "id")Integer id){
        //调用Service查询
        Template template = templateService.findByCategoryId(id);
        return new Result<Template>(true, StatusCode.OK,"查询成功",template);
    }

}
