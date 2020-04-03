package cn.csg.ucs.bi.business.entity;

import cn.csg.core.common.annotation.SetAlias;
import cn.csg.ucs.bi.base.entity.BaseBusinessInfo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 合同，非合同汇总表
 * 用于统计，是中间结果过程表
 */
@Data
@Table(name = "P_SELF_CLIENT_SUMMARY")
@SetAlias("p_self_client_summary")
public class P_CONTRACT_SUMMARY extends BaseBusinessInfo {

    // 主键ID
    @Id
    @Column(name = "GUID")
    private String guid;

    // 统计月份，必填，格式：yyyy-mm
    @Column(name = "THEMONTH")
    private String theMonth;

    // 所属单位（所属供电局），必填
    @Column(name = "COMPANY")
    private String company;

    // 项目编码，必填，码表
    @Column(name = "PROJECT_CODE")
    private String projectCode;

    // 统计类别，必填，码表
    @Column(name = "STATISTICS_CATEGORY")
    private String statisticsCategory;

    // 口径类型，必填，0：口径一，1：口径二，码表
    @Column(name = "CALIBER_TYPE")
    private String caliberType;

    // 分类，必填，码表
    @Column(name = "CATEGORY")
    private String category;

    // 是否客户自身技术力量实施，必填，0：否，1：是，码表
    @Column(name = "EX_SELF_IMPL")
    private String exSelfImpl;

    // 本月值，选填
    @Column(name = "MONTH_VALUE")
    private String monthValue;

    // 累计值，选填
    @Column(name = "TOTAL_VALUE")
    private String totalValue;

    // 操作人，必填
    @Column(name = "OPERATOR")
    private String operator;

    // 操作时间，必填，格式：yyyy-mm-dd hh24:mi:ss
    @Column(name = "OPERATE_DATE")
    private String operateDate;

    // 操作人所在单位，必填
    @Column(name = "OPERATOR_COMPANY")
    private String operatorCompany;

    // 合同类型，必填，0：合同，1：非合同，码表
    @Column(name = "CONTRACT_TYPE")
    private String contractType;

}
