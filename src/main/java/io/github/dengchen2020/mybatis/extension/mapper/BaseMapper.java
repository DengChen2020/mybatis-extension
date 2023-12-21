package io.github.dengchen2020.mybatis.extension.mapper;

import io.github.dengchen2020.mybatis.extension.core.DeleteWrapper;
import io.github.dengchen2020.mybatis.extension.core.QueryWrapper;
import io.github.dengchen2020.mybatis.extension.metainfo.TableInfo;
import org.apache.ibatis.cursor.Cursor;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.*;
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
     *
     * @apiNote 能触发@PostLoad回调
     */
    default Optional<T> findById(Serializable id) {
        if (Objects.isNull(id)) throw new IllegalArgumentException("id不能为null");
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
        if (Objects.isNull(id)) throw new IllegalArgumentException("id不能为null");
        return exists(QueryWrapper.create().exists().eq(getTableInfo().getIdColumn(), id));
    }

    default List<T> findAll() {
        return selectList();
    }

    default List<T> findAllById(List<?> ids) {
        if (Objects.isNull(ids) || ids.contains(null)) throw new IllegalArgumentException("ids不能为null或包含null");
        return selectList(QueryWrapper.create().in(getTableInfo().getIdColumn(), ids));
    }

    /**
     * 更新，忽略null值
     *
     * @param entity 实体
     * @return 影响条数
     * @apiNote 能触发@PreUpdate和@PostUpdate回调，支持乐观锁
     */
    default int update(T entity) {
        return update(entity, true);
    }

    /**
     * 更新
     *
     * @param entity     实体
     * @param ignoreNull 是否忽略null
     * @return 1-成功
     * @apiNote 能触发@PreUpdate和@PostUpdate回调，支持乐观锁
     */
    default int update(T entity, boolean ignoreNull) {
        if (Objects.isNull(entity)) throw new IllegalArgumentException("entity不能为null");
        TableInfo tableInfo = getTableInfo();
        if (tableInfo.getPreUpdate() != null) {
            try {
                tableInfo.getPreUpdate().invoke(entity);
            } catch (Exception e) {
                log.error("@PreUpdate回调失败：", e);
            }
        }
        int result = updateOne(entity, ignoreNull);
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
     * @return 大于0-成功
     * @apiNote 忽略null值，不触发乐观锁，url需加allowMultiQueries=true
     */
    default long updateBatch(List<T> list, Integer size) {
        return updateBatch(list, size, true);
    }

    /**
     * 批量更新
     *
     * @param list 数据集合
     * @param size 单批次数量
     * @param ignoreNull 是否忽略null值
     * @return 大于0-成功
     * @apiNote 不触发乐观锁，url需加allowMultiQueries=true
     */
    default long updateBatch(List<T> list, Integer size, boolean ignoreNull) {
        if (Objects.isNull(list) || list.contains(null)) throw new IllegalArgumentException("list不能为null或包含null");
        if (list.isEmpty()) return 0;
        TableInfo tableInfo = getTableInfo();
        Method preUpdate = tableInfo.getPreUpdate();
        Method postUpdate = tableInfo.getPostUpdate();
        long result = 0;
        for (int i = 0; i < (int) (Math.ceil((double) list.size() / size)); i++) {
            List<T> updateList = list.stream().skip((long) i * size).limit(size).collect(Collectors.toList());
            if (preUpdate != null) {
                updateList.forEach(entity -> {
                    try {
                        preUpdate.invoke(entity);
                    } catch (Exception e) {
                        log.error("@PreUpdate回调失败：", e);
                    }
                });
            }
            if (updateBatch(updateList, ignoreNull) > 0) {
                result += updateList.size();
            }
            if (postUpdate != null) {
                updateList.forEach(entity -> {
                    try {
                        postUpdate.invoke(entity);
                    } catch (Exception e) {
                        log.error("@PostUpdate回调失败：", e);
                    }
                });
            }
        }
        return result;
    }

    default long delete(List<?> ids) {
        if (Objects.isNull(ids) || ids.contains(null)) throw new IllegalArgumentException("ids不能为null或包含null");
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
     *
     * @return 受影响的行数
     * @apiNote 能触发@PreRemove和@PostRemove回调
     */
    default int deleteById(Serializable id) {
        if (Objects.isNull(id)) throw new IllegalArgumentException("id不能为null");
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
        int result = Math.toIntExact(delete(DeleteWrapper.create().eq(getTableInfo().getIdColumn(), id)));
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
     *
     * @apiNote 能触发@PrePersist和@PostPersist回调。如使用自增主键策略时需要返回自增id，需重写insertOne，添加@Options(useGeneratedKeys = true,keyProperty = "id")
     * @see Mapper#insertOne(Object)
     */
    default int insert(T entity) {
        if (Objects.isNull(entity)) throw new IllegalArgumentException("entity不能为null");
        TableInfo tableInfo = getTableInfo();
        if (tableInfo.getPrePersist() != null) {
            try {
                tableInfo.getPrePersist().invoke(entity);
            } catch (Exception e) {
                log.error("@PrePersist回调失败：", e);
            }
        }
        int result = insertOne(entity);
        if (tableInfo.getPostPersist() != null) {
            try {
                tableInfo.getPostPersist().invoke(entity);
            } catch (Exception e) {
                log.error("@PostPersist回调失败：", e);
            }
        }
        return result;
    }

    /**
     * 批量新增
     *
     * @param list 数据集合
     * @param size 每批数量
     * @return 大于0-成功
     * @apiNote 能触发@PrePersist和@PostPersist回调。如使用自增主键策略时需要返回自增id，需重写insertBatch，添加@Options(useGeneratedKeys = true,keyProperty = "id")
     * @see Mapper#insertBatch(List)
     */
    default long insertBatch(List<T> list, Integer size) {
        if (Objects.isNull(list) || list.contains(null)) throw new IllegalArgumentException("list不能为null或包含null");
        if (list.isEmpty()) return 0;
        long result = 0;
        TableInfo tableInfo = getTableInfo();
        Method prePersist = tableInfo.getPrePersist();
        Method postPersist = tableInfo.getPostPersist();
        for (int i = 0; i < (Math.ceil((double) list.size() / size)); i++) {
            List<T> insertList = list.stream().skip((long) i * size).limit(size).collect(Collectors.toList());
            if (prePersist != null) {
                insertList.forEach(entity -> {
                    try {
                        prePersist.invoke(entity);
                    } catch (Exception e) {
                        log.error("@PrePersist回调失败：", e);
                    }
                });
            }
            if (insertBatch(insertList) > 0) {
                result += insertList.size();
            }
            if (postPersist != null) {
                insertList.forEach(entity -> {
                    try {
                        postPersist.invoke(entity);
                    } catch (Exception e) {
                        log.error("@PostPersist回调失败：", e);
                    }
                });
            }
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
