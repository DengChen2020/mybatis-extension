package io.github.dengchen2020.mybatis.extension.core;

import io.github.dengchen2020.mybatis.extension.constant.Insert;
import io.github.dengchen2020.mybatis.extension.util.ProviderUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static io.github.dengchen2020.mybatis.extension.constant.SQL.*;
import static io.github.dengchen2020.mybatis.extension.util.ProviderUtils.getTableInfo;

/**
 * 新增构建
 *
 * @author dengchen
 */
class InsertSqlBuilder {

    protected String tableName;

    protected List<String> columns;

    protected List<String> values;

    public static InsertSqlBuilder builder() {
        return new InsertSqlBuilder();
    }

    public InsertSqlBuilder insert(String tableName) {
        this.tableName = tableName;
        return this;
    }

    public InsertSqlBuilder column(final List<String> columns) {
        this.columns = columns;
        values = new ArrayList<>(1);
        values.add(columns.stream().map(column -> ognlParam(ProviderUtils.getTableInfo(tableName).getField(column))).collect(Collectors.joining(COMMA, OPEN, CLOSE)));
        return this;
    }

    public InsertSqlBuilder column(String... columns) {
        return column(new ArrayList<>(Arrays.asList(columns)));
    }

    @Override
    public String toString() {
        StringBuilder sqlBuilder = new StringBuilder(Insert.INSERT_INTO).append(tableName);
        if (columns == null) column(ProviderUtils.getTableInfo(tableName).getAllColumn());
        sqlBuilder.append(columns.stream().collect(Collectors.joining(COMMA, OPEN, CLOSE)));
        if (!values.isEmpty()) sqlBuilder.append(Insert.VALUES).append(String.join(COMMA, values));
        return sqlBuilder.toString();
    }

    public List<String> getColumns() {
        return columns;
    }
}
