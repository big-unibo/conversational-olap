package it.unibo.trummervocalization.cache;

import it.unibo.trummervocalization.data.Dimension;
import it.unibo.trummervocalization.data.Measure;
import it.unibo.trummervocalization.data.Member;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;

public class DataCube {

	public final Map<Dimension, Member> coordinates;
	public final Measure measure;
	public final double sumValue;
	public final int countValue;

	public DataCube(Map<Dimension, Member> coordinates, Measure measure, double sumValue, int countValue) {
		this.coordinates = Collections.unmodifiableMap(coordinates);
		this.measure = measure;
		this.sumValue = sumValue;
		this.countValue = countValue;
	}

	public boolean isIncluded(Map<Dimension, Member> otherDataCoordinates, Measure otherDataMeasure) {
		if (!this.measure.equals(otherDataMeasure)) {
			return false;
		}
		for (Entry<Dimension, Member> entry : this.coordinates.entrySet()) {
			if (otherDataCoordinates.containsKey(entry.getKey())) {
				Member otherMember = otherDataCoordinates.get(entry.getKey());
				if (!entry.getValue().isDescendantOf(otherMember)) {
					return false;
				}
			}
		}
		return true;
	}

}
