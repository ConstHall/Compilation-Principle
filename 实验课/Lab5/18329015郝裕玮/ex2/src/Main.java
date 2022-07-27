import java.io.*;
import exceptions.*;

public class Main {
	public static void main(String[] args) throws Exception {
		String word;
		for (int i = 0; i < args.length; i++) {
			System.out.println("\n" + args[i] + "\n");
			OberonScanner scanner = new OberonScanner(new java.io.FileReader(args[i]));
			while (true) {
				try {
					word = scanner.yylex();
					if (word.equals("EOF")) {
						System.out.println("\n扫描完毕，该程序未出现词法错误！\n");
						break;
					}
					System.out.println(word + " : " + scanner.yytext());
				} 
				catch (LexicalException error) {
					System.out.println("\nLine "+ scanner.get_line() + ", Column " + scanner.get_column() + ": " + scanner.yytext() + " 存在异常");
					System.out.println(error.getMessage()+"\n");
					break;
				}
			}
		}
	}
}
