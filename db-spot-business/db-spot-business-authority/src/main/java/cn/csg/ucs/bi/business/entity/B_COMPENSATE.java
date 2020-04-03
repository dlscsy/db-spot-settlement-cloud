package cn.csg.ucs.bi.business.entity;

import cn.csg.core.common.annotation.SetAlias;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 变电站无功补偿项目信息表
 */
@Data
@Table(name = "B_COMPENSATE")
@SetAlias("b_compensate")
public class B_COMPENSATE extends B_SELF_SAVE_BASE {

    // 主键ID
    @Id
    @Column(name = "GUID")
    private String guid;

    // 电网自身节电量节电力基本信息主键ID
    @Column(name = "PROJECT_ID")
    private String projectId;

    // 无功补偿全投的容量值(kvar)
    @Column(name = "CAPACITY")
    private String capacity;

    // 电容器的介质损耗角正切值
    @Column(name = "TANGENT")
    private String tangent;

    // 无功经济当量
    @Column(name = "EQUIVALENT")
    private String equivalent;

    // 无功补偿装置在最大节电力情况下等效运行时间（小时）
    @Column(name = "HOURS")
    private String hours;

    // 理论年节约电量(万千瓦时)
    @Column(name = "THEORY_QUANTITY")
    private String theoryQuantity;

    // 折算年节约电量(万千瓦时)
    @Column(name = "CONVERT_QUANTITY")
    private String convertQuantity;
}
