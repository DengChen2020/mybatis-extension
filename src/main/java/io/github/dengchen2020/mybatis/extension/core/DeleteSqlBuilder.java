package io.github.dengchen2020.mybatis.extension.core;

import io.github.dengchen2020.mybatis.extension.exception.MybatisCustomException;

import static io.github.dengchen2020.mybatis.extension.constant.Delete.DELETE;
import static io.github.dengchen2020.mybatis.extension.constant.SQL.LIMIT;
import static io.github.dengchen2020.mybatis.extension.constant.SQL.WHERE;

/**
 * 删除构建
 *
 * @author dengchen
 */
class DeleteSqlBuilder extends DeleteWrapper {

    public DeleteSqlBuilder() {
    }

    public DeleteSqlBuilder(DeleteWrapper wrapper) {
        this.limit = wrapper.limit;
        this.conditions = wrapper.conditions;
        this.args = wrapper.args;
        this.paramName = wrapper.paramName;
    }

    protected String tableName;

    public static DeleteSqlBuilder builder() {
        return new DeleteSqlBuilder();
    }

    public DeleteSqlBuilder delete(String tableName) {
        this.tableName = tableName;
        return this;
    }

    @Override
    public String toString() {
        if (conditions.isEmpty()) throw new MybatisCustomException("删除条件不能为空");
        StringBuilder sqlBuilder = new StringBuilder(DELETE).append(tableName);
        sqlBuilder.append(WHERE).append(where());
        if (limit != null) sqlBuilder.append(LIMIT).append(limit);
        return sqlBuilder.toString();
    }
}
