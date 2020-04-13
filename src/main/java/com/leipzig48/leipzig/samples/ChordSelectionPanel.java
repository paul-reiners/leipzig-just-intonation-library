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
package com.leipzig48.leipzig.samples;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import com.jsyn.JSyn;
import com.jsyn.Synthesizer;
import com.jsyn.instruments.SubtractiveSynthVoice;
import com.jsyn.unitgen.LineOut;
import com.jsyn.unitgen.UnitVoice;
import com.jsyn.util.VoiceAllocator;
import com.leipzig48.leipzig.core.FiveLimitChord;
import com.leipzig48.leipzig.core.Interval;
import com.leipzig48.leipzig.exceptions.InvalidIntervalException;
import com.softsynth.shared.time.TimeStamp;

/**
 * @author Paul Reiners
 */
class ChordSelectionPanel extends JPanel implements ActionListener {

    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 3616727171339857972L;

    static String noChordString = "No chord";

    static String majorTriadString = "The Major Triad";

    static String minorTriadString = "The Minor Triad";

    static String condissonantTriad1String = "Condissonant Triad 1";

    static String condissonantTriad2String = "Condissonant Triad 2";

    static String majorSeventhChordString = "The Major-Seventh Chord";

    static String minorSeventhChordString = "The Minor-Seventh Chord";

    static String majorNinthChordString = "The Major-Ninth Chord";

    static String minorNinthChordString = "The Minor-Ninth Chord";

    static String actionPrefix = "chord:";

    private final JRadioButton noChordButton;

    private final JRadioButton majorTriadButton;

    private final JRadioButton minorTriadButton;

    private final JRadioButton condissonantTriad1Button;

    private final JRadioButton condissonantTriad2Button;

    private final JRadioButton majorSeventhChordButton;

    private final JRadioButton minorSeventhChordButton;

    private final JRadioButton majorNinthChordButton;

    private final JRadioButton minorNinthChordButton;

    private final HashMap<FiveLimitChord, JRadioButton> chordToButton;

    private static final int MAX_VOICES = 8;
    private Synthesizer synth;
    private VoiceAllocator allocator;
    private final double secondsPerBeat = 0.6;

