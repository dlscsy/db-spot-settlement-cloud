package cn.csg.ucs.bi.business.entity;

import cn.csg.core.common.annotation.SetAlias;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 高效变压器应用项目信息表
 */
@Data
@Table(name = "B_HE_TRANSFORMER")
@SetAlias("b_he_transformer")
public class B_HE_TRANSFORMER extends B_SELF_SAVE_BASE {

    // 主键ID
    @Id
    @Column(name = "GUID")
    private String guid;

    // 电网自身节电量节电力基本信息主键ID
    @Column(name = "PROJECT_ID")
    private String projectId;

    // 变压器型号及容量（改造前）
    @Column(name = "BEFORE_MODEL_NUM")
    private String beforeModelNum;

    // 容量（改造前）
    @Column(name = "BEFORE_CAPACITY")
    private String beforeCapacity;

    // 变压器空载损耗（kW）（改造前）
    @Column(name = "BEFORE_NOLOAD_LOSS")
    private String beforeNoloadLoss;

    // 变压器短路损耗（改造前）
    @Column(name = "BEFORE_SHORT_LOSS")
    private String beforeShortLoss;

    // 变压器型号及容量（改造后）
    @Column(name = "AFTER_MODEL_NUM")
    private String afterModelNum;

    // 容量（改造后）
    @Column(name = "AFTER_CAPACITY")
    private String afterCapacity;

    // 变压器空载损耗（kW）（改造后）
    @Column(name = "AFTER_NOLOAD_LOSS")
    private String afterNoloadLoss;

    // 变压器短路损耗（改造后）
    @Column(name = "AFTER_SHORT_LOSS")
    private String afterShortLoss;

    // 变压器平均负载率（改造后）
    @Column(name = "AFTER_AVG_LOAD_RATE")
    private String afterAvgLoadRate;

    // 理论年节约电量(万千瓦时)
    @Column(name = "THEORY_QUANTITY")
    private String theoryQuantity;

    // 折算年节约电量(万千瓦时)
    @Column(name = "CONVERT_QUANTITY")
    private String convertQuantity;
}
