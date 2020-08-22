package cn.tedu.account.tx;

import cn.tedu.account.service.AccountService;
import cn.tedu.account.util.JsonUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RocketMQMessageListener(consumerGroup = "account-consumer-group", topic = "order-topic", selectorExpression = "account")
public class TxConsumer implements RocketMQListener<String> {
    @Autowired
    private AccountService accountService;

    @Override
    public void onMessage(String msg) {
        TxAccountMessage txAccountMessage = JsonUtil.from(msg, new TypeReference<TxAccountMessage>() {});
        log.info("收到消息： "+txAccountMessage);

        accountService.decrease(txAccountMessage.getUserId(), txAccountMessage.getMoney());
    }
}
