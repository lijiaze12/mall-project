package com.changgou.goods.service.impl;

import com.changgou.goods.dao.CommentMapper;
import com.changgou.goods.dao.HavezanMapper;
import com.changgou.goods.pojo.Comment;
import com.changgou.goods.pojo.Havezan;
import com.changgou.goods.service.HavezanService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/****
 * @Author:
 * @Description:Havezan业务层接口实现类
 * @Date 2019/6/14 0:16
 *****/
@Service
@Transactional
public class HavezanServiceImpl implements HavezanService {
    @Autowired
    private RedisTemplate redisTemplate;



    @Override
    public Result<Map> toRedis(Long cmtid, String username){
        //查询redis  返回的是用户是否点赞的数据
        String isZan= (String)redisTemplate.boundHashOps("Zan_" + username).get(cmtid);
        if(isZan==null){
            //没点过赞
            //实现点赞的逻辑  在redis中定义存储   namespace为 评价id    数据为点赞数据
          //  redisTemplate.boundValueOps("Comment"+cmtid).increment(1);


            //并且把redis中的记录改为已经点过赞了
            redisTemplate.boundHashOps("Zan_" + username).put(cmtid,"1");

            //redis中的点赞数 自增 并且返回自增数
            RedisAtomicLong counter = new RedisAtomicLong("Comment"+cmtid, Objects.requireNonNull(redisTemplate.getConnectionFactory()));
            // 获取值 并且自增1    获取到的值和存在库中的不一致
            long andIncrement = counter.getAndIncrement();
            // 直接获取值
            long l = counter.get();





            Map<String, Object> map = new HashMap<>();
            map.put("haveZan",false);
            map.put("ZanNum",l);
            return new Result<Map>(true, StatusCode.OK,"没有点过赞",map);
        }else {
            //点过赞
            //实现点赞数减一的逻辑
         //   redisTemplate.boundValueOps("Comment"+cmtid).decrement(1);


            //并且修改redis中的记录为未点赞
            redisTemplate.boundHashOps("Zan_"+username).delete(cmtid);

            //redis中评价数自减  并且返回当前评价数
            RedisAtomicLong counter = new RedisAtomicLong("Comment"+cmtid, Objects.requireNonNull(redisTemplate.getConnectionFactory()));
            long andDecrement = counter.getAndDecrement();
            // 直接获取值
            long l = counter.get();


            //查看对应商品的点赞数
          //  int num = (int)redisTemplate.boundValueOps("Comment" + cmtid).get();

            Map<String, Object> map = new HashMap<>();
            map.put("haveZan",true);
            map.put("ZanNum",l);

            return new Result(true,StatusCode.OK,"已经点过赞了",map);
        }

    }

















    @Autowired
    private HavezanMapper havezanMapper;


    /**
     * Havezan条件+分页查询
     * @param havezan 查询条件
     * @param page 页码
     * @param size 页大小
     * @return 分页结果
     */
    @Override
    public PageInfo<Havezan> findPage(Havezan havezan, int page, int size){
        //分页
        PageHelper.startPage(page,size);
        //搜索条件构建
        Example example = createExample(havezan);
        //执行搜索
        return new PageInfo<Havezan>(havezanMapper.selectByExample(example));
    }

    /**
     * Havezan分页查询
     * @param page
     * @param size
     * @return
     */
    @Override
    public PageInfo<Havezan> findPage(int page, int size){
        //静态分页
        PageHelper.startPage(page,size);
        //分页查询
        return new PageInfo<Havezan>(havezanMapper.selectAll());
    }

    /**
     * Havezan条件查询
     * @param havezan
     * @return
     */
    @Override
    public List<Havezan> findList(Havezan havezan){
        //构建查询条件
        Example example = createExample(havezan);
        //根据构建的条件查询数据
        return havezanMapper.selectByExample(example);
    }


    /**
     * Havezan构建查询对象
     * @param havezan
     * @return
     */
    public Example createExample(Havezan havezan){
        Example example=new Example(Havezan.class);
        Example.Criteria criteria = example.createCriteria();
        if(havezan!=null){
            // 用户账号名
            if(!StringUtils.isEmpty(havezan.getUsername())){
                    criteria.andLike("username","%"+havezan.getUsername()+"%");
            }
            // 主键id
            if(!StringUtils.isEmpty(havezan.getCmtId())){
                    criteria.andEqualTo("cmtId",havezan.getCmtId());
            }
        }
        return example;
    }

    /**
     * 删除
     * @param id
     */
    @Override
    public void delete(String id){
        havezanMapper.deleteByPrimaryKey(id);
    }

    /**
     * 修改Havezan
     * @param havezan
     */
    @Override
    public void update(Havezan havezan){
        havezanMapper.updateByPrimaryKey(havezan);
    }

    /**
     * 增加Havezan
     * @param havezan
     */
    @Override
    public void add(Havezan havezan){
        havezanMapper.insert(havezan);
    }

    /**
     * 根据ID查询Havezan
     * @param id
     * @return
     */
    @Override
    public Havezan findById(String id){
        return  havezanMapper.selectByPrimaryKey(id);
    }

    /**
     * 查询Havezan全部数据
     * @return
     */
    @Override
    public List<Havezan> findAll() {
        return havezanMapper.selectAll();
    }
}
