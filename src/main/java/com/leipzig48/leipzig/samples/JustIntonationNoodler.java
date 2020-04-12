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

import com.jsyn.Synthesizer;
import com.jsyn.scope.AudioScope;
import com.jsyn.swing.JAppletFrame;
import com.jsyn.unitgen.LineOut;
import com.jsyn.unitgen.SineOscillator;
import com.jsyn.unitgen.SineOscillatorPhaseModulated;
import com.leipzig48.leipzig.core.Interval;
import com.softsynth.jsyn.Synth;
import com.softsynth.jsyn.SynthAlert;
import com.softsynth.jsyn.SynthException;
import com.softsynth.jsyn.SynthNote;
import com.softsynth.jsyn.util.BussedVoiceAllocator;
import com.softsynth.jsyn.view102.SynthScope;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import com.jsyn.JSyn;
import com.softsynth.shared.time.TimeStamp;

/**
 * @author Paul Reiners
 */
public class JustIntonationNoodler extends JApplet implements Runnable,
		ActionListener {

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 3257850982670217265L;

	/* Declare Synthesis Objects here */
	private static final int MAX_NUM_DENOM = 2048;

	BussedVoiceAllocator allocator;

	AudioScope scope;

	boolean go = false;

	final static int MAX_NOTES = 4;

	private final static int WIDTH = 600;

	private final static int HEIGHT = 500;

	int[] state;

	private Random rand = new Random();

	private JButton startStopBtn;
	SineOscillatorPhaseModulated carrier;
	LineOut lineOut;
	SineOscillator modulator;
	private Synthesizer synth;

	/* Can be run as either an application or as an applet. */
	public static void main(String args[]) {
		JustIntonationNoodler applet = new JustIntonationNoodler();
		JAppletFrame frame = new JAppletFrame("Just Intonation Noodler", applet);
		frame.setSize(WIDTH, HEIGHT);
		frame.setVisible(true);
		frame.test();
	}

	/*
	 * Ê* Setup synthesis by overriding start() method. Ê
	 */
	public void start() {
		try {
			synth = JSyn.createSynthesizer();
			// Add a tone generator.
			synth.add(modulator = new SineOscillator());
			// Add a trigger.
			synth.add(carrier = new SineOscillatorPhaseModulated());
			// Add an output mixer.
			synth.add(lineOut = new LineOut());

			modulator.output.connect(carrier.modulation);
			carrier.output.connect(0, lineOut.input, 0);
			carrier.output.connect(0, lineOut.input, 1);
			modulator.amplitude.setup(0.0, 1.0, 10.0);
			carrier.amplitude.setup(0.0, 0.25, 1.0);

			//			Show signal on a scope.
			scope = new AudioScope(synth);
			scope.addProbe(carrier.output);
			scope.addProbe(modulator.output);
			scope.setTriggerMode(AudioScope.TriggerMode.NORMAL);
			scope.getView().setControlsVisible(true);

			JPanel supplementaryControls = getSupplementaryControls();

			setLayout(new GridLayout(2, 1));
			add(supplementaryControls);
			add(scope.getView(), BorderLayout.CENTER);
			scope.start();
			validate();

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
		Synth.initialize();
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
		// calculate pitch from Interval
		double freq = interval.getFrequency();

		// Schedule a note on and off.
		// Get synthesizer time in seconds.
		double timeNow = synth.getCurrentTime();
		// Advance to a near future time so we have a clean start.
		TimeStamp timeStamp = new TimeStamp(timeNow + 0.5);

		carrier.noteOn(freq, 0.5, timeStamp);
		carrier.noteOff(timeStamp.makeRelative(onTime));
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