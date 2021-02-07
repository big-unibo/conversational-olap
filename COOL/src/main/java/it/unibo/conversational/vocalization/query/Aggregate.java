package it.unibo.conversational.vocalization.query;

import it.unibo.conversational.vocalization.data.Measure;

public class Aggregate {

	public final Function function;
	public final Measure measure;

	public Aggregate(Function function, Measure measure) {
		this.function = function;
		this.measure = measure;
	}

	public String getText() {
		if (this.function == Function.SUM) return "Sum of " + this.measure.spokenName;
		else if (this.function == Function.COUNT) return "Number of entries";
		else return "Average " + this.measure.spokenName;
	}

	public enum Function {
		AVG, COUNT, SUM
	}

}
