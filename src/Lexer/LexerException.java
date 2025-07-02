package Lexer;

public class LexerException extends RuntimeException {
    
    public LexerException(String message) {
        super(message);
    }
    
    public LexerException(String message, int position) {
        super("Lexical error at: " + position + ": " + message);
    }
}
