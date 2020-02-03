/*
 * Created on Dec 14, 2004
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
package com.leipzig48.leipzig.turmites;

import junit.framework.TestCase;

/**
 * @author Paul Reiners
 */
public class TurmiteTest extends TestCase {

	public static void main(String[] args) {
		junit.textui.TestRunner.run(TurmiteTest.class);
	}

	public void testMove() {
		int[] expectedXPos = { 0, 0, 1, 1, 2, 2 };
		int[] expectedYPos = { 0, 1, 1, 2, 2, 3 };
		for (int i = 1; i < expectedXPos.length; i++) {
			Turmite.STAIRS.move();
			int curColor = Turmite.STAIRS.getColor(expectedXPos[i - 1],
					expectedYPos[i - 1]);
			assertEquals(1, curColor);
			int xPos = Turmite.STAIRS.getX();
			assertEquals(expectedXPos[i], xPos);
			int yPos = Turmite.STAIRS.getY();
			assertEquals(expectedYPos[i], yPos);
		}
	}
}