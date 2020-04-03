package cn.csg.ucs.bi.core.demo.entity;

import cn.csg.core.common.annotation.*;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * TODO 码表变了
 * 样例实体
 */
@Data
@Table(name = "DEMO_USER_INFO")
@SetAlias("demo_user_info")
@OrderConfig(FIELDS = "REGDATE, USER_NAME")
public class Demo implements Serializable {
    // 用户ID
    @Id
    @Column(name = "USER_ID")
    private String userId;

    // 用户姓名
    @Column(name = "USER_NAME")
    @NeedLike
    private String userName;

    // 用户账户
    @Column(name = "ACCOUNT")
    @NeedLike
    @ValidateExistField
    private String account;

    // 用户密码
    @Column(name = "PWD")
    private String pwd;

    // 联系电话
    @Column(name = "CONTACT_NUM")
    private String contactNum;

    // 邮箱
    @Column(name = "EMAIL")
    private String email;

//    // 用户状态
//    @Column(name = "USER_STATE")
//    @ValueSetting(DEFAULT_VALUE = "1")
//    @DictionarieField(
//            TABLE_NAME = "S_CODE_INFO",
//            RELATIONFIELD = "CODE_VALUE",
//            MAPPINGFIELD = "CODE_NAME",
//            ADDITIONAL = "CODE_TYPE = 1",
//            PROPERTY = "dictionaries"
//    )
//    private String userState;

//    // 用户状态
//    @Column(name = "LOCK_STATE")
//    @ValueSetting(DEFAULT_VALUE = "0")
//    @DictionarieField(
//            TABLE_NAME = "S_CODE_INFO",
//            RELATIONFIELD = "CODE_VALUE",
//            MAPPINGFIELD = "CODE_NAME",
//            ADDITIONAL = "CODE_TYPE = 2",
//            PROPERTY = "dictionaries"
//    )
//    private String lockState;
//
//    // 用户性别
//    @Column(name = "SEX")
//    @DictionarieField(
//            TABLE_NAME = "S_CODE_INFO",
//            RELATIONFIELD = "CODE_VALUE",
//            MAPPINGFIELD = "CODE_NAME",
//            ADDITIONAL = "CODE_TYPE = 0",
//            PROPERTY = "dictionaries"
//    )
//    private String sex;

    // 注册日期
    @Column(name = "REGDATE")
    @DateField(FORMAT = "yyyy-mm-dd", NEEDDATERANGE = true)
    private String regdate;

    // 所属组织机构
    @Column(name = "ORG_CODE")
    @DictionarieField(
            TABLE_NAME = "S_ORG_CODE_INFO",
            RELATIONFIELD = "ORG_CODE",
            MAPPINGFIELD = "ORG_NAME",
            PROPERTY = "dictionaries"
    )
    private String orgCode;

    // 身份证号
    @Column(name = "IDENTIFICATION_NUM")
    private String identificationNum;

    // 密码输入错误次数
    @Column(name = "WRONG_NUM")
    @ValueSetting(DEFAULT_VALUE = "0")
    private String wrongNum;

    // 用户最后登录时间
    @Column(name = "LAST_LOGINTIME")
    @DateField
    private String lastLoginTime;

    // 操作人
    @Column(name = "OPERATOR")
    private String operator;

    // 操作时间
    @Column(name = "OPERATION_DATE")
    private String operationDate;

    // 字典映射集合
    private Map<String, String> dictionaries = new HashMap<String, String>();

    public Map<String, String> getDictionaries() {
        return dictionaries;
    }

    public void setDictionaries(Map<String, String> dictionaries) {
        this.dictionaries.putAll(dictionaries);
    }
}
