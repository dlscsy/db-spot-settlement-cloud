package cn.csg.ucs.bi.business.service.calculate.impl;

import cn.csg.core.common.utils.CommonUtils;
import cn.csg.ucs.bi.base.constant.BaseCode;
import cn.csg.ucs.bi.base.exception.BusinessException;
import cn.csg.ucs.bi.business.entity.P_SELF_CLIENT_SUMMARY;
import cn.csg.ucs.bi.business.entity.R_REPORT_INFO;
import cn.csg.ucs.bi.business.entity.dto.BatchSubmitDTO;
import cn.csg.ucs.bi.business.entity.dto.CalculateSideSummaryDTO;
import cn.csg.ucs.bi.business.entity.dto.ReportSummaryDTO;
import cn.csg.ucs.bi.business.mapper.calculate.CalculateSideMapper;
import cn.csg.ucs.bi.business.mapper.require.ClientSideMapper;
import cn.csg.ucs.bi.business.service.calculate.CalculateSideService;
import cn.csg.ucs.bi.business.service.require.ClientSideService;
import cn.csg.ucs.bi.common.entity.S_STATISTICS_CONFIG;
import cn.csg.ucs.bi.common.entity.S_USER_INFO;
import cn.csg.ucs.bi.common.service.CommonService;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Transactional
@Service("calculateSideService")
public class CalculateSideServiceImpl implements CalculateSideService {

    @Autowired
    private CalculateSideMapper calculateSideMapper;

    @Override
    public List<CalculateSideSummaryDTO> getReportSummary(JSONObject jo){
        return calculateSideMapper.getReportSummary(jo);
    }

}
