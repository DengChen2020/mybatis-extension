package io.github.dengchen2020.mybatis.extension.help;

import static io.github.dengchen2020.mybatis.extension.constant.SQL.AS;

/**
 * 别名
 * @author dengchen
 */
public class As {

    public static String as(String column, String as){
        return column + AS + as;
    }

}
