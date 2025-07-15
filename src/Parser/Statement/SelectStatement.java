package Parser.Statement;

import Aggregator.Aggregator;
import Aggregator.*;
import Executor.QueryExecutor;
import FileManager.FileManager;
import FileManager.RowReader;
import Lexer.TokenType;
import Parser.Enums.StatementType;
import Parser.Expression.BinExp;
import Parser.Expression.Expression;
import Parser.Expression.FunctionCallExp;
import Parser.Expression.IdentifierExp;
import Parser.Types.Result;
import Parser.Types.Row;
import Parser.Types.Table;

import java.util.*;

public class SelectStatement extends Statement {

    private final String tableName;
    private final List<Expression> columns;
    private final Map<Integer, String> columnAliases;
    private final Expression whereClause;
    private final Expression havingClause;
    private final List<String> groupBy;
    
    public SelectStatement(String tableName, List<Expression> columns, Map<Integer, String> columnAliases) {
        super(StatementType.SELECT);
        this.tableName = tableName;
        this.columns = columns;
        this.whereClause = null;
        this.columnAliases = columnAliases;
        this.havingClause = null;
        this.groupBy = null;
    }
    
    public SelectStatement(String tableName, List<Expression> columns, Expression whereClause, Map<Integer, String> columnAliases, Expression havingClause, List<String> groupBy) {
        super(StatementType.SELECT);
        this.tableName = tableName;
        this.columns = columns;
        this.whereClause = whereClause;
        this.columnAliases = columnAliases;
        this.havingClause = havingClause;
        this.groupBy = groupBy;
    }

    public String getTableName() {
        return tableName;
    }

    public List<Expression> getColumns() {
        return columns;
    }

    public Expression getWhereClause() {
        return whereClause;
    }

    public Map<Integer, String> getColumnAliases() {
        return columnAliases;
    }

    public Expression getHavingClause() {
        return havingClause;
    }

    public List<String> getGroupBy() {
        return groupBy;
    }

    private List<Row> collectFilteredRows(Table table, SelectStatement selectStatement) throws Exception {
        List<Row> result = new ArrayList<>();
        try (RowReader reader = table.getRowReader()) {
            while (reader.hasNext()) {
                Row row = reader.next();
                if (passesWhereClause(selectStatement, row)) {
                    result.add(row);
                }
            }
        }
        return result;
    }

    private List<List<String>> executePlainQuery(List<Row> rows, SelectStatement selectStatement) throws Exception {
        List<List<String>> evaluatedRows = new ArrayList<>();
        for (Row row : rows) {
            List<String> evaluatedRow = new ArrayList<>();
            for(Expression expression : selectStatement.getColumns()) {
                evaluatedRow.add(expression.evaluate(row.getValues()).toString());
            }
            evaluatedRows.add(evaluatedRow);
        }
        return evaluatedRows;
    }

    private List<List<String>> executeAggregateQuery(SelectStatement selectStatement,
                                                     Set<FunctionCallExp> aggregateFunctions, List<Row> filteredRows) throws Exception {

        Map<FunctionCallExp, Aggregator> aggregators = new HashMap<>();


        for(FunctionCallExp functionCallExp : aggregateFunctions) {
            aggregators.put(functionCallExp, createAggregator(functionCallExp));
        }

        for(Row row : filteredRows) {
            for(FunctionCallExp functionCallExp : aggregateFunctions) {
                aggregators.get(functionCallExp)
                        .add((Integer) functionCallExp.getFunctionArgument().evaluate(row.getValues()));
            }
        }

        Map<String, Object> context = new HashMap<>();
        for (FunctionCallExp f : aggregateFunctions) {
            context.put(f.toFlatString(), aggregators.get(f).getResult());
        }

        if (selectStatement.getHavingClause() != null) {
            if (!(boolean) selectStatement.getHavingClause().evaluate(context)) {
                return List.of();
            }
        }

        List<String> row = new ArrayList<>();
        for (Expression expr : selectStatement.getColumns()) {
            row.add(expr.evaluate(context).toString());
        }

        return List.of(row);
    }


