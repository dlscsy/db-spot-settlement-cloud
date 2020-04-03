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
 * 合同能源管理项目
 */
@Data
@Table(name = "B_CONTRACT")
@SetAlias("b_contract")
public class B_CONTRACT extends BaseBusinessInfo {

    // 主键ID
    @Id
    @Column(name = "GUID")
    private String guid;

    @Column(name = "PROJECT_ID")
    private String projectId;
    
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

    // 分类，必填，码表
    @Column(name = "CATEGORY")
    @DictionarieField(
            TABLE_NAME = "S_CODE_INFO",
            RELATIONFIELD = "CODE_VALUE",
            MAPPINGFIELD = "CODE_NAME",
            ADDITIONAL = "CODE_TYPE = '3'",
            PROPERTY = "dictionaries",
            RELATION_COLUMN_ALIAS_NAME_SUFFIX = "_RV"
    )
    private String category;

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

    // 项目类型，必填，码表
    @Column(name = "PROJECT_TYPE")
    @DictionarieField(
            TABLE_NAME = "S_CODE_INFO",
            RELATIONFIELD = "CODE_VALUE",
            MAPPINGFIELD = "CODE_NAME",
            ADDITIONAL = "CODE_TYPE = '6'",
            PROPERTY = "dictionaries",
            RELATION_COLUMN_ALIAS_NAME_SUFFIX = "_RV"
    )
    private String projectType;

    // 项目竣工时间，必填，格式：yyyy-mm
    @Column(name = "PROJECT_COMPLETE_DATE")
    @DateField(FORMAT = "yyyy-mm")
    private String projectCompleteDate;

    // 项目施工单位名称，必填
    @Column(name = "IMPL_COMPANY")
    private String implCompany;

    // 诊断时间，必填，格式：yyyy-mm-dd
    @Column(name = "DIAGNOSIS_DATE")
    @DateField(FORMAT = "yyyy-mm-dd")
    private String diagnosisDate;

    // 技术分类，必填，码表
    @Column(name = "TECHNOLOGY_CATEGORY")
    @DictionarieField(
            TABLE_NAME = "S_CODE_INFO",
            RELATIONFIELD = "CODE_VALUE",
            MAPPINGFIELD = "CODE_NAME",
            ADDITIONAL = "CODE_TYPE = '5'",
            PROPERTY = "dictionaries",
            RELATION_COLUMN_ALIAS_NAME_SUFFIX = "_RV"
    )
    private String technologyCategory;

    // 合同开始时间（年月），必填，格式：yyyy-mm
    @Column(name = "CONTRACT_START_DATE")
    @DateField(FORMAT = "yyyy-mm")
    private String contractStartDate;

    // 合同截止时间（年月），必填，格式：yyyy-mm
    @Column(name = "CONTRACT_END_DATE")
    @DateField(FORMAT = "yyyy-mm")
    private String contractEndDate;

    // 是否经过第三方审核，必填，0：否，1：是，码表
    @Column(name = "EX_CHECK")
    @DictionarieField(
            TABLE_NAME = "S_CODE_INFO",
            RELATIONFIELD = "CODE_VALUE",
            MAPPINGFIELD = "CODE_NAME",
            ADDITIONAL = "CODE_TYPE = '11'",
            PROPERTY = "dictionaries",
            RELATION_COLUMN_ALIAS_NAME_SUFFIX = "_RV"
    )
    private String exCheck;

    // 口径类型，必填，0：口径一，1：口径二，码表
    @Column(name = "CALIBER_TYPE")
    @DictionarieField(
            TABLE_NAME = "S_CODE_INFO",
            RELATIONFIELD = "CODE_VALUE",
            MAPPINGFIELD = "CODE_NAME",
            ADDITIONAL = "CODE_TYPE = '4'",
            PROPERTY = "dictionaries",
            RELATION_COLUMN_ALIAS_NAME_SUFFIX = "_RV"
    )
    private String caliberType;

    // 投资金额（万元），必填
    @Column(name = "INVEST_AMOUNT")
    private String investAmount;

    // 年度用电规模（万千瓦时），必填
    @Column(name = "ELE_SCALE")
    private String eleScale;

    // 实际节约电量（万千瓦时），必填
    @Column(name = "SAVE_QUANTITY")
    private String saveQuantity;

    // 实际节约电力（万千瓦），必填
    @Column(name = "SAVE_POWER")
    private String savePower;

    // 折算后节约电量（万千瓦时），必填
    @Column(name = "SAVE_CONVERT_QUANTITY")
    private String saveConvertQuantity;

    // 折算后节约电力（万千瓦），必填
    @Column(name = "SAVE_CONVERT_POWER")
    private String saveConvertPower;

    // 实际节约电费（万元），必填
    @Column(name = "SAVE_COST")
    private String saveCost;

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

    //节能服务推动方式，选填，码表
    @Column(name = "SAVE_PROMOTE_TYPE")
    @DictionarieField(
            TABLE_NAME = "S_CODE_INFO",
            RELATIONFIELD = "CODE_VALUE",
            MAPPINGFIELD = "CODE_NAME",
            ADDITIONAL = "CODE_TYPE = '14'",
            PROPERTY = "dictionaries",
            RELATION_COLUMN_ALIAS_NAME_SUFFIX = "_RV"
    )
    private String savePromoteType;


    //口径二投资金额（万元）
    @Transient
    private String investAmount1;

    // 口径二年度用电规模（万千瓦时）
    @Transient
    private String eleScale1;

    // 口径二实际节约电量（万千瓦时）
    @Transient
    private String saveQuantity1;

    //口径二 实际节约电力（万千瓦）
    @Transient
    private String savePower1;

    // 口径二折算后节约电量（万千瓦时）
    @Transient
    private String saveConvertQuantity1;

    // 口径二折算后节约电力（万千瓦）
    @Transient
    private String saveConvertPower1;

    // 口径二实际节约电费（万元）
    @Transient
    private String saveCost1;

    // 口径二id
    @Transient
    private String guid1;

    // 字典映射集合
    private Map<String, String> dictionaries = new HashMap<String, String>();

    public Map<String, String> getDictionaries() {
        return dictionaries;
    }

    public void setDictionaries(Map<String, String> dictionaries) {
        this.dictionaries.putAll(dictionaries);
    }
}
