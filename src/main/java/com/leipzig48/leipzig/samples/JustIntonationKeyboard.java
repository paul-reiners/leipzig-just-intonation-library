/*
 * Created on Dec 6, 2004
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
package com.leipzig48.leipzig.samples;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.ButtonGroup;
import javax.swing.JApplet;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import com.leipzig48.leipzig.core.FiveLimitChord;
import com.leipzig48.leipzig.core.FiveLimitTransposition;
import com.leipzig48.leipzig.core.Interval;
import com.leipzig48.leipzig.exceptions.InvalidIntervalException;
import com.leipzig48.leipzig.gui.TransposePanel;
import com.leipzig48.leipzig.lattices.Direction;
import com.leipzig48.leipzig.lattices.FiveLimitLattice;

/**
 * @author Paul Reiners
 * 
 * Based on Nick Didkovsky's VirtualKeyboard
 */
public class JustIntonationKeyboard extends JApplet implements ItemListener,
		ActionListener {

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 3835151770447131442L;

	private static final Color ROOT_COLOR = Color.GREEN;

	TuningTable tuning;

	// TODO Fix.
//	JSynInsFromClassName instrument;
//	JMSLMixerContainer mixer;

	private final static int X_RADIUS = 4;

	private final static int Y_RADIUS = 2;

	private FiveLimitLattice lattice;

	private ChordSelectionPanel chordSelectionPanel;

	private TransposePanel transposePanel;

	private JCheckBox[][] cb;

	private JPanel mainPanel;

	private int rootX;

	private int rootY;

	private Color bGColor;

	private ButtonGroup synthNoteG = new ButtonGroup();

	private JRadioButton sineWaveRB = new JRadioButton("Sine Wave", true),
			sawToothRB = new JRadioButton("Sawtooth", false);

	private ActionListener synthNoteAL = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			boolean[][] prevState = new boolean[cb.length][cb[0].length];
			for (int i = 0; i < prevState.length; i++) {
				for (int j = 0; j < prevState[i].length; j++) {
					prevState[i][j] = cb[i][j].isSelected();
				}
			}
			clearNotes();
			System.out.println("Radio button "
					+ ((JRadioButton) e.getSource()).getText());
			buildInstrumentAndMixer();
			for (int i = 0; i < prevState.length; i++) {
				for (int j = 0; j < prevState[i].length; j++) {
					cb[i][j].setSelected(prevState[i][j]);
				}
			}
		}
	};

	public JPanel buildSynthNotePanel() {
		sineWaveRB.addActionListener(synthNoteAL);
		sawToothRB.addActionListener(synthNoteAL);
		synthNoteG.add(sineWaveRB);
		synthNoteG.add(sawToothRB);
		JPanel synthNotePanel = new JPanel(new GridLayout(2, 1));
		synthNotePanel.add(sineWaveRB);
		synthNotePanel.add(sawToothRB);

		return synthNotePanel;
	}

	public void start() {
		// TODO Fix.
//		JMSL.setIsApplet(true);
//		JSynMusicDevice.instance().open();
		try {
			lattice = new FiveLimitLattice(X_RADIUS, Y_RADIUS);
		} catch (InvalidIntervalException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		rootX = 0;
		rootY = 0;
		buildTuning();
		mainPanel = new JPanel();
		getContentPane().add(mainPanel);
		buildVirtualKeys();
		buildChordButtons();
		buildTranspositionPanel();

		buildInstrumentAndMixer();

		JPanel synthNotePanel = buildSynthNotePanel();
		mainPanel.add(synthNotePanel);
	}

	/**
	 *  
	 */
	private void buildInstrumentAndMixer() {
		// TODO Fix.
//		String instrumentClassName = SineCircuit.class.getName();
		String instrumentName = "Sine Wave";
		if (sawToothRB.isSelected()) {
//			instrumentClassName = "com.softsynth.jsyn.circuits.FilteredSawtoothBL";
			instrumentName = "Sawtooth";
		}

//		buildInstrument(instrumentClassName, instrumentName);
		buildMixer();
	}

	private void buildMixer() {
		// TODO Fix.
//		mixer = new JMSLMixerContainer();
//		mixer.start();
//		mixer.addInstrument(instrument);
	}

	public void stop() {
		removeAll();
		// TODO Fix.
//		JMSL.closeMusicDevices();
	}

	private void buildInstrument(String instrumentClassName,
			String instrumentName) {
		// 8 voice polyphony. Substitute any fully qualified SynthNote class
		// name
		// TODO Fix.
//		instrument = new JSynInsFromClassName(8, instrumentClassName);
//		instrument.setTuning(tuning); // !!!!
//		instrument.setName(instrumentName);
	}

	private void buildTuning() {
		int cnt = (2 * Y_RADIUS + 1) * (2 * X_RADIUS + 1);
		double[] frequencies = new double[cnt];
		for (int y = Y_RADIUS; y >= -Y_RADIUS; y--) {
			for (int x = -X_RADIUS; x <= X_RADIUS; x++) {
				int pitch = indexToPitch(x, y);
				double frequency = lattice.getFrequency(x, y);
				frequencies[pitch] = frequency;
				System.out
						.println("Pitch " + pitch + " = " + frequency + " Hz");
			}
		}
		tuning = new TuningTable(frequencies, 0);
		System.out.println("Octave = " + tuning.getStepsPerOctave());
	}

	private void buildVirtualKeys() {
		JPanel panel = new JPanel();
		int height = 2 * Y_RADIUS + 1;
		int width = 2 * X_RADIUS + 1;
		panel.setLayout(new GridLayout(height, width));
		cb = new JCheckBox[height][width];
		for (int y = Y_RADIUS; y >= -Y_RADIUS; y--) {
			for (int x = -X_RADIUS; x <= X_RADIUS; x++) {
				cb[-y + Y_RADIUS][x + X_RADIUS] = new JCheckBox(lattice
						.getNode(x, y).toString());
				cb[-y + Y_RADIUS][x + X_RADIUS].setActionCommand("" + x + ","
						+ y);
				panel.add(cb[-y + Y_RADIUS][x + X_RADIUS]);
				cb[-y + Y_RADIUS][x + X_RADIUS].addItemListener(this);
			}
		}
		bGColor = cb[Y_RADIUS][X_RADIUS].getBackground();
		cb[Y_RADIUS][X_RADIUS].setBackground(ROOT_COLOR);
		mainPanel.add(panel);
	}

	private void buildChordButtons() {
		chordSelectionPanel = new ChordSelectionPanel();
		chordSelectionPanel.registerListener(this);
		mainPanel.add(chordSelectionPanel);
	}

	private void buildTranspositionPanel() {
		transposePanel = new TransposePanel();
		transposePanel.addActionListener(this);
		mainPanel.add(transposePanel);
	}

	private int indexToPitch(int x, int y) {
		int yy = -y + Y_RADIUS;
		int xx = x + X_RADIUS;

		return yy * (2 * X_RADIUS + 1) + xx;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.ItemListener#itemStateChanged(java.awt.event.ItemEvent)
	 */
	public void itemStateChanged(ItemEvent ev) {
		JCheckBox source = (JCheckBox) ev.getSource();
		boolean state = source.isSelected();
		String actionCommand = source.getActionCommand();
		String[] parts = actionCommand.split(",");
		int x = Integer.parseInt(parts[0]);
		int y = Integer.parseInt(parts[1]);
		double pitch = indexToPitch(x, y);
		double dur = 1.0; // not important since virtual key controls sustain
		double amplitude = 0.4;
		double hold = 1.0; // not important since virtual key controls sustain
		double[] data = { dur, pitch, amplitude, hold };
		// TODO Fix.
		if (state) {
//			instrument.on(JMSL.now(), 1.0, data);
//			System.out.println("Playing " + tuning.getFrequency(pitch) + " Hz");
		} else {
//			instrument.off(JMSL.now(), 1.0, data);
		}
		enableControls();
	}

	private void enableControls() {
		transposePanel.setTranspositionEnabled(Direction.UP,
				isRoomToMove(Direction.UP));
		transposePanel.setTranspositionEnabled(Direction.DOWN,
				isRoomToMove(Direction.DOWN));
		transposePanel.setTranspositionEnabled(Direction.LEFT,
				isRoomToMove(Direction.LEFT));
		transposePanel.setTranspositionEnabled(Direction.RIGHT,
				isRoomToMove(Direction.RIGHT));

		FiveLimitChord[] chords = { FiveLimitChord.MAJOR_TRIAD,
				FiveLimitChord.MINOR_TRIAD,
				FiveLimitChord.CONDISSONANT_TRIADS[0],
				FiveLimitChord.CONDISSONANT_TRIADS[1],
				FiveLimitChord.MAJOR_SEVENTH_CHORD,
				FiveLimitChord.MINOR_SEVENTH_CHORD,
				FiveLimitChord.MAJOR_NINTH_CHORD,
				FiveLimitChord.MINOR_NINTH_CHORD };
		for (int i = 0; i < chords.length; i++) {
			FiveLimitChord chord = chords[i];
			chordSelectionPanel.setChordEnabled(chord, isRoomForChord(chord));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */

	public void actionPerformed(ActionEvent e) {
		String actionCmd = e.getActionCommand();
		System.out.println(actionCmd + " chosen");
		String name = actionCmd.split(":")[1];
		if (actionCmd.startsWith(ChordSelectionPanel.actionPrefix)) {
			FiveLimitChord chord = ChordSelectionPanel.createChord(name);
			if (chord == null) {
				clearNotes();
			} else {
				chord = chord.transpose(rootX, rootY);
				playChord(chord);
			}
		} else {
			transpose(Direction.getDirection(name));
		}
		enableControls();
	}

	/**
	 * @param direction
	 */
	private void transpose(Direction direction) {
		FiveLimitTransposition transposition = TransposePanel
				.createTransposition(direction);
		int height = cb.length;
		int width = cb[0].length;
		if (!isRoomToMove(direction)) {
			System.err.println("No room to move!");

			return;
		}

		cb[-rootY + Y_RADIUS][rootX + X_RADIUS].setBackground(bGColor);
		rootX += transposition.getDX();
		rootY += transposition.getDY();
		cb[-rootY + Y_RADIUS][rootX + X_RADIUS].setBackground(ROOT_COLOR);
		if (direction.equals(Direction.UP)) {
			for (int i = 0; i < height - 1; i++) {
				for (int j = 0; j < width; j++) {
					cb[i][j].setSelected(cb[i + 1][j].isSelected());
				}
			}
			for (int j = 0; j < width; j++) {
				cb[height - 1][j].setSelected(false);
			}
		} else if (direction.equals(Direction.DOWN)) {
			for (int i = height - 1; i > 0; i--) {
				for (int j = 0; j < width; j++) {
					cb[i][j].setSelected(cb[i - 1][j].isSelected());
				}
			}
			for (int j = 0; j < width; j++) {
				cb[0][j].setSelected(false);
			}
		} else if (direction.equals(Direction.LEFT)) {
			for (int i = 0; i < height; i++) {
				for (int j = 0; j < width - 1; j++) {
					cb[i][j].setSelected(cb[i][j + 1].isSelected());
				}
			}
			for (int i = 0; i < height; i++) {
				cb[i][width - 1].setSelected(false);
			}
		} else if (direction.equals(Direction.RIGHT)) {
			for (int i = 0; i < height; i++) {
				for (int j = width - 1; j > 0; j--) {
					cb[i][j].setSelected(cb[i][j - 1].isSelected());
				}
			}
			for (int i = 0; i < height; i++) {
				cb[i][0].setSelected(false);
			}
		}
	}

	private boolean isRoomForChord(FiveLimitChord chord) {
		int[][] displacements = chord.getDisplacements();
		for (int i = 0; i < displacements.length; i++) {
			int dX = displacements[i][0];
			int dY = displacements[i][1];
			if (dX + rootX < -X_RADIUS || dX + rootX > X_RADIUS
					|| dY + rootY > Y_RADIUS || dY + rootY < -Y_RADIUS) {
				return false;
			}
		}

		return true;
	}

	/**
	 * @param direction
	 */
	private boolean isRoomToMove(Direction direction) {
		int height = cb.length;
		int width = cb[0].length;
		if (direction.equals(Direction.UP)) {
			for (int i = 0; i < width; i++) {
				if (cb[0][i].isSelected()) {
					return false;
				}
			}
			if (rootY == Y_RADIUS) {
				return false;
			}
		} else if (direction.equals(Direction.DOWN)) {
			for (int i = 0; i < width; i++) {
				if (cb[height - 1][i].isSelected()) {
					return false;
				}
			}
			if (rootY == -Y_RADIUS) {
				return false;
			}
		} else if (direction.equals(Direction.LEFT)) {
			for (int i = 0; i < height; i++) {
				if (cb[i][0].isSelected()) {
					return false;
				}
			}
			if (rootX == -X_RADIUS) {
				return false;
			}
		} else if (direction.equals(Direction.RIGHT)) {
			for (int i = 0; i < height; i++) {
				if (cb[i][width - 1].isSelected()) {
					return false;
				}
			}
			if (rootX == X_RADIUS) {
				return false;
			}
		}

		return true;
	}

	private void playChord(FiveLimitChord chord) {
		clearNotes();
		if (chord != null) {
			for (int y = Y_RADIUS; y >= -Y_RADIUS; y--) {
				for (int x = -X_RADIUS; x <= X_RADIUS; x++) {
					Interval[] intervals = new Interval[0];
					try {
						intervals = chord.getIntervals();
					} catch (InvalidIntervalException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					cb[-y + Y_RADIUS][x + X_RADIUS].setSelected(false);
					for (int i = 0; i < intervals.length; i++) {
						if (lattice.getNode(x, y).equals(intervals[i])) {
							cb[-y + Y_RADIUS][x + X_RADIUS].setSelected(true);
						}
					}
				}
			}
		}
	}

	/**
	 *  
	 */
	private void clearNotes() {
		for (int y = Y_RADIUS; y >= -Y_RADIUS; y--) {
			for (int x = -X_RADIUS; x <= X_RADIUS; x++) {
				cb[-y + Y_RADIUS][x + X_RADIUS].setSelected(false);
			}
		}
	}
}