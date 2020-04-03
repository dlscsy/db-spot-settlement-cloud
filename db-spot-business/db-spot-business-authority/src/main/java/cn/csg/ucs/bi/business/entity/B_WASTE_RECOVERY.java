package cn.csg.ucs.bi.business.entity;

import cn.csg.core.common.annotation.SetAlias;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 中央空调余热回收项目信息表
 */
@Data
@Table(name = "B_WASTE_RECOVERY")
@SetAlias("b_waste_recovery")
public class B_WASTE_RECOVERY extends B_SELF_SAVE_BASE {

    // 主键ID
    @Id
    @Column(name = "GUID")
    private String guid;

    // 电网自身节电量节电力基本信息主键ID
    @Column(name = "PROJECT_ID")
    private String projectId;

    // 加热生活热水流量（吨/小时）（改造后）
    @Column(name = "AFTER_FLOW")
    private String afterFlow;

    // 加热生活热水进口水温（℃）（改造后）
    @Column(name = "AFTER_TEMPERATURE_IMP")
    private String afterTemperatureImp;

    // 加热生活热水出口水温（℃）（改造后）
    @Column(name = "AFTER_TEMPERATURE_EXP")
    private String afterTemperatureExp;

    // 设备年运行小时数（小时）
    @Column(name = "HOURS")
    private String hours;

    // 项目年节约电量（万千瓦时）
    @Column(name = "SAVE_QUANTITY")
    private String saveQuantity;

    // 项目年节约电力（万千瓦）
    @Column(name = "SAVE_POWER")
    private String savePower;
}
