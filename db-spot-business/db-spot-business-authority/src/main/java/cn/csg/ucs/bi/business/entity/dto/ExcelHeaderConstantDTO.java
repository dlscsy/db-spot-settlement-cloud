package cn.csg.ucs.bi.business.entity.dto;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import jdk.nashorn.internal.parser.JSONParser;

/**
 * Excel模板下载，列名、下拉框、下拉框值映射常量类
 *
 * @author G.A.N
 * @Date 2019-12-09
 */

public class ExcelHeaderConstantDTO {

    /**
     * ----------------客户侧----------------
     **/
    //节能诊断
    public static final String[] DIAGNOSIS_HEADER = new String[]{"*所属单位", "*上报月份", "*户号", "*诊断用户名称", "*诊断实施单位", "*用户所处行业", "*诊断时间", "*诊断费用", "诊断报告年节电量",
            "是否意向开展改造", "是否意向开展合同能源管理", "是否完成改造", "改造实施单位", "改造实施方式", "*诊断类别"};
    public static final String[] DIAGNOSIS_DOWNROWS = new String[]{"0", "5", "9", "10", "11", "13", "14"};
    public static final String[] DIAGNOSIS_METHOTYPE = new String[]{"1&null", "2&-1", "3&11", "3&11", "3&11", "3&17", "3&18"};
    public static final JSONArray DIAGNOSIS_TYPE = JSONArray.parseArray("[{\"column\":1,\"format\":\"yyyy-mm\"},{\"column\":2,\"format\":\"@\"},{\"column\":6,\"format\":\"yyyy-mm-dd\"}]");


    //高损变压器
    public static final String[] HL_TRANSFORMER_HEADER = {"*所属单位", "*上报月份", "*户号", "*客户名称", "*变压器编号", "*用户所处行业", "*高损变压器型号", "容量", "改造或淘汰年月",
            "改造后变压器型号", "改造实施单位", "改造实施方式", "改造后年节约电量"};
    public static final String[] HL_TRANSFORMER_DOWNROWS = {"0", "5", "11"};
    public static final String[] HL_TRANSFORMER_METHOTYPE = new String[]{"1&null", "2&-1", "3&17"};
    public static final JSONArray HL_TRANSFORMER_TYPE = JSONArray.parseArray("[{\"column\":1,\"format\":\"yyyy-mm\"},{\"column\":2,\"format\":\"@\"},{\"column\":8,\"format\":\"yyyy-mm\"}]");

    //推广LED
    public static final String[] LED_HEADER = {"*所属单位", "*上报月份", "*户号", "*客户名称", "*用户所处行业", "*项目名称", "*是否系统内改造", "*项目实施单位",
            "*项目实施方式", "*LED灯具改造数量", "*年节电能力", "灯具总降低功率", "项目投资金额", "改造完成时间或合同开始时间", "合同截止时间"};
    public static final String[] LED_DOWNROWS = {"0", "4", "6", "8"};
    public static final String[] LED_METHOTYPE = new String[]{"1&null", "2&-1", "3&11", "3&17"};
    public static final JSONArray LED_TYPE = JSONArray.parseArray("[{\"column\":1,\"format\":\"yyyy-mm\"},{\"column\":2,\"format\":\"@\"},{\"column\":13,\"format\":\"yyyy-mm\"},{\"column\":14,\"format\":\"yyyy-mm\"}]");

    //激励措施或新设备新技术降低负荷
    public static final String[] INCENTIVE_HEADER = {"*所属单位", "*上报月份", "*户号", "*客户名称", "*用户所处行业", "*项目名称", "降低负荷手段", "激励措施或应用新技术新设备内容",
            "项目实施年月", "转移电量", "峰段时长"};
    public static final String[] INCENTIVE_DOWNROWS = {"0", "4", "6"};
    public static final String[] INCENTIVE_METHOTYPE = new String[]{"1&null", "2&-1", "3&21"};
    public static final JSONArray INCENTIVE_TYPE = JSONArray.parseArray("[{\"column\":1,\"format\":\"yyyy-mm\"},{\"column\":2,\"format\":\"@\"}]");

