package cn.tedu.order.tx;

import cn.tedu.order.entity.Order;
import cn.tedu.order.feign.EasyIdGeneratorClient;
import cn.tedu.order.mapper.OrderMapper;
import cn.tedu.order.mapper.TxMapper;
import cn.tedu.order.service.OrderService;
import cn.tedu.order.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Primary
@Service
public class TxOrderService implements OrderService {
    @Autowired
    EasyIdGeneratorClient easyIdGeneratorClient;
    @Autowired
    private RocketMQTemplate rocketMQTemplate;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private TxMapper txMapper;

    /*
    创建订单的业务方法
    这里修改为：只向 Rocketmq 发送事务消息。
     */
    @Override
    public void create(Order order) {
        // 产生事务ID
        String xid = UUID.randomUUID().toString().replace("-", "");

        //对事务相关数据进行封装，并转成 json 字符串
        TxAccountMessage sMsg = new TxAccountMessage(order.getUserId(), order.getMoney(), xid);
        String json = JsonUtil.to(sMsg);

        //json字符串封装到 Spring Message 对象
        Message<String> msg = MessageBuilder.withPayload(json).build();

        //发送事务消息
        rocketMQTemplate.sendMessageInTransaction("order-topic:account", msg, order);
        log.info("事务消息已发送");
    }

    //本地事务，执行订单保存
    //这个方法在事务监听器中调用
    @Transactional
    public void doCreate(Order order, String xid) {
        log.info("执行本地事务，保存订单");

        // 从全局唯一id发号器获得id
        Long orderId = easyIdGeneratorClient.nextId("order_business");
        order.setId(orderId);

        orderMapper.create(order);

        log.info("订单已保存！ 事务日志已保存");
    }
}
