package scanner;

/**
 * The token of boolean, which is subclass of Token.
 * @author ZhiHan Jiang
 * @see Token
 */
public class TokenBoolean extends Token {
	/**
	 * The boolean value of boolen Token. {@value}
	 */
	private Boolean value;

	/**
	 * The generator function of TokenBoolean.
	 * 1. set the type to "Boolean"
	 * 2. get the value of value
	 * @param val the String of Boolean value
	 */
	public TokenBoolean(String val) {
		type = "Boolean";
		value = val.toLowerCase().equals("true");
	}

	/**
	 * The public function to get the Boolean value.
	 * @return the boolean value
	 */
	public Boolean getValue() {
		return value;
	}

	/**
	 * The public function to set the Boolean value.
	 * @param newValue  the new boolean value
	 */
	public void setValue(Boolean newValue) {
		value = newValue;
	}
}
