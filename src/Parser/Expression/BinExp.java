package Parser.Expression;

import Lexer.Token;
import Lexer.TokenType;
import Parser.Enums.ExpressionType;

import java.util.Map;
import java.util.Objects;

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
    public String toFlatString() {
        return left.toFlatString() + " " + operator.getLexeme() + " " + right.toFlatString();
    }

    @Override
    public Object evaluate(Map<String, Object> values) throws Exception {
        Object left = getLeft().evaluate(values);
        Object right = getRight().evaluate(values);
        
        switch (operator.getTokenType()) {
            case TokenType.PLUS -> {
                return (Integer) right + (Integer) left;
            }
            case TokenType.MINUS -> {
                return (Integer) right - (Integer) left;
            }
            case TokenType.ASTERISK -> {
                return (Integer) right * (Integer) left;
            }
            case TokenType.SLASH -> {
                return (Integer) right / (Integer) left;
            }
            case TokenType.AND -> {
                return (Boolean) right && (Boolean) left;
            }
            case TokenType.OR -> {
                return (Boolean) right|| (Boolean) left;
            }
            case TokenType.GREATER_THAN ->  {
                return (Integer) right > (Integer) left;
            }
            case TokenType.GREATER_EQUAL -> {
                return (Integer) right >= (Integer) left;
            }
            case TokenType.LESS_THAN -> {
                return (Integer) right < (Integer) left;
            }
            case TokenType.LESS_EQUAL -> {
                return (Integer) right <= (Integer) left;
            }
            case TokenType.EQUALS -> {
                return Objects.equals((Integer) right, (Integer) left);
            }
            case TokenType.NOT_EQUALS -> {
                return !Objects.equals((Integer) right, (Integer) left);
            }

            default -> throw new IllegalStateException("Unexpected value: " + operator);
        }
    }

    @Override
    public String toString() {
        return this.toTreeString("", true);
    }
}
