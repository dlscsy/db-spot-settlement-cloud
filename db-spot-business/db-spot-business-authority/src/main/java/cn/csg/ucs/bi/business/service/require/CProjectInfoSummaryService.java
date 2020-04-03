package cn.csg.ucs.bi.business.service.require;

import cn.csg.ucs.bi.business.entity.S_PROJECT_OPTION_CONFIG;
import cn.csg.ucs.bi.business.entity.dto.ProjectInfoSummaryDTO;
import cn.csg.ucs.bi.common.structure.DropDown;

import java.util.List;

public interface CProjectInfoSummaryService {

    /**
     * 根据下拉选项查询汇总报表情况
     * @param company  所属单位
     * @param theMonth 统计年月
     * @param projectOptionConfig 项目信息汇总下拉框配置类
     * @return 报表汇总信息List
     */
    List<ProjectInfoSummaryDTO> getReportInfoSummary(String company, String theMonth, S_PROJECT_OPTION_CONFIG projectOptionConfig);

    /**
     * @Author chengzhifeng
     * @Description 获取分类下拉框
     * @Date 14:26 2019/12/23
     * @Param [projectCategory]
     * @return java.util.List<cn.csg.ucs.bi.common.structure.DropDown>
     **/
    List<DropDown> getCategoryDropDown(String projectCategory);
}
