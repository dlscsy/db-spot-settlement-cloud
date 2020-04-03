package cn.csg.ucs.bi.business.entity;

import cn.csg.core.common.annotation.*;
import cn.csg.ucs.bi.base.entity.BaseBusinessInfo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.HashMap;
import java.util.Map;

/**
 * 激励措施或新设备新技术降低负荷项目信息表
 */
@Data
@Table(name = "B_INCENTIVE")
@SetAlias("b_incentive")
public class B_INCENTIVE extends BaseBusinessInfo {

    // 主键ID
    @Id
    @Column(name = "GUID")
    private String guid;

    // 统计月份，必填，格式：yyyy-mm
    @Column(name = "THEMONTH")
    @DateField(FORMAT = "yyyy-mm")
    @ValidateExistField
    private String theMonth;

    // 所属单位（所属供电局），必填
    @Column(name = "COMPANY")
    @DictionarieField(
            TABLE_NAME = "S_ORG_CODE_INFO",
            RELATIONFIELD = "ORG_CODE",
            MAPPINGFIELD = "ORG_NAME",
            PROPERTY = "dictionaries",
            RELATION_COLUMN_ALIAS_NAME_SUFFIX = "_RV"
    )
    private String company;

    // 户号，必填
    @Column(name = "USER_NUM")
    @ValidateExistField
    private String userNum;

    // 客户名称，必填
    @Column(name = "USER_NAME")
    private String userName;

    // 用户所处行业，必填，码表
    @Column(name = "USER_TRADE_TYPE")
    @DictionarieField(
            TABLE_NAME = "S_TRADE_CODE_INFO",
            RELATIONFIELD = "CODE_VALUE",
            MAPPINGFIELD = "CODE_NAME",
            PROPERTY = "dictionaries",
            RELATION_COLUMN_ALIAS_NAME_SUFFIX = "_RV"
    )
    private String userTradeType;

    // 项目名称，必填
    @Column(name = "PROJECT_NAME")
    private String projectName;

    // 降低负荷手段，选填
    @Column(name = "REDUCE_METHOD")
    @DictionarieField(
            TABLE_NAME = "S_CODE_INFO",
            RELATIONFIELD = "CODE_VALUE",
            MAPPINGFIELD = "CODE_NAME",
            ADDITIONAL = "CODE_TYPE = '21'",
            PROPERTY = "dictionaries",
            RELATION_COLUMN_ALIAS_NAME_SUFFIX = "_RV"
    )
    private String reduceMethod;

    // 激励措施或应用新技术新设备内容，选填
    @Column(name = "INCENTIVE_CONTENT")
    private String incentiveContent;

    // 项目实施年月，选填，格式：yyyy-mm
    @Column(name = "IMPL_DATE")
    @DateField(FORMAT = "yyyy-mm")
    private String implDate;

    // 转移电量（万kWh），选填
    @Column(name = "TRANSFER_QUANTITY")
    private String transferQuantity;

    // 峰段时长（小时），选填
    @Column(name = "PEAK_DURATION")
    private String peakDuration;

    // 数据来源，必填，0：页面手工录入，1：excel导入，2：营销系统同步，码表
    @Column(name = "DATASOURCE")
    @DictionarieField(
            TABLE_NAME = "S_CODE_INFO",
            RELATIONFIELD = "CODE_VALUE",
            MAPPINGFIELD = "CODE_NAME",
            ADDITIONAL = "CODE_TYPE = 16",
            PROPERTY = "dictionaries",
            RELATION_COLUMN_ALIAS_NAME_SUFFIX = "_RV"
    )
    private String datasource;

    // 数据状态，必填，0：未锁定，1：锁定，码表
    @Column(name = "DATA_STATE")
    @DictionarieField(
            TABLE_NAME = "S_CODE_INFO",
            RELATIONFIELD = "CODE_VALUE",
            MAPPINGFIELD = "CODE_NAME",
            ADDITIONAL = "CODE_TYPE = 15",
            PROPERTY = "dictionaries",
            RELATION_COLUMN_ALIAS_NAME_SUFFIX = "_RV"
    )
    private String dataState;

    //年累计转移电量（万kWh）
    @Transient
    private String transferQuantityYear;

    //年累计峰段时长（h）
    @Transient
    private String peakDurationYear;

    //年累计转移负荷（万kW）
    @Transient
    private String transferLoadYear;

    // 字典映射集合
    private Map<String, String> dictionaries = new HashMap<String, String>();

    public Map<String, String> getDictionaries() {
        return dictionaries;
    }

    public void setDictionaries(Map<String, String> dictionaries) {
        this.dictionaries.putAll(dictionaries);
    }
}
