package agenda;

/**
 * Exception类：处理意外情况
 */

class Exception extends RuntimeException {
	//错误信息
	private String error;

	public Exception() { }

	/**
	 * 生成错误信息
	 * @param 错误信息message
	 */	
	public Exception(String message) {
		error = message;
	}

	/**
	 * 获取意外信息
	 * @return 返回错误信息
	 */
	public String getException() {
		return error;
	}
}