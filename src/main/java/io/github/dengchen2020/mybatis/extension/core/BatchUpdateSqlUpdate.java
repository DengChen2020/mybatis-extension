package io.github.dengchen2020.mybatis.extension.core;

import io.github.dengchen2020.mybatis.extension.constant.Params;
import io.github.dengchen2020.mybatis.extension.constant.Update;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static io.github.dengchen2020.mybatis.extension.constant.SQL.*;
import static io.github.dengchen2020.mybatis.extension.util.ProviderUtils.getTableInfo;

/**
 * 批量更新构建
 *
 * @author dengchen
 */
class BatchUpdateSqlUpdate extends UpdateSqlBuilder {

    public static BatchUpdateSqlUpdate builder() {
        return new BatchUpdateSqlUpdate();
    }

    public BatchUpdateSqlUpdate update(String tableName) {
        super.update(tableName);
        return this;
    }

    public BatchUpdateSqlUpdate set(String[] columns, Integer size) {
        return set(new ArrayList<>(Arrays.asList(columns)), size);
    }

    public BatchUpdateSqlUpdate set(List<String> columns, Integer size) {
        sets = foreachSetParam(size, columns, getTableInfo(tableName)::getField, EQ);
        return this;
    }

    private List<String> foreachSetParam(Integer size, List<String> columns, Function<String, String> function, String oper) {
        return IntStream.range(0, size)
                .mapToObj(i -> columns.stream()
                        .map(column -> column + oper + ognlParam(Params.LIST + forListParam(i) + function.apply(column)))
                        .collect(Collectors.joining(COMMA)))
                .collect(Collectors.toList());
    }

    @Override
    public String toString() {
        String id = getTableInfo(tableName).getIdColumn();
        return IntStream.range(0, sets.size())
                .mapToObj(i -> Update.UPDATE + tableName +
                        Update.SET + sets.get(i) + WHERE + id + EQ + ognlParam(Params.LIST + forListParam(i) + id))
                .collect(Collectors.joining(SEMICOLON));
    }
}
