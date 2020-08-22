package cn.tedu.order.tx;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TxInfo {
    private String xid;
    private long created;
    private int status;
}
