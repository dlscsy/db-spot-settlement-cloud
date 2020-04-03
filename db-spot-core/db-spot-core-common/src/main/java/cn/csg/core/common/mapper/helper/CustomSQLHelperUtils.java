package cn.csg.core.common.mapper.helper;

import cn.csg.core.common.constant.BaseBusinessMapperExceptionCode;
import cn.csg.core.common.exception.BaseBusinessMapperException;
import com.alibaba.fastjson.JSONArray;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class CustomSQLHelperUtils {

    /**
     * 构建IN条件查询所需要的数据
     *
     * @param value
     * @return
     */
    public static String getInValues(Object value) {
        List<String> values = new ArrayList<>();
        try {
            values = JSONArray.parseArray(String.valueOf(value), String.class);
        } catch (Exception e) {
            throw new BaseBusinessMapperException(
                    BaseBusinessMapperExceptionCode.JSONARRAY_PARSE_LIST_EXCEPTION,
                    "构建IN条件查询所需要的数据时，无法将JSONArray转成List！");
        }

        if (listToStringIsEmpty(values)) {
            throw new BaseBusinessMapperException(
                    BaseBusinessMapperExceptionCode.IN_VALUES_VALIDATE_FAIL_EXCEPTION,
                    "获得的条件值集合无法构建IN条件查询所需要的数据！");
        }

        StringBuffer inValues = new StringBuffer();
        for (int i = 0; i < values.size(); i++) {
            inValues.append("'" + values.get(i) + "'");
            if (i < values.size() - 1) {
                inValues.append(", ");
            }
        }
        return inValues.toString();
    }

    /**
     * 构建区间条件查询所需要的数据
     *
     * @param value
     * @return
     */
    public static List<String> getRangeValues(Object value) {
        List<String> values = new ArrayList<>();
        try {
            values = JSONArray.parseArray(String.valueOf(value), String.class);
        } catch (Exception e) {
            throw new BaseBusinessMapperException(
                    BaseBusinessMapperExceptionCode.JSONARRAY_PARSE_LIST_EXCEPTION,
                    "构建区间条件查询所需要的数据时，无法将JSONArray转成List！");
        }

        if (listToStringIsEmpty(values) || values.size() != 2) {
            throw new BaseBusinessMapperException(
                    BaseBusinessMapperExceptionCode.RANGE_VALUES_VALIDATE_FAIL_EXCEPTION,
                    "获得的条件值集合无法构建区间条件查询所需要的数据！");
        }

        return values;
    }

    /**
     * 字符数据集合为空或里面的内容为空
     *
     * @param values
     * @return
     */
    public static boolean listToStringIsEmpty(List<String> values) {
        StringBuffer sbf = new StringBuffer("");
        if (CollectionUtils.isEmpty(values)) {
            return true;
        }

        for (String currStr : values) {
            sbf.append(currStr);
        }

        if (StringUtils.isEmpty(sbf.toString())) {
            return true;
        }

        return false;
    }

    /**
     * 将复选数据拼接成一个字符串返回
     *
     * @param split
     * @param value
     * @return
     */
    public static String getCheckBoxValues(String split, Object value) {
        List<String> values = new ArrayList<>();
        try {
            values = JSONArray.parseArray(String.valueOf(value), String.class);
        } catch (Exception e) {
            throw new BaseBusinessMapperException(
                    BaseBusinessMapperExceptionCode.JSONARRAY_PARSE_LIST_EXCEPTION,
                    "构建数据集合时，无法将JSONArray转成List！");
        }

        if (listToStringIsEmpty(values)) {
            return "";
        }

        StringBuffer checkBoxValues = new StringBuffer();
        for (int i = 0; i < values.size(); i++) {
            checkBoxValues.append(values.get(i));
            if (i < values.size() - 1) {
                checkBoxValues.append(split);
            }
        }
        return checkBoxValues.toString();
    }

    /**
     * 根据字符串集合得到IN条件查询所需要的数据
     *
     * @param keys
     * @return
     */
    public static String getInValues(List<String> keys) {
        StringBuffer inValues = new StringBuffer();
        for (int i = 0; i < keys.size(); i++) {
            inValues.append("'" + keys.get(i) + "'");
            if (i < keys.size() - 1) {
                inValues.append(", ");
            }
        }
        return inValues.toString();
    }
}
