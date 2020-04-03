package cn.csg.ucs.bi.base.controller;

import cn.csg.core.common.mapper.helper.CustomSQLHelperUtils;
import cn.csg.ucs.bi.base.service.AbstractBaseBusinessService;
import cn.csg.ucs.bi.base.structure.JSONResponseBody;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.lang.reflect.ParameterizedType;
import java.util.List;

public abstract class AbstractBaseBusinessController<T> {

    @ResponseBody
    @GetMapping("/queryByConditions")
    public JSONResponseBody queryByConditions(String params) {
        JSONObject jo = JSONObject.parseObject(params);
        Integer page = jo.getInteger("page");
        Integer limit = jo.getInteger("limit");
        if (page != null && limit != null) {
            jo.remove("page");
            jo.remove("limit");
            PageHelper.startPage(page, limit);
        }
        List<T> values = getService().queryByConditions(jo);

        JSONObject result = new JSONObject();
        if (page != null && limit != null) {
            result.put("total", new PageInfo<T>(values).getTotal());
        } else {
            result.put("total", values.size());
        }
        result.put("rows", values);
//        result.put("page", page);
//        result.put("limit", limit);
        return JSONResponseBody.createSuccessResponseBody("查询成功！", result);
    }

    @ResponseBody
    @PostMapping("/add")
    public JSONResponseBody add(String params) {
        JSONObject jo = JSONObject.parseObject(params);
        getService().add(jo);
        return JSONResponseBody.createSuccessResponseBody("新增成功！", null);
    }

    @ResponseBody
    @PostMapping("/update")
    public JSONResponseBody update(String params) {
        JSONObject jo = JSONObject.parseObject(params);
        getService().update(jo);
        return JSONResponseBody.createSuccessResponseBody("更新成功！", null);
    }

    @ResponseBody
    @PostMapping("/del")
    public JSONResponseBody del(String params) {
        JSONObject jo = JSONObject.parseObject(params);
        List<String> keys = JSONArray.parseArray(jo.getString("keys"), String.class);
        getService().del(CustomSQLHelperUtils.getInValues(keys));
        return JSONResponseBody.createSuccessResponseBody("删除成功！", null);
    }

    @ResponseBody
    @PostMapping("/validateExist")
    public JSONResponseBody validateExist(String params) {
        JSONObject jo = JSONObject.parseObject(params);
        List<T> values = getService().queryExistsForValidate(jo);
        return JSONResponseBody.createSuccessResponseBody("",
                CollectionUtils.isEmpty(values) ? true : false);
    }

    // 获得子类注入的service
    public abstract <S extends AbstractBaseBusinessService> S getService();

    /**
     * 获取泛型的Class类型
     *
     * @return
     */
    private Class<T> getGenericClass() {
        return (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }
}
