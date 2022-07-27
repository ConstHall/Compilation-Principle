package scanner;

/**
 * The token of digit, which is subclass of Token.
 * @author ZhiHan Jiang
 * @see Token
 */
public class TokenDigit extends Token {
	/**
	 * The private value of double Token. {@value}
	 */
	private Double value;

	/**
	 * The generator function of TokenDigit.
	 * 1. set the type to "Decimal"
	 * 2. get the double value of token
	 * @param strNum the string of number
	 */
	public TokenDigit(String strNum) {
		type = "Decimal";
		strNum = strNum.toLowerCase();
		int sci = strNum.indexOf('e');
		if (sci != -1) { // xex
			double frac = Double.parseDouble(strNum.substring(0, sci));
			double exp;
			switch (strNum.charAt(sci+1)) {
			case '-':
				exp = Double.parseDouble(strNum.substring(sci+2, strNum.length()));
				value = frac / Math.pow(10.0, exp);
				break;
			case '+':
				exp = Double.parseDouble(strNum.substring(sci+2, strNum.length()));
				value = frac * Math.pow(10.0, exp);
				break;
			default:
				exp = Double.parseDouble(strNum.substring(sci+1, strNum.length()));
				value = frac * Math.pow(10.0, exp);
				break;
			}
		}
		else value = Double.parseDouble(strNum);
		
	}

	/**
	 * The public function to get the double value.
	 * @return the double value
	 */
	public Double getValue() {
		return value;
	}

	/**
	 * The public function to set the double value.
	 * @param newValue  the new double value
	 */
	public void setValue(Double newValue) {
		value = newValue;
	}
}


