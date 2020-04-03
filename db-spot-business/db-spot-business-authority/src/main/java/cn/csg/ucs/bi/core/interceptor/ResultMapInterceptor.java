package cn.csg.ucs.bi.core.interceptor;

import cn.csg.core.common.annotation.DictionarieField;
import cn.csg.core.common.constant.BaseBusinessMapperExceptionCode;
import cn.csg.core.common.exception.BaseBusinessMapperException;
import cn.csg.core.common.mapper.helper.CustomMapTypeHandler;
import cn.csg.core.common.structure.TableMeta;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.*;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

@Component
@Intercepts({
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class})
})
public class ResultMapInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        if (!(invocation.getTarget() instanceof Executor)) {
            return invocation.proceed();
        }

        MappedStatement ms = (MappedStatement) invocation.getArgs()[0];

        // 如果是通过XML文件构建的SQL，则不做处理
        if (ms.getResource().contains(".xml")) {
            return invocation.proceed();
        }

        ResultMap resultMap = ms.getResultMaps().iterator().next();
        // 如果有ResultMap，则不做处理
        if (!CollectionUtils.isEmpty(resultMap.getResultMappings())) {
            return invocation.proceed();
        }

        Class<?> mapType = resultMap.getType();
        if (BeanUtils.isSimpleValueType(mapType)) {
            return invocation.proceed();
        }

        if (mapType.isAssignableFrom(List.class)) {
            String resource = ms.getResource();
            String className = resource.substring(0, resource.lastIndexOf(".")).replaceAll("/", ".");
            Class<?> mapperClass = Class.forName(className);
            Type[] types = mapperClass.getGenericInterfaces();
            ParameterizedType pt = (ParameterizedType) types[0];
            Class<?> genericClass = (Class<?>) pt.getActualTypeArguments()[0];
            TableMeta tableMeta = TableMeta.forClass(genericClass);
            Map<String, DictionarieField> dictionarieFieldsColumnMap = tableMeta.getDictionarieFieldsColumnMap();
            Map<String, Class<?>> fieldTypeMap = tableMeta.getAllFieldsTypeMap();
            List<ResultMapping> resultMappings = new ArrayList<>(fieldTypeMap.size());
            for (Map.Entry<String, String> entry : tableMeta.getAllFieldsColumnMap().entrySet()) {
                ResultMapping resultMapping = new ResultMapping.Builder(ms.getConfiguration(), entry.getKey(), entry.getValue(), fieldTypeMap.get(entry.getKey())).build();
                resultMappings.add(resultMapping);
                if (dictionarieFieldsColumnMap.containsKey(entry.getKey())) {
                    String dictionarieProperty = dictionarieFieldsColumnMap.get(entry.getKey()).PROPERTY();
                    if (!tableMeta.getAllFieldsMap().containsKey(dictionarieProperty) || tableMeta.getAllFieldsMap().get(dictionarieProperty).getType() != Map.class) {
                        throw new BaseBusinessMapperException(BaseBusinessMapperExceptionCode.CLASSTYPE_MATCH_MAP_EXCEPTION,
                                "实体类中为定义用于映射的属性或者用于映射的属性不是Map类型");
                    }
                    ResultMapping resultDictionarieMapping = new ResultMapping.Builder(ms.getConfiguration(), dictionarieProperty, entry.getValue() + tableMeta.DEFAULT_RELATION_COLUMN_ALIAS_NAME_SUFFIX, CustomMapTypeHandler.class.newInstance()).build();
                    resultMappings.add(resultDictionarieMapping);
                }
            }
            ResultMap newRm = new ResultMap.Builder(ms.getConfiguration(), resultMap.getId(), genericClass, resultMappings).build();
            Field field = ReflectionUtils.findField(MappedStatement.class, "resultMaps");
            ReflectionUtils.makeAccessible(field);
            ReflectionUtils.setField(field, ms, Collections.singletonList(newRm));
        }

        return invocation.proceed();
    }

    @Override
    public Object plugin(Object o) {
        return Plugin.wrap(o, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }
}
