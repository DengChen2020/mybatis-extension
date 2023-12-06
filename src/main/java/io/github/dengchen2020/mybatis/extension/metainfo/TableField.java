package io.github.dengchen2020.mybatis.extension.metainfo;

import java.lang.reflect.Field;

public class TableField {

    /**
     * 表字段名
     */
    private final String column;

    /**
     * java字段
     */
    private final Field javaField;

    public TableField(final String column, final Field javaField) {
        this.column = column;
        this.javaField = javaField;
    }

    public String getColumn() {
        return column;
    }

    public Field getJavaField() {
        return javaField;
    }
}
