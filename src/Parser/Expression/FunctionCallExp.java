package Parser.Expression;

import Lexer.TokenType;
import Parser.Enums.ExpressionType;

import java.util.*;

public class FunctionCallExp extends Expression {
    
    private Expression functionArgument;
    private TokenType functionType;
    
    public FunctionCallExp(Expression functionArgument, TokenType functionType) {
        super(ExpressionType.FunctionCall);
        this.functionArgument = functionArgument;
        this.functionType = functionType;
    }
    
    @Override
    public ExpressionType getExpressionType() {
        return super.getExpressionType();
    }

    @Override
    protected String toTreeString(String indent, boolean isLast) {
        String childIndent = indent + (isLast ? "\t" : "│\t");
        return indent +
                (isLast ? "└── " : "├── ") +
                functionType.toString() + "\n" +
                functionArgument.toTreeString(childIndent, false);
    }

    @Override
    public String toFlatString() {
        return this.getFunctionType() + "(" + functionArgument.toFlatString() + ")";
    }

    @Override
    public Object evaluate(Map<String, Object> values) {
        return values.get(this.toFlatString());
    }

    public Expression getFunctionArgument() {
        return functionArgument;
    }

    public TokenType getFunctionType() {
        return functionType;
    }

    @Override
    public String toString() {
        return toTreeString("", true);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof FunctionCallExp that)) return false;
        return this.toFlatString().equals(that.toFlatString());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.toFlatString());
    }
}
