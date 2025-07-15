package Lexer;

public enum TokenType {
    // Keywords
    SELECT, FROM, WHERE, INSERT, INTO, VALUES,
    UPDATE, SET, DELETE, CREATE, TABLE, AND, OR, NOT, NULL, IS, AS, GROUP_BY, HAVING,
    
    // Data types
    INTEGER, VARCHAR,

    // Identifiers and literals
    IDENTIFIER, STRING_LITERAL, NUMBER_LITERAL,

    // Operators
    EQUALS, NOT_EQUALS, GREATER_THAN, LESS_THAN,
    GREATER_EQUAL, LESS_EQUAL, PLUS, MINUS, ASTERISK, SLASH,
    
    //Functions
    SUM, AVG, COUNT, MAX, MIN, SUBQUERY,

    // Symbols
    COMMA, SEMICOLON, DOT, LEFT_PAREN, RIGHT_PAREN,
    
    EOF // End of file
}