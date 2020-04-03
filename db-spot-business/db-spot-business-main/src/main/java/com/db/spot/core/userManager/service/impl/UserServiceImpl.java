package com.db.spot.core.userManager.service.impl;

import com.db.spot.core.userManager.service.UserService;
import com.db.spot.entity.User;
import com.db.spot.core.userManager.mapper.UserMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sgcc.comm.model.PageQuery;
import com.sgcc.comm.util.ResultUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Random;

/**
 * @创建人: 陈岳
 * @创建时间: 2020/4/3 11:38
 * @描述: 用户管理实现类
 */
@Service
public class UserServiceImpl implements UserService {
	@Resource
	private UserMapper userMapper;

	/**
	 * 查询所有的信息
	 * @param pageQuery
	 * @return
	 */
	@Override
	public ResultUtil getAllInfo(PageQuery<User> pageQuery) {
		User user = pageQuery.getData();
		PageInfo pageInfo = pageQuery.getPageInfo();
		PageHelper.startPage(pageInfo.getPageNum(), pageInfo.getPageSize());
		List<User> list = userMapper.getAllInfo(user);
		return ResultUtil.success(new PageInfo(list));
	}

	/**
	 * 添加相应的信息
	 * @param user
	 * @return
	 */
	@Override
	public ResultUtil addInfo(User user) {
		ResultUtil resultUtil = new ResultUtil();
		if(user == null || "".equals(user)){
			resultUtil.setStatus(0);
			resultUtil.setMessage("参数不能为空");
			return resultUtil;
		}
		user.setId(new Random().nextInt(10000));
		int count = userMapper.addInfo(user);
		resultUtil.setMessage("添加成功");
		resultUtil.setStatus(0);
		resultUtil.setData(count);
		return resultUtil;
	}

	/**
	 * 根据id删除相应的信息
	 * @param userList
	 * @return
	 */
	@Override
	public ResultUtil deleteInfo(List<User> userList) {

		ResultUtil resultUtil = new ResultUtil();
		if(userList == null || userList.size()<=0){
			resultUtil.setStatus(0);
			resultUtil.setMessage("参数不能为空");
			return resultUtil;
		}
		int count = userMapper.deleteInfo(userList);
		resultUtil.setMessage("删除成功");
		resultUtil.setStatus(0);
		resultUtil.setData(count);
		return resultUtil;
	}

	/**
	 * 根据id修改相应的信息
	 * @param user
	 * @return
	 */
	@Override
	public ResultUtil updateInfo(User user) {
		ResultUtil resultUtil = new ResultUtil();
		if(user == null || "".equals(user)){
			resultUtil.setStatus(0);
			resultUtil.setMessage("参数不能为空");
			return resultUtil;
		}
		if(user.getId() == null || "".equals(user.getId())){
			resultUtil.setStatus(0);
			resultUtil.setMessage("id不能为空");
			return resultUtil;
		}
		int count = userMapper.updateInfo(user);
		resultUtil.setMessage("修改成功");
		resultUtil.setStatus(0);
		resultUtil.setData(count);
		return resultUtil;
	}
}
