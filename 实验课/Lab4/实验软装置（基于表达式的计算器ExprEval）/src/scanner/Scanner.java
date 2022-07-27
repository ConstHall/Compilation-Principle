package scanner;

import exceptions.*;

/**
 * A lexicial analyzer to read expression and divide the elements into four defined lexical units.
 * @author ZhiHan Jiang
 */
public class Scanner {

	/** The private int value indicate the postion of currenct charactor {@value} */
	private int pos;

	/** The private int value indicate the length of the string of expression {@value} */
	private int len;

	/** The private String of the length of the lowercase and no whitespace string of expression.{@value} */
	private String exprStr;

	/**
	 * The generator function of Scanner.
	 * 1. set the pos to zero
	 * 2. get the modified string of expression and get length to len
	 * @param exprStr the String of expression
	 */
	public Scanner(String exprStr) {
		pos = 0;
		exprStr = exprStr.toLowerCase().replace(" ", "");
		len = exprStr.length();
	}

	/**
	 * The public function of to get the next Token.
	 * @return the next Token
	 * @throws LexicalException
	 * @throws ExpressionException
	 */
	public Token getNextToken() throws LexicalException, ExpressionException {
		String opStr = "+-*/^?:><=&|!(),";
		if (len == 0) throw new EmptyExpressionException();
		if (pos >= len)	return new TokenDollar();
		Character curChar = exprStr.charAt(pos);

		if (Character.isDigit(curChar)) // Digit
			return processDigit();
		else if (curChar == 't' || curChar == 'f') // Boolean
			return processBool(curChar);
		else if (opStr.indexOf(curChar) != -1) // Operator
			return processOperator(curChar);
		else if (curChar == 's' || curChar == 'c' || curChar == 'm') // Function
			return processFunction();
		else processError(curChar); // Error
		return new Token();
	}

	/**
	 * The private function of to process Digit Token.
	 * @return the next TokenDigit
	 * @throws IllegalDecimalException
	 */	
	private Token processDigit() throws IllegalDecimalException {
		Boolean dotFlag = false, eFlag = false, breakFlag = false;
		int curPos = pos;

		for (int i = curPos + 1; i < len; i++) {
			char curChar = exprStr.charAt(i);
			if (Character.isDigit(curChar)) {
				pos++;
				continue;
			} else {
				switch(curChar) {
				case '.':
					if (eFlag || dotFlag) throw new IllegalDecimalException();
					else if (i >= len - 1) 
						throw new IllegalDecimalException();
					else if (!Character.isDigit(exprStr.charAt(i+1)))
						throw new IllegalDecimalException();
					else {
						pos++;
						dotFlag = true;
					}
					break;
				case 'e':
					if (eFlag)	throw new IllegalDecimalException();
					else if (i >= len - 1) 
						throw new IllegalDecimalException();
					else if (!(Character.isDigit(exprStr.charAt(i+1)) || 
						exprStr.charAt(i+1) == '+' || exprStr.charAt(i+1) == '-' ))
						throw new IllegalDecimalException();
					else {
						pos++;
						eFlag = true;
					}
					break;
				case '-':
				case '+':
					if ( (exprStr.charAt(i-1) != 'e') ) {
						breakFlag = true;
						break;
					}
					else if (i == len - 1)
						throw new IllegalDecimalException();
					else 
						pos++;
					break;
				default:
					breakFlag = true;
					break;
				}
			}
			if (breakFlag)	break;
		}
		pos++; 
		return new TokenDigit(exprStr.substring(curPos, pos));
	}

	/**
	 * The private function of to process boolean Token.
	 * @param curChar the currenct charactor
	 * @return the next TokenBoolean
	 * @throws IllegalIdentifierException
	 */	
	private Token processBool(Character curChar) throws IllegalIdentifierException {
		if (curChar == 't') {
			String boolStrLow = exprStr.substring(pos, pos+4);
			if (boolStrLow.equals("true")) {
				pos += 4;
				return new TokenBoolean(boolStrLow);
			}
			else throw new IllegalIdentifierException();
		}
		else {
			String boolStrLow = exprStr.substring(pos, pos+5);
			if (boolStrLow.equals("false")) {
				pos += 5;
				return new TokenBoolean(boolStrLow);
			}
			else throw new IllegalIdentifierException();
		}
	}

	/**
	 * The private function of to process operator Token.
	 * @param curChar the currenct charactor
	 * @return the next TokenOperator
	 * @throws MissingOperandException
	 */	
	private Token processOperator(Character curChar) throws MissingOperandException {
		switch(curChar) {
		case '>':
			if (pos + 1 < len && exprStr.charAt(pos+1) == '=') {
				pos += 2;
				return new TokenOperator(">=");
			}
			break;
		case '<':
			if (pos < len -1 && exprStr.charAt(pos+1) == '=') {
				pos += 2;
				return new TokenOperator("<=");
			}
			if (pos < len -1 && exprStr.charAt(pos+1) == '>') {
				pos += 2;
				return new TokenOperator("<>");
			}
			break;
		case '-':
			if (pos >= 1 && (exprStr.charAt(pos - 1 ) == ')' || 
					Character.isDigit(exprStr.charAt(pos -1))) ) {
				pos += 1;
				return new TokenOperator("-");
			}
			pos += 1;
			return new TokenOperator("minus");
		case ')':
			if (pos >= 1 && exprStr.charAt(pos - 1) == '(')
				throw new MissingOperandException();
			break;
		case ':':
			if (pos >= 1 && exprStr.charAt(pos - 1) == '?')
				throw new MissingOperandException();
			break;
		default:
			break;
		}
		pos += 1;
		return new TokenOperator(curChar.toString());
	}

	/**
	 * The private function of to process function Token.
	 * @return the next TokenFunction
	 * @throws IllegalIdentifierException
	 */		
	private Token processFunction() throws IllegalIdentifierException {
		String funcLow = exprStr.substring(pos, pos+3);
		if (funcLow.equals("max") || funcLow.equals("min") || funcLow.equals("sin") || funcLow.equals("cos")) {
			pos += 3;
			return new TokenFunction(funcLow);
		}
		else throw new IllegalIdentifierException();
	}

	/**
	 * The private function of to process error.
	 * @param curChar the currenct charactor
	 * @return the next TokenOperator
	 * @throws LexicalException
	 */	
	private void processError(Character curChar) throws LexicalException{
		if (curChar == '.') {
			if (pos + 1 < exprStr.length()) {
				if (Character.isDigit(exprStr.charAt(pos+1)))
					throw new IllegalDecimalException();
				else 
					throw new LexicalException();
			}
			else throw new LexicalException();
		} else if (!Character.isAlphabetic(curChar))
			throw new IllegalSymbolException();
		else 
			throw new IllegalIdentifierException();
	}
}
