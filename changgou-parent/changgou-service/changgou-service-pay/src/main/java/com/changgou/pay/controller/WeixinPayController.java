package com.changgou.pay.controller;

import com.alibaba.fastjson.JSON;
import com.changgou.pay.service.WeixinPayService;
import com.github.wxpay.sdk.WXPayUtil;
import entity.Result;
import entity.StatusCode;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

/**
 * @author ljh
 * @version 1.0
 * @date 2020/11/25 10:11
 * @description 标题
 * @package com.changgou.pay.controller
 */
@RestController
@RequestMapping("/weixin/pay")
public class WeixinPayController {

    @Autowired
    private WeixinPayService weixinPayService;

    /**
     * 生成二维码的接口
     *
     * @param out_trade_no 订单号
     * @param total_fee    金额（分）
     * @return {out_trade_no，total_fee，code_url}
     */
    @RequestMapping("/create/native")
    public Result<Map<String, String>> createNative(String out_trade_no, String total_fee) {
        Map<String, String> resultMap = weixinPayService.createNative(out_trade_no, total_fee);
        return new Result<Map<String, String>>(true, StatusCode.OK, "创建二维码链接成功", resultMap);
    }



    //查询订单的支付的状态


    /**
     * 根据订单号查询该订单的支付的状态
     *
     * @param out_trade_no
     * @return
     */
    @GetMapping(value = "/status/query")
    public Result queryStatus(String out_trade_no) {
        Map<String, String> resultMap = weixinPayService.queryPayStatus(out_trade_no);
        return new Result(true, StatusCode.OK, "查询状态成功", resultMap);
    }

    private static final String WX_SUCCESS = "<xml>\n" +
            "  <return_code><![CDATA[SUCCESS]]></return_code>\n" +
            "  <return_msg><![CDATA[OK]]></return_msg>\n" +
            "</xml>";
    private static final String WX_FAILE = "<xml>\n" +
            "  <return_code><![CDATA[FAIL]]></return_code>\n" +
            "  <return_msg><![CDATA[error]]></return_msg>\n" +
            "</xml>";

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${mq.pay.exchange.order}")
    private String exchange;
    @Value("${mq.pay.queue.order}")
    private String queue;
    @Value("${mq.pay.routing.key}")
    private String routing;

    //接收微信发送过来的请求（微信通知）
    //请求:/notify/url    POST /GET
    //参数：没有参数 一定没参数
    //返回值：String

    @RequestMapping(value = "/notify/url", method = {RequestMethod.POST, RequestMethod.GET})
    public String notifyurl(HttpServletRequest request) {
        try {
            //1.接收流信息
            ServletInputStream inputStream = request.getInputStream();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] bytes = new byte[1024];
            int len = 0;
            while((len=inputStream.read(bytes))!=-1){
                outputStream.write(bytes,0,len);
            }

            byte[] bytes1 = outputStream.toByteArray();
            String xml = new String(bytes1);
            System.out.println(xml);
            //结果
            Map<String, String> map = WXPayUtil.xmlToMap(xml);//1MB

            outputStream.close();
            inputStream.close();


            //2.发送消息
            //参数1 指定要发送的交换机  参数2 指定发送消息的routing key  参数3 消息本身
            rabbitTemplate.convertAndSend(exchange,routing, JSON.toJSONString(map));

            //3.返回给微信支付系统
            return WX_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return WX_FAILE;
    }






}
