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
package com.leipzig48.leipzig.gui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.leipzig48.leipzig.core.FiveLimitTransposition;
import com.leipzig48.leipzig.lattices.Direction;

/**
 * @author Paul Reiners
 */
public class TransposePanel extends JPanel implements ActionListener {
	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 3546362812206429239L;

	protected JButton up, down, left, right;

	private final static String ACTION_COMMAND_PREFIX = "transpose:";

	/**
	 *  
	 */
	public TransposePanel() {
		super(new GridLayout(3, 3));
		up = new JButton("Up");
		up.setMnemonic(KeyEvent.VK_U);
		up.setActionCommand(ACTION_COMMAND_PREFIX + "up");

		down = new JButton("Down");
		down.setMnemonic(KeyEvent.VK_D);
		down.setActionCommand(ACTION_COMMAND_PREFIX + "down");

		left = new JButton("Left");
		left.setMnemonic(KeyEvent.VK_L);
		left.setActionCommand(ACTION_COMMAND_PREFIX + "left");

		right = new JButton("Right");
		right.setMnemonic(KeyEvent.VK_R);
		right.setActionCommand(ACTION_COMMAND_PREFIX + "right");

		up.setToolTipText("Click this button to go up a third.");
		down.setToolTipText("Click this button to go down a third.");
		left.setToolTipText("Click this button to go down a fifth.");
		right.setToolTipText("Click this button to go up a fifth.");

		//Add Components to this container.
		add(new JLabel());
		add(up);
		add(new JLabel());
		add(left);
		add(new JLabel());
		add(right);
		add(new JLabel());
		add(down);
		add(new JLabel());
	}

	/**
	 *  
	 */
	public void addActionListener(ActionListener listener) {
		//Listen for actions on buttons.
		up.addActionListener(listener);
		left.addActionListener(listener);
		down.addActionListener(listener);
		right.addActionListener(listener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		System.out.println(e.getActionCommand() + " chosen");
	}

	/** Returns an FiveLimitTransposition, or null if the name was invalid. */
	public static FiveLimitTransposition createTransposition(Direction direction) {
		return direction.getTransposition();
	}

	public void setTranspositionEnabled(Direction dir, boolean enabled) {
		if (dir.equals(Direction.UP)) {
			up.setEnabled(enabled);
		} else if (dir.equals(Direction.DOWN)) {
			down.setEnabled(enabled);
		} else if (dir.equals(Direction.LEFT)) {
			left.setEnabled(enabled);
		} else if (dir.equals(Direction.RIGHT)) {
			right.setEnabled(enabled);
		}
	}
}