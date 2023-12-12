package io.github.dengchen2020.mybatis.extension.core;

import java.util.ArrayList;
import java.util.List;

import static io.github.dengchen2020.mybatis.extension.constant.SQL.*;

/**
 * 更新构建
 *
 * @author dengchen
 */
public class UpdateWrapper extends Wrapper {

    public UpdateWrapper() {}

    protected List<String> sets;

    public static UpdateWrapper create(){
        return new UpdateWrapper();
    }

    public void set(String setFragment) {
        if (sets == null) sets = new ArrayList<>();
        sets.add(setFragment);
    }

    public UpdateWrapper set(String column, Object value) {
        this.set(column + EQ + safeParam(value));
        return this;
    }

    public UpdateWrapper increase(String column, long num) {
        this.set(column + EQ + column + ADD + num);
        return this;
    }

    public UpdateWrapper decrease(String column, long num) {
        this.set(column + EQ + column + SUBTRACT + num);
        return this;
    }

    public UpdateWrapper limit(Integer limit) {
        this.limit = limit;
        return this;
    }




    @Override
    public UpdateWrapper addCondition(final String condition) {
        super.addCondition(condition);
        return this;
    }

    @Override
    public UpdateWrapper or() {
        super.or();
        return this;
    }

    @Override
    public UpdateWrapper eq(final String column, final Object value) {
        super.eq(column, value);
        return this;
    }

    @Override
    public UpdateWrapper ne(final String column, final Object value) {
        super.ne(column, value);
        return this;
    }

    @Override
    public UpdateWrapper gt(final String column, final Object value) {
        super.gt(column, value);
        return this;
    }

    @Override
    public UpdateWrapper lt(final String column, final Object value) {
        super.lt(column, value);
        return this;
    }

    @Override
    public UpdateWrapper goe(final String column, final Object value) {
        super.goe(column, value);
        return this;
    }

    @Override
    public UpdateWrapper loe(final String column, final Object value) {
        super.loe(column, value);
        return this;
    }

    @Override
    public UpdateWrapper in(final String column, final Object... value) {
        super.in(column, value);
        return this;
    }

    @Override
    public UpdateWrapper in(final String column, final List<?> list) {
        super.in(column, list);
        return this;
    }

    @Override
    public UpdateWrapper notIn(final String column, final Object... value) {
        super.notIn(column, value);
        return this;
    }

    @Override
    public UpdateWrapper notIn(final String column, final List<?> list) {
        super.notIn(column, list);
        return this;
    }

    @Override
    public UpdateWrapper contains(final String column, final String value) {
        super.contains(column, value);
        return this;
    }

    @Override
    public UpdateWrapper notContains(final String column, final String value) {
        super.notContains(column, value);
        return this;
    }

    @Override
    public UpdateWrapper startsWith(final String column, final String value) {
        super.startsWith(column, value);
        return this;
    }

    @Override
    public UpdateWrapper notStartsWith(final String column, final String value) {
        super.notStartsWith(column, value);
        return this;
    }

    @Override
    public UpdateWrapper endsWith(final String column, final String value) {
        super.endsWith(column, value);
        return this;
    }

    @Override
    public UpdateWrapper notEndsWith(final String column, final String value) {
        super.notEndsWith(column, value);
        return this;
    }

    @Override
    public UpdateWrapper isNull(final String column) {
        super.isNull(column);
        return this;
    }

    @Override
    public UpdateWrapper isNotNull(final String column) {
        super.isNotNull(column);
        return this;
    }

    @Override
    public UpdateWrapper isTrue(final String column) {
        super.isTrue(column);
        return this;
    }

    @Override
    public UpdateWrapper isFalse(final String column) {
        super.isFalse(column);
        return this;
    }

    @Override
    public UpdateWrapper isUnknown(final String column) {
        super.isUnknown(column);
        return this;
    }

    @Override
    public UpdateWrapper regexp(final String column, final String value) {
        super.regexp(column, value);
        return this;
    }

    @Override
    public UpdateWrapper notRegexp(final String column, final String value) {
        super.notRegexp(column, value);
        return this;
    }

}
