package scanner;

/**
 * The token of dollar, which is subclass of Token.
 * @author ZhiHan Jiang
 * @see Token
 */
public class TokenDollar extends Token {
	/**
	 * The generator function of Token.
	 * set type to "TokenDollar"
	 */
	public TokenDollar() {
		type = "TokenDollar";
	}

	/**
	 * The public function to get the end dollar.
	 * @return the String of "$"
	 */
	public String getName() {
		return "$";
	}

	/**
	 * The public function to get the end dollar label in OPP.
	 * @return the String of "$"
	 */
	public String getLabel() {
		return "$";
	}
	
}
