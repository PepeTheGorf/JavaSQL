package Executor;

import Parser.Expression.Expression;
import Parser.Statement.SelectStatement;

import java.util.*;

public class QueryExecutor {
    
    private static String pad(String text, int width) {
        int padding = width - 1 - text.length();
        return " " + text + " ".repeat(Math.max(padding, 1));
    }
    
    public static void printResults(SelectStatement selectStatement, List<List<String>> evaluatedRows, long startTime) {
        String[] tableHeaders = new String[selectStatement.getColumns().size()];
        int[] tableColumnWidths = new int[tableHeaders.length];

        int columnIndex = 0;
        for (Expression expression : selectStatement.getColumns()) {
            tableHeaders[columnIndex] = selectStatement.getColumnAliases()
                    .getOrDefault(columnIndex++, expression.toFlatString());
        }

        // Calculate column widths
        for (int i = 0; i < tableHeaders.length; i++) {
            tableColumnWidths[i] = tableHeaders[i].length() + 2;
        }

        for (List<String> data : evaluatedRows) {
            int i = 0;
            for (String str : data) {
                tableColumnWidths[i] = Math.max(tableColumnWidths[i], str.length() + 2);
                i++;
            }
        }

        // Print table
        StringBuilder border = new StringBuilder("+");
        for (int tableColumnWidth : tableColumnWidths) {
            border.append("-".repeat(tableColumnWidth)).append('+');
        }

        System.out.println(border);
        System.out.print("|");
        for (int i = 0; i < tableHeaders.length; i++) {
            String columnAlias = selectStatement.getColumnAliases().getOrDefault(i, tableHeaders[i]);
            System.out.print(pad(columnAlias, tableColumnWidths[i]) + "|");
        }
        System.out.println();
        System.out.println(border);

        for (List<String> data : evaluatedRows) {
            System.out.print("|");
            int colIndex = 0;
            for (String str : data) {
                System.out.print(pad(str, tableColumnWidths[colIndex++]) + "|");
            }
            System.out.println();
        }

        System.out.println(border);
        printExecutionTime(startTime, evaluatedRows.size());
    }
    
    public static void printExecutionTime(long startTime, int rowsAffected) {
        long endTime = System.nanoTime();
        long elapsedNanos = endTime - startTime;
        double elapsedSeconds = elapsedNanos / 1000000000.0;

        System.out.printf("Query OK, %d row(s) affected. Executed in: (%.3f s)%n", rowsAffected, elapsedSeconds);
    }
}
