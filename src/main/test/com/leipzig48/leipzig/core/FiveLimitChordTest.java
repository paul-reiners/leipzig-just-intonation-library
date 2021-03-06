/*
 * Created on Dec 11, 2004
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
public class FiveLimitChordTest extends TestCase {

	private static final Interval[] MAJOR_TRIAD_INTERVALS = new Interval[] {
			new Interval(1, 1), new Interval(5, 4), new Interval(3, 2) };

	private static final Interval[] MINOR_TRIAD_INTERVALS = new Interval[] {
			new Interval(5, 4), new Interval(3, 2), new Interval(15, 8) };

	private static final Interval[][] CONDISSONANT_TRIADS_INTERVALS = {
			new Interval[] { new Interval(1, 1), new Interval(5, 4),
					new Interval(15, 8) },
			new Interval[] { new Interval(1, 1), new Interval(3, 2),
					new Interval(15, 8) } };

	private static final Interval[] MAJOR_SEVENTH_CHORD_INTERVALS = new Interval[] {
			new Interval(1, 1), new Interval(5, 4), new Interval(3, 2),
			new Interval(15, 8) };

	private static final Interval[] MINOR_SEVENTH_CHORD_INTERVALS = new Interval[] {
			new Interval(5, 4), new Interval(3, 2), new Interval(15, 8),
			new Interval(9, 8) };

	private static final Interval[] MAJOR_NINTH_CHORD_INTERVALS = new Interval[] {
			new Interval(1, 1), new Interval(5, 4), new Interval(3, 2),
			new Interval(15, 8), new Interval(9, 8) };

	private static final Interval[] MINOR_NINTH_CHORD_INTERVALS = new Interval[] {
			new Interval(5, 4), new Interval(3, 2), new Interval(15, 8),
			new Interval(9, 8), new Interval(45, 32) };

	public static void main(String[] args) {
		junit.textui.TestRunner.run(FiveLimitChordTest.class);
	}

	public void testGetIntervals() throws InvalidIntervalException {
		assertTrue(Arrays.equals(MAJOR_TRIAD_INTERVALS, FiveLimitChord.MAJOR_TRIAD
				.getIntervals()));
		assertTrue(Arrays.equals(MINOR_TRIAD_INTERVALS, FiveLimitChord.MINOR_TRIAD
				.getIntervals()));
		assertTrue(Arrays.equals(CONDISSONANT_TRIADS_INTERVALS[0],
				FiveLimitChord.CONDISSONANT_TRIADS[0].getIntervals()));
		assertTrue(Arrays.equals(CONDISSONANT_TRIADS_INTERVALS[1],
				FiveLimitChord.CONDISSONANT_TRIADS[1].getIntervals()));
		assertTrue(Arrays.equals(MAJOR_SEVENTH_CHORD_INTERVALS,
				FiveLimitChord.MAJOR_SEVENTH_CHORD.getIntervals()));
		assertTrue(Arrays.equals(MINOR_SEVENTH_CHORD_INTERVALS,
				FiveLimitChord.MINOR_SEVENTH_CHORD.getIntervals()));
		assertTrue(Arrays.equals(MAJOR_NINTH_CHORD_INTERVALS,
				FiveLimitChord.MAJOR_NINTH_CHORD.getIntervals()));
		assertTrue(Arrays.equals(MINOR_NINTH_CHORD_INTERVALS,
				FiveLimitChord.MINOR_NINTH_CHORD.getIntervals()));
	}
}