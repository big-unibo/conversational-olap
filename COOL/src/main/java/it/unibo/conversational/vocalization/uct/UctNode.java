package it.unibo.conversational.vocalization.uct;

import it.unibo.conversational.vocalization.cache.Cache;
import it.unibo.conversational.vocalization.speech.Speech;
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
