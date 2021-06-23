package com.changgou.search.service;

import com.changgou.goods.pojo.Sku;
import com.changgou.goods.pojo.Spu;

import java.util.List;
import java.util.Map;

public interface SpuService {

    /***
     * 导入SpU数据到ElasticSearch
     */
    void importSpu();

    /***
     * 搜索 提供了根据关键词搜索的功能
     */
    Map search(Map<String, String> searchMap);
}