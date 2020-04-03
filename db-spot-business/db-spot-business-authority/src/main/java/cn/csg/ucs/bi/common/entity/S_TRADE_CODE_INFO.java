package cn.csg.ucs.bi.common.entity;


import cn.csg.core.common.annotation.SetAlias;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 用户所处行业编码表
 */
@Data
@Table(name = "S_TRADE_CODE_INFO")
@SetAlias("s_trade_code_info")
public class S_TRADE_CODE_INFO implements Serializable {

    // 唯一标识
    @Id
    @Column(name = "CODE_ID")
    private String codeId;

    // 编码类型
    @Column(name = "CODE_TYPE")
    private String codeType;

    // 编码值
    @Column(name = "CODE_VALUE")
    private String codeValue;

    // 编码对应内容
    @Column(name = "CODE_NAME")
    private String codeName;

    // 编码描述
    @Column(name = "CODE_DESC")
    private String codeDesc;

    // 上级编码
    @Column(name = "PARENT_CODE_ID")
    private String parentCodeId;

    // 编码顺序
    @Column(name = "CODE_ORDER")
    private String codeOrder;
}
