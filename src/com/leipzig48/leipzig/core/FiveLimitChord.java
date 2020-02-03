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

import com.leipzig48.leipzig.exceptions.InvalidIntervalException;
import com.leipzig48.leipzig.lattices.FiveLimitLattice;

/**
 * @author Paul Reiners
 */
public class FiveLimitChord {

	public static final FiveLimitChord MAJOR_TRIAD = new FiveLimitChord(
			new int[][] { new int[] { 0, 0 }, new int[] { 0, 1 },
					new int[] { 1, 0 } });

	public static final FiveLimitChord MINOR_TRIAD = new FiveLimitChord(
			new int[][] { new int[] { 0, 1 }, new int[] { 1, 0 },
					new int[] { 1, 1 } });

	public static final FiveLimitChord[] CONDISSONANT_TRIADS = {
			new FiveLimitChord(new int[][] { new int[] { 0, 0 },
					new int[] { 0, 1 }, new int[] { 1, 1 } }),
			new FiveLimitChord(new int[][] { new int[] { 0, 0 },
					new int[] { 1, 0 }, new int[] { 1, 1 } }) };

	public static final FiveLimitChord MAJOR_SEVENTH_CHORD = new FiveLimitChord(
			new int[][] { new int[] { 0, 0 }, new int[] { 0, 1 },
					new int[] { 1, 0 }, new int[] { 1, 1 } });

	public static final FiveLimitChord MINOR_SEVENTH_CHORD = new FiveLimitChord(
			new int[][] { new int[] { 0, 1 }, new int[] { 1, 0 },
					new int[] { 1, 1 }, new int[] { 2, 0 } });

	public static final FiveLimitChord MAJOR_NINTH_CHORD = new FiveLimitChord(
			new int[][] { new int[] { 0, 0 }, new int[] { 0, 1 },
					new int[] { 1, 0 }, new int[] { 1, 1 }, new int[] { 2, 0 } });

	public static final FiveLimitChord MINOR_NINTH_CHORD = new FiveLimitChord(
			new int[][] { new int[] { 0, 1 }, new int[] { 1, 0 },
					new int[] { 1, 1 }, new int[] { 2, 0 }, new int[] { 2, 1 } });

	private Interval[] intervals;

	private int[][] displacements;

	FiveLimitChord(int[][] displacements) {
		this.displacements = displacements;
	}

	public Interval[] getIntervals() throws InvalidIntervalException {
		if (intervals == null) {
			initializeIntervals();
		}

		return intervals;
	}

	private void initializeIntervals() throws InvalidIntervalException {
		int maxX = 0;
		int maxY = 0;
		for (int i = 0; i < displacements.length; i++) {
			int x = displacements[i][0];
			if (x < 0) {
				x = -x;
			}
			if (x > maxX) {
				maxX = x;
			}
			int y = displacements[i][1];
			if (y < 0) {
				y = -y;
			}
			if (y > maxY) {
				maxY = y;
			}
		}

		FiveLimitLattice lattice = new FiveLimitLattice(maxX, maxY);
		intervals = new Interval[displacements.length];
		for (int i = 0; i < intervals.length; i++) {
			intervals[i] = lattice.getNode(displacements[i][0],
					displacements[i][1]);
		}
	}

	/**
	 * @param rootX
	 * @param rootY
	 * @return
	 */
	public FiveLimitChord transpose(int dX, int dY) {
		int[][] transposedDisplacements = new int[displacements.length][2];
		for (int i = 0; i < displacements.length; i++) {
			transposedDisplacements[i][0] = displacements[i][0] + dX;
			transposedDisplacements[i][1] = displacements[i][1] + dY;
		}
		
		return new FiveLimitChord(transposedDisplacements);
	}
	/**
	 * @return Returns the displacements.
	 */
	public int[][] getDisplacements() {
		return displacements;
	}
}