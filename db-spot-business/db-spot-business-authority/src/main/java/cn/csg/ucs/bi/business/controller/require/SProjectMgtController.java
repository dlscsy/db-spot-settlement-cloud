package cn.csg.ucs.bi.business.controller.require;

import cn.csg.core.common.mapper.helper.CustomSQLHelperUtils;
import cn.csg.ucs.bi.base.constant.BaseCode;
import cn.csg.ucs.bi.base.controller.AbstractBaseBusinessController;
import cn.csg.ucs.bi.base.service.AbstractBaseBusinessService;
import cn.csg.ucs.bi.base.structure.JSONResponseBody;
import cn.csg.ucs.bi.business.entity.helper.SelfSaveBase;
import cn.csg.ucs.bi.business.service.require.SProjectMgtService;
import cn.csg.ucs.bi.common.entity.S_LOGIN_LOG;
import cn.csg.ucs.bi.common.entity.S_USER_INFO;
import cn.csg.ucs.bi.common.service.CommonService;
import cn.csg.ucs.bi.common.service.LogService;
import cn.csg.ucs.bi.common.structure.DropDown;
import cn.csg.ucs.bi.utils.CommonUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@CrossOrigin
@Transactional
@RestController
@RequestMapping("/sProjectMgtController")
public class SProjectMgtController extends AbstractBaseBusinessController<SelfSaveBase> {

    @Autowired
    @Qualifier("commonService")
    private CommonService commonService;

    @Autowired
    @Qualifier("sProjectMgtService")
    private SProjectMgtService service;

    @Autowired
    private LogService logService;

    @ResponseBody
    @GetMapping("/getSProjectInfo")
    public JSONResponseBody getSProjectInfo(String params) {
        JSONObject jo = JSONObject.parseObject(params);
        Integer page = jo.getInteger("page");
        Integer limit = jo.getInteger("limit");
        if (page != null && limit != null) {
            jo.remove("page");
            jo.remove("limit");
            PageHelper.startPage(page, limit);
        }
        List<SelfSaveBase> values = service.getSProjectInfo(jo);

        JSONObject result = new JSONObject();
        if (page != null && limit != null) {
            result.put("total", new PageInfo<SelfSaveBase>(values).getTotal());
        } else {
            result.put("total", values.size());
        }
        result.put("rows", values);
        return JSONResponseBody.createSuccessResponseBody("查询成功！", result);
    }

    @ResponseBody
    @GetMapping("/getProjectCategory")
    public JSONResponseBody getProjectCategory() {
        List<DropDown> values = commonService.getDropDownCodesByCodeType("0");
//        return JSONResponseBody.createSuccessResponseBody("", values.stream().filter(code -> {
//            return "1, 2, 3, 4, 5, 13".indexOf(code.getValue()) >= 0;
//        }).collect(Collectors.toList()));
        return JSONResponseBody.createSuccessResponseBody("", values);
    }

    @ResponseBody
    @PostMapping("/addSProjectInfo")
    public JSONResponseBody addSProjectInfo(String params,HttpServletRequest req) {
        JSONObject jo = JSONObject.parseObject(params);
        JSONObject tableChangeData = jo.getJSONObject("tableChangeData");
        if (tableChangeData != null) {
            List<JSONObject> subInfos = JSONArray.parseArray(tableChangeData.getString("insertDatas"), JSONObject.class);
            String projectCategory = jo.getString("projectCategory");
            String className = commonService.getEntityNames("0", projectCategory).get("CLASS_NAME");
            commonService.batchInsertCategoryInfo(className, subInfos, false);
            jo.remove("tableChangeData");
        }
        getService().add(jo);

        // 异步插入日志数据库
        logService.addLog(S_LOGIN_LOG.LOGTYPE_OPERTION,"新增成功",getProjectCategoryName(jo.getString("projectCategory")),(S_USER_INFO) req.getAttribute("LOGIN_USER"), CommonUtil.getLoginIp(req));

        return JSONResponseBody.createSuccessResponseBody("新增成功！", null);
    }

    @ResponseBody
    @PostMapping("/updateSProjectInfo")
    public JSONResponseBody updateSProjectInfo(String params,HttpServletRequest req) {
        JSONObject jo = JSONObject.parseObject(params);
        JSONObject tableChangeData = jo.getJSONObject("tableChangeData");
        if (tableChangeData != null) {
            String projectCategory = jo.getString("projectCategory");
            HashMap<String, String> names = commonService.getEntityNames("0", projectCategory);
            String className = names.get("CLASS_NAME");
            String tableName = names.get("TABLE_NAME");
            List<JSONObject> insertDatas = JSONArray.parseArray(tableChangeData.getString("insertDatas"), JSONObject.class);
            if (insertDatas.size() > 0) {
                commonService.batchInsertCategoryInfo(className, insertDatas, false);
            }
            List<JSONObject> updateDatas = JSONArray.parseArray(tableChangeData.getString("updateDatas"), JSONObject.class);
            if (updateDatas.size() > 0) {
                List<JSONObject> realUpdateDatas = updateDatas.stream().map(obj -> {
                    String guid = obj.getString("GUID");
                    obj.put("primaryKeyValue", guid);
                    obj.remove("GUID");
                    return obj;
                }).collect(Collectors.toList());
                for(JSONObject curr : realUpdateDatas) {
                    service.updateSubInfos(curr, className);
                }
            }
            List<String> delDatakeys = JSONArray.parseArray(tableChangeData.getString("delDatakeys"), String.class);
            if (delDatakeys.size() > 0) {
                service.delSubInfos(tableName, CustomSQLHelperUtils.getInValues(delDatakeys));
            }
            jo.remove("tableChangeData");
            jo.remove("projectCategory");
        }
        getService().update(jo);

        // 异步插入日志数据库
        logService.addLog(S_LOGIN_LOG.LOGTYPE_OPERTION,"更新成功",getProjectCategoryName(jo.getString("projectCategory")), (S_USER_INFO) req.getAttribute("LOGIN_USER"), CommonUtil.getLoginIp(req));
        return JSONResponseBody.createSuccessResponseBody("更新成功！", null);
    }

