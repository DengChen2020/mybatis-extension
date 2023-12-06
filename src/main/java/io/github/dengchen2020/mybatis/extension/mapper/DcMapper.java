package io.github.dengchen2020.mybatis.extension.mapper;

import io.github.dengchen2020.mybatis.extension.core.QueryWrapper;

import java.io.Serializable;
import java.util.Optional;

/**
 * 通用CRUD的扩展
 *
 * @author dengchen
 */
public interface DcMapper<T> extends BaseMapper<T> {

    default T selectByIdForUpdate(Serializable id){
        return selectOne(QueryWrapper.create().eq(getTableInfo().getIdColumn(),id).forUpdate());
    }

    default Optional<T> findByIdForUpdate(Serializable id) {
        return Optional.ofNullable(selectByIdForUpdate(id));
    }

}
