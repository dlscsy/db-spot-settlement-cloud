package cn.csg.ucs.bi.common.entity;

import cn.csg.core.common.annotation.SetAlias;
import cn.csg.ucs.bi.base.entity.BaseBusinessInfo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * 附件信息表
 */
@Data
@Table(name = "S_FILE_INFO")
@SetAlias("s_file_info")
public class S_FILE_INFO extends BaseBusinessInfo {

    // 文件ID
    @Column(name = "FILE_ID")
    private String fileId;

    // 文件名称
    @Column(name = "FILE_NAME")
    private String fileName;

    // 文件路径
    @Column(name = "FILE_PATH")
    private String filePath;

    // 项目编码，必填，码表
    @Column(name = "PROJECT_CATEGORY")
    private String projectCategory;

    // 所属单位编码（所属供电局），必填
    @Column(name = "COMPANY_CODE")
    private String companyCode;

    // 所属单位名称（所属供电局），必填
    @Column(name = "COMPANY_NAME")
    private String companyName;

    // 项目ID，必填
    @Column(name = "PROJECT_ID")
    private String projectId;

    // 项目名称，必填
    @Column(name = "PROJECT_NAME")
    private String projectName;

    // 项目所属侧（取值实际与项目类别对应的codetype一致），必填，0：自身侧，1：客户侧
    @Column(name = "PROJECT_SIDE")
    private String projectSide;
}
