package cn.csg.ucs.bi.common.entity;

import cn.csg.core.common.annotation.DateField;
import cn.csg.core.common.annotation.DictionarieField;
import cn.csg.core.common.annotation.OrderConfig;
import cn.csg.core.common.annotation.SetAlias;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.HashMap;
import java.util.Map;

/**
 *  系统日志表
 */
@Data
@Table(name = "S_LOGIN_LOG")
@SetAlias("s_login_log")
@OrderConfig(FIELDS="OPERATION_TIME")
public class S_LOGIN_LOG {

    // 登录日志类型
    public static final String LOGTYPE_LOGIN = "1";

    // 操作日志类型
    public static final String LOGTYPE_OPERTION = "2";

    // 日志标识
    @Id
    @Column(name = "LOG_ID")
    private String logId;

    // 日志类型，1：登录日志；2：操作日志
    @Column(name = "LOG_TYPE")
    @DictionarieField(
            TABLE_NAME = "S_CODE_INFO",
            RELATIONFIELD = "CODE_VALUE",
            MAPPINGFIELD = "CODE_NAME",
            ADDITIONAL = "CODE_TYPE = 24",
            PROPERTY = "dictionaries",
            RELATION_COLUMN_ALIAS_NAME_SUFFIX = "_RV"
    )
    private String logType;

    // 账号
    @Column(name = "ACCOUNT")
    private String account;

    // 账号所在单位
    @Column(name = "ORG_CODE")
    @DictionarieField(
            TABLE_NAME = "S_ORG_CODE_INFO",
            RELATIONFIELD = "ORG_CODE",
            MAPPINGFIELD = "ORG_NAME",
            PROPERTY = "dictionaries",
            RELATION_COLUMN_ALIAS_NAME_SUFFIX = "_RV")
    private String orgCode;

    // 登录时间
    @Column(name = "LOGIN_TIME")
    @DateField(FORMAT = "yyyy-MM-dd hh24:mi:ss")
    private String loginTime;

    // 操作时间
    @Column(name = "OPERATION_TIME")
    @DateField(FORMAT = "yyyy-MM-dd hh24:mi:ss")

    private String operationTime;

    // 登录人IP地址
    @Column(name = "LOG_IP")
    private String logIp;

    // 操作内容
    @Column(name = "OPERATOR_CONTENT")
    private String operatorContent;

    // 备注
    @Column(name = "REMARK")
    private String remark;

    // 字典映射集合
    private Map<String, String> dictionaries = new HashMap<String, String>();

    public Map<String, String> getDictionaries() {
        return dictionaries;
    }

    public void setDictionaries(Map<String, String> dictionaries) {
        this.dictionaries.putAll(dictionaries);
    }

}
