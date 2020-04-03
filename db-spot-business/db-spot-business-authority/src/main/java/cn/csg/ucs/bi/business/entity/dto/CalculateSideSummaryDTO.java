package cn.csg.ucs.bi.business.entity.dto;

import lombok.Data;

/**
 * 计量报表前端展示类
 * @author G.A.N
 * @Date 2019-12-02
 */
@Data
public class CalculateSideSummaryDTO {

    // 报表ID
    private String reportId;

    // 报表名称
    private String reportName;

    //smartbi对应的节点id
    private String smartId;

    // 报表编码
    private String codeValue;

    // 报表年月
    private String theMonth;

    // 报表状态
    private String statisticsState;

    // 下级未上报个数
    private String noSubmit;

    // 下级已上报个数
    private String yesSubmit;

    // 统计人
    private String statisticsOperator;

    // 统计时间
    private String statisticsDate;

}
