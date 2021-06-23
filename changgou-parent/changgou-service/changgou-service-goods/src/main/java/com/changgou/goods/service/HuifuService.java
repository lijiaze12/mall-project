package com.changgou.goods.service;

import com.changgou.goods.pojo.Huifu;
import com.github.pagehelper.PageInfo;

import java.util.List;

/****
 * @Author:
 * @Description:Huifu业务层接口
 * @Date 2019/6/14 0:16
 *****/
public interface HuifuService {

    List<Huifu> findByCmtId(int cmtId);

    /***
     * Huifu多条件分页查询
     * @param huifu
     * @param page
     * @param size
     * @return
     */
    PageInfo<Huifu> findPage(Huifu huifu, int page, int size);

    /***
     * Huifu分页查询
     * @param page
     * @param size
     * @return
     */
    PageInfo<Huifu> findPage(int page, int size);

    /***
     * Huifu多条件搜索方法
     * @param huifu
     * @return
     */
    List<Huifu> findList(Huifu huifu);

    /***
     * 删除Huifu
     * @param id
     */
    void delete(Integer id);

    /***
     * 修改Huifu数据
     * @param huifu
     */
    void update(Huifu huifu);

    /***
     * 新增Huifu
     * @param huifu
     */
    void add(Huifu huifu);

    /**
     * 根据ID查询Huifu
     * @param id
     * @return
     */
     Huifu findById(Integer id);

    /***
     * 查询所有Huifu
     * @return
     */
    List<Huifu> findAll();
}
