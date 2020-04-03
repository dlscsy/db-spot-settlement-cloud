package cn.csg.ucs.bi.core.permission.service;

import cn.csg.ucs.bi.common.entity.S_USER_INFO;

public interface UserMgtService {

    /**
     * 根据用户账户获取用户信息
     *
     * @param account
     * @return
     */
    S_USER_INFO getUserByAccount(String account);

    /**
     * 登录成功后更新用户账户信息
     *
     * @param account
     * @param token
     * @param tokenExpDate
     * @param operator
     * @param operateDate
     * @param operatorCompany
     * @return
     */
    int updateUserInfoAfterLoginSuccess(String account, String token,
                                        String tokenExpDate, String operator,
                                        String operateDate, String operatorCompany);

    /**
     * 登出成功后更新用户账户信息
     *
     * @param account
     * @param operator
     * @param operateDate
     * @param operatorCompany
     * @return
     */
    int updateUserInfoAfterLogOutSuccess(String account, String operator, String operateDate, String operatorCompany);

    /**
     * 更新用户TOKEN的过期时间
     *
     * @param tokenExpDate
     * @param account
     * @param operator
     * @param operateDate
     * @param operatorCompany
     * @return
     */
    int updateTokenExpDate(String tokenExpDate, String account, String operator, String operateDate, String operatorCompany);

    /**
     * 修改密码
     * @param newPassword
     */
    int changePassword(String account,String newPassword);

    /**
     * 更新密码输入错误次数
     * @param account
     * @param wrongNum
     * @param updater
     * @param updateDate
     * @return
     */
    int updateWrongNum(String account, String wrongNum, String updater, String updateDate);

    /**
     * 获取用户密码输入次数
     * @param account
     */
    String getWrongNumByAccount(String account);

    /**
     * 锁定用户
     * @param account
     * @param updateDate
     */
    int lockedUser(String account, String updater, String updateDate);

    /**
     * 解锁用户
     * @param account
     * @param updateDate
     */
    int unlockedUser(String account, String updater, String updateDate);
}
