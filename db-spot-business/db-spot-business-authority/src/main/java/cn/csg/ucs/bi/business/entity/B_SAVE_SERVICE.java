package cn.csg.ucs.bi.business.entity;

import cn.csg.core.common.annotation.DateField;
import cn.csg.core.common.annotation.SetAlias;
import cn.csg.core.common.annotation.ValidateExistField;
import cn.csg.ucs.bi.base.entity.BaseBusinessInfo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 节能服务项目信息表
 */
@Data
@Table(name = "B_SAVE_SERVICE")
@SetAlias("b_save_service")
public class B_SAVE_SERVICE extends BaseBusinessInfo {

    // 主键ID
    @Id
    @Column(name = "GUID")
    private String guid;

    // 统计月份，必填，格式：yyyy-mm
    @Column(name = "THEMONTH")
    @DateField(FORMAT = "yyyy-mm")
    @ValidateExistField
    private String theMonth;

    // 所属单位（所属供电局），必填
    @Column(name = "COMPANY")
    @ValidateExistField
    private String company;

    // 项目编码，必填，码表
    @Column(name = "PROJECT_CODE")
    private String projectCode;

    // 各节能服务对应的值
    @Column(name = "SERVICE_VALUE")
    private String serviceValue;

    // 数据来源，必填，0：页面手工录入，1：excel导入，2：营销系统同步，码表
    @Column(name = "DATASOURCE")
    private String datasource;

    // 数据状态，必填，0：未锁定，1：锁定，码表
    @Column(name = "DATA_STATE")
    private String dataState;

    // 项目ID，必填 （统一为一条数据）
    @Column(name = "PROJECT_ID")
    private String projectId;
}
