package parser;

import java.util.*;
import exceptions.*;
import scanner.Scanner;
import token.*;

public class Parser {
	private final String[] TAG = {"(", ")", "func", "-", "^", "md", "pm", "cmp", "!", "&", "|", "?", ":", ",", "$"};
	private Stack<Token> operator = new Stack<Token>();	
	private Stack<Token> operand = new Stack<Token>();
	private Token curToken = new Token();
	private Token topToken = new Token();
	private static final Double ERROR = 0.00001;

	/**
	 * 构造函数
	 */
	public Parser() {
		//对运算符和运算量2个堆栈进行初始化
		operator.clear();
		operand.clear();
		//将终结符$压入操作符堆栈中
		DollarToken DollarToken = new DollarToken();
		operator.push(DollarToken);				
	}

	/**
	 * 获取对应词法单元的tag
	 * @param temp 词法单元
	 * @return 词法单元的tag
	 */
	private String getTag(Token temp) {
		//获取词法单元类型
		String tempType = temp.getType();
		if (tempType.equals("Function")) {
			return ((FunctionToken)temp).getTag();
		}
		if (tempType.equals("Operator")) {
			return ((OperatorToken)temp).getTag();
		}
		return ((DollarToken)temp).getTag();
	}

	/**
	 * 获取词法单元tag在TAG中的对应下标
	 * @param label 符号表中的词法单元标记
	 * @return 词法单元在TAG数组中的下标
	 */
	private int getIndex(String tag) {
		return Arrays.asList(TAG).indexOf(tag);
	}

	/**
	 * 4个预定义函数：sin,cos,max,min
	 * @param cnt 逗号数量，用来确定max和min函数所需的操作数个数
	 * @param func 预定义函数名
	 * @throws SyntacticException
	 */
	private void doFunction(int cnt, String func) throws SyntacticException {
		//运算量缺失则返回错误MissingOperandException
		if (operand.size() == 0) {
			throw new MissingOperandException();
		}

		Token tempOperand = operand.pop();
		//intern:如果常量池里面不存在该字符的引用,则将其引入常量池中并返回这个对象的引用(string类型)
		//发现运算量不是十进制数
		if (tempOperand.getType().intern() != "Decimal") {
			throw new FunctionCallException();//预定义函数调用的语法形式错误
		}

		double ansValue = ((DecimalToken)tempOperand).getValue();
		//cnt > 0代表存在逗号(只可能是max或min函数)
		if (cnt > 0) {
			if (operand.size() < cnt){//发现运算量数量不匹配
				throw new MissingOperandException();
			}
				
			//不可能是sin或cos函数(因为有逗号)
			if (func.equals("sin") || func.equals("cos")){
				throw new FunctionCallException();
			}

			double topValue = 0.0;
			//找出运算量堆栈中最大或最小的数值(由max或min来决定)
			for (int i = 0; i < cnt; i++) {
				tempOperand = operand.pop();
				topValue = ((DecimalToken)tempOperand).getValue();
				if (func.equals("max") && topValue > ansValue) {
					ansValue = topValue; 
				}
				if (func.equals("min") && topValue < ansValue) {
					ansValue = topValue;
				}
			}
		}
		//没有逗号代表是sin或cos函数
		else {
			Double radians = ((DecimalToken)tempOperand).getValue();
			if (func.equals("sin")) {
				ansValue = Math.sin(radians);
			}
			else if (func.equals("cos")) {
				ansValue = Math.cos(radians);
			}
			else {
				throw new MissingOperandException();
			}
		}
		operand.push(new DecimalToken(Double.toString(ansValue)));
	}

	/**
	 * 将词法单元移入(shift)operator堆栈(运算符堆栈)中
	 * @param tempOperator 符号词法单元
	 */
	private void shift(Token tempOperator) {
		operator.push(tempOperator);
	}

	/**
	 * 一元运算归约(reduce):非(!)，负号(数字取负)(-)
	 * 这些运算只需要一个运算量
	 * @throws SyntacticException
	 */
	private void reduceUnary() throws SyntacticException{
		//若缺少运算量(数量为0)
		if (operand.empty()){
			throw new MissingOperandException();
		}
			
		OperatorToken tempOperator = (OperatorToken)operator.pop();
		Token tempOperand = operand.pop();
		//单元运算符为负号(-)
		if (tempOperator.getTag().equals("-")) {
			Double tempValue = ((DecimalToken)tempOperand).getValue();
			tempValue = (-1) * tempValue;
			((DecimalToken)tempOperand).setValue(tempValue);
		}
		//单元运算符为非(!)
		if (tempOperator.getTag().equals("!")) {
			Boolean tempValue = ((BooleanToken)tempOperand).getValue();
			tempValue = !tempValue;
			((BooleanToken)tempOperand).setValue(tempValue);			
		}
		operand.push(tempOperand);
	}

