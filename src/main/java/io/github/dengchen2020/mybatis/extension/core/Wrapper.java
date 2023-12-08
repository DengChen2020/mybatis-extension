package io.github.dengchen2020.mybatis.extension.core;

import io.github.dengchen2020.mybatis.extension.exception.MybatisCustomException;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static io.github.dengchen2020.mybatis.extension.constant.Params.*;
import static io.github.dengchen2020.mybatis.extension.constant.SQL.*;

public class Wrapper {

    protected List<String> conditions = new ArrayList<>();

    protected Integer limit;

    protected Map<String, Object> args = new HashMap<>();

    protected Map<String, Object> getArgs() {
        return args;
    }

    protected AtomicInteger paramName = new AtomicInteger(0);

    protected String getParamStr(Object param) {
        String paramNameStr = ARG + paramName.incrementAndGet();
        String paramStr = WRAPPER + ARGS + paramNameStr;
        args.put(paramNameStr, param);
        return paramStr;
    }

    protected String safeParam(Object param) {
        if (param instanceof List<?>) {
            List<?> list = (List<?>) param;
            return safeParam(list);
        }
        String paramStr = getParamStr(param);
        return ognlParam(paramStr);
    }

    protected String safeParam(List<?> list) {
        String paramStr = getParamStr(list);
        return IntStream.range(0, list.size())
                .mapToObj(i -> ognlParam(paramStr + forObjParam(i)))
                .collect(Collectors.joining(COMMA, OPEN, CLOSE));
    }

    public Wrapper addCondition(String condition) {
        if (!this.conditions.isEmpty() && !AND.contentEquals(this.conditions.get(this.conditions.size() - 1)) && !OR.contentEquals(this.conditions.get(this.conditions.size() - 1)) && !AND.equals(condition) && !OR.equals(condition)) {
            this.conditions.add(AND);
        }
        this.conditions.add(condition);
        return this;
    }

    protected String where() {
        return String.join(SPACE, conditions);
    }

    private Wrapper add(String column, String keyword) {
        return add(column, keyword, null, "");
    }

    private Wrapper add(String column, String keyword, Object value) {
        return add(column, keyword, value, "");
    }

    private Wrapper add(String column, String keyword, Object value, String suffix) {
        return addCondition(column + keyword + (Objects.isNull(value) ? "" : safeParam(value)) + suffix);
    }

    public Wrapper or() {
        addCondition(OR);
        return this;
    }

    /**
     * 等于
     *
     * @param column 字段
     * @param value  值
     * @apiNote = value
     */
    public Wrapper eq(String column, Object value) {
        return add(column, EQ, value);
    }

    /**
     * 不等于
     *
     * @param column 字段
     * @param value  值
     * @apiNote <> value
     */
    public Wrapper ne(String column, Object value) {
        return add(column, NE, value);
    }

    /**
     * 大于
     *
     * @param column 字段
     * @param value  值
     * @apiNote > value
     */
    public Wrapper ge(String column, Object value) {
        return add(column, GE, value);
    }

    /**
     * 小于
     *
     * @param column 字段
     * @param value  值
     * @apiNote < value
     */
    public Wrapper lt(String column, Object value) {
        return add(column, LT, value);
    }

    /**
     * 大于等于
     *
     * @param column 字段
     * @param value  值
     * @apiNote >= value
     */
    public Wrapper goe(String column, Object value) {
        return add(column, GOE, value);
    }

    /**
     * 小于等于
     *
     * @param column 字段
     * @param value  值
     * @apiNote <= value
     */
    public Wrapper loe(String column, Object value) {
        return add(column, LOE, value);
    }

    public Wrapper in(String column, Object... value) {
        return in(column, Arrays.stream(value).collect(Collectors.toList()));
    }

    public Wrapper in(String column, List<?> list) {
        return add(column, IN, list);
    }

    public Wrapper notIn(String column, Object... value) {
        return notIn(column, Arrays.stream(value).collect(Collectors.toList()));
    }

    public Wrapper notIn(String column, List<?> list) {
        return add(column, NOT_IN, list);
    }

    private String checkString(String value) {
        if (value == null || value.isEmpty()) {
            throw new MybatisCustomException("条件值不能为空");
        }
        return value.replace(PERCENT, "\\" + PERCENT).replace("_", "\\" + PERCENT);
    }

    /**
     * 模糊查询
     *
     * @param column 字段
     * @param value  值
     * @apiNote like '%value%'
     */
    public Wrapper contains(String column, String value) {
        return add(column, LIKE + CONCAT + OPEN + SINGLE_QUOTE + PERCENT + SINGLE_QUOTE + COMMA, checkString(value), COMMA + SINGLE_QUOTE + PERCENT + SINGLE_QUOTE + CLOSE);
    }

    /**
     * 模糊查询
     *
     * @param column 字段
     * @param value  值
     * @apiNote not like '%value%'
     */
    public Wrapper notContains(String column, String value) {
        return add(column, NOT_LIKE + CONCAT + OPEN + SINGLE_QUOTE + PERCENT + SINGLE_QUOTE + COMMA, checkString(value), COMMA + SINGLE_QUOTE + PERCENT + SINGLE_QUOTE + CLOSE);
    }

    /**
     * 模糊查询
     *
     * @param column 字段
     * @param value  值
     * @apiNote like 'value%'
     */
    public Wrapper startsWith(String column, String value) {
        return add(column, LIKE + CONCAT + OPEN, checkString(value), COMMA + SINGLE_QUOTE + PERCENT + SINGLE_QUOTE + CLOSE);
    }

    /**
     * 模糊查询
     *
     * @param column 字段
     * @param value  值
     * @apiNote not like 'value%'
     */
    public Wrapper notStartsWith(String column, String value) {
        return add(column, NOT_LIKE + CONCAT + OPEN, checkString(value), COMMA + SINGLE_QUOTE + PERCENT + SINGLE_QUOTE + CLOSE);
    }

    /**
     * 模糊查询
     *
     * @param column 字段
     * @param value  值
     * @apiNote not like '%value'
     */
    public Wrapper endsWith(String column, String value) {
        return add(column, LIKE + CONCAT + OPEN + SINGLE_QUOTE + PERCENT + SINGLE_QUOTE + COMMA, checkString(value), CLOSE);
    }

    /**
     * 模糊查询
     *
     * @param column 字段
     * @param value  值
     * @apiNote not like '%value'
     */
    public Wrapper notEndsWith(String column, String value) {
        return add(column, NOT_LIKE + CONCAT + OPEN + SINGLE_QUOTE + PERCENT + SINGLE_QUOTE + COMMA, checkString(value), CLOSE);
    }

    public Wrapper isNull(String column) {
        return add(column, IS_NULL);
    }

    public Wrapper isNotNull(String column) {
        return add(column, IS_NOT_NULL);
    }

    public Wrapper isTrue(String column) {
        return add(column, IS_TRUE);
    }

    public Wrapper isFalse(String column) {
        return add(column, IS_FALSE);
    }

    public Wrapper isUnknown(String column) {
        return add(column, IS_UNKNOWN);
    }

    public Wrapper regexp(String column, String value) {
        return add(column, REGEXP, value);
    }

    public Wrapper notRegexp(String column, String value) {
        return add(column, NOT_REGEXP, value);
    }

}
