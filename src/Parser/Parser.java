package Parser;

import Lexer.Token;
import Lexer.TokenType;
import Parser.Expression.Expression;
import Parser.Expression.ExpressionParser;
import Parser.Statement.CreateStatement;
import Parser.Statement.InsertStatement;
import Parser.Statement.SelectStatement;
import Parser.Statement.Statement;
import Parser.Types.Column;
import Parser.Types.DataType;

import java.util.ArrayList;
import java.util.List;

public class Parser {
    
    private final List<Token> tokens;
    private int position = 0;
    
    private void advance() {
        if(position < tokens.size()) position++;
        else throw new ParserException("Token list overflow", position);
    }
    
    private Token getNextToken() {
        if(this.tokens.size() <= position) throw new ParserException("Parser token list limit exceeded");
        
        return this.tokens.get(position);
    }
    
    private void expectNextOrThrow(TokenType token) {
        expectNextOrThrow(List.of(token));
    }

    private void expectNextOrThrow(List<TokenType> expectedTokens) {
        Token current = tokens.get(position);
        if (!expectedTokens.contains(current.getTokenType())) {
            throw new ParserException(
                    "Invalid token: \"" + current.getLexeme() + "\", " +
                            "expected: " + expectedTokens + ". " +
                            "At token index: " + position
            );
        }
        advance();
    }
    
    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }
    
    public Statement parse() {
        switch(tokens.get(position).getTokenType()) {
            case TokenType.CREATE -> {
                advance();
                return parseCreate();
            }
            case TokenType.INSERT -> {
                advance();
                return parseInsert();
            }
            case TokenType.SELECT -> {
                advance();
                return parseSelect();
            }
        }
        throw new ParserException("Invalid token: " + "\"" + tokens.get(position).getLexeme() + "\"" + ". At token index: " + position);
    }
    //CREATE TABLE Users (ID Integer, Name VarChar(255));
    public CreateStatement parseCreate() {
        expectNextOrThrow(TokenType.TABLE);
        
        Token tableName = getNextToken();
        expectNextOrThrow(TokenType.IDENTIFIER);
        
        expectNextOrThrow(TokenType.LEFT_PAREN);
        
        List<Column> columns = new ArrayList<>();
        while(true) {
            Token columnName = getNextToken();
            expectNextOrThrow(TokenType.IDENTIFIER);
            
            Token columnDataType = getNextToken();
            expectNextOrThrow(List.of(TokenType.INTEGER, TokenType.VARCHAR));
            
            int lengthInBytes = 4; //Default for integer columns.
            if(columnDataType.getTokenType().equals(TokenType.VARCHAR)) {
                expectNextOrThrow(TokenType.LEFT_PAREN);
                
                Token nextToken = getNextToken();
                expectNextOrThrow(TokenType.NUMBER_LITERAL);
                lengthInBytes = (Integer)nextToken.getLiteral();
                
                expectNextOrThrow(TokenType.RIGHT_PAREN);
            }
            
            Token nextToken = getNextToken();
            expectNextOrThrow(List.of(TokenType.COMMA, TokenType.RIGHT_PAREN));
            
            columns.add(new Column(
                    columnName.getLexeme(),
                    lengthInBytes,
                    columnDataType.getTokenType()
            ));
            
            if(nextToken.getTokenType().equals(TokenType.RIGHT_PAREN)) break;
        }
        expectNextOrThrow(TokenType.SEMICOLON);
        
        return new CreateStatement(tableName.getLexeme(), columns);
    }
    
    
    //INSERT INTO Users VALUES (1,'Bob'),(2,'John');
    public InsertStatement parseInsert() {
        expectNextOrThrow(TokenType.INTO);

        Token tableName = getNextToken();
        expectNextOrThrow(TokenType.IDENTIFIER);
        
        expectNextOrThrow(TokenType.VALUES);
        
        List<List<Expression>> rows = new ArrayList<>();
        while(true) {
            expectNextOrThrow(TokenType.LEFT_PAREN);
            
            List<Expression> row = new ArrayList<>();
            while(true) {
                row.add(parseExpression());
                
                Token nextToken = getNextToken();
                expectNextOrThrow(List.of(TokenType.COMMA, TokenType.RIGHT_PAREN));
                
                if(nextToken.getTokenType().equals(TokenType.RIGHT_PAREN)) break;
            }
            rows.add(row);
            
            Token nextToken = getNextToken();
            expectNextOrThrow(List.of(TokenType.COMMA, TokenType.SEMICOLON));
            
            if(nextToken.getTokenType().equals(TokenType.SEMICOLON)) break;
        }
        return new InsertStatement(tableName.getLexeme(), rows);
    }
    
    //SELECT Id, Name FROM Users;
    public SelectStatement parseSelect() {
        List<Expression> columns = new ArrayList<>();
        while(true) {
            columns.add(parseExpression());
            
            Token nextToken = getNextToken();
            expectNextOrThrow(List.of(TokenType.COMMA, TokenType.FROM));
            
            if(nextToken.getTokenType().equals(TokenType.FROM)) break;
        }
        Token tableName = getNextToken();
        expectNextOrThrow(TokenType.IDENTIFIER);
        
        expectNextOrThrow(TokenType.SEMICOLON);
        
        return new SelectStatement(tableName.getLexeme(), columns);
    }
    //INSERT INTO Users VALUES (1 + 1 + 2,'Bob'),(2,'John');
    private Expression parseExpression() {
        List<Token> expressionTokens = new ArrayList<>();
        int parenCounter = 0;
        while(true) {
            Token nextToken = getNextToken();
            if(nextToken.getTokenType().equals(TokenType.COMMA) || nextToken.getTokenType().equals(TokenType.FROM)) break;
            if(nextToken.getTokenType().equals(TokenType.LEFT_PAREN)) parenCounter++;
            
            if(nextToken.getTokenType().equals(TokenType.RIGHT_PAREN)) {
                if(parenCounter-- == 0) break;
            }
            
            expressionTokens.add(nextToken);
            advance();
        }
        return ExpressionParser.postfixToAST(ExpressionParser.formPostfix(expressionTokens));
    }
}
