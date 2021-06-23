package com.changgou.content.controller;

import com.changgou.content.pojo.Content;
import com.changgou.content.service.ContentService;
import com.changgou.core.AbstractCoreController;
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
@RequestMapping("/content")
@CrossOrigin
public class ContentController extends AbstractCoreController<Content>{

    private ContentService  contentService;

    @Autowired
    public ContentController(ContentService  contentService) {
        super(contentService, Content.class);
        this.contentService = contentService;
    }

    /**
     * 根据分类的ID 获取该分类下的所有的广告的列表数据
     * @param id
     * @return
     */
    @GetMapping(value = "/list/category/{id}")
    public Result<List<Content>> findByCategory(@PathVariable(name="id") Long id){
        List<Content> contentList = contentService.findByCategory(id);
        return new Result<List<Content>>(true, StatusCode.OK,"查询成功",contentList);
    }
}
