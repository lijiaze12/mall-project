package com.changgou.goods.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * VO对象用来接收页面传递过来的大表单数据
 * @author ljh
 * @version 1.0
 * @date 2020/11/11 11:16
 * @description 标题
 * @package com.changgou.goods.pojo
 */
public class Goods implements Serializable {
    private Spu spu;//1  对
    private List<Sku> skuList;//N

    public Goods() {
    }

    public Goods(Spu spu, List<Sku> skuList) {
        this.spu = spu;
        this.skuList = skuList;
    }

    public Spu getSpu() {
        return spu;
    }

    public void setSpu(Spu spu) {
        this.spu = spu;
    }

    public List<Sku> getSkuList() {
        return skuList;
    }

    public void setSkuList(List<Sku> skuList) {
        this.skuList = skuList;
    }
}
