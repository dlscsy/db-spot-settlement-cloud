package cn.csg.ucs.bi.common.service;

import cn.csg.ucs.bi.common.entity.S_LOGIN_LOG;
import cn.csg.ucs.bi.common.entity.S_USER_INFO;
import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface LogService {

    /**
     * @Author chengzhifeng
     * @Description 新增日志记录
     * @Date 9:51 2020/1/13
     * @Param [logType, operatorContent, remark, req]
     * @return void
     **/
    public void addLog(String logType, String operatorContent, String remark, S_USER_INFO userInfo,String ip);

    /**
     * @Author chengzhifeng
     * @Description 
     * @Date 11:05 2020/1/13
     * @Param [json]
     * @return java.util.List<cn.csg.ucs.bi.common.entity.S_LOGIN_LOG>
     **/
    List<S_LOGIN_LOG> getLoggerByPage(JSONObject json);
}
