package io.github.dengchen2020.mybatis.extension.constant;

/**
 * @author dengchen
 */
public class SQL {

    public static final String WHERE = " WHERE ";

    public static final String FROM = " FROM ";

    public static final String SPACE = " ";

    public static final String AS = " AS ";

    public static final String COMMA = ",";

    public static final String POINT = ".";

    public static final String OPEN = "(";

    public static final String CLOSE = ")";

    public static final String AND = "AND";

    public static final String OR = "OR";

    public static final String IN = " IN ";

    public static final String NOT_IN = " NOT IN ";

    public static final String EQ = " = ";

    public static final String NE = " <> ";

    public static final String GE = " > ";

    public static final String LT = " < ";

    public static final String GOE = " >= ";

    public static final String LOE = " <= ";

    public static final String LIKE = " LIKE ";

    public static final String NOT_LIKE = " NOT LIKE ";

    public static final String PERCENT = "%";

    public static final String IS_NULL = " IS NULL";

    public static final String IS_NOT_NULL = " IS NOT NULL";

    public static final String IS_TRUE = " IS TRUE";

    public static final String IS_FALSE = " IS FALSE";

    public static final String IS_UNKNOWN = " IS UNKNOWN";

    public static final String REGEXP = " REGEXP ";

    public static final String NOT_REGEXP = " NOT REGEXP ";

    public static final String LIMIT = " LIMIT ";

    public static final String SEMICOLON = ";";

    public static final String BACK_QUOTE = "`";

    public static final String ADD = " + ";

    public static final String SUBTRACT = " - ";

    public static final String SINGLE_QUOTE = "'";

    public static String ognlParam(String param) {
        return "#{" + param + "}";
    }

    public static String unsafeParam(String param) {
        return "${" + param + "}";
    }

    public static String forObjParam(Object i) {
        return "[" + i + "]";
    }

    public static String forListParam(Object i) {
        return "[" + i + "].";
    }

    public static String backQuote(String column) {
        return BACK_QUOTE + column + BACK_QUOTE;
    }

}
