package Parser.Types;

import Parser.Expression.Expression;

import java.util.List;

public class Result {
    private List<List<String>> values;
    private List<Expression> columns;

    public Result(List<List<String>> values, List<Expression> columns) {
        this.values = values;
        this.columns = columns;
    }

    public List<List<String>> getValues() {
        return values;
    }

    public List<Expression> getColumns() {
        return columns;
    }
}