    private List<List<String>> executeGroupByQuery(SelectStatement selectStatement, Table table,
                                                   Set<FunctionCallExp> aggregateFunctions) throws Exception {

        List<List<String>> evaluatedRows = new ArrayList<>();
        Map<List<String>, List<Row>> groups = groupRows(selectStatement, table);

        Map<FunctionCallExp, Aggregator> aggregators = new HashMap<>();

        for(FunctionCallExp functionCallExp : aggregateFunctions) {
            aggregators.put(functionCallExp, createAggregator(functionCallExp));
        }

        for(Map.Entry<List<String>, List<Row>> entry : groups.entrySet()) {
            for(Row row : entry.getValue()) {
                for(FunctionCallExp functionCallExp : aggregateFunctions) {
                    aggregators.get(functionCallExp)
                            .add((Integer) functionCallExp.getFunctionArgument().evaluate(row.getValues()));
                }
            }

            Map<String, Object> context = new HashMap<>();
            for (FunctionCallExp f : aggregateFunctions) {
                context.put(f.toFlatString(), aggregators.get(f).getResult());
            }

            List<String> evaluatedRow = new ArrayList<>();

            for(Expression expression : selectStatement.getColumns()) {
                if (selectStatement.getHavingClause() != null) {
                    if (!(boolean) selectStatement.getHavingClause().evaluate(context)) {
                        continue;
                    }
                }

                if(expression instanceof FunctionCallExp functionCallExp) {
                    evaluatedRow.add(functionCallExp.evaluate(context).toString());
                } else if(expression instanceof IdentifierExp identifierExp) {
                    int index = selectStatement.getGroupBy().indexOf(identifierExp.getName());
                    if(index == -1) throw new RuntimeException("Column: " + identifierExp.getName() + " is not in a GROUP BY clause");
                    evaluatedRow.add(entry.getKey().get(index));
                } else if(expression instanceof BinExp binExp) {
                    evaluatedRow.add(binExp.evaluate(context).toString());
                }

            }
            if(!evaluatedRow.isEmpty()) evaluatedRows.add(evaluatedRow);
            aggregators.forEach((k,v) -> v.clear());
        }
        return evaluatedRows;
    }

    private Map<List<String>, List<Row>> groupRows(SelectStatement selectStatement, Table table) throws Exception {
        Map<List<String>, List<Row>> groups = new LinkedHashMap<>();

        try (RowReader reader = table.getRowReader()) {
            while (reader.hasNext()) {
                Row row = reader.next();

                if (passesWhereClause(selectStatement, row)) {
                    List<String> groupKey = buildGroupKey(selectStatement, row);
                    groups.computeIfAbsent(groupKey, k -> new ArrayList<>()).add(row);
                }
            }
        }

        return groups;
    }

    private List<String> buildGroupKey(SelectStatement selectStatement, Row row) {
        List<String> groupKey = new ArrayList<>(selectStatement.getGroupBy().size());

        for (String column : selectStatement.getGroupBy()) {
            groupKey.add(row.getValue(column).toString());
        }

        return groupKey;
    }

    private boolean passesWhereClause(SelectStatement selectStatement, Row row) throws Exception {
        return selectStatement.getWhereClause() == null ||
                (boolean) selectStatement.getWhereClause().evaluate(row.getValues());
    }

    public Set<FunctionCallExp> collectAggregateFunctions(Expression expr) {
        Set<FunctionCallExp> aggregates = new HashSet<>();
        collectAggregatesRecursive(expr, aggregates);
        return aggregates;
    }

    private void collectAggregatesRecursive(Expression expression, Set<FunctionCallExp> aggregates) {
        if(expression instanceof FunctionCallExp e) {
            aggregates.add(e);
        } else if (expression instanceof BinExp) {
            collectAggregatesRecursive(((BinExp)expression).getLeft(), aggregates);
            collectAggregatesRecursive(((BinExp)expression).getRight(), aggregates);
        }
    }

    private Aggregator createAggregator(FunctionCallExp functionCallExp) {
        return switch (functionCallExp.getFunctionType()) {
            case TokenType.SUM -> new SumAggregator();
            case TokenType.AVG -> new AvgAggregator();
            case TokenType.MAX -> new MaxAggregator();

            default -> throw new RuntimeException("Unsupported aggregate type");
        };
    }

    @Override
    public Result execute(Map<String, Object> context, boolean print) throws Exception {
        long startTime = System.nanoTime();

        Table table = FileManager.loadTableFromFile(getTableName() + ".bin");

        List<List<String>> evaluatedRows;
        Set<FunctionCallExp> aggregateFunctions = new HashSet<>();

        this.getColumns()
                .forEach(expression -> {
                    aggregateFunctions.addAll(collectAggregateFunctions(expression));
                });

        aggregateFunctions.addAll(collectAggregateFunctions(this.getHavingClause()));

        List<Row> filteredRows;

        if(this.getGroupBy() != null) {
            evaluatedRows = executeGroupByQuery(this, table, aggregateFunctions);
        } else {
            filteredRows = collectFilteredRows(table, this);

            if(aggregateFunctions.isEmpty()) {
                evaluatedRows = executePlainQuery(filteredRows, this);
            } else {
                evaluatedRows = executeAggregateQuery(this, aggregateFunctions, filteredRows);
            }
        }
        if(print) {
            QueryExecutor.printResults(this, evaluatedRows, startTime);
        }
        return new Result(evaluatedRows, this.getColumns());
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
