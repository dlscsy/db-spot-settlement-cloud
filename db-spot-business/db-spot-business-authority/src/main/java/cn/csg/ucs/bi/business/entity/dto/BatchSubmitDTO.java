package cn.csg.ucs.bi.business.entity.dto;

import lombok.Data;

/**
 * 批量上报前端展示类
 * @author G.A.N
 * @Date 2019-11-24
 */
@Data
public class BatchSubmitDTO {

    // 报表ID
    private String reportId;

    // 报表编码
    private String codeValue;

    // 所属单位名称
    private String orgName;

    // 所属单位简称
    private String orgShortName;

    // 报表状态
    private String statisticsState;

    // 上报状态
    private String submitState;

}