	/**
	 * 二元运算归约:加(+)，减(-)，乘(*)，除(/)，取幂(^)，关系运算(= <> < <= > >=)，与(&)，或(|)
	 * 这些运算需要2个运算量
	 * @throws TypeMismatchedException 
	 * @throws DividedByZeroException 
	 * @throws SyntacticException
	 */
	private void reduceBinary() throws TypeMismatchedException, SyntacticException, DividedByZeroException {
		//若运算量数量少于2
		if (operand.size() < 2){
			throw new MissingOperandException();
		}
		
		OperatorToken tempOperator = (OperatorToken)operator.pop();
		//从运算量栈中依次弹出两个运算量
		Token operandB = operand.pop();
		Token operandA = operand.pop();
		if (tempOperator.getTag().equals("pm") || tempOperator.getTag().equals("md") || tempOperator.getTag().equals("^")) {
			if (operandA.getType().equals("Decimal") && operandB.getType().equals("Decimal")) {
				Double valueA = ((DecimalToken)operandA).getValue();
				Double valueB = ((DecimalToken)operandB).getValue();
				Double valueC = 0.0;
				//加减乘除幂运算
				switch (tempOperator.getValue().charAt(0)) {
				case '+':
					valueC = valueA + valueB;
					break;
				case '-':
					valueC = valueA - valueB; 
					break;
				case '*':
					valueC = valueA * valueB; 
					break;			
				case '/':
					if (Math.abs(valueB - 0.0) < ERROR)
						throw new DividedByZeroException();
					valueC = valueA / valueB; 
					break;
				case '^':
					valueC = Math.pow(valueA, valueB); 
					break;
				default:
					break;
				}
				operand.push(new DecimalToken(Double.toString(valueC)));
			}
			else {
				throw new TypeMismatchedException();
			}
		}
		//与运算和或运算
		if (tempOperator.getTag().equals("&") || tempOperator.getTag().equals("|")) {
			if (operandA.getType().equals("Boolean") && operandB.getType().equals("Boolean")) {
				Boolean boolA = ((BooleanToken)operandA).getValue();
				Boolean boolB = ((BooleanToken)operandB).getValue();
				Boolean boolC = false;
				switch (tempOperator.getValue().charAt(0)) {
				case '&':
					boolC = boolA & boolB; 
					break;
				case '|':	
					boolC = boolA | boolB; 
					break;
				default:
					break;
				}
				operand.push(new BooleanToken(Boolean.toString(boolC)));
			}
			else {
				throw new TypeMismatchedException();
			}
		}
		//关系运算(= <> < <= > >=)
		if (tempOperator.getTag().equals("cmp")) {
			if (operandA.getType().equals("Decimal") && operandB.getType().equals("Decimal")) {
				Double valueA = ((DecimalToken)operandA).getValue();
				Double valueB = ((DecimalToken)operandB).getValue();
				Boolean boolC = false;
				String operLexeme = tempOperator.getValue();
				if ( (operLexeme.equals(">") && valueA > valueB) || 
					 (operLexeme.equals("<") && valueA < valueB) || 
					 (operLexeme.equals("=") && (Math.abs(valueA - valueB) < ERROR) ) || 
					 (operLexeme.equals(">=")&& ( (valueA > valueB) || (Math.abs(valueA - valueB) < ERROR)) ) ||
					 (operLexeme.equals("<=")&& ( (valueA < valueB) || (Math.abs(valueA - valueB) < ERROR)) ) || 
					 (operLexeme.equals("<>")&& (Math.abs(valueA - valueB) >= ERROR)) ) {
					boolC = true;
				}
				operand.push(new BooleanToken(Boolean.toString(boolC)));
			}
			else {
				throw new TypeMismatchedException();
			}
		}		
	}

	/**
	 * 三元运算归约：?: 
	 * 该运算需要3个运算量
	 * @throws TrinaryOperationException
	 * @throws TypeMismatchedException
	 * @throws SyntacticException
	 */
	private void reduceTrinary() throws TrinaryOperationException, TypeMismatchedException, SyntacticException {
		//运算量或运算符数量少于3
		if (operand.size() < 3 || operator.size() < 3) {
			throw new MissingOperandException();
		}
		//从运算符栈中依次弹出两个运算量
		OperatorToken operatorB = (OperatorToken)operator.pop();
		OperatorToken operatorA = (OperatorToken)operator.pop();
		Token operandC = operand.pop();
		Token operandB = operand.pop();
		Token operandA = operand.pop();

		if (operatorA.getValue().equals("?") && operatorB.getValue().equals(":")) {
			if (operandA.getType().equals("Boolean") && operandB.getType().equals("Decimal") && operandC.getType().equals("Decimal")) {
				Double valueD = 0.0;
				if ( ((BooleanToken)operandA).getValue() ) {
					valueD = ((DecimalToken)operandB).getValue(); 
				}
				else {
					valueD = ((DecimalToken)operandC).getValue();
				}
				operand.push(new DecimalToken(Double.toString(valueD)));
			}
			else {
				throw new TypeMismatchedException();
			}
		}
		else {
			throw new TrinaryOperationException();
		}
	}

