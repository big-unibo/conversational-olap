package it.unibo.trummervocalization.query;

import it.unibo.trummervocalization.data.Dimension;
import it.unibo.trummervocalization.data.Member;
import it.unibo.trummervocalization.utils.Common;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Query {

	private final Aggregate aggregate;
	private final Map<Dimension, Integer> groupByCoordinates;
	private final Map<Dimension, Member> whereCoordinates;

	public Query(Aggregate aggregate, Map<Dimension, Integer> groupByCoordinates, Map<Dimension, Member> whereCoordinates) {
		this.aggregate = aggregate;
		this.groupByCoordinates = filterGroupBy(groupByCoordinates, whereCoordinates);
		this.whereCoordinates = Collections.unmodifiableMap(whereCoordinates);
	}

	private static Map<Dimension, Integer> filterGroupBy(Map<Dimension, Integer> groupByCoordinates, Map<Dimension, Member> whereCoordinates) {
		return groupByCoordinates.entrySet().stream().filter(e -> !whereCoordinates.containsKey(e.getKey()) ||
				whereCoordinates.get(e.getKey()).level < e.getValue()).collect(Collectors.toMap(Entry::getKey, Entry::getValue));
	}

	public Aggregate getAggregate() {
		return this.aggregate;
	}

	public String getPreamble(List<Dimension> dimensions) {
		String where = dimensions.stream().map(d -> {
			try {
				Member member = this.whereCoordinates.get(d);
				return d.levelIntroductions.get(member.level - 1) + " " + member.dbName;
			} catch (NullPointerException | IndexOutOfBoundsException e) {
				return d.rootMember.dbName;
			}
		}).collect(Collectors.joining(", "));
		where = Common.replaceLast(where, ", ", " and ");
		String groupBy = dimensions.stream().flatMap(d -> {
			try {
				return Stream.of(d.levelSpokenNames.get(this.groupByCoordinates.get(d) - 1));
			} catch (NullPointerException | IndexOutOfBoundsException e) {
				return Stream.empty();
			}
		}).collect(Collectors.joining(", "));
		groupBy = Common.replaceLast(groupBy, ", ", " and ");
		return "Considering " + where + ".\n" + (groupBy.isEmpty() ?
				"Results are not broken down by any dimensional attribute." : "Results are broken down by " + groupBy + ".");
	}

	public List<Map<Dimension, Member>> getSpecializationGroups() {
		// Compute all available members for each dimension
		Map<Dimension, List<Member>> available = new HashMap<>();
		for (Entry<Dimension, Member> entry : this.whereCoordinates.entrySet()) {
			available.put(entry.getKey(), Collections.singletonList(entry.getValue()));
		}
		for (Entry<Dimension, Integer> entry : this.groupByCoordinates.entrySet()) {
			List<Member> members = available.getOrDefault(entry.getKey(), Collections.singletonList(entry.getKey().rootMember));
			while (members.stream().anyMatch(m -> m.level < entry.getValue())) {
				members = members.stream().flatMap(m -> m.level < entry.getValue() ? m.getChildren().stream() : Stream.of(m)).collect(Collectors.toList());
			}
			available.put(entry.getKey(), members);
		}
		// Compute all combinations between available members
		List<Map<Dimension, Member>> groups = new ArrayList<>();
		for (Entry<Dimension, List<Member>> entry : available.entrySet()) {
			if (groups.isEmpty()) {
				groups = entry.getValue().stream().map(m -> Collections.singletonMap(entry.getKey(), m)).collect(Collectors.toList());
			} else {
				List<Map<Dimension, Member>> previousGroups = groups;
				groups = entry.getValue().stream().flatMap(m -> previousGroups.stream().map(g -> Common.updateMap(g, entry.getKey(), m))).collect(Collectors.toList());
			}
		}
		return groups.isEmpty() ? Collections.singletonList(Collections.emptyMap()) : groups;
	}

}
