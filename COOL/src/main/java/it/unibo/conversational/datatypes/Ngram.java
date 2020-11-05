package it.unibo.conversational.datatypes;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import it.unibo.conversational.Utils;
import it.unibo.conversational.Utils.DataType;
import it.unibo.conversational.algorithms.Parser.Type;
import it.unibo.conversational.database.Cube;
import it.unibo.conversational.database.QueryGenerator;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.atomic.DoubleAdder;
import java.util.concurrent.atomic.LongAdder;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

 /**
  * An ngram (a list of tokens).
  */
public class Ngram implements Serializable {
  /** serialVersionUID */
  private static final long serialVersionUID = 8224288094300406179L;
  /** Type of errors and ambiguities. */
  public enum AnnotationType {
    /** Type check: Attributes in the drill down / roll up belongs to different hierarchies */
    A2,
    /** Inference: Attribute is already a dimension, cannot drill down */
    A3,
    /** Inference: Several attributes can be inferred for the given member. */
    AA,
    /** Type check: Dom(L) != Dom(Member). */
    AVM,
    /** Inference: Branch ambiguity. Choose "to" attribute for drill down / roll up */
    BA,
    /** Inference: Clause already exists */
    EAE,
    /** Inference: Clause does not exist */
    ENE,
    /** Type check: Attributes in the drill down / roll up clause is not in the right order */
    GSA,
    /** Inference: Several measure can be inferred for the given measure. */
    MA,
    /** Type check: Group by on descriptive attribute. TODO: not implemented for now. */
    MDMG,
    /** Type check: Operator cannot be applied to the given measure. */
    MDMV,
    /** Inference: Unparsed clause. */
    UP,
    /** Inference: Choose "from" attribute for drill down / roll up */
    CA
  }

  public static final Pair<Integer, Integer> DUMMY_POSITION = Pair.of(-1, -1);
  public static final Ngram DUMMY_AND = new Ngram("and", Type.AND, new Entity("and"));
  public static final Ngram DUMMY_BY = new Ngram("by", Type.BY, new Entity("by"));
  public static final Ngram DUMMY_CNT = new Ngram("count", Type.COUNT, new Entity("count"));
  public static final Ngram DUMMY_EQ = new Ngram("=", Type.COP, new Entity("="));
  public static final Ngram DUMMY_WHERE = new Ngram("where", Type.WHERE, new Entity("where"));
  /**
   * Add to children
   * @param parent a node
   * @param child another node
   */
  public static void addNode(final Ngram parent, final Ngram child) {
    final List<Ngram> children = Lists.newLinkedList(parent.children);
    children.add(child);
    parent.setChildren(ImmutableList.copyOf(children));
  }
  /**
   * Add to children
   * @param parent a node
   * @param child another node
   * @param pos position of the node
   */
  public static void addNode(final Ngram parent, final Ngram child, final int pos) {
    final List<Ngram> children = Lists.newLinkedList(parent.children);
    children.add(pos, child);
    parent.setChildren(ImmutableList.copyOf(children));
  }

  /**
   * @param n tree
   * @return average similarity
   */
  public static double avgSimilarity(final Ngram n) {
    DoubleAdder score = new DoubleAdder();
    DoubleAdder count = new DoubleAdder();
    traverseLeaves(n, (l, acc) -> { score.add(l.similarity()); count.add(1); return -1; });
    return score.doubleValue() / count.doubleValue();
  }
  /**
   * Compare ngrams by position.
   * @param o1 an ngram
   * @param o2 another ngram
   * @return ngram comparison
   */
  public static int compareNgrams(final Ngram o1, final Ngram o2) {
    final int s = o1.pos().getLeft().compareTo(o2.pos().getLeft());
    if (s == 0) { // same start
      final int e = o1.pos().getRight().compareTo(o2.pos().getRight());
      if (e == 0) { // same end
        return -Double.compare(o1.similarity(), o1.similarity()); // sort by similarity desc
      }
      return e;
    }
    return s;
  }

  /**
   * @param tree where to search the node
   * @param n a node
   * @return true if tree contains n
   */
  public static boolean contains(final Ngram tree, final Ngram n) {
    return contains(tree, n, false);
  }

  /**
   * @param tree where to search the node
   * @param n a node
   * @param usePartialEquals whether to use partial equals (i.e., check only ngram content and not its position in text)
   * @return true if tree contains n
   */
  public static boolean contains(final Ngram tree, final Ngram n, final boolean usePartialEquals) {
    LongAdder res = new LongAdder();
    Ngram.traverse(tree, //
        (c, acc) -> !usePartialEquals && c.equals(n) || usePartialEquals && c.partialContains(n), //
        (c, acc) -> { res.add(1); return -1; });
    return res.intValue() > 0;
  }

  /**
   * @param n tree
   * @return number of leabes
   */
  public static int countLeaves(final Ngram n) {
    DoubleAdder score = new DoubleAdder();
    traverseLeaves(n, (l, acc) -> { score.add(1); return -1; });
    return score.intValue();
  }

  /**
   * @param n a node
   * @param con a condition to satisfy
   * @return the node satisfying the condition, if any
   */
  public static Optional<Ngram> find(final Ngram n, final BiFunction<Ngram, Boolean, Boolean> con) {
    final List<Ngram> ngrams = findMany(n, con);
    return ngrams.isEmpty() ? Optional.absent() : Optional.of(ngrams.get(0));
  }

  /**
   * @param tree where to search the node
   * @param n a node
   * @return the parent of the current node
   */
  public static List<Ngram> findContainer(final Ngram tree, final Ngram n) {
    return Ngram.findMany(tree, (c, acc) -> c.partialContains(n));
  }

  /**
   * @param n a node
   * @param con a condition to satisfy
   * @return the nodes satisfying the condition, if any
   */
  public static <T> List<Ngram> findMany(final Ngram n, final BiFunction<Ngram, Boolean, Boolean> con) {
    final List<Ngram> ngrams = Lists.newLinkedList();
    traverse(n, con, (c, acc) -> ngrams.add(c));
    return ngrams;
  }

  /**
   * @param tree where to search the node
   * @param n a node
   * @return the parent of the current node
   */
  public static Optional<Ngram> findParent(final Ngram tree, final Ngram n) {
    return findParent(tree, n, false);
  }

  /**
   * @param tree where to search the node
   * @param n a node
   * @param usePartialEquals whether to use partial equals (i.e., check only ngram content and not its position in text)
   * @return the parent of the current node
   */
  public static Optional<Ngram> findParent(final Ngram tree, final Ngram n, final boolean usePartialEquals) {
    // A node cannot be part of itself, this breaks disambiguation
    //    if (!usePartialEquals && tree.equals(n) || usePartialEquals && tree.partialEquals(n)) {
    //      return Optional.of(tree);
    //    }
    return Ngram.find(tree, (c, acc) -> //
        !usePartialEquals && c.children.contains(n) //
        || usePartialEquals && c.children.stream().anyMatch(nn -> nn.partialEquals(n)));
  }

  /**
   * @param tree where to search the node
   * @param n a node
   * @param usePartialEquals whether to use partial equals (i.e., check only ngram content and not its position in text)
   * @return the parent of the current node
   */
  public static List<Ngram> findParents(final Ngram tree, final Ngram n, final boolean usePartialEquals) {
    return Ngram.findMany(tree, (c, acc) -> //
      !usePartialEquals && c.children.contains(n) //
      || usePartialEquals && c.children.stream().anyMatch(nn -> nn.partialEquals(n)));
  }

  /**
   * Estimate the frequency of a disambiguation
   * @param log list of previous disambiguations
   * @param toCheck the disambiguation to check
   * @param t the disambiguation solution
   * @return the disambiguation frequency
   */
  public static double frequency(final List<Triple<AnnotationType, Ngram, Ngram>> log, final Pair<AnnotationType, Ngram> toCheck, Ngram t) {
    final List<Triple<AnnotationType, Ngram, Ngram>> all = log.stream().filter(l -> l.getLeft().equals(toCheck.getLeft()) && l.getMiddle().partialEquals(toCheck.getRight())).collect(Collectors.toList());
    return all.stream().filter(l -> l.getRight().partialEquals(t)).count() / (all.size() + 1.0);
  }

  /**
   * @param n tree
   * @return score of the tree
   */
  public static double getScore(final Ngram n) {
    final DoubleAdder score = new DoubleAdder();
    traverse(n, (c, acc) -> {
      final boolean isAnnotated = (acc == null ? false : (boolean) acc) || !c.getAnnotations().isEmpty();
      if (c.getChildren().isEmpty() && (!c.tokens.equals("where") || c.tokens.equals("where") && !c.pos().equals(DUMMY_POSITION))) {
        if (isAnnotated) System.out.println(isAnnotated);
        score.add((isAnnotated ? 0.99 : 1) * c.similarity()); 
      }
      return isAnnotated;
    });
    return score.doubleValue();
  }

  /**
   * Get the ngram leaves.
   * @param ngram current elements
   * @return ngram leaves
   */
  public static Set<Ngram> leaves(final Ngram ngram) {
    final Set<Ngram> leaves = Sets.newLinkedHashSet();
    traverseLeaves(ngram, (n, acc) -> { leaves.add(n); return -1; });
    return leaves;
  }

  /**
   * Replace the current node
   */
  public static void removeNode(final Ngram tree, final Ngram n) {
    removeNode(tree, n, false);
  }

  /**
   * Replace the current node
   * @param tree where to search the node
   * @param n a node
   * @param usePartialEquals whether to use partial equals (i.e., check only ngram content and not its position in text)
   */
  public static void removeNode(final Ngram tree, final Ngram n, final boolean usePartialEquals) {
    final Ngram parent = findParent(tree, n, usePartialEquals).get();
    final ArrayList<Ngram> parentChildren = new ArrayList<>(parent.children);
    final Iterator<Ngram> iterator = parentChildren.iterator();
    while (iterator.hasNext()) {
      final Ngram cur = iterator.next();
      if (usePartialEquals && n.partialEquals(cur) || !usePartialEquals && n.equals(cur)) {
        iterator.remove();
      }
    }
    parent.setChildren(ImmutableList.copyOf(parentChildren));
    sanitize(tree);
  }

  /**
   * Replace the node n with its children
   * @param tree where to search the node
   * @param n a node
   */
  public static void replaceNode(final Ngram tree, final Ngram n) {
    replaceNode(tree, n, false);
  }

  /**
   * Replace the node n with its children
   * @param tree where to search the node
   * @param usePartialEquals whether to use partial equals (i.e., check only ngram content and not its position in text)
   * @param n a node
   */
  public static void replaceNode(final Ngram tree, final Ngram n, final boolean usePartialEquals) {
    Ngram parent = findParent(tree, n, usePartialEquals).get();
    ArrayList<Ngram> parentChildren = new ArrayList<>(parent.children);
    parentChildren.remove(n);
    parentChildren.addAll(n.children);
    parent.setChildren(ImmutableList.copyOf(parentChildren));
    sanitize(tree);
  }

  /**
   * Replace a node with another in the "from" node's parent
   * @param tree where to search the node
   * @param from a node
   * @param to another node
   */
  public static void replaceNode(final Ngram tree, final Ngram from, final Ngram to) {
    replaceNode(tree, from, to, false);
  }

  /**
   * Complete a node with another in the "from" node's parent
   * @param tree where to search the node
   * @param from a node
   * @param to another node
   * @param usePartialEquals whether to use partial equals (i.e., check only ngram content and not its position in text)
   */
  public static void completeNode(final Ngram tree, final Ngram from, final Ngram to, final boolean usePartialEquals) {
    final Ngram parent = findContainer(tree, from).get(0);
    final List<Ngram> fromChildren = Lists.newLinkedList(parent.children);
    final List<Ngram> toChildren = Lists.newLinkedList(to.children);
    for (final Ngram tc : toChildren) {
      if (!fromChildren.stream().anyMatch(fc -> fc.partialEquals(tc))) {
        fromChildren.add(tc);
      }
    }
    parent.setChildren(ImmutableList.copyOf(fromChildren));
  }

  /**
   * Replace a node with another in the "from" node's parent
   * @param tree where to search the node
   * @param from a node
   * @param to another node
   * @param usePartialEquals whether to use partial equals (i.e., check only ngram content and not its position in text)
   */
  public static void replaceNode(final Ngram tree, final Ngram from, final Ngram to, final boolean usePartialEquals) {
    final Ngram parent = findParent(tree, from, usePartialEquals).get();
    final List<Ngram> children = Lists.newLinkedList(parent.children);
    final Iterator<Ngram> iterator = children.iterator();
    while (iterator.hasNext()) {
      final Ngram n = iterator.next();
      if (usePartialEquals && n.partialEquals(from) || !usePartialEquals && n.equals(from)) {
        iterator.remove();
      }
    }
    children.add(to);
    parent.setChildren(ImmutableList.copyOf(children));
  }

  private static void sanitize(final Ngram tree) {
    traverse(tree, (n, acc) -> {
      switch (n.type) {
      case SC:
        // If the selection clause only contains a where.... drop it from the tree
        if (n.children.stream().allMatch(nn -> nn.type.equals(Type.WHERE))) { // CONTIENE UN SINGOLO WHERE
          Ngram.removeNode(tree, n);
        } else if (n.children.size() == 2 && n.children.stream().anyMatch(nn -> nn.type.equals(Type.AND) || nn.type.equals(Type.OR))) { // CONTIENE UN AND / OR CON UNA SINGOLA CLAUSOLA
          final Ngram op = n.children.stream().filter(nn -> nn.type.equals(Type.AND) || nn.type.equals(Type.OR)).findAny().get();
          final Ngram sc = n.children.stream().filter(nn -> nn.type.equals(Type.SC)).findAny().get();
          Ngram.replaceNode(tree, sc);
          Ngram.removeNode(tree, op);
        } else if (n.children.size() == 2 && !n.children.stream().anyMatch(nn -> nn.type.equals(Type.WHERE))) { // NON CONTIENE TUTTI E TRE ATTR COP VAL
          Ngram.removeNode(tree, n);
        }
      case GC:
        // If the selection clause only contains a where.... drop it from the tree
        if (n.children.stream().allMatch(nn -> nn.type.equals(Type.BY))) {
          Ngram.removeNode(tree, n);
        }
      case MC:
        // If a MC/SC/GC only contains a MC/GC/SC, we can replace the parent with the children
        if (n.children.size() == 1 && n.children.stream().allMatch(nn -> nn.type.equals(n.type))) {
          Ngram.replaceNode(tree, n);
        }
        // If a MC/SC/GC/GPSJ has no children (i.e., is empty) remove it
        if (n.children.isEmpty()) {
          Ngram.removeNode(tree, n);
        }
      default:
        break;
      }
      return -1;
    });
  }

  /**
   * Get lowest-level clauses.
   * @param ngram current ngram
   * @return ngram leaves
   */
  public static Set<Ngram> simpleClauses(final Ngram ngram) {
    final Set<Ngram> clauses = Sets.newLinkedHashSet();
    simpleClauses(ngram, clauses);
    return clauses;
  }

  /**
   * Return the smallest ngram clauses. I.e., the clauses that contains leaves.
   * @return smallest ngram clauses
   */
  private static void simpleClauses(final Ngram n, final Set<Ngram> acc) {
    if (!n.children.isEmpty() && n.children.stream().flatMap(c -> c.children.stream()).count() == 0) {
      acc.add(n);
    } else if (!n.children.isEmpty()) {
      n.children.forEach(c -> simpleClauses(c, acc));
    }
  }

  public static <T> void traverse(final Ngram n, final BiFunction<Ngram, T, Boolean> c, final BiFunction<Ngram, T, T> f) {
    traverse(n, c, f, null);
  }

  public static <T> void traverse(final Ngram n, final BiFunction<Ngram, T, Boolean> c, final BiFunction<Ngram, T, T> f, final T acc) {
    final T res = c.apply(n, acc) ? f.apply(n, acc) : acc;
    for (final Ngram child : n.getChildren()) {
      traverse(child, c, f, res);
    }
  }

  public static <T> void traverse(final Ngram n, final BiFunction<Ngram, T, T> f) {
    traverse(n, (c, acc) -> true, f);
  }

  private static <T> void traverseLeaves(final Ngram n, final BiFunction<Ngram, T, T> f) {
    traverse(n, (c, acc) -> c.getChildren().isEmpty() && (!c.tokens.equals("where") || !c.pos().equals(DUMMY_POSITION)), f);
  }

  /** Applied annotations. */
  private Map<String, Pair<AnnotationType, Set<Entity>>> annotations;
  /** Appleid hints. */
  private Map<String, Pair<AnnotationType, Set<Entity>>> hints;
  /** Children. Tokens have no children. */
  public ImmutableList<Ngram> children;
  /** Entity from KB. */
  public final Optional<Entity> mde;
  /** Position in sentence, if any. */
  private final Pair<Integer, Integer> posInPhrase;
  /**
   * Used for the OLAP operator in order to know if the following ngrams 
   * representing an OLAP operator requires the existence of its content in the
   * previous tree (this is required by remove and replace)
   */
  private boolean requiresExistence = true;
  /** Similarity to the synonym. */
  private final Optional<Double> similarity;
  /** Synonym used to reach the element. */
  private final Optional<String> synonym;
  /** Concatenated tokens. Aggregation ngrams have no value. */
  public final String tokens;
  /** Ngram type. */
  public Type type;

  /**
   * @param mde reference to the database entry
   */
  public Ngram(final Ngram m, final Entity mde) {
    this(m.tokens, m.type, m.children, Optional.of(mde), m.similarity, m.synonym, Optional.of(m.posInPhrase));
    this.annotations = m.annotations;
    this.hints = m.hints;
    this.requiresExistence = m.requiresExistence;
  }

  public Ngram(String value, Type type, Entity entity, final Double sim, Pair<Integer, Integer> of) {
    this(value, type, entity, sim, null, of);
  }

  /**
   * Leaf ngram.
   * @param value textual token(s)
   * @param type type of the ngram
   * @param mappingToMde reference to the multi dimensional element
   * @param pos begin/end positions
   */
  public Ngram(final String value, final Type type, final Entity mappingToMde, final Double similarity, final String synonym, final Pair<Integer, Integer> pos) {
    this(value, type, //
        Lists.newArrayList(), mappingToMde == null ? Optional.absent() : Optional.of(mappingToMde), //
        Optional.of(similarity), //
        synonym == null ? Optional.absent() : Optional.of(synonym), //
        pos == null ? Optional.absent() : Optional.of(pos));
  }

  public Ngram(String value, Type type, Entity entity, Pair<Integer, Integer> of) {
    this(value, type, entity, 1.0, null, of);
  }

  /**
   * 
   * @param tokens
   * @param type
   * @param children
   * @param mde
   * @param similarity
   * @param synonym
   * @param pos
   */
  private Ngram(final String tokens, final Type type, final List<Ngram> children, final Optional<Entity> mde, final Optional<Double> similarity, final Optional<String> synonym, final Optional<Pair<Integer, Integer>> pos) {
    this.tokens = tokens;
    this.type = type;
    if (type.equals(Type.GPSJ)) {
      this.children = ImmutableList.copyOf(children.stream().sorted((n1, n2) -> n1.type.compareTo(n2.type)).collect(Collectors.toList()));
    } else {
      this.children = ImmutableList.copyOf(children);
    }
    this.mde = mde;
    this.posInPhrase = pos.or(DUMMY_POSITION);
    this.similarity = similarity;
    this.synonym = synonym;
    annotations = Maps.newLinkedHashMap();
    hints = Maps.newLinkedHashMap();
  }

  /**
   * Aggregated ngram generated by the grammar.
   * @param type type of the ngram
   * @param children aggregated children
   */
  public Ngram(final Type type, final List<Ngram> children) {
    this("", type, children, Optional.absent(), Optional.absent(), Optional.absent(), Optional.absent());
  }

  public Ngram(final String value, final Type type, final Entity entity) {
    this(value, type, entity, 1.0, null, DUMMY_POSITION);
  }

  /**
   * Add an annotation to this ngram.
   * @param error error type
   * @param validEntities set of valid entities
   */
  public void annotate(final String id, final AnnotationType error, final Set<Entity> validEntities) {
    annotations.put(id, Pair.of(error, validEntities));
  }

  /**
   * Return a new ngram identical to the current one
   * @param completeClone whether to only the content, or also ambiguities, hints, and existance condition
   * @return the cloned ngram
   */
  public Ngram clone(final boolean completeClone) {
    final List<Ngram> clonedNgrams = Lists.newArrayList();
    children.forEach(n -> clonedNgrams.add(n.clone(completeClone)));
    final Ngram cloned = new Ngram(tokens, type, clonedNgrams, mde, similarity, synonym, Optional.of(posInPhrase));
    cloned.requiresExistence = requiresExistence;
    if (completeClone) {
      cloned.annotations = annotations;
      cloned.hints = hints;
    }
    return cloned;
  }

  /**
   * @return count annotations in the tree
   */
  public Set<Entity> getEntitiesInTree() {
    Set<Entity> entities = Sets.newHashSet();
    Ngram.traverse(this, (c, acc) -> { if (c.mde.isPresent()) { entities.add(c.mde()); } return -1; });
    return entities;
  }

  /**
   * @return count annotations in the tree
   */
  public Set<Entry<String, Pair<AnnotationType, Set<Entity>>>> getAnnotationsInTree() {
    Set<Entry<String, Pair<AnnotationType, Set<Entity>>>> annotations = Sets.newHashSet();
    Ngram.traverse(this, (c, acc) -> { if (!c.getAnnotations().isEmpty()) { annotations.addAll(c.getAnnotations().entrySet()); } return -1; });
    return annotations;
  }

  /**
   * @return count annotated nodes in the tree
   */
  public int countAnnotatedNodesInTree() {
    LongAdder adder = new LongAdder();
    Ngram.traverse(this, (c, acc) -> { if (!c.getAnnotations().isEmpty()) { adder.add(1); } return -1; });
    return adder.intValue();
  }

  /**
   * @return count all the annotations in the tree
   */
  public int countAnnotationsInTree() {
    LongAdder adder = new LongAdder();
    Ngram.traverse(this, (c, acc) -> { c.getAnnotations().entrySet().forEach(a -> adder.add(1)); return -1; });
    return adder.intValue();
  }

  /**
   * @return count hints in the tree
   */
  public Object countHintsInTree() {
    LongAdder adder = new LongAdder();
    Ngram.traverse(this, (c, acc) -> { if (!c.getHints().isEmpty()) adder.add(1); return -1; });
    return adder.intValue();
  }

  /**
   * Count the children nodes recursively.
   * @return number of nested node (plus self)
   */
  public int countNode() {
    int s = 1;
    for (final Ngram c : children) {
      if (!c.children.isEmpty()) {
        s += c.countNode();
      } else {
        s++;
      }
    }
    return s;
  }

  /**
   * Solve the annotation
   * @param annotation annotation to solve
   * @param approximateVal value
   * @param log log
   * @param isHint if the annotation is an ambiguity or an hint
   */
  public void disambiguate(final Pair<AnnotationType, Set<Entity>> annotation, final String approximateVal, final List<Triple<AnnotationType, Ngram, Ngram>> log, final boolean isHint) {
    final Ngram orig = clone(false);
    final List<Ngram> prev = Lists.newLinkedList(children);
    final Optional<Entity> value = Optional.fromJavaUtil(//
        annotation.getValue()//
          .stream()//
          .max(Comparator.comparingDouble(e -> Utils.tokenSimilarity(e.nameInTable(), approximateVal)))
      );
    switch (annotation.getKey()) {
    case AVM:
      final Ngram lev0 = prev.stream().filter(nn -> nn.type.equals(Type.ATTR)).findAny().get();
      final Ngram cop0 = prev.stream().filter(nn -> nn.type.equals(Type.COP)).findAny().get();
      if (value.isPresent()) {
        setChildren(ImmutableList.of(lev0, cop0, new Ngram(value.get().nameInTable(), Type.VAL, value.get())));
      } else {
        // if value comes from integer, such values is not present in the list of entities associated to the ambiguity
        setChildren(ImmutableList.of(lev0, cop0, new Ngram(approximateVal, Type.VAL, new Entity(approximateVal))));
      }
      break;
    case MDMV:
      final Ngram mea0 = prev.stream().filter(nn -> nn.type.equals(Type.MEA)).findAny().get();
      setChildren(ImmutableList.of(new Ngram(value.get().nameInTable(), Type.AGG, value.get()), mea0));
      break;
    case MA:
      if (!isHint) {
        prev.add(0, new Ngram(value.get().nameInTable(), Type.AGG, value.get()));
        setChildren(ImmutableList.copyOf(prev));
      } else {
        final Ngram mea1 = prev.stream().filter(nn -> nn.type.equals(Type.MEA)).findAny().get();
        setChildren(ImmutableList.of(new Ngram(value.get().nameInTable(), Type.AGG, value.get()), mea1));
      }
      break;
    case AA:
      if (!isHint) {
        prev.add(0, new Ngram(value.get().nameInTable(), Type.ATTR, value.get()));
        prev.add(1, Ngram.DUMMY_EQ);
        setChildren(ImmutableList.copyOf(prev));
      } else {
        final Ngram cop1 = prev.stream().filter(nn -> nn.type.equals(Type.COP)).findAny().get();
        final Ngram val1 = prev.stream().filter(nn -> nn.type.equals(Type.VAL)).findAny().get();
        setChildren(ImmutableList.of(new Ngram(value.get().nameInTable(), Type.ATTR, value.get()), cop1, val1));
      }
      break;
    case BA:
      if (!isHint) {
        prev.add(new Ngram(value.get().nameInTable(), Type.ATTR, value.get()));
        setChildren(ImmutableList.copyOf(prev));
      } else {
        final Ngram attr2 = prev.stream().filter(nn -> nn.type.equals(Type.ATTR)).findAny().get();
        setChildren(ImmutableList.of(attr2, new Ngram(value.get().nameInTable(), Type.ATTR, value.get())));
      }
      break;
    case CA:
      if (!isHint) {
        prev.add(0, new Ngram(value.get().nameInTable(), Type.ATTR, value.get()));
        setChildren(ImmutableList.copyOf(prev));
      } else {
        final Ngram attr1 = prev.stream().filter(nn -> nn.type.equals(Type.ATTR)).findAny().get();
        setChildren(ImmutableList.of(attr1, new Ngram(value.get().nameInTable(), Type.ATTR, value.get())));
      }
      break;
    default:
      throw new IllegalArgumentException("Unknown " + annotation);
    }
    log.add(Triple.of(annotation.getKey(), orig, clone(false)));
  }