	/**
	 * 括号运算和预定义函数(max,min,sin,cos)归约
	 * @throws TypeMismatchedException
	 * @throws SyntacticException
	 * @throws DividedByZeroException 
	 */
	private void matchReduce() throws TypeMismatchedException, SyntacticException, DividedByZeroException {	
		Token tempOperator = operator.peek();
		int cntComma = 0;//逗号数量
		Boolean matchCompleted = false;
		while (!matchCompleted) {
			//直接匹配到终结符号
			if (tempOperator.getType().equals("Dollar")) {
				throw new MissingLeftParenthesisException();
			}
			else {
				if (((OperatorToken)tempOperator).getValue().equals("(")) {
					operator.pop();
					matchCompleted = true;
					break;
				}
				int tempNum = ((OperatorToken)tempOperator).getNum();
				if (cntComma == 0 && tempNum == 1) {
					reduceUnary();
				}
				else if (cntComma == 0 && tempNum == 2) {
					reduceBinary();
				}
				else if (cntComma == 0 && tempNum == 3) {
					reduceTrinary();
				}
				else if (((OperatorToken)tempOperator).getValue().equals(",")) {
					operator.pop();
					cntComma++;
				}
				else {
					throw new SyntacticException();
				}			
			}
			//peek()方法用于从此Stack中返回顶部元素，并且它不删除就检索元素。
			tempOperator = operator.peek();
		}
		tempOperator = operator.peek();
		if (tempOperator.getType().equals("Function")) {
			doFunction(cntComma, ((FunctionToken)tempOperator).getValue());
			operator.pop();
		}		
	}

	/**
	 * 语法分析和执行语义动作
	 * @param expression 表达式字符串
	 * @return 表达式运算结果
	 * @throws ExpressionException
	 */
	public Double parsing(String expression) throws ExpressionException {
		Scanner scanner = new Scanner(expression);
		curToken = (token.Token)scanner.getNextToken();
		Boolean completed = false;
		int action = 0;
		int lableStackIndex;
		int lableReadIndex;
				
		while(!completed) {
			topToken = operator.peek();
			if (curToken.getType().equals("Boolean") || curToken.getType().equals("Decimal")) {
				operand.push(curToken);
				curToken = scanner.getNextToken();
				continue;
			}
			else {
				lableReadIndex = getIndex(getTag(curToken));
				lableStackIndex = getIndex(getTag(topToken));
				action = OPP.table[lableStackIndex][lableReadIndex];
				switch (action) {
					case OPP.ACCEPT:
						completed = true;
						break;
					case OPP.SHIFT:
						shift(curToken);
						curToken = scanner.getNextToken();
						break;
					case OPP.RD_UNIQUE_OPERATION:
						reduceUnary();
						break;
					case OPP.RD_BINARY_OPERATION:
						reduceBinary();
						break;
					case OPP.RD_TRINARY_OPERATION:
						reduceTrinary();
						break;
					case OPP.RD_BRACKET:
						matchReduce();
						curToken = scanner.getNextToken();
						break;
					case OPP.MISSING_LEFT_BRACKET:
						throw new MissingLeftParenthesisException();
					case OPP.SYNTACTIC_EXCEPTION:
						throw new SyntacticException();
					case OPP.MISSING_OPERAND:
						throw new MissingOperandException();
					case OPP.TYPE_ERROR:
						throw new TypeMismatchedException();
					case OPP.FUNCTION_ERROR:
						throw new FunctionCallException();
					case OPP.MISSING_RIGHT_BRACKET:
						throw new MissingRightParenthesisException();
					case OPP.TRINARY_OPERATION_ERROR:
						throw new TrinaryOperationException();
					default:
						break;
				}
			}
		}
		if (completed) {
			if (operand.size() == 1) {
				if (operand.peek().getType().equals("Decimal"))
					return ((DecimalToken)operand.peek()).getValue();
				else
					throw new TypeMismatchedException();
			}
			else {
				throw new MissingOperatorException();
			}
		}
		else {
			throw new SyntacticException();
		}		
	}
}