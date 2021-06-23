package com.changgou.goods.service.impl;

import com.changgou.goods.dao.CommentMapper;
import com.changgou.goods.pojo.Comment;
import com.changgou.goods.service.CommentService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.*;

/****
 * @Author:
 * @Description:Comment业务层接口实现类
 * @Date 2019/6/14 0:16
 *****/
@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private RedisTemplate redisTemplate;


    /**
     * Comment条件+分页查询
     * @param comment 查询条件
     * @param page 页码
     * @param size 页大小
     * @return 分页结果
     */
    @Override
    public PageInfo<Comment> findPage(Comment comment, int page, int size){
        //分页
        PageHelper.startPage(page,size);
        //搜索条件构建
        Example example = createExample(comment);
        //执行搜索
        return new PageInfo<Comment>(commentMapper.selectByExample(example));
    }
    //根据spuid  分页查询 评价数据  同时计算出指标
    @Override
    public Map<String, Object> findBySpu(Long id, int page){
        String spuid = id.toString();

        //搜索条件构建
        Comment comment1 = new Comment();
        comment1.setSpuId(spuid);
        Example example = createExample(comment1);
        List<Comment> comments1 = commentMapper.selectByExample(example);
        //每页展示4条评论
        int size=4 ;
        //分页
        PageHelper.startPage(page,size);

        List<Comment> comments2 = new ArrayList<>();
        //从redis中获取点赞数量  并设置到comment对象中 。返回给前端展示
        //遍历每一条评价数据  获取评价id   根据评价id  到redis中获取该条评价的点赞数
        for (Comment comment :  commentMapper.selectByExample(example)) {
            Integer cmtid = comment.getId();
            //拼接名称空间  获取指定名称空间下的值
            RedisAtomicLong counter = new RedisAtomicLong("Comment"+cmtid, Objects.requireNonNull(redisTemplate.getConnectionFactory()));
            // 直接获取值
            long l = counter.get();
            //把点赞数据设置到comment对象中
            comment.setCmtZannum((int)l);

            comments2.add(comment);
        }


        String goodPrecent;
        int totalComments=0;
        int goodComments=0;
        int midComments=0;
        int badComments=0;
        for (Comment comment : comments1) {
            totalComments+=1;
            String cmtStar = comment.getCmtStar();
            if(cmtStar.equals("4")||cmtStar.equals("5")){
                //好评
                goodComments+=1;
            }else if(cmtStar.equals("3")){
                //中评
                midComments+=1;
            }else{
                //差评
                badComments+=1;
            }
        }
        if(totalComments!=0) {
            goodPrecent = (int) ((Double.valueOf(goodComments) / Double.valueOf(totalComments)) * 100) + "%";
        }else {
            goodPrecent= "0%";
        }
        Map<String, Object> map = new HashMap<>();
        map.put("goodPrecent",goodPrecent);
        map.put("totalComments",totalComments);
        map.put("goodComments",goodComments);
        map.put("midComments",midComments);
        map.put("badComments",badComments);


        PageInfo<Comment> pageInfo = new PageInfo<>(comments2);
        map.put("pageInfo",pageInfo);

        return map;
    }



    /**
     * Comment分页查询
     * @param page
     * @param size
     * @return
     */
    @Override
    public PageInfo<Comment> findPage(int page, int size){
        //静态分页
        PageHelper.startPage(page,size);
        //分页查询
        return new PageInfo<Comment>(commentMapper.selectAll());
    }

    /**
     * Comment条件查询
     * @param comment
     * @return
     */
    @Override
    public List<Comment> findList(Comment comment){
        //构建查询条件
        Example example = createExample(comment);
        //根据构建的条件查询数据
        return commentMapper.selectByExample(example);
    }


    /**
     * Comment构建查询对象
     * @param comment
     * @return
     */
    public Example createExample(Comment comment){
        Example example=new Example(Comment.class);
        Example.Criteria criteria = example.createCriteria();
        if(comment!=null){
            // 主键id
            if(!StringUtils.isEmpty(comment.getId())){
                    criteria.andEqualTo("id",comment.getId());
            }
            // 评价内容
            if(!StringUtils.isEmpty(comment.getCmtMsg())){
                    criteria.andEqualTo("cmtMsg",comment.getCmtMsg());
            }
            // 用户账号名
            if(!StringUtils.isEmpty(comment.getUsername())){
                    criteria.andLike("username","%"+comment.getUsername()+"%");
            }
            // 商品的id
            if(!StringUtils.isEmpty(comment.getSpuId())){
                    criteria.andEqualTo("spuId",comment.getSpuId());
            }
            // 评价星级
            if(!StringUtils.isEmpty(comment.getCmtStar())){
                    criteria.andEqualTo("cmtStar",comment.getCmtStar());
            }
            // 点赞次数
            if(!StringUtils.isEmpty(comment.getCmtZannum())){
                    criteria.andEqualTo("cmtZannum",comment.getCmtZannum());
            }
            // 回复次数
            if(!StringUtils.isEmpty(comment.getCmtHuifunum())){
                    criteria.andEqualTo("cmtHuifunum",comment.getCmtHuifunum());
            }
            // 商品图片列表
            if(!StringUtils.isEmpty(comment.getImages())){
                    criteria.andEqualTo("images",comment.getImages());
            }
            // 0:显示 1:隐藏
            if(!StringUtils.isEmpty(comment.getIsShow())){
                    criteria.andEqualTo("isShow",comment.getIsShow());
            }
            // 记录生产时间
            if(!StringUtils.isEmpty(comment.getAddTime())){
                    criteria.andEqualTo("addTime",comment.getAddTime());
            }
            // 记录更新时间
            if(!StringUtils.isEmpty(comment.getUpdTime())){
                    criteria.andEqualTo("updTime",comment.getUpdTime());
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
        commentMapper.deleteByPrimaryKey(id);
    }

    /**
     * 修改Comment
     * @param comment
     */
    @Override
    public void update(Comment comment){
        commentMapper.updateByPrimaryKey(comment);
    }

    /**
     * 增加Comment
     * @param comment
     */
    @Override
    public void add(Comment comment){
        commentMapper.insert(comment);
    }

    /**
     * 根据ID查询Comment
     * @param id
     * @return
     */
    @Override
    public Comment findById(Integer id){
        return  commentMapper.selectByPrimaryKey(id);
    }

    /**
     * 查询Comment全部数据
     * @return
     */
    @Override
    public List<Comment> findAll() {
        return commentMapper.selectAll();
    }
}
