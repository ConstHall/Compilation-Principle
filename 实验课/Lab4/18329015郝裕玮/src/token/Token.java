package token;

public class Token {
	protected String type;

	/**
	 * 构造函数
	 */
	public Token() {
		type = "";
	}

	/**
	 * 获取词法单元的类型
	 * @return 词法单元类型
	 */
	public String getType() {
		return type;
	}
}
