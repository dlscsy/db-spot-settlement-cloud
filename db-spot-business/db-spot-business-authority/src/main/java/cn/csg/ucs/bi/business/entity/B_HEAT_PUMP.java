package cn.csg.ucs.bi.business.entity;

import cn.csg.core.common.annotation.SetAlias;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 水（地）源热泵项目信息表
 */
@Data
@Table(name = "B_HEAT_PUMP")
@SetAlias("b_heat_pump")
public class B_HEAT_PUMP extends B_SELF_SAVE_BASE {

    // 主键ID
    @Id
    @Column(name = "GUID")
    private String guid;

    // 电网自身节电量节电力基本信息主键ID
    @Column(name = "PROJECT_ID")
    private String projectId;

    // 热泵主机平均耗电功率（千瓦）（改造后）
    @Column(name = "AFTER_AVG_POWER_RATE")
    private String afterAvgPowerRate;

    // 设备年运行小时数（小时）
    @Column(name = "HOURS")
    private String hours;

    // cop（热电比）
    @Column(name = "COP")
    private String cop;

    // 项目年节约电量（万千瓦时）
    @Column(name = "SAVE_QUANTITY")
    private String saveQuantity;

    // 项目年节约电力（万千瓦）
    @Column(name = "SAVE_POWER")
    private String savePower;
}