    //客户侧节能改造发电
    public static final String[] CLIENT_SIDE_HEADER = {"*所属单位", "*上报月份", "*户号", "*客户名称", "*项目名称", "*用户所处行业", "*技术分类", "*项目类型", "*机组编号",
            "机组容量", "投入运行时间", "目录电价", "上网电价", "机组发电量", "余额上网电量", "余额上网电量（折算）", "企业自用电量"};
    public static final String[] CLIENT_SIDE_DOWNROWS = {"0", "5", "6", "7"};
    public static final String[] CLIENT_SIDE_METHOTYPE = new String[]{"1&null", "2&-1", "3&5", "3&6"};
    public static final JSONArray CLIENT_SIDE_TYPE = JSONArray.parseArray("[{\"column\":1,\"format\":\"yyyy-mm\"},{\"column\":2,\"format\":\"@\"},{\"column\":10,\"format\":\"yyyy-mm-dd\"}]");

    //合同能源管理
    public static final String[] CONTRACT_HEADER = {"*所属单位", "*上报月份", "*分类", "*客户编号", "*客户名称", "*所处行业", "*诊断时间", "*技术分类", "*项目类型", "*项目名称",
            "*合同开始时间（年月）", "*合同截止时间（年月）", "*项目竣工时间（年月）", "*是否经过第三方审核", "*口径二投资金额",
            "*口径一投资金额", "*项目施工单位名称", "节能服务推动方式", "*口径二上年度用电规模", "*口径一上年度用电规模",
            "*口径二实际节约电量", "*口径二折算后节约电量", "*口径二实际节约电力", "*口径二折算后节约电力", "*口径二节约电费",
            "*口径一实际节约电量", "*口径一折算后节约电量", "*口径一实际节约电力", "*口径一折算后节约电力", "*口径一节约电费"};
    public static final String[] CONTRACT_DOWNROWS = {"0", "2", "5", "7", "8", "13", "17"};
    public static final String[] CONTRACT_METHOTYPE = new String[]{"1&null", "3&3", "2&-1", "3&5", "3&6", "3&11", "3&14"};
    public static final JSONArray CONTRACT_TYPE = JSONArray.parseArray("[{\"column\":1,\"format\":\"yyyy-mm\"},{\"column\":2,\"format\":\"@\"},{\"column\":6,\"format\":\"yyyy-mm-dd\"},{\"column\":10,\"format\":\"yyyy-mm\"},{\"column\":11,\"format\":\"yyyy-mm\"},{\"column\":12,\"format\":\"yyyy-mm\"}]");

    //非合同能源管理
    public static final String[] UNCONTRACT_HEADER = {"*所属单位", "*上报月份", "*分类", "*客户编号", "*客户名称", "*所处行业", "*诊断时间", "*技术分类", "*项目类型", "*项目名称或技术措施",
            "*项目竣工时间（年月）", "*是否经过第三方审核", "*口径二投资金额", "*口径一投资金额", "*项目施工单位名称", "节能服务推动方式",
            "是否客户自身技术力量实施", "*年度用电规模", "*口径二实际节约电量", "*口径二折算后节约电量",
            "*口径二实际节约电力", "*口径二折算后节约电力", "*口径二节约电费", "*口径一实际节约电量", "*口径一折算后节约电量",
            "*口径一实际节约电力", "*口径一折算后节约电力", "*口径一节约电费"};
    public static final String[] UNCONTRACT_DOWNROWS = {"0", "2", "5", "7", "8", "11", "15", "16"};
    public static final String[] UNCONTRACT_METHOTYPE = new String[]{"1&null", "3&3", "2&-1", "3&5", "3&6", "3&11", "3&14", "3&11"};
    public static final JSONArray UNCONTRACT_TYPE = JSONArray.parseArray("[{\"column\":1,\"format\":\"yyyy-mm\"},{\"column\":2,\"format\":\"@\"},{\"column\":6,\"format\":\"yyyy-mm-dd\"},{\"column\":10,\"format\":\"yyyy-mm\"}]");

    /**
     * ----------------自身侧----------------
     **/
    //变电站无功补偿
    public static final String[] COMPENSATE_HEADER = new String[]{"*上报月份", "*项目名称", "*节能量属性", "*所属单位", "*项目类别", "*节能单位名称", "*行业类别", "*是否合同能源管理", "*项目总投资（万元）", "*是否节能量审核",
            "节能量审核单位", "投运年月", "项目编号", "*无功补偿全投的容量值", "*电容器的介质损耗角正切值", "*无功经济当量", "*无功补偿装置在最大节电力情况下等效运行时间", "*理论年节约电量", "*折算年节约电量"};
    public static final String[] COMPENSATE_DOWNROWS = new String[]{"2", "3", "4", "5", "6", "7", "9"};
    public static final String[] COMPENSATE_METHOTYPE = new String[]{"3&10", "1&null", "5&null", "4&null", "2&-1", "3&11", "3&11"};
    public static final JSONArray COMPENSATE_TYPE = JSONArray.parseArray("[{\"column\":0,\"format\":\"yyyy-mm\"},{\"column\":11,\"format\":\"yyyy-mm\"}]");

