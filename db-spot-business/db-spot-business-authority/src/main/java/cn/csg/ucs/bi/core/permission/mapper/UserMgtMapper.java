package cn.csg.ucs.bi.core.permission.mapper;

import cn.csg.core.common.mapper.BaseBusinessMapper;
import cn.csg.ucs.bi.common.entity.S_USER_INFO;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMgtMapper extends BaseBusinessMapper<S_USER_INFO> {

    @Update("UPDATE S_USER_INFO SET LOCK_STATE = '0', WRONG_NUM = '0', LAST_LOGINTIME = to_date(#{operateDate}, 'yyyy-mm-dd, hh24:mi:ss'), " +
            "TOKEN = #{token}, TOKEN_EXP_DATE = to_date(#{tokenExpDate}, 'yyyy-mm-dd, hh24:mi:ss'), " +
            "OPERATOR = #{operator}, OPERATE_DATE = to_date(#{operateDate}, 'yyyy-mm-dd, hh24:mi:ss'), " +
            "OPERATOR_COMPANY = #{operatorCompany} WHERE ACCOUNT = #{account}")
    int updateUserInfoAfterLoginSuccess(@Param("account") String account,
                                        @Param("token") String token,
                                        @Param("tokenExpDate") String tokenExpDate,
                                        @Param("operator") String operator,
                                        @Param("operateDate") String operateDate,
                                        @Param("operatorCompany") String operatorCompany);

    @Update("UPDATE S_USER_INFO SET TOKEN = '', TOKEN_EXP_DATE = NULL, " +
            "OPERATOR = #{operator}, OPERATE_DATE = to_date(#{operateDate}, 'yyyy-mm-dd, hh24:mi:ss'), " +
            "OPERATOR_COMPANY = #{operatorCompany} WHERE ACCOUNT = #{account}")
    int updateUserInfoAfterLogOutSuccess(@Param("account") String account,
                                         @Param("operator") String operator,
                                         @Param("operateDate") String operateDate,
                                         @Param("operatorCompany") String operatorCompany);

    @Update("UPDATE S_USER_INFO SET TOKEN_EXP_DATE = to_date(#{tokenExpDate}, 'yyyy-mm-dd, hh24:mi:ss'), " +
            "OPERATOR = #{operator}, OPERATE_DATE = to_date(#{operateDate}, 'yyyy-mm-dd, hh24:mi:ss'), " +
            "OPERATOR_COMPANY = #{operatorCompany} WHERE ACCOUNT = #{account}")
    int updateTokenExpDate(@Param("tokenExpDate") String tokenExpDate,
                           @Param("account") String account,
                           @Param("operator") String operator,
                           @Param("operateDate") String operateDate,
                           @Param("operatorCompany") String operatorCompany);

    @Update("UPDATE S_USER_INFO SET PWD = #{newPassword} WHERE ACCOUNT = #{account}" )
    int changePassword(@Param("account")String account, @Param("newPassword") String newPassword);

//    @Results({
//            @Result(column = "USER_ID",property = "userId"),
//            @Result(column = "USER_NAME",property = "userName"),
//            @Result(column = "ACCOUNT",property = "account"),
//            @Result(column = "PWD",property = "pwd"),
//            @Result(column = "CONTACT_NUM",property = "contactNum"),
//            @Result(column = "EMAIL",property = "email"),
//            @Result(column = "sex",property = "sex"),
//            @Result(column = "USER_STATE",property = "userState"),
//            @Result(column = "LOCK_STATE",property = "lockState"),
//            @Result(column = "REGDATE",property = "regdate"),
//            @Result(column = "DATASOURCE",property = "datasource"),
//            @Result(column = "IDENTIFICATION_NUM",property = "identificationNum"),
//            @Result(column = "WRONG_NUM",property = "wrongNum"),
//            @Result(column = "LAST_LOGINTIME",property = "lastLoginTime"),
//            @Result(column = "TOKEN",property = "token"),
//            @Result(column = "TOKEN_EXP_DATE",property = "tokenExpDate"),
//            @Result(column = "COMPANY_RV", property = "dictionaries", typeHandler = CustomMapTypeHandler.class),
//    })
//    List<S_USER_INFO> getUserByAccount(JSONObject jo);

    @Update("UPDATE S_USER_INFO SET WRONG_NUM = #{wrongNum}, " +
            "OPERATOR = #{updater}, OPERATE_DATE = to_date(#{updateDate}, 'yyyy-mm-dd, hh24:mi:ss') " +
            "WHERE ACCOUNT = #{account}")
    int updateWrongNum(@Param("account") String account, @Param("wrongNum") String wrongNum, @Param("updater") String updater, @Param("updateDate") String updateDate);

    @Select("SELECT s.WRONG_NUM FROM S_USER_INFO s WHERE s.ACCOUNT = #{account}")
    String getWrongNumByAccount(@Param("account") String account);

    @Update("UPDATE S_USER_INFO SET LOCK_STATE = '1', OPERATOR = #{updater}, OPERATE_DATE = to_date(#{updateDate}, 'yyyy-mm-dd, hh24:mi:ss') WHERE ACCOUNT = #{account}")
    int lockedUser(@Param("account") String account, @Param("updater") String updater, @Param("updateDate") String updateDate);

    @Update("UPDATE S_USER_INFO SET LOCK_STATE = '0',WRONG_NUM = '0',TOKEN = '', OPERATOR = #{updater}, OPERATE_DATE = to_date(#{updateDate}, 'yyyy-mm-dd, hh24:mi:ss') WHERE ACCOUNT = #{account}")
    int unlockedUser(@Param("account") String account, @Param("updater") String updater, @Param("updateDate") String updateDate);
}
