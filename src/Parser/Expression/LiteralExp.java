package Parser.Expression;

import Parser.Enums.ExpressionType;

import java.util.Map;

public class LiteralExp extends Expression {
    
    private final Object value;
    
    public LiteralExp(Object value) {
        super(ExpressionType.Literal);
        this.value = value;
    }

    public Object getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public String toTreeString(String indent, boolean isLast) {
        return indent + (isLast ? "└── " : "├── ") + value + "\n";
    }
    
    @Override
    public String toFlatString() {
        return value.toString();
    }

    @Override
    public Object evaluate(Map<String, Object> values) {
        return value;
    }
}
