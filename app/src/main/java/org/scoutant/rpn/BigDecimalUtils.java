package org.scoutant.rpn;


import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Cortesy to Java/Android calculator https://github.com/lpar/RPN, GNU GPL v3 licence.
 */
public class BigDecimalUtils {

  /**
   * Computes the square root of x to a given scale, x >= 0.
   * Use Newton's algorithm.
   * Taken from "Java Number Cruncher: The Java Programmer's Guide to
   * Numerical Computing" (Ronald Mak, 2003) http://goo.gl/CXpi2
   * @param x the value of x
   * @param scale the desired scale of the result
   * @return the result value
   */
  public static BigDecimal sqrt(final BigDecimal x, final int scale) {
    // Check that x >= 0.
    if (x.signum() < 0) {
      throw new IllegalArgumentException("x < 0");
    }
    if (x.signum() == 0) {
      return BigDecimal.ZERO;
    }

    // n = x*(10^(2*scale))
    BigInteger n = x.movePointRight(scale << 1).toBigInteger();

    // The first approximation is the upper half of n.
    int bits = (n.bitLength() + 1) >> 1;
    BigInteger ix = n.shiftRight(bits);
    BigInteger ixPrev;

    // Loop until the approximations converge
    // (two successive approximations are equal after rounding).
    do {
      ixPrev = ix;

      // x = (x + n/x)/2
      ix = ix.add(n.divide(ix)).shiftRight(1);

      Thread.yield();
    } while (ix.compareTo(ixPrev) != 0);

    return new BigDecimal(ix, scale);
  }

  /**
   * Compute the power x^y to a the given scale, using doubles.
   * Loses some precision, but means y can have non integer values.
   */
 public static BigDecimal approxPow(final BigDecimal x, final BigDecimal y) {
    double d;

    // Check that |y| >= 1 for negative x.
    if (x.signum() < 0 && y.abs().doubleValue() < 1.0) {
      throw new IllegalArgumentException("|n| < 1");
    }
    // Check that y is positive or 0 for x = 0.
    else if (x.signum() == 0 && y.signum() < 0) {
      throw new IllegalArgumentException("n < 0");
    }

    d = Math.pow(x.doubleValue(), y.doubleValue());
    return new BigDecimal(d);
  }

}
