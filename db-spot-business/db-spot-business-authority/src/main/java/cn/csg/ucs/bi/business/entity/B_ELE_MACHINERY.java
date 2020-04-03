package cn.csg.ucs.bi.business.entity;

import cn.csg.core.common.annotation.SetAlias;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 电机系统节能项目信息表
 */
@Data
@Table(name = "B_ELE_MACHINERY")
@SetAlias("b_ele_machinery")
public class B_ELE_MACHINERY extends B_SELF_SAVE_BASE {

    // 主键ID
    @Id
    @Column(name = "GUID")
    private String guid;

    // 电网自身节电量节电力基本信息主键ID
    @Column(name = "PROJECT_ID")
    private String projectId;

    // 工频工况或原电机耗电功率(千瓦)（改造前）
    @Column(name = "BEFORE_POWER_RATE")
    private String beforePowerRate;

    // 变频工况或更换后电机耗电功率（千瓦）（改造后）
    @Column(name = "AFTER_POWER_RATE")
    private String afterPowerRate;

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
