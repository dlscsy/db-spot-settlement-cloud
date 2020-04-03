package cn.csg.ucs.bi.common.mapper;

import cn.csg.ucs.bi.common.entity.S_CODE_INFO;
import cn.csg.ucs.bi.common.entity.S_FILE_INFO;
import cn.csg.ucs.bi.common.entity.S_ORG_CODE_INFO;
import cn.csg.ucs.bi.common.mapper.provider.FileMgtMapperProvider;
import cn.csg.ucs.bi.common.structure.TreeNode;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileMgtMapper {

    @Select("SELECT t.ZZBM, t.ZZMC FROM FW_ZZJG t WHERE t.SJZZBM = #{parentZzbm} ORDER BY t.ZZBM")
    @Results({
            @Result(column = "ZZBM", property = "value"),
            @Result(column = "ZZMC", property = "label"),
            @Result(column = "ZZBM", property = "childrenNodes", many = @Many(select = "cn.csg.ucs.bi.common.mapper.FileMgtMapper.getTreeZzjgs", fetchType = FetchType.EAGER))
    })
    List<TreeNode> getTreeZzjgs(@Param("parentZzbm") String parentZzbm);

    @SelectProvider(type = FileMgtMapperProvider.class, method = "getFileInfo")
    @Results({
            @Result(column = "FILE_ID", property = "fileId"),
            @Result(column = "FILE_NAME", property = "fileName"),
            @Result(column = "FILE_PATH", property = "filePath"),
            @Result(column = "PROJECT_CATEGORY", property = "projectCategory"),
            @Result(column = "COMPANY_CODE", property = "companyCode"),
            @Result(column = "COMPANY_NAME", property = "companyName"),
            @Result(column = "PROJECT_ID", property = "projectId"),
            @Result(column = "PROJECT_NAME", property = "projectName"),
            @Result(column = "OPERATOR", property = "operator"),
            @Result(column = "OPERATE_DATE", property = "operateDate"),
            @Result(column = "OPERATOR_COMPANY", property = "operatorCompany")
    })
    List<S_FILE_INFO> getFileInfo(String orgCode, String projectSide,String projectCategory,String projectId, String fileName);

    @InsertProvider(type = FileMgtMapperProvider.class, method = "addFileInfo")
    int addFileInfo(S_FILE_INFO obj);

    @DeleteProvider(type = FileMgtMapperProvider.class, method = "removeFileInfo")
    int removeFileInfo(String keys);

    @SelectProvider(type = FileMgtMapperProvider.class, method = "getAllFileInfoByOrgCode")
    @Results({
            @Result(column = "FILE_ID", property = "fileId"),
            @Result(column = "FILE_NAME", property = "fileName"),
            @Result(column = "FILE_PATH", property = "filePath"),
            @Result(column = "PROJECT_CATEGORY", property = "projectCategory"),
            @Result(column = "COMPANY_CODE", property = "companyCode"),
            @Result(column = "COMPANY_NAME", property = "companyName"),
            @Result(column = "PROJECT_ID", property = "projectId"),
            @Result(column = "PROJECT_NAME", property = "projectName"),
            @Result(column = "PROJECT_SIDE", property = "projectSide")
    })
    List<S_FILE_INFO> getAllFileInfoByOrgCode(String projectId);

    @Select("SELECT * FROM S_ORG_CODE_INFO t1 WHERE T1.ORG_CODE = #{orgCode}")
    @Results({
            @Result(column = "ORG_CODE", property = "orgCode"),
            @Result(column = "ORG_NAME", property = "orgName"),
            @Result(column = "ORG_SHORT_NAME", property = "orgShortName"),
            @Result(column = "PARENT_ORG_CODE", property = "parentOrgCode"),
            @Result(column = "ORG_CLASS", property = "orgClass")

    })
    S_ORG_CODE_INFO getOrgByOrgCode(@Param("orgCode") String orgCode);

}
