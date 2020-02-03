/*
 * Created on Dec 8, 2004
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
package com.leipzig48.leipzig.midi;

import java.util.Arrays;

import com.leipzig48.leipzig.core.Interval;
import com.leipzig48.leipzig.lattices.FiveLimitLattice;

/**
 * @author Paul Reiners
 */
class MIDITuningTable {

	private Interval[] table;

	private FiveLimitLattice lattice;

	private int basePitch;

	MIDITuningTable(FiveLimitLattice lattice, int basePitch) {
		this.lattice = lattice;
		this.basePitch = basePitch;

		int width = 2 * lattice.getXRadius() + 1;
		int height = 2 * lattice.getYRadius() + 1;
		table = new Interval[width * height];

		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				int index = i * height + j;
				table[index] = lattice.getNode(i - lattice.getXRadius(), j
						- lattice.getYRadius());
			}
		}

		Arrays.sort(table);
	}

	MIDITuningTable(FiveLimitLattice lattice) {
		this(lattice, 0);
	}

	double getFrequency(int pitch) {
		return table[pitch].getFrequency();
	}

	int getPitch(int x, int y) {
		// TODO Inefficient. Use HashTable, instead.
		Interval interval = lattice.getNode(x, y);
		for (int i = 0; i < table.length; i++) {
			if (table[i].equals(interval)) {
				return basePitch + i;
			}
		}

		return -1;
	}
}