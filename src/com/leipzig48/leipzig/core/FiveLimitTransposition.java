/*
 * Created on Dec 13, 2004
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
public class FiveLimitTransposition {

	final public static FiveLimitTransposition FIFTH_UP = new FiveLimitTransposition(
			1, 0);

	final public static FiveLimitTransposition FIFTH_DOWN = new FiveLimitTransposition(
			-1, 0);

	final public static FiveLimitTransposition THIRD_UP = new FiveLimitTransposition(
			0, 1);

	final public static FiveLimitTransposition THIRD_DOWN = new FiveLimitTransposition(
			0, -1);

	private int dX;

	private int dY;

	FiveLimitTransposition(int dX, int dY) {
		this.dX = dX;
		this.dY = dY;
	}
	/**
	 * @return Returns the dX.
	 */
	public int getDX() {
		return dX;
	}
	/**
	 * @return Returns the dY.
	 */
	public int getDY() {
		return dY;
	}
}