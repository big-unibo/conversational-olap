package it.unibo.trummervocalization.uct;

import it.unibo.trummervocalization.cache.Cache;
import it.unibo.trummervocalization.speech.Speech;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class Inner extends UctNode {

	private final List<UctNode> children;

	public Inner(Cache cache, Speech speech, List<UctNode> children) {
		super(cache, speech);
		this.children = Collections.unmodifiableList(children);
	}

	private Optional<UctNode> getMaxRewardChild() {
		return this.children.stream().filter(c -> c.nVisits > 0).max(Comparator.comparingDouble(c -> c.accReward / c.nVisits));
	}

	@Override
	public Optional<UctNode> nextNode(long timeout) {
		long start = System.currentTimeMillis();
		while (System.currentTimeMillis() - start < timeout) {
			this.sample();
		}
		Optional<UctNode> maxRewardChild = this.getMaxRewardChild();
		while (maxRewardChild.isEmpty()) {
			this.sample();
			maxRewardChild = this.getMaxRewardChild();
		}
		return maxRewardChild;
	}

	@Override
	protected double sample() {
		Optional<UctNode> unvisited = this.children.stream().filter(c -> c.nVisits == 0).findAny();
		Optional<UctNode> maxUct = unvisited.or(() -> this.children.stream().filter(c -> c.nVisits > 0).max(
				Comparator.comparingDouble(c -> c.accReward / c.nVisits + Math.sqrt(2 * Math.log(this.nVisits) / c.nVisits))));
		double reward = maxUct.map(UctNode::sample).orElse(0.0);
		this.accReward += reward;
		this.nVisits += 1;
		return reward;
	}

}
