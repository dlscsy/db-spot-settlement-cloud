package com.db.spot.core.userManager.service;

import com.db.spot.entity.User;
import com.sgcc.comm.model.PageQuery;
import com.sgcc.comm.util.ResultUtil;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @创建人: 陈岳
 * @创建时间: 2020/4/3 11:28
 * @描述: 用户管理接口
 */
@Service
public interface UserService {

    ResultUtil getAllInfo(PageQuery<User> pageQuery);

    ResultUtil addInfo(User user);

    ResultUtil deleteInfo(List<User> userList);

    ResultUtil updateInfo(User user);

}
