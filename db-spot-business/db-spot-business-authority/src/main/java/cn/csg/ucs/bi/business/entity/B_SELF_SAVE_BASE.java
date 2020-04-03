package cn.csg.ucs.bi.business.entity;

import cn.csg.core.common.annotation.*;
import cn.csg.ucs.bi.base.entity.BaseBusinessInfo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 电网自身节电量节电力基础信息表
 */
@Data
public class B_SELF_SAVE_BASE extends BaseBusinessInfo {

    // 统计月份，必填，格式：yyyy-mm
    @Column(name = "THEMONTH")
    @DateField(FORMAT = "yyyy-mm", NEEDDATERANGE = true)
    @ValidateExistField
    private String theMonth;

    // 项目名称，必填
    @Column(name = "PROJECT_NAME")
    @NeedLike
    @ValidateExistField
    private String projectName;

    // 节能量属性，必填，码表
    @Column(name = "SAVE_PROPERTY")
    @DictionarieField(
            TABLE_NAME = "S_CODE_INFO",
            RELATIONFIELD = "CODE_VALUE",
            MAPPINGFIELD = "CODE_NAME",
            ADDITIONAL = "CODE_TYPE = '10'",
            PROPERTY = "dictionaries",
            RELATION_COLUMN_ALIAS_NAME_SUFFIX = "_RV"
    )
    private String saveProperty;

    // 所属单位（所属供电局），必填
    @Column(name = "COMPANY")
    @DictionarieField(
            TABLE_NAME = "S_ORG_CODE_INFO",
            RELATIONFIELD = "ORG_CODE",
            MAPPINGFIELD = "ORG_NAME",
            PROPERTY = "dictionaries",
            RELATION_COLUMN_ALIAS_NAME_SUFFIX = "_RV"
    )
    @ValidateExistField
    @NeedLike(PREFIX = false,SUFFIX = true)
    private String company;

    // 项目类别，必填，码表
    @Column(name = "PROJECT_CATEGORY")
    @DictionarieField(
            TABLE_NAME = "S_CODE_INFO",
            RELATIONFIELD = "CODE_VALUE",
            MAPPINGFIELD = "CODE_NAME",
            ADDITIONAL = "CODE_TYPE = '0'",
            PROPERTY = "dictionaries",
            RELATION_COLUMN_ALIAS_NAME_SUFFIX = "_RV"
    )
    @ValidateExistField
    private String projectCategory;

    // 节能单位，必填，19个
    @Column(name = "SAVE_COMPANY")
    @NeedLike
    @DictionarieField(
            TABLE_NAME = "S_ORG_CODE_INFO",
            RELATIONFIELD = "ORG_CODE",
            MAPPINGFIELD = "ORG_NAME",
            PROPERTY = "dictionaries",
            RELATION_COLUMN_ALIAS_NAME_SUFFIX = "_RV"
    )
    private String saveCompany;

    // 行业类别，必填，码表
    @Column(name = "TRADE_CATEGORY")
    @DictionarieField(
            TABLE_NAME = "S_TRADE_CODE_INFO",
            RELATIONFIELD = "CODE_VALUE",
            MAPPINGFIELD = "CODE_NAME",
            PROPERTY = "dictionaries",
            RELATION_COLUMN_ALIAS_NAME_SUFFIX = "_RV"
    )
    private String tradeCategory;

    // 所属地区，必填，广东，固定值
    @Column(name = "AREA")
    private String area;

    // 是否合同能源管理，必填，0：否，1：是，码表
    @Column(name = "EX_CONTRACT")
    @DictionarieField(
            TABLE_NAME = "S_CODE_INFO",
            RELATIONFIELD = "CODE_VALUE",
            MAPPINGFIELD = "CODE_NAME",
            ADDITIONAL = "CODE_TYPE = '11'",
            PROPERTY = "dictionaries",
            RELATION_COLUMN_ALIAS_NAME_SUFFIX = "_RV"
    )
    private String exContract;

    // 项目总投资（万元），必填
    @Column(name = "PROJECT_TOTAL_INVEST")
    private String projectTotalInvest;

