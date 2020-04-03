package cn.csg.ucs.bi.common.mapper;

import cn.csg.core.common.mapper.helper.CustomMapTypeHandler;
import cn.csg.ucs.bi.common.entity.S_LOGIN_LOG;
import cn.csg.ucs.bi.common.mapper.provider.FileMgtMapperProvider;
import cn.csg.ucs.bi.common.mapper.provider.LogMapperProvider;
import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public interface LogMapper {

    @InsertProvider(type = LogMapperProvider.class, method = "addLog")
    public int addLog(S_LOGIN_LOG log);

    @SelectProvider(type = LogMapperProvider.class, method = "getLoggerByPage")
    @Results({
            @Result(column = "LOG_ID",property = "logId"),
            @Result(column = "LOG_TYPE",property = "logType"),
            @Result(column = "ACCOUNT",property = "account"),
            @Result(column = "ORG_CODE",property = "orgCode"),
            @Result(column = "LOGIN_TIME",property = "loginTime"),
            @Result(column = "OPERATION_TIME",property = "operationTime"),
            @Result(column = "LOG_IP",property = "logIp"),
            @Result(column = "OPERATOR_CONTENT",property = "operatorContent"),
            @Result(column = "REMARK",property = "remark"),
            @Result(column = "LOG_TYPE_RV", property = "dictionaries", typeHandler = CustomMapTypeHandler.class),
            @Result(column = "ORG_CODE_RV",property = "dictionaries",typeHandler = CustomMapTypeHandler.class)
    })
    List<S_LOGIN_LOG> getLoggerByPage(JSONObject json);

}
