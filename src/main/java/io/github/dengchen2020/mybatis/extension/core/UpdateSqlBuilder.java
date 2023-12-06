package io.github.dengchen2020.mybatis.extension.core;

import io.github.dengchen2020.mybatis.extension.constant.Params;
import io.github.dengchen2020.mybatis.extension.exception.MybatisCustomException;
import io.github.dengchen2020.mybatis.extension.constant.Update;
import io.github.dengchen2020.mybatis.extension.metainfo.TableInfo;
import io.github.dengchen2020.mybatis.extension.util.ProviderUtils;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static io.github.dengchen2020.mybatis.extension.constant.SQL.*;
import static io.github.dengchen2020.mybatis.extension.util.ProviderUtils.getTableInfo;

/**
 * 更新构建
 *
 * @author dengchen
 */
class UpdateSqlBuilder extends UpdateWrapper {

    Log log = LogFactory.getLog(this.getClass());

    public UpdateSqlBuilder() {
        super();
    }

    public UpdateSqlBuilder(UpdateWrapper wrapper) {
        this.sets = wrapper.sets;
        this.limit = wrapper.limit;
        this.conditions = wrapper.conditions;
        this.args = wrapper.args;
        this.paramName = wrapper.paramName;
    }

    protected String tableName;

    public static UpdateSqlBuilder builder() {
        return new UpdateSqlBuilder();
    }

    public UpdateSqlBuilder update(String tableName) {
        this.tableName = tableName;
        return this;
    }

    public UpdateSqlBuilder set(List<String> columns) {
        sets = new ArrayList<>(1);
        sets.add(columns.stream().map(column -> column + EQ + ognlParam(Params.ENTITY + POINT + getTableInfo(tableName).getField(column))).collect(Collectors.joining(COMMA)));
        return this;
    }

    @Override
    public String toString() {
        if (conditions == null) throw new MybatisCustomException("更新条件不能为空");
        TableInfo tableInfo = getTableInfo(tableName);
        StringBuilder sqlBuilder = new StringBuilder(Update.UPDATE).append(tableName);
        if (sets == null) set(ProviderUtils.getTableInfo(tableName).getUpdateColumns());
        sqlBuilder.append(Update.SET).append(sets.get(0)).append(WHERE).append(conditions == null ? tableInfo.getIdColumn() + EQ + ognlParam(tableInfo.getIdField()) : where());
        if (limit != null) sqlBuilder.append(LIMIT).append(limit);
        return sqlBuilder.toString();
    }

}
