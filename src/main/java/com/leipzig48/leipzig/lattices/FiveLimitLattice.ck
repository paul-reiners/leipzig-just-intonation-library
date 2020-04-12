/*
 * Created on Dec 7, 2004
 * 
 * Leipzig: A Just Intonation Library 
 * Copyright (C) 2004, 2020 Paul Reiners
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
 * 	4319 Bryant Ave S
 * 	Apt C001
 * 	Minneapolis, MN  55409
 * 
 * 	paul.reiners@gmail.com
 */
import com.leipzig48.leipzig.core.Interval;
import com.leipzig48.leipzig.exceptions.CoordinatesOutOfBoundsException;
import com.leipzig48.leipzig.exceptions.InvalidIntervalException;

/**
 * @author Paul Reiners
 */
public class FiveLimitLattice {

	private Interval[][] nodes;

	private int xRadius;

	private int yRadius;

	private final static Interval D_X = Interval.PERFECT_FIFTH;

	private final static Interval D_Y = Interval.MAJOR_THIRD;

	public FiveLimitLattice(int xRadius, int yRadius) throws InvalidIntervalException {
		this.xRadius = xRadius;
		this.yRadius = yRadius;

		nodes = new Interval[2 * yRadius + 1][2 * xRadius + 1];
		nodes[yRadius][xRadius] = Interval.MIDDLE_C;
		for (int y = 1; y <= yRadius; y++) {
			nodes[yRadius + y][xRadius] = nodes[yRadius + y - 1][xRadius]
					.add(D_Y);
			nodes[yRadius - y][xRadius] = nodes[yRadius - y + 1][xRadius]
					.subtract(D_Y);
		}
		for (int y = 0; y < 2 * yRadius + 1; y++) {
			for (int x = 1; x <= xRadius; x++) {
				nodes[y][xRadius + x] = nodes[y][xRadius + x - 1].add(D_X);
				nodes[y][xRadius - x] = nodes[y][xRadius - x + 1].subtract(D_X);
			}
		}
	}

	/**
	 * 
	 */
	void printOut() {
		int maxLength = 0;
		for (int i = 0; i < nodes.length; i++) {
			for (int j = 0; j < nodes[i].length; j++) {
				int len = nodes[i][j].toString().length();
				if (len > maxLength) {
					maxLength = len;
				}
			}
		}

		for (int i = nodes.length - 1; i >= 0; i--) {
			for (int j = 0; j < nodes[i].length; j++) {
				int len = nodes[i][j].toString().length();
				for (int k = 0; k < maxLength - len + 1; k++) {
					System.out.print(" ");
				}
				System.out.print(nodes[i][j]);
			}
			System.out.println();
		}
	}

	public Interval getNode(int x, int y) {
		if (!(-xRadius <= x && x <= xRadius)) {
			throw new CoordinatesOutOfBoundsException(
					"Absolute value of x must be not more than the x radius.");
		}
		if (!(-yRadius <= y && y <= yRadius)) {
			throw new CoordinatesOutOfBoundsException(
					"Absolute value of y must be not more than the y radius.");
		}

		return nodes[y + yRadius][x + xRadius];
	}

	public double getFrequency(int x, int y) {
		return getNode(x, y).getFrequency();
	}

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