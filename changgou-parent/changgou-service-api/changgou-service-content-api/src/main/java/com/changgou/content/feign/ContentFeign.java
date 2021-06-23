package com.changgou.content.feign;

import com.changgou.content.pojo.Content;
import entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * @author ljh
 * @version 1.0
 * @date 2020/11/13 16:07
 * @description 标题
 * @package com.changgou.content.feign
 */

@FeignClient(name="content",path = "/content")
public interface ContentFeign {
    //根据广告分类ID 获取广告列表数据
    @GetMapping(value = "/list/category/{id}")
    public Result<List<Content>> findByCategory(@PathVariable(name="id") Long id);
}
