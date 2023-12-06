package io.github.dengchen2020.mybatis.extension.mapper;

import io.github.dengchen2020.mybatis.extension.core.DeleteWrapper;
import io.github.dengchen2020.mybatis.extension.core.QueryWrapper;
import io.github.dengchen2020.mybatis.extension.metainfo.TableInfo;
import org.apache.ibatis.cursor.Cursor;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 执行通用CRUD操作的接口
 *
 * @author dengchen
 */
public interface BaseMapper<T> extends CrudMapper<T> {

    Log log = LogFactory.getLog(BaseMapper.class);

    /**
     * 根据id查询
     * @apiNote 能触发@PostLoad回调
     */
    default Optional<T> findById(Serializable id) {
        long startTime = System.currentTimeMillis();
        T entity = selectById(id);
        long time = (System.currentTimeMillis() - startTime);
        Optional<T> optional = Optional.ofNullable(entity);
        if (optional.isPresent() && time > 1) {
            TableInfo tableInfo = getTableInfo();
            if (tableInfo.getPostLoad() != null) {
                try {
                    tableInfo.getPostLoad().invoke(optional.get());
                } catch (Exception e) {
                    log.error("@PostLoad回调失败：", e);
                }
            }
        }
        return optional;
    }

    default boolean existsById(Serializable id) {
        return exists(QueryWrapper.create().exists().eq(getTableInfo().getIdColumn(), id));
    }

    default List<T> findAll() {
        return selectList();
    }

    default List<T> findAllById(List<Serializable> list) {
        return selectList(QueryWrapper.create().in(getTableInfo().getIdColumn(), list));
    }

    /**
     * 更新，忽略null值
     *
     * @param entity 实体
     * @return 影响条数
     */
    default long update(T entity) {
        return update(entity, true);
    }

    /**
     * 更新
     * @apiNote 能触发@PreUpdate和@PostUpdate回调
     * @param entity     实体
     * @param ignoreNull 是否忽略null
     * @return 1-成功
     */
    default long update(T entity, boolean ignoreNull) {
        TableInfo tableInfo = getTableInfo();
        if (tableInfo.getPreUpdate() != null) {
            try {
                tableInfo.getPreUpdate().invoke(entity);
            } catch (Exception e) {
                log.error("@PreUpdate回调失败：", e);
            }
        }
        long result = updateOne(entity, ignoreNull);
        if (tableInfo.getPostUpdate() != null) {
            try {
                tableInfo.getPostUpdate().invoke(entity);
            } catch (Exception e) {
                log.error("@PostUpdate回调失败：", e);
            }
        }
        return result;
    }

    /**
     * 批量更新
     *
     * @param list 数据集合
     * @param size 单批次数量
     * @return 大于1-成功
     * @apiNote 不忽略null值，不触发乐观锁
     */
    default long updateBatch(List<T> list, Integer size) {
        long result = 0;
        for (int i = 0; i < (int) (Math.ceil((double) list.size() / size)); i++) {
            List<T> updateList = list.stream().skip((long) i * size).limit(size).collect(Collectors.toList());
            result += updateBatch(updateList);
        }
        return result;
    }

    default int delete(List<Serializable> ids) {
        if (ids.isEmpty()) {
            return 0;
        }
        return delete(DeleteWrapper.create().in(getTableInfo().getIdColumn(), ids));
    }

    default int delete(Serializable id) {
        return deleteById(id);
    }

    /**
     * 根据id删除
     * @apiNote 能触发@PreRemove和@PostRemove回调
     * @return 受影响的行数
     */
    default int deleteById(Serializable id) {
        if (Objects.isNull(id)) {
            return 0;
        }
        Object entity = null;
        TableInfo tableInfo = getTableInfo();
        if (tableInfo.getPreRemove() != null) {
            try {
                entity = selectById(id);
                tableInfo.getPreRemove().invoke(entity);
            } catch (Exception e) {
                log.error("@PreRemove回调失败：", e);
            }
        }
        int result = delete(DeleteWrapper.create().eq(getTableInfo().getIdColumn(), id));
        if (tableInfo.getPostRemove() != null) {
            try {
                if (entity == null) entity = selectById(id);
                tableInfo.getPostRemove().invoke(entity);
            } catch (Exception e) {
                log.error("@PostRemove回调失败：", e);
            }
        }
        return result;
    }

    /**
     * 新增
     * @apiNote 能触发@PrePersist和@PostPersist回调
     */
    default long insert(T entity) {
        TableInfo tableInfo = getTableInfo();
        if (tableInfo.getPrePersist() != null) {
            try {
                tableInfo.getPrePersist().invoke(entity);
            } catch (Exception e) {
                log.error("@PrePersist回调失败：", e);
            }
        }
        long result = insertOne(entity);
        if (tableInfo.getPostPersist() != null) {
            try {
                tableInfo.getPostPersist().invoke(entity);
            } catch (Exception e) {
                log.error("@PostPersist回调失败：", e);
            }
        }
        return result;
    }

    default long insertBatch(List<T> list, Integer size) {
        long result = 0;
        for (int i = 0; i < (Math.ceil((double) list.size() / size)); i++) {
            List<T> insertList = list.stream().skip((long) i * size).limit(size).collect(Collectors.toList());
            result += insertBatch(insertList);
        }
        return result;
    }

    default List<T> selectList() {
        return selectList(QueryWrapper.create());
    }

    default Cursor<T> selectCursor() {
        return selectCursor(QueryWrapper.create());
    }

}
