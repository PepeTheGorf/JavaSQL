import Lexer.Lexer;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Lexer lexer = new Lexer(scanner.nextLine());

        System.out.println(lexer.tokenizeQuery());
    }
}