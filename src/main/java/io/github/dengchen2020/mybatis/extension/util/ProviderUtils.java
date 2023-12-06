package io.github.dengchen2020.mybatis.extension.util;

import io.github.dengchen2020.mybatis.extension.annotation.AS;
import io.github.dengchen2020.mybatis.extension.constant.Callback;
import io.github.dengchen2020.mybatis.extension.constant.SQL;
import io.github.dengchen2020.mybatis.extension.metainfo.TableField;
import io.github.dengchen2020.mybatis.extension.metainfo.TableInfo;
import jakarta.persistence.*;

import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author dengchen
 */
public class ProviderUtils {

    private static final ConcurrentMap<Class<?>, TableInfo> entityTableInfoMap = new ConcurrentHashMap<>();

    private static final ConcurrentMap<Class<?>, TableInfo> mapperTableInfoMap = new ConcurrentHashMap<>();

    private static final ConcurrentMap<String, TableInfo> tableInfoMap = new ConcurrentHashMap<>();

    /**
     * 获取表信息
     *
     * @param entity 实体类
     * @return 表信息
     */
    public static TableInfo getTableInfo(Class<?> entity) {
        TableInfo value = entityTableInfoMap.get(entity);
        if (value != null) return value;
        return entityTableInfoMap.computeIfAbsent(entity, (key) -> {
            Table table = key.getAnnotation(Table.class);
            String tableName;
            if (table != null) {
                tableName = table.name();
            } else {
                tableName = key.getSimpleName().toLowerCase();
            }
            String idField = null;
            String idColumn = null;
            Set<String> columns = new HashSet<>();
            final Map<String, String> columnCacheMap = new HashMap<>();
            final Map<String, String> fieldCacheMap = new HashMap<>();
            Set<String> updateColumns = new HashSet<>();
            List<Field> fields = new ArrayList<>();
            Class<?> entitySuperClass = key.getSuperclass();
            if (entitySuperClass != null && entitySuperClass.getAnnotation(MappedSuperclass.class) != null) {
                fields.addAll(new ArrayList<>(Arrays.asList(entitySuperClass.getDeclaredFields())));
            }
            fields.addAll(new ArrayList<>(Arrays.asList(key.getDeclaredFields())));
            TableField versionField = null;
            for (final Field field : fields) {
                if (shouldBeIgnoredField(field)) {
                    continue;
                }
                String fieldName = field.getName();
                String column = getColumnName(field);
                AS as = field.getAnnotation(AS.class);
                if (as != null) {
                    columns.add(column + SQL.AS + (!as.value().isEmpty() ? as.value() : fieldName));
                } else {
                    columns.add(column);
                }
                columnCacheMap.put(fieldName, column);
                fieldCacheMap.put(column, fieldName);
                if (hasIdAnnotation(field)) {
                    idField = field.getName();
                    idColumn = getId(field);
                } else {
                    updateColumns.add(column);
                }
                if (hasVersionAnnotation(field)) {
                    versionField = new TableField(column, field);
                }
            }
            Map<String, Method> callbackMethodMap = new HashMap<>();
            List<Method> methods = new ArrayList<>();
            if (entitySuperClass != null) {
                methods.addAll(new ArrayList<>(Arrays.asList(entitySuperClass.getDeclaredMethods())));
            }
            methods.addAll(new ArrayList<>(Arrays.asList(key.getDeclaredMethods())));
            for (Method method : methods) {
                saveCallbackMethod(method, callbackMethodMap);
            }
            TableInfo tableInfo = new TableInfo(columnCacheMap, fieldCacheMap, tableName, idField, idColumn, new ArrayList<>(columns), new ArrayList<>(updateColumns), callbackMethodMap, versionField);
            tableInfoMap.put(tableName, tableInfo);
            return tableInfo;
        });
    }

    /**
     * 获取表信息
     *
     * @param tableName 表名
     * @return 表信息
     */
    public static TableInfo getTableInfo(String tableName) {
        return tableInfoMap.get(tableName);
    }

    /**
     * 获取字段名
     *
     * @param field 字段
     * @return 字段名
     */
    public static String getColumnName(Field field) {
        Column column = field.getAnnotation(Column.class);
        if (column == null || column.name().isEmpty()) {
            return convertColumnName(field.getName());
        }
        return column.name();
    }

    /**
     * 获取id字段
     *
     * @param field 字段
     * @return 字段名
     */
    private static boolean hasIdAnnotation(Field field) {
        return field.getAnnotation(Id.class) != null;
    }

    /**
     * 获取版本字段
     *
     * @param field 字段
     * @return 字段名
     */
    private static boolean hasVersionAnnotation(Field field) {
        return field.getAnnotation(Version.class) != null;
    }

