package cn.csg.ucs.bi.business.service.require.impl;

import cn.csg.ucs.bi.business.entity.S_PROJECT_OPTION_CONFIG;
import cn.csg.ucs.bi.business.entity.dto.ProjectInfoSummaryDTO;
import cn.csg.ucs.bi.business.mapper.require.CProjectInfoSummaryMapper;
import cn.csg.ucs.bi.business.service.require.CProjectInfoSummaryService;
import cn.csg.ucs.bi.common.structure.DropDown;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CProjectInfoSummaryServiceImpl implements CProjectInfoSummaryService {

    @Autowired
    private CProjectInfoSummaryMapper cProjectInfoSummaryMapper;

    public List<ProjectInfoSummaryDTO> getReportInfoSummary(String company, String theMonth, S_PROJECT_OPTION_CONFIG projectOptionConfig){
        return cProjectInfoSummaryMapper.getReportInfoSummary(company,theMonth,projectOptionConfig);
    }

    @Override
    public List<DropDown> getCategoryDropDown(String projectCategory) {

        List<DropDown> dropDownslist = new ArrayList<DropDown>();
        List<S_PROJECT_OPTION_CONFIG> list = cProjectInfoSummaryMapper.getCategoryDropDown(projectCategory);

        for(S_PROJECT_OPTION_CONFIG projectOption : list){
            DropDown dropDown = new DropDown();
            dropDown.setLabel(projectOption.getOptionName());
            dropDown.setValue(JSONObject.toJSONString(projectOption));
            dropDownslist.add(dropDown);
        }

        return dropDownslist;
    }

}
