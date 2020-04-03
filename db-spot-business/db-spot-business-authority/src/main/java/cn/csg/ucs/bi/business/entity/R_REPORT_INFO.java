package cn.csg.ucs.bi.business.entity;

import cn.csg.core.common.annotation.SetAlias;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * 报表信息表
 */
@Data
@Table(name = "R_REPORT_INFO")
@SetAlias("r_report_info")
public class R_REPORT_INFO implements Serializable {

    // 报表ID
    @Id
    @Column(name = "REPORT_ID")
    private String reportId;

    // 报表编码
    @Column(name = "REPORT_CODE")
    private String reportCode;

    // 所属单位（所属供电局），必填
    @Column(name = "COMPANY")
    private String company;

    // 统计时间，必填，格式：yyyy-mm-dd hh24:mi:ss
    @Column(name = "STATISTICS_DATE")
    private String statisticsDate;

    // 统计操作人，必填
    @Column(name = "STATISTICS_OPERATOR")
    private String statisticsOperator;

    // 上报时间，选填，格式：yyyy-mm-dd hh24:mi:ss
    @Column(name = "SUBMIT_DATE")
    private String submitDate;

    // 统计月份，必填，格式：yyyy-mm
    @Column(name = "THEMONTH")
    private String theMonth;

    // 上报操作人，选填
    @Column(name = "SUBMIT_OPERATOR")
    private String submitOperator;

    // 统计状态，必填，0：未统计，1：已统计，码表
    @Column(name = "STATISTICS_STATE")
    private String statisticsState;

    // 报表上报状态，必填，0：未上报，1：已上报，2：退回，码表
    @Column(name = "SUBMIT_STATE")
    private String submitState;

    // 退回意见，选填
    @Column(name = "RETURN_OPINION")
    private String returnOpinion;

    //项目所属侧（取值实际与项目类别对应的codetype一致），必填，0：自身侧，1：客户侧
    @Column(name = "PROJECT_SIDE")
    private String projectSide;

}
