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
 * 高损变压器项目信息表
 */
@Data
@Table(name = "B_HL_TRANSFORMER")
@SetAlias("b_hl_transformer")
@OrderConfig(FIELDS="COMPANY",ORDERTYPE = "ASC")
public class B_HL_TRANSFORMER extends BaseBusinessInfo {

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

    // 高损变压器编号，必填
    @Column(name = "TRANSFORMER_CODE")
    private String transformerCode;

    // 高损变压器型号，必填
    @Column(name = "TRANSFORMER_MODEL_NUM")
    private String transformerModelNum;

    // 容量（万千伏安），必填
    @Column(name = "CAPACITY")
    private String capacity;

    // 改造或淘汰年月，选填，格式：yyyy-mm
    @Column(name = "RE_OR_EL_MONTH")
    @DateField(FORMAT = "yyyy-mm")
    private String reOrElMonth;

    // 改造后变压器型号，选填
    @Column(name = "REFORM_MODEL_NUM")
    private String reformModelNum;

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

    // 改造后年节约电量（万千瓦时）
    @Column(name = "SAVE_QUANTITY")
    private String saveQuantity;

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