    //高效变压器应用项目
    public static final String[] HE_TRANSFORMER_HEADER = new String[]{"*上报月份", "*项目名称", "*节能量属性", "*所属单位", "*项目类别", "*节能单位名称", "*行业类别", "*是否合同能源管理", "*项目总投资（万元）", "*是否节能量审核",
            "节能量审核单位", "投运年月", "项目编号", "*变压器型号及容量（改造前）", "*容量（改造前）", "*变压器空载损耗（改造前）", "*变压器短路损耗（改造前）", "*变压器型号及容量（改造后）",
            "*容量（改造后）", "*变压器空载损耗（改造后）", "*变压器短路损耗（改造后）", "*变压器平均负载率（改造后）", "*理论年节约电量(万千瓦时)", "*折算年节约电量(万千瓦时)"};
    public static final String[] HE_TRANSFORMER_DOWNROWS = new String[]{"2", "3", "4", "5", "6", "7", "9"};
    public static final String[] HE_TRANSFORMER_METHOTYPE = new String[]{"3&10", "1&null", "5&null", "4&null", "2&-1", "3&11", "3&11"};
    public static final JSONArray HE_TRANSFORMER_TYPE = JSONArray.parseArray("[{\"column\":0,\"format\":\"yyyy-mm\"},{\"column\":11,\"format\":\"yyyy-mm\"}]");

    //线路改造项目
    public static final String[] LINE_HEADER = new String[]{"*上报月份", "*项目名称", "*节能量属性", "*所属单位", "*项目类别", "*节能单位名称", "*行业类别", "*是否合同能源管理", "*项目总投资（万元）", "*是否节能量审核",
            "节能量审核单位", "投运年月", "项目编号", "*导线型号（改造前）", "*导线长度（km）（改造前）", "*单位线长导线电阻（Ω/km）（改造前）", "*导线型号（改造后）", "*单位线长导线电阻（Ω/km）（改造后）",
            "*线路平均电流（A）（改造后）", "*理论年节约电量(万千瓦时)", "*折算年节约电量(万千瓦时)"};
    public static final String[] LINE_DOWNROWS = new String[]{"2", "3", "4", "5", "6", "7", "9"};
    public static final String[] LINE_METHOTYPE = new String[]{"3&10", "1&null", "5&null", "4&null", "2&-1", "3&11", "3&11"};
    public static final JSONArray LINE_TYPE = JSONArray.parseArray("[{\"column\":0,\"format\":\"yyyy-mm\"},{\"column\":11,\"format\":\"yyyy-mm\"}]");

    //升压改造项目
    public static final String[] BOOST_HEADER = new String[]{"*上报月份", "*项目名称", "*节能量属性", "*所属单位", "*项目类别", "*节能单位名称", "*行业类别", "*是否合同能源管理", "*项目总投资（万元）", "*是否节能量审核",
            "节能量审核单位", "投运年月", "项目编号", "*线路额定电压（kV）（改造前）", "*线路型号（改造前）", "*线路长度（km）（改造前）", "*单位线长导线电阻（Ω/km）（改造前）", "*线路额定电压（kV）（改造后）",
            "*线路型号（改造后）", "*线路长度（km）（改造后）", "*单位线长导线电阻（Ω/km）（改造后）", "*线路平均电流（A）（改造后）", "*理论年节约电量(万千瓦时)", "*折算年节约电量(万千瓦时)"};
    public static final String[] BOOST_DOWNROWS = new String[]{"2", "3", "4", "5", "6", "7", "9"};
    public static final String[] BOOST_METHOTYPE = new String[]{"3&10", "1&null", "5&null", "4&null", "2&-1", "3&11", "3&11"};
    public static final JSONArray BOOST_TYPE = JSONArray.parseArray("[{\"column\":0,\"format\":\"yyyy-mm\"},{\"column\":11,\"format\":\"yyyy-mm\"}]");

