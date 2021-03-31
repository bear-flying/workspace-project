package com.changgou.order.listener;

import com.changgou.order.service.OrderService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderTimeOutListener {

    @Autowired
    private OrderService orderService;

    //监听的是 死信交换机绑定的队列 专门用于接收延迟消息
    @RabbitListener(queues = "queue.ordertimeout")
    public void  receiveCloseOrderMessage(String message){
        System.out.println("接收到关闭订单的消息:"+message);

        try{
            //调用业务层完成关闭订单的操作
            orderService.closeOrder(message);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}