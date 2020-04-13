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

import javax.swing.*;

import com.jsyn.JSyn;
import com.jsyn.Synthesizer;
import com.jsyn.instruments.SubtractiveSynthVoice;
import com.jsyn.unitgen.LineOut;
import com.jsyn.unitgen.UnitVoice;
import com.jsyn.util.VoiceAllocator;
import com.leipzig48.leipzig.core.FiveLimitChord;
import com.leipzig48.leipzig.core.FiveLimitTransposition;
import com.leipzig48.leipzig.core.Interval;
import com.leipzig48.leipzig.exceptions.InvalidIntervalException;
import com.leipzig48.leipzig.gui.TransposePanel;
import com.leipzig48.leipzig.lattices.Direction;
import com.leipzig48.leipzig.lattices.FiveLimitLattice;
import com.softsynth.shared.time.TimeStamp;

/**
 * @author Paul Reiners
 * 
 * Based on Nick Didkovsky's VirtualKeyboard
 */
public class JustIntonationKeyboard extends JPanel implements ItemListener,
		ActionListener {

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 3835151770447131442L;

	private static final Color ROOT_COLOR = Color.GREEN;

	private final static int X_RADIUS = 4;

	private final static int Y_RADIUS = 2;

	private FiveLimitLattice lattice;

	private ChordSelectionPanel chordSelectionPanel;

	private TransposePanel transposePanel;

	private JCheckBox[][] cb;

	private final JPanel mainPanel;

	private int rootX;

	private int rootY;

	private Color bGColor;

	private Synthesizer synth;
	private static final int MAX_VOICES = 8;
	private VoiceAllocator allocator;

	private double[] frequencies;

	public JustIntonationKeyboard() {
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
		this.add(mainPanel);
		buildVirtualKeys();
		buildChordButtons();
		buildTranspositionPanel();

		initializeMusic();
	}


	/**
	 * Create the GUI and show it. For thread safety, this method should be
	 * invoked from the event-dispatching thread.
	 */
	private static void createAndShowGUI() {
		//Make sure we have nice window decorations.
		JFrame.setDefaultLookAndFeelDecorated(true);

		//Create and set up the window.
		JFrame frame = new JFrame("Chord Selection");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//Create and set up the content pane.
		JComponent newContentPane = new JustIntonationKeyboard();
		newContentPane.setOpaque(true); //content panes must be opaque
		frame.setContentPane(newContentPane);

		//Display the window.
		frame.pack();
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		//Schedule a job for the event-dispatching thread:
		//creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeLater(JustIntonationKeyboard::createAndShowGUI);
	}

	public void stop() {
		removeAll();
	}

	private void buildTuning() {
		int cnt = (2 * Y_RADIUS + 1) * (2 * X_RADIUS + 1);
		frequencies = new double[cnt];
		for (int y = Y_RADIUS; y >= -Y_RADIUS; y--) {
			for (int x = -X_RADIUS; x <= X_RADIUS; x++) {
				int pitch = indexToPitch(x, y);
				double frequency = lattice.getFrequency(x, y);
				frequencies[pitch] = frequency;
				System.out
						.println("Pitch " + pitch + " = " + frequency + " Hz");
			}
		}
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
		int pitch = indexToPitch(x, y);
		double amplitude = 0.4;
		// Get synthesizer time in seconds.
		double timeNow = synth.getCurrentTime();

		// Advance to a near future time so we have a clean start.
		double time = timeNow + 1.0;
		TimeStamp timeStamp = new TimeStamp(time);
		if (state) {
			allocator.noteOn(pitch, frequencies[pitch], amplitude, timeStamp);
		} else {
			allocator.noteOff(pitch, timeStamp);
		}
		enableControls();
	}

	private void initializeMusic() {
		synth = JSyn.createSynthesizer();

		// Add an output.
		LineOut lineOut;
		synth.add(lineOut = new LineOut());

		UnitVoice[] voices = new UnitVoice[MAX_VOICES];
		for (int i = 0; i < MAX_VOICES; i++) {
			SubtractiveSynthVoice voice = new SubtractiveSynthVoice();
			synth.add(voice);
			voice.getOutput().connect(0, lineOut.input, 0);
			voice.getOutput().connect(0, lineOut.input, 1);
			voices[i] = voice;
		}
		allocator = new VoiceAllocator(voices);

		// Start synthesizer using default stereo output at 44100 Hz.
		synth.start();
		// We only need to start the LineOut. It will pull data from the
		// voices.
		lineOut.start();
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
		for (FiveLimitChord chord : chords) {
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
			for (JCheckBox[] jCheckBoxes : cb) {
				for (int j = 0; j < width - 1; j++) {
					jCheckBoxes[j].setSelected(jCheckBoxes[j + 1].isSelected());
				}
			}
			for (JCheckBox[] jCheckBoxes : cb) {
				jCheckBoxes[width - 1].setSelected(false);
			}
		} else if (direction.equals(Direction.RIGHT)) {
			for (JCheckBox[] jCheckBoxes : cb) {
				for (int j = width - 1; j > 0; j--) {
					jCheckBoxes[j].setSelected(jCheckBoxes[j - 1].isSelected());
				}
			}
			for (JCheckBox[] jCheckBoxes : cb) {
				jCheckBoxes[0].setSelected(false);
			}
		}
	}

	private boolean isRoomForChord(FiveLimitChord chord) {
		int[][] displacements = chord.getDisplacements();
		for (int[] displacement : displacements) {
			int dX = displacement[0];
			int dY = displacement[1];
			if (dX + rootX < -X_RADIUS || dX + rootX > X_RADIUS
					|| dY + rootY > Y_RADIUS || dY + rootY < -Y_RADIUS) {
				return false;
			}
		}

		return true;
	}

	/**
	 * @param direction direction to move
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
			return rootY != Y_RADIUS;
		} else if (direction.equals(Direction.DOWN)) {
			for (int i = 0; i < width; i++) {
				if (cb[height - 1][i].isSelected()) {
					return false;
				}
			}
			return rootY != -Y_RADIUS;
		} else if (direction.equals(Direction.LEFT)) {
			for (JCheckBox[] jCheckBoxes : cb) {
				if (jCheckBoxes[0].isSelected()) {
					return false;
				}
			}
			return rootX != -X_RADIUS;
		} else if (direction.equals(Direction.RIGHT)) {
			for (JCheckBox[] jCheckBoxes : cb) {
				if (jCheckBoxes[width - 1].isSelected()) {
					return false;
				}
			}
			return rootX != X_RADIUS;
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
					for (Interval interval : intervals) {
						if (lattice.getNode(x, y).equals(interval)) {
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