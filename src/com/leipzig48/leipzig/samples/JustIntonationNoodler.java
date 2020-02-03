/*
 * Created on Dec 5, 2004
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

import java.applet.Applet;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JPanel;

import com.leipzig48.leipzig.core.Interval;
import com.softsynth.jsyn.AppletFrame;
import com.softsynth.jsyn.LineOut;
import com.softsynth.jsyn.Synth;
import com.softsynth.jsyn.SynthAlert;
import com.softsynth.jsyn.SynthCircuit;
import com.softsynth.jsyn.SynthException;
import com.softsynth.jsyn.SynthNote;
import com.softsynth.jsyn.circuits.RingModBell;
import com.softsynth.jsyn.util.BussedVoiceAllocator;
import com.softsynth.jsyn.view102.SynthScope;

/**
 * @author Paul Reiners
 */
public class JustIntonationNoodler extends Applet implements Runnable,
		ActionListener {

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 3257850982670217265L;

	/* Declare Synthesis Objects here */
	private static final int MAX_NUM_DENOM = 2048;

	LineOut unitOut;

	BussedVoiceAllocator allocator;

	SynthScope scope;

	boolean go = false;

	final static int MAX_NOTES = 4;

	private final static int WIDTH = 600;

	private final static int HEIGHT = 500;

	int[] state;

	private Random rand = new Random();

	private JButton startStopBtn;

	/* Can be run as either an application or as an applet. */
	public static void main(String args[]) {
		JustIntonationNoodler applet = new JustIntonationNoodler();
		AppletFrame frame = new AppletFrame("Just Intonation Noodler", applet);
		frame.setSize(WIDTH, HEIGHT);
		frame.setVisible(true);
		frame.test();
	}

	/*
	 * Ê* Setup synthesis by overriding start() method. Ê
	 */
	public void start() {
		try {
			Synth.startEngine(0);
			/* Your setup code goes here. */
			//			Create a voice allocator and connect it to a LineOut.
			allocator = new BussedVoiceAllocator(MAX_NOTES) {
				public SynthCircuit makeVoice() throws SynthException {
					SynthNote circ = new RingModBell();
					return addVoiceToMix(circ); // mix through bus writer
				}
			};

			unitOut = new LineOut();
			allocator.getOutput().connect(0, unitOut.input, 0);
			allocator.getOutput().connect(0, unitOut.input, 1);

			unitOut.start();

			//			Show signal on a scope.
			scope = new SynthScope();
			scope.createProbe(allocator.getOutput(), "Bus out", Color.yellow);
			scope.finish();
			scope.hideControls();

			JPanel supplementaryControls = getSupplementaryControls();

			setLayout(new GridLayout(2, 1));
			add(supplementaryControls);
			add(scope);

			/* Synchronize Java display. */
			getParent().validate();
			getToolkit().sync();

			// start thread that plays notes
			Thread thread = new Thread(this);
			go = true;
			thread.start();
		} catch (SynthException e) {
			SynthAlert.showError(this, e);
		}
	}

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

		Interval currentInterval = Interval.MIDDLE_C;

		try {
			do {
				if (rand.nextBoolean()) {
					if (rand.nextBoolean()) {
						currentInterval = currentInterval
								.add(Interval.PERFECT_FIFTH);
					} else {
						currentInterval = currentInterval
								.subtract(Interval.PERFECT_FIFTH);
					}
				} else {
					if (rand.nextBoolean()) {
						currentInterval = currentInterval
								.add(Interval.MAJOR_THIRD);
					} else {
						currentInterval = currentInterval
								.subtract(Interval.MAJOR_THIRD);
					}
				}
				if (currentInterval.getNumerator().longValue() > MAX_NUM_DENOM
						|| currentInterval.getDenominator().longValue() > MAX_NUM_DENOM) {
					currentInterval = Interval.MIDDLE_C;
				}
				createNote(ticksPerBeat, nextTime, onTime, currentInterval);

				nextTime += ticksPerBeat;

				// wake up before we need to play note to cover system latency
				Synth.sleepUntilTick(nextTime - advanceTime);
			} while (go);
		} catch (SynthException e) {
			System.err.println("Song exiting." + e);
		}
	}

	private void createNote(int ticksPerBeat, int nextTime, int onTime,
			Interval interval) {
		// allocate a new note, stealing one if necessary
		SynthNote note = (SynthNote) allocator.steal(nextTime, nextTime
				+ (2 * ticksPerBeat));
		// calculate pitch from Interval
		double frequency = interval.getFrequency();
		// play note using event buffer for accurate timing
		note.noteOnFor(nextTime, onTime, frequency, 0.15);
	}

	JPanel getSupplementaryControls() {
		JPanel pnlSupplementaryControls = new JPanel();

		JPanel pnlCommandButtons = new JPanel(new GridLayout(1, 1));

		startStopBtn = new JButton();
		startStopBtn.setText("Stop");
		startStopBtn.setActionCommand("startstop");
		startStopBtn.addActionListener(this);
		pnlCommandButtons.add(startStopBtn);

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
		allocator.clear();
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		startStopBtn.setText("Stop");

		// start thread that plays notes
		Thread thread = new Thread(this);
		go = true;
		thread.start();
	}
}