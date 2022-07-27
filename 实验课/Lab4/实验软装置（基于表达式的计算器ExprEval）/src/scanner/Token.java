package scanner;

/**
 * The super class of all Token.
 * @author ZhiHan Jiang
 */
public class Token {
	/**
	 * The protected value indicates type of Token,
	 * which includes {Boolen, Decimal, Function, Operator, Dollar}
	 */
	protected String type;

	/**
	 * The generator function of Token.
	 * set type to ""
	 */
	public Token() {
		type = "";
	}

	/**
	 * The public function to get the private String type.
	 * @return The type of Token.
	 */
	public String getType() {
		return type;
	}	
}
