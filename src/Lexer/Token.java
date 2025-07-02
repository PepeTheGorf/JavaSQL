package Lexer;

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
        return "Token{" +
                "lexeme='" + lexeme + '\'' +
                ", tokenType=" + tokenType +
                ", literal=" + literal +
                '}';
    }
}
