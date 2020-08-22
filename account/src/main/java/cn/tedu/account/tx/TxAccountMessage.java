package cn.tedu.account.tx;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TxAccountMessage {
    Long userId;
    BigDecimal money;
    String xid;
}
