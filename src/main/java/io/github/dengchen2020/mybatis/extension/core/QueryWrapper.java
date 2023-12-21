package io.github.dengchen2020.mybatis.extension.core;

import io.github.dengchen2020.mybatis.extension.help.Join;
import io.github.dengchen2020.mybatis.extension.help.OrderBy;
import io.github.dengchen2020.mybatis.extension.util.ProviderUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static io.github.dengchen2020.mybatis.extension.constant.SQL.*;
import static io.github.dengchen2020.mybatis.extension.constant.SQL.AND;
import static io.github.dengchen2020.mybatis.extension.constant.Select.*;
import static io.github.dengchen2020.mybatis.extension.util.ProviderUtils.getTableInfo;

/**
 * 查询构建
 *
 * @author dengchen
 */
public class QueryWrapper extends Wrapper {

    public QueryWrapper() {}

    protected String first;

    protected boolean exists;

    protected boolean count;

    protected String[] distints;

    protected List<String> columns;

    protected List<Join> leftJoinList;

    protected List<Join> innerJoinList;

    protected List<Join> rightJoinList;

    protected String[] groupBys;

    protected boolean having;

    protected List<String> havingConditions = new ArrayList<>();

    protected OrderBy orderBy;

    protected Integer offset;

    protected String last;

    public List<String> getColumns() {
        return columns;
    }

    public static QueryWrapper create() {
        return new QueryWrapper();
    }

    public QueryWrapper distint(String... columns) {
        this.distints = columns;
        return this;
    }

    public QueryWrapper select() {
        this.count = false;
        this.exists = false;
        this.first = null;
        this.last = null;
        return this;
    }

    public QueryWrapper select(String... columns) {
        this.columns = new ArrayList<>(columns.length);
        this.columns.addAll(new ArrayList<>(Arrays.asList(columns)));
        return select();
    }

    public QueryWrapper select(List<String> columns, String... optionalColumns) {
        this.columns = new ArrayList<>(columns.size() + optionalColumns.length);
        this.columns.addAll(columns);
        if (optionalColumns.length > 0) {
            this.columns.addAll(new ArrayList<>(Arrays.asList(optionalColumns)));
        }
        return select();
    }

    /**
     * 根据类中的字段进行查询
     *
     * @param entityClass 要查询的字段类
     * @return queryWrapper
     * @apiNote 可使用@Column、@AS、@Transient等注解辅助查询
     */
    public QueryWrapper select(Class<?> entityClass) {
        return select(ProviderUtils.getTableInfo(entityClass).getAllColumn().toArray(new String[0]));
    }

    public QueryWrapper exists() {
        this.exists = true;
        this.first = EXISTS;
        this.last = CLOSE;
        return this;
    }

    public QueryWrapper count() {
        this.count = true;
        this.first = COUNT;
        this.last = CLOSE + AS + "total";
        return this;
    }

    public QueryWrapper leftJoin(String tableName, String leftColumn, String rightColumn) {
        if (leftJoinList == null || leftJoinList.isEmpty()) leftJoinList = new ArrayList<>();
        leftJoinList.add(new Join(tableName, leftColumn, rightColumn));
        return this;
    }

    public QueryWrapper leftJoin(Class<?> entityClass, String leftColumn, String rightColumn) {
        return leftJoin(getTableInfo(entityClass).getTableName(), leftColumn, rightColumn);
    }

    public QueryWrapper innerJoin(String tableName, String leftColumn, String rightColumn) {
        if (innerJoinList == null || innerJoinList.isEmpty()) innerJoinList = new ArrayList<>();
        innerJoinList.add(new Join(tableName, leftColumn, rightColumn));
        return this;
    }

    public QueryWrapper innerJoin(Class<?> entityClass, String leftColumn, String rightColumn) {
        return innerJoin(getTableInfo(entityClass).getTableName(), leftColumn, rightColumn);
    }

    public QueryWrapper rightJoin(String tableName, String leftColumn, String rightColumn) {
        if (rightJoinList == null || rightJoinList.isEmpty()) rightJoinList = new ArrayList<>();
        rightJoinList.add(new Join(tableName, leftColumn, rightColumn));
        return this;
    }

    public QueryWrapper rightJoin(Class<?> entityClass, String leftColumn, String rightColumn) {
        return rightJoin(getTableInfo(entityClass).getTableName(), leftColumn, rightColumn);
    }

    public QueryWrapper groupBy(String... columns) {
        groupBys = columns;
        return this;
    }

    public QueryWrapper having() {
        having = true;
        return this;
    }

