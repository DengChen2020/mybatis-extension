package io.github.dengchen2020.mybatis.extension.help;

/**
 * @author dengchen
 */
public class Join {

    public Join(final String table, final String leftColumn, final String rightColumn) {
        this.table = table;
        this.leftColumn = leftColumn;
        this.rightColumn = rightColumn;
    }

    private final String table;

    private final String leftColumn;

    private final String rightColumn;

    public String getTable() {
        return table;
    }

    public String getLeftColumn() {
        return leftColumn;
    }

    public String getRightColumn() {
        return rightColumn;
    }
}
