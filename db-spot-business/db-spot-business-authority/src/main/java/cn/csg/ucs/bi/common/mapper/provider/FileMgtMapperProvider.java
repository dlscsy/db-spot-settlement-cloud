package cn.csg.ucs.bi.common.mapper.provider;

import cn.csg.core.common.mapper.helper.CustomColumnsSQLHelper;
import cn.csg.core.common.mapper.helper.CustomInsertSQLHelper;
import cn.csg.core.common.structure.TableMeta;
import cn.csg.ucs.bi.common.entity.S_FILE_INFO;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;


public class FileMgtMapperProvider {

    private Logger logger = LoggerFactory.getLogger(FileMgtMapperProvider.class);

    public String getFileInfo(String orgCode, String projectSide,String projectCategory,String projectId, String fileName) {
       StringBuffer sbf = new StringBuffer("SELECT ");
       sbf.append("T1.FILE_ID,T1.FILE_NAME,T1.FILE_PATH,T1.PROJECT_CATEGORY,T1.COMPANY_CODE,T1.COMPANY_NAME,T1.PROJECT_ID,T1.PROJECT_NAME,T1.PROJECT_SIDE,T1.OPERATOR,T1.OPERATE_DATE,T1.OPERATOR_COMPANY");
       sbf.append(" FROM ");
       sbf.append("S_FILE_INFO t1 ");
       sbf.append("WHERE ");
//       sbf.append("T1.COMPANY_CODE = " + "'" + orgCode + "' ");
//       sbf.append(" AND T1.PROJECT_CATEGORY = " + "'"  + projectCategory + "' ");
//       sbf.append(" AND T1.PROJECT_SIDE = " + "'" + projectSide + "' ");
       sbf.append(" T1.PROJECT_ID = " + "'" + projectId + "' ");
       if (!StringUtils.isEmpty(fileName)) {
           sbf.append(" AND T1.FILE_NAME like '%" + fileName + "%'");
       }
       return sbf.toString();
    }
    public String addFileInfo(S_FILE_INFO obj) {
        TableMeta tableMeta = TableMeta.forClass(S_FILE_INFO.class);
        StringBuffer sql = new StringBuffer();
        sql.append(CustomInsertSQLHelper.getInsertSQL(JSONObject.parseObject(JSONObject.toJSONString(obj)),tableMeta));

        return sql.toString();
    }

    public String removeFileInfo(String keys) {
        StringBuffer sbf = new StringBuffer("DELETE FROM S_FILE_INFO t WHERE t.FILE_ID IN (" + keys + ")");
        return sbf.toString();
    }

    public String getAllFileInfoByOrgCode(String projectId) {
        StringBuffer sbf = new StringBuffer("SELECT ");
        TableMeta tableMeta = TableMeta.forClass(S_FILE_INFO.class);
        sbf.append(CustomColumnsSQLHelper.getBaseColumnsSQL(tableMeta));
        sbf.append(" FROM " + tableMeta.getTableName() + " " + tableMeta.getTableAliasName());
        sbf.append(" WHERE "+tableMeta.getTableAliasName()+".PROJECT_ID = " + "'" + projectId + "'");
        return sbf.toString();
    }
}
