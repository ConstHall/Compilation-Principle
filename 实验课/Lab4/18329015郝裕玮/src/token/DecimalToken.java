package token;

public class DecimalToken extends Token {
	protected Double value;
	/**
	 * 构造函数
	 * @param num 输入的数字字符串
	 */
	public DecimalToken(String num) {
		type = "Decimal";
		//科学计数法
		int E = num.indexOf('e');
		if (E != -1) {
			double fraction = Double.parseDouble(num.substring(0, E));
			double exp;
			switch (num.charAt(E+1)) {
			case '-':
				exp = Double.parseDouble(num.substring(E+2, num.length()));
				value = fraction / Math.pow(10.0, exp);
				break;
			case '+':
				exp = Double.parseDouble(num.substring(E+2, num.length()));
				value = fraction * Math.pow(10.0, exp);
				break;
			default:
				exp = Double.parseDouble(num.substring(E+1, num.length()));
				value = fraction * Math.pow(10.0, exp);
				break;
			}
		}
		else {
			value = Double.parseDouble(num);
		}
	}

	/**
	 * 获取Decimal的值
	 * @return Decimal的值
	 */
	public Double getValue() {
		return value;
	}

	/**
	 * 设置Decimal的值
	 * @param newValue 需要设定的Decimal值
	 */
	public void setValue(Double newValue) {
		value = newValue;
	}
}