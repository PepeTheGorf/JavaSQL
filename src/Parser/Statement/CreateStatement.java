package Parser.Statement;

import Parser.Enums.StatementType;
import Parser.Types.Column;

import java.util.List;

public class CreateStatement extends Statement {
    
    private final String tableName;
    private final List<Column> columns;
    
    public CreateStatement(String tableName, List<Column> columns) {
        super(StatementType.CREATE);
        this.tableName = tableName;
        this.columns = columns;
    }

    @Override
    public String toString() {
        return "CreateStatement{" +
                "tableName='" + tableName + '\'' +
                ", columns=" + columns +
                '}';
    }
}
