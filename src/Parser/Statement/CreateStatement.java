package Parser.Statement;

import Executor.QueryExecutor;
import FileManager.FileManager;
import Parser.Enums.StatementType;
import Parser.Types.Column;
import Parser.Types.Result;
import Parser.Types.Table;

import java.util.List;
import java.util.Map;

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

    public String getTableName() {  
        return tableName;
    }

    public List<Column> getColumns() {
        return columns;
    }

    @Override
    public Result execute(Map<String, Object> context, boolean print) {
        long startTime = System.nanoTime();

        FileManager.saveTableToFile(new Table(
                this.getTableName() + ".bin",
                this.getColumns()
        ));

        QueryExecutor.printExecutionTime(startTime, 0);
        return null;
    }
}
