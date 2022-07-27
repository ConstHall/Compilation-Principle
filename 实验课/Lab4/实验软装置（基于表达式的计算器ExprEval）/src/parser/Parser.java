package parser;

import java.util.Arrays;
import java.util.Stack;
import exceptions.*;
import scanner.*;

/**
 * The Parser class is the core part of Parser, it has the parsing() method to parsing a String of expression.
 * And it completes all the shift and reduce method which will be used.
 * @author ZhiHan Jiang
 */
public class Parser {
	/**
	 * The private Stack of Token that store the symbols.
	 */
	private Stack<Token> operators = new Stack<Token>();

	/**
	 * The private Stack of Token that store the operands.
	 */
	private Stack<Token> operands = new Stack<Token>();

	/**
	 * The generator function of Parser.
	 * 1. init the operators and operands.
	 * 2. push the '$' into the operands.
	 */
	public Parser() {
		operators.clear();
		operands.clear();
		TokenDollar TokenDollar = new TokenDollar();
		operators.push(TokenDollar);				
	}

	/**
	 * Performs parsing and semantic actions.
	 * @param exprStr the String of expression
	 * @return the result of expression
	 * @throws ExpressionException
	 */
	public Double parsing(String exprStr) throws ExpressionException {
		Boolean done = false;
		Scanner scanner = new Scanner(exprStr);
		Token curToken = scanner.getNextToken();
				
		while(!done) {
			Token topToken = operators.peek();
			if (curToken.getType().equals("Decimal") || curToken.getType().equals("Boolean")) {
				operands.push(curToken);
				curToken = scanner.getNextToken();
				continue;
			}
			else {
				int labelStack = getIndex(getLabel(topToken));
				int labelRead = getIndex(getLabel(curToken));
				int action = OPP.OPP_TABLE[labelStack][labelRead];
				switch (action) {
				case OPP.ACCEPT:
					done = true;
					break;
				case OPP.SHIFT:
					shift(curToken);
					curToken = scanner.getNextToken();
					break;
				case OPP.BRACETREDUCE:
					reduce0();
					curToken = scanner.getNextToken();
					break;
				case OPP.REDUCE1:
					reduce1();
					break;
				case OPP.REDUCE2:
					reduce2();
					break;
				case OPP.REDUCE3:
					reduce3();
					break;
				case OPP.ERRORLEFT:
					throw new MissingLeftParenthesisException();
				case OPP.ERRORGRAM:
					throw new SyntacticException();
				case OPP.ERROROP:
					throw new MissingOperandException();
				case OPP.ERRORTYPE:
					throw new TypeMismatchedException();
				case OPP.ERRORFUNC:
					throw new FunctionCallException();
				case OPP.ERRORRIGHT:
					throw new MissingRightParenthesisException();
				case OPP.ERRORTHREE:
					throw new TrinaryOperationException();
				default:
					break;
				}
			}
		}
		
		if (operands.size() == 1) {
			if (operands.peek().getType().equals("Decimal"))
				return ((TokenDigit)operands.peek()).getValue();
			else throw new TypeMismatchedException();
		}
		else throw new MissingOperatorException();
	}
	
	/**
	 * The shift action that push the token into operators.
	 * @param opr The operator's Token
	 */
	private void shift(Token opr) {
		operators.push(opr);
	}
	
	/**
	 * The reduce of unary operation, includes '!' and '-'.
	 * 1. get the top element of operands.
	 * 2. do the unary operation.
	 * 3. push the result into operands.
	 * @throws SyntacticException
	 */
	private void reduce1() throws MissingOperandException{
		if (operands.empty())
			throw new MissingOperandException();
		TokenOperator operator = (TokenOperator)operators.pop();
		Token operand = operands.pop();
		if (operator.getLabel().equals("-")) {
			Double value = ((TokenDigit)operand).getValue();
			((TokenDigit)operand).setValue(-value);
		}
		else if (operator.getLabel().equals("!")) {
			Boolean value = ((TokenBoolean)operand).getValue();
			((TokenBoolean)operand).setValue(!(Boolean)value);			
		}
		operands.push(operand);
	}
	
