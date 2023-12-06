package io.github.dengchen2020.mybatis.extension.help;

/**
 * @author dengchen
 */
public class OrderBy {

    public OrderBy(final String[] column, final String orderType) {
        this.column = column;
        this.orderType = orderType;
    }

    private final String[] column;

    private final String orderType;

    public String[] getColumn() {
        return column;
    }

    public String getOrderType() {
        return orderType;
    }
}
