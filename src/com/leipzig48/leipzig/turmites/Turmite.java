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

/**
 * Based on A&#46;K&#46; Dewdney's and Rudy Rucker's writings on Turmites.
 * 
 * @author Paul Reiners
 */
class Turmite implements Cloneable {

	private static final int DIR_CNT = 4;

	final static Turmite STAIRS = new Turmite(
			new int[][] { { 1, 1 }, { 1, 1 } }, new int[][] { { 1, 1 },
					{ 0, 0 } }, new int[][] { { 1, 0 }, { 3, 3 } });

	final static Turmite TURK_PATTERN = new Turmite(new int[][] { { 1, 0 }, },
			new int[][] { { 0, 0 } }, new int[][] { { 3, 1 } });

	final static Turmite MULTICOLORED = new Turmite(
			new int[][] { { 1, 2, 3, 0 } }, new int[][] { { 0, 0, 0, 0 } },
			new int[][] { { 1, 1, 3, 3 } });

	final static int[][][][] ED_PEGG_JRS_TURMITES = {
			{ { { 1, 1, 0 }, { 0, -1, 0 } } },
			{ { { 0, -1, 1 }, { 1, 1, 1 } }, { { 1, 0, 0 }, { 1, 0, 1 } } },
			{ { { 0, 0, 1 }, { 0, -1, 1 } }, { { 1, 1, 0 }, { 0, 0, 1 } } },
			{ { { 0, 1, 1 }, { 0, -1, 0 } }, { { 1, -1, 1 }, { 0, 1, 0 } } },
			{ { { 0, 1, 1 }, { 0, -1, 0 } }, { { 1, -1, 1 }, { 1, 0, 0 } } },
			{ { { 0, 1, 1 }, { 0, 0, 1 } }, { { 1, 1, 1 }, { 1, -1, 0 } } },
			{ { { 0, 1, 1 }, { 0, 1, 1 } }, { { 1, 0, 0 }, { 0, 1, 1 } } },
			{ { { 0, 1, 1 }, { 1, 1, 1 } }, { { 1, -1, 1 }, { 1, -1, 0 } } },
			{ { { 1, -1, 0 }, { 0, 0, 1 } }, { { 0, -1, 0 }, { 0, -1, 1 } } },
			{ { { 1, -1, 0 }, { 1, 1, 1 } }, { { 0, 1, 0 }, { 0, -1, 1 } } },
			{ { { 1, -1, 0 }, { 1, 1, 1 } }, { { 0, 1, 1 }, { 1, -1, 0 } } },
			{ { { 1, -1, 1 }, { 0, -1, 0 } }, { { 1, 0, 0 }, { 0, 0, 0 } } },
			{ { { 1, -1, 1 }, { 0, -1, 0 } }, { { 1, 1, 1 }, { 1, 1, 0 } } },
			{ { { 1, -1, 1 }, { 0, -1, 1 } }, { { 1, 1, 1 }, { 0, -1, 0 } } },
			{ { { 1, -1, 1 }, { 0, 0, 0 } }, { { 1, 0, 0 }, { 1, 1, 0 } } },
			{ { { 1, -1, 1 }, { 0, 1, 0 } }, { { 0, -1, 0 }, { 0, -1, 0 } } },
			{ { { 1, -1, 1 }, { 1, -1, 1 } }, { { 1, 1, 1 }, { 0, 0, 0 } } },
			{ { { 1, -1, 1 }, { 1, 1, 0 } }, { { 0, -1, 0 }, { 0, -1, 0 } } },
			{ { { 1, -1, 1 }, { 1, 1, 1 } }, { { 1, 0, 0 }, { 0, 0, 1 } } },
			{ { { 1, 0, 1 }, { 0, -1, 1 } }, { { 1, 1, 0 }, { 1, 0, 1 } } },
			{ { { 1, 0, 1 }, { 0, 0, 0 } }, { { 0, 1, 0 }, { 1, -1, 0 } } },
			{ { { 1, 0, 1 }, { 0, 0, 1 } }, { { 1, 1, 1 }, { 0, 0, 0 } } },
			{ { { 1, 0, 1 }, { 0, 1, 0 } }, { { 0, -1, 0 }, { 0, -1, 0 } } },
			{ { { 1, 0, 1 }, { 0, 1, 1 } }, { { 1, -1, 0 }, { 1, 1, 0 } } },
			{ { { 1, 0, 1 }, { 1, -1, 0 } }, { { 1, 1, 1 }, { 0, 0, 0 } } },
			{ { { 1, 1, 0 }, { 0, -1, 1 } }, { { 1, -1, 0 }, { 0, 0, 1 } } },
			{ { { 1, 1, 0 }, { 0, -1, 1 } }, { { 1, -1, 0 }, { 0, 1, 1 } } },
			{ { { 1, 1, 0 }, { 1, 1, 1 } }, { { 0, 0, 0 }, { 0, 0, 1 } } },
			{ { { 1, 1, 1 }, { 0, -1, 1 } }, { { 1, 0, 0 }, { 0, 0, 0 } } },
			{ { { 1, 1, 1 }, { 0, 1, 0 } }, { { 0, -1, 1 }, { 1, -1, 0 } } },
			{ { { 1, 1, 1 }, { 0, 1, 0 } }, { { 0, 0, 0 }, { 1, 1, 1 } } },
			{ { { 1, 1, 1 }, { 0, 1, 1 } }, { { 1, 0, 0 }, { 1, 0, 1 } } },
			{ { { 1, 1, 1 }, { 1, -1, 1 } }, { { 1, 1, 1 }, { 0, 1, 0 } } },
			{ { { 1, 1, 1 }, { 1, 0, 0 } }, { { 1, 0, 0 }, { 0, 0, 1 } } },
			{ { { 1, 1, 1 }, { 1, 1, 0 } }, { { 0, 0, 0 }, { 0, 0, 0 } } },
			{ { { 1, 1, 1 }, { 1, 1, 0 } }, { { 0, 1, 0 }, { 1, 0, 1 } } },
			{ { { 1, 1, 1 }, { 1, 1, 1 } }, { { 1, -1, 1 }, { 0, 1, 0 } } },
			{ { { 0, 1, 1 }, { 1, 1, 1 } }, { { 0, -1, 2 }, { 0, -1, 0 } },
					{ { 1, 1, 2 }, { 1, -1, 0 } } },
			{ { { 1, -1, 1 }, { 0, 0, 0 } }, { { 0, 1, 2 }, { 1, -1, 0 } },
					{ { 1, 1, 1 }, { 1, 0, 0 } } },
			{ { { 1, -1, 1 }, { 0, 0, 2 } }, { { 0, 1, 2 }, { 1, 0, 1 } },
					{ { 1, 1, 1 }, { 1, 0, 0 } } },
			{ { { 1, -1, 1 }, { 1, -1, 1 } }, { { 1, 0, 0 }, { 0, 0, 2 } },
					{ { 0, -1, 1 }, { 1, 0, 1 } } },
			{ { { 1, -1, 2 }, { 0, 1, 0 } }, { { 1, -1, 0 }, { 0, 1, 0 } },
					{ { 0, -1, 0 }, { 0, -1, 1 } } },
			{ { { 1, 1, 0 }, { 0, 1, 2 } }, { { 0, -1, 0 }, { 0, 1, 0 } },
					{ { 0, 0, 1 }, { 1, -1, 0 } } },
			{ { { 1, 1, 1 }, { 0, -1, 0 } }, { { 1, 1, 2 }, { 0, 0, 0 } },
					{ { 1, -1, 0 }, { 0, -1, 0 } } } };

