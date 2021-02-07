package it.unibo.conversational.vocalization.data;

import it.unibo.conversational.database.Cube;
import it.unibo.conversational.database.DBmanager;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Dimension {

	public final String dbName;
	public final String spokenName;
	public final List<String> levelDbNames;
	public final List<String> levelSpokenNames;
	public final List<String> levelIntroductions;
	public final Map<Integer, Map<String, Member>> membersByLevelAndName;
	public final Member rootMember;

	public Dimension(String dbName, String spokenName, List<String> levelDbNames, List<String> levelSpokenNames,
			List<String> levelIntroductions, Map<Integer, Map<String, Member>> membersByLevelAndName, Member rootMember) {
		this.dbName = dbName;
		this.spokenName = spokenName;
		this.levelDbNames = Collections.unmodifiableList(levelDbNames);
		this.levelSpokenNames = Collections.unmodifiableList(levelSpokenNames);
		this.levelIntroductions = Collections.unmodifiableList(levelIntroductions);
		this.membersByLevelAndName = Collections.unmodifiableMap(membersByLevelAndName);
		this.rootMember = rootMember;
	}

	public static Dimension initialize(Cube cube, String dbName, String spokenName,
			List<String> levelDbNames, List<String> levelSpokenNames, List<String> levelIntroductions) throws Exception {
		String query = "select distinct " + String.join(", ", levelDbNames) + " from " + dbName;
		Map<Integer, Map<String, Member>> membersByLevelAndName = new HashMap<>();
		for (int i = 0; i <= levelDbNames.size(); i++) {
			membersByLevelAndName.put(i, new HashMap<>());
		}
		Member rootMember = new Member("any " + spokenName, 0);
		membersByLevelAndName.get(0).put(rootMember.dbName, rootMember);
		DBmanager.executeDataQuery(cube, query, result -> {
			while (result.next()) {
				Member member = rootMember;
				for (int i = 1; i <= levelDbNames.size(); i++) {
					String nextDbName = result.getString(i);
					Optional<Member> child = member.getChild(nextDbName);
					if (child.isPresent()) {
						member = child.get();
					} else {
						Member newMember = new Member(nextDbName, member.level + 1, member);
						membersByLevelAndName.get(i).put(newMember.dbName, newMember);
						member.addChild(newMember);
						member = newMember;
					}
				}
			}
		});
		return new Dimension(dbName, spokenName, levelDbNames, levelSpokenNames, levelIntroductions, membersByLevelAndName, rootMember);
	}

}
