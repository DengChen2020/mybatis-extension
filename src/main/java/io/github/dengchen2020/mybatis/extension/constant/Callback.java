package io.github.dengchen2020.mybatis.extension.constant;

/**
 * 回调函数
 * @author dengchen
 */
public class Callback {

    /**
     * 新增前执行
     */
    public static final String PRE_PERSIST = "prePersist";

    /**
     * 新增后执行
     */
    public static final String POST_PERSIST = "postPersist";

    /**
     * 更新前执行
     */
    public static final String PRE_UPDATE = "preUpdate";

    /**
     * 更新后执行
     */
    public static final String POST_UPDATE = "postUpdate";

    /**
     * 删除前执行
     */
    public static final String PRE_REMOVE = "preRemove";

    /**
     * 删除后执行
     */
    public static final String POST_REMOVE = "postRemove";

    /**
     * 检索到数据后执行
     */
    public static final String POST_LOAD = "postLoad";

}
