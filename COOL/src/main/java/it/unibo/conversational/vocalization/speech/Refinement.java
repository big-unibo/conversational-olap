package it.unibo.conversational.vocalization.speech;

import it.unibo.conversational.vocalization.data.Dimension;
import it.unibo.conversational.vocalization.data.Member;
import it.unibo.conversational.vocalization.query.Aggregate.Function;
import it.unibo.conversational.vocalization.utils.Common;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.commons.lang3.tuple.Pair;

public class Refinement extends Speech {

	protected final Speech prefix;
	protected final Pair<Dimension, Member> restriction;
	protected final double variationFactor;
	private final Baseline baseline;
	private final Map<Dimension, Map<Member, Double>> knowledge;

	public Refinement(Speech prefix, Dimension dimension, Member member, double variationFactor) {
		super(prefix.aggregate, prefix.members, prefix.standardDeviation, prefix.probabilityRange, prefix.nDigits);
		this.prefix = prefix;
		this.restriction = Pair.of(dimension, member);
		this.variationFactor = variationFactor;
		this.baseline = findBaseline(this);
		this.knowledge = findKnowledge(this);
	}

	private static Baseline findBaseline(Speech speech) {
		if (speech instanceof Baseline) return (Baseline) speech;
		else if (speech instanceof Dummy) return findBaseline(((Dummy) speech).prefix);
		else return findBaseline(((Refinement) speech).prefix);
	}

	private static Map<Dimension, Map<Member, Double>> findKnowledge(Speech speech) {
		Map<Dimension, Map<Member, Double>> knowledge = new HashMap<>();
		while (!(speech instanceof Baseline)) {
			if (speech instanceof Refinement) {
				Pair<Dimension, Member> res = ((Refinement) speech).restriction;
				double variation = ((Refinement) speech).variationFactor;
				knowledge.putIfAbsent(res.getLeft(), new HashMap<>());
				knowledge.get(res.getLeft()).put(res.getRight(), variation);
			}
			speech = (speech instanceof Refinement) ? ((Refinement) speech).prefix : ((Dummy) speech).prefix;
		}
		return knowledge;
	}

	@Override
	public double computeMeanValue(Map<Dimension, Member> coordinates) {
		double result = this.baseline.computeMeanValue(coordinates);
		for (Entry<Dimension, Member> entry : coordinates.entrySet()) {
			Map<Member, Double> factors = this.knowledge.getOrDefault(entry.getKey(), Collections.emptyMap());
			if (this.aggregate.function == Function.AVG && factors.containsKey(entry.getValue())) {
				result *= 1 + factors.get(entry.getValue());
			} else if (this.aggregate.function == Function.AVG) {
				long nUnknown = this.members.get(entry.getKey()).size() - factors.size();
				double sumKnown = factors.values().stream().reduce(0.0, Double::sum);
				result *= 1 - sumKnown / nUnknown;
			} else if (factors.containsKey(entry.getValue())) {
				result *= this.members.get(entry.getKey()).size() * factors.get(entry.getValue());
			} else {
				long nUnknown = this.members.get(entry.getKey()).size() - factors.size();
				double sumKnown = factors.values().stream().reduce(0.0, Double::sum);
				result *= this.members.get(entry.getKey()).size() * (1 - sumKnown) / nUnknown;
			}
		}
		return result;
	}

	@Override
	public String getDescription() {
		String levelIntroduction = this.restriction.getLeft().levelIntroductions.get(this.restriction.getRight().level - 1);
		String memberName = this.restriction.getRight().dbName;
		if (this.aggregate.function == Function.AVG) {
			String change = this.variationFactor > 0 ? "increases" : "decreases";
			String variation = Common.stringifyDouble(100 * Math.abs(this.variationFactor), this.nDigits);
			return "The value " + change + " by " + variation + " percent for " + levelIntroduction + " " + memberName + ".";
		} else {
			String variation = Common.stringifyDouble(100 * this.variationFactor, this.nDigits);
			return variation + " percent of the value comes from " + levelIntroduction + " " + memberName + ".";
		}
	}

	@Override
	public String getFullDescription() {
		return this.prefix.getFullDescription() + ".\n" + this.getDescription();
	}

}
