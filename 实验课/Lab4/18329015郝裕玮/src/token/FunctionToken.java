package token;

public class FunctionToken extends Token {
	protected String value;
	protected String tag;

	/**
	 * 构造函数
	 * @param func 输入的字符串
	 */
	public FunctionToken(String func) {
		type = "Function";
		//值为函数名的全小写字符串
		value = func.toLowerCase();
		tag = "func";
	}

	/**
	 * 获取函数名
	 * @return 返回函数名
	 */
	public String getValue() {
		return value;
	}

	/**
	 * 获取词法单元在OPP表中的标记
	 * @return 词法单元在OPP表中对应的tag
	 */
	public String getTag() {
		return tag;
	}
}