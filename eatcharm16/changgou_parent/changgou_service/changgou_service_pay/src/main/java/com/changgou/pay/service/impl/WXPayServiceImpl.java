package com.changgou.pay.service.impl;

import com.changgou.pay.service.WXPayService;
import com.github.wxpay.sdk.WXPay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
public class WXPayServiceImpl implements WXPayService {

    @Autowired
    private WXPay wxPay;

    @Value("${wxpay.notify_url}")
    private String notify_url;

    //统一下单接口调用
    @Override
    public Map nativePay(String orderId, Integer money) {
        try {
            //1. 封装请求参数
            Map<String,String> map = new HashMap<>();
            map.put("body","畅购");
            map.put("out_trade_no",orderId);

            //前端传值是以元为单位 微信统一支付接口要求以分为单位
            BigDecimal payMoney = new BigDecimal("0.01");  //1分钱
            BigDecimal fen = payMoney.multiply(new BigDecimal("100")); //1.00  相当于用0.01*100 最终得到的值是1.00
            //第一个参数表示保留小数点后几位（0表示不保留）  第二个参数ROUND_UP表示向上取整
            fen = fen.setScale(0,BigDecimal.ROUND_UP); // 1
            map.put("total_fee",String.valueOf(fen));

            map.put("spbill_create_ip","127.0.0.1");
            map.put("notify_url",notify_url);
            map.put("trade_type","NATIVE");

            //2.基于wxpay完成统一下单接口的调用,并获取返回结果
            Map<String, String> result = wxPay.unifiedOrder(map);
            return result;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Map queryOrder(String orderId) {
        try{
            Map<String ,String> map = new HashMap();
            map.put("out_trade_no",orderId);
            Map<String, String> resultMap = wxPay.orderQuery(map);
            return resultMap;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Map closeOrder(String orderId) {
        try{
            Map<String,String> map = new HashMap();
            map.put("out_trade_no",orderId);
            Map<String, String> resultMap = wxPay.closeOrder(map);
            return resultMap;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

}
