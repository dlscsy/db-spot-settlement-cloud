package cn.csg.core.common.structure;

import cn.csg.core.common.annotation.DictionarieField;
import cn.csg.core.common.annotation.SetAlias;
import cn.csg.core.common.annotation.ValidateExistField;
import org.springframework.beans.BeanUtils;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.beans.PropertyDescriptor;
import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 实体类对应的表格的元数据信息
 */
public class TableMeta {

    // 默认的表的别名
    public static final String DEFAULT_TABLE_ALIAS_NAME = "dant";

    // 默认的映射关联的表的别名
    public static final String DEFAULT_RELATION_TABLE_ALIAS_NAME = "drant";

    // 默认的映射关联的列的别名的后缀
    public static final String DEFAULT_RELATION_COLUMN_ALIAS_NAME_SUFFIX = "_RELATION_VALUE";

    // 默认的复选值在数据库中持久化时各个数值之间的分隔符
    public static final String DEFAULT_CHECK_BOX_VALUES_SPLIT = ",";

    // 缓存实体类对应的表格的元数据信息
    private static final Map<Class<?>, TableMeta> TABLE_CACHE = new ConcurrentHashMap<>(64);

    // 表名
    private String tableName;

    // 表的别名
    private String tableAliasName;

    // 主键属性名
    private String pkProperty;

    // 主键对应的列名
    private String pkColumn;

    // 所有属性字段对象集合
    private Map<String, Field> allFieldsMap;

    /**
     * 全部属性字段和列名的映射关系
     * 不包括static和@Transient注解修饰的属性字段以及不是基本类型的属性字段
     */
    private Map<String, String> allFieldsColumnMap;
    /**
     * 全部列名和属性字段的映射关系
     * 不包括static和@Transient注解修饰的属性字段以及不是基本类型的属性字段
     */
    private Map<String, String> allColumnsFieldMap;

    // 需要做数据字典映射的属性字段和列名的映射关系
    private Map<String, DictionarieField> dictionarieFieldsColumnMap;

    // 全部属性字段和字段类型的映射关系
    private Map<String, Class<?>> allFieldsTypeMap;

    // 验证已存在数据的条件字段和列名的映射关系
    private Map<String, String> validateExistFieldsColumnMap;

    private TableMeta(Class<?> clazz) {
        allFieldsMap = new HashMap<>();
        allFieldsColumnMap = new HashMap<>();
        allColumnsFieldMap = new HashMap<>();
        dictionarieFieldsColumnMap = new HashMap<>();
        allFieldsTypeMap = new HashMap<>();
        validateExistFieldsColumnMap = new HashMap<>();
        initTableMetaInfo(clazz);
    }

    public String getTableName() {
        return tableName;
    }

    public String getTableAliasName() {
        return tableAliasName;
    }

    public String getPkProperty() {
        return pkProperty;
    }

    public String getPkColumn() {
        return pkColumn;
    }

    public Map<String, Field> getAllFieldsMap() {
        return allFieldsMap;
    }

    public Map<String, String> getAllFieldsColumnMap() {
        return allFieldsColumnMap;
    }

    public Map<String, String> getAllColumnsFieldMap() {
        return allColumnsFieldMap;
    }

    public Map<String, DictionarieField> getDictionarieFieldsColumnMap() {
        return dictionarieFieldsColumnMap;
    }

    public Map<String, Class<?>> getAllFieldsTypeMap() {
        return allFieldsTypeMap;
    }

    public Map<String, String> getValidateExistFieldsColumnMap() {
        return validateExistFieldsColumnMap;
    }

    /**
     * 根据实体类获取该实体类对应的表格的元数据信息
     *
     * @param entityClass
     * @return
     */
    public static TableMeta forClass(Class<?> entityClass) {
        TableMeta tableMata = TABLE_CACHE.get(entityClass);
        if (tableMata == null) {
            tableMata = new TableMeta(entityClass);
            TABLE_CACHE.put(entityClass, tableMata);
        }

        return tableMata;
    }

