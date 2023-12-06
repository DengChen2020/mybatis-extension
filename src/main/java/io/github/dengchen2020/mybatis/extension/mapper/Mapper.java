package io.github.dengchen2020.mybatis.extension.mapper;

import io.github.dengchen2020.mybatis.extension.constant.Params;
import io.github.dengchen2020.mybatis.extension.core.CrudProvider;
import io.github.dengchen2020.mybatis.extension.metainfo.TableInfo;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.UpdateProvider;

import java.util.List;

import static io.github.dengchen2020.mybatis.extension.util.ProviderUtils.getTableInfoByMapper;

/**
 * Mapper基层
 *
 * @author dengchen
 */
public interface Mapper<T> {

    default TableInfo getTableInfo() {
        return getTableInfoByMapper(this.getClass().getInterfaces()[0]);
    }

    @InsertProvider(value = CrudProvider.class, method = "insertOne")
    int insertOne(@Param(Params.LIST) T entity);

    @InsertProvider(value = CrudProvider.class, method = "insertBatch")
    long insertBatch(@Param(Params.LIST) List<T> list);

    @UpdateProvider(value = CrudProvider.class, method = "updateOne")
    int updateOne(@Param(Params.ENTITY) T entity, @Param(Params.IGNORE_NULL) boolean ignoreNull);

    @UpdateProvider(value = CrudProvider.class, method = "updateBatch")
    int updateBatch(@Param(Params.LIST) List<T> list);

}
