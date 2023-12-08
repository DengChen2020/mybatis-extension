package io.github.dengchen2020.mybatis.extension.help;

/**
 * @author dengchen
 */
public class Aggregation {

    public static String sum(String column) {
        return "SUM(" + column + ")";
    }

    public static String avg(String column) {
        return "AVG(" + column + ")";
    }

    public static  String count(String column) {
        return "COUNT(" + column + ")";
    }

    public static  String max(String column) {
        return "MAX(" + column + ")";
    }

    public static  String min(String column) {
        return "MIN(" + column + ")";
    }

    public static  String ifnull(String column, String defaultValue) {
        return "IFNULL(" + column + ",'" + defaultValue + "')";
    }

    public static  String coalesce(String column) {
        return "COALESCE(" + column + ")";
    }

    public static  String groupConcat(String column) {
        return "GROUP_CONCAT(" + column + ")";
    }

    public static  String groupConcat(String column, String separator) {
        return "GROUP_CONCAT(" + column + ",'" + separator + "')";
    }

    public static  String stddevPop(String column) {
        return "STDDEV_POP(" + column + ")";
    }

    public static  String stddevSamp(String column) {
        return "STDDEV_SAMP(" + column + ")";
    }

    public static  String varPop(String column) {
        return "VAR_POP(" + column + ")";
    }

    public static  String varSamp(String column) {
        return "VAR_SAMP(" + column + ")";
    }

    public static  String jsonObjectAgg(String keyColumn, String valueColumn) {
        return "JSON_OBJECTAGG(" + keyColumn + "," + valueColumn + ")";
    }

    public static  String bitAnd(String column) {
        return "BIT_AND(" + column + ")";
    }

    public static  String bitOr(String column) {
        return "BIT_OR(" + column + ")";
    }

    public static  String bitXor(String column) {
        return "BIT_XOR(" + column + ")";
    }


}
