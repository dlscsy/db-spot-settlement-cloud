package cn.csg.ucs.bi.business.entity;

import cn.csg.core.common.annotation.SetAlias;
import cn.csg.ucs.bi.base.entity.BaseBusinessInfo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 电网自身侧，客户侧（除合同/非合同）汇总表
 * 用于统计，是中间结果过程表
 */
@Data
@Table(name = "P_SELF_CLIENT_SUMMARY")
@SetAlias("p_self_client_summary")
public class P_SELF_CLIENT_SUMMARY extends BaseBusinessInfo {

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

    // 本月值，选填
    @Column(name = "MONTH_VALUE")
    private String monthValue;

    // 累计值，选填
    @Column(name = "TOTAL_VALUE")
    private String totalValue;
}
