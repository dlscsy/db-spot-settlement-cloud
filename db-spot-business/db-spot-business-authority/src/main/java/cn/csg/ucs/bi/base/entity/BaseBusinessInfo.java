package cn.csg.ucs.bi.base.entity;

import cn.csg.core.common.annotation.DateField;
import cn.csg.core.common.annotation.DictionarieField;
import lombok.Data;

import javax.persistence.Column;
import java.io.Serializable;

@Data
public class BaseBusinessInfo implements Serializable {

    // 操作人，必填
    @Column(name = "OPERATOR")
    private String operator;

    // 操作时间，必填，格式：yyyy-mm-dd hh24:mi:ss
    @Column(name = "OPERATE_DATE")
    @DateField(NEEDDATERANGE = true)
    private String operateDate;

    // 操作人所在单位，必填
    @Column(name = "OPERATOR_COMPANY")
    @DictionarieField(
            TABLE_NAME = "S_ORG_CODE_INFO",
            RELATIONFIELD = "ORG_CODE",
            MAPPINGFIELD = "ORG_NAME",
            PROPERTY = "dictionaries",
            RELATION_COLUMN_ALIAS_NAME_SUFFIX = "_RV"
    )
    private String operatorCompany;
}
