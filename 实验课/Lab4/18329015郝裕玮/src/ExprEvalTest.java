import static org.junit.Assert.*;
import org.junit.Test;

import parser.Calculator;
import exceptions.*;

public class ExprEvalTest {
	private Calculator c = new Calculator();
	private final double accurancy = 0.00001;
	
	@Test
	public void test1() throws Exception {
		try {
			c.calculate("(2 + 3) ^ 3) - ((1 + 1)");
		} catch (Exception e) {
			assertTrue(e instanceof MissingLeftParenthesisException);
		}
	}
	
	@Test
	public void test2() throws Exception {
		assertTrue(Math.abs(c.calculate("9 - 3 * 2") - 3) <= accurancy);
	}
	
	@Test
	public void test3() throws Exception {
		assertTrue(Math.abs(c.calculate("2.25E+2 - (55.5 + 4 * (10 / 2) ^ 2)") - 69.5) <= accurancy);
	}
	
	@Test
	public void test4() throws Exception {
		assertTrue(Math.abs(c.calculate("65 / 5 - 130e-1")) <= accurancy);
	}
	
	
	@Test
	public void test5() throws Exception {
		assertTrue(Math.abs(c.calculate("(5 > 3) & (4 < 8) ? 15 : 16") - 15) <= accurancy);
	}
	
	@Test
	public void test6() throws Exception {
		assertTrue(Math.abs(c.calculate("max(1,2) + sin(1)^2 + cos(min(1,2))^2") - 3) <= accurancy);
	}
	
	@Test
	public void test7() throws Exception {
		try {
			c.calculate("(2 + 3) ^ 3) - ((1 + 1)");
		} catch (Exception e) {
			assertTrue(e instanceof MissingLeftParenthesisException);
		}
	}

	@Test
	public void test8() throws Exception {
		try {
			c.calculate("(1 + 2) ^ (3 - 4) 5");
		} catch (Exception e) {
			assertTrue(e instanceof MissingOperatorException);
		}
	}
	
	@Test
	public void test9() throws Exception {
		try {
			c.calculate("(1 + 2) ^ (3 - ) + 5");
		} catch (Exception e) {
			assertTrue(e instanceof MissingOperandException);
		}
	}
	
	@Test
	public void test10() throws Exception {
		try {
			c.calculate("4 / (12 - 3 * 4) + 1");
		} catch (Exception e) {
			assertTrue(e instanceof DividedByZeroException);
		}
	}
	
	@Test
	public void test11() throws Exception {
		try {
			c.calculate("(13 < 2 * 5) + 12");
		} catch (Exception e) {
			assertTrue(e instanceof TypeMismatchedException);
		}
	}
	
	@Test
	public void test12() throws Exception {
		assertTrue(Math.abs(c.calculate("-9 - 3 * 2") + 15) <= accurancy);
	}
}
