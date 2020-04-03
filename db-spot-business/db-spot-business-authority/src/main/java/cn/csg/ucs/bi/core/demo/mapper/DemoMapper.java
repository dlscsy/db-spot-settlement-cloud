package cn.csg.ucs.bi.core.demo.mapper;

import cn.csg.core.common.mapper.BaseBusinessMapper;
import cn.csg.ucs.bi.core.demo.entity.Demo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DemoMapper extends BaseBusinessMapper<Demo> {
    // --CustomConditionProvider -- customQueryByConditions--, customDelByConditions--, customExistByConditions--
    // --CustomBaseSelectProvider -- customQueryByPrimaryKey--, customDelByPrimaryKey--
    // --CustomIdsProvider -- customQueryByPrimaryKeys--, customDelByPrimaryKeys--
    // CustomInsertProvider -- customInsert, customBatchInsert, customBatchInsertFromSelect
    // CustomUpdateProvider -- customUpdate, customBatchUpdate
    // CustomMySqlProvider
    // --CustomProviderException
}
