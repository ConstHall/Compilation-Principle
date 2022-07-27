package scanner;

/**
 * The token of Function, which is subclass of Token.
 * @author ZhiHan Jiang
 * @see Token
 */
public class TokenFunction extends Token {
	/**
	 * The private value indicates the Token's labels. {@value}
	 */
	private String label;

	/**
	 * The private value indicates the name of function. {@value}
	 */
	private String name;
	
	/**
	 * The genarator function of TokenFunction.
	 * 1. set the type to "Function"
	 * 2. set the name with String func
	 * 3. set the label to "func"
	 * @param func the String of function
	 */
	public TokenFunction(String func) {
		type = "Function";
		name = func.toLowerCase();
		label = "func";
	}

	/**
	 * The public function to get function name.
	 * @return the name of function
	 */
	public String getName() {
		return name;
	}

	/**
	 * The public function to get the function label.
	 * @return The label in OPP of function.
	 */
	public String getLabel() {
		return label;
	}
}
