package Parser.Statement;

import Parser.Enums.StatementType;
import Parser.Expression.Expression;
import Parser.Types.Table;

import java.util.List;

public class SelectStatement extends Statement {

    private final String tableName;
    private final List<Expression> columns;
    private final Expression whereClause;
    
    public SelectStatement(String tableName, List<Expression> columns) {
        super(StatementType.SELECT);
        this.tableName = tableName;
        this.columns = columns;
        this.whereClause = null;
    }
    
    public SelectStatement(String tableName, List<Expression> columns, Expression whereClause) {
        super(StatementType.SELECT);
        this.tableName = tableName;
        this.columns = columns;
        this.whereClause = whereClause;
    }

    @Override
    public String toString() {
        return "SelectStatement{" +
                "tableName='" + tableName + '\'' +
                ", columns=" + columns +
                ", whereClause=" + whereClause +
                '}';
    }
}
