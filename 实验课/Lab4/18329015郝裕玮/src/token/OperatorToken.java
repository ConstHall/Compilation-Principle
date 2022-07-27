package token;

public class OperatorToken extends Token {
	protected String value;
	protected String tag;

	/**
	 * 构造函数
	 * @param operator 输入的字符串
	 */
	public OperatorToken(String operator) {
		type = "Operator";
		value = operator;
		//加减运算plus and minus
		if(operator.equals("+") || operator.equals("-") ) {
			tag = "pm";
		}
		//乘除运算multiple and divide
		else if(operator.equals("*") || operator.equals("/")) {
			tag = "md";
		}
		//取负运算negative
		else if(operator.equals("neg")) {
			tag = "-";
		}
		//关系运算compare
		else if (operator.equals("=") || operator.equals("<") || operator.equals(">") || operator.equals("<=") || operator.equals(">=") || operator.equals("<>")) {
			tag = "cmp";
		}
		else {
			tag = operator;
		}
	}

	/**
	 * 获取操作符
	 * @return 操作符
	 */
	public String getValue() {
		return value;
	}

	/**
	 * 获取操作符在OPP表中的标记
	 * @return 操作符在OPP表中对应的tag
	 */
	public String getTag() {
		return tag;
	}

	/**
	 * 获取操作符是几元运算符
	 * @return 需要的操作数个数
	 */
	public int getNum() {
		int num = 0;
				
		if (tag.equals("-") || tag.equals("!")) {
			num = 1;
		}
		if (tag.equals("pm") || tag.equals("md") || tag.equals("^") || tag.equals("&") || tag.equals("|") || tag.equals("cmp")) {
			num = 2;
		}
		if (tag.equals("?") || tag.equals(":")) {
			num = 3;
		}
		
		return num;			
	}
}
