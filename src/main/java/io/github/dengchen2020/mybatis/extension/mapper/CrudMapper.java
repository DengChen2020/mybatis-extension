package io.github.dengchen2020.mybatis.extension.mapper;

import io.github.dengchen2020.mybatis.extension.constant.Params;
import io.github.dengchen2020.mybatis.extension.core.CrudProvider;
import io.github.dengchen2020.mybatis.extension.core.DeleteWrapper;
import io.github.dengchen2020.mybatis.extension.core.QueryWrapper;
import io.github.dengchen2020.mybatis.extension.core.UpdateWrapper;
import io.github.dengchen2020.mybatis.extension.help.Page;
import io.github.dengchen2020.mybatis.extension.util.ConvertUtils;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.cursor.Cursor;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 提供通用的CRUD接口
 * @author dengchen
 */
public interface CrudMapper<T> extends Mapper<T> {

    @UpdateProvider(value = CrudProvider.class, method = "update")
    int update(@Param(Params.WRAPPER) UpdateWrapper builder);

    @DeleteProvider(value = CrudProvider.class, method = "delete")
    int delete(@Param(Params.WRAPPER) DeleteWrapper builder);

    @SelectProvider(value = CrudProvider.class, method = "count")
    long count(@Param(Params.WRAPPER) QueryWrapper wrapper);

    @SelectProvider(value = CrudProvider.class, method = "selectList")
    List<T> selectList(@Param(Params.WRAPPER) QueryWrapper wrapper);

    @SelectProvider(value = CrudProvider.class, method = "selectList")
    List<Map<String, Object>> selectObjectList(@Param(Params.WRAPPER) QueryWrapper wrapper);

    @SelectProvider(value = CrudProvider.class, method = "selectOne")
    T selectOne(@Param(Params.WRAPPER) QueryWrapper wrapper);

    @SelectProvider(value = CrudProvider.class, method = "selectById")
    T selectById(@Param(Params.ID) Serializable id);

    @SelectProvider(value = CrudProvider.class, method = "selectOne")
    Map<String, Object> selectObject(@Param(Params.WRAPPER) QueryWrapper wrapper);

    @SelectProvider(value = CrudProvider.class, method = "exists")
    boolean exists(@Param(Params.WRAPPER) QueryWrapper wrapper);

    @SelectProvider(value = CrudProvider.class, method = "selectList")
    Cursor<T> selectCursor(@Param(Params.WRAPPER) QueryWrapper wrapper);

    @SuppressWarnings("unchecked")
    default <R> List<R> selectList(QueryWrapper wrapper, Class<R> entityClass) {
        return (List<R>) ConvertUtils.convertList(selectObjectList(wrapper), entityClass);
    }

    @SuppressWarnings("unchecked")
    default <R> R selectOne(QueryWrapper wrapper, Class<R> entityClass) {
        Map<String, Object> result = selectObject(wrapper);
        if (result == null) return null;
        return (R) ConvertUtils.convertObject(result, entityClass);
    }

    default Page selectPage(QueryWrapper wrapper, Page page, Class<?> dataType) {
        if(page.isQueryCount()){
            page.setCount(count(wrapper.count()));
        }
        if ((page.getCount() == null || page.getCount() > 0) && page.getSize() > 0) {
            if (wrapper.getColumns() != null && !wrapper.getColumns().isEmpty()) {
                wrapper.select(wrapper.getColumns().toArray(new String[0]));
            } else {
                wrapper.select();
            }
            wrapper.limit((page.getPage() - 1) * page.getSize(), page.getSize());
            if (dataType != null) {
                page.setData(selectList(wrapper, dataType));
            } else {
                page.setData(selectList(wrapper));
            }
        }
        return page;
    }

    default Page selectPage(QueryWrapper wrapper, Page page) {
        return selectPage(wrapper, page, null);
    }


}
