package cn.csg.ucs.bi.business.service.require;

import cn.csg.ucs.bi.business.entity.P_SELF_CLIENT_SUMMARY;
import cn.csg.ucs.bi.business.entity.R_REPORT_INFO;
import cn.csg.ucs.bi.business.entity.dto.BatchSubmitDTO;
import cn.csg.ucs.bi.business.entity.dto.ReportSummaryDTO;
import cn.csg.ucs.bi.common.entity.S_STATISTICS_CONFIG;
import cn.csg.ucs.bi.common.entity.S_USER_INFO;

import java.util.List;

public interface ClientSideService {

    /**
     * 根据所属单位、统计年月获取报表信息
     * @param codeType 编码类型（电网自身节电量节电力侧报表编码类型：7  客户侧报表编码类型：8）
     * @param company 所属单位
     * @param theMonth 统计年月
     * @return 报表汇总信息List
     */
    List<ReportSummaryDTO> getReportSummary(String codeType, String projectSide, String company, String theMonth);

    /**
     * 报表统计方法（统计+新增报表信息）
     * @param codeType 编码类型（0：自身侧 1：客户侧）
     * @param company 所属单位
     * @param theMonth 统计年月
     * @param codeValues 报表编码集合
     * @param isBatchSubmit 是否是批量上报（是：用于批量上报的统计；否：用于普通统计）
     * @return 报表汇总信息List
     */
    Object statisticsReport(String codeType, String company, String theMonth, List<String> codeValues, S_USER_INFO userInfo,boolean isBatchSubmit);

    /**
     * 报表统计方法（仅统计）
     * @param codeType 编码类型（0：自身侧 1：客户侧）
     * @param company 所属单位
     * @param theMonth 统计年月
     * @param codeValues 报表编码集合
     * @param isBatchSubmit 是否是批量上报（是：用于批量上报的统计；否：用于普通统计）
     * @return 报表汇总信息List
     */
    Object onlyStatisticsReport(String codeType, String company, String theMonth, List<String> codeValues, S_USER_INFO userInfo,boolean isBatchSubmit);

    /**
     * 根据条件获取报表信息
     * @param company 所属单位
     * @param theMonth 统计年月
     * @param codeValues 报表编码集合
     * @return 报表信息
     */
    List<R_REPORT_INFO> getReportInfoByParams(String theMonth, String company, List<String> codeValues, String projectSide);

    /**
     * 根据条件获取报表中间计算结果信息
     * @param company 所属单位
     * @param theMonth 统计年月
     * @param projectSide 项目所属侧（0：自身侧 1：客户侧）
     * @param codeValues 报表编码集合
     * @return 报表信息
     */
    List<P_SELF_CLIENT_SUMMARY> getSelfClientSummaryByParams(String theMonth, String company, List<String> codeValues, String projectSide);

    /**
     * 批量插入报表信息
     * @param reportInfoList 报表信息实体集合
     * @return 影响行数
     */
    int insertBatchReportInfo(List<R_REPORT_INFO> reportInfoList);

    /**
     * 根据条件获取统计配置信息集合
     * @param codeType 编码类别(0:自身侧 1：客户侧)
     * @param codeValues 报表编码集合
     * @return 统计配置信息集合
     */
    List<S_STATISTICS_CONFIG> getStatisticsConfigList(String codeType, List<String> codeValues);

    /**
     * @description 根据条件删除报表信息
     * @param theMonth 统计年月
     * @param company 所属单位
     * @param codeValues 前端勾选的报表编码
     * @param projectSide 项目所属侧
     * @return 影响行数
     */
    int deleteReportInfo(String theMonth, String company, String codeValues, String projectSide);

    /**
     * @description 根据条件删除中间结果信息（除合同/非合同外）
     * @param theMonth 统计年月
     * @param company 所属单位
     * @param codeValues 前端勾选的报表编码
     * @param projectSide 项目所属侧
     * @return 影响行数
     */
    int deleteSelfClientSummary(String theMonth, String company, String codeValues, String projectSide);

    /**
     * @description 根据条件删除合同/非合同中间结果信息
     * @param theMonth 统计年月
     * @param company 所属单位
     * @param codeValues 前端勾选的报表编码
     * @return 影响行数
     */
    int deleteContractSummary(String theMonth, String company, String codeValues);

    /**
     * @description 上报功能
     * @param theMonth 统计年月
     * @param company 所属单位
     * @param codeValues 前端勾选的报表编码
     * @param projectSide 项目所属侧
     * @param userInfo 当前登录用户信息
     * @return 上报结果
     */
    int submitReport(String theMonth, String company, List<String> codeValues, String projectSide, S_USER_INFO userInfo);

