package com.changgou.pay.service.impl;

import com.changgou.pay.service.WeixinPayService;
import com.github.wxpay.sdk.WXPayUtil;
import entity.HttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ljh
 * @version 1.0
 * @date 2020/11/25 10:16
 * @description 标题
 * @package com.changgou.pay.service.impl
 */
@Service
public class WeixinPayServiceImpl implements WeixinPayService {

    //公众号ID
    @Value("${weixin.appid}")
    private String appid;

    //商户的ID
    @Value("${weixin.partner}")
    private String partner;

    //秘钥
    @Value("${weixin.partnerkey}")
    private String partnerkey;

    //通知地址
    @Value("${weixin.notifyurl}")
    private String notifyurl;


    @Override
    public Map<String, String> createNative(String out_trade_no, String total_fee) {
        try {
            //1.创建一个MAP对象
            Map<String,String> paramMap = new HashMap<String,String>();
            //2.将参数封装到map中
            paramMap.put("appid",appid);
            //商户号
            paramMap.put("mch_id",partner);

            //随机数
            paramMap.put("nonce_str", WXPayUtil.generateNonceStr());
            //签名 todo
            paramMap.put("body", "畅购的商品");
            //out_trade_no
            paramMap.put("out_trade_no", out_trade_no);
            //支付金额 total_fee
            paramMap.put("total_fee", total_fee);

            paramMap.put("spbill_create_ip", "127.0.0.1");

            paramMap.put("notify_url", notifyurl);

            paramMap.put("trade_type", "NATIVE");
            //3.将map转成XML 自动添加签名了
            String xmlParam = WXPayUtil.generateSignedXml(paramMap, partnerkey);
            //4.模拟浏览器发送https请求
            HttpClient client=new HttpClient("https://api.mch.weixin.qq.com/pay/unifiedorder");
            client.setHttps(true);//是否是https协议
            client.setXmlParam(xmlParam);//发送的xml数据
            client.post();//执行post请求
            //5.模拟浏览器接收微信支付系统返回的响应结果（code_url）
            String result = client.getContent(); //获取结果
            System.out.println(result);
            //6.封装map 返回给前端


            Map<String, String> stringStringMap = WXPayUtil.xmlToMap(result);
            Map<String,String> resultMap = new HashMap<>();
            resultMap.put("code_url",stringStringMap.get("code_url"));
            resultMap.put("out_trade_no",out_trade_no);
            resultMap.put("total_fee",total_fee);
            return resultMap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Map<String, String> queryPayStatus(String out_trade_no) {
        try {
            //1.创建一个MAP对象
            Map<String,String> paramMap = new HashMap<String,String>();
            //2.封装参数
            paramMap.put("appid",appid);
            //商户号
            paramMap.put("mch_id",partner);
            //随机数
            paramMap.put("nonce_str", WXPayUtil.generateNonceStr());
            paramMap.put("out_trade_no", out_trade_no);
            //3.将map转成XML 自动添加签名了
            String xmlParam = WXPayUtil.generateSignedXml(paramMap, partnerkey);

            //4.模拟浏览器发送https请求
            HttpClient client=new HttpClient("https://api.mch.weixin.qq.com/pay/orderquery");
            client.setHttps(true);//是否是https协议
            client.setXmlParam(xmlParam);//发送的xml数据
            client.post();//执行post请求
            //5.模拟浏览器接收微信支付系统返回的响应结果 支付相关的状态
            String result = client.getContent(); //获取结果
            System.out.println(result);
            //6.返回给前端
            Map<String, String> resultMap = WXPayUtil.xmlToMap(result);
            return resultMap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
