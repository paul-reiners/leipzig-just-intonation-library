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

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;

import jmsltutorial.SineCircuit;

import com.leipzig48.leipzig.exceptions.InvalidIntervalException;
import com.leipzig48.leipzig.lattices.FiveLimitLattice;
import com.softsynth.jsyn.LineOut;
import com.softsynth.jsyn.Synth;
import com.softsynth.jsyn.SynthAlert;
import com.softsynth.jsyn.SynthCircuit;
import com.softsynth.jsyn.SynthException;
import com.softsynth.jsyn.SynthNote;
import com.softsynth.jsyn.util.BussedVoiceAllocator;

/**
 * @author Paul Reiners
 */
public abstract class AbstractJITurmite extends JApplet implements Runnable,
		ActionListener {

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 3256444702969770552L;

	protected static final int CATCH_UP_TIME_IN_MILLISECONDS = 5000;

	/* Declare Synthesis Objects here */
	LineOut unitOut;

	BussedVoiceAllocator allocator;

	boolean go = false;

	final static int MAX_NOTES = 4;

	protected final static int DEFAULT_RADIUS = 48;

	protected final static int WIDTH = 600;

	protected final static int HEIGHT = 500;

	protected Turmite[] turmites;

	protected JButton startStopBtn;

	protected TurmiteComponent turmiteComponent;

	private FiveLimitLattice lattice;

	private Container cp;

	private int[][] xCache;

	private int[][] yCache;

	private int[][] colorCache;

	private int cacheIndex;

	protected String[] description;

	protected Random rand = new Random();

	protected int radius = DEFAULT_RADIUS;

	/*
	 * Ê* Setup synthesis by overriding start() method. Ê
	 */
	public void start() {
		try {
			startSynthEngine();

			JPanel supplementaryControls = getSupplementaryControls();

			JPanel southPanel = new JPanel();
			southPanel.setLayout(new GridLayout(1, 1));
			southPanel.add(supplementaryControls);

			cp.add(BorderLayout.SOUTH, southPanel);

			/* Synchronize Java display. */
			getParent().validate();
			getToolkit().sync();

			initTurmite();
		} catch (SynthException e) {
			SynthAlert.showError(this, e);
		}
	}

	/**
	 *  
	 */
	private void startSynthEngine() {
		Synth.startEngine(0);
		/* Your setup code goes here. */
		//			Create a voice allocator and connect it to a LineOut.
		allocator = new BussedVoiceAllocator(MAX_NOTES) {
			public SynthCircuit makeVoice() throws SynthException {
				SynthNote circ = new SineCircuit();
				return addVoiceToMix(circ); // mix through bus writer
			}
		};

		unitOut = new LineOut();
		allocator.getOutput().connect(0, unitOut.input, 0);
		allocator.getOutput().connect(0, unitOut.input, 1);

		unitOut.start();
	}

	/**
	 *  
	 */
	protected void startNotePlayerThread() {
		// start thread that plays notes
		Thread thread = new Thread(this);
		go = true;
		thread.start();
	}

	public void init() {
		turmiteComponent = new TurmiteComponent(
				"Just Intonation Five-Limit Lattice");
		cp = getContentPane();
		cp.add(BorderLayout.CENTER, turmiteComponent);

		try {
			lattice = new FiveLimitLattice(radius, radius);
		} catch (InvalidIntervalException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		xCache = new int[2][4];
		yCache = new int[2][4];
		colorCache = new int[2][4];
		cacheIndex = 0;
	}

	abstract JPanel createTurmiteSelectionPanel();

	/*
	 * Ê* Clean up synthesis by overriding stop() method. Ê
	 */
	public void stop() {
		try {
			/* Your cleanup code goes here. */
			// tell song thread to finish
			go = false;
			removeAll(); // remove components from Applet panel.
			Synth.stopEngine();
		} catch (SynthException e) {
			SynthAlert.showError(this, e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		// calculate tempo
		int ticksPerBeat = (int) (Synth.getTickRate() * 0.2);
		// calculate time ahead of any system latency
		int advanceTime = (int) (Synth.getTickRate() * 0.5);
		// time for next note to start
		int nextTime = Synth.getTickCount() + advanceTime;
		// note is ON for half the duration
		int onTime = ticksPerBeat / 2;

		try {
			do {
				for (int i = 0; i < turmites.length; i++) {
					int x = turmites[i].getX();
					int y = turmites[i].getY();
					xCache[i][cacheIndex] = x;
					yCache[i][cacheIndex] = y;
					colorCache[i][cacheIndex] = turmites[i].getColor();

					createNote(ticksPerBeat, nextTime, onTime, i);
				}

				nextTime += ticksPerBeat;
				boolean moved = move();
				if (!moved) {
					break;
				}

				cacheIndex = (cacheIndex + 1) % 4;

				// wake up before we need to play note to cover system latency
				Synth.sleepUntilTick(nextTime - advanceTime);
			} while (go);

			System.out.println("Exiting run() loop.");
		} catch (SynthException e) {
			System.err.println("Song exiting." + e);
		}
	}

	abstract protected boolean move();

	private void createNote(int ticksPerBeat, int nextTime, int onTime,
			int turmiteIndex) {
		// allocate a new note, stealing one if necessary
		SynthNote note = (SynthNote) allocator.steal(nextTime, nextTime
				+ (2 * ticksPerBeat));
		// calculate frequency from Turmite
		int index = (cacheIndex + turmiteIndex) % 4;
		int x = xCache[turmiteIndex][index];
		int y = yCache[turmiteIndex][index];
		double frequency = lattice.getFrequency(x, y);
		int mult = 1;
		for (int i = 0; i < colorCache[turmiteIndex][index]; i++) {
			mult = mult << 1;
		}
		frequency /= mult;
		// play note using event buffer for accurate timing
		note.noteOnFor(nextTime, onTime, frequency, 0.15);
	}

	abstract JComponent[] getExtraControls();

	JPanel getSupplementaryControls() {
		JPanel pnlSupplementaryControls = new JPanel();

		JComponent[] extraControls = getExtraControls();

		JPanel pnlCommandButtons;

		JPanel turmiteSelectionPanel = createTurmiteSelectionPanel();
		if (turmiteSelectionPanel != null) {
			pnlCommandButtons = new JPanel(new GridLayout(1,
					2 + extraControls.length));
			pnlCommandButtons.add(turmiteSelectionPanel);
		} else {
			pnlCommandButtons = new JPanel(new GridLayout(1,
					1 + extraControls.length));
		}

		startStopBtn = new JButton();
		startStopBtn.setText("Start");
		startStopBtn.setActionCommand("startstop");
		startStopBtn.addActionListener(this);
		pnlCommandButtons.add(startStopBtn);
		for (int i = 0; i < extraControls.length; i++) {
			pnlCommandButtons.add(extraControls[i]);
		}

		pnlSupplementaryControls.add(pnlCommandButtons);

		return pnlSupplementaryControls;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		if ("startstop".equals(e.getActionCommand())) {
			if (go) {
				startStopBtn.setText("Start");
				go = false;
			} else {
				restart();
			}
		}
	}

	private void restart() {
		stopNotes();

		startStopBtn.setText("Stop");

		// start thread that plays notes
		startNotePlayerThread();
	}

	/**
	 *  
	 */
	void stopNotes() {
		allocator.clear();
		try {
			Thread.sleep(CATCH_UP_TIME_IN_MILLISECONDS);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 *  
	 */
	void startTurmite() {
		initTurmite();

		startNotePlayerThread();
		startStopBtn.setText("Stop");
	}

	abstract int getNextTurmiteIndex();

	abstract int[] getNextTurmiteIndices();

	abstract boolean playDuet();

	/**
	 *  
	 */
	protected void initTurmite() {
		Pattern pattern = new Pattern(radius, radius);
		if (playDuet()) {
			turmites = new Turmite[2];
			int[] nextTurmiteIndices = getNextTurmiteIndices();
			turmites[0] = new Turmite(
					Turmite.ED_PEGG_JRS_TURMITES[nextTurmiteIndices[0]],
					pattern);
			turmites[1] = new Turmite(
					Turmite.ED_PEGG_JRS_TURMITES[nextTurmiteIndices[1]],
					pattern);
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
		} else {
			turmites = new Turmite[] { new Turmite(
					Turmite.ED_PEGG_JRS_TURMITES[getNextTurmiteIndex()],
					pattern) };
		}
	}

	/**
	 * @return
	 */
	protected boolean updateTurmitesAndTheirWorld() {
		int[] x = new int[turmites.length];
		int[] y = new int[turmites.length];
		for (int i = 0; i < turmites.length; i++) {
			x[i] = turmites[i].getX();
			y[i] = turmites[i].getY();
			boolean moved = turmites[i].move();
			if (!moved) {
				System.out.println("A turmite has went out of bounds.");
				startStopBtn.setText("Start");

				return false;
			}
		}

		turmiteComponent.updateWorld(turmites, x, y);

		return true;
	}
}