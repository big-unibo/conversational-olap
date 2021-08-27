package it.unibo.trummervocalization.uct;

import it.unibo.trummervocalization.cache.Cache;
import it.unibo.trummervocalization.speech.Speech;
import java.util.Optional;

public abstract class UctNode {

	protected final Cache cache;
	protected final Speech speech;
	protected double accReward;
	protected int nVisits;

	public UctNode(Cache cache, Speech speech) {
		this.cache = cache;
		this.speech = speech;
		this.accReward = 0;
		this.nVisits = 0;
	}

	public Speech getSpeech() {
		return this.speech;
	}

	public abstract Optional<UctNode> nextNode(long timeout);

	protected abstract double sample();

}
