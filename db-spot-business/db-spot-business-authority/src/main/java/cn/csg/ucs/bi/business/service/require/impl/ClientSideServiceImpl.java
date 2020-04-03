package cn.csg.ucs.bi.business.service.require.impl;

import cn.csg.core.common.utils.CommonUtils;
import cn.csg.ucs.bi.business.entity.P_SELF_CLIENT_SUMMARY;
import cn.csg.ucs.bi.business.entity.R_REPORT_INFO;
import cn.csg.ucs.bi.business.entity.dto.BatchSubmitDTO;
import cn.csg.ucs.bi.business.entity.dto.ReportSummaryDTO;
import cn.csg.ucs.bi.business.mapper.require.ClientSideMapper;
import cn.csg.ucs.bi.business.service.require.ClientSideService;
import cn.csg.ucs.bi.common.entity.S_STATISTICS_CONFIG;
import cn.csg.ucs.bi.common.entity.S_USER_INFO;
import cn.csg.ucs.bi.common.service.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service("clientSideService")
public class ClientSideServiceImpl implements ClientSideService {

    //结果值：0 失败
    private static final int result_0 = 0;
    //结果值：1 成功
    private static final int result_1 = 1;
    //结果值：2 其他
    private static final int result_2 = 2;
    //合同：35（S_STATISTICS_CONFIG中project_code值）
    private static final String CONTRACT= "35";
    //非合同：36（S_STATISTICS_CONFIG中project_code值）
    private static final String UNCONTRACT= "36";
    //自身侧：0
    private static final String SELF_SIDE= "0";
    //合同：7（前端勾选合同报表的值）
    private static final String REPORT_CONTRACT= "7";
    //非合同：8（前端勾选非合同报表的值）
    private static final String REPORT_UNCONTRACT= "8";
    //自身侧：1
    private static final String CLIENT_SIDE= "1";


    @Autowired
    private ClientSideMapper clientSideMapper;

    @Autowired
    private CommonService commonService;

    @Override
    public List<ReportSummaryDTO> getReportSummary(String codeType, String projectSide, String company, String theMonth){
        return clientSideMapper.getReportSummary(codeType,projectSide,company,theMonth);
    }

    public Object statisticsInfo(String projectSide, String company, String theMonth, List<String> codeValues, S_USER_INFO userInfo) {
        //若当月该所属单位已统计，则先删除相关报表信息和中间结果数据
        deleteInfo(projectSide,company,theMonth,codeValues);
        return statisticsReport(projectSide,company,theMonth,codeValues,userInfo,false);
    }

    public void deleteInfo(String projectSide, String company, String theMonth, List<String> codeValues) {
        //若当月该所属单位已统计，则先删除相关报表信息和中间结果数据
        if(ClientSideServiceImpl.CLIENT_SIDE.equals(projectSide)){
            //客户侧删除逻辑
            List<String> contractCodeValueList = null;
            List<String> otherCodeValueList = null;
            //获取合同类报表编码
            contractCodeValueList = codeValues.stream().filter(codeValue ->
                    ClientSideServiceImpl.REPORT_CONTRACT.equals(codeValue) || ClientSideServiceImpl.REPORT_UNCONTRACT.equals(codeValue)
            ).collect(Collectors.toList());
            //获取除了合同类报表编码
            otherCodeValueList = codeValues.stream().filter(codeValue ->
                    !ClientSideServiceImpl.REPORT_CONTRACT.equals(codeValue) && !ClientSideServiceImpl.REPORT_UNCONTRACT.equals(codeValue)
            ).collect(Collectors.toList());
            if(otherCodeValueList != null && otherCodeValueList.size() > 0){
                deleteReportInfoAndResult(theMonth,company,String.join(",", otherCodeValueList),projectSide);
            }
            if(contractCodeValueList != null && contractCodeValueList.size() > 0) {
                deleteContractInfoAndResult(theMonth, company, String.join(",", contractCodeValueList),projectSide);
            }
        }else {
            //自身侧删除逻辑
            deleteReportInfoAndResult(theMonth,company,String.join(",", codeValues),projectSide);
        }
    }

