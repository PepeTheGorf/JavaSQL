package Parser.Expression;

import Parser.Enums.ExpressionType;

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
}
