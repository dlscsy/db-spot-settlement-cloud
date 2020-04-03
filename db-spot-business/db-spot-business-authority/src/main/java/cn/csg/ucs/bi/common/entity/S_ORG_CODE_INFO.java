package cn.csg.ucs.bi.common.entity;

import cn.csg.core.common.annotation.SetAlias;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 组织机构编码表
 */
@Data
@Table(name = "S_ORG_CODE_INFO")
@SetAlias("s_org_code_info")
public class S_ORG_CODE_INFO implements Serializable {

    /**
     * 组织机构编码
     */
    @Id
    @Column(name = "ORG_CODE")
    private String orgCode;

    /**
     * 组织机构名称
     */
    @Column(name = "ORG_NAME")
    private String orgName;

    /**
     * 组织机构简称
     */
    @Column(name = "ORG_SHORT_NAME")
    private String orgShortName;

    /**
     * 上级组织机构编码
     */
    @Column(name = "PARENT_ORG_CODE")
    private String parentOrgCode;

    /**
     * 组织机构级别
     */
    @Column(name = "ORG_CLASS")
    private String orgClass;
}