    /**
     * 根据实体类注解初始化实体类对应的表格的元数据信息
     *
     * @param cla
     */
    private void initTableMetaInfo(Class<?> cla) {
        // 初始化表名
        tableName = cla.getAnnotation(Table.class).name();

        // 初始化表的别名
        tableAliasName = cla.isAnnotationPresent(SetAlias.class) ? cla.getAnnotation(SetAlias.class).value() : DEFAULT_TABLE_ALIAS_NAME;

        // 获得所有字段
        List<Field> fieldList = new ArrayList<>() ;
        Class tempClass = cla;
        while (tempClass != null) {// 当父类为null的时候说明到达了最上层的父类(Object类)
            fieldList.addAll(Arrays.asList(tempClass.getDeclaredFields()));
            tempClass = tempClass.getSuperclass(); // 得到父类, 然后赋给自己
        }
//
//        Field[] fields = cla.getDeclaredFields();
        for (Field field : fieldList) {
            String property = field.getName();

            // 初始化所有属性字段对象集合
            allFieldsMap.put(property, field);

            // 过滤static和@Transient注解修饰的属性字段以及不是基本类型的属性字段
            if (Modifier.isStatic(field.getModifiers()) ||
                    field.isAnnotationPresent(Transient.class) ||
                    !BeanUtils.isSimpleValueType(field.getType())) {
                continue;
            }

            Column column = field.getAnnotation(Column.class);
            String columnName = column.name().toUpperCase();

            if (field.isAnnotationPresent(Id.class)) {
                // 如果有主键注解标识，则初始化主键属性名和主键对应的列名
                pkProperty = property;
                pkColumn = columnName;
            }

            // 获取属性的描述信息
            PropertyDescriptor descriptor = BeanUtils.getPropertyDescriptor(cla, property);
            if (descriptor != null && descriptor.getReadMethod() != null && descriptor.getWriteMethod() != null) {
                // 如果该属性存在get和set方法，则执行如下初始化操作

                // 初始化全部属性字段和列名的映射关系
                allFieldsColumnMap.put(property, columnName);

                // 初始化全部列名和属性字段的映射关系
                allColumnsFieldMap.put(columnName, property);

                // 初始化全部属性字段和字段类型的映射关系
                allFieldsTypeMap.put(property, field.getType());

                if (field.isAnnotationPresent(DictionarieField.class)) {
                    // 如果有数据字典映射注解，则初始化需要做数据字典映射的属性字段和列名的映射关系
                    dictionarieFieldsColumnMap.put(property, field.getAnnotation(DictionarieField.class));
                }

                if (field.isAnnotationPresent(ValidateExistField.class)) {
                    // 如果有ValidateExistField注解，则初始化验证已存在数据的条件字段和列名的映射关系
                    validateExistFieldsColumnMap.put(property, columnName);
                }
            }
        }
    }

    /**
     * 反射获取属性值
     *
     * @param entityClass
     * @param property
     * @return
     * @throws Exception
     */
    public static Object getPropertyValue(Class<?> entityClass, String property, Object obj) throws Exception {

        // 获取属性的描述信息
        Field field = entityClass.getDeclaredField(property);
        field.setAccessible(true);
        PropertyDescriptor pd = BeanUtils.getPropertyDescriptor(entityClass, property);
        Method readMethod = pd.getReadMethod();
        return readMethod.invoke(obj);
    }

    /**
     * 反射设置属性值
     *
     * @param entityClass
     * @param property
     * @return
     * @throws Exception
     */
    public static Object setPropertyValue(Class<?> entityClass, String property, String value, Object obj) throws Exception {

        // 获取属性的描述信息
        Field field = entityClass.getDeclaredField(property);
        field.setAccessible(true);
        PropertyDescriptor pd = BeanUtils.getPropertyDescriptor(entityClass, property);
        Method writeMethod = pd.getWriteMethod();
        return writeMethod.invoke(obj, value);
    }
}