    //电机系统节能项目
    public static final String[] ELE_MACHINERY_HEADER = new String[]{"*上报月份", "*项目名称", "*节能量属性", "*所属单位", "*项目类别", "*节能单位名称", "*行业类别", "*是否合同能源管理", "*项目总投资（万元）", "*是否节能量审核",
            "节能量审核单位", "投运年月", "项目编号", "*工频工况或原电机耗电功率(千瓦)（改造前）", "*变频工况或更换后电机耗电功率（千瓦）（改造后）", "*设备年运行小时数（小时）", "*项目年节约电量（万千瓦时）", "*项目年节约电力（万千瓦）"};
    public static final String[] ELE_MACHINERY_DOWNROWS = new String[]{"2", "3", "4", "5", "6", "7", "9"};
    public static final String[] ELE_MACHINERY_METHOTYPE = new String[]{"3&10", "1&null", "5&null", "4&null", "2&-1", "3&11", "3&11"};
    public static final JSONArray ELE_MACHINERY_TYPE = JSONArray.parseArray("[{\"column\":0,\"format\":\"yyyy-mm\"},{\"column\":11,\"format\":\"yyyy-mm\"}]");

    //中央空调余热回收项目
    public static final String[] WASTE_RECOVERY_HEADER = new String[]{"*上报月份", "*项目名称", "*节能量属性", "实施单位", "*项目类别", "*节能单位名称", "*行业类别", "*所属供电局", "是否节能诊断", "诊断时间",
            "项目实施属性", "节能服务推动方式", "*是否合同能源管理", "合同开始时间", "合同截止时间", "*项目总投资（万元）", "*是否节能量审核", "节能量审核单位", "项目编号",
            "*加热生活热水流量（吨/小时）（改造后）", "*加热生活热水进口水温（℃）（改造后）", "*加热生活热水出口水温（℃）（改造后）", "*设备年运行小时数（小时）",
            "*项目年节约电量（万千瓦时）", "*项目年节约电力（万千瓦）"};
    public static final String[] WASTE_RECOVERY_DOWNROWS = new String[]{"2", "4", "5", "6", "7", "8", "10", "11", "12", "16"};
    public static final String[] WASTE_RECOVERY_METHOTYPE = new String[]{"3&10", "5&null", "4&null", "2&-1", "1&null", "3&11", "3&13", "3&14", "3&11", "3&11"};
    public static final JSONArray WASTE_RECOVERY_TYPE = JSONArray.parseArray("[{\"column\":0,\"format\":\"yyyy-mm\"},{\"column\":9,\"format\":\"yyyy-mm-dd\"},{\"column\":13,\"format\":\"yyyy-mm-dd\"},{\"column\":14,\"format\":\"yyyy-mm-dd\"}]");

    //中央空调系统控制节能及中央空调过渡季冷却水制冷项目
    public static final String[] VRV_HEADER = new String[]{"*上报月份", "*项目名称", "*节能量属性", "实施单位", "*项目类别", "*节能单位名称", "*行业类别", "*所属供电局", "是否节能诊断", "诊断时间",
            "项目实施属性", "节能服务推动方式", "*是否合同能源管理", "合同开始时间", "合同截止时间", "*项目总投资（万元）", "*是否节能量审核", "节能量审核单位", "投运年月", "项目编号",
            "*中央空调系统平均耗电功率（千瓦）（改造前）", "*中央空调系统平均耗电功率（千瓦）（改造后）", "*设备年运行小时数（小时）", "*项目年节约电量（万千瓦时）",
            "*项目年节约电力（万千瓦）"};
    public static final String[] VRV_DOWNROWS = new String[]{"2", "4", "5", "6", "7", "8", "10", "11", "12", "16"};
    public static final String[] VRV_METHOTYPE = new String[]{"3&10", "5&null", "4&null", "2&-1", "1&null", "3&11", "3&13", "3&14", "3&11", "3&11"};
    public static final JSONArray VRV_TYPE = JSONArray.parseArray("[{\"column\":0,\"format\":\"yyyy-mm\"},{\"column\":9,\"format\":\"yyyy-mm-dd\"},{\"column\":13,\"format\":\"yyyy-mm-dd\"},{\"column\":14,\"format\":\"yyyy-mm-dd\"},{\"column\":18,\"format\":\"yyyy-mm\"}]");

