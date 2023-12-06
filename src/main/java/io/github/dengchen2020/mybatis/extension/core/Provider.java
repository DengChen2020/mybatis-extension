package io.github.dengchen2020.mybatis.extension.core;

import io.github.dengchen2020.mybatis.extension.constant.Params;
import io.github.dengchen2020.mybatis.extension.metainfo.TableInfo;
import org.apache.ibatis.builder.annotation.ProviderContext;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.ReflectorFactory;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.reflection.wrapper.DefaultObjectWrapperFactory;
import org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory;

import java.util.Map;

import static io.github.dengchen2020.mybatis.extension.util.ProviderUtils.getTableInfoByMapper;

/**
 * sql构建基层
 *
 * @author dengchen
 */
public class Provider {

    protected static TableInfo getTableInfo(ProviderContext context) {
        return getTableInfoByMapper(context.getMapperType());
    }

    static ObjectFactory objectFactory = new DefaultObjectFactory();

    static ObjectWrapperFactory objectWrapperFactory = new DefaultObjectWrapperFactory();

    static ReflectorFactory reflectorFactory = new DefaultReflectorFactory();

    public static MetaObject getMetaObject(Object entity) {
        return MetaObject.forObject(entity, objectFactory, objectWrapperFactory, reflectorFactory);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getSqlBuilder(Map<String, Object> params) {
        T builder;
        if (!params.containsKey(Params.WRAPPER)) {
            return null;
        } else {
            builder = (T) params.get(Params.WRAPPER);
        }
        if (builder instanceof QueryWrapper) {
            QueryWrapper wrapper = (QueryWrapper) builder;
            return (T) new SelectSqlBuilder(wrapper);
        }
        if (builder instanceof UpdateWrapper) {
            UpdateWrapper wrapper = (UpdateWrapper) builder;
            return (T) new UpdateSqlBuilder(wrapper);
        }
        if (builder instanceof DeleteWrapper) {
            DeleteWrapper wrapper = (DeleteWrapper) builder;
            return (T) new DeleteSqlBuilder(wrapper);
        }
        return builder;
    }

    public static SelectSqlBuilder getSelectSqlBuilder(Map<String, Object> params) {
        SelectSqlBuilder builder = getSqlBuilder(params);
        if (builder == null) {
            builder = SelectSqlBuilder.builder();
            params.put(Params.WRAPPER, builder);
            builder.getArgs().putAll(params);
        }
        return builder;
    }

    public static DeleteSqlBuilder getDeleteSqlBuilder(Map<String, Object> params) {
        DeleteSqlBuilder builder = getSqlBuilder(params);
        if (builder == null) {
            builder = DeleteSqlBuilder.builder();
            params.put(Params.WRAPPER, builder);
            builder.getArgs().putAll(params);
        }
        return builder;
    }

    public static UpdateSqlBuilder getUpdateSqlBuilder(Map<String, Object> params) {
        UpdateSqlBuilder builder = getSqlBuilder(params);
        if (builder == null) {
            builder = UpdateSqlBuilder.builder();
            params.put(Params.WRAPPER, builder);
            builder.getArgs().putAll(params);
        }
        return builder;
    }

}
