package Lexer;

import java.util.Arrays;

public class Token {
    
    private TokenType tokenType;
    private String lexeme;
    private Object literal;
    
    public Token(TokenType tokenType, String lexeme, Object literal) {
        this.tokenType = tokenType;
        this.lexeme = lexeme;
        this.literal = literal;
    }

    public Token(TokenType tokenType, String lexeme) {
        this.tokenType = tokenType;
        this.lexeme = lexeme;
    }
    
    public boolean isOperand() {
        return this.tokenType.equals(TokenType.NUMBER_LITERAL) || this.tokenType.equals(TokenType.STRING_LITERAL);
    }
    
    public boolean isIdentifier() {
        return this.tokenType.equals(TokenType.IDENTIFIER);
    }
    
    public boolean isOperator() {
        return this.tokenType.equals(TokenType.PLUS) || this.tokenType.equals(TokenType.MINUS) || this.tokenType.equals(TokenType.ASTERISK) ||
               this.tokenType.equals(TokenType.SLASH) || this.tokenType.equals(TokenType.AND) || this.tokenType.equals(TokenType.OR) || this.tokenType.equals(TokenType.GREATER_THAN) ||
               this.tokenType.equals(TokenType.GREATER_EQUAL) || this.tokenType.equals(TokenType.EQUALS) || this.tokenType.equals(TokenType.LESS_THAN) || this.tokenType.equals(TokenType.LESS_EQUAL) ||
               this.tokenType.equals(TokenType.LEFT_PAREN) || this.tokenType.equals(TokenType.RIGHT_PAREN); 
    }
    
    public int getPrecedence() {
        return switch (this.tokenType) {
            case TokenType.ASTERISK, TokenType.SLASH -> 6;
            case TokenType.PLUS, TokenType.MINUS -> 5;
            case TokenType.GREATER_THAN, TokenType.LESS_THAN, TokenType.GREATER_EQUAL, TokenType.LESS_EQUAL -> 4;
            case TokenType.EQUALS -> 3;
            case TokenType.AND -> 2;
            case TokenType.OR -> 1;
            default -> -1;
        };
    }

    public TokenType getTokenType() {
        return tokenType;
    }

    public void setTokenType(TokenType tokenType) {
        this.tokenType = tokenType;
    }

    public String getLexeme() {
        return lexeme;
    }

    public void setLexeme(String lexeme) {
        this.lexeme = lexeme;
    }

    public Object getLiteral() {
        return literal;
    }

    public void setLiteral(Object literal) {
        this.literal = literal;
    }

    @Override
    public String toString() {
        return String.format("'%s' [%s]", lexeme, tokenType);
    }

}
