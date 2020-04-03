package cn.csg.ucs.bi.business.entity;

import cn.csg.core.common.annotation.SetAlias;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 项目信息汇总下拉框配置表
 */
@Data
@Table(name = "S_PROJECT_OPTION_CONFIG")
@SetAlias("s_project_option_config")
public class S_PROJECT_OPTION_CONFIG implements Serializable {

    // 关联关系ID
    @Id
    @Column(name = "GUID")
    private String guid;

    // 项目类别，码表，必填
    @Column(name = "PROJECT_TYPE")
    private String projectType;

    // 下拉选项名称，必填
    @Column(name = "OPTION_NAME")
    private String optionName;

    // 项目编码，码表，必填
    @Column(name = "PROJECT_CODE")
    private String projectCode;

    // 统计类别，码表，必填
    @Column(name = "STATISTICS_CATEGORY")
    private String statisticsCategory;

    // 分类（合同/非合同），码表，必填
    @Column(name = "CATEGORY")
    private String category;

    // 口径类型（合同/非合同），码表，必填，0：口径一  1：口径二
    @Column(name = "CALIBER_TYPE")
    private String caliberType;

}
