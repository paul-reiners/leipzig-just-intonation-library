/*
 * Created on Oct 23, 2004
 * 
 * Leipzig: A Just Intonation Library 
 * Copyright (C) 2004, 2020 Paul Reiners
 * 
 * This library is free software; you can redistribute it and/or modify it under the 
 * terms of the GNU Lesser General Public License as published by the Free Software 
 * Foundation; either version 2.1 of the License, or (at your option) any later 
 * version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY 
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along 
 * with this library; if not, write to the Free Software Foundation, Inc., 59 Temple 
 * Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 * Contact Info:
 * 
 * 	Paul Reiners
 * 	4319 Bryant Ave S
 * 	Apt C0001
 * 	Minneapolis, MN  55409
 * 
 * 	paul.reiners@gmail.com
 */

/**
 * @author Paul Reiners
 */
public class Interval implements Comparable {

	BigInteger numerator;

	BigInteger denominator;

	SuperparticularInterval.PERFECT_FOURTH => static Interval PERFECT_FOURTH;

	SuperparticularInterval.PERFECT_FIFTH => static Interval PERFECT_FIFTH;

	Interval(6, 5) => static Interval MINOR_THIRD;

	Interval(5, 4) => static Interval MAJOR_THIRD;

	Interval(8, 7) => static Interval SEPTIMAL_SECOND;

	SEPTIMAL_SECOND => static Interval SUPERMAJOR_SECOND;

	Interval(7, 6) => static Interval SEPTIMAL_MINOR_THIRD;

	SEPTIMAL_MINOR_THIRD => static Interval SUBMINOR_THIRD;

	Interval(5, 3) => static Interval MAJOR_SIXTH;

	Interval(8, 5) => static Interval MINOR_SIXTH;

	Interval(7, 4) => static Interval HARMONIC_SEVENTH;

	SuperparticularInterval.OCTAVE => static Interval OCTAVE;

	Interval(1, 1) => static Interval MIDDLE_C;

	440 => static int A_FREQ;

	Math.round(A_FREQ * (3.0 / 5.0)) => static int C_FREQ;

	public Interval(long numerator, long denominator) {
		this(new BigInteger(Long.toString(numerator)), new BigInteger(Long
				.toString(denominator)));
	}

	Interval() {
	}

	/**
	 * @param newNum
	 * @param newDenom
	 */
	public Interval(BigInteger newNum, BigInteger newDenom) {
		numerator = newNum;
		denominator = newDenom;

		BigInteger gCD = this.numerator.gcd(this.denominator);
		this.numerator = this.numerator.divide(gCD);
		this.denominator = this.denominator.divide(gCD);
	}

	public boolean equals(Object o) {
		if (o instanceof Interval) {
			Interval other = (Interval) o;

			return other.numerator.compareTo(numerator) == 0
					&& other.denominator.compareTo(denominator) == 0;
		}

		return false;
	}

	public String toString() {
		return "" + numerator + ":" + denominator;
	}

	public Interval add(Interval interval) {
		BigInteger newNum = numerator.multiply(interval.numerator);
		BigInteger newDenom = denominator.multiply(interval.denominator);

		Interval sum = new Interval(newNum, newDenom);
		while (sum.numerator.compareTo(sum.denominator.multiply(new BigInteger(
				"2"))) > 0) {
			sum = sum.subtract(Interval.OCTAVE);
		}

		return sum;
	}

	/**
	 * Subtracts <code>interval</code> from <code>this</code>.
	 * 
	 * @param interval
	 *            the interval to be subtracted.
	 * @return a new interval resulting from the subtraction.
	 * @throws InvalidIntervalException
	 */
	public Interval subtract(Interval interval) {
		BigInteger newNum = numerator.multiply(interval.denominator);
		BigInteger newDenom = denominator.multiply(interval.numerator);
		while (newDenom.compareTo(newNum) > 0) {
			newNum = newNum.multiply(new BigInteger("2"));
		}

		return new Interval(newNum, newDenom);
	}

	/**
	 * Calculates the complement.
	 * 
	 * @return the complement of <code>this</code>.
	 * @throws InvalidIntervalException
	 */
	Interval complement() throws InvalidIntervalException {
		if (numerator.compareTo(denominator) >= 0) {
			return OCTAVE.subtract(this);
		}

		return invert().complement().invert();
	}

	Interval invert() {
		return new Interval(denominator, numerator);
	}

	/**
	 * @return
	 */
	double toCents() {
		return Math.log(numerator.doubleValue() / denominator.doubleValue())
				* (1200.0 / Math.log(2.0));
	}

	boolean isSuperparticular() {
		return numerator.compareTo(denominator.add(BigInteger.ONE)) == 0
				|| denominator.compareTo(numerator.add(BigInteger.ONE)) == 0;
	}

	Interval[] divideIntoParts(long upper, long lower) {
		long cnt = upper - lower;
		Interval[] parts = new Interval[(int) cnt];
		for (int i = 0; i < cnt; i++) {
			parts[i] = new Interval(upper - i, upper - i - 1);
		}

		return parts;
	}

	/**
	 * @return
	 */
	Interval[] divideIntoParts() {
		return divideIntoParts(numerator.longValue(), denominator.longValue());
	}

	/**
	 * @param multiplier
	 * @return
	 */
	Interval[] divideIntoParts(int multiplier) {
		return divideIntoParts(multiplier * numerator.longValue(), multiplier
				* denominator.longValue());
	}

	/**
	 * @param multiplier
	 * @param limit
	 * @return
	 */
	Interval[] divideIntoParts(int multiplier, PrimeLimit limit) {
		int cnt = 0;
		long upper = multiplier * numerator.longValue();
		long lower = multiplier * denominator.longValue();
		for (long i = upper; i >= lower; i--) {
			if (limit.limits(i)) {
				cnt++;
			}
		}

		long[] series = new long[cnt];
		int index = 0;
		for (long i = upper; i >= lower; i--) {
			if (limit.limits(i)) {
				series[index++] = i;
			}
		}

		Interval[] parts = new Interval[cnt - 1];
		for (int i = 0; i < series.length - 1; i++) {
			parts[i] = new Interval(series[i], series[i + 1]);
		}

		return parts;
	}

	/**
	 * @param parts
	 * @return
	 */
	boolean isCapableOfEqualDivision(int parts) {
		return Utilities.isPerfectPower(numerator.longValue(), parts)
				&& Utilities.isPerfectPower(denominator.longValue(), parts);
	}

	/**
	 * @return
	 */
	public double getFrequency() {
		return (double) C_FREQ * numerator.doubleValue()
				/ denominator.doubleValue();
	}

	/**
	 * @return Returns the denominator.
	 */
	public BigInteger getDenominator() {
		return denominator;
	}

	/**
	 * @return Returns the numerator.
	 */
	public BigInteger getNumerator() {
		return numerator;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Object obj) {
		return (int) Math.round(getFrequency()
				- ((Interval) obj).getFrequency());
	}
}