package cn.csg.ucs.bi.business.entity;

import cn.csg.core.common.annotation.SetAlias;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * smartbi节点id与报表编码关联关系表
 */
@Data
@Table(name = "S_SMARTBI_REPORT_REL")
@SetAlias("s_smartbi_report_rel")
public class S_SMARTBI_REPORT_REL implements Serializable {

    // 关联关系ID
    @Id
    @Column(name = "GUID")
    private String guid;

    // smartbi的报表节点ID,必填，来自smartbi节点id
    @Column(name = "SMART_ID")
    private String smartId;

    // 报表类型，必填，码表，7：自身侧，8：客户侧，22：计量侧
    @Column(name = "REPORT_TYPE")
    private String reportType;

    // 报表编码，必填，码表
    @Column(name = "REPORT_CODE")
    private String reportCode;

}
