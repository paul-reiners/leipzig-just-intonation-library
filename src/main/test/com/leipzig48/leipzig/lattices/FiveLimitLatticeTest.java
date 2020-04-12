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
package com.leipzig48.leipzig.lattices;

import junit.framework.TestCase;

import com.leipzig48.leipzig.core.Interval;
import com.leipzig48.leipzig.exceptions.InvalidIntervalException;

/**
 * @author Paul Reiners
 */
public class FiveLimitLatticeTest extends TestCase {

	public static void main(String[] args) {
		junit.textui.TestRunner.run(FiveLimitLatticeTest.class);
	}

	public void testGetNode() throws InvalidIntervalException {
		int xRadius = 4;
		int yRadius = 2;
		FiveLimitLattice lattice = new FiveLimitLattice(xRadius, yRadius);
		lattice.printOut();
		Interval[][] expectedNodes = {
				{ new Interval(100, 81), new Interval(50, 27),
						new Interval(25, 18), new Interval(25, 24),
						new Interval(25, 16), new Interval(75, 64),
						new Interval(225, 128), new Interval(675, 512),
						new Interval(2025, 1024) },
				{ new Interval(160, 81), new Interval(40, 27),
						new Interval(10, 9), new Interval(5, 3),
						new Interval(5, 4), new Interval(15, 8),
						new Interval(45, 32), new Interval(135, 128),
						new Interval(405, 1256) },
				{ new Interval(125, 81), new Interval(32, 27),
						new Interval(16, 9), new Interval(4, 3),
						new Interval(1, 1), new Interval(3, 2),
						new Interval(9, 8), new Interval(27, 16),
						new Interval(81, 64) },
				{ new Interval(512, 405), new Interval(256, 135),
						new Interval(64, 45), new Interval(16, 15),
						new Interval(8, 5), new Interval(6, 5),
						new Interval(9, 5), new Interval(27, 20),
						new Interval(81, 80) },
				{ new Interval(2048, 2025), new Interval(1024, 675),
						new Interval(256, 225), new Interval(128, 75),
						new Interval(32, 25), new Interval(48, 25),
						new Interval(36, 25), new Interval(27, 25),
						new Interval(81, 50) } };
		for (int y = -yRadius; y <= -yRadius; y++) {
			for (int x = -xRadius; x <= xRadius; x++) {
				assertEquals(expectedNodes[-y + yRadius][x + xRadius], lattice
						.getNode(x, y));
			}
		}
	}
}