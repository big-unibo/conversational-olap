package it.unibo.conversational.vocalization.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class Member {

    public final String dbName;
    public final int level;
    private final Optional<Member> parent;
    private final List<Member> children;

    public Member(String dbName, int level) {
        this.dbName = dbName;
        this.level = level;
        this.parent = Optional.empty();
        this.children = new ArrayList<>();
    }

    public Member(String dbName, int level, Member parent) {
        this.dbName = dbName;
        this.level = level;
        this.parent = Optional.of(parent);
        this.children = new ArrayList<>();
    }

    protected void addChild(Member member) {
        this.children.add(member);
    }

    public Optional<Member> getChild(String memberName) {
        return this.children.stream().filter(m -> m.dbName.equals(memberName)).findFirst();
    }

    public List<Member> getChildren() {
        return Collections.unmodifiableList(this.children);
    }

    public boolean isDescendantOf(Member member) {
        return this == member || (this.parent.isPresent() && this.parent.get().isDescendantOf(member));
    }

}
