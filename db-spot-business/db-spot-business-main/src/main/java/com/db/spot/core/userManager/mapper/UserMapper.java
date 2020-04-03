package com.db.spot.core.userManager.mapper;

import com.db.spot.entity.User;
import com.db.spot.util.MyMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @创建人: 陈岳
 * @创建时间: 2020/4/3 11:42
 * @描述: 用户管理mapper
 */
@Repository
public interface UserMapper extends MyMapper<User> {

    int addInfo(User user);

    int updateInfo(User user);

    int deleteInfo(List<User> user);

    List<User> getAllInfo(User user);

}