    //绿色照明项目
    public static final String[] LIGHTING_HEADER = new String[]{"*上报月份", "*项目名称", "*节能量属性", "实施单位", "*项目类别", "*节能单位名称", "*行业类别", "*所属供电局", "是否节能诊断", "诊断时间",
            "项目实施属性", "节能服务推动方式", "*是否合同能源管理", "合同开始时间", "合同截止时间", "*项目总投资（万元）", "*是否节能量审核", "节能量审核单位", "投运年月", "项目编号",
            "*灯具耗电平均功率(千瓦)（改造前）", "*灯具耗电平均功率(千瓦)（改造后）", "*设备年运行小时数（小时）", "*项目年节约电量（万千瓦时）", "*项目年节约电力（万千瓦）"};
    public static final String[] LIGHTING_DOWNROWS = new String[]{"2", "4", "5", "6", "7", "8", "10", "11", "12", "16"};
    public static final String[] LIGHTING_METHOTYPE = new String[]{"3&10", "5&null", "4&null", "2&-1", "1&null", "3&11", "3&13", "3&14", "3&11", "3&11"};
    public static final JSONArray LIGHTING_TYPE = JSONArray.parseArray("[{\"column\":0,\"format\":\"yyyy-mm\"},{\"column\":9,\"format\":\"yyyy-mm-dd\"},{\"column\":13,\"format\":\"yyyy-mm-dd\"},{\"column\":14,\"format\":\"yyyy-mm-dd\"},{\"column\":18,\"format\":\"yyyy-mm\"}]");

    //水（地）源热泵项目
    public static final String[] HEAT_PUMP_HEADER = new String[]{"*上报月份", "*项目名称", "*节能量属性", "实施单位", "*项目类别", "*节能单位名称", "*行业类别", "*所属供电局", "是否节能诊断", "诊断时间",
            "项目实施属性", "节能服务推动方式", "*是否合同能源管理", "合同开始时间", "合同截止时间", "*项目总投资（万元）", "*是否节能量审核", "节能量审核单位", "投运年月", "项目编号",
            "*热泵主机平均耗电功率（千瓦）（改造后）", "*设备年运行小时数（小时）", "*cop（热电比）", "*项目年节约电量（万千瓦时）", "*项目年节约电力（万千瓦）"};
    public static final String[] HEAT_PUMP_DOWNROWS = new String[]{"2", "4", "5", "6", "7", "8", "10", "11", "12", "16"};
    public static final String[] HEAT_PUMP_METHOTYPE = new String[]{"3&10", "5&null", "4&null", "2&-1", "1&null", "3&11", "3&13", "3&14", "3&11", "3&11"};
    public static final JSONArray HEAT_PUMP_TYPE = JSONArray.parseArray("[{\"column\":0,\"format\":\"yyyy-mm\"},{\"column\":9,\"format\":\"yyyy-mm-dd\"},{\"column\":13,\"format\":\"yyyy-mm-dd\"},{\"column\":14,\"format\":\"yyyy-mm-dd\"},{\"column\":18,\"format\":\"yyyy-mm\"}]");

    //电蓄冷（热）项目
    public static final String[] ELE_STORAGE_HEADER = new String[]{"*上报月份", "*项目名称", "*节能量属性", "实施单位", "*项目类别", "*节能单位名称", "*行业类别", "*所属供电局", "是否节能诊断", "诊断时间",
            "项目实施属性", "节能服务推动方式", "*是否合同能源管理", "合同开始时间", "合同截止时间", "*项目总投资（万元）", "*是否节能量审核", "节能量审核单位", "投运年月", "项目编号",
            "*电力高峰时段机组最大耗电功率（千瓦）（基期）", "*电力高峰时段机组最大耗电功率（千瓦）（报告期）", "*项目年节约电力（万千瓦）"};
    public static final String[] ELE_STORAGE_DOWNROWS = new String[]{"2", "4", "5", "6", "7", "8", "10", "11", "12", "16"};
    public static final String[] ELE_STORAGE_METHOTYPE = new String[]{"3&10", "5&null", "4&null", "2&-1", "1&null", "3&11", "3&13", "3&14", "3&11", "3&11"};
    public static final JSONArray ELE_STORAGE_TYPE = JSONArray.parseArray("[{\"column\":0,\"format\":\"yyyy-mm\"},{\"column\":9,\"format\":\"yyyy-mm-dd\"},{\"column\":13,\"format\":\"yyyy-mm-dd\"},{\"column\":14,\"format\":\"yyyy-mm-dd\"},{\"column\":18,\"format\":\"yyyy-mm\"}]");

