/*
 * Created on Dec 12, 2004
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

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

/**
 * @author Paul Reiners
 */
public class RadioButtonPanel extends JPanel implements ActionListener {

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 1L;

	private String actionPrefix;

	private JRadioButton[] buttons;

	private HashMap objectToButton;

	private HashMap nameToObject;

	/**
	 *  
	 */
	RadioButtonPanel(String[] buttonStrings, String actionPrefix,
			int[] mnemonics, Object[] correspondingObjects, boolean selectFirst) {
		//Put the radio buttons in a column in a panel.
		this(buttonStrings, actionPrefix, mnemonics, correspondingObjects,
				selectFirst, 0, 1);
	}

	/**
	 * @param buttonStrings
	 * @param actionPrefix2
	 * @param mnemonics
	 * @param turmites
	 * @param selectFirst
	 * @param rows
	 * @param cols
	 */
	public RadioButtonPanel(String[] buttonStrings, String actionPrefix,
			int[] mnemonics, Object[] correspondingObjects,
			boolean selectFirst, int rows, int cols) {
		super(new BorderLayout());

		this.actionPrefix = actionPrefix;

		//Create the radio buttons.
		buttons = new JRadioButton[buttonStrings.length];
		objectToButton = new HashMap();
		nameToObject = new HashMap();

		for (int i = 0; i < buttons.length; i++) {
			buttons[i] = new JRadioButton(buttonStrings[i]);
			buttons[i].setMnemonic(mnemonics[i]);
			buttons[i].setActionCommand(actionPrefix + ":" + buttonStrings[i]);
			if (i == 0 && selectFirst) {
				buttons[i].setSelected(true);
			}
			objectToButton.put(correspondingObjects[i], buttons[i]);
			nameToObject.put(buttonStrings[i], correspondingObjects[i]);
		}

		//Group the radio buttons.
		ButtonGroup group = new ButtonGroup();
		for (int i = 0; i < buttons.length; i++) {
			group.add(buttons[i]);
		}

		registerListener(this);

		JPanel radioPanel = new JPanel(new GridLayout(rows, cols));
		for (int i = 0; i < buttons.length; i++) {
			radioPanel.add(buttons[i]);
		}

		add(radioPanel, BorderLayout.LINE_START);
		setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
	}

	/**
	 *  
	 */
	public void registerListener(ActionListener listener) {
		//Register a listener for the radio buttons.
		for (int i = 0; i < buttons.length; i++) {
			buttons[i].addActionListener(listener);
		}
	}

	/*
	 * Listens to the radio buttons.
	 * 
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		System.out.println(e.getActionCommand() + " chosen");
	}

	/** Returns the corresponding Object, or null if the name was invalid. */
	public Object getObject(String name) {
		return nameToObject.get(name);
	}

	/**
	 * @return Returns the actionPrefix.
	 */
	String getActionPrefix() {
		return actionPrefix;
	}

	void setObjectButtonEnabled(Object object, boolean enabled) {
		((JRadioButton) objectToButton.get(object)).setEnabled(enabled);
	}
}