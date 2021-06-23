package com.changgou.search.service;

import java.util.Map;

/**
 * @author ljh
 * @version 1.0
 * @date 2020/11/14 11:24
 * @description 标题
 * @package com.changgou.search.service
 */
public interface SkuService {
    //导入入sku数据到ElasticSearch
    void importSku();

    //实现搜索的功能
    Map<String, Object> search(Map<String, String> searchMap);
}
