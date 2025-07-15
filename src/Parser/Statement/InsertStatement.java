package Parser.Statement;

import Executor.QueryExecutor;
import FileManager.FileManager;
import Parser.Enums.StatementType;
import Parser.Expression.Expression;
import Parser.Types.Result;
import Parser.Types.Row;
import Parser.Types.Table;
import FileManager.*;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Override
    public Result execute(Map<String, Object> context, boolean print) throws Exception {
        long startTime = System.nanoTime();

        Table table = FileManager.loadTableFromFile(this.getTableName() + ".bin");


        List<Row> rows = new ArrayList<>();
        for(List<Expression> rowExpressions : this.getRows()) {
            List<Object> rowValues = new ArrayList<>();
            for(Expression rowExpression : rowExpressions) {
                rowValues.add(rowExpression.evaluate(new HashMap<>()));
            }
            rows.add(new Row(table.getColumns(), rowValues));
        }

        RowWriter.writeRows(Path.of(table.getName() + "Data.bin"), rows, table.getColumns());

        QueryExecutor.printExecutionTime(startTime, rows.size());
        return null;
    }
}
