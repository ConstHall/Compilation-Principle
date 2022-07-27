package scanner;

import exceptions.*;
import token.Token;
import token.BooleanToken;
import token.OperatorToken;
import token.DecimalToken;
import token.FunctionToken;
import token.DollarToken;

//Scanner类：根据不同词法单元的状态转换图将表达式转换成词法单元流
public class Scanner {
	protected int index;
	protected String input;
	protected int len;
	protected final String operators = "+-*/^?:><=&|!(),";

	/**
	 * 构造函数
	 * @param expression 表达式字符串
	 */
	public Scanner(String expression) {
		//全部转为小写并删除空格，便于后续处理
		input = expression.toLowerCase().replace(" ", "");
		index = 0;
		len = input.length();
	}

	/**
	 * decimal数字token的状态自动机
	 * @throws LexicalException
	 * @throws ExpressionException
	 */
	public Token decimalDFA() throws LexicalException, ExpressionException {
		Boolean dotFlag = false;
		Boolean eFlag = false;
		int start = index;
		for (int i = start + 1; i < len; i++) {
			char peek = input.charAt(i);
			if (Character.isDigit(peek)) {
				index++;
				continue;
			}
			else if (peek == '.') {
				if (eFlag == true || dotFlag == true) {
					throw new IllegalDecimalException();
				}
				else if (i + 1 >= len) {
					throw new IllegalDecimalException();
				}
				else if (!Character.isDigit(input.charAt(i+1))) {
					throw new IllegalDecimalException();
				}
				else {
					index++;
					dotFlag = true;
				}					
			}
			else if (peek == 'e') {
				if (eFlag == true) {
					throw new IllegalDecimalException();
				}
					
				else if (i + 1 >= len) {
					throw new IllegalDecimalException();
				}
				else if (!(Character.isDigit(input.charAt(i+1)) || input.charAt(i+1) == '+' || input.charAt(i+1) == '-' )) {
					throw new IllegalDecimalException();
				}
					
				else {
					index++;
					eFlag = true;
				}
			}
			else if (peek == '+' || peek == '-') {
				if (!(input.charAt(i-1) == 'e')) {
					break;
				}
				else if (i+1 == len) {
					throw new IllegalDecimalException();
				}	
				else {
					index++;
				}
			}
			else {
				break;
			}
		}
		index++; 
		return new DecimalToken(input.substring(start, index));
	}

	/**
	 * 布尔变量token的状态自动机
	 * @param curChar
	 * @throws LexicalException
	 * @throws ExpressionException
	 */
	public Token booleanDFA(Character curChar) throws LexicalException, ExpressionException {
		String boolStr = "";
		//true
		if (curChar == 't') {
			boolStr = input.substring(index, index+4);
			if (boolStr.equals("true")) {
				index += 4;
				return new BooleanToken(boolStr);
			}
			else {
				throw new IllegalIdentifierException();
			}
		}
		//false
		else {
			boolStr = input.substring(index, index+5);
			if (boolStr.equals("false")) {
				index += 5;
				return new BooleanToken(boolStr);
			}
			else {
				throw new IllegalIdentifierException();
			}
		}
	}

	/**
	 * 预定义函数token的状态自动机
	 * @throws LexicalException
	 * @throws ExpressionException
	 */
	public Token functionDFA() throws LexicalException, ExpressionException {
		String funcLow = input.substring(index, index+3);
		if (funcLow.equals("sin") || funcLow.equals("cos") || funcLow.equals("max") || funcLow.equals("min")) {
			index += 3;
			return new FunctionToken(funcLow);
		}
		else {
			throw new IllegalIdentifierException();
		}
	}

	/**
	 * 操作符token的状态自动机
	 * @param curChar
	 * @throws LexicalException
	 * @throws ExpressionException
	 */
	public Token operDFA(Character curChar) throws LexicalException, ExpressionException {
		if (curChar == '>') {
			if (index < len - 1) {
				if (input.charAt(index+1) == '=') {
					index += 2;
					return new OperatorToken(">=");
				}
			}
		}

		if (curChar == '<') {
			if (index < len -1) {
				if (input.charAt(index+1) == '=') {
					index += 2;
					return new OperatorToken("<=");
				}
				if (input.charAt(index+1) == '>') {
					index += 2;
					return new OperatorToken("<>");
				}
			}
		}

		if (curChar == '-') {
			if (index - 1 >= 0) {
				//只有当减号-前为右括号)或者数字时，它才代表二元运算符减号-，否则就是一元运算符负号-
				if (input.charAt(index - 1 ) == ')' || Character.isDigit(input.charAt(index -1))) {
					index += 1;
					return new OperatorToken("-");
				}
			}
			index += 1;
			return new OperatorToken("neg");
		}

		if (curChar == ')') {
			if (index - 1 >= 0) {
				if (input.charAt(index - 1) == '(')
					throw new MissingOperandException();
			}
		}

		if (curChar == ':') {
			if (index - 1 >= 0) {
				if (input.charAt(index - 1) == '?')
					throw new MissingOperandException();
			}
		}
		index += 1;
		return new OperatorToken(curChar.toString());
	}

	/**
	 * 获取下一个词法单元
	 * @return 下一个词法单元
	 * @throws LexicalException
	 * @throws ExpressionException
	 */
	public Token getNextToken() throws LexicalException, ExpressionException { 
		if (len == 0) {
			throw new EmptyExpressionException();
		}
		if (index >= len) {
			return new DollarToken();
		}	
			
		Character curChar = input.charAt(index);

		if (Character.isDigit(curChar)) {
			return decimalDFA();
		}
		else if (curChar == 't' || curChar == 'f') {
			return booleanDFA(curChar);
		}
		else if (curChar == 's' || curChar == 'c' || curChar == 'm') {
			return functionDFA();
		}	
		else if (operators.indexOf(curChar) != -1) {
			return operDFA(curChar);
		}		
		else {
			if (curChar == '.') {
				if (index + 1 < input.length()) {
					if (Character.isDigit(input.charAt(index+1))) {
						throw new IllegalDecimalException();
					}
					else {
						throw new LexicalException();
					}	
				}
				else {
					throw new LexicalException();
				}
			}
			else if (Character.isAlphabetic(curChar)) {
				throw new IllegalIdentifierException();
			}
			else {
				throw new IllegalSymbolException();
			}
		}
	}
}