package io.github.dengchen2020.mybatis.extension.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static io.github.dengchen2020.mybatis.extension.constant.SQL.*;
import static io.github.dengchen2020.mybatis.extension.constant.Select.*;

/**
 * 查询构建
 *
 * @author dengchen
 */
class SelectSqlBuilder extends QueryWrapper {

    public SelectSqlBuilder() {
        super();
    }

    public SelectSqlBuilder(QueryWrapper wrapper) {
        this.first = wrapper.first;
        this.exists = wrapper.exists;
        this.count = wrapper.count;
        this.distints = wrapper.distints;
        this.columns = wrapper.columns;
        this.leftJoinList = wrapper.leftJoinList;
        this.innerJoinList = wrapper.innerJoinList;
        this.rightJoinList = wrapper.rightJoinList;
        this.groupBys = wrapper.groupBys;
        this.orderBy = wrapper.orderBy;
        this.offset = wrapper.offset;
        this.limit = wrapper.limit;
        this.last = wrapper.last;
        this.conditions = wrapper.conditions;
        this.args = wrapper.args;
        this.paramName = wrapper.paramName;
    }

    private String tableName;

    public static SelectSqlBuilder builder() {
        return new SelectSqlBuilder();
    }

    public SelectSqlBuilder select(List<String> columns) {
        this.columns = new ArrayList<>(columns.size());
        this.columns.addAll(columns);
        return this;
    }

    public SelectSqlBuilder from(String tableName) {
        this.tableName = tableName;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sqlBuilder = new StringBuilder(SELECT);
        if (first != null) sqlBuilder.append(first);
        if (!exists && !count) {
            if (distints != null) {
                sqlBuilder.append(DISTINCT).append(String.join(COMMA, distints));
            } else {
                if (columns == null || columns.isEmpty()) columns = new ArrayList<>(Arrays.asList(ASTERISK));
                sqlBuilder.append(String.join(COMMA, columns));
            }
        }
        sqlBuilder.append(FROM).append(tableName);
        if (leftJoinList != null)
            leftJoinList.forEach(leftJoin -> sqlBuilder.append(LEFT_JOIN).append(leftJoin.getTable()).append(ON).append(leftJoin.getLeftColumn()).append(EQ).append(leftJoin.getRightColumn()));
        if (innerJoinList != null)
            innerJoinList.forEach(innerJoin -> sqlBuilder.append(INNER_JOIN).append(innerJoin.getTable()).append(ON).append(innerJoin.getLeftColumn()).append(EQ).append(innerJoin.getRightColumn()));
        if (rightJoinList != null)
            rightJoinList.forEach(rightJoin -> sqlBuilder.append(RIGHT_JOIN).append(rightJoin.getTable()).append(ON).append(rightJoin.getLeftColumn()).append(EQ).append(rightJoin.getRightColumn()));
        if (!conditions.isEmpty()) sqlBuilder.append(WHERE).append(where());
        if (!exists) {
            if (groupBys != null) sqlBuilder.append(GROUP_BY).append(String.join(COMMA, groupBys));
            if (!count) {
                if (orderBy != null)
                    sqlBuilder.append(ORDER_BY).append(String.join(COMMA, orderBy.getColumn())).append(orderBy.getOrderType());
                if (limit != null && offset != null) {
                    sqlBuilder.append(LIMIT).append(offset).append(COMMA).append(limit);
                } else if (limit != null) {
                    sqlBuilder.append(LIMIT).append(limit);
                }
            }
        }
        if (last != null) sqlBuilder.append(last);
        return sqlBuilder.toString();
    }

}
