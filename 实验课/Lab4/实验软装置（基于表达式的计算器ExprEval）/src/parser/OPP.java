package parser;


/**
 * This class is to store the OPP table and define the action in OPP.
 * @author 18329015郝裕玮
 */
public class OPP {
	/**
	 * The static value indicate accept {@value}
	 */
	public static final int ACCEPT = 0; 

	/**
	 * The static value indicate shift {@value}
	 */
	public static final int SHIFT = 1;

	/**
	 * The static value indicate unary operation reduce, includes '-' and '!' {@value}
	 */
	public static final int REDUCE1 = 2;

	/**
	 * The static value indicate binary operation reduce {@value}
	 */
	public static final int REDUCE2 = 3;

	/**
	 * The static value indicate three unary operation reduce {@value}
	 */
	public static final int REDUCE3 = 4; 

	/**
	 * The static value indicate bracket operation reduce {@value}
	 */
	public static final int BRACETREDUCE = 5;

	/**
	 * The static value indicate error about three unary operation {@value}
	 */
	public static final int ERRORTHREE = -1;

	/**
	 * The static value indicate error about missing right bracket {@value}
	 */
	public static final int ERRORRIGHT = -2;

	/**
	 * The static value indicate error about function {@value}
	 */
	public static final int ERRORFUNC = -3;

	/**
	 * The static value indicate error about type {@value}
	 */
	public static final int ERRORTYPE = -4;

	/**
	 * The static value indicate error about missing operand {@value}
	 */
	public static final int ERROROP = -5;

	/**
	 * The static value indicate error about grammer {@value}
	 */
	public static final int ERRORGRAM = -6;

	/**
	 * The static value indicate error about missing left bracket {@value}
	 */
	public static final int ERRORLEFT = -7;

	/**
	 * The static two-dimensional array indicate the OPP parser OPP_TABLE.
	 * OPP_TABLE[i][j] indicates that if currenct top symbol of stack's label is i,
	 * 	and current read symbol's label is j, which action should be choosen. 
	 */
	public static int OPP_TABLE[][] = {
			    /*(  ) func - ^  md pm cmp ! &  |  ?  :  ,  $*/
		/*(*/    {1, 5, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,-1, 1,-2},
		/*)*/    {5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5},
		/*func*/ {1,-3,-3,-3,-3,-3,-3,-3,-3,-3,-3,-3,-3,-3,-3},
		/*-*/    {1, 5, 1, 1, 2, 2, 2, 2,-6,-4,-4, 2, 2, 2, 2},
		/*^*/    {1, 5, 1, 1, 1, 3, 3, 3,-6,-4,-4, 3, 3, 3, 3},
		/*md*/   {1, 5, 1, 1, 1, 3, 3, 3,-6,-4,-4, 3, 3, 3, 3},
		/*pm*/   {1, 5, 1, 1, 1, 1, 3, 3,-6,-4,-4, 3, 3, 3, 3},
		/*cmp*/  {1, 5, 1, 1, 1, 1, 1,-4,-6, 3, 3, 3,-1,-3, 3},
		/*!*/    {1, 5,-4,-4,-4,-4,-4, 1, 1, 2, 2, 2,-1,-3, 2},
		/*&*/    {1, 5,-4,-4,-4,-4,-4, 1, 1, 3, 3, 3,-1,-3, 3},
		/*|*/    {1, 5,-4,-4,-4,-4,-4, 1, 1, 1, 3, 3,-1,-3, 3},
		/*?*/    {1,-1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,-1,-1},
		/*:*/    {1, 5, 1, 1, 1, 1, 1, 1,-1,-1,-1, 1,-1,-1, 4},
		/*,*/    {1, 5, 1, 1, 1, 1, 1,-3,-3,-3,-3, 1,-1, 1,-3},
		/*$*/    {1,-7, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,-1,-3, 0}
	};
}
