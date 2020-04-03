package com.db.spot.core.userManager.controller;

import com.db.spot.entity.User;
import com.db.spot.core.userManager.service.UserService;
import com.github.pagehelper.PageInfo;
import com.sgcc.comm.model.PageQuery;
import com.sgcc.comm.util.ResultUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @创建人: 陈岳
 * @创建时间: 2020/4/3 11:58
 * @描述: 用户管理
 */

@RequestMapping("/userController")
@RestController
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 获取所有的信息
     * @return
     */
    @PostMapping("/getAllInfo")
    public ResultUtil getAllInfo(@RequestBody PageQuery<User> pageQuery) {
        ResultUtil resultUtil = null;
        try {
            resultUtil = userService.getAllInfo(pageQuery);
        } catch (Exception e) {
        }
        return resultUtil;
    }

    @RequestMapping("/test")
    public String test() {
        return "1";
    }
    /**
     * 添加信息
     * @return
     */
    @PostMapping("/addInfo")
    public ResultUtil addInfo(@RequestBody User user){
        ResultUtil resultUtil = null;
        try {
            resultUtil =  userService.addInfo(user);
        }catch (Exception e){
        }
        return resultUtil;
    }
    /**
     * 删除信息
     * @return
     */
    @PostMapping("/deleteInfo")
    public ResultUtil deleteInfo(@RequestBody List<User> userList){
        ResultUtil resultUtil = null;
        try {
            resultUtil =  userService.deleteInfo(userList);
        }catch (Exception e){
        }
        return resultUtil;
    }
    /**
     * 修改信息
     * @return
     */
    @PostMapping("/updateInfo")
    public ResultUtil updateInfo(@RequestBody User user){
        ResultUtil resultUtil = null;
        try {
            resultUtil =  userService.updateInfo(user);
        }catch (Exception e){
        }
        return resultUtil;
    }
}
