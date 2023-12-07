package io.github.dengchen2020.mybatis.extension.core;

import io.github.dengchen2020.mybatis.extension.constant.Params;
import io.github.dengchen2020.mybatis.extension.exception.MybatisCustomException;
import io.github.dengchen2020.mybatis.extension.metainfo.TableInfo;
import io.github.dengchen2020.mybatis.extension.util.DateUtils;
import org.apache.ibatis.builder.annotation.ProviderContext;
import org.apache.ibatis.reflection.MetaObject;

import java.time.LocalDateTime;
import java.util.*;

import static io.github.dengchen2020.mybatis.extension.constant.SQL.EQ;
import static io.github.dengchen2020.mybatis.extension.constant.SQL.SINGLE_QUOTE;
import static io.github.dengchen2020.mybatis.extension.core.Provider.*;

/**
 * 执行通用CRUD操作的sql生成
 *
 * @author dengchen
 */
public class CrudProvider {

    CrudProvider() {
    }

    public static String insertOne(ProviderContext context) {
        TableInfo tableInfo = getTableInfo(context);
        return InsertSqlBuilder.builder().insert(tableInfo.getTableName())
                .column(tableInfo.getAllColumn()).toString();
    }

    public static String insertBatch(ProviderContext context, Map<String, Object> params) {
        TableInfo tableInfo = getTableInfo(context);
        return BatchInsertSqlBuilder.builder().insert(tableInfo.getTableName())
                .column(tableInfo.getAllColumn(), ((List<?>) params.get(Params.LIST)).size()).toString();
    }

    public static String update(ProviderContext context, Map<String, Object> params) {
        TableInfo tableInfo = getTableInfo(context);
        return getUpdateSqlBuilder(params).update(tableInfo.getTableName()).toString();
    }

    public static String updateOne(ProviderContext context, Map<String, Object> params) {
        TableInfo tableInfo = getTableInfo(context);
        Object entity = params.get(Params.ENTITY);
        UpdateSqlBuilder updateSqlBuilder = getUpdateSqlBuilder(params);
        boolean ignoreNull = (boolean) params.get(Params.IGNORE_NULL);
        List<String> updateColumns = tableInfo.getUpdateColumns();
        MetaObject metaObject = getMetaObject(entity);
        Object id;
        if (metaObject.hasGetter(tableInfo.getIdField())) {
            id = metaObject.getValue(tableInfo.getIdField());
            updateSqlBuilder.eq(tableInfo.getIdField(), id);
        } else {
            throw new MybatisCustomException("更新必须设置id");
        }
        String version = null;
        if (tableInfo.getVersionField() != null) {
            version = tableInfo.getVersionField().getColumn();
        }
        List<String> columns = new ArrayList<>(updateColumns);
        for (Iterator<String> iterator = columns.iterator(); iterator.hasNext(); ) {
            String column = iterator.next();
            String fieldName = tableInfo.getField(column);
            Object value = null;
            if (metaObject.hasGetter(fieldName)) {
                value = metaObject.getValue(fieldName);
                //乐观锁
                if (column.equals(version) && !Objects.isNull(value)) {
                    if (value instanceof Long) {
                        Long num = (Long) value;
                        updateSqlBuilder.addCondition(version + EQ + num);
                        metaObject.setValue(fieldName, num + 1);
                    } else if (value instanceof Integer) {
                        Integer num = (Integer) value;
                        updateSqlBuilder.addCondition(version + EQ + num);
                        metaObject.setValue(fieldName, num + 1);
                    } else if (value instanceof Date) {
                        updateSqlBuilder.addCondition(version + EQ + SINGLE_QUOTE + DateUtils.parse((Date) value) + SINGLE_QUOTE);
                        metaObject.setValue(fieldName, new Date());
                    } else if (value instanceof LocalDateTime) {
                        updateSqlBuilder.addCondition(version + EQ + SINGLE_QUOTE + DateUtils.parse((LocalDateTime) value) + SINGLE_QUOTE);
                        metaObject.setValue(fieldName, LocalDateTime.now());
                    }
                }
            }
            if (ignoreNull && Objects.isNull(value)) {
                iterator.remove();
            }
        }
        return updateSqlBuilder.update(tableInfo.getTableName())
                .set(updateColumns).toString();
    }

    public static String updateBatch(ProviderContext context, Map<String, Object> params) {
        TableInfo tableInfo = getTableInfo(context);
        return BatchUpdateSqlUpdate.builder().update(tableInfo.getTableName()).set(tableInfo.getUpdateColumns(), ((List<?>) params.get(Params.LIST)).size()).toString();
    }

    public static String delete(ProviderContext context, Map<String, Object> params) {
        return getDeleteSqlBuilder(params).delete(getTableInfo(context).getTableName()).toString();
    }

    public static String selectOne(ProviderContext context, Map<String, Object> params) {
        return getSelectSqlBuilder(params).from(getTableInfo(context).getTableName()).limit(1).toString();
    }

    public static String selectList(ProviderContext context, Map<String, Object> params) {
        return getSelectSqlBuilder(params).from(getTableInfo(context).getTableName()).toString();
    }

    public static String selectById(ProviderContext context, Map<String, Object> params) {
        TableInfo tableInfo = getTableInfo(context);
        return getSelectSqlBuilder(params).from(tableInfo.getTableName()).eq(tableInfo.getIdColumn(), params.get(Params.ID)).toString();
    }

    public static String exists(ProviderContext context, Map<String, Object> params) {
        return getSelectSqlBuilder(params).from(getTableInfo(context).getTableName()).toString();
    }

    public static String count(ProviderContext context, Map<String, Object> params) {
        return getSelectSqlBuilder(params).from(getTableInfo(context).getTableName()).toString();
    }

}
