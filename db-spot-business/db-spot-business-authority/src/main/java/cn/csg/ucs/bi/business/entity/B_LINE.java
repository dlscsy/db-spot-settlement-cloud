package cn.csg.ucs.bi.business.entity;

import cn.csg.core.common.annotation.SetAlias;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 线路改造项目信息表
 */
@Data
@Table(name = "B_LINE")
@SetAlias("b_line")
public class B_LINE extends B_SELF_SAVE_BASE {

    // 主键ID
    @Id
    @Column(name = "GUID")
    private String guid;

    // 电网自身节电量节电力基本信息主键ID
    @Column(name = "PROJECT_ID")
    private String projectId;

    // 导线型号（改造前）
    @Column(name = "BEFORE_MODEL_NUM")
    private String beforeModelNum;

    // 导线长度（km）（改造前）
    @Column(name = "BEFORE_LENGTH")
    private String beforeLength;

    // 单位线长导线电阻（Ω/km）（改造前）
    @Column(name = "BEFORE_RESISTANCE")
    private String beforeResistance;

    // 导线型号（改造后）
    @Column(name = "AFTER_MODEL_NUM")
    private String afterModelNum;

    // 单位线长导线电阻（Ω/km）（改造后）
    @Column(name = "AFTER_RESISTANCE")
    private String afterResistance;

    // 线路平均电流（A）（改造后）
    @Column(name = "AFTER_AVG_ELE_CURRENT")
    private String afterAvgEleCurrent;

    // 理论年节约电量(万千瓦时)
    @Column(name = "THEORY_QUANTITY")
    private String theoryQuantity;

    // 折算年节约电量(万千瓦时)
    @Column(name = "CONVERT_QUANTITY")
    private String convertQuantity;
}
