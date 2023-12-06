package io.github.dengchen2020.mybatis.extension.core;

import java.util.List;

public class DeleteWrapper extends Wrapper {

    public DeleteWrapper(){}

    public static DeleteWrapper create(){
        return new DeleteWrapper();
    }

    public DeleteWrapper limit(int limit){
        this.limit = limit;
        return this;
    }



    @Override
    public DeleteWrapper addCondition(final String condition) {
        super.addCondition(condition);
        return this;
    }

    @Override
    public DeleteWrapper or() {
        super.or();
        return this;
    }

    @Override
    public DeleteWrapper eq(final String column, final Object value) {
        super.eq(column, value);
        return this;
    }

    @Override
    public DeleteWrapper ne(final String column, final Object value) {
        super.ne(column, value);
        return this;
    }

    @Override
    public DeleteWrapper ge(final String column, final Object value) {
        super.ge(column, value);
        return this;
    }

    @Override
    public DeleteWrapper lt(final String column, final Object value) {
        super.lt(column, value);
        return this;
    }

    @Override
    public DeleteWrapper goe(final String column, final Object value) {
        super.goe(column, value);
        return this;
    }

    @Override
    public DeleteWrapper loe(final String column, final Object value) {
        super.loe(column, value);
        return this;
    }

    @Override
    public DeleteWrapper in(final String column, final String... value) {
        super.in(column, value);
        return this;
    }

    @Override
    public DeleteWrapper in(final String column, final List<?> list) {
        super.in(column, list);
        return this;
    }

    @Override
    public DeleteWrapper notIn(final String column, final String... value) {
        super.notIn(column, value);
        return this;
    }

    @Override
    public DeleteWrapper notIn(final String column, final List<?> list) {
        super.notIn(column, list);
        return this;
    }

    @Override
    public DeleteWrapper contains(final String column, final String value) {
        super.contains(column, value);
        return this;
    }

    @Override
    public DeleteWrapper notContains(final String column, final String value) {
        super.notContains(column, value);
        return this;
    }

    @Override
    public DeleteWrapper startsWith(final String column, final String value) {
        super.startsWith(column, value);
        return this;
    }

    @Override
    public DeleteWrapper notStartsWith(final String column, final String value) {
        super.notStartsWith(column, value);
        return this;
    }

    @Override
    public DeleteWrapper endsWith(final String column, final String value) {
        super.endsWith(column, value);
        return this;
    }

    @Override
    public DeleteWrapper notEndsWith(final String column, final String value) {
        super.notEndsWith(column, value);
        return this;
    }

    @Override
    public DeleteWrapper isNull(final String column) {
        super.isNull(column);
        return this;
    }

    @Override
    public DeleteWrapper isNotNull(final String column) {
        super.isNotNull(column);
        return this;
    }

    @Override
    public DeleteWrapper isTrue(final String column) {
        super.isTrue(column);
        return this;
    }

    @Override
    public DeleteWrapper isFalse(final String column) {
        super.isFalse(column);
        return this;
    }

    @Override
    public DeleteWrapper isUnknown(final String column) {
        super.isUnknown(column);
        return this;
    }

    @Override
    public DeleteWrapper regexp(final String column, final String value) {
        super.regexp(column, value);
        return this;
    }

    @Override
    public DeleteWrapper notRegexp(final String column, final String value) {
        super.notRegexp(column, value);
        return this;
    }

}
