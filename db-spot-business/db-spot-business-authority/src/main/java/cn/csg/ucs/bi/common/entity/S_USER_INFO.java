package cn.csg.ucs.bi.common.entity;

import cn.csg.core.common.annotation.*;
import cn.csg.ucs.bi.base.entity.BaseBusinessInfo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户信息表
 */
@Data
@Table(name = "S_USER_INFO")
@SetAlias("s_user_info")
@OrderConfig(FIELDS = "REGDATE,USER_NAME")
public class S_USER_INFO extends BaseBusinessInfo {

    // 用户ID
    @Id
    @Column(name = "USER_ID")
    private String userId;

    // 用户姓名，必填
    @Column(name = "USER_NAME")
    @NeedLike
    private String userName;

    // 用户账号，必填，不能有相同的
    @Column(name = "ACCOUNT")
    @NeedLike
    @ValidateExistField
    private String account;

    // 用户密码，必填
    @Column(name = "PWD")
    private String pwd;

    // 联系电话，选填
    @Column(name = "CONTACT_NUM")
    private String contactNum;

    // 邮箱，选填
    @Column(name = "EMAIL")
    private String email;

    // TODO 用户性别，选填，码表
    @Column(name = "SEX")
//    @DictionarieField(
//            TABLE_NAME = "T_CODE",
//            RELATIONFIELD = "CODE_VALUE",
//            MAPPINGFIELD = "CODE_NAME",
//            ADDITIONAL = "CODE_TYPE = 0",
//            PROPERTY = "dictionaries"
//    )
    private String sex;

    // TODO 用户可用状态，必填，0：不可用，1：可用，码表
    @Column(name = "USER_STATE")
    @ValueSetting(DEFAULT_VALUE = "1")
//    @DictionarieField(
//            TABLE_NAME = "T_CODE",
//            RELATIONFIELD = "CODE_VALUE",
//            MAPPINGFIELD = "CODE_NAME",
//            ADDITIONAL = "CODE_TYPE = 1",
//            PROPERTY = "dictionaries"
//    )
    private String userState;

    // TODO 用户锁定状态，必填，0：未锁定，1：锁定，码表
    @Column(name = "LOCK_STATE")
    @ValueSetting(DEFAULT_VALUE = "0")
//    @DictionarieField(
//            TABLE_NAME = "T_CODE",
//            RELATIONFIELD = "CODE_VALUE",
//            MAPPINGFIELD = "CODE_NAME",
//            ADDITIONAL = "CODE_TYPE = 2",
//            PROPERTY = "dictionaries"
//    )
    private String lockState;

    // 注册日期，必填，格式：yyyy-mm-dd
    @Column(name = "REGDATE")
    @DateField(FORMAT = "yyyy-mm-dd", NEEDDATERANGE = true)
    private String regdate;

    // TODO 所属组织机构编码，选填
    @Column(name = "ORG_CODE")
    @DictionarieField(
            TABLE_NAME = "S_ORG_CODE_INFO",
            RELATIONFIELD = "ORG_CODE",
            MAPPINGFIELD = "ORG_NAME",
            PROPERTY = "dictionaries")
    private String orgCode;

    // TODO 数据来源，必填，0：页面手工录入，1：excel导入，2：营销系统同步，码表
    @Column(name = "DATASOURCE")
    private String datasource;

    // 身份证号，选填
    @Column(name = "IDENTIFICATION_NUM")
    private String identificationNum;

    // 密码输入错误次数，必填
    @Column(name = "WRONG_NUM")
    @ValueSetting(DEFAULT_VALUE = "0")
    private String wrongNum;

    // 用户最后登录时间，选填，格式：yyyy-mm-dd hh24:mi:ss
    @Column(name = "LAST_LOGINTIME")
    @DateField
    private String lastLoginTime;

    // 用户登录成功后的TOKEN信息，选填
    @Column(name = "TOKEN")
    private String token;

    // 用户登录成功后的TOKEN信息的失效时间，选填，格式：yyyy-mm-dd hh24:mi:ss
    @Column(name = "TOKEN_EXP_DATE")
    private String tokenExpDate;

    // 字典映射集合
    private Map<String, String> dictionaries = new HashMap<String, String>();

    public Map<String, String> getDictionaries() {
        return dictionaries;
    }

    public void setDictionaries(Map<String, String> dictionaries) {
        this.dictionaries.putAll(dictionaries);
    }
}
