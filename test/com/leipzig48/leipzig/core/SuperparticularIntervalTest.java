/*
 * Created on Dec 4, 2004
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

/**
 * @author Paul Reiners
 */
public class SuperparticularIntervalTest extends IntervalTest {

	public static void main(String[] args) {
		junit.textui.TestRunner.run(SuperparticularIntervalTest.class);
	}

	public void testDivideIntoSpecifiedNumberOfParts() {
		Interval[] parts = SuperparticularInterval.OCTAVE.divideIntoSpecifiedNumberOfParts(2);
		Interval[] expectedParts = { Interval.PERFECT_FOURTH,
				Interval.PERFECT_FIFTH };
		assertTrue(Arrays.equals(expectedParts, parts));

		parts = SuperparticularInterval.OCTAVE.divideIntoSpecifiedNumberOfParts(3);
		expectedParts = new Interval[] { Interval.MINOR_THIRD,
				Interval.MAJOR_THIRD, Interval.PERFECT_FOURTH };
		assertTrue(Arrays.equals(expectedParts, parts));

		parts = SuperparticularInterval.OCTAVE.divideIntoSpecifiedNumberOfParts(4);
		expectedParts = new Interval[] { Interval.SEPTIMAL_SECOND,
				Interval.SEPTIMAL_MINOR_THIRD, Interval.MINOR_THIRD,
				Interval.MAJOR_THIRD };
		assertTrue(Arrays.equals(expectedParts, parts));

		parts = SuperparticularInterval.PERFECT_FOURTH.divideIntoSpecifiedNumberOfParts(2);
		expectedParts = new Interval[] { Interval.SEPTIMAL_SECOND,
				Interval.SEPTIMAL_MINOR_THIRD };
		assertTrue(Arrays.equals(expectedParts, parts));

		parts = SuperparticularInterval.PERFECT_FIFTH.divideIntoSpecifiedNumberOfParts(2);
		expectedParts = new Interval[] { Interval.MINOR_THIRD,
				Interval.MAJOR_THIRD };
		assertTrue(Arrays.equals(expectedParts, parts));
	}
}
