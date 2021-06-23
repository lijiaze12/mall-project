package com.changgou.goods.service.impl;

import com.changgou.goods.dao.CommentMapper;
import com.changgou.goods.dao.HuifuMapper;
import com.changgou.goods.pojo.Comment;
import com.changgou.goods.pojo.Huifu;
import com.changgou.goods.service.HuifuService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/****
 * @Author:
 * @Description:Huifu业务层接口实现类
 * @Date 2019/6/14 0:16
 *****/
@Service
public class HuifuServiceImpl implements HuifuService {

    @Autowired
    private HuifuMapper huifuMapper;

    @Autowired
    private CommentMapper commentMapper;


    //根据评价id 查询回复列表数据
    @Override
    public List<Huifu> findByCmtId(int cmtId){


        Huifu huifu = new Huifu();
        huifu.setCmtId(cmtId);


        //搜索条件构建
        Example example = createExample(huifu);

        List<Huifu> huifus = huifuMapper.selectByExample(example);


        return huifus;
    }


    /**
     * Huifu条件+分页查询
     * @param huifu 查询条件
     * @param page 页码
     * @param size 页大小
     * @return 分页结果
     */
    @Override
    public PageInfo<Huifu> findPage(Huifu huifu, int page, int size){
        //分页
        PageHelper.startPage(page,size);
        //搜索条件构建
        Example example = createExample(huifu);
        //执行搜索
        return new PageInfo<Huifu>(huifuMapper.selectByExample(example));
    }

    /**
     * Huifu分页查询
     * @param page
     * @param size
     * @return
     */
    @Override
    public PageInfo<Huifu> findPage(int page, int size){
        //静态分页
        PageHelper.startPage(page,size);
        //分页查询
        return new PageInfo<Huifu>(huifuMapper.selectAll());
    }

    /**
     * Huifu条件查询
     * @param huifu
     * @return
     */
    @Override
    public List<Huifu> findList(Huifu huifu){
        //构建查询条件
        Example example = createExample(huifu);
        //根据构建的条件查询数据
        return huifuMapper.selectByExample(example);
    }


    /**
     * Huifu构建查询对象
     * @param huifu
     * @return
     */
    public Example createExample(Huifu huifu){
        Example example=new Example(Huifu.class);
        Example.Criteria criteria = example.createCriteria();
        if(huifu!=null){
            // 主键id
            if(!StringUtils.isEmpty(huifu.getId())){
                    criteria.andEqualTo("id",huifu.getId());
            }
            // 回复内容
            if(!StringUtils.isEmpty(huifu.getHuifuMsg())){
                    criteria.andEqualTo("huifuMsg",huifu.getHuifuMsg());
            }
            // 用户账号名
            if(!StringUtils.isEmpty(huifu.getUsername())){
                    criteria.andLike("username","%"+huifu.getUsername()+"%");
            }
            // 评价id
            if(!StringUtils.isEmpty(huifu.getCmtId())){
                    criteria.andEqualTo("cmtId",huifu.getCmtId());
            }
            // 0:显示 1:隐藏
            if(!StringUtils.isEmpty(huifu.getIsShow())){
                    criteria.andEqualTo("isShow",huifu.getIsShow());
            }
            // 记录生成时间
            if(!StringUtils.isEmpty(huifu.getAddTime())){
                    criteria.andEqualTo("addTime",huifu.getAddTime());
            }
            // 记录更新时间
            if(!StringUtils.isEmpty(huifu.getUpdTime())){
                    criteria.andEqualTo("updTime",huifu.getUpdTime());
            }
        }
        return example;
    }

    /**
     * 删除
     * @param id
     */
    @Override
    public void delete(Integer id){
        huifuMapper.deleteByPrimaryKey(id);
    }

    /**
     * 修改Huifu
     * @param huifu
     */
    @Override
    public void update(Huifu huifu){
        huifuMapper.updateByPrimaryKey(huifu);
    }

    /**
     * 增加Huifu
     * @param huifu
     */
    @Override
    public void add(Huifu huifu){

        huifuMapper.insert(huifu);

        Integer cmtId = huifu.getCmtId();
        Comment comment = commentMapper.selectByPrimaryKey(cmtId);
        Integer cmtHuifunum = comment.getCmtHuifunum();
        comment.setCmtHuifunum(cmtHuifunum+1);
        commentMapper.updateByPrimaryKey(comment);


    }

    /**
     * 根据ID查询Huifu
     * @param id
     * @return
     */
    @Override
    public Huifu findById(Integer id){
        return  huifuMapper.selectByPrimaryKey(id);
    }

    /**
     * 查询Huifu全部数据
     * @return
     */
    @Override
    public List<Huifu> findAll() {
        return huifuMapper.selectAll();
    }
}
