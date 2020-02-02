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

/**
 * @author Paul Reiners
 */
class Utilities {

	static long gCD(long a, long b) {
		long aa = a;
		long bb = b;

		if (aa < bb) {
			long temp = aa;
			aa = bb;
			bb = temp;
		}

		if (aa % bb == 0) {
			return bb;
		}
		long r = aa % bb;
		aa = bb;
		bb = r;

		return gCD(aa, bb);
	}

	static boolean isPrime(int n) {
		if (n == 1) {
			return false;
		}
		for (int i = 2; i <= Math.sqrt(n); i++) {
			if (n % i == 0) {
				return false;
			}
		}

		return true;
	}

	/**
	 * @param n
	 * @param power
	 * @return
	 */
	static boolean isPerfectPower(long n, int power) {
		int root = (int) Math.round(Math.pow(n, 1.0 / power));
		int rootToPower = 1;
		for (int i = 0; i < power; i++) {
			rootToPower *= root;
		}

		return rootToPower == n;
	}

	static int factorOut2s(int n) {
		int nn = n;
		while (nn % 2 == 0) {
			nn /= 2;
		}

		return nn;
	}
}