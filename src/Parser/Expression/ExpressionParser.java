package Parser.Expression;

import Lexer.Token;
import Lexer.TokenType;
import Parser.Statement.SelectStatement;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class ExpressionParser {
    
    public static List<Token> formPostfix(List<Token> tokens) {
        Stack<Token> op_stack = new Stack<>();
        List<Token> output = new ArrayList<>();
        
        for(Token token : tokens) {
            if(token.isOperand() || token.isIdentifier()) {
                output.add(token);
            } else if(token.getTokenType().equals(TokenType.LEFT_PAREN)) {
                op_stack.push(token);
            } else if(token.getTokenType().equals(TokenType.RIGHT_PAREN)) {
                while (!op_stack.lastElement().getTokenType().equals(TokenType.LEFT_PAREN)) {
                    output.add(op_stack.pop());
                }
                op_stack.pop();
                
                if(!op_stack.isEmpty() && (op_stack.peek().isFunctionCall()) || op_stack.peek().isSubQuery()) {
                    output.add(op_stack.pop());
                }
            } else if(token.isFunctionCall() || token.isSubQuery()) {
                op_stack.push(token);
            } else {
                while(!op_stack.isEmpty()) {
                    if(token.getPrecedence() <= op_stack.lastElement().getPrecedence()) {
                        output.add(op_stack.pop());
                    } else {
                        break;
                    }
                }
                op_stack.push(token);
            }
        }
        while (!op_stack.isEmpty()) {
            output.add(op_stack.pop());
        }
        return output;
    }
    
    public static Expression postfixToAST(List<Token> postfixTokens) {
        Stack<Expression> expressionStack = new Stack<>();
        
        for(Token token : postfixTokens) {
            if(token.isOperand()) {
                expressionStack.push(new LiteralExp(token.getLiteral()));
            } else if(token.isIdentifier()) {
                expressionStack.push(new IdentifierExp(token.getLexeme()));
            } else if (token.isFunctionCall()) {
                expressionStack.push(new FunctionCallExp(expressionStack.pop(), token.getTokenType()));
            } else if (token.isSubQuery()) {
                expressionStack.push(new SelectExp((SelectStatement) token.getLiteral()));
            } else {
                expressionStack.push(new BinExp(
                        expressionStack.pop(),
                        token,
                        expressionStack.pop()
                ));
            }
        }
        return expressionStack.getFirst();
    }
}
