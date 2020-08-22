package cn.tedu.order.mapper;

import cn.tedu.order.tx.TxInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

public interface TxMapper extends BaseMapper<TxInfo> {
    Boolean exists(String xid);
}