    @ResponseBody
    @PostMapping("/delSProjectInfo")
    public JSONResponseBody del(String params,HttpServletRequest req) {
        JSONObject jo = JSONObject.parseObject(params);
        List<SelfSaveBase> datas = JSONArray.parseArray(jo.getString("datas"), SelfSaveBase.class);
        List<String> keys = datas.stream().map(data -> {
            return data.getProjectId();
        }).collect(Collectors.toList());
        String keys_str = CustomSQLHelperUtils.getInValues(keys);
        Map<String, List<SelfSaveBase>> currentDatasGroupByTableName = datas.stream()
                .collect(Collectors.groupingBy(SelfSaveBase::getTableName));
        service.delSProjectInfo("B_SELF_SAVE_BASE", keys_str);
        for (Map.Entry<String, List<SelfSaveBase>> entry : currentDatasGroupByTableName.entrySet()) {
            String tableName = entry.getKey();
            service.delSProjectInfo(tableName, keys_str);
        }

        // 异步插入日志数据库
        logService.addLog(S_LOGIN_LOG.LOGTYPE_OPERTION,"更新成功",getProjectCategoryName(jo.getString("projectCategory")), (S_USER_INFO) req.getAttribute("LOGIN_USER"), CommonUtil.getLoginIp(req));
        return JSONResponseBody.createSuccessResponseBody("删除成功！", null);
    }

    @PostMapping("/getSelfExcelTemplate")
    public void getSelfExcelTemplate(String params, HttpServletRequest request, HttpServletResponse response){
        File file = null;
        FileInputStream in = null;
        OutputStream out = null;
        try {
            JSONObject json = JSONObject.parseObject(params);
            //1.设置文件ContentType类型，这样设置，会自动判断下载文件类型
            response.setContentType("multipart/form-data");
            String fileName = "模板.xls";
            S_USER_INFO userInfo = (S_USER_INFO)request.getAttribute("LOGIN_USER");
            //设置参数 使前端可以获取返回头参数
            response.setHeader("Access-Control-Expose-Headers","Content-Disposition");
            file = service.generateSelfTemplate(fileName,userInfo,json.getString("projectCategory"));;
            //2.设置文件头：最后一个参数是设置下载文件名
            response.setHeader("Content-Disposition", "attachment;filename="
                    + new String(file.getName().getBytes("UTF-8"),"ISO-8859-1")); //中文文件名
            //获得File对象
            in = new FileInputStream(file);
            //3.通过response获取OutputStream对象(out)
            out = new BufferedOutputStream(response.getOutputStream());

            int b = 0;
            byte[] buffer = new byte[2048];
            while ((b=in.read(buffer)) != -1){
                out.write(buffer,0,b); //4.写到输出流(out)中
            }
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭流
            try {
                if (null != in)
                    in.close();
                if (null != out)
                    out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            file.delete();//删除文件
        }
    }

    @ResponseBody
    @PostMapping("/batchSelfAdd")
    public JSONResponseBody batchSelfAdd(String params,HttpServletRequest req) throws ClassNotFoundException {
        JSONObject json = JSONObject.parseObject(params);
        JSONResponseBody result = new JSONResponseBody();
        S_USER_INFO userInfo = (S_USER_INFO)req.getAttribute("LOGIN_USER");
        result= service.batchSelfAdd(json,userInfo);

        if(BaseCode.BUSINESS_SUCCESS.equals(result.getBusinessCode())){
            // 异步插入日志数据库
            logService.addLog(S_LOGIN_LOG.LOGTYPE_OPERTION,"导入成功",getProjectCategoryName(json.getString("projectCategory")),userInfo, CommonUtil.getLoginIp(req));
        }
        return result;
    }

    private String getProjectCategoryName(String projectCategory){

        String projectCategoryName = "";

        switch (projectCategory){
            case "1":
                projectCategoryName = "变电站无功补偿项目";
                break;
            case "2":
                projectCategoryName = "高效变压器应用项目";
                break;
            case "3":
                projectCategoryName = "线路改造项目";
                break;
            case "4":
                projectCategoryName = "升压改造项目";
                break;
            case "5":
                projectCategoryName = "电机系统节能项目";
                break;
            case "6":
                projectCategoryName = "中央空调余热回收项目";
                break;
            case "7":
                projectCategoryName = "中央空调系统控制节能及中央空调过渡季冷却水制冷项目";
                break;
            case "8":
                projectCategoryName = "绿色照明项目";
                break;
            case "9":
                projectCategoryName = "水（地）源热泵项目";
                break;
            case "10":
                projectCategoryName = "电蓄冷（热）项目";
                break;
            case "11":
                projectCategoryName = "燃煤工业锅炉分层燃烧项目";
                break;
            case "12":
                projectCategoryName = "燃气锅炉冷凝式余热回收项目";
                break;
            case "13":
                projectCategoryName = "资源利用发电项目";
                break;
        }

        return projectCategoryName;
    }

    @Override
    public <S extends AbstractBaseBusinessService> S getService() {
        return (S) service;
    }
}
