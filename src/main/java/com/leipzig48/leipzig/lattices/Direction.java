/*
 * Created on Dec 14, 2004
 * 
 * Leipzig: A Just Intonation Library Copyright (C) 2004 Paul Reiners
 * 
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 * Contact Info:
 * 
 * Paul Reiners 2506 18 1/2 Ave NW Apt 206 Rochester, MN 55901
 * 
 * paulreiners@earthlink.net
 */
package com.leipzig48.leipzig.lattices;

import java.util.HashMap;

import com.leipzig48.leipzig.core.FiveLimitTransposition;

public final class Direction {
	private String name;

	private Direction(String nm) {
		name = nm;
	}

	public String toString() {
		return name;
	}

	public static final Direction UP = new Direction("Up"),
			DOWN = new Direction("Down"), LEFT = new Direction("Left"),
			RIGHT = new Direction("Right");

	private static final Direction[] directions = { UP, DOWN, LEFT, RIGHT };

	public static final Direction getDirection(String name) {
		for (int i = 0; i < directions.length; i++) {
			if (directions[i].name.equalsIgnoreCase(name)) {
				return directions[i];
			}
		}

		return null;
	}

	private static HashMap directionToTransposition;

	static {
		directionToTransposition = new HashMap(4);
		directionToTransposition.put(Direction.UP,
				FiveLimitTransposition.THIRD_UP);
		directionToTransposition.put(Direction.DOWN,
				FiveLimitTransposition.THIRD_DOWN);
		directionToTransposition.put(Direction.LEFT,
				FiveLimitTransposition.FIFTH_DOWN);
		directionToTransposition.put(Direction.RIGHT,
				FiveLimitTransposition.FIFTH_UP);
	}

	public FiveLimitTransposition getTransposition() {
		return (FiveLimitTransposition) directionToTransposition.get(this);
	}
}