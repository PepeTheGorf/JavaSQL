package Parser.Expression;

import Parser.Enums.ExpressionType;

import java.util.Map;

public abstract class Expression {
    private final ExpressionType expressionType;
    
    public Expression(ExpressionType expressionType) {
        this.expressionType = expressionType;
    }

    public ExpressionType getExpressionType() {
        return expressionType;
    }
    
    protected abstract String toTreeString(String childIndent, boolean b);
    
    public abstract String toFlatString();
    
    public abstract Object evaluate(Map<String, Object> values) throws Exception;
    
}