    //燃煤工业锅炉分层燃烧项目
    public static final String[] COAL_HEADER = new String[]{"*上报月份", "*项目名称", "*节能量属性", "实施单位", "*项目类别", "*节能单位名称", "*行业类别", "*所属供电局", "是否节能诊断", "诊断时间",
            "项目实施属性", "节能服务推动方式", "*是否合同能源管理", "合同开始时间", "合同截止时间", "*项目总投资（万元）", "*是否节能量审核", "节能量审核单位", "投运年月", "项目编号",
            "*锅炉平均热效率(％)（改造前）", "*锅炉平均热效率(％)（改造后）", "*锅炉每小时耗原煤量（吨）（改造后）", "*设备年运行小时数（小时）", "*项目年节约电量（万千瓦时）"};
    public static final String[] COAL_DOWNROWS = new String[]{"2", "4", "5", "6", "7", "8", "10", "11", "12", "16"};
    public static final String[] COAL_METHOTYPE = new String[]{"3&10", "5&null", "4&null", "2&-1", "1&null", "3&11", "3&13", "3&14", "3&11", "3&11"};
    public static final JSONArray COAL_TYPE = JSONArray.parseArray("[{\"column\":0,\"format\":\"yyyy-mm\"},{\"column\":9,\"format\":\"yyyy-mm-dd\"},{\"column\":13,\"format\":\"yyyy-mm-dd\"},{\"column\":14,\"format\":\"yyyy-mm-dd\"},{\"column\":18,\"format\":\"yyyy-mm\"}]");

    //燃气锅炉冷凝式余热回收项目
    public static final String[] GAS_HEADER = new String[]{"*上报月份", "*项目名称", "*节能量属性", "实施单位", "*项目类别", "*节能单位名称", "*行业类别", "*所属供电局", "是否节能诊断", "诊断时间",
            "项目实施属性", "节能服务推动方式", "*是否合同能源管理", "合同开始时间", "合同截止时间", "*项目总投资（万元）", "*是否节能量审核", "节能量审核单位", "投运年月", "项目编号",
            "*锅炉平均热效率(％)（改造前）", "*锅炉平均热效率(％)（改造后）", "*平均每小时耗燃气量（Nm3/h）（改造后）", "*设备年运行小时数（小时）", "*项目年节约电量（万千瓦时）"};
    public static final String[] GAS_DOWNROWS = new String[]{"2", "4", "5", "6", "7", "8", "10", "11", "12", "16"};
    public static final String[] GAS_METHOTYPE = new String[]{"3&10", "5&null", "4&null", "2&-1", "1&null", "3&11", "3&13", "3&14", "3&11", "3&11"};
    public static final JSONArray GAS_TYPE = JSONArray.parseArray("[{\"column\":0,\"format\":\"yyyy-mm\"},{\"column\":9,\"format\":\"yyyy-mm-dd\"},{\"column\":13,\"format\":\"yyyy-mm-dd\"},{\"column\":14,\"format\":\"yyyy-mm-dd\"},{\"column\":18,\"format\":\"yyyy-mm\"}]");

    //资源利用发电项目
    public static final String[] RESOURCE_HEADER = new String[]{"*上报月份", "*项目名称", "*节能量属性", "实施单位", "*项目类别", "*节能单位名称", "*行业类别", "*所属供电局", "是否节能诊断", "诊断时间",
            "项目实施属性", "节能服务推动方式", "*是否合同能源管理", "合同开始时间", "合同截止时间", "*项目总投资（万元）", "*是否节能量审核", "节能量审核单位", "投运年月", "项目编号",
            "*发电机平均发电功率（万千瓦）（改造后）", "*资源综合利用发电占自用电率（％）（改造后）", "*设备年运行小时数（小时）", "*项目年节约电量（万千瓦时）", "*项目年节约电力（万千瓦）"};
    public static final String[] RESOURCE_DOWNROWS = new String[]{"2", "4", "5", "6", "7", "8", "10", "11", "12", "16"};
    public static final String[] RESOURCE_METHOTYPE = new String[]{"3&10", "5&null", "4&null", "2&-1", "1&null", "3&11", "3&13", "3&14", "3&11", "3&11"};
    public static final JSONArray RESOURCE_TYPE = JSONArray.parseArray("[{\"column\":0,\"format\":\"yyyy-mm\"},{\"column\":9,\"format\":\"yyyy-mm-dd\"},{\"column\":13,\"format\":\"yyyy-mm-dd\"},{\"column\":14,\"format\":\"yyyy-mm-dd\"},{\"column\":18,\"format\":\"yyyy-mm\"}]");
}
