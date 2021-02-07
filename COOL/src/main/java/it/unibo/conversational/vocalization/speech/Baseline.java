package it.unibo.conversational.vocalization.speech;

import it.unibo.conversational.vocalization.data.Dimension;
import it.unibo.conversational.vocalization.data.Member;
import it.unibo.conversational.vocalization.query.Aggregate;
import it.unibo.conversational.vocalization.query.Aggregate.Function;
import it.unibo.conversational.vocalization.utils.Common;
import java.util.Map;
import java.util.Set;

public class Baseline extends Speech {

	private final double value;

	public Baseline(Aggregate aggregate, Map<Dimension, Set<Member>> members, double value, double stDevFactor, double pRangeFactor, int nDigits) {
		super(aggregate, members, quantifyMean(aggregate, members, value) * stDevFactor, quantifyMean(aggregate, members, value) * pRangeFactor, nDigits);
		this.value = value;
	}

	private static double quantifyMean(Aggregate aggregate, Map<Dimension, Set<Member>> members, double value) {
		if (aggregate.function == Function.AVG) {
			return value;
		} else {
			return value / members.values().stream().map(Set::size).reduce(1, (a, b) -> a * b);
		}
	}

	@Override
	public double computeMeanValue(Map<Dimension, Member> coordinates) {
		return quantifyMean(this.aggregate, this.members, this.value);
	}

	@Override
	public String getDescription() {
		return "Around " + Common.stringifyDouble(this.value, this.nDigits) + " is the " + this.aggregate.getText() + ".";
	}

	@Override
	public String getFullDescription() {
		return this.getDescription();
	}

}
