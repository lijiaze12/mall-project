package com.changgou.goods.controller;

import com.changgou.goods.pojo.Huifu;
import com.changgou.goods.service.HuifuService;
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
@RequestMapping("/huifu")
@CrossOrigin
public class HuifuController {

    @Autowired
    private HuifuService huifuService;

    /***
     * 新增Huifu数据  新增评价回复的数据的时候  需要实现对应评价id的回复数+1 的操作
     * @param huifu
     * @return
     */
    @PostMapping
    public Result add(@RequestBody Huifu huifu){

        System.out.println(huifu);
        //调用HuifuService实现添加Huifu
        huifuService.add(huifu);
        return new Result(true, StatusCode.OK,"添加成功");
    }

    /*
          点击回复评价的图标  需要根据评价id  查询回复表中的数据  回复表中评价id的字段名为cmtId;
     */
    @GetMapping("/findByCmtId/{cmtId}")
    public Result<List<Huifu>> findByCmtId(@PathVariable(name="cmtId")int cmtId){
        List<Huifu> huifuList = huifuService.findByCmtId(cmtId);
        return new Result<List<Huifu>>(true,StatusCode.OK,"查询评价回复数据成功",huifuList);

    }

    /***
     * Huifu分页条件搜索实现
     * @param huifu
     * @param page
     * @param size
     * @return
     */
    @PostMapping(value = "/search/{page}/{size}" )
    public Result<PageInfo> findPage(@RequestBody(required = false) Huifu huifu, @PathVariable  int page, @PathVariable  int size){
        //调用HuifuService实现分页条件查询Huifu
        PageInfo<Huifu> pageInfo = huifuService.findPage(huifu, page, size);
        return new Result(true, StatusCode.OK,"查询成功",pageInfo);
    }

    /***
     * Huifu分页搜索实现
     * @param page:当前页
     * @param size:每页显示多少条
     * @return
     */
    @GetMapping(value = "/search/{page}/{size}" )
    public Result<PageInfo> findPage(@PathVariable  int page, @PathVariable  int size){
        //调用HuifuService实现分页查询Huifu
        PageInfo<Huifu> pageInfo = huifuService.findPage(page, size);
        return new Result<PageInfo>(true, StatusCode.OK,"查询成功",pageInfo);
    }

    /***
     * 多条件搜索品牌数据
     * @param huifu
     * @return
     */
    @PostMapping(value = "/search" )
    public Result<List<Huifu>> findList(@RequestBody(required = false) Huifu huifu){
        //调用HuifuService实现条件查询Huifu
        List<Huifu> list = huifuService.findList(huifu);
        return new Result<List<Huifu>>(true, StatusCode.OK,"查询成功",list);
    }

    /***
     * 根据ID删除品牌数据
     * @param id
     * @return
     */
    @DeleteMapping(value = "/{id}" )
    public Result delete(@PathVariable Integer id){
        //调用HuifuService实现根据主键删除
        huifuService.delete(id);
        return new Result(true, StatusCode.OK,"删除成功");
    }

    /***
     * 修改Huifu数据
     * @param huifu
     * @param id
     * @return
     */
    @PutMapping(value="/{id}")
    public Result update(@RequestBody Huifu huifu, @PathVariable Integer id){
        //设置主键值
        huifu.setId(id);
        //调用HuifuService实现修改Huifu
        huifuService.update(huifu);
        return new Result(true, StatusCode.OK,"修改成功");
    }


    /***
     * 根据ID查询Huifu数据
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<Huifu> findById(@PathVariable Integer id){
        //调用HuifuService实现根据主键查询Huifu
        Huifu huifu = huifuService.findById(id);
        return new Result<Huifu>(true, StatusCode.OK,"查询成功",huifu);
    }

    /***
     * 查询Huifu全部数据
     * @return
     */
    @GetMapping
    public Result<List<Huifu>> findAll(){
        //调用HuifuService实现查询所有Huifu
        List<Huifu> list = huifuService.findAll();
        return new Result<List<Huifu>>(true, StatusCode.OK,"查询成功",list) ;
    }
}