    @Override
    public Object statisticsReport(String projectSide, String company, String theMonth, List<String> codeValues, S_USER_INFO userInfo,boolean isBatchSubmit) {
        List<S_STATISTICS_CONFIG> statisticsConfigList = null;
        //往报表信息插入数据的结果
        int addResult = 0;
        //判断有没有勾选第一个报表，即报表编码是否包含0，获取相应的统计配置类集合
        if(!codeValues.contains("0")){
            //没有勾选第一个报表
            statisticsConfigList = getStatisticsConfigList(projectSide,codeValues);
        }else{
            //勾选第一个报表
            statisticsConfigList = getStatisticsConfigList(projectSide,null);
        }
        //判断是否查询出统计配置
        if(statisticsConfigList == null || statisticsConfigList.size() == 0) return -1;
        if(ClientSideServiceImpl.CLIENT_SIDE.equals(projectSide)) {
            List<S_STATISTICS_CONFIG> contractStatisticsConfigList = null;
            List<S_STATISTICS_CONFIG> otherStatisticsConfigList = null;
            //暂存合同/非合同统计配置类
            contractStatisticsConfigList = statisticsConfigList.stream().filter(config ->
                    ClientSideServiceImpl.CONTRACT.equals(config.getProjectCode()) || ClientSideServiceImpl.UNCONTRACT.equals(config.getProjectCode())
            ).collect(Collectors.toList());
            //暂存除合同/非合同统计配置类
            otherStatisticsConfigList = statisticsConfigList.stream().filter(config ->
                    !ClientSideServiceImpl.CONTRACT.equals(config.getProjectCode()) && !ClientSideServiceImpl.UNCONTRACT.equals(config.getProjectCode())
            ).collect(Collectors.toList());
            if(contractStatisticsConfigList != null && contractStatisticsConfigList.size() > 0){
                statisticsConfigList = otherStatisticsConfigList;
                //将统计结果批量插入到合同/非合同中间表
                clientSideMapper.insertBatchContractProcess(contractStatisticsConfigList,theMonth,company,userInfo,codeValues);
            }
        }
        if(statisticsConfigList != null && statisticsConfigList.size() > 0){
            //将统计结果批量插入到中间表
            clientSideMapper.insertBatchProcess(statisticsConfigList,theMonth,company,userInfo,codeValues,projectSide);
        }
        //新增统计后的报表存入报表信息表
        addResult = addReportInfo(projectSide,company,theMonth,codeValues,userInfo,isBatchSubmit);
        return addResult;
    }

    @Override
    public Object onlyStatisticsReport(String projectSide, String company, String theMonth, List<String> codeValues, S_USER_INFO userInfo,boolean isBatchSubmit) {

        List<S_STATISTICS_CONFIG> statisticsConfigList = null;
        int statisticsResult = 0;
        int addResult = 0;
        int statisticsContractResult = 0;
        //判断有没有勾选第一个报表，即报表编码是否包含0，获取相应的统计配置类集合
        if(!codeValues.contains("0")){
            //没有勾选第一个报表
            statisticsConfigList = getStatisticsConfigList(projectSide,codeValues);
        }else{
            //勾选第一个报表
            statisticsConfigList = getStatisticsConfigList(projectSide,null);
        }
        if(ClientSideServiceImpl.CLIENT_SIDE.equals(projectSide)) {
            List<S_STATISTICS_CONFIG> contractStatisticsConfigList = null;
            List<S_STATISTICS_CONFIG> otherStatisticsConfigList = null;
            //暂存合同/非合同统计配置类
            contractStatisticsConfigList = statisticsConfigList.stream().filter(config ->
                    ClientSideServiceImpl.CONTRACT.equals(config.getProjectCode()) || ClientSideServiceImpl.UNCONTRACT.equals(config.getProjectCode())
            ).collect(Collectors.toList());
            //暂存除合同/非合同统计配置类
            otherStatisticsConfigList = statisticsConfigList.stream().filter(config ->
                    !ClientSideServiceImpl.CONTRACT.equals(config.getProjectCode()) && !ClientSideServiceImpl.UNCONTRACT.equals(config.getProjectCode())
            ).collect(Collectors.toList());
            if(contractStatisticsConfigList != null && contractStatisticsConfigList.size() > 0){
                statisticsConfigList = otherStatisticsConfigList;
                //将统计结果批量插入到合同/非合同中间表
                statisticsContractResult = clientSideMapper.insertBatchContractProcess(contractStatisticsConfigList,theMonth,company,userInfo,codeValues);
            }
        }
        //将统计结果批量插入到中间表
        if(statisticsConfigList != null && statisticsConfigList.size() > 0){
            statisticsResult = clientSideMapper.insertBatchProcess(statisticsConfigList,theMonth,company,userInfo,codeValues,projectSide);
        }
        return statisticsResult;
    }

