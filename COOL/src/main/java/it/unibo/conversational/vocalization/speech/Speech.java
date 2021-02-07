package it.unibo.conversational.vocalization.speech;

import it.unibo.conversational.vocalization.cache.DataCube;
import it.unibo.conversational.vocalization.data.Dimension;
import it.unibo.conversational.vocalization.data.Member;
import it.unibo.conversational.vocalization.query.Aggregate;
import java.util.Map;
import java.util.Set;
import org.apache.commons.math3.distribution.NormalDistribution;

public abstract class Speech {

	protected final Aggregate aggregate;
	protected final Map<Dimension, Set<Member>> members;
	protected final double standardDeviation;
	protected final double probabilityRange;
	protected final int nDigits;

	public Speech(Aggregate aggregate, Map<Dimension, Set<Member>> members, double standardDeviation, double probabilityRange, int nDigits) {
		this.aggregate = aggregate;
		this.members = members;
		this.standardDeviation = standardDeviation;
		this.probabilityRange = probabilityRange;
		this.nDigits = nDigits;
	}

	public abstract double computeMeanValue(Map<Dimension, Member> coordinates);

	public double computeProbability(DataCube sample) {
		return this.computeProbability(sample.coordinates, sample.sumValue, sample.countValue);
	}

	public double computeProbability(Map<Dimension, Member> coordinates, double sum, int count) {
		double exact = switch (this.aggregate.function) {
			case SUM ->  sum;
			case COUNT -> count;
			default -> sum / count;
		};
		double meanValue = this.computeMeanValue(coordinates);
		NormalDistribution distribution = new NormalDistribution(meanValue, this.standardDeviation);
		double lbProb = distribution.cumulativeProbability(exact - this.probabilityRange / 2);
		double ubProb = distribution.cumulativeProbability(exact + this.probabilityRange / 2);
		return ubProb - lbProb;
	}

	public abstract String getDescription();

	public abstract String getFullDescription();

}
