package cn.csg.ucs.bi.business.entity;

import cn.csg.core.common.annotation.*;
import cn.csg.ucs.bi.base.entity.BaseBusinessInfo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.HashMap;
import java.util.Map;

/**
 * 节能诊断明细项目信息表
 */
@Data
@Table(name = "B_DIAGNOSIS")
@SetAlias("b_diagnosis")
@OrderConfig(FIELDS="COMPANY",ORDERTYPE = "ASC")
public class B_DIAGNOSIS extends BaseBusinessInfo {

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
    @NeedLike(PREFIX = false,SUFFIX = true)
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

    // 诊断实施单位，必填
    @Column(name = "IMPL_COMPANY")
    private String implCompany;

    // 诊断类别，必填，码表，0：高级，1：常规
    @Column(name = "DIAGNOSIS_CATEGORY")
    @DictionarieField(
            TABLE_NAME = "S_CODE_INFO",
            RELATIONFIELD = "CODE_VALUE",
            MAPPINGFIELD = "CODE_NAME",
            ADDITIONAL = "CODE_TYPE = 18",
            PROPERTY = "dictionaries",
            RELATION_COLUMN_ALIAS_NAME_SUFFIX = "_RV"
    )
    private String diagnosisCategory;

    // 诊断时间，必填，格式：yyyy-mm-dd
    @Column(name = "DIAGNOSIS_DATE")
    @DateField(FORMAT = "yyyy-mm-dd")
    private String diagnosisDate;

    // 诊断费用（万元），必填
    @Column(name = "DIAGNOSIS_COST")
    private String diagnosisCost;

    // 诊断报告年节电量（万千瓦时）
    @Column(name = "SAVE_QUANTITY")
    private String saveQuantity;

    // 是否意向开展改造，选填，码表，0：否，1：是
    @Column(name = "EX_REFORM")
    @DictionarieField(
            TABLE_NAME = "S_CODE_INFO",
            RELATIONFIELD = "CODE_VALUE",
            MAPPINGFIELD = "CODE_NAME",
            ADDITIONAL = "CODE_TYPE = 11",
            PROPERTY = "dictionaries",
            RELATION_COLUMN_ALIAS_NAME_SUFFIX = "_RV"
    )
    private String exReform;

    // 是否意向开展合同能源管理，选填，码表，0：否，1：是
    @Column(name = "EX_CONTRACT")
    @DictionarieField(
            TABLE_NAME = "S_CODE_INFO",
            RELATIONFIELD = "CODE_VALUE",
            MAPPINGFIELD = "CODE_NAME",
            ADDITIONAL = "CODE_TYPE = 11",
            PROPERTY = "dictionaries",
            RELATION_COLUMN_ALIAS_NAME_SUFFIX = "_RV"
    )
    private String exContract;

    // 是否完成改造，选填，码表，0：否，1：是
    @Column(name = "EX_COMPLETE")
    @DictionarieField(
            TABLE_NAME = "S_CODE_INFO",
            RELATIONFIELD = "CODE_VALUE",
            MAPPINGFIELD = "CODE_NAME",
            ADDITIONAL = "CODE_TYPE = 11",
            PROPERTY = "dictionaries",
            RELATION_COLUMN_ALIAS_NAME_SUFFIX = "_RV"
    )
    private String exComplete;

    // 改造实施单位，选填
    @Column(name = "REFORM_COMPANY")
    private String reformCompany;

    // 改造实施方式，选填，码表
    @Column(name = "REFORM_TYPE")
    @DictionarieField(
            TABLE_NAME = "S_CODE_INFO",
            RELATIONFIELD = "CODE_VALUE",
            MAPPINGFIELD = "CODE_NAME",
            ADDITIONAL = "CODE_TYPE = 17",
            PROPERTY = "dictionaries",
            RELATION_COLUMN_ALIAS_NAME_SUFFIX = "_RV"
    )
    private String reformType;

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

    // 字典映射集合
    private Map<String, String> dictionaries = new HashMap<String, String>();

    public Map<String, String> getDictionaries() {
        return dictionaries;
    }

    public void setDictionaries(Map<String, String> dictionaries) {
        this.dictionaries.putAll(dictionaries);
    }
}