  /**
   * Find and solve the annotation
   * @param annotationId id of the ambiguity to find
   * @param approximateVal value
   */
  public void disambiguate(String annotationId, String approximateVal, final List<Triple<AnnotationType, Ngram, Ngram>> log) {
    final LongAdder adder = new LongAdder();
    Ngram.traverse(this, (n, acc) -> n.getAnnotations().containsKey(annotationId) || n.getHints().containsKey(annotationId), (n, acc) -> { 
      adder.add(1);
      final Pair<AnnotationType, Set<Entity>> annotation = n.annotations.getOrDefault(annotationId, n.hints.get(annotationId));
      n.disambiguate(annotation, approximateVal, log, n.getHints().containsKey(annotationId));
      n.getAnnotations().remove(annotationId);
      n.getHints().remove(annotationId);
      return -1;
    });
    if (adder.intValue() == 0) {
      throw new IllegalArgumentException("Cannot find annotation with id: " + annotationId);
    }
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj instanceof Ngram) {
      final Ngram n = (Ngram) obj;
          return type.equals(n.type) // with the same type 
              && tokens.equals(n.tokens) // and natural language tokens
              && children.equals(n.children) // and children
              && mde.equals(n.mde) // and md-element
              && posInPhrase.equals(n.posInPhrase) // and position in the natural language text
              && synonym.equals(n.synonym) // and synonym used to retrieve the ngram
              && Math.abs(similarity.or(0.0) - n.similarity.or(0.0)) < 0.001;// and synonym similarity btw syn and tokens
    }
    return false;
  }

  /**
   * @return annotations used for disambiguation
   */
  public Map<String, Pair<AnnotationType, Set<Entity>>> getAnnotations() {
    return annotations;
  }

  /**
   * @return annotations used for disambiguation
   */
  public Map<String, Pair<AnnotationType, Set<Entity>>> getHints() {
    return hints;
  }

  /**
   * @return ngram's children
   */
  public List<Ngram> getChildren() {
    return children;
  }

  @Override
  public int hashCode() {
    return Objects.hash(type, tokens, children, mde, posInPhrase);
  }

  /**
   * Automatically solve frequent disambiguations
   * @param tau frequency threshold
   * @param log list of previous disambiguations
   */
  public void hint(double tau, List<Triple<AnnotationType, Ngram, Ngram>> log) {
    Ngram.traverse(this, (n, acc) -> !n.getAnnotations().isEmpty(), (n, acc) -> {
      final Iterator<Entry<String, Pair<AnnotationType, Set<Entity>>>> iterator = n.getAnnotations().entrySet().iterator();

      //  if (n.getAnnotations().size() > 1) {
      //    // TODO: probably the management of multiple node annotations is wrong. This happens with UP and MA
      //    throw new IllegalAccessError("More than one annotation per node: " + n.getAnnotations());
      //  }

      while (iterator.hasNext()) {
        final Entry<String, Pair<AnnotationType, Set<Entity>>> e = iterator.next();
        final AnnotationType a = e.getValue().getLeft();
        if (a.equals(AnnotationType.BA)) { // DO NOT DISAMBIGUATE BA
          continue;
        }
        final Optional<Pair<Ngram, Double>> mf = //
            Optional.fromJavaUtil(//
              log.stream()//
                  .filter(l -> l.getLeft().equals(a) && l.getMiddle().partialEquals(n))//
                  .map(l -> l.getRight()).distinct().map(l -> Pair.of(l, frequency(log, Pair.of(a, n), l)))
                  .max((l1, l2) -> Double.compare(l1.getRight(), l2.getRight()))//
            );
        if (!mf.isPresent() || mf.get().getRight() <= tau) {
          continue;
        }
        final Ngram mostFrequentEntry = mf.get().getLeft().clone(false);
        if (a.equals(AnnotationType.BA)) { // a parent cannot replace itself
          Ngram.completeNode(this, n, mostFrequentEntry, true);
          n.getAnnotations().putAll(n.getAnnotations());
          n.hints.put(e.getKey(), e.getValue());
          iterator.remove();
        } else {
          Ngram.replaceNode(this, n, mostFrequentEntry, true);
          iterator.remove();
          mostFrequentEntry.getAnnotations().putAll(n.getAnnotations());
          mostFrequentEntry.hints.put(e.getKey(), e.getValue());
          mostFrequentEntry.requiresExistence  = n.requiresExistence;
        }
      }
      return -1;
    });
  }

  /**
   * @return mapping to element in the datawarehouse
   */
  public Entity mde() {
    if (!mde.isPresent()) {
      throw new IllegalArgumentException(this + "has empty mde");
    }
    return mde.get();
  }

  /**
   * Check if two ngram are equals but not with respect to their position in the text
   * @param obj another ngram
   * @return true if the two ngrams are equal
   */
  public boolean partialContains(final Object obj) {
    if (obj instanceof Ngram) {
      final Ngram n = (Ngram) obj;
      boolean tmp = type.equals(n.type) && mde.equals(n.mde) && children.size() >= n.children.size();
      if (!tmp) {
        return false;
      }
      final Set<Ngram> myChildren = Sets.newLinkedHashSet(children);
      for (Ngram c: Sets.newLinkedHashSet(n.children)) {
        tmp &= myChildren.stream().anyMatch(cc -> cc.partialContains(c));
        if (!tmp) {
          return false;
        }
      }
    }
    return true;
  }

  /**
   * Check if two ngram are equals but not with respect to their position in the text
   * @param obj another ngram
   * @return true if the two ngrams are equal
   */
  public boolean partialEquals(final Object obj) {
    if (obj instanceof Ngram) {
      final Ngram n = (Ngram) obj;
      boolean tmp = type.equals(n.type) && mde.equals(n.mde) && children.size() == n.children.size();
      for (int i = 0; i < children.size() && tmp; i++) {
        tmp &= children.get(i).partialEquals(n.children.get(i));
      }
      return tmp;
    }
    return false;
  }

  /**
   * @return token position in sentence
   */
  public Pair<Integer, Integer> pos() {
    return posInPhrase;
  }

  /**
   * @return true if requires existence, false otherwise
   */
  public boolean requiresExistence() {
    return requiresExistence;
  }

  /**
   * Update the children of the ngram. Used in the expand function.
   * @param children new children
   */
  public void setChildren(final ImmutableList<Ngram> children) {
    this.children = children;
    if (type.equals(Type.MC) || type.equals(Type.SC)) {
      for (final Ngram c : children) {
        Ngram.traverse(c, (n, acc) -> {
          n.requiresExistence = requiresExistence;
          return -1;
        });
      }
    }
  }

  public void setRequiresExistence(final boolean requiresExistence) {
    this.requiresExistence = requiresExistence;
  }

  public void setNotRequiresExistence() {
    requiresExistence = false;
    for (final Ngram c: children) {
      Ngram.traverse(c, (n, acc) -> { n.requiresExistence = requiresExistence; return -1; });
    }
  }

  /**
   * @return the average similarity
   */
  public Double similarity() {
    return mde.isPresent() ? similarity.get() : avgSimilarity(this);
  }

  public JSONObject toJSON(final Cube cube) {
    final JSONObject res = new JSONObject();
    res.put("type", type);
    exportAnnotation(cube, annotations, res, true);
    exportAnnotation(cube, hints, res, false);

    if (children.isEmpty()) {
      res.put("tokens", tokens);
      if (mde.isPresent()) {
        res.put("entity", mde());
      }
      if (synonym.isPresent()) {
        res.put("synonym", synonym.get());
        res.put("similarity", Utils.DF.format(similarity()));
      }
    } else {
      children.stream().forEach(c -> res.append("children", c.toJSON(cube)));
    }
    return res;
  }

  private void exportAnnotation(final Cube cube, final Map<String, Pair<AnnotationType, Set<Entity>>> map, final JSONObject res, final boolean isAnnotation) {
    map.forEach((key, value) -> {
      final JSONObject ann = new JSONObject();
      final boolean isError;
      switch (value.getLeft()) {
        case EAE:
        case ENE:
        case GSA:
        case A2:
        case A3:
          isError = true;
          break;
        default:
          isError = false;
          break;
      }
      ann.put("type", isAnnotation ? (isError ? "error" : "annotation") : "hint");
      ann.put("annotationid", key);
      final JSONObject amb = new JSONObject();
      if (value.getRight().isEmpty()) { // add values even if the list is empty
        amb.put("values", Lists.newArrayList());
      } else {
        value.getRight().forEach(v -> amb.append("values", v));
      }
      if (value.getLeft().equals(AnnotationType.AVM)) { // in case of AVM also describe the level involved in the ambiguity
        amb.put("describe", QueryGenerator.describeLevel2JSON(cube, children.stream().filter(cc -> cc.type.equals(Type.ATTR)).findAny().get().mde.get().nameInTable(), 5));
      }
      ann.put(value.getLeft().toString(), amb);
      ann.put("name", value.getLeft().toString());
      ann.put("val", amb);
      res.append("annotations", ann);
    });
  }

  @Override
  public String toString() {
    if (children.isEmpty()) {
      return "{\"" + type + "\":\"" + tokens + pos() + "\"" // token with position
          + (mde.isPresent() ? ",\"mde\":" + mde().toString() : "")
          + (synonym.isPresent() ? ",\"syn\":\"" + synonym.get() + "\",\"sim\":" + Utils.DF.format(similarity()) : "")
          + "}";
    }
    return "{\"" + type + "\":" + children + "}";
  }
  /**
   * Return the ngram as a string tree parsable by zhsh. Removes `_` from string.
   * @return string tree parsable by zhsh.
   */
  public String toStringTree() {
    return 
        (children.isEmpty() 
            ? ((type.equals(Type.VAL) ? "v" : "") +  mde().nameInTable().replace(" ", ""))
            : type + "(" + 
                children.stream()
                        .sorted((a, b) -> type.equals(Type.GPSJ) ? a.type.compareTo(b.type) : 1)
                        .map((Ngram n) -> n.toStringTree()).reduce((n1, n2) -> n1 + " " + n2).get() + ")")
        .replace("_", "") // Tree distance breaks when _ = > < are in the string
        .replace("=", "e")
        .replace(">", "g")
        .replace("<", "l")
        .replace("!", "not");
  }
  /**
   * @return type of the mdelement in the database.
   */
  public DataType typeInDB() {
    return mde().getTypeInDB();
  }
}