	/**
	 * The reduce of binary operation
	 * @throws TypeMismatchedException 
	 * @throws DividedByZeroException 
	 * @throws SyntacticException
	 */
	private void reduce2() throws TypeMismatchedException, SyntacticException, DividedByZeroException {
		if (operands.size() < 2)
			throw new MissingOperandException();
		TokenOperator operator = (TokenOperator)operators.pop();
		Token operand2 = operands.pop();
		Token operand1 = operands.pop();
		// includes + - * / ^
		if (operator.getLabel().equals("plus_minus") || operator.getLabel().equals("multiply_divid") || operator.getLabel().equals("^")) {
			if (operand1.getType().equals("Decimal") && operand2.getType().equals("Decimal")) {
				Double value1 = ((TokenDigit)operand1).getValue();
				Double value2 = ((TokenDigit)operand2).getValue();
				Double result = 0.0;
				switch (operator.getName().charAt(0)) {
				case '+':
					result = value1 + value2;
					break;
				case '-':
					result = value1 - value2;
					break;
				case '*':
					result = value1 * value2;
					break;			
				case '/':
					if (Math.abs(value2 - 0.0) < 1e-9)
						throw new DividedByZeroException();
					result = value1 / value2;
					break;
				case '^':
					result = Math.pow(value1, value2);
					break;
				default:
					break;
				}
				operands.push(new TokenDigit(Double.toString(result)));
			}
			else throw new TypeMismatchedException();
		}
		// include & |
		else if (operator.getLabel().equals("&") || operator.getLabel().equals("|")) {
			if (operand1.getType().equals("Boolean") && operand2.getType().equals("Boolean")) {
				Boolean value1 = ((TokenBoolean)operand1).getValue();
				Boolean value2 = ((TokenBoolean)operand2).getValue();
				Boolean result = false;
				switch (operator.getName().charAt(0)) {
				case '&':
					result = value1 & value2; break;
				case '|':	
					result = value1 | value2; break;
				default:
					break;
				}
				operands.push(new TokenBoolean(Boolean.toString(result)));
			}
			else {
				throw new TypeMismatchedException();
			}
		}
		//include > < = >= <= <>
		else if (operator.getLabel().equals("cmp")) {
			if (operand1.getType().equals("Decimal") && operand2.getType().equals("Decimal")) {
				Double value1 = ((TokenDigit)operand1).getValue();
				Double value2 = ((TokenDigit)operand2).getValue();
				Boolean result = false;
				String operName = operator.getName();
				if ( (operName.equals(">") && value1 > value2) || 
					 (operName.equals("<") && value1 < value2) || 
					 (operName.equals("=") && (Math.abs(value1 - value2) < 1e-9) ) || 
					 (operName.equals(">=")&& ( (value1 > value2) || (Math.abs(value1 - value2) < 1e-9)) ) ||
					 (operName.equals("<=")&& ( (value1 < value2) || (Math.abs(value1 - value2) < 1e-9)) ) || 
					 (operName.equals("<>")&& (Math.abs(value1 - value2) >= 1e-9)) )
					result = true;
				operands.push(new TokenBoolean(Boolean.toString(result)));
			}
			else throw new TypeMismatchedException();
		}		
	}
	
	/**
	 * The reduce of reduce3 operation: the "? :"
	 * @throws TrinaryOperationException
	 * @throws TypeMismatchedException
	 * @throws SyntacticException
	 */
	private void reduce3() throws TrinaryOperationException, TypeMismatchedException, SyntacticException {
		if (operands.size() < 3 || operators.size() < 3)
			throw new MissingOperandException();
		TokenOperator operator1 = (TokenOperator)operators.pop();
		TokenOperator operator2 = (TokenOperator)operators.pop();
		Token operand3 = operands.pop();
		Token operand2 = operands.pop();
		Token operand1 = operands.pop();
		if (operator1.getName().equals(":") && operator2.getName().equals("?")) {
			if (operand1.getType().equals("Boolean") && operand2.getType().equals("Decimal") 
					&& operand3.getType().equals("Decimal")) {
				Double result = 0.0;
				if ( ((TokenBoolean)operand1).getValue() )
					result = ((TokenDigit)operand2).getValue(); 
				else
					result = ((TokenDigit)operand3).getValue();
				operands.push(new TokenDigit(Double.toString(result)));
			}
			else throw new TypeMismatchedException();
		}
		else throw new TrinaryOperationException();
	}
	
