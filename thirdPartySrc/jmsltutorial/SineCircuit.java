/** JMSL Tutorial.
	Simple JSyn SynthNote whose noteOn() method simply sets freq/amp of a sine osc with an amp envelope
	@author Nick Didkovsky, (c) 2000 Nick Didkovsky
*/

package jmsltutorial;
import com.softsynth.jsyn.*;

public class SineCircuit extends SynthNote {

	SineOscillator myOsc;
	EnvelopePlayer envPlayer;
	SynthEnvelope env;

	public SineCircuit() {
		super();
		/* Make waveform unit generator. */
		add(myOsc = new SineOscillator());
		add(envPlayer = new EnvelopePlayer());
		envPlayer.output.connect(myOsc.amplitude);

		/* Make ports on internal units appear as ports on circuit. */
		addPort(amplitude = envPlayer.amplitude);
		addPort(frequency = myOsc.frequency, "frequency");
		addPort(output = myOsc.output);

		double[] envData = { 0.01, 1.0, 0.2, 0.5, 0.1, 0.0 };
		env = new SynthEnvelope(envData);

		/* Set signal type for frequency control so that we can operate in hertz. */
		frequency.set(0.0);
		amplitude.set(0.0);
	}

	/** Bang the circuit to make a sound */
	public void noteOn(int time, double frq, double ampl) {
		start(time);
		frequency.set(time, frq); /* Hz */
		amplitude.set(time, ampl);
		envPlayer.envelopePort.clear(time);
		envPlayer.envelopePort.queue(time, env, 0, 2);
		envPlayer.envelopePort.queueLoop(time, env, 1, 1);

	}

	public void noteOff(int time) {
		envPlayer.envelopePort.clear(time);
		envPlayer.envelopePort.queue(time, env, env.getNumFrames() - 1, 1, Synth.FLAG_AUTO_STOP);
	}
}
