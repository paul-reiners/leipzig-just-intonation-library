/** JMSL Tutorial.
	JMSL Instrument which uses a JSyn SynthNote to make a sound.
	Good instrument design practice follows these principles:<br>
	1) Keep JSyn stuff out of the constructor<br>
	2) Specify MusicDevice and Mixer classname in constructor<br>
	3) implement AttributeBuildable and do all JSyn building in buildFromAttributes()<br><br>
	The idea is that you should be able to make a new Instrument without JSyn actually running.
	Then you should be able to anonymously query this new object for its MusicDevice, and open() it
	without knowing what it is.
	Then you should be able to add this instrument to a JMSLMixerContainer, again witout knowing the details of
	which Mixer implementation it uses.
	<br><br>
	
	@author Nick Didkovsky, (c) 2000 Nick Didkovsky
*/

package jmsltutorial;
import com.softsynth.jmsl.*;
import com.softsynth.jmsl.util.AttributeBuildable;

import java.awt.*;
import java.awt.event.*;

/**  This Instrument owns a simple JSyn SineCircuit, and plays it with pitch and amplitude */
public class SineInstrument extends InstrumentAdapter implements AttributeBuildable {

	SineCircuit circuit;
	protected static final boolean DEBUG = false;

	/** Constructor makes no references to JSyn units.  Shoud be able to execute without synth engine running */
	public SineInstrument() {
		this.setMixerClassName("com.softsynth.jmsl.jsyn.JSynMixer");
		this.setMusicDevice(com.softsynth.jmsl.jsyn.JSynMusicDevice.instance());
		buildDimensionNameSpace();
	}

	/** Constructing a DimensionNameSpace for your instrument is not required but is highly advisable */
	private void buildDimensionNameSpace() {
		DimensionNameSpaceAdapter dns = new DimensionNameSpaceAdapter();
		dns.setDimensionName(0, "duration");
		dns.setLimits(0, 0, 8);
		dns.setDefault(0, 1);

		dns.setDimensionName(1, "frequency");
		dns.setLimits(1, 20, 1500);
		dns.setDefault(1, 440);

		dns.setDimensionName(2, "amplitude");
		dns.setLimits(2, 0, 1);
		dns.setDefault(2, 0.4);

		dns.setDimensionName(3, "hold");
		dns.setLimits(3, 0, 8);
		dns.setDefault(3, 0.8);

		// add extra dimensions here if your circuit has SynthInputs that you want to control

		this.setDimensionNameSpace(dns);
	}

	/** We don't want to do any JSyn allocation in the constructor.  Put all JSyn stuff in this method, which
	 * is the AttributeBuildable interface. 
	 */
	public void buildFromAttributes() throws Exception {
		circuit = new SineCircuit();
	}

	/** Instrument interface.  
	 * @return the output of the circuit 
	 * */
	public Object getOutput() {
		return circuit.output;
	}

	/** In this case return 1.  Note this method will fail if synth not running since it refers to a live JSyn object.
	 * You could hardcode the returned 1 if this were a problem 
	 * */
	public int getNumOutputs() {
		return circuit.output.getNumParts();
	}

	/** Specify all sonic behavior in play() 
	 * A MusicShape calls its Instrument's play() method for each element, and waits until the time 
	 * returned by play() before proceeding to the next element
	 *  
	 * @return updated playTime */
	public double play(double playTime, double timeStretch, double dar[]) {
		double duration = dar[0];
		double frequency = dar[1];
		double amplitude = dar[2];
		double hold = dar[3];
		/* JMSL runs on a different clock than JSyn, so convert. */
		int itime = (int) JMSL.clock.timeToNative(playTime);
		int offtime = itime + (int) (JMSL.clock.getNativeRate() * hold * timeStretch);
		circuit.noteOn(itime, frequency, amplitude);
		circuit.noteOff(offtime);

		JMSL.out.println("SineInstrument playing at " + playTime + ", freq=" + frequency);

		return playTime + duration * timeStretch;
	}

	/** optionally define update().  update() might get called while a sound is sustaining.
	 * It is used in JMSL Score for example, to change timbre of tied notes , without re-attacking 
	 * The source below looks almost like play(), except that it simply sets frequency and amplitude without 
	 * calling noteOn (which calls setStage(0), which in the case of SineCircuit triggers the amp envelope)
	 * update() is not automatically called by MusicShape.  It is called in JMSL Score for notes that are tied-in.
	 * 
	 * @return updated playTime */
	public double update(double playTime, double timeStretch, double dar[]) {
		double duration = dar[0];
		double frequency = dar[1];
		double amplitude = dar[2];
		/* JMSL runs on a different clock than JSyn, so convert. */
		int itime = (int) JMSL.clock.timeToNative(playTime);
		circuit.frequency.set(itime, frequency);
		circuit.amplitude.set(itime, amplitude);
		return playTime + duration * timeStretch;
	}

	/** Test your instrument.  Note that we never refer to any JSyn explicitely in this method! 
	 * If your instrument can follow this example, it's nicely designed.
	 * */
	public static void main(String args[]) {
		Frame myFrame = new Frame("Close to quit");
		myFrame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				if (DEBUG) {
					System.out.println("WindowEvent has occurred: " + e);
				}
				// close any open MusicDevices 
				JMSL.closeMusicDevices();
				System.exit(0);
			}
		});

		try {
			// create an instrument, anonymously get its MusicDevice and open it
			SineInstrument ins = new SineInstrument();
			MusicDevice dev = ins.getMusicDevice();
			dev.edit(myFrame);
			dev.open();
			ins.buildFromAttributes();

			// Add instrument to a mixer
			JMSLMixerContainer mixer = new JMSLMixerContainer();
			mixer.start();
			mixer.addInstrument(ins);
			myFrame.add(mixer.getPanAmpControlPanel());
			//myFrame.setSize(mixer.getSuggestedFrameSize());
			myFrame.pack();
			myFrame.setVisible(true);

			// Make a MusicShape to play the instrument
			MusicShape shape = new MusicShape(ins.getDimensionNameSpace());
			shape.add(2.0, 440, 0.62, 1.8);
			shape.add(1.0, 880, 0.62, 0.5);
			shape.add(1.0, 660, 0.62, 0.3);
			shape.add(1.0, 770, 0.62, 0.1);
			shape.setInstrument(ins);

			shape.setRepeats(1000);
			shape.launch(JMSL.now());
		}
		catch (Exception e) {
			System.out.println("SineInstrument.main() trouble: " + e);
		}
	}

}