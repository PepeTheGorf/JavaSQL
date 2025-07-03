package Parser.Statement;

import Parser.Enums.StatementType;
import Parser.Expression.Expression;

import java.util.List;

public class InsertStatement extends Statement {
    
    private String tableName;
    private List<List<Expression>> rows;
    
    public InsertStatement(String tableName, List<List<Expression>> rows) {
        super(StatementType.INSERT);
        this.tableName = tableName;
        this.rows = rows;
    }

    public List<List<Expression>> getRows() {
        return rows;
    }

    public String getTableName() {
        return tableName;
    }

    @Override
    public String toString() {
        return "InsertStatement{" +
                "tableName='" + tableName + '\'' +
                ", rows=" + rows +
                '}';
    }
}
