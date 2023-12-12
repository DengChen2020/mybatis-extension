package io.github.dengchen2020.mybatis.extension.core;

import io.github.dengchen2020.mybatis.extension.constant.Params;
import io.github.dengchen2020.mybatis.extension.constant.Update;

import java.util.ArrayList;
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

    public BatchUpdateSqlUpdate update(String tableName,List<List<String>> columns) {
        super.update(tableName);
        sets = foreachSetParam(columns, getTableInfo(tableName)::getField);
        return this;
    }

    private List<String> foreachSetParam(List<List<String>> columns, Function<String, String> function) {
        List<String> list = new ArrayList<>();
        IntStream.range(0, columns.size()).forEach(i -> list.add(columns.get(i).stream()
                .map(column -> column + EQ + ognlParam(Params.LIST + forListParam(i) + function.apply(column)))
                .collect(Collectors.joining(COMMA))));
        return list;
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
