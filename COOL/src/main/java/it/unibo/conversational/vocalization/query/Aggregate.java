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
		return switch (this.function) {
			case AVG -> "Average " + this.measure.spokenName;
			case SUM -> "Sum of " + this.measure.spokenName;
			case COUNT -> "Number of entries";
		};
	}

	public enum Function {
		AVG, COUNT, SUM
	}

}