	/**
	 * The result of bracket and function.
	 * @throws TypeMismatchedException
	 * @throws SyntacticException
	 * @throws DividedByZeroException 
	 * @see Parser#calFunction(int, String)
	 */
	private void reduce0() throws TypeMismatchedException, SyntacticException, DividedByZeroException {	
		Token operator = operators.peek();
		int cnt = 0; // the count of comma
		Boolean done = false;
		while (!done) {
			if (operator.getType().equals("TokenDollar"))
				throw new MissingLeftParenthesisException();
			else {
				if (((TokenOperator)operator).getName().equals("(")) {
					operators.pop();
					done = true;
					break;
				}
				int number = ((TokenOperator)operator).getNumber();
				
				if (cnt == 0 && number == 1)
					reduce1();
				else if (cnt == 0 && number == 2)
					reduce2();
				else if (cnt == 0 && number == 3)
					reduce3();
				else if (((TokenOperator)operator).getName().equals(",")) {
					operators.pop();
					cnt++;
				}
				else throw new SyntacticException();
			}
			operator = operators.peek();
		}

		operator = operators.peek();
		if (operator.getType().equals("Function")) { // function
			calFunction(cnt + 1, ((TokenFunction)operator).getName());
			operators.pop();
		}		
	}
	

		/**
	 * Get the label of Token.
	 * @param token the token
	 * @return the label of Token
	 */
	private String getLabel(Token token) {
		String type = token.getType();
		if (type.equals("Function"))
			return ((TokenFunction)token).getLabel();
		else if (type.equals("Operator"))
			return ((TokenOperator)token).getLabel();
		else return ((TokenDollar)token).getLabel();
	}
	
	/**
	 * Get the index of Token's label. 
	 * @param label the label of Token.
	 * @return the index of Token's label
	 */
	private int getIndex(String label) {
		String[] labelList = {"(", ")", "func", "-", "^", "multiply_divid", "plus_minus", "cmp", "!", "&", "|", "?", ":", ",", "$"};
		return Arrays.asList(labelList).indexOf(label);
	}
	
	/**
	 * Calculate the function value.
	 * includes sin, cos, max, min
	 * @param argCount the count of function parameter
	 * @param strFunc the String of function's name
	 * @throws SyntacticException
	 */
	private void calFunction(int argCount, String strFunc) throws SyntacticException {
		if (operands.size() == 0)
			throw new MissingOperandException();

		Token operand = operands.pop();
		Double result = ((TokenDigit)operand).getValue();
		if (operand.getType().intern() != "Decimal")
			throw new FunctionCallException();
		if (argCount >= 2) {
			if (operands.size() < argCount - 1)
				throw new MissingOperandException();
			if (strFunc.equals("sin") || strFunc.equals("cos"))
				throw new FunctionCallException();			
			for (int i = 0; i < argCount - 1; i++) {
				operand = operands.pop();
				Double topValue = ((TokenDigit)operand).getValue();
				if (strFunc.equals("max") && topValue > result)
					result = topValue; 
				else if (strFunc.equals("min") && topValue < result)
					result = topValue;
			}
		}
		else {
			if(strFunc.equals("sin"))
				result = Math.sin(result);
			else if (strFunc.equals("cos"))
				result = Math.cos(result);
			else throw new MissingOperandException();
		}
		operands.push(new TokenDigit(Double.toString(result)));
	}
	
}
