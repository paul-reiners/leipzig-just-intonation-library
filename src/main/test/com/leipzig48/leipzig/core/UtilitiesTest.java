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

import junit.framework.TestCase;

/**
 * @author Paul Reiners
 */
public class UtilitiesTest extends TestCase {

	public static void main(String[] args) {
		junit.textui.TestRunner.run(UtilitiesTest.class);
	}

	public void testGCD() {
		assertEquals(1, Utilities.gCD(3, 5));
		assertEquals(12, Utilities.gCD(12, 60));
		assertEquals(6, Utilities.gCD(12, 90));
	}

	public void testIsPrime() {
		assertFalse(Utilities.isPrime(1));
		assertTrue(Utilities.isPrime(2));
		assertTrue(Utilities.isPrime(3));
		assertFalse(Utilities.isPrime(4));
		assertTrue(Utilities.isPrime(5));
		assertFalse(Utilities.isPrime(6));
		assertTrue(Utilities.isPrime(7));
		assertFalse(Utilities.isPrime(8));
	}

	public void testIsPerfectPower() {
		assertTrue(Utilities.isPerfectPower(1, 2));
		assertTrue(Utilities.isPerfectPower(4, 2));
		assertTrue(Utilities.isPerfectPower(9, 2));
		assertFalse(Utilities.isPerfectPower(2, 2));
		assertFalse(Utilities.isPerfectPower(5, 2));
		assertFalse(Utilities.isPerfectPower(10, 2));

		assertTrue(Utilities.isPerfectPower(1, 3));
		assertTrue(Utilities.isPerfectPower(8, 3));
		assertTrue(Utilities.isPerfectPower(27, 3));
		assertFalse(Utilities.isPerfectPower(2, 2));
		assertFalse(Utilities.isPerfectPower(5, 2));
		assertFalse(Utilities.isPerfectPower(10, 2));
	}
	
	public void testFactorOut2s() {
		assertEquals(5, Utilities.factorOut2s(10));
		assertEquals(3, Utilities.factorOut2s(12));
		assertEquals(15, Utilities.factorOut2s(15));
	}
}