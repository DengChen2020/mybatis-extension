package io.github.dengchen2020.mybatis.extension.core;

import io.github.dengchen2020.mybatis.extension.constant.Params;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static io.github.dengchen2020.mybatis.extension.constant.SQL.*;
import static io.github.dengchen2020.mybatis.extension.util.ProviderUtils.getTableInfo;

/**
 * 批量新增构建
 * @author dengchen
 */
class BatchInsertSqlBuilder extends InsertSqlBuilder {

    public static BatchInsertSqlBuilder builder(){
        return new BatchInsertSqlBuilder();
    }

    public BatchInsertSqlBuilder insert(String tableName) {
        super.insert(tableName);
        return this;
    }

    public BatchInsertSqlBuilder column(final String[] columns, Integer size) {
        return column(new ArrayList<>(Arrays.asList(columns)), size);
    }

    public BatchInsertSqlBuilder column(final List<String> columns, Integer size) {
        this.columns = columns;
        values = foreachInsertValuesParam(size, columns, getTableInfo(tableName)::getField);
        return this;
    }

    protected List<String> foreachInsertValuesParam(Integer size, List<String> columns, Function<String, String> function) {
        return IntStream.range(0, size)
                .mapToObj(i -> {
                    StringJoiner joiner = new StringJoiner(COMMA, OPEN, CLOSE);
                    columns.stream().map(column -> ognlParam(Params.LIST + forListParam(i) + function.apply(column)))
                            .forEach(joiner::add);
                    return joiner.toString();
                }).collect(Collectors.toList());
    }

}
