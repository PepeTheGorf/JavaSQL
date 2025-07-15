package Parser.Expression;

import Parser.Enums.ExpressionType;

import java.util.Map;

public class IdentifierExp extends Expression {
    
    private final String name;
    
    public IdentifierExp(String name) {
        super(ExpressionType.Identifier);
        this.name = name;
    }

    public String getName() {
        return name;
    }
    
    @Override
    public String toString() {
        return name;
    }

    @Override
    protected String toTreeString(String indent, boolean isLast) {
        return indent + (isLast ? "└── " : "├── ") + name + "\n";
    }
    
    @Override
    public String toFlatString() {
        return name;
    }

    @Override
    public Object evaluate(Map<String, Object> values) {
        if(values == null) throw new RuntimeException("Column name not allowed here");
        return values.get(name);
    }
}
