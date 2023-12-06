package io.github.dengchen2020.mybatis.extension.help;

import java.util.Collections;
import java.util.List;

/**
 * 分页
 * @author dengchen
 */
public class Page {

    /**
     * 当前页码
     */
    private Integer page;

    /**
     * 每页显示数
     */
    private Integer size;

    /**
     * 总数据量
     */
    private Long count;

    /**
     * 数据
     */
    private List<?> data;

    public Page(final Integer page, final Integer size) {
        this.page = page;
        this.size = size;
    }

    public Integer getPage() {
        return page < 1 ? 1 : page;
    }

    public void setPage(final Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size < 0 ? 0 : size;
    }

    public void setSize(final Integer size) {
        this.size = size;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(final Long count) {
        this.count = count;
    }

    public Integer getTotalPage() {
        return size != 0 && count != 0 ? (int) Math.ceil((double) count / size) : 0;
    }

    public List<?> getData() {
        return data == null ? Collections.emptyList() : data;
    }

    public void setData(final List<?> data) {
        this.data = data;
    }

}