    // 是否节能量审核，必填，0：否，1：是，码表
    @Column(name = "EX_SAVE_CHECK")
    @DictionarieField(
            TABLE_NAME = "S_CODE_INFO",
            RELATIONFIELD = "CODE_VALUE",
            MAPPINGFIELD = "CODE_NAME",
            ADDITIONAL = "CODE_TYPE = '11'",
            PROPERTY = "dictionaries",
            RELATION_COLUMN_ALIAS_NAME_SUFFIX = "_RV"
    )
    private String exSaveCheck;

    // 节能量审核单位，选填
    @Column(name = "SAVE_CHECK_COMPANY")
    @NeedLike
    private String saveCheckCompany;

    // 投运年月，选填，格式：yyyy-mm
    @Column(name = "WORKING_DATE")
    @DateField(FORMAT = "yyyy-mm", NEEDDATERANGE = true)
    private String workingDate;

    // 项目编号，选填
    @Column(name = "PROJECT_NUM")
    private String projectNum;

    // 实施单位，选填
    @Column(name = "IMPL_COMPANY")
    private String implCompany;

    // 是否节能诊断，选填，0：否，1：是，码表
    @Column(name = "EX_SAVE_DIAGNOSE")
    @DictionarieField(
            TABLE_NAME = "S_CODE_INFO",
            RELATIONFIELD = "CODE_VALUE",
            MAPPINGFIELD = "CODE_NAME",
            ADDITIONAL = "CODE_TYPE = '11'",
            PROPERTY = "dictionaries",
            RELATION_COLUMN_ALIAS_NAME_SUFFIX = "_RV"
    )
    private String exSaveDiagnose;

    // 诊断时间，选填，格式：yyyy-mm-dd
    @Column(name = "DIAGNOSE_DATE")
    @DateField(FORMAT = "yyyy-mm-dd", NEEDDATERANGE = true)
    private String diagnoseDate;

    // 项目实施属性，选填，码表
    @Column(name = "PROJECT_IMPL_PROPERTY")
    @DictionarieField(
            TABLE_NAME = "S_CODE_INFO",
            RELATIONFIELD = "CODE_VALUE",
            MAPPINGFIELD = "CODE_NAME",
            ADDITIONAL = "CODE_TYPE = '13'",
            PROPERTY = "dictionaries",
            RELATION_COLUMN_ALIAS_NAME_SUFFIX = "_RV"
    )
    private String projectImplProperty;

    // 节能服务推动方式，选填，码表
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

    // 合同开始时间，选填，格式：yyyy-mm-dd
    @Column(name = "CONTRACT_START_DATE")
    @DateField(FORMAT = "yyyy-mm-dd")
    private String contractStartDate;

    // 合同截止时间，选填，格式：yyyy-mm-dd
    @Column(name = "CONTRACT_END_DATE")
    @DateField(FORMAT = "yyyy-mm-dd")
    private String contractEndDate;

    // 数据来源，必填，0：页面手工录入，1：excel导入，2：营销系统同步，码表
    @Column(name = "DATASOURCE")
    @DictionarieField(
            TABLE_NAME = "S_CODE_INFO",
            RELATIONFIELD = "CODE_VALUE",
            MAPPINGFIELD = "CODE_NAME",
            ADDITIONAL = "CODE_TYPE = '16'",
            PROPERTY = "dictionaries",
            RELATION_COLUMN_ALIAS_NAME_SUFFIX = "_RV"
    )
    private String dataSource;

    // 数据状态，必填，0：未锁定，1：锁定，码表
    @Column(name = "DATA_STATE")
    @DictionarieField(
            TABLE_NAME = "S_CODE_INFO",
            RELATIONFIELD = "CODE_VALUE",
            MAPPINGFIELD = "CODE_NAME",
            ADDITIONAL = "CODE_TYPE = '15'",
            PROPERTY = "dictionaries",
            RELATION_COLUMN_ALIAS_NAME_SUFFIX = "_RV"
    )
    private String dataState;
}
