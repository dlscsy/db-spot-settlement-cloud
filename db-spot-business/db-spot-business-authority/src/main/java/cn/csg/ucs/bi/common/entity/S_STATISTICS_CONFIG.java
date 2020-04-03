package cn.csg.ucs.bi.common.entity;

import cn.csg.core.common.annotation.SetAlias;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 系统统计类别配置表
 */
@Data
@Table(name = "S_STATISTICS_CONFIG")
@SetAlias("s_statistics_config")
public class S_STATISTICS_CONFIG implements Serializable {

    // 配置主键ID
    @Id
    @Column(name = "CONFIG_ID")
    private String configId;

    // 项目编码，必填，码表
    @Column(name = "PROJECT_CODE")
    private String projectCode;

    // 统计类别，必填，码表
    @Column(name = "STATISTICS_CATEGORY")
    private String statisticsCateGory;

    // 统计该项目该类别所要查询的数据库表名，必填
    @Column(name = "TABLE_NAME")
    private String tableName;

    // 统计该项目该类别所要查询的数据列，选填
    @Column(name = "COLUMN_NAME")
    private String columnName;

    // 统计方式，必填，0：本月，1：累计，2：本月及累计，码表
    @Column(name = "STATISTICS_TYPE")
    private String statisticsType;

}
