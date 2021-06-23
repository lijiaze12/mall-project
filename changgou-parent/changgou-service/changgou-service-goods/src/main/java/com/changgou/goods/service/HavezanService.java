package com.changgou.goods.service;

import com.changgou.goods.pojo.Havezan;
import com.github.pagehelper.PageInfo;
import entity.Result;

import java.util.List;

/****
 * @Author:
 * @Description:Havezan业务层接口
 * @Date 2019/6/14 0:16
 *****/
public interface HavezanService {

    Result toRedis(Long spuid, String username);

    /***
     * Havezan多条件分页查询
     * @param havezan
     * @param page
     * @param size
     * @return
     */
    PageInfo<Havezan> findPage(Havezan havezan, int page, int size);

    /***
     * Havezan分页查询
     * @param page
     * @param size
     * @return
     */
    PageInfo<Havezan> findPage(int page, int size);

    /***
     * Havezan多条件搜索方法
     * @param havezan
     * @return
     */
    List<Havezan> findList(Havezan havezan);

    /***
     * 删除Havezan
     * @param id
     */
    void delete(String id);

    /***
     * 修改Havezan数据
     * @param havezan
     */
    void update(Havezan havezan);

    /***
     * 新增Havezan
     * @param havezan
     */
    void add(Havezan havezan);

    /**
     * 根据ID查询Havezan
     * @param id
     * @return
     */
     Havezan findById(String id);

    /***
     * 查询所有Havezan
     * @return
     */
    List<Havezan> findAll();
}
