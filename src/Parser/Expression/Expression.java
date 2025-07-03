package Parser.Expression;

import Parser.Enums.ExpressionType;

public abstract class Expression {
    private final ExpressionType expressionType;
    
    public Expression(ExpressionType expressionType) {
        this.expressionType = expressionType;
    }

    public ExpressionType getExpressionType() {
        return expressionType;
    }
    
    protected abstract String toTreeString(String childIndent, boolean b);
}
