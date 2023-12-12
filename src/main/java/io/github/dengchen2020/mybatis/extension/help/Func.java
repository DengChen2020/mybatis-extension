package io.github.dengchen2020.mybatis.extension.help;

import java.util.Arrays;
import java.util.stream.Collectors;

import static io.github.dengchen2020.mybatis.extension.constant.SQL.AS;

/**
 * @author dengchen
 */
public class Func {

    private static String formatParam(String s) {
        if (s != null && !s.isEmpty()) {
            char start = s.charAt(0);
            char end = s.charAt(s.length() - 1);
            if (start != '`' && end != '`' && start != '\'' && end != '\'') {
                return "'" + s + "'";
            }
        }
        return s;
    }

    public static String sum(String column) {
        return "SUM(" + column + ")";
    }

    public static String avg(String column) {
        return "AVG(" + column + ")";
    }

    public static String count(String column) {
        return "COUNT(" + column + ")";
    }

    public static String max(String column) {
        return "MAX(" + column + ")";
    }

    public static String min(String column) {
        return "MIN(" + column + ")";
    }

    public static String as(String column, String as) {
        return column + AS + as;
    }

    public static String rand() {
        return "RAND()";
    }

    public static String now() {
        return "NOW()";
    }

    public static String sysdate() {
        return "SYSDATE()";
    }

    public static String curdate() {
        return "CURDATE()";
    }

    public static String curtime() {
        return "CURTIME()";
    }

    public static String dateFormat(String column, String format) {
        return "DATE_FORMAT(" + column + ",'" + format + "')";
    }

    public static String timestampDiff(String unit, String column1, String column2) {
        return "TIMESTAMPDIFF(" + unit + "," + column1 + "," + column2 + ")";
    }

    public static String substring(String column, int start, int len) {
        return "SUBSTRING(" + column + "," + start + "," + len + ")";
    }

    public static String length(String column) {
        return "LENGTH(" + column + ")";
    }

    public static String upper(String column) {
        return "UPPER(" + column + ")";
    }

    public static String lower(String column) {
        return "LOWER(" + column + ")";
    }

    public static String trim(String column) {
        return "TRIM(" + column + ")";
    }

    public static String ltrim(String column) {
        return "LTRIM(" + column + ")";
    }

    public static String rtrim(String column) {
        return "RTRIM(" + column + ")";
    }

    public static String abs(String column) {
        return "ABS(" + column + ")";
    }

    public static String round(String column, int bits) {
        return "ROUND(" + column + "," + bits + ")";
    }

    public static String floor(String column) {
        return "FLOOR(" + column + ")";
    }

    public static String ceiling(String column) {
        return "CEILING(" + column + ")";
    }

    public static String ifnull(String column, String defaultValue) {
        return "IFNULL(" + column + ",'" + defaultValue + "')";
    }

    public static String replace(String column, String findStr, String replaceWith) {
        return "REPLACE(" + column + ",'" + findStr + "','" + replaceWith + "')";
    }

    public static String concat(String... column) {
        return "CONCAT(" + Arrays.stream(column).map(Func::formatParam).collect(Collectors.joining(",")) + ")";
    }

    public static String jsonObjectAgg(String keyColumn, String valueColumn) {
        return "JSON_OBJECTAGG(" + keyColumn + "," + valueColumn + ")";
    }

    public static String jsonExtract(String column, String keyExpression) {
        return "JSON_EXTRACT(" + column + ",'" + keyExpression + "')";
    }

    public static String jsonInsert(String column, String keyExpression, String value) {
        return "JSON_INSERT(" + column + ",'" + keyExpression + "','" + value + "')";
    }

    public static String jsonReplace(String column, String keyExpression, String value) {
        return "JSON_REPLACE(" + column + ",'" + keyExpression + "','" + value + "')";
    }

    public static String jsonRemove(String column, String keyExpression) {
        return "JSON_REMOVE(" + column + ",'" + keyExpression + ")";
    }

    public static String jsonSet(String column, String keyExpression, String value) {
        return "JSON_SET(" + column + ",'" + keyExpression + "','" + value + "')";
    }

    public static String jsonArrayAppend(String column, String keyExpression, String value) {
        return "JSON_ARRAY_APPEND(" + column + ",'" + keyExpression + "','" + value + "')";
    }

    public static String jsonArrayInsert(String column, String keyExpression, String value) {
        return "JSON_ARRAY_INSERT(" + column + ",'" + keyExpression + "','" + value + "')";
    }

}