	static Turmite[] getEdPeggJrsTurmites() {
		Turmite[] turmites = new Turmite[ED_PEGG_JRS_TURMITES.length];
		for (int i = 0; i < turmites.length; i++) {
			turmites[i] = new Turmite(ED_PEGG_JRS_TURMITES[i]);
		}

		return turmites;
	}

	private int[][] color;

	private int[][] motion;

	private int[][] state;

	private int s;

	private int x;

	private int y;

	private Pattern pattern;

	private int dir;

	Turmite(int[][] color, int[][] state, int[][] motion, Pattern pattern) {
		this.color = color;
		this.motion = motion;
		this.state = state;
		this.pattern = pattern;
		s = 0;
		x = 0;
		y = 0;
		dir = 0;
	}

	Turmite(int[][] color, int[][] state, int[][] motion) {
		this(color, state, motion, new Pattern());
	}

	Turmite(int[][][] program) {
		this(program, new Pattern());
	}

	/**
	 * @param program
	 */
	Turmite(int[][][] program, Pattern pattern) {
		this.pattern = pattern;

		// turmite: A rule that moves in the indicated direction, considers the
		// current \{color, state}, and changes the {color, direction, state}
		// accordingly.
		// {{w0\[Rule] BN1, b0\[Rule] BR0}, {w1\[Rule] BR1, b1\[Rule] WN0}}
		// {{{1,0,1},{1,-1,0}},{{1,1,1},{0,0,0}}}\

		int stateCnt = program.length;
		int colorCnt = program[0].length;

		color = new int[stateCnt][colorCnt];
		motion = new int[stateCnt][colorCnt];
		state = new int[stateCnt][colorCnt];

		for (int i = 0; i < stateCnt; i++) {
			for (int j = 0; j < colorCnt; j++) {
				color[i][j] = program[i][j][0];
				motion[i][j] = program[i][j][1];
				state[i][j] = program[i][j][2];
			}
		}
	}

	int getColor(int x, int y) {
		return pattern.getColor(x, y);
	}

	int getColor() {
		return pattern.getColor(x, y);
	}

	boolean move() {
		int oldXX = x;
		int oldYY = y;
		int c = pattern.getColor(x, y);
		switch (dir) {
		case 0:
			y++;
			if (y > pattern.getYRadius()) {
				return false;
			}
			break;
		case 1:
			x++;
			if (x > pattern.getXRadius()) {
				return false;
			}
			break;
		case 2:
			y--;
			if (y < -pattern.getYRadius()) {
				return false;
			}
			break;
		case 3:
			x--;
			if (x < -pattern.getXRadius()) {
				return false;
			}
			break;
		}
		int m = motion[s][c];
		dir = (dir + m + DIR_CNT) % DIR_CNT;
		int newC = color[s][c];
		s = state[s][c];
		pattern.setColor(oldXX, oldYY, newC);

		return true;
	}

	/**
	 * @return Returns the xRadius.
	 */
	int getXRadius() {
		return pattern.getXRadius();
	}

	/**
	 * @return Returns the yRadius.
	 */
	int getYRadius() {
		return pattern.getYRadius();
	}

	/**
	 * @return Returns the x.
	 */
	int getX() {
		return x;
	}

	/**
	 * @return Returns the y.
	 */
	int getY() {
		return y;
	}

	/**
	 * @param pattern
	 *            The pattern to set.
	 */
	public void setPattern(Pattern pattern) {
		this.pattern = pattern;
	}

	/**
	 * @param x
	 *            The x to set.
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * @param y
	 *            The y to set.
	 */
	public void setY(int y) {
		this.y = y;
	}

	public Object clone() {
		Object o = null;
		try {
			o = super.clone();
		} catch (CloneNotSupportedException e) {
			System.err.println("Turmite can't clone");
		}
		return o;
	}
}