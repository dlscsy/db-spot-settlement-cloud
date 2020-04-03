package cn.csg.core.common.mapper.helper;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class CustomMapTypeHandler extends BaseTypeHandler<Map<String, String>> {
    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, Map<String, String> stringStringMap, JdbcType jdbcType) throws SQLException {

    }

    @Override
    public Map<String, String> getNullableResult(ResultSet resultSet, String columnName) throws SQLException {
        String value = resultSet.getString(columnName);
        return this.getMap(columnName, value);
    }

    @Override
    public Map<String, String> getNullableResult(ResultSet resultSet, int columnIndex) throws SQLException {
        String value = resultSet.getString(columnIndex);
        return this.getMap(columnIndex + "", value);
    }

    @Override
    public Map<String, String> getNullableResult(CallableStatement callableStatement, int columnIndex) throws SQLException {
        String value = callableStatement.getString(columnIndex);
        return this.getMap(columnIndex + "", value);
    }

    private Map<String, String> getMap(String key, String value) {
        Map<String, String> map = new HashMap<String, String>();
        map.put(key, value);
        return map;
    }
}
