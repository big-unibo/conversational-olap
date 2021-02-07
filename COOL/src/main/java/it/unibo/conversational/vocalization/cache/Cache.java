package it.unibo.conversational.vocalization.cache;

import it.unibo.conversational.vocalization.data.Dimension;
import it.unibo.conversational.vocalization.data.Member;
import it.unibo.conversational.vocalization.query.Aggregate;
import it.unibo.conversational.vocalization.query.Aggregate.Function;
import it.unibo.conversational.vocalization.speech.Speech;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

public class Cache {

    public final Aggregate aggregate;
    public final List<DataCube> groupedEntries;

    public Cache(Aggregate aggregate, List<DataCube> groupedEntries) {
        this.aggregate = aggregate;
        this.groupedEntries = Collections.unmodifiableList(groupedEntries);
    }

    public double computeAggregateValue() {
        return this.computeAggregateValue(Collections.emptyMap());
    }

    public double computeAggregateValue(Dimension dimension, Member member) {
        return this.computeAggregateValue(Collections.singletonMap(dimension, member));
    }

    public double computeAggregateValue(Map<Dimension, Member> coordinates) {
        int count = 0;
        double sum = 0;
        for (DataCube entry : this.groupedEntries) {
            if (entry.isIncluded(coordinates, this.aggregate.measure)) {
                count += entry.countValue;
                sum += entry.sumValue;
            }
        }
        return this.getAggregateValue(this.aggregate.function, sum, count);
    }

    public double evaluateSpeechError(Speech speech) {
        double errorSum = 0;
        for (DataCube entry : this.groupedEntries) {
            double exact = this.getAggregateValue(this.aggregate.function, entry.sumValue, entry.countValue);
            errorSum += Math.abs(speech.computeMeanValue(entry.coordinates) - exact) / exact;
        }
        return errorSum / this.groupedEntries.size();
    }

    public double evaluateSpeechQuality(Speech speech) {
        double qualitySum = 0;
        for (DataCube entry : this.groupedEntries) {
            qualitySum += speech.computeProbability(entry);
        }
        return qualitySum / this.groupedEntries.size();
    }

    private double getAggregateValue(Function function, double sum, int count) {
        if (function == Function.SUM) return sum;
        else if (function == Function.COUNT) return count;
        else return sum / count;
    }

    public Map<Dimension, Set<Member>> getAvailableMembers() {
        return this.groupedEntries.stream().flatMap(dc -> dc.coordinates.entrySet().stream())
                .collect(Collectors.groupingBy(Entry::getKey, Collectors.mapping(Entry::getValue, Collectors.toSet())));
    }

    public DataCube getSample() {
        int randomIndex = new Random().nextInt(this.groupedEntries.size());
        return this.groupedEntries.get(randomIndex);
    }

}
