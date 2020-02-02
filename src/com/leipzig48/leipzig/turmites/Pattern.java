/*
 * Created on Feb 18, 2005
 * 
 * Leipzig: A Just Intonation Library 
 * Copyright (C) 2005 Paul Reiners
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

/**
 * @author Paul Reiners
 */
public class Pattern {

	private static final int DEFAULT_Y_RADIUS = 128;

	private static final int DEFAULT_X_RADIUS = 128;

	private int[][] pattern;

	public Pattern() {
		this(DEFAULT_X_RADIUS, DEFAULT_Y_RADIUS);
	}

	public Pattern(int xRadius, int yRadius) {
		this.xRadius = xRadius;
		this.yRadius = yRadius;
		pattern = new int[2 * yRadius + 1][2 * xRadius + 1];
	}

	void setRadii(int newXRadius, int newYRadius) {
		xRadius = newXRadius;
		yRadius = newYRadius;
	}

	int getColor(int x, int y) {
		return pattern[getI(y)][getJ(x)];
	}

	void setColor(int x, int y, int c) {
		pattern[getI(y)][getJ(x)] = c;
	}

	/**
	 * @return
	 */
	private int getJ(int x) {
		int j = x + xRadius;
		return j;
	}

	/**
	 * @return
	 */
	private int getI(int y) {
		int i = -y + yRadius;
		return i;
	}

	private int xRadius;

	private int yRadius;

	/**
	 * @return Returns the xRadius.
	 */
	public int getXRadius() {
		return xRadius;
	}

	/**
	 * @return Returns the yRadius.
	 */
	public int getYRadius() {
		return yRadius;
	}
}