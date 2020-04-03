package cn.csg.ucs.bi.business.entity;

import cn.csg.core.common.annotation.SetAlias;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 燃煤工业锅炉分层燃烧项目信息表
 */
@Data
@Table(name = "B_COAL")
@SetAlias("b_coal")
public class B_COAL extends B_SELF_SAVE_BASE {

    // 主键ID
    @Id
    @Column(name = "GUID")
    private String guid;

    // 电网自身节电量节电力基本信息主键ID
    @Column(name = "PROJECT_ID")
    private String projectId;

    // 锅炉平均热效率(％)（改造前）
    @Column(name = "BEFORE_EFFICIENCY")
    private String beforeEfficiency;

    // 锅炉平均热效率(％)（改造后）
    @Column(name = "AFTER_EFFICIENCY")
    private String afterEfficiency;

    // 锅炉每小时耗原煤量（吨）（改造后）
    @Column(name = "AFTER_COAL_QUANTITY")
    private String afterCoalQuantity;

    // 设备年运行小时数（小时）
    @Column(name = "HOURS")
    private String hours;

    // 项目年节约电量（万千瓦时）
    @Column(name = "SAVE_QUANTITY")
    private String saveQuantity;
}