    public QueryWrapper orderByAsc(String... columns) {
        orderBy = new OrderBy(columns, ASC);
        return this;
    }

    public QueryWrapper orderByDesc(String... columns) {
        orderBy = new OrderBy(columns, DESC);
        return this;
    }

    public QueryWrapper limit(Integer offset, Integer limit) {
        this.offset = offset;
        this.limit = limit;
        return this;
    }

    public QueryWrapper limit(Integer limit) {
        this.limit = limit;
        return this;
    }

    public QueryWrapper forUpdate() {
        if (!exists && !count) {
            last = FOR_UPDATE;
        }
        return this;
    }

    public QueryWrapper forUpdateNoWait() {
        if (!exists && !count) {
            last = FOR_UPDATE_NO_WAIT;
        }
        return this;
    }

    public QueryWrapper forShare() {
        if (!exists && !count) {
            last = FOR_SHARE;
        }
        return this;
    }

    public QueryWrapper forShareNoWait() {
        if (!exists && !count) {
            last = FOR_SHARE_NO_WAIT;
        }
        return this;
    }


    @Override
    public QueryWrapper addCondition(final String condition) {
        if (having) {
            addHavingCondition(condition);
            return this;
        }
        super.addCondition(condition);
        return this;
    }

    public Wrapper addHavingCondition(String condition) {
        if (!havingConditions.isEmpty() && !AND.contentEquals(havingConditions.get(havingConditions.size() - 1)) && !OR.contentEquals(havingConditions.get(havingConditions.size() - 1)) && !AND.equals(condition) && !OR.equals(condition)) {
            havingConditions.add(AND);
        }
        havingConditions.add(condition);
        return this;
    }

    @Override
    public QueryWrapper or() {
        super.or();
        return this;
    }

    @Override
    public QueryWrapper eq(final String column, final Object value) {
        super.eq(column, value);
        return this;
    }

    @Override
    public QueryWrapper ne(final String column, final Object value) {
        super.ne(column, value);
        return this;
    }

    @Override
    public QueryWrapper gt(final String column, final Object value) {
        super.gt(column, value);
        return this;
    }

    @Override
    public QueryWrapper lt(final String column, final Object value) {
        super.lt(column, value);
        return this;
    }

    @Override
    public QueryWrapper goe(final String column, final Object value) {
        super.goe(column, value);
        return this;
    }

    @Override
    public QueryWrapper loe(final String column, final Object value) {
        super.loe(column, value);
        return this;
    }

    @Override
    public QueryWrapper in(final String column, final Object... value) {
        super.in(column, value);
        return this;
    }

    @Override
    public QueryWrapper in(final String column, final List<?> list) {
        super.in(column, list);
        return this;
    }

    @Override
    public QueryWrapper notIn(final String column, final Object... value) {
        super.notIn(column, value);
        return this;
    }

    @Override
    public QueryWrapper notIn(final String column, final List<?> list) {
        super.notIn(column, list);
        return this;
    }

    @Override
    public QueryWrapper contains(final String column, final String value) {
        super.contains(column, value);
        return this;
    }

    @Override
    public QueryWrapper notContains(final String column, final String value) {
        super.notContains(column, value);
        return this;
    }

    @Override
    public QueryWrapper startsWith(final String column, final String value) {
        super.startsWith(column, value);
        return this;
    }

    @Override
    public QueryWrapper notStartsWith(final String column, final String value) {
        super.notStartsWith(column, value);
        return this;
    }

    @Override
    public QueryWrapper endsWith(final String column, final String value) {
        super.endsWith(column, value);
        return this;
    }

    @Override
    public QueryWrapper notEndsWith(final String column, final String value) {
        super.notEndsWith(column, value);
        return this;
    }

    @Override
    public QueryWrapper isNull(final String column) {
        super.isNull(column);
        return this;
    }

    @Override
    public QueryWrapper isNotNull(final String column) {
        super.isNotNull(column);
        return this;
    }

    @Override
    public QueryWrapper isTrue(final String column) {
        super.isTrue(column);
        return this;
    }

    @Override
    public QueryWrapper isFalse(final String column) {
        super.isFalse(column);
        return this;
    }

    @Override
    public QueryWrapper isUnknown(final String column) {
        super.isUnknown(column);
        return this;
    }

    @Override
    public QueryWrapper regexp(final String column, final String value) {
        super.regexp(column, value);
        return this;
    }

    @Override
    public QueryWrapper notRegexp(final String column, final String value) {
        super.notRegexp(column, value);
        return this;
    }

}
