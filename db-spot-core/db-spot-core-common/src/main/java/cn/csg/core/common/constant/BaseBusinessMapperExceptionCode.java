package cn.csg.core.common.constant;

public class BaseBusinessMapperExceptionCode {

    // 实体类中为定义用于映射的属性或者用于映射的属性不是Map类型
    public final static String CLASSTYPE_MATCH_MAP_EXCEPTION = "CPE00";

    // 构建区间条件查询所需要的数据时，无法将JSONArray转成List
    public final static String JSONARRAY_PARSE_LIST_EXCEPTION = "CPE01";

    // 获得的条件值集合无法构建IN条件查询所需要的数据
    public final static String IN_VALUES_VALIDATE_FAIL_EXCEPTION = "CPE02";

    // 获得的条件值集合无法构建区间条件查询所需要的数据
    public final static String RANGE_VALUES_VALIDATE_FAIL_EXCEPTION = "CPE03";

    // 要新增的数据的所有属性与实体类属性不一致
    public final static String OBJECT_FOR_INSERT_PROPERTY_MATCH_EXCEPTION = "CPE04";

    // 要新增的数据为空
    public final static String OBJECT_FOR_INSERT_EMPTY_EXCEPTION = "CPE05";

    // 要更新的数据为空
    public final static String OBJECT_FOR_UPDATE_EMPTY_EXCEPTION = "CPE06";

    // 更新数据时主键的值不能为空
    public final static String PK_VALUE_FOR_UPDATE_EMPTY_EXCEPTION = "CPE07";

    // 要更新的数据的所有属性与实体类属性不一致
    public final static String OBJECT_FOR_UPDATE_PROPERTY_MATCH_EXCEPTION = "CPE08";
}
