package scanner;

/**
 * The token of Operator, which is subclass of Token.
 * @author ZhiHan Jiang
 * @see Token
 */
public class TokenOperator extends Token {
	/**
	 * The private value indicates the Token's labels. 
	 */
	protected String label;

	/**
	 * The private value indicates the name of function. 
	 */
	protected String name;

	/**
	 * The genarator function of TokenOperator.
	 * 1. set the type to "Operator"
	 * 2. set the name to strOperator
	 * 3. set the label of this operator
	 * @param strOperator the String of operator
	 */
	public TokenOperator(String strOperator) {
		type = "Operator";
		name = strOperator;
		if(strOperator.equals("+") || strOperator.equals("-") )
			label = "plus_minus";
		else if(strOperator.equals("*") || strOperator.equals("/"))
			label = "multiply_divid";
		else if (strOperator.equals("=") || strOperator.equals("<") || strOperator.equals(">") ||
				 strOperator.equals("<=") || strOperator.equals(">=") || strOperator.equals("<>"))
			label = "cmp";
		else if(strOperator.equals("minus"))
			label = "-";
		else label = strOperator;
	}

	/**
	 * The public function to get operator name.
	 * @return the name of operator
	 */
	public String getName() {
		return name;
	}	

	/**
	 * The public function to get the operator label.
	 * @return The label in OPP of operator.
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * The public function to get how many operands there are.
	 * @return the number of operands
	 */
	public int getNumber() {
		int num = 0;
				
		if (label.equals("-") || label.equals("!"))
			num = 1;
		else if (label.equals("&") || label.equals("|") || label.equals("cmp") ||
				 label.equals("plus_minus") || label.equals("multiply_divid") || label.equals("^"))
			num = 2;
		else if (label.equals("?") || label.equals(":"))
			num = 3;
		
		return num;			
	}
}
