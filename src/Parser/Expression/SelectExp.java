package Parser.Expression;

import Parser.Enums.ExpressionType;
import Parser.Statement.SelectStatement;
import Parser.Types.Result;

import java.util.HashMap;
import java.util.Map;

public class SelectExp extends Expression{

    private final SelectStatement selectStatement;

    private Result cachedResult = null;
    
    public SelectExp(SelectStatement selectStatement) {
        super(ExpressionType.Identifier);
        this.selectStatement = selectStatement;
    }

    public SelectStatement getSelectStatement() {
        return selectStatement;
    }

    @Override
    protected String toTreeString(String indent, boolean isLast) {
        return indent + (isLast ? "└── " : "├── ") + "Subquery\n" +
                indent + "\t" + selectStatement.toString();
    }

    @Override
    public String toFlatString() {
        return "(" + selectStatement.toString() + ")";
    }

    @Override
    public Object evaluate(Map<String, Object> values) throws Exception {
        if (cachedResult == null) {
            cachedResult = selectStatement.execute(new HashMap<>(), false);
        }
        if(cachedResult.getValues().size() > 1 || cachedResult.getValues().getFirst().size() > 1) throw new RuntimeException("Subquery returns more than 1 row");
        return Integer.valueOf(cachedResult.getValues().getFirst().getFirst());
    }

    public Result getCachedResult() throws Exception {
        if (cachedResult == null) {
            cachedResult = selectStatement.execute(new HashMap<>(), false);
        }
        return cachedResult;
    }

    public void clearCache() {
        cachedResult = null;
    }
}
