/*
 * Created on Dec 19, 2004
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

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * @author Paul Reiners
 */
public class JITurmiteApplet extends AbstractJITurmite {

	private final JComboBox<String > c = new JComboBox<>();

	private JCheckBox chkDuet;

	/**
	 *  
	 */
	protected boolean move() {
		return updateTurmitesAndTheirWorld();
	}

	/* Can be run as either an application or as an applet. */
	public static void main(String[] args) {
		AbstractJITurmite applet = new JITurmiteApplet();
		applet.radius = DEFAULT_RADIUS;
		JFrame frame = new JFrame("Thirty-Seventh Street Blues");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(applet);
		frame.setSize(WIDTH, HEIGHT);
		applet.init();
		applet.start();
		frame.setVisible(true);

		applet.startTurmite();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.leipzig48.leipzig.turmites.AbstractJITurmite#getNextTurmiteIndex()
	 */
	int getNextTurmiteIndex() {
		return c.getSelectedIndex();
	}

	/**
	 *  
	 */
	JPanel createTurmiteSelectionPanel() {
		int turmiteCnt = Turmite.ED_PEGG_JRS_TURMITES.length;
		description = new String[turmiteCnt];
		for (int i = 0; i < turmiteCnt; i++) {
			description[i] = "Turmite " + i;
		}
		for (int i = 0; i < turmiteCnt; i++)
			c.addItem(description[i]);
		c.addActionListener(e -> {
			System.out.println("index: " + c.getSelectedIndex() + "   "
					+ ((JComboBox) e.getSource()).getSelectedItem());
			go = false;
			stopNotes();
			turmiteComponent.clearWorld();
			startTurmite();
		});
		JPanel panel = new JPanel();
		panel.add(c);

		chkDuet = new JCheckBox("Duet");
		panel.add(chkDuet);

		return panel;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.leipzig48.leipzig.turmites.AbstractJITurmite#playDuet()
	 */
	boolean playDuet() {
		return chkDuet.isSelected();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.leipzig48.leipzig.turmites.AbstractJITurmite#getNextTurmiteIndices()
	 */
	int[] getNextTurmiteIndices() {
		int index = getNextTurmiteIndex();

		return new int[] { index, index };
	}

	/* (non-Javadoc)
	 * @see com.leipzig48.leipzig.turmites.AbstractJITurmite#getExtraControls()
	 */
	JComponent[] getExtraControls() {
		return new JComponent[0];
	}
}