package io.github.dengchen2020.mybatis.extension.mapper;

import io.github.dengchen2020.mybatis.extension.core.QueryWrapper;
import io.github.dengchen2020.mybatis.extension.util.ProviderUtils;
import org.apache.ibatis.reflection.MetaObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * 通用CRUD的扩展
 *
 * @author dengchen
 */
public interface DcMapper<T> extends BaseMapper<T> {

    /**
     * 根据id查询
     *
     * @apiNote 悲观锁，需开启事务
     */
    default T selectByIdForUpdate(Serializable id) {
        if (Objects.isNull(id)) throw new IllegalArgumentException("id不能为null");
        return selectOne(QueryWrapper.create().eq(getTableInfo().getIdColumn(), id).forUpdate());
    }

    /**
     * 根据id查询
     *
     * @apiNote 悲观锁，需开启事务
     */
    default Optional<T> findByIdForUpdate(Serializable id) {
        return Optional.ofNullable(selectByIdForUpdate(id));
    }

    /**
     * 新增或修改
     *
     * @apiNote id为null时新增，否则修改
     */
    default long save(T entity) {
        MetaObject metaObject = ProviderUtils.getMetaObject(entity);
        if (Objects.isNull(metaObject.getValue(getTableInfo().getIdField()))) {
            return insert(entity);
        }
        return update(entity);
    }

    /**
     * 批量新增或修改
     *
     * @apiNote 应在事务中执行，id为null时新增，否则修改
     */
    default long saveAll(List<T> list) {
        List<T> insertList = new ArrayList<>();
        List<T> updateList = new ArrayList<>();
        for (T entity : list) {
            MetaObject metaObject = ProviderUtils.getMetaObject(entity);
            if (Objects.isNull(metaObject.getValue(getTableInfo().getIdField()))) {
                insertList.add(entity);
            } else {
                updateList.add(entity);
            }
        }
        return updateBatch(updateList, 1000) + insertBatch(insertList, 1000);
    }

}