    @Override
    public int batchSubmitReport(String theMonth, String company, List<String> codeValues, String projectSide, S_USER_INFO userInfo){

        //获取所有下属单位
        List<String> allNextCompany = commonService.getAllSubOrgCodes(company);
        //删除下属中间过程数据和报表信息表数据
        allNextCompany.parallelStream().forEach(companyItem -> {
            //统计
            deleteInfo(projectSide,companyItem,theMonth,codeValues);
        });
        //解锁原始数据
        lockOrUnlockData(projectSide,codeValues,theMonth,allNextCompany,false);
        //遍历每个单位，获取该单位需要统计和上报的报表（parallelStream底层实际多线程执行，不保证顺序）
        allNextCompany.parallelStream().forEach(companyItem -> {
            //统计
            statisticsReport(projectSide,companyItem,theMonth,codeValues,userInfo,true);
        });
        //解锁原始数据
        lockOrUnlockData(projectSide,codeValues,theMonth,allNextCompany,true);
        //返回成功
        return ClientSideServiceImpl.result_1;
    }

    @Override
    public List<R_REPORT_INFO> getReportInfoByParams(String theMonth, String company, List<String> codeValues, String projectSide){

        return clientSideMapper.getReportInfoByParams(theMonth,company,String.join(",", codeValues),projectSide);
    }

    @Override
    public int insertBatchReportInfo(List<R_REPORT_INFO> reportInfoList){
        return clientSideMapper.insertBatchReportInfo(reportInfoList);
    }

    private int addReportInfo(String codeType,String company, String theMonth, List<String> codeValues, S_USER_INFO userInfo,boolean isBatchSubmit){

        List<R_REPORT_INFO> reportInfoList = new ArrayList<R_REPORT_INFO>();
        for(String codeValue : codeValues){
            //新增
            R_REPORT_INFO info = new R_REPORT_INFO();
            //设置报表id
            info.setReportId(CommonUtils.createUUID());
            //设置报表编码
            info.setReportCode(codeValue);
            //设置所属单位
            info.setCompany(company);
            //设置统计时间
            info.setStatisticsDate(CommonUtils.createCurrentTimeStr());
            //设置统计人
            info.setStatisticsOperator(userInfo.getUserName());
            //设置统计月份
            info.setTheMonth(theMonth);
            //设置统计状态 0：未统计 1：已统计
            info.setStatisticsState("1");
            if(isBatchSubmit){
                //设置上报状态 0：未上报 1：已上报 2：已退回
                info.setSubmitState("1");
                //设置上报时间
                info.setSubmitDate(CommonUtils.createCurrentTimeStr());
                //设置上报人
                info.setSubmitOperator(userInfo.getUserName());
            }else{
                info.setSubmitState("0");
                info.setSubmitDate("");
                info.setSubmitOperator("");
            }
            //设置项目所属侧
            info.setProjectSide(codeType);
            //设置回退意见
            info.setReturnOpinion("");
            reportInfoList.add(info);
        }
        int insertResult = insertBatchReportInfo(reportInfoList);
        if(insertResult > 0) return ClientSideServiceImpl.result_1;
        return ClientSideServiceImpl.result_0;
    }

    @Override
    public List<S_STATISTICS_CONFIG> getStatisticsConfigList(String codeType, List<String> codeValues){
        if(codeValues != null && codeValues.size() > 0){
            return clientSideMapper.getStatisticsConfigList(codeType,String.join(",", codeValues));
        }else{
            return clientSideMapper.getStatisticsConfigList(codeType,"");
        }
    }

    @Override
    public List<P_SELF_CLIENT_SUMMARY> getSelfClientSummaryByParams(String theMonth, String company, List<String> codeValues, String projectSide){
        return clientSideMapper.getSelfClientSummaryByParams(theMonth,company,String.join(",", codeValues),projectSide);
    }

    public void deleteReportInfoAndResult(String theMonth, String company, String codeValues, String projectSide){
        deleteReportInfo(theMonth,company,codeValues,projectSide);
        deleteSelfClientSummary(theMonth,company,codeValues,projectSide);
    }

    public void deleteContractInfoAndResult(String theMonth, String company, String codeValues, String projectSide){
        deleteReportInfo(theMonth,company,codeValues,projectSide);
        deleteContractSummary(theMonth,company,codeValues);
    }

    @Override
    public int deleteReportInfo(String theMonth, String company, String codeValues, String projectSide){
        return clientSideMapper.deleteReportInfo(theMonth,company,codeValues,projectSide);
    }

    @Override
    public int deleteSelfClientSummary(String theMonth, String company, String codeValues, String projectSide){
        return clientSideMapper.deleteSelfClientSummary(theMonth,company,codeValues,projectSide);
    }

    @Override
    public int deleteContractSummary(String theMonth, String company, String codeValues){
        return clientSideMapper.deleteContractSummary(theMonth,company,codeValues);
    }

