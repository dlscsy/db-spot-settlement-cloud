package cn.csg.ucs.bi.business.entity.dto;

import lombok.Data;

/**
 * 项目信息汇总前端展示类
 * @author G.A.N
 * @Date 2019-12-19
 */
@Data
public class ProjectInfoSummaryDTO {

    // 单位编码
    private String orgCode;

    // 单位名称
    private String orgName;

    // 本月值
    private String monthValue;

    // 累计值
    private String totalValue;

}
