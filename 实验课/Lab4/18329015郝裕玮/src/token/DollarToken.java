package token;

public class DollarToken extends Token {
	protected String tag;

	/**
	 * 构造函数
	 */
	public DollarToken() {
		type = "Dollar";
		tag = "$";
	}

	/**
	 * 获取终结符在OPP表中的标记。
	 * @return 终结符在OPP表中的标记
	 */
	public String getTag() {
		return tag;
	}
	
}