    @Override
    public int submitReport(String theMonth, String company, List<String> codeValues, String projectSide, S_USER_INFO userInfo){
        //初始化上报结果
        int submitRes = 0;
        //报表上报
        submitRes  = clientSideMapper.updateReportInfo(theMonth,company,String.join(",", codeValues),projectSide,userInfo);
        //上报成功
        if(submitRes > 0){
            //构造参数
            List<String> companys = new ArrayList<String>();
            companys.add(company);
            //锁定原始数据
            lockOrUnlockData(projectSide,codeValues,theMonth,companys,true);
            return ClientSideServiceImpl.result_1;
        }
        return ClientSideServiceImpl.result_0;
    }

    @Override
    public void lockOrUnlockData(String projectSide, List<String> codeValues, String theMonth, List<String> companys,boolean isLock){

        List<String> tableNames = null;
        //判断有没有勾选第一个报表，即报表编码是否包含0，获取相应的统计配置类集合
        if(ClientSideServiceImpl.CLIENT_SIDE.equals(projectSide)){
            if(!codeValues.contains("0")){
                //没有勾选第一个报表
                tableNames = getTableByCodeValues(projectSide,codeValues);
            }else{
                //勾选第一个报表
                tableNames = getTableByCodeValues(projectSide,null);
            }
            //循环获取的表名
            for(String tableName : tableNames){
                //客户侧更新
                clientSideMapper.updateDataState(theMonth,String.join(",", companys),tableName,"",projectSide,isLock);
            }
        }else{
            //是否勾选第一张报表，是则去掉第一张报表编码
            List<String> codeValuesTemp = null;
            if(codeValues.contains("0")){
                codeValuesTemp = codeValues.stream().filter(codeValue ->
                        !"0".equals(codeValue)
                ).collect(Collectors.toList());
            }else{
                codeValuesTemp = codeValues;
            }
            //自身侧更新
            clientSideMapper.updateDataState(theMonth,String.join(",", companys),"",String.join(",", codeValuesTemp),projectSide,isLock);
        }

    }

    @Override
    public List<String> getStatisticsedCompany(String theMonth, String codeValue, String projectSide, String stateType, String stateValue){
        return clientSideMapper.getStatisticsedCompany(theMonth,codeValue,projectSide,stateType,stateValue);
    }

    @Override
    public List<BatchSubmitDTO> getNoSubmitNextCompany(String theMonth, String codeValue, String projectSide, String company){
        return clientSideMapper.getNoSubmitNextCompany(theMonth,codeValue,projectSide,company);
    }

    @Override
    public int updateAllCompanyReportInfo(String theMonth, String company, String projectSide, S_USER_INFO userInfo){
        return clientSideMapper.updateAllCompanyReportInfo(theMonth,company,projectSide,userInfo);
    }

    @Override
    public List<String> getNoStatistAndSubmitCodeValue(String theMonth, String company, String codeType, String projectSide){
        return clientSideMapper.getNoStatistAndSubmitCodeValue(theMonth,company,codeType,projectSide);
    }

    @Override
    public int reportReturn(String theMonth, String company, List<String> codeValues, String projectSide, String returnOpinion, S_USER_INFO userInfo){
        //初始化回退结果
        int returnRes = 0;
        //报表回退
        returnRes = clientSideMapper.reportReturn(theMonth,company,String.join(",", codeValues),projectSide,returnOpinion,userInfo);
        //回退成功
        if(returnRes > 0){
            List<String> companys = new ArrayList<String>();
            companys.add(company);
            //解锁原始数据
            lockOrUnlockData(projectSide,codeValues,theMonth,companys,false);
            return ClientSideServiceImpl.result_1;
        }
        return ClientSideServiceImpl.result_0;
    }

    @Override
    public List<String> getNoSubmitAllNextCompany(String theMonth, String company, String codeValue, String projectSide, boolean isStatistics){
        return clientSideMapper.getNoSubmitAllNextCompany(theMonth,company,codeValue,projectSide,isStatistics);
    }

    @Override
    public List<String> getSubmitAllNextCompany(String theMonth, String company, String codeValue, String projectSide){
        return clientSideMapper.getSubmitAllNextCompany(theMonth,company,codeValue,projectSide);
    }

    @Override
    public int updateSubmitState(String theMonth, String companys, String codeValue, String projectSide, S_USER_INFO userInfo){
        return clientSideMapper.updateSubmitState(theMonth,companys,codeValue,projectSide,userInfo);
    }

    @Override
    public List<String> getTableByCodeValues(String codeType, List<String> codeValues){
        if(codeValues != null && codeValues.size() > 0){
            return clientSideMapper.getTableByCodeValues(codeType, String.join(",", codeValues));
        }else{
            return clientSideMapper.getTableByCodeValues(codeType,"");
        }
    }
}
