package cn.csg.ucs.bi.business.entity;

import cn.csg.core.common.annotation.DateField;
import cn.csg.core.common.annotation.DictionarieField;
import cn.csg.core.common.annotation.SetAlias;
import cn.csg.core.common.annotation.ValidateExistField;
import cn.csg.ucs.bi.base.entity.BaseBusinessInfo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.HashMap;
import java.util.Map;

/**
 * 客户侧节能改造发电项目信息表
 */
@Data
@Table(name = "B_CLIENT_SIDE")
@SetAlias("b_client_side")
public class B_CLIENT_SIDE extends BaseBusinessInfo {

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

    // 机组编号，必填
    @Column(name = "UNIT_CODE")
    private String unitCode;

    // 机组容量（万千瓦），选填
    @Column(name = "UNIT_CAPACITY")
    private String unitCapacity;

    // 投入运行时间，选填，格式：yyyy-mm-dd
    @Column(name = "WORKING_DATE")
    @DateField(FORMAT = "yyyy-mm-dd")
    private String workingDate;

    // 目录电价（万元/万千瓦时），选填
    @Column(name = "CATALOG_PRICE")
    private String catalogPrice;

    // 上网电价（万元/万千瓦时），选填
    @Column(name = "GRID_PRICE")
    private String gridPrice;

    // 机组发电量（万千瓦时），选填
    @Column(name = "UNIT_QUANTITY")
    private String unitQuantity;

    // 余额上网电量（万千瓦时），选填
    @Column(name = "RE_QUANTITY")
    private String reQuantity;

    // 余额上网电量（折算）（万千瓦时），选填
    @Column(name = "RE_CONVERT_QUANTITY")
    private String reConvertQuantity;

    // 企业自用电量（万千瓦时），选填
    @Column(name = "SELF_QUANTITY")
    private String selfQuantity;

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

    /**
     * 年累计机组发电量（万千瓦时）
     */
    @Transient
    private String unitQuantityYear;

    /**
     * 余额上网电量（万千瓦时）
     */
    @Transient
    private String reQuantityYear;

    /**
     * 余额上网电量（折算）（万千瓦时）
     */
    @Transient
    private String reConvertQuantityYear;

    /**
     * 企业自用电量（万千瓦时）
     */
    @Transient
    private String selfQuantityYear;

    // 字典映射集合
    private Map<String, String> dictionaries = new HashMap<String, String>();

    public Map<String, String> getDictionaries() {
        return dictionaries;
    }

    public void setDictionaries(Map<String, String> dictionaries) {
        this.dictionaries.putAll(dictionaries);
    }
}
