package Parser;

public class ParserException extends RuntimeException {

    public ParserException(String message) {
        super(message);
    }

    public ParserException(String message, int position) {
        super("Lexical error at: " + position + ": " + message);
    }
}
