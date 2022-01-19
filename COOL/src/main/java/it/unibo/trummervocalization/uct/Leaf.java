package it.unibo.trummervocalization.uct;

import it.unibo.trummervocalization.cache.Cache;
import it.unibo.trummervocalization.speech.Speech;
import java.util.Optional;

public class Leaf extends UctNode {

	public Leaf(Cache cache, Speech speech) {
		super(cache, speech);
	}

	@Override
	public Optional<UctNode> nextNode(long timeout) {
		return Optional.empty();
	}

	@Override
	protected double sample() {
		double reward = this.speech.computeProbability(this.cache.getSample());
		this.accReward += reward;
		this.nVisits += 1;
		return reward;
	}

}
