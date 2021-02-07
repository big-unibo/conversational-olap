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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;

public class TreeFactory {

	public static UctNode getRoot(Cache cache, boolean complete, int nRefinements, int nDigits, double stDevFactor, double pRangeFactor) {
		double base = cache.computeAggregateValue();
		double baseRounded = Common.roundDouble(base, nDigits);
		Map<Dimension, Set<Member>> availableMembers = cache.getAvailableMembers();
		Map<Dimension, Map<Member, Double>> availableFactors = new HashMap<>();
		for (Dimension dimension : availableMembers.keySet()) {
			for (Member member : availableMembers.get(dimension)) {
				double agg = cache.computeAggregateValue(dimension, member);
				if (agg > 0 && agg != base) {
					double factor = cache.aggregate.function == Function.AVG ? agg / baseRounded - 1 : agg / baseRounded;
					availableFactors.putIfAbsent(dimension, new HashMap<>());
					availableFactors.get(dimension).put(member, Common.roundDouble(factor, nDigits));
				}
			}
		}
		Baseline baseline = new Baseline(cache.aggregate, availableMembers, baseRounded, stDevFactor, pRangeFactor, nDigits);
		UctNode baseNode = complete ? completeRefine(cache, baseline, availableFactors, nRefinements, 0) : originalRefine(cache, baseline, availableFactors, nRefinements, 0);
		return new Inner(cache, null, Collections.singletonList(baseNode));
	}

	private static UctNode completeRefine(Cache cache, Speech speech, Map<Dimension, Map<Member, Double>> availableFactors, int nRefinements, int refinement) {
		if (refinement < nRefinements) {
			List<UctNode> children = new ArrayList<>();
			for (Dimension dimension : availableFactors.keySet()) {
				for (Entry<Member, Double> entry : availableFactors.get(dimension).entrySet()) {
					Speech nextSpeech = new Refinement(speech, dimension, entry.getKey(), entry.getValue());
					Map<Dimension, Map<Member, Double>> updatedFactors = Common.removeMap(availableFactors, dimension, entry.getKey());
					children.add(completeRefine(cache, nextSpeech, updatedFactors, nRefinements, refinement + 1));
				}
			}
			Speech dummySpeech = new Dummy(speech);
			children.add(completeRefine(cache, dummySpeech, availableFactors, nRefinements, refinement + 1));
			return new Inner(cache, speech, children);
		} else {
			return new Leaf(cache, speech);
		}
	}

	private static UctNode originalRefine(Cache cache, Speech speech, Map<Dimension, Map<Member, Double>> availableFactors, int nRefinements, int refinement) {
		Optional<Dimension> dimension = availableFactors.keySet().stream().findFirst();
		if (refinement < nRefinements && dimension.isPresent()) {
			List<UctNode> children = new ArrayList<>();
			Map<Dimension, Map<Member, Double>> updatedFactors = Common.removeMap(availableFactors, dimension.get());
			for (Entry<Member, Double> entry : availableFactors.get(dimension.get()).entrySet()) {
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
