package it.unibo.conversational.vocalization.cache;

import it.unibo.conversational.database.Cube;
import it.unibo.conversational.database.DBmanager;
import it.unibo.conversational.vocalization.data.Dimension;
import it.unibo.conversational.vocalization.data.Measure;
import it.unibo.conversational.vocalization.data.Member;
import it.unibo.conversational.vocalization.query.Aggregate;
import it.unibo.conversational.vocalization.query.Query;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.lang3.tuple.Pair;

public class CacheFactory {

	private final List<DataCube> cachedEntries;

	public CacheFactory(List<DataCube> cachedEntries) {
		this.cachedEntries = Collections.unmodifiableList(cachedEntries);
	}

	public static CacheFactory getCache(Cube cube, String factDbName, List<Dimension> dimensions, List<Measure> measures) throws Exception {
		List<DataCube> cachedEntries = new ArrayList<>();
		String dNames = dimensions.stream().map(d -> d.levelDbNames.get(d.levelDbNames.size() - 1)).collect(Collectors.joining(","));
		String mNames = measures.stream().map(m -> m.dbName).collect(Collectors.joining(","));
		DBmanager.executeDataQuery(cube, "select " + dNames + "," + mNames + " from " + factDbName, result -> {
			while (result.next()) {
				Map<Dimension, Member> coordinates = new HashMap<>();
				for (int i = 0; i < dimensions.size(); i++) {
					Dimension dimension = dimensions.get(i);
					String memberName = result.getString(i + 1);
					Member member = dimension.membersByLevelAndName.get(dimension.levelDbNames.size()).get(memberName);
					coordinates.put(dimension, member);
				}
				for (int j = 0; j < measures.size(); j++) {
					Measure measure = measures.get(j);
					double value = result.getDouble(dimensions.size() + j + 1);
					cachedEntries.add(new DataCube(coordinates, measure, value, 1));
				}
			}
		});
		return new CacheFactory(cachedEntries);
	}

	public Optional<Cache> groupBy(Query query, long maxDimensionSize, long maxGroupSize) {
		Aggregate aggregate = query.getAggregate();
		Map<Dimension, List<Member>> members = query.getAvailableMembers();
		List<Map<Dimension, Member>> groups = query.getSpecializationGroups();
		if (members.values().stream().map(List::size).max(Integer::compareTo).orElse(0) > maxDimensionSize || groups.size() > maxGroupSize) {
			return Optional.empty();
		}
		Map<Map<Dimension, Member>, Pair<Double, Integer>> grouped = new HashMap<>();
		for (DataCube cachedEntry : this.cachedEntries) {
			for (Map<Dimension, Member> g : groups) {
				if (cachedEntry.isIncluded(g, aggregate.measure)) {
					Pair<Double, Integer> prev = grouped.getOrDefault(g, Pair.of(0.0, 0));
					grouped.put(g, Pair.of(prev.getLeft() + cachedEntry.sumValue, prev.getRight() + 1));
					break;
				}
			}
		}
		return Optional.of(new Cache(aggregate, grouped.entrySet().stream().map(e -> new DataCube(
				e.getKey(), aggregate.measure, e.getValue().getLeft(), e.getValue().getRight())).collect(Collectors.toList())));
	}

}
