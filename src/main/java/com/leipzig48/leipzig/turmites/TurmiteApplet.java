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

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JApplet;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

import com.leipzig48.leipzig.gui.RadioButtonPanel;

/**
 * @author Paul Reiners
 */
public class TurmiteApplet extends JApplet {
	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 3256721801391257909L;

	private static final int DEFAULT_RADIUS = 64;

	private static final int MOVE_RATE = 100; // milliseconds

	Dimension totalSize;

	private Turmite[] turmites;

	Timer timer;

	private RadioButtonPanel turmiteSelectionPanel;

	private TurmiteComponent turmiteComponent;

	private JCheckBox chkTwoTurmites;

	private Random rand;

	public void init() {
		rand = new Random();
		timer = new Timer();
		timer.schedule(new MoveTask(), 0, //initial delay
				1 * MOVE_RATE); //subsequent rate

		turmiteComponent = new TurmiteComponent("Turmite World");
		Container cp = getContentPane();
		cp.add(BorderLayout.CENTER, turmiteComponent);

		createTurmiteSelectionPanel();
		JPanel controlPanel = new JPanel();
		controlPanel.add(turmiteSelectionPanel);
		chkTwoTurmites = new JCheckBox("Use two turmites");
		controlPanel.add(chkTwoTurmites);
		cp.add(BorderLayout.EAST, controlPanel);
		if (chkTwoTurmites.isSelected()) {
			turmites = new Turmite[2];
		} else {
			turmites = new Turmite[1];
		}
	}

	class MoveTask extends TimerTask {
		boolean stillMoving = true;

		public void run() {
			if (turmites == null) {
				return;
			} else {
				for (int i = 0; i < turmites.length; i++) {
					if (turmites[i] == null) {
						return;
					}
				}
			}
			if (stillMoving) {
				stillMoving = move();
			} else {
				System.out.println("Turmite no longer moving!");
				turmites = null;
			}
		}
	}

	/**
	 *  
	 */
	private void createTurmiteSelectionPanel() {
		Turmite[] otherTurmites = Turmite.getEdPeggJrsTurmites();
		String[] localButtonStrings = {};
		int[] localMnemonics = {};
		Turmite[] localTurmites = {};

		int localCnt = localButtonStrings.length;
		int cnt = localCnt + otherTurmites.length;
		String[] buttonStrings = new String[cnt];
		int[] mnemonics = new int[cnt];
		Turmite[] turmites = new Turmite[cnt];

		for (int i = 0; i < turmites.length; i++) {
			if (i < localCnt) {
				buttonStrings[i] = localButtonStrings[i];
				mnemonics[i] = localMnemonics[i];
				turmites[i] = localTurmites[i];
			} else {
				buttonStrings[i] = "Turmite " + i;
				turmites[i] = otherTurmites[i - localCnt];
			}
			turmites[i].setPattern(new Pattern(DEFAULT_RADIUS, DEFAULT_RADIUS));
		}

		String actionPrefix = "selectTurmite";
		int rows = 22;
		int cols = 2;
		turmiteSelectionPanel = new RadioButtonPanel(buttonStrings,
				actionPrefix, mnemonics, turmites, false, rows, cols);
		turmiteSelectionPanel.registerListener(tSL);
	}

	class ButtonListener implements ActionListener {
		private static final boolean DEBUG = false;

		public void actionPerformed(ActionEvent e) {
			if (DEBUG) {
				System.out.println("ActionEvent has occurred: " + e);
			}
			move();
		}
	}

	class TurmiteSelectionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String actionCmd = e.getActionCommand();
			System.out.println(actionCmd + " chosen");
			String name = actionCmd.split(":")[1];
			if (chkTwoTurmites.isSelected()) {
				turmites = new Turmite[2];
			} else {
				turmites = new Turmite[1];
			}
			Pattern pattern = new Pattern();
			turmites[0] = (Turmite) ((Turmite) turmiteSelectionPanel
					.getObject(name)).clone();
			turmites[0].setPattern(pattern);
			if (chkTwoTurmites.isSelected()) {
				turmites[1] = (Turmite) ((Turmite) turmiteSelectionPanel
						.getObject(name)).clone();
				turmites[1].setPattern(pattern);
				int x = rand.nextInt(21);
				if (rand.nextBoolean()) {
					x *= -1;
				}
				turmites[1].setX(x);
				int y = rand.nextInt(21);
				if (rand.nextBoolean()) {
					y *= -1;
				}
				turmites[1].setY(y);
			}

			turmiteComponent.clearWorld();
		}
	}

	private TurmiteSelectionListener tSL = new TurmiteSelectionListener();

	/**
	 *  
	 */
	private boolean move() {
		int[] x = new int[turmites.length];
		int[] y = new int[turmites.length];
		for (int i = 0; i < turmites.length; i++) {
			x[i] = turmites[i].getX();
			y[i] = turmites[i].getY();
			boolean moved = turmites[i].move();
			if (!moved) {
				System.out.println("A Turmite has went out of bounds.");

				return false;
			}
		}

		turmiteComponent.updateWorld(turmites, x, y);

		return true;
	}
}