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

import java.awt.event.ActionEvent;
import java.util.Iterator;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.darwinsys.lang.GetOpt;
import com.darwinsys.lang.GetOptDesc;

/**
 * @author Paul Reiners
 */
public class ThirtySeventhStreetBlues extends AbstractJITurmite {

	private JButton cmdNext;

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.applet.Applet#start()
	 */
	public void start() {
		super.start();

		if (!runningFromCmdLine) {
			radius = DEFAULT_RADIUS;
			startTurmite();
		}
	}

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 3256444702969770552L;

	private int curTurmiteIndex;

	private boolean runningFromCmdLine = false;

	/* Can be run as either an application or as an applet. */
	public static void main(String args[]) {
		ThirtySeventhStreetBlues applet = new ThirtySeventhStreetBlues();
		applet.curTurmiteIndex = 0;
		applet.radius = DEFAULT_RADIUS;
		applet.parseCommandLineOptions(args);
		JFrame frame = new JFrame("Thirty-Seventh Street Blues");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(applet);
		frame.setSize(WIDTH, HEIGHT);
		applet.runningFromCmdLine = true;
		applet.init();
		applet.start();
		frame.setVisible(true);

		applet.startTurmite();
	}

	private void parseCommandLineOptions(String[] argv) {
		boolean errs = false;

		GetOptDesc options[] = { new GetOptDesc('r', "radius", true), };
		GetOpt parser = new GetOpt(options);
		Map optionsFound = parser.parseArguments(argv);
		Iterator it = optionsFound.keySet().iterator();
		while (it.hasNext()) {
			String key = (String) it.next();
			char c = key.charAt(0);
			switch (c) {
			case 'r':
				radius = Integer.parseInt((String) optionsFound.get(key));
				break;
			case '?':
				errs = true;
				break;
			default:
				throw new IllegalStateException("Unexpected option character: "
						+ c);
			}
		}
		if (errs) {
			System.err.println("Usage: JITurmiteDemo [-r radius]");
		}
		System.out.print("Options: ");
		System.out.print("Radius: " + radius + ' ');
		System.out.println();
	}

	/**
	 *  
	 */
	JPanel createTurmiteSelectionPanel() {
		return null;
	}

	/**
	 *  
	 */
	protected boolean move() {
		boolean returnVal = updateTurmitesAndTheirWorld();
		if (!returnVal) {
			startNextTurmite();

			return false;
		}

		return true;
	}

	/**
	 *  
	 */
	void startTurmite() {
		initTurmite();
		curTurmiteIndex = (curTurmiteIndex + 1)
				% Turmite.ED_PEGG_JRS_TURMITES.length;

		startNotePlayerThread();
		startStopBtn.setText("Stop");
	}

	/**
	 *  
	 */
	private void startNextTurmite() {
		go = false;
		stopNotes();
		turmiteComponent.clearWorld();
		startTurmite();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.leipzig48.leipzig.turmites.AbstractJITurmite#getNextTurmiteIndex()
	 */
	int getNextTurmiteIndex() {
		return rand.nextInt(Turmite.ED_PEGG_JRS_TURMITES.length);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.leipzig48.leipzig.turmites.AbstractJITurmite#playDuet()
	 */
	boolean playDuet() {
		return rand.nextBoolean();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.leipzig48.leipzig.turmites.AbstractJITurmite#getNextTurmiteIndices()
	 */
	int[] getNextTurmiteIndices() {
		int[] nextIndices = new int[2];
		nextIndices[0] = getNextTurmiteIndex();
		if (rand.nextBoolean()) {
			nextIndices[1] = getNextTurmiteIndex();
		} else {
			nextIndices[1] = nextIndices[0];
		}

		return nextIndices;
	}

	public void actionPerformed(ActionEvent e) {
		if ("next".equals(e.getActionCommand())) {
			startNextTurmite();
		} else {
			super.actionPerformed(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.leipzig48.leipzig.turmites.AbstractJITurmite#getExtraControls()
	 */
	JComponent[] getExtraControls() {
		cmdNext = new JButton("Next");
		cmdNext.addActionListener(this);
		cmdNext.setActionCommand("next");

		return new JComponent[] { cmdNext };
	}
}