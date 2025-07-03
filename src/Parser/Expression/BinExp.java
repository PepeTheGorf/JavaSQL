package Parser.Expression;

import Lexer.Token;
import Parser.Enums.ExpressionType;

public class BinExp extends Expression {
    
    private final Expression left;
    private final Token operator;
    private final Expression right;
    
    public BinExp(Expression left, Token operator, Expression right) {
        super(ExpressionType.BinExp);
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    public Expression getLeft() {
        return left;
    }

    public Token getOperator() {
        return operator;
    }
    
    public Expression getRight() {
        return right;
    }

    @Override
    public String toTreeString(String indent, boolean isLast) {
        String childIndent = indent + (isLast ? "\t" : "│\t");
        return indent +
                (isLast ? "└── " : "├── ") +
                operator.getLexeme() + "\n" +
                left.toTreeString(childIndent, false) +
                right.toTreeString(childIndent, true);
    }
    
    @Override
    public String toString() {
        return this.toTreeString("", true);
    }
}
