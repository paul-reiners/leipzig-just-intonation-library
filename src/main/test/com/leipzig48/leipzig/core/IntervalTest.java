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

import java.util.Arrays;

import com.leipzig48.leipzig.exceptions.InvalidIntervalException;

import junit.framework.TestCase;

/**
 * @author Paul Reiners
 */
public class IntervalTest extends TestCase {

	public static void main(String[] args) {
		junit.textui.TestRunner.run(IntervalTest.class);
	}

	protected Interval threeToTwo;

	protected Interval nineToEight;

	protected Interval fourToThree;

	protected Interval twentySevenToSixteen;

	protected Interval tenToNine;

	protected Interval sixToFive;

	protected void setUp() {
		threeToTwo = new Interval(3, 2);
		nineToEight = new Interval(9, 8);
		fourToThree = new Interval(4, 3);
		twentySevenToSixteen = new Interval(27, 16);
		tenToNine = new Interval(10, 9);
		sixToFive = new Interval(6, 5);
	}

	public void testAdd() throws InvalidIntervalException {
		assertEquals(twentySevenToSixteen, threeToTwo.add(nineToEight));

		Interval twoToOne = new Interval(2, 1);
		assertEquals(twoToOne, threeToTwo.add(fourToThree));

		Interval sevenToFour = new Interval(7, 4);
		Interval twentyOneToSixteen = new Interval(21, 16);
		assertEquals(twentyOneToSixteen, threeToTwo.add(sevenToFour));
	}

	public void testSubtract() throws InvalidIntervalException {
		assertEquals(fourToThree, threeToTwo.subtract(nineToEight));
		assertEquals(nineToEight, threeToTwo.subtract(fourToThree));
		assertEquals(fourToThree, nineToEight.subtract(twentySevenToSixteen));
	}

	public void testComplement() throws InvalidIntervalException {
		assertEquals(fourToThree, threeToTwo.complement());

		Interval twoToThree = new Interval(2, 3);
		Interval threeToFour = new Interval(3, 4);
		assertEquals(threeToFour, twoToThree.complement());

		Interval fourToFive = new Interval(4, 5);
		Interval fiveToEight = new Interval(5, 8);
		assertEquals(fiveToEight, fourToFive.complement());

		Interval fiveToFour = new Interval(5, 4);
		Interval eightToFive = new Interval(8, 5);
		assertEquals(eightToFive, fiveToFour.complement());

		Interval fiveToSix = new Interval(5, 6);
		Interval threeToFive = new Interval(3, 5);
		assertEquals(threeToFive, fiveToSix.complement());

		Interval fiveToThree = new Interval(5, 3);
		assertEquals(fiveToThree, sixToFive.complement());

		Interval eightToFifteen = new Interval(8, 15);
		Interval fifteenToSixteen = new Interval(15, 16);
		assertEquals(fifteenToSixteen, eightToFifteen.complement());

		Interval fifteenToEight = new Interval(15, 8);
		Interval sixteenToFifteen = new Interval(16, 15);
		assertEquals(sixteenToFifteen, fifteenToEight.complement());
	}

	public void testToCents() {
		assertEquals(701.95, threeToTwo.toCents(), 0.01);
	}

	public void testDivideIntoPartsWithLimit() {
		Interval[] parts = SuperparticularInterval.OCTAVE.divideIntoParts(5,
				PrimeLimit.FIVE_LIMIT);
		Interval[] expectedParts = new Interval[] { tenToNine, nineToEight,
				fourToThree, sixToFive };
		assertTrue(Arrays.equals(expectedParts, parts));

		parts = SuperparticularInterval.OCTAVE.divideIntoParts(6,
				PrimeLimit.FIVE_LIMIT);
		expectedParts = new Interval[] { sixToFive, tenToNine, nineToEight,
				fourToThree };
		assertTrue(Arrays.equals(expectedParts, parts));
	}

	public void testDivideIntoParts() {
		Interval[] parts = Interval.MAJOR_SIXTH.divideIntoParts(2);
		Interval[] expectedParts = { tenToNine, nineToEight,
				Interval.SEPTIMAL_SECOND, Interval.SEPTIMAL_MINOR_THIRD };
		assertTrue(Arrays.equals(expectedParts, parts));
	}

	public void testDivideIntoPartsNonSuperparticular() {
		Interval[] parts = Interval.MAJOR_SIXTH.divideIntoParts();
		Interval[] expectedParts = { Interval.MAJOR_THIRD,
				Interval.PERFECT_FOURTH };
		assertTrue(Arrays.equals(expectedParts, parts));

		parts = Interval.MINOR_SIXTH.divideIntoParts();
		expectedParts = new Interval[] { Interval.SEPTIMAL_SECOND,
				Interval.SEPTIMAL_MINOR_THIRD, Interval.MINOR_THIRD };
		assertTrue(Arrays.equals(expectedParts, parts));

		parts = Interval.HARMONIC_SEVENTH.divideIntoParts();
		expectedParts = new Interval[] { Interval.SEPTIMAL_MINOR_THIRD,
				Interval.MINOR_THIRD, Interval.MAJOR_THIRD };
		assertTrue(Arrays.equals(expectedParts, parts));
	}

	public void testIsCapableOfEqualDivision() {
		for (int i = 1; i <= 5; i++) {
			assertTrue(new Interval((i + 1) * (i + 1), i * i)
					.isCapableOfEqualDivision(2));
		}
		for (int i = 1; i <= 3; i++) {
			assertTrue(new Interval((i + 1) * (i + 1) * (i + 1), i * i * i)
					.isCapableOfEqualDivision(3));
		}
		Interval[] interestingIntervals = { Interval.PERFECT_FIFTH,
				Interval.PERFECT_FOURTH, Interval.MAJOR_THIRD,
				Interval.MINOR_THIRD };
		for (int i = 0; i < interestingIntervals.length; i++) {
			assertFalse(interestingIntervals[i].isCapableOfEqualDivision(2));
			assertFalse(interestingIntervals[i].isCapableOfEqualDivision(3));
		}
	}
	
	public void testGetFrequency() {
		Interval middleC = new Interval(1, 1);
		assertEquals(264, (int) Math.round(middleC.getFrequency()));
		
		Interval perfectFifthAboveC = new Interval(3, 2);
		assertEquals(396, (int) Math.round(perfectFifthAboveC.getFrequency()));
		
		Interval majorTenthAboveC = new Interval(5, 2);
		assertEquals(660, (int) Math.round(majorTenthAboveC.getFrequency()));
		
		Interval perfectFifthBelowC = new Interval(2, 3);
		assertEquals(176, (int) Math.round(perfectFifthBelowC.getFrequency()));
	}
}