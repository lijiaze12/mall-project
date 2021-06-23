package com.changgou.search.pojo;


import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/** pojo  建立映射关系到es服务器中的document
 *  注解的方式实现以下的功能：
 *      创建索引 创建类型  创建映射
 *      具体的映射：
 *          + 是否分词
 *          + 是否索引
 *          + 是否存储
 *          + 数据类型
 *          + 分词器是什么
 *  @Document(indexName = "skuinfo",type = "docs")
 *
 *      document 用于和es服务器建立映射关系
 *           indexName 指定索引的名称
 *           type 指定索引里面的类型
 *      @Id 指定文档的唯一标识
 *      @Field(type = FieldType.Text, analyzer = "ik_smart",searchAnalyzer = "ik_smart",index = true,store = false)
 *      field 用于配置字段
 *          type 指定 字段的数据类型
 *          analyzer 指定字段的当建立倒排索引表的时候使用的分词器
 *          searchAnalzyer 指定字段的当实现搜索的时候使用的分词器 一般使用相同的分词器 （可以不用写）
 *          index  是否索引 默认是true 要索引
 *          store 是否存储  默认是false 不存储到lucene 存储到了es中的_source中
 *
 *
 * @author ljh
 * @version 1.0
 * @date 2020/11/14 10:47
 * @description 标题
 * @package PACKAGE_NAME
 */
@Document(indexName = "skuinfo",type = "docs")
public class SkuInfo implements Serializable {
    //spring data elasticsearch的注解
    @Id
    private Long id;

    //SKU名称
    @Field(type = FieldType.Text, analyzer = "ik_smart",searchAnalyzer = "ik_smart",index = true,store = false)
    private String name;

    //商品价格，单位为：元
    @Field(type = FieldType.Double)
    private Long price;

    //库存数量
    private Integer num;

    //商品图片
    private String image;

    //商品状态，1-正常，2-下架，3-删除
    private String status;

    //创建时间
    private Date createTime;

    //更新时间
    private Date updateTime;

    //是否默认
    private String isDefault;

    //SPUID
    private Long spuId;

    //商品分类ID
    private Long categoryId;

    //商品分类名称
    @Field(type = FieldType.Keyword)
    private String categoryName;

    //品牌名称
    @Field(type = FieldType.Keyword)
    private String brandName;

    //规格
    private String spec;

    //规格参数
    private Map<String,Object> specMap;




    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(String isDefault) {
        this.isDefault = isDefault;
    }

    public Long getSpuId() {
        return spuId;
    }

    public void setSpuId(Long spuId) {
        this.spuId = spuId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public Map<String, Object> getSpecMap() {
        return specMap;
    }

    public void setSpecMap(Map<String, Object> specMap) {
        this.specMap = specMap;
    }
}