    /**
     * @description 批量上报功能
     * @param theMonth 统计年月
     * @param company 所属单位
     * @param projectSide 项目所属侧
     * @param userInfo 当前登录用户信息
     * @return 上报结果
     */
    int batchSubmitReport(String theMonth, String company, List<String> codeValues, String projectSide, S_USER_INFO userInfo);
//    int batchSubmitReport(String theMonth, String company, String projectSide, S_USER_INFO userInfo);

    /**
     * @description 更新所有下属单位的报表状态（将已统计/未上报—>已统计/已上报）
     * @param theMonth 统计年月
     * @param company 所属单位
     * @param projectSide 项目所属侧
     * @param userInfo 当前登录用户信息
     * @return 更新影响行数
     */
    int updateAllCompanyReportInfo(String theMonth, String company, String projectSide, S_USER_INFO userInfo);

    /**
     * @description 更新单位的报表状态（将已统计/未上报—>已统计/已上报）
     * @param theMonth 统计年月
     * @param companys 所属单位集合
     * @param codeValue 报表编码
     * @param projectSide 项目所属侧
     * @param userInfo 当前登录用户信息
     * @return 更新影响行数
     */
    int updateSubmitState(String theMonth, String companys, String codeValue, String projectSide, S_USER_INFO userInfo);

    /**
     * @description 获取该月单位集合
     * @param theMonth 统计年月
     * @param codeValue 报表编码
     * @param projectSide 项目所属侧
     * @return 所属单位集合
     */
    List<String> getStatisticsedCompany(String theMonth, String codeValue, String projectSide, String stateType, String stateValue);

    /**
     * @description 获取未上报的的下级所属单位（用于批量上报功能）
     * @param theMonth 统计年月
     * @param codeValue 报表编码
     * @param projectSide 项目所属侧
     * @param company 所属单位
     * @return 所属单位集合
     */
    List<BatchSubmitDTO> getNoSubmitNextCompany(String theMonth, String codeValue, String projectSide, String company);

    /**
     * @description 获取未统计和未上报的报表编码集合（用于批量上报功能）
     * @param theMonth 统计年月
     * @param company 所属单位
     * @param codeType 报表编码类型
     * @param projectSide 项目所属侧
     * @return 未统计和未上报的报表编码集合
     */
    List<String> getNoStatistAndSubmitCodeValue(String theMonth, String company, String codeType, String projectSide);

    /**
     * @description 回退报表（用于回退功能）
     * @param theMonth 统计年月
     * @param company 所属单位
     * @param codeValues 报表编码
     * @param projectSide 项目所属侧
     * @param returnOpinion 回退意见
     * @param userInfo 用户信息
     * @return 回退成功/失败（0：失败 1：成功）
     */
    int reportReturn(String theMonth, String company, List<String>  codeValues, String projectSide, String returnOpinion, S_USER_INFO userInfo);

    /**
     * @description 获取所有未上报的下属单位（用于批量上报功能）
     * @param theMonth     统计年月
     * @param company      所属单位
     * @param codeValue    报表编码
     * @param projectSide  项目所属侧
     * @param isStatistics 是否统计（true:已统计 false:未统计）
     * @return 所有未上报的下属单位集合
     */
    List<String> getNoSubmitAllNextCompany(String theMonth, String company, String codeValue, String projectSide, boolean isStatistics);

    /**
     * @description 获取所有已上报的下属单位（用于批量上报功能）
     * @param theMonth     统计年月
     * @param company      所属单位
     * @param codeValue    报表编码
     * @param projectSide  项目所属侧
     * @return 所有未上报的下属单位集合
     */
    List<String> getSubmitAllNextCompany(String theMonth, String company, String codeValue, String projectSide);

    /**
     * @description 根据编码类别和报表编码获取表名
     * @param codeType 编码类别(0:自身侧 1：客户侧)
     * @param codeValues 报表编码集合
     * @return 统计配置信息集合
     */
    List<String> getTableByCodeValues(String codeType, List<String> codeValues);

    /**
     * @description 数据锁定
     * @param projectSide  项目所属侧（0：客户侧 1：自身侧）
     * @param codeValues  所选报表编码
     * @param theMonth  统计年月
     * @param company  所属单位
     * @param isLock  所是否锁定（true:锁定  false：解锁）
     * @return void
     */
    void lockOrUnlockData(String projectSide, List<String> codeValues, String theMonth, List<String> company,boolean isLock);
}
