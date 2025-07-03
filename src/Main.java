import Lexer.Lexer;
import Parser.Expression.ExpressionParser;
import Parser.Parser;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Lexer lexer = new Lexer(scanner.nextLine());
        
//        System.out.println("TOKENIZED QUERY");
//        System.out.println(ExpressionParser.formPostfix(
//                lexer.tokenizeQuery()
//        ));
//        System.out.println("************************************************************************************************************************************************************");
//        System.out.println("AST AFTER PARSING");
//        System.out.println(ExpressionParser.postfixToAST(
//                ExpressionParser.formPostfix(
//                        lexer.tokenizeQuery()
//                )
//        ));

        Parser parser = new Parser(lexer.tokenizeQuery());
        System.out.println(parser.parse());
    }
}