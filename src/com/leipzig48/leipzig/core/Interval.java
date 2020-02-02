/*
 * Created on Oct 23, 2004
 * 
 * Leipzig: A Just Intonation Library 
 * Copyright (C) 2004 Paul Reiners
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
 * 	2506 18 1/2 Ave NW
 * 	Apt 206
 * 	Rochester, MN  55901
 * 
 * 	paulreiners@earthlink.net
 */
package com.leipzig48.leipzig.core;

import java.math.BigInteger;

import com.leipzig48.leipzig.exceptions.InvalidIntervalException;

/**
 * @author Paul Reiners
 */
public class Interval implements Comparable {

	BigInteger numerator;

	BigInteger denominator;

	final static Interval PERFECT_FOURTH = SuperparticularInterval.PERFECT_FOURTH;

	public final static Interval PERFECT_FIFTH = SuperparticularInterval.PERFECT_FIFTH;

	final static Interval MINOR_THIRD = new Interval(6, 5);

	public final static Interval MAJOR_THIRD = new Interval(5, 4);

	final static Interval SEPTIMAL_SECOND = new Interval(8, 7);

	final static Interval SUPERMAJOR_SECOND = SEPTIMAL_SECOND;

	final static Interval SEPTIMAL_MINOR_THIRD = new Interval(7, 6);

	final static Interval SUBMINOR_THIRD = SEPTIMAL_MINOR_THIRD;

	final static Interval MAJOR_SIXTH = new Interval(5, 3);

	final static Interval MINOR_SIXTH = new Interval(8, 5);

	final static Interval HARMONIC_SEVENTH = new Interval(7, 4);

	final static Interval OCTAVE = SuperparticularInterval.OCTAVE;

	public final static Interval MIDDLE_C = new Interval(1, 1);

	final static int A_FREQ = 440;

	final static int C_FREQ = (int) Math.round(A_FREQ * (3.0 / 5.0));

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