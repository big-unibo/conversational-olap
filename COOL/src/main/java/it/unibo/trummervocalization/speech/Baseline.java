package it.unibo.trummervocalization.speech;

import it.unibo.trummervocalization.data.Dimension;
import it.unibo.trummervocalization.data.Member;
import it.unibo.trummervocalization.query.Aggregate;
import it.unibo.trummervocalization.query.Aggregate.Function;
import it.unibo.trummervocalization.utils.Common;
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
