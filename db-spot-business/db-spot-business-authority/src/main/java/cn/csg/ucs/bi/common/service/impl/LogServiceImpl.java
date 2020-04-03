package cn.csg.ucs.bi.common.service.impl;

import cn.csg.core.common.utils.CommonUtils;
import cn.csg.ucs.bi.common.entity.S_LOGIN_LOG;
import cn.csg.ucs.bi.common.entity.S_USER_INFO;
import cn.csg.ucs.bi.common.mapper.LogMapper;
import cn.csg.ucs.bi.common.service.LogService;
import cn.csg.ucs.bi.utils.CommonUtil;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class LogServiceImpl implements LogService {

    @Autowired
    private LogMapper logMapper;

    @Override
    @Async
    public void addLog(String logType, String operatorContent, String remark, S_USER_INFO userInfo,String ip) {

        S_LOGIN_LOG log = new S_LOGIN_LOG();
        log.setAccount(userInfo.getAccount());
        log.setLogId(CommonUtils.createUUID());
        log.setOrgCode(userInfo.getOrgCode());
        log.setLogIp(ip);
        log.setLogType(logType);
        log.setOperatorContent(operatorContent);
        log.setRemark(remark);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if(logType.equals(S_LOGIN_LOG.LOGTYPE_LOGIN)){
            log.setLoginTime(sdf.format(new Date()));
        }
        log.setOperationTime(sdf.format(new Date()));
        logMapper.addLog(log);

    }

    @Override
    public List<S_LOGIN_LOG> getLoggerByPage(JSONObject json) {
        return logMapper.getLoggerByPage(json);
    }
}
