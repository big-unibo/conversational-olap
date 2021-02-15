package it.unibo.conversational.vocalization.uct;

import it.unibo.conversational.vocalization.cache.Cache;
import it.unibo.conversational.vocalization.data.Dimension;
import it.unibo.conversational.vocalization.data.Member;
import it.unibo.conversational.vocalization.query.Aggregate.Function;
import it.unibo.conversational.vocalization.speech.Baseline;
import it.unibo.conversational.vocalization.speech.Dummy;
import it.unibo.conversational.vocalization.speech.Refinement;
import it.unibo.conversational.vocalization.speech.Speech;
import it.unibo.conversational.vocalization.utils.Common;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.lang3.tuple.Pair;

public class TreeFactory {

	public static UctNode getRoot(Cache cache, boolean complete, long maxChildren, int nRefinements, int nDigits, double stDevFactor, double pRangeFactor) {
		double base = cache.computeAggregateValue();
		double baseRounded = Common.roundDouble(base, nDigits);
		Function function = cache.aggregate.function;
		Map<Dimension, Set<Member>> availableMembers = cache.getAvailableMembers();
		Map<Pair<Dimension, Member>, Double> availableFactors = new HashMap<>();
		availableMembers.forEach((dimension, members) -> members.forEach(member -> {
			double agg = cache.computeAggregateValue(dimension, member);
			if (agg > 0 && agg != base) {
				double factor = function == Function.AVG ? agg / baseRounded - 1 : agg / baseRounded;
				availableFactors.put(Pair.of(dimension, member), Common.roundDouble(factor, nDigits));
			}
		}));
		Map<Dimension, Map<Member, Double>> factors = bestFactors(availableFactors, availableMembers, function, maxChildren);
		Baseline baseline = new Baseline(cache.aggregate, availableMembers, baseRounded, stDevFactor, pRangeFactor, nDigits);
		UctNode baseNode = complete ? completeRefine(cache, baseline, factors, nRefinements, 0) : originalRefine(cache, baseline, factors, nRefinements, 0);
		return new Inner(cache, null, Collections.singletonList(baseNode));
	}

	private static Map<Dimension, Map<Member, Double>> bestFactors(Map<Pair<Dimension, Member>, Double> factors, Map<Dimension, Set<Member>> members, Function f, long max) {
		long n = factors.size() > max ? factors.size() - max : 0;
		return factors.entrySet().stream()
				.sorted(Comparator.comparingDouble(e -> f == Function.AVG ? Math.abs(e.getValue()) : Math.abs(1.0 / members.get(e.getKey().getLeft()).size() - e.getValue())))
				.skip(n).collect(Collectors.groupingBy(e -> e.getKey().getLeft(), Collectors.toMap(e -> e.getKey().getRight(), Entry::getValue)));
	}

	private static UctNode completeRefine(Cache cache, Speech speech, Map<Dimension, Map<Member, Double>> factors, int nRefinements, int refinement) {
		if (refinement < nRefinements) {
			List<UctNode> children = new ArrayList<>();
			for (Dimension dimension : factors.keySet()) {
				for (Entry<Member, Double> entry : factors.get(dimension).entrySet()) {
					Speech nextSpeech = new Refinement(speech, dimension, entry.getKey(), entry.getValue());
					Map<Dimension, Map<Member, Double>> updatedFactors = Common.removeMap(factors, dimension, entry.getKey());
					children.add(completeRefine(cache, nextSpeech, updatedFactors, nRefinements, refinement + 1));
				}
			}
			Speech dummySpeech = new Dummy(speech);
			children.add(completeRefine(cache, dummySpeech, factors, nRefinements, refinement + 1));
			return new Inner(cache, speech, children);
		} else {
			return new Leaf(cache, speech);
		}
	}

	private static UctNode originalRefine(Cache cache, Speech speech, Map<Dimension, Map<Member, Double>> factors, int nRefinements, int refinement) {
		Optional<Dimension> dimension = factors.keySet().stream().findFirst();
		if (refinement < nRefinements && dimension.isPresent()) {
			List<UctNode> children = new ArrayList<>();
			Map<Dimension, Map<Member, Double>> updatedFactors = Common.removeMap(factors, dimension.get());
			for (Entry<Member, Double> entry : factors.get(dimension.get()).entrySet()) {
				Speech nextSpeech = new Refinement(speech, dimension.get(), entry.getKey(), entry.getValue());
				children.add(originalRefine(cache, nextSpeech, updatedFactors, nRefinements, refinement + 1));
			}
			Speech dummySpeech = new Dummy(speech);
			children.add(originalRefine(cache, dummySpeech, updatedFactors, nRefinements, refinement + 1));
			return new Inner(cache, speech, children);
		} else {
			return new Leaf(cache, speech);
		}
	}

}
