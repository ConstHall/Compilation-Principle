package token;

public class BooleanToken extends Token {
	protected Boolean value;
	/**
	 * 构造函数
	 * @param temp 输入的字符串
	 */
	public BooleanToken(String temp) {
		type = "Boolean";
		//转为全小写
		temp = temp.toLowerCase();
		//true
		if (temp.equals("true") )
			value = true;
		//false
		else {
			value = false;
		}
	}

	/**
	 * 获取布尔对象的
	 * @return 返回布尔对象的值(true或false)
	 */
	public Boolean getValue() {
		return value;
	}

	/**
	 * 设置布尔对象的值
	 * @param newValue 需要设置的布尔值
	 */
	public void setValue(Boolean newValue) {
		value = newValue;
	}
}