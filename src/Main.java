import Executor.QueryExecutor;
import FileManager.FileManager;
import Lexer.Lexer;
import Parser.Expression.Expression;
import Parser.Parser;
import Parser.Statement.CreateStatement;
import Parser.Statement.SelectStatement;
import Parser.Statement.Statement;
import Parser.Types.Row;
import Parser.Types.Table;
import Parser.Test;


import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        while(true) {
            Scanner scanner = new Scanner(System.in);
            String query = scanner.nextLine();
            
            if(query.equalsIgnoreCase("quit") || query.equalsIgnoreCase("q")) break;
            
            
            Lexer lexer = new Lexer(query);
            Parser parser = new Parser(lexer.tokenizeQuery());


            Statement parsedStatement = parser.parse();

            if(parsedStatement instanceof SelectStatement selectStatement) {
                selectStatement.execute(null, true);
            }
            
            //QueryExecutor queryExecutor = new QueryExecutor(parsedStatement);
            
            //queryExecutor.executeStatement();

            //queryExecutor.executeStatement();
            
        }
    }
}