package cn.csg.ucs.bi.business.entity;

import cn.csg.core.common.annotation.SetAlias;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 电蓄冷（热）项目信息表
 */
@Data
@Table(name = "B_ELE_STORAGE")
@SetAlias("b_ele_storage")
public class B_ELE_STORAGE extends B_SELF_SAVE_BASE {

    // 主键ID
    @Id
    @Column(name = "GUID")
    private String guid;

    // 电网自身节电量节电力基本信息主键ID
    @Column(name = "PROJECT_ID")
    private String projectId;

    // 电力高峰时段机组最大耗电功率（千瓦）（基期）
    @Column(name = "BEFORE_POWER_RATE")
    private String beforePowerRate;

    // 电力高峰时段机组最大耗电功率（千瓦）（报告期）
    @Column(name = "AFTER_POWER_RATE")
    private String afterPowerRate;

    // 项目年节约电力（万千瓦）
    @Column(name = "SAVE_POWER")
    private String savePower;
}
