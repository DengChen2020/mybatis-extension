package io.github.dengchen2020.mybatis.extension.metainfo;

import io.github.dengchen2020.mybatis.extension.constant.Callback;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * 表信息
 *
 * @author dengchen
 */
public class TableInfo {

    public TableInfo(final Map<String, String> columnCacheMap, final Map<String, String> fieldCacheMap, final String tableName, final String idField, final String idColumn, final List<String> allColumn, final List<String> updateColumns, Map<String,Method> callbackMethodMap, final TableField versionField) {
        this.columnCacheMap = columnCacheMap;
        this.fieldCacheMap = fieldCacheMap;
        this.tableName = tableName;
        this.idField = idField;
        this.idColumn = idColumn;
        this.allColumn = allColumn;
        this.updateColumns = updateColumns;
        this.callbackMethodMap = callbackMethodMap;
        this.versionField = versionField;
    }

    private final Map<String, String> columnCacheMap;

    private final Map<String, String> fieldCacheMap;

    /**
     * 表名
     */
    private final String tableName;

    /**
     * 主键字段
     */
    private final String idField;

    /**
     * 主键映射列
     */
    private final String idColumn;

    /**
     * 所有字段
     */
    private final List<String> allColumn;

    /**
     * 需要更新的字段
     */
    private final List<String> updateColumns;

    public String getColumn(String field) {
        return columnCacheMap.get(field);
    }

    public String getField(String column) {
        return fieldCacheMap.get(column);
    }

    /**
     * 回调方法
     */
    private final Map<String,Method> callbackMethodMap;

    /**
     * 版本字段
     */
    private final TableField versionField;

    private Map<String, String> getColumnCacheMap() {
        return columnCacheMap;
    }

    private Map<String, String> getFieldCacheMap() {
        return fieldCacheMap;
    }

    public String getTableName() {
        return tableName;
    }

    public String getIdField() {
        return idField;
    }

    public String getIdColumn() {
        return idColumn;
    }

    public List<String> getAllColumn() {
        return allColumn;
    }

    public List<String> getUpdateColumns() {
        return updateColumns;
    }

    public Method getPrePersist() {
        return callbackMethodMap.get(Callback.PRE_PERSIST);
    }

    public Method getPostPersist() {
        return callbackMethodMap.get(Callback.POST_PERSIST);
    }

    public Method getPreUpdate() {
        return callbackMethodMap.get(Callback.PRE_UPDATE);
    }

    public Method getPostUpdate() {
        return callbackMethodMap.get(Callback.POST_UPDATE);
    }

    public Method getPreRemove() {
        return callbackMethodMap.get(Callback.PRE_REMOVE);
    }

    public Method getPostRemove() {
        return callbackMethodMap.get(Callback.POST_REMOVE);
    }

    public Method getPostLoad(){return callbackMethodMap.get(Callback.POST_LOAD);}

    public TableField getVersionField() {
        return versionField;
    }

}
