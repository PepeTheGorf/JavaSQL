package Lexer;

import java.util.ArrayList;
import java.util.List;

public class Lexer {
    
    private final String query;
    
    public Lexer(String query) {
        this.query = query;
    }
    
    public List<Token> tokenizeQuery() {
        List<Token> list = new ArrayList<>();
        List<Character> chars = new ArrayList<>();
        
        for(char ch : query.toCharArray()) chars.add(ch);
        
        for (int i = 0; i < chars.size(); i++) {
            char ch = chars.get(i);
            if(Character.isAlphabetic(ch)) {
                StringBuilder lexemeBuilder = new StringBuilder();
                while (i < chars.size() && Character.isAlphabetic(chars.get(i))) {
                    lexemeBuilder.append(chars.get(i));
                    i++;
                }
                i--;
                
                switch (lexemeBuilder.toString().toUpperCase()) {
                    case "SELECT" -> list.add(new Token(TokenType.SELECT, "SELECT"));
                    case "FROM" -> list.add(new Token(TokenType.FROM, "FROM"));
                    case "WHERE" -> list.add(new Token(TokenType.WHERE, "WHERE"));
                    case "INSERT" -> list.add(new Token(TokenType.INSERT, "INSERT"));
                    case "INTO" -> list.add(new Token(TokenType.INTO, "INTO"));
                    case "VALUES" -> list.add(new Token(TokenType.VALUES, "VALUES"));
                    case "CREATE" -> list.add(new Token(TokenType.CREATE, "CREATE"));
                    case "TABLE" -> list.add(new Token(TokenType.TABLE, "TABLE"));
                    case "UPDATE" -> list.add(new Token(TokenType.UPDATE, "UPDATE"));
                    case "SET" -> list.add(new Token(TokenType.SET, "SET"));
                    case "DELETE" -> list.add(new Token(TokenType.DELETE, "DELETE"));
                    case "AND" -> list.add(new Token(TokenType.AND, "AND"));
                    case "OR" -> list.add(new Token(TokenType.OR, "OR"));
                    case "NOT" -> list.add(new Token(TokenType.NOT, "NOT"));
                    case "NULL" -> list.add(new Token(TokenType.NULL, "NULL"));
                    case "IS" -> list.add(new Token(TokenType.IS, "IS"));
                    case "INTEGER" -> list.add(new Token(TokenType.INTEGER, "INTEGER"));
                    case "VARCHAR" -> list.add(new Token(TokenType.VARCHAR, "VARCHAR"));
                    default -> list.add(new Token(TokenType.IDENTIFIER, lexemeBuilder.toString()));
                }
                 
            } else if (Character.isDigit(ch)) {
                StringBuilder lexemeBuilder = new StringBuilder();
                while (i < chars.size() && Character.isDigit(chars.get(i))) {
                    lexemeBuilder.append(chars.get(i));
                    i++;
                }
                i--;
                try {
                    int parsed = Integer.parseInt(lexemeBuilder.toString());
                    list.add(new Token(TokenType.NUMBER_LITERAL, lexemeBuilder.toString(), parsed));
                } catch (NumberFormatException exception) {
                    System.out.println("Error while parsing integer: " + lexemeBuilder);
                }
            } else {
                switch (ch) {
                    case ' ', '\n', '\t' -> {
                        
                    }
                    case '+' -> list.add(new Token(TokenType.PLUS, "+"));
                    case '-' -> list.add(new Token(TokenType.MINUS, "-"));
                    case '*' -> list.add(new Token(TokenType.ASTERISK, "*"));
                    case '/' -> list.add(new Token(TokenType.SLASH, "/"));
                    case '(' -> list.add(new Token(TokenType.LEFT_PAREN, "("));
                    case ')' -> list.add(new Token(TokenType.RIGHT_PAREN, ")"));
                    case '>' -> {
                        if(i < chars.size() - 1 && chars.get(++i) == '=') {
                            list.add(new Token(TokenType.GREATER_EQUAL, ">="));
                        } else {
                            list.add(new Token(TokenType.GREATER_THAN, ">"));
                        }
                    }
                    case '<' -> {
                        if(i < chars.size() - 1 && chars.get(++i) == '=') {
                            list.add(new Token(TokenType.LESS_EQUAL, "<="));
                        } else {
                            list.add(new Token(TokenType.LESS_THAN, "<"));
                        }
                    }
                    case '=' -> list.add(new Token(TokenType.EQUALS, "="));
                    case '!' -> {
                        if(i < chars.size() - 1 && chars.get(++i) == '=') {
                            list.add(new Token(TokenType.NOT_EQUALS, "!="));
                        } else {
                            throw new LexerException("Invalid character found at position " + i + ", expected \"=\", but found " + "\""  + chars.get(i) + "\"");
                        }
                    }
                    case '.' -> list.add(new Token(TokenType.DOT, "."));
                    case ';' -> list.add(new Token(TokenType.SEMICOLON, ";"));
                    case ',' -> list.add(new Token(TokenType.COMMA, ","));
                    case '\'' -> {
                        i++;
                        StringBuilder lexemeBuilder = new StringBuilder();
                        while (i < chars.size() && Character.isAlphabetic(chars.get(i))) {
                            lexemeBuilder.append(chars.get(i));
                            i++;
                        }
                        if(chars.size() == i || chars.get(i) != '\'') throw new LexerException("Invalid character found at position " + i + ", expected \"'\"");
                        
                        list.add(new Token(TokenType.STRING_LITERAL, lexemeBuilder.toString(), lexemeBuilder.toString()));
                    }
                    default -> throw new LexerException("Invalid character found at: " + i);
                }
            }
        }
        //todo: uncomment later list.add(new Token(TokenType.EOF, "EOF"));
        return list;
    }
}