    /**
     *
     */
    ChordSelectionPanel() {
        super(new BorderLayout());

        //Create the radio buttons.
        chordToButton = new HashMap();

        noChordButton = new JRadioButton(noChordString);
        noChordButton.setMnemonic(KeyEvent.VK_H);
        noChordButton.setActionCommand(actionPrefix + noChordString);
        noChordButton.setSelected(true);

        majorTriadButton = new JRadioButton(majorTriadString);
        majorTriadButton.setMnemonic(KeyEvent.VK_M);
        majorTriadButton.setActionCommand(actionPrefix + majorTriadString);
        chordToButton.put(FiveLimitChord.MAJOR_TRIAD, majorTriadButton);

        minorTriadButton = new JRadioButton(minorTriadString);
        minorTriadButton.setMnemonic(KeyEvent.VK_I);
        minorTriadButton.setActionCommand(actionPrefix + minorTriadString);
        chordToButton.put(FiveLimitChord.MINOR_TRIAD, minorTriadButton);

        condissonantTriad1Button = new JRadioButton(condissonantTriad1String);
        condissonantTriad1Button.setMnemonic(KeyEvent.VK_C);
        condissonantTriad1Button.setActionCommand(actionPrefix
                + condissonantTriad1String);
        chordToButton.put(FiveLimitChord.CONDISSONANT_TRIADS[0],
                condissonantTriad1Button);

        condissonantTriad2Button = new JRadioButton(condissonantTriad2String);
        condissonantTriad2Button.setMnemonic(KeyEvent.VK_O);
        condissonantTriad2Button.setActionCommand(actionPrefix
                + condissonantTriad2String);
        chordToButton.put(FiveLimitChord.CONDISSONANT_TRIADS[1],
                condissonantTriad2Button);

        majorSeventhChordButton = new JRadioButton(majorSeventhChordString);
        majorSeventhChordButton.setMnemonic(KeyEvent.VK_S);
        majorSeventhChordButton.setActionCommand(actionPrefix
                + majorSeventhChordString);
        chordToButton.put(FiveLimitChord.MAJOR_SEVENTH_CHORD,
                majorSeventhChordButton);

        minorSeventhChordButton = new JRadioButton(minorSeventhChordString);
        minorSeventhChordButton.setMnemonic(KeyEvent.VK_E);
        minorSeventhChordButton.setActionCommand(actionPrefix
                + minorSeventhChordString);
        chordToButton.put(FiveLimitChord.MINOR_SEVENTH_CHORD,
                minorSeventhChordButton);

        majorNinthChordButton = new JRadioButton(majorNinthChordString);
        majorNinthChordButton.setMnemonic(KeyEvent.VK_N);
        majorNinthChordButton.setActionCommand(actionPrefix
                + majorNinthChordString);
        chordToButton.put(FiveLimitChord.MAJOR_NINTH_CHORD,
                majorNinthChordButton);

        minorNinthChordButton = new JRadioButton(minorNinthChordString);
        minorNinthChordButton.setMnemonic(KeyEvent.VK_T);
        minorNinthChordButton.setActionCommand(actionPrefix
                + minorNinthChordString);
        chordToButton.put(FiveLimitChord.MINOR_NINTH_CHORD,
                minorNinthChordButton);

        //Group the radio buttons.
        ButtonGroup group = new ButtonGroup();
        group.add(noChordButton);
        group.add(majorTriadButton);
        group.add(minorTriadButton);
        group.add(condissonantTriad1Button);
        group.add(condissonantTriad2Button);
        group.add(majorSeventhChordButton);
        group.add(minorSeventhChordButton);
        group.add(majorNinthChordButton);
        group.add(minorNinthChordButton);

        registerListener(this);

        //Put the radio buttons in a column in a panel.
        JPanel radioPanel = new JPanel(new GridLayout(0, 1));
        radioPanel.add(noChordButton);
        radioPanel.add(majorTriadButton);
        radioPanel.add(minorTriadButton);
        radioPanel.add(condissonantTriad1Button);
        radioPanel.add(condissonantTriad2Button);
        radioPanel.add(majorSeventhChordButton);
        radioPanel.add(minorSeventhChordButton);
        radioPanel.add(majorNinthChordButton);
        radioPanel.add(minorNinthChordButton);

        add(radioPanel, BorderLayout.LINE_START);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        initializeMusic();
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

    /**
     *
     */
    void registerListener(ActionListener listener) {
        //Register a listener for the radio buttons.
        noChordButton.addActionListener(listener);
        majorTriadButton.addActionListener(listener);
        minorTriadButton.addActionListener(listener);
        condissonantTriad1Button.addActionListener(listener);
        condissonantTriad2Button.addActionListener(listener);
        majorSeventhChordButton.addActionListener(listener);
        minorSeventhChordButton.addActionListener(listener);
        majorNinthChordButton.addActionListener(listener);
        minorNinthChordButton.addActionListener(listener);
    }

    /*
     * Listens to the radio buttons.
     *
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
        String actionCommand = e.getActionCommand();
        System.out.println(actionCommand + " chosen");
    }

    private void playMeasure1(double time, Interval[] intervals) throws InvalidIntervalException {
        double[] freqs = new double[intervals.length];
        for (int i = 0; i < intervals.length; i++) {
            freqs[i] = intervals[i].getFrequency();
        }
        playChord1(time, freqs);
    }

    private void playChord1(double time, double[] freqs) {
        // on time over note duration
        double dutyCycle = 0.8;
        double dur = dutyCycle * secondsPerBeat;
        playChord(time, dur, freqs);
        time += secondsPerBeat;
        playChord(time, dur, freqs);
        time += secondsPerBeat;
        playChord(time, dur * 0.25, freqs);
        time += secondsPerBeat * 0.25;
        playChord(time, dur * 0.25, freqs);
        time += secondsPerBeat * 0.75;
        playChord(time, dur, freqs);
    }

    private void playChord(double time, double dur, double[] freqs) {
        for (int i = 0; i < freqs.length; i++) {
            noteOn(time, i, freqs[i]);
        }
        double offTime = time + dur;
        for (int i = 0; i < freqs.length; i++) {
            noteOff(offTime, i);
        }
    }

    private void noteOff(double time, int tag) {
        allocator.noteOff(tag, new TimeStamp(time));
    }

    private void noteOn(double time, int tag, double frequency) {
        double amplitude = 0.2;
        TimeStamp timeStamp = new TimeStamp(time);
        allocator.noteOn(tag, frequency, amplitude, timeStamp);
    }

    /**
     * Number of seconds to generate music in advance of presentation-time.
     */
    private void catchUp(double time) throws InterruptedException {
        double advance = 0.2;
        synth.sleepUntil(time - advance);
    }

    /**
     * Returns a Chord, or null if the name was invalid.
     */
    static FiveLimitChord createChord(String name) {
        if (name.equals(majorTriadString)) {
            return FiveLimitChord.MAJOR_TRIAD;
        } else if (name.equals(minorTriadString)) {
            return FiveLimitChord.MINOR_TRIAD;
        } else if (name.equals(condissonantTriad1String)) {
            return FiveLimitChord.CONDISSONANT_TRIADS[0];
        } else if (name.equals(condissonantTriad2String)) {
            return FiveLimitChord.CONDISSONANT_TRIADS[1];
        } else if (name.equals(majorSeventhChordString)) {
            return FiveLimitChord.MAJOR_SEVENTH_CHORD;
        } else if (name.equals(minorSeventhChordString)) {
            return FiveLimitChord.MINOR_SEVENTH_CHORD;
        } else if (name.equals(majorNinthChordString)) {
            return FiveLimitChord.MAJOR_NINTH_CHORD;
        } else if (name.equals(minorNinthChordString)) {
            return FiveLimitChord.MINOR_NINTH_CHORD;
        } else {
            System.err.println("Incorrect chord name: " + name);
            return null;
        }
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
        JComponent newContentPane = new ChordSelectionPanel();
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }

    /**
     * @return Returns the actionPrefix.
     */
    static String getActionPrefix() {
        return actionPrefix;
    }

    void setChordEnabled(FiveLimitChord chord, boolean enabled) {
        ((JRadioButton) chordToButton.get(chord)).setEnabled(enabled);
    }
}