package it.unibo.conversational.vocalization.speech;

import it.unibo.conversational.vocalization.data.Dimension;
import it.unibo.conversational.vocalization.data.Member;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

public class Dummy extends Speech {

	protected final Speech prefix;

	public Dummy(Speech prefix) {
		super(prefix.aggregate, prefix.members, prefix.standardDeviation, prefix.probabilityRange, prefix.nDigits);
		this.prefix = prefix;
	}

	@Override
	public double computeMeanValue(Map<Dimension, Member> coordinates) {
		return this.prefix.computeMeanValue(coordinates);
	}

	@Override
	public String getDescription() {
		return StringUtils.EMPTY;
	}

	@Override
	public String getFullDescription() {
		return this.prefix.getFullDescription();
	}

}
