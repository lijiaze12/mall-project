package com.changgou.goods.controller;

import com.changgou.goods.pojo.Havezan;
import com.changgou.goods.service.HavezanService;
import com.github.pagehelper.PageInfo;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/****
 * @Author:
 * @Description:
 * @Date 2019/6/14 0:18
 *****/

@RestController
@RequestMapping("/havezan")
@CrossOrigin
public class HavezanController {
    //用户对商品评价的点赞要请求这个controller,返回改用户对改条评价是否点赞的确定  和  点完赞之后当前评价的 点赞数
    @GetMapping("/toRedis/{cmtid}")
    public Result toRedis(@PathVariable(name="cmtid")Long cmtid){
        String username ="lijiaze";
        Result result = havezanService.toRedis(cmtid, username);
        return  result;
    }


    @Autowired
    private HavezanService havezanService;

    /***
     * Havezan分页条件搜索实现
     * @param havezan
     * @param page
     * @param size
     * @return
     */
    @PostMapping(value = "/search/{page}/{size}" )
    public Result<PageInfo> findPage(@RequestBody(required = false) Havezan havezan, @PathVariable  int page, @PathVariable  int size){
        //调用HavezanService实现分页条件查询Havezan
        PageInfo<Havezan> pageInfo = havezanService.findPage(havezan, page, size);
        return new Result(true, StatusCode.OK,"查询成功",pageInfo);
    }

    /***
     * Havezan分页搜索实现
     * @param page:当前页
     * @param size:每页显示多少条
     * @return
     */
    @GetMapping(value = "/search/{page}/{size}" )
    public Result<PageInfo> findPage(@PathVariable  int page, @PathVariable  int size){
        //调用HavezanService实现分页查询Havezan
        PageInfo<Havezan> pageInfo = havezanService.findPage(page, size);
        return new Result<PageInfo>(true, StatusCode.OK,"查询成功",pageInfo);
    }

    /***
     * 多条件搜索品牌数据
     * @param havezan
     * @return
     */
    @PostMapping(value = "/search" )
    public Result<List<Havezan>> findList(@RequestBody(required = false) Havezan havezan){
        //调用HavezanService实现条件查询Havezan
        List<Havezan> list = havezanService.findList(havezan);
        return new Result<List<Havezan>>(true, StatusCode.OK,"查询成功",list);
    }

    /***
     * 根据ID删除品牌数据
     * @param id
     * @return
     */
    @DeleteMapping(value = "/{id}" )
    public Result delete(@PathVariable String id){
        //调用HavezanService实现根据主键删除
        havezanService.delete(id);
        return new Result(true, StatusCode.OK,"删除成功");
    }

    /***
     * 修改Havezan数据
     * @param havezan
     * @param id
     * @return
     */
    @PutMapping(value="/{id}")
    public Result update(@RequestBody Havezan havezan, @PathVariable String id){
        //设置主键值
        havezan.setUsername(id);
        //调用HavezanService实现修改Havezan
        havezanService.update(havezan);
        return new Result(true, StatusCode.OK,"修改成功");
    }

    /***
     * 新增Havezan数据
     * @param havezan
     * @return
     */
    @PostMapping
    public Result add(@RequestBody Havezan havezan){
        //调用HavezanService实现添加Havezan
        havezanService.add(havezan);
        return new Result(true, StatusCode.OK,"添加成功");
    }

    /***
     * 根据ID查询Havezan数据
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<Havezan> findById(@PathVariable String id){
        //调用HavezanService实现根据主键查询Havezan
        Havezan havezan = havezanService.findById(id);
        return new Result<Havezan>(true, StatusCode.OK,"查询成功",havezan);
    }

    /***
     * 查询Havezan全部数据
     * @return
     */
    @GetMapping
    public Result<List<Havezan>> findAll(){
        //调用HavezanService实现查询所有Havezan
        List<Havezan> list = havezanService.findAll();
        return new Result<List<Havezan>>(true, StatusCode.OK,"查询成功",list) ;
    }
}