    /**
     * 获取id字段
     *
     * @param field 字段
     * @return 字段名
     */
    private static String getId(Field field) {
        return getColumnName(field);
    }

    /**
     * 是否应该忽略
     *
     * @param field 字段
     * @return true：忽略，false：不忽略
     */
    public static boolean shouldBeIgnoredField(Field field) {
        if (Modifier.isStatic(field.getModifiers()) || Modifier.isFinal(field.getModifiers())) {
            return true;
        }
        return field.getAnnotation(Transient.class) != null;
    }

    /**
     * 转换成表字段名，驼峰转下划线
     *
     * @param fieldName 实体字段名
     * @return 表字段名
     */
    public static String convertColumnName(String fieldName) {
        StringBuilder result = new StringBuilder();
        boolean isFirst = true;
        for (int i = 0; i < fieldName.length(); i++) {
            char currentChar = fieldName.charAt(i);
            if (Character.isUpperCase(currentChar)) {
                if (!isFirst) {
                    result.append("_");
                }
                result.append(Character.toLowerCase(currentChar));
            } else {
                result.append(currentChar);
            }
            isFirst = false;
        }
        return result.toString();
    }

    /**
     * 下划线转驼峰
     *
     * @param columnName 表字段名
     * @return 驼峰命名
     */
    public static String convertToCamelCase(String columnName) {
        StringBuilder result = new StringBuilder();
        boolean toUpperCase = false;
        for (int i = 0; i < columnName.length(); i++) {
            char currentChar = columnName.charAt(i);
            if (currentChar == '_') {
                toUpperCase = true;
            } else {
                if (toUpperCase) {
                    result.append(Character.toUpperCase(currentChar));
                    toUpperCase = false;
                } else {
                    result.append(currentChar);
                }
            }
        }
        return result.toString();
    }

    public static TableInfo getTableInfoByMapper(Class<?> mapperClass) {
        TableInfo value = mapperTableInfoMap.get(mapperClass);
        if (value != null) return value;
        return mapperTableInfoMap.computeIfAbsent(mapperClass, key -> {
            Class<?> entityClass = getEntityClass(mapperClass);
            return entityClass == null ? null : getTableInfo(entityClass);
        });
    }

    public static Class<?> getEntityClass(Class<?> mapperClass) {
        if (mapperClass != null && mapperClass != Object.class) {
            Type[] genericInterfaces = mapperClass.getGenericInterfaces();
            if (genericInterfaces.length == 1) {
                Type type = genericInterfaces[0];
                if (type instanceof ParameterizedType) {
                    Type actualTypeArgument = ((ParameterizedType) type).getActualTypeArguments()[0];
                    return actualTypeArgument instanceof Class ? (Class<?>) actualTypeArgument : null;
                }
                if (type instanceof Class) {
                    return getEntityClass((Class<?>) type);
                }
            }
            return getEntityClass(mapperClass.getSuperclass());
        } else {
            return null;
        }
    }

    /**
     * 保存回调函数
     */
    private static void saveCallbackMethod(Method method, Map<String, Method> callbackMethodMap) {
        if (callbackMethodMap.get(Callback.PRE_PERSIST) == null && method.getAnnotation(PrePersist.class) != null) {
            callbackMethodMap.put(Callback.PRE_PERSIST, method);
        }
        if (callbackMethodMap.get(Callback.POST_PERSIST) == null && method.getAnnotation(PostPersist.class) != null) {
            callbackMethodMap.put(Callback.POST_PERSIST, method);
        }
        if (callbackMethodMap.get(Callback.PRE_UPDATE) == null && method.getAnnotation(PreUpdate.class) != null) {
            callbackMethodMap.put(Callback.PRE_UPDATE, method);
        }
        if (callbackMethodMap.get(Callback.POST_UPDATE) == null && method.getAnnotation(PostUpdate.class) != null) {
            callbackMethodMap.put(Callback.POST_UPDATE, method);
        }
        if (callbackMethodMap.get(Callback.PRE_REMOVE) == null && method.getAnnotation(PreRemove.class) != null) {
            callbackMethodMap.put(Callback.PRE_REMOVE, method);
        }
        if (callbackMethodMap.get(Callback.POST_REMOVE) == null && method.getAnnotation(PostRemove.class) != null) {
            callbackMethodMap.put(Callback.POST_REMOVE, method);
        }
        if (callbackMethodMap.get(Callback.POST_LOAD) == null && method.getAnnotation(PostLoad.class) != null) {
            callbackMethodMap.put(Callback.POST_LOAD, method);
        }
    }

}
