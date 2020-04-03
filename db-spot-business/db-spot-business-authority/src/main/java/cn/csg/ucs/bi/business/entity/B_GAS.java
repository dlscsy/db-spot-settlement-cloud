package cn.csg.ucs.bi.business.entity;

import cn.csg.core.common.annotation.SetAlias;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 燃气锅炉冷凝式余热回收项目信息表
 */
@Data
@Table(name = "B_GAS")
@SetAlias("b_gas")
public class B_GAS extends B_SELF_SAVE_BASE {

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

    // 平均每小时耗燃气量（Nm3/h）（改造后）
    @Column(name = "AFTER_GAS_QUANTITY")
    private String afterGasQuantity;

    // 设备年运行小时数（小时）
    @Column(name = "HOURS")
    private String hours;

    // 项目年节约电量（万千瓦时）
    @Column(name = "SAVE_QUANTITY")
    private String saveQuantity;
}
