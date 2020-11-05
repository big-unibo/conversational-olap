package it.unibo.describe;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.Triple;
import org.json.JSONObject;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import it.unibo.conversational.algorithms.Parser.Type;
import it.unibo.conversational.datatypes.DependencyGraph;

public class Describe {
  private String cube;
  private List<String> measures = Lists.newLinkedList();
  private List<String> models = Lists.newLinkedList();
  private Optional<Integer> k = Optional.absent();
  private List<Triple<String, String, List<String>>> clause = Lists.newLinkedList();
  private Set<String> attributes = Sets.newHashSet();
  private Set<String> prev = Sets.newHashSet();
  private final String filename;
  private List<Triple<String, String, List<String>>> prevClause = Lists.newLinkedList();

  public Describe(Describe d) {
    if (d != null) {
      sessionStep = d.sessionStep;
      filename = d.getFilename();
      attributes = Sets.newLinkedHashSet(d.attributes);
      prevClause = Lists.newLinkedList(d.clause);
      clause = d.getClause();
    } else {
      filename = UUID.randomUUID().toString();
    }
  }

  public List<Triple<String, String, List<String>>> getPrevClause() {
    return prevClause;
  }

  public Set<String> getPreviousAttributes() {
    return prev;
  }

  public Set<String> getAttributes() {
    return attributes;
  }

  public void setCube(final String cube) {
    this.cube = cube;
  }

  public List<String> getMeasures() {
    return measures;
  }

  public void setMeasures(List<String> measures) {
    this.measures = measures;
  }

  public List<String> getModels() {
    return models;
  }

  public void addMeasure(String measure) {
    this.measures.add(measure);
  }

  public void addModels(String model) {
    this.models.add(model);
  }

  public void setModels(List<String> models) {
    this.models = models;
  }

  public Optional<Integer> getK() {
    return k;
  }

  public void setK(Optional<Integer> k) {
    this.k = k;
  }

  public List<Triple<String, String, List<String>>> getClause() {
    return clause;
  }

  public void addClause(Triple<String, String, List<String>> clause) {
    final Iterator<Triple<String, String, List<String>>> iterator = this.clause.iterator();
    while (iterator.hasNext()) {
      final Triple<String, String, List<String>> a = iterator.next();
      final Optional<String> lca = DependencyGraph.lca(a.getLeft(), clause.getLeft());
      if (lca.isPresent() && (lca.get().equals(a.getLeft()) || lca.get().equals(clause.getLeft()))) {
        iterator.remove();
      }
    }
    this.clause.add(clause);
  }

  public String getCube() {
    return cube;
  }

  public void setAttribute(Optional<String> optAttribute) {
    final String attribute = optAttribute.get().toLowerCase();
    prev = Sets.newLinkedHashSet(attributes);
    final Iterator<String> iterator = attributes.iterator();
    while (iterator.hasNext()) {
      final String a = iterator.next();
      final Optional<String> lca = DependencyGraph.lca(a, attribute);
      if (lca.isPresent() && (lca.get().equals(a.toLowerCase()) || lca.get().equals(attribute))) {
        iterator.remove();
      }
    }
    attributes.add(attribute);
  }

  public JSONObject getJSON() {
    final JSONObject obj = new JSONObject();
    obj.put(Type.GC.toString().toUpperCase(), attributes.stream().sorted().collect(Collectors.toList()));
    obj.put(Type.SC.toString().toUpperCase(), clause.stream().map(t -> {
      JSONObject c = new JSONObject();
      c.put(Type.ATTR.toString().toUpperCase(), t.getLeft());
      c.put(Type.COP.toString().toUpperCase(), t.getMiddle());
      c.put(Type.VAL.toString().toUpperCase(), t.getRight());
      return c;
    }).collect(Collectors.toList()));
    obj.put(Type.MC.toString().toUpperCase(), measures.stream().sorted().map(t -> {
      JSONObject c = new JSONObject();
      c.put(Type.AGG.toString().toUpperCase(), "sum");
      c.put(Type.MEA.toString().toUpperCase(), t);
      return c;
    }).collect(Collectors.toList()));
    return obj;
  }

  private int sessionStep = 0;

  public int getSessionStep() {
    return sessionStep;
  }

  public void incSessionStep() {
    sessionStep += 1;
  }

  public String getFilename() {
    if (DescribeExecute.DEBUG) {
      return "debug";
    }
    return filename;
  }
}
