package parser;

public class OPP {
	public static final int MISSING_LEFT_BRACKET = -7;	// 缺少左括号
	public static final int SYNTACTIC_EXCEPTION = -6;	// 语法错误
	public static final int MISSING_OPERAND = -5;	// 缺少操作数
	public static final int TYPE_ERROR = -4;	// 类型错误
	public static final int FUNCTION_ERROR = -3;	// 函数语法错误
	public static final int MISSING_RIGHT_BRACKET = -2;	// 缺少右括号
	public static final int TRINARY_OPERATION_ERROR = -1;	// 三元运算符异常
	public static final int SHIFT = 0;	// 移入
	public static final int RD_UNIQUE_OPERATION = 1;	// 单元运算
	public static final int RD_BINARY_OPERATION = 2;	// 双元运算
	public static final int RD_TRINARY_OPERATION = 3;	// 三元运算
	public static final int RD_BRACKET = 4;	// 括号运算
	public static final int ACCEPT = 5;	// 接受
	
	// md: 乘除运算 "multiple & divide"
	// pm: 加减运算 "plus & minus"
	// cmp: 关系运算 "compare"
	public static final int table[][] = {
           /* (  ) fun -  ^ md pm cmp !  &  |  ?  :  ,  $ */
	/*(*/    {0, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,-1, 0,-2},
	/*)*/    {4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4},
	/*fun*/  {0,-3,-3,-3,-3,-3,-3,-3,-3,-3,-3,-3,-3,-3,-3},
	/*-*/    {0, 4, 0, 0, 1, 1, 1, 1,-6,-4,-4, 1, 1, 1, 1},
	/*^*/    {0, 4, 0, 0, 0, 2, 2, 2,-6,-4,-4, 2, 2, 2, 2},
	/*md*/   {0, 4, 0, 0, 0, 2, 2, 2,-6,-4,-4, 2, 2, 2, 2},
	/*pm*/   {0, 4, 0, 0, 0, 0, 2, 2,-6,-4,-4, 2, 2, 2, 2},
	/*cmp*/  {0, 4, 0, 0, 0, 0, 0,-4,-6, 2, 2, 2,-1,-3, 2},
	/*!*/    {0, 4,-4,-4,-4,-4,-4, 0, 0, 1, 1, 1,-1,-3, 1},
	/*&*/    {0, 4,-4,-4,-4,-4,-4, 0, 0, 2, 2, 2,-1,-3, 2},
	/*|*/    {0, 4,-4,-4,-4,-4,-4, 0, 0, 0, 2, 2,-1,-3, 2},
	/*?*/    {0,-1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,-1,-1},
	/*:*/    {0, 4, 0, 0, 0, 0, 0,-1,-1,-1,-1, 0,-1,-1, 3},
	/*,*/    {0, 4, 0, 0, 0, 0, 0,-3,-3,-3,-3, 0,-1, 0,-3},
	/*$*/    {0,-7, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,-1,-3, 5}
	};
}
