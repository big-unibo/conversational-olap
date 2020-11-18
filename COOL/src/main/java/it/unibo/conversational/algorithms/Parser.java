package it.unibo.conversational.algorithms;

import com.google.common.base.Optional;
import com.google.common.collect.*;
import it.unibo.antlr.gen.COOLLexer;
import it.unibo.antlr.gen.COOLParser;
import it.unibo.conversational.Utils.DataType;
import it.unibo.conversational.antlr.CustomConversationalVisitor;
import it.unibo.conversational.database.Cube;
import it.unibo.conversational.database.QueryGenerator;
import it.unibo.conversational.datatypes.DependencyGraph;
import it.unibo.conversational.datatypes.Entity;
import it.unibo.conversational.datatypes.Mapping;
import it.unibo.conversational.datatypes.Ngram;
import it.unibo.conversational.datatypes.Ngram.AnnotationType;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.BailErrorStrategy;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ConsoleErrorListener;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.antlr.v4.runtime.tree.ParseTree;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

/**
 * Handling grammar and paring.
 */
public final class Parser {

  private Parser() {
  }

  /**
   * Rules type.
   */
  public enum Type {
    DRILL, ROLLUP, SAD, ADD, DROP, REPLACE, ACCESSORY,
    /** Measure clause. */
    MC,
    /** Measure. */
    MEA,
    /** Fact name. */
    FACT,
    /** Hierarchy. */
    H,
    /** Measure aggregation. */
    AGG,
    /** Group by `by`. */
    BY,
    /** Group by `where`. */
    WHERE,
    /** Group by clause. */
    GC,
    /** Level. */
    ATTR,
    /** Comparison operator. */
    COP,
    /** Selection clause. */
    SC,
    /** Value. */
    VAL,
    /** Between operator. */
    BETWEEN,
    /** Logical and. */
    AND,
    /** Logical or. */
    OR,
    /** Logical not. */
    NOT,
    /** `Select`. */
    SELECT,
    /** Container for not mapped tokens. */
    BIN,
    /** Query. */
    GPSJ, PARSEFOREST,
    /** Count, count distinct. */
    COUNT,
    /** Dummy container for Servlet purpose. */
    FOO;
  }

  // This IDS are used only for tests, to assign human friendly ids to ambiguities
  public static boolean TEST = true;
  public static void resetIds() {
    typeCheckId = 0;
    inferId = 0;
    unparsedId = 0;
  }

  private static int typeCheckId = 0;
  private static String incTypeCheckId() {
    final String id = TEST ? "t" + typeCheckId++ : UUID.randomUUID().toString();
    if (TEST) L.debug(id);
    return id;
  }

  private static int inferId = 0;
  private static String incInferId() {
    final String id = TEST ? "i" + inferId++ : UUID.randomUUID().toString();
    if (TEST) L.debug(id);
    return id;
  }

  private static int unparsedId = 0;
  private static String incUnparsedId() {
    final String id = TEST ? "u" + unparsedId++ : UUID.randomUUID().toString();
    if (TEST) L.debug(id);
    return id;
  }

  public static void typeCheck(final Cube cube, final Mapping m) {
    for (Ngram n : m.ngrams.stream().filter(n -> !n.children.isEmpty()).collect(Collectors.toList())) {
      typeCheck(cube, n,
          QueryGenerator.operatorOfMeasure(cube),
          QueryGenerator.membersofLevels(cube),
          QueryGenerator.levelsOfMembers(cube));
    }
  }

  /**
   * Parse a mapping.
   * @param input mapping to be translated
   * @return parsing interpretations sorted by number of matched entities
   */
  public static Optional<Mapping> parse(final Cube cube, final Mapping input) {
    final COOLLexer lexer = new COOLLexer(new ANTLRInputStream(input.getMappedNgrams())); // new ANTLRInputStream(System.in);
    final CommonTokenStream tokens = new CommonTokenStream(lexer); // create a buffer of tokens pulled from the lexer
    try {
       lexer.removeErrorListener(ConsoleErrorListener.INSTANCE);
      final COOLParser parser = new COOLParser(tokens); // create a parser that feeds off the tokens buffer
      parser.setErrorHandler(new BailErrorStrategy());
       parser.removeErrorListener(ConsoleErrorListener.INSTANCE);
      final ParseTree tree = parser.init(); // begin parsing at init rule
      final Ngram answer = new CustomConversationalVisitor(input).visit(tree);
      return answer == null? Optional.absent() : Optional.of(new Mapping(cube, Lists.newArrayList(answer.getChildren())));
    } catch (final ParseCancellationException e) {
      return Optional.absent();
    }
  }

  private static final Logger L = LoggerFactory.getLogger(Parser.class);

  /**
   * Type check the generated parsing tree and annotate the ngrams that do not satisfy the type checking.
   * @param ngram parse tree
   * @param mea2op operator/measure constraints
   * @param attr2val attribute/value constraints
   * @param val2attr value/attribute constraints
   * @return the annotated parse tree
   */
  public static Ngram typeCheck(
      final Cube cube,
      final Ngram ngram,
      final Map<String, Set<Entity>> mea2op,
      final Map<String, Set<Entity>> attr2val,
      final Map<String, Set<Entity>> val2attr) {
    /* *************************************************************************
     * Annotations only for OLAP operator
     ************************************************************************ */
    Ngram.traverse(ngram, (c, acc) -> {
      if ((c.type.equals(Type.DRILL) || c.type.equals(Type.ROLLUP)) && c.children.size() == 2) {
        // after inference, 2 attributes are always present, unless trying to drill down on a dimension
        final List<Ngram> children = c.children.stream().filter(cc -> cc.type.equals(Type.ATTR)).collect(Collectors.toList());
        final Ngram from = children.get(0);
        final Ngram to = children.get(1);
        final String lca = DependencyGraph.lca(cube, from.mde().nameInTable(), to.mde().nameInTable()).orNull();
        if (lca == null) {
          c.annotate(incTypeCheckId(), AnnotationType.A2, Sets.newHashSet(from.mde(), to.mde()));
        } else {
          if (from.getAnnotations().values().stream().noneMatch(p -> p.getKey().equals(AnnotationType.ENE))
              && to.getAnnotations().values().stream().noneMatch(p -> p.getKey().equals(AnnotationType.EAE))
              && (c.type.equals(Type.DRILL) && lca.equalsIgnoreCase(from.mde().nameInTable())
                  || c.type.equals(Type.ROLLUP) && lca.equalsIgnoreCase(to.mde().nameInTable()))) {
            c.annotate(incTypeCheckId(), AnnotationType.GSA, Sets.newHashSet(from.mde(), to.mde()));
          }
        }
      }
      return -1;
    });

    /* *************************************************************************
     * Annotations for both OLAP operator and Full query
     ************************************************************************ */
    for (final Ngram c : Ngram.simpleClauses(ngram)) {
      switch (c.type) {
      case MC:
        if (c.children.size() == 2) { // the clause contains (operator + measure | count fact )
          final Optional<Ngram> op = Optional.fromJavaUtil(c.children.stream().filter(cc -> cc.type.equals(Type.AGG)).findAny());
          if (op.isPresent()) { // (operator + measure)
            final Ngram mea = c.children.stream().filter(cc -> cc.type.equals(Type.MEA)).findAny().get();
            if (!mea2op.isEmpty() && !mea2op.get(mea.mde().nameInTable()).contains(op.get().mde())) {
              c.annotate(incTypeCheckId(), AnnotationType.MDMV, mea2op.get(mea.mde().nameInTable()));
            }
          } else {
            // TODO do nothing, count can only be applied to fact (for now)
          }
        }
        break;
      case GC: // TODO MDGV is not implemented
        break;
      case SC:
        if (c.children.size() >= 2) { // the clause contains both a level and a value
          final Ngram lev = c.children.stream().filter(cc -> cc.type.equals(Type.ATTR)).findAny().get();
          final Ngram val = c.children.stream().filter(cc -> cc.type.equals(Type.VAL)).findAny().get();
          if (lev.typeInDB().equals(DataType.DATE)) {
            if (!(val.typeInDB().equals(DataType.DATE) || val.typeInDB().equals(DataType.NUMERIC))) {
              c.annotate(incTypeCheckId(), AnnotationType.AVM, attr2val.getOrDefault(lev.mde().nameInTable(), Sets.newLinkedHashSet()));
            }
          } else if (!lev.typeInDB().equals(val.typeInDB()) // if the types differ or the member is not in the domain of the level
              || !lev.typeInDB().equals(DataType.NUMERIC) && !val.mde().refToOtherTable().equals(lev.mde().pkInTable())) {
            c.annotate(incTypeCheckId(), AnnotationType.AVM, attr2val.getOrDefault(lev.mde().nameInTable(), Sets.newLinkedHashSet()));
          }
        }
        break;
      default: 
        L.warn("Type checking cannot be applied to " + c);
      }
    }
    return ngram;
  }

  /**
   * Infer missing information and add it to the parse forest.
   * @param mapping current forest
   * @param prevTree context (i.e., parse tree)
   */
  public static void infer(final Cube cube, final Mapping mapping, final Mapping prevTree) {
    for (Ngram n : mapping.ngrams) {
      infer(cube, n, prevTree,
              QueryGenerator.operatorOfMeasure(cube), QueryGenerator.membersofLevels(cube), QueryGenerator.levelsOfMembers(cube),
              QueryGenerator.string2level(cube), QueryGenerator.yearLevels(cube));
      if (!n.equals(mapping.bestNgram)) {
        if (n.type.equals(Type.GPSJ)) {
          n.children.stream().filter(nn -> nn.type.equals(Type.MC)).findAny().get().annotate(incUnparsedId(), AnnotationType.UP, Sets.newHashSet());
        } else {
          n.annotate(incUnparsedId(), AnnotationType.UP, Sets.newHashSet());
        }
      }
    }
  }

  /**
   * Infer missing information and add it to the parse forest.
   * @param mapping current forest
   */
  public static void infer(final Cube cube, final Mapping mapping) {
    infer(cube, mapping, null);
  }

  /**
   * Infer missing information and add it to the parse tree.
   * @param ngram parse tree
   * @param mea2op operator/measure constraints
   * @param attr2val attribute/values constraints
   * @param val2attr value/attributes constraints
   * @param dateAttributes list of date attributes
   * @return a partially filled parse forest
   */
  public static Ngram infer(
      final Cube cube,
      final Ngram ngram,//
      final Map<String, Set<Entity>> mea2op,//
      final Map<String, Set<Entity>> attr2val,//
      final Map<String, Set<Entity>> val2attr,//
      final Map<String, Entity> string2attr,//
      final Set<Entity> dateAttributes) {
    return infer(cube, ngram, null, mea2op, attr2val, val2attr, string2attr, dateAttributes);
  }

  /**
   * Infer missing information and add it to the parsing tree.
   * @param tree parse tree
   * @param mea2op operator/measure constraints
   * @param attr2val attribute/values constraints
   * @param val2attr value/attributes constraints
   * @param dateAttributes list of date attributes
   * @return a partially filled parse forest
   */
  public static Ngram infer(
      final Cube cube,
      final Ngram tree,//
      final Mapping forest,//
      final Map<String, Set<Entity>> mea2op,//
      final Map<String, Set<Entity>> attr2val,//
      final Map<String, Set<Entity>> val2attr,//
      final Map<String, Entity> string2attr,//
      final Set<Entity> dateAttributes) {

    /* *************************************************************************
     * Try to exploit the prevTree (context) to enrich the given structure
     ************************************************************************ */
    final Map<Ngram, Set<Entity>> lookup = Maps.newLinkedHashMap();
    if (forest != null) {
      final Ngram prevTree = forest.getBest();
      for (final Ngram c : Ngram.simpleClauses(tree)) {
        if (c.requiresExistence()) {
          final List<Ngram> parents = Ngram.findContainer(prevTree, c);
          if (parents.size() == 1) { // if the member is already inside the previous context...
            c.setChildren(parents.get(0).children);
          } else if (parents.size() > 1) {
            lookup.put(c, ImmutableSet.copyOf(parents.stream().flatMap(p -> p.getEntitiesInTree().stream()).collect(Collectors.toSet())));
          }
        }
      }

      /* *********************************************************************** 
       * Annotations only for OLAP operator
       * *********************************************************************/
      Ngram.traverse(tree, //
          (c, acc) -> (c.type.equals(Type.DRILL) || c.type.equals(Type.ROLLUP)) && c.children.stream().filter(cc -> cc.type.equals(Type.ATTR)).count() == 1, //
          (c, acc) -> {
            final Ngram attr = c.children.stream().filter(cc -> cc.type.equals(Type.ATTR)).findAny().get();
            final Optional<Ngram> gc = Optional.fromJavaUtil(forest.bestNgram.children.stream().filter(cc -> cc.type.equals(Type.GC)).findAny());

            // GC clause exists and contains the attribute, we consider the given attribute as FROM
            if (gc.isPresent() && Ngram.contains(gc.get(), attr, true)) {
              final Graph<String, DefaultEdge> graph = DependencyGraph.getDependencies(cube);
              final Set<DefaultEdge> edges;
              if (c.type.equals(Type.DRILL)) {
                edges = graph.incomingEdgesOf(attr.mde().nameInTable().toLowerCase());
              } else {
                edges = graph.outgoingEdgesOf(attr.mde().nameInTable().toLowerCase());
              }
              if (edges.size() == 1) { // infer coarser/finer attribute
                final DefaultEdge edge = edges.stream().findAny().get();
                final String otherAttr = c.type.equals(Type.DRILL) ? graph.getEdgeSource(edge) : graph.getEdgeTarget(edge);
                final Ngram n = new Ngram(otherAttr, Type.ATTR, string2attr.get(otherAttr));
                n.setNotRequiresExistence();
                Ngram.addNode(c, n);
              } else if (edges.size() > 1) { // cannot choose in a branch
                c.annotate(incInferId(), AnnotationType.BA, ImmutableSet.copyOf(//
                    edges.stream().map(e -> string2attr.get(c.type.equals(Type.DRILL) ? graph.getEdgeSource(e) : graph.getEdgeTarget(e))).collect(Collectors.toSet()))//
                );
              } else { // already a dimension
                c.annotate(incInferId(), AnnotationType.A3, ImmutableSet.of(attr.mde()));
              }
              return -1;
            }

            final List<Ngram> finer = !gc.isPresent() ? Lists.newArrayList() : Ngram.findMany(gc.get(), //
                (cc, accc) -> {
                  boolean ret = cc.mde.isPresent() && cc.type.equals(Type.ATTR) && !cc.mde().equals(attr.mde());
                  if (ret) {
                    final Optional<String> lca = DependencyGraph.lca(cube, cc.mde().nameInTable(), attr.mde().nameInTable());
                    ret &= lca.isPresent() && lca.get().equalsIgnoreCase(cc.mde().nameInTable()); // NB: in the dependency graph all attributes names are lowercase
                  }
                  return ret;
                });
            final List<Ngram> coarser = !gc.isPresent() ? Lists.newArrayList() : Ngram.findMany(gc.get(), //
                (cc, accc) -> {
                  boolean ret = cc.mde.isPresent() && cc.type.equals(Type.ATTR) && !cc.mde().equals(attr.mde());
                  if (ret) {
                    final Optional<String> lca = DependencyGraph.lca(cube, cc.mde().nameInTable(), attr.mde().nameInTable());
                    ret &= lca.isPresent() && lca.get().equalsIgnoreCase(attr.mde().nameInTable()); // NB: in the dependency graph all attributes names are lowercase
                  }
                  return ret;
                });

            // We already handled the rollup / drill down from the same attribute, then from now on we will try to infer the TO attribute
            if (c.type.equals(Type.ROLLUP)) {
                if (finer.size() == 0) { // If the group by clause does not exists or does not contains a finer attribute, we refer to the "all" level
                  if (coarser.size() == 0) { // GC = {} + roll up to year = roll up all_dates to year 
                    Ngram.addNode(c, new Ngram("all_" + attr.mde().dataTable(), Type.ATTR, new Entity("all_" + attr.mde().dataTable())), 0);
                  } else { // GC = {month, quarter} + roll up to data = roll up month to date
                    coarser.get(0).setRequiresExistence(true);
                    Ngram.addNode(c, coarser.get(0), 0);
                  }
                } else if (finer.size() == 1) { // If there is a single finer attribute, use it as FROM attribute
                  // GC = {month} + roll up to year = roll up month to year
                  finer.get(0).setRequiresExistence(true);
                  Ngram.addNode(c, finer.get(0), 0);
                } else { // If more than one finer attributes exist, don't know from where to rollup
                  // GC = {month, quarter} + roll up to year = Ambiguity
                  c.annotate(incInferId(), AnnotationType.CA, ImmutableSet.copyOf(finer.stream().map(nn -> nn.mde()).collect(Collectors.toSet())));
                }
            } else {
              if (coarser.size() == 0) { // If the group by clause does not exists or does not contains a coarser attribute, we refer to the "all" level
                if (finer.size() == 0) { // GC = {} + drill down to year = drill down all_dates to year
                  Ngram.addNode(c, new Ngram("all_" + attr.mde().dataTable(), Type.ATTR, new Entity("all_" + attr.mde().dataTable())), 0);
                } else { // GC = { date } + drill down to year = drill down date to year 
                  finer.get(0).setRequiresExistence(true);
                  Ngram.addNode(c, finer.get(0), 0);
                }
              } else if (coarser.size() == 1) { // If there is a single coarser attribute, use it as FROM attribute
                // GC = {month} + drill down to date = drill down month to date
                coarser.get(0).setRequiresExistence(true);
                Ngram.addNode(c, coarser.get(0), 0);
              } else { // If more than one coarser attributes exist, don't know from where to drill down
                // GC = {month, quarter} + drill down to date = Ambiguity
                c.annotate(incInferId(), AnnotationType.CA, ImmutableSet.copyOf(coarser.stream().map(Ngram::mde).collect(Collectors.toSet())));
              }
            }
            attr.setNotRequiresExistence();
            return -1;
          });

      Ngram.traverse(tree, (c, acc) -> {
        if (c.type.equals(Type.ATTR) || c.type.equals(Type.MC) || c.type.equals(Type.SC)) {
          if (Ngram.findParent(tree, c).get().getAnnotations().values().stream() // do not annotate a children with EAE or ENE if its parent is already annotated
                  .noneMatch(p -> p.getKey().equals(AnnotationType.ENE) || p.getKey().equals(AnnotationType.EAE)) //
          ) {
            if (!Ngram.contains(prevTree, c, true) && c.requiresExistence()) {
              // all_products is never inside the previous query, do not tag it
              if (!c.type.equals(Type.ATTR) || !c.type.equals(Type.ATTR) && c.mde().nameInTable().startsWith("all_")) {
                c.annotate(incInferId(), AnnotationType.ENE, ImmutableSet.of());
              }
            } else if (Ngram.contains(prevTree, c, true) && !c.requiresExistence()) {
              c.annotate(incInferId(), AnnotationType.EAE, ImmutableSet.of());
            }
          }
        }
        return -1;
      });
    }
    /* *************************************************************************
     * Annotations for both OLAP operator and Full query
     ************************************************************************ */
    for (final Ngram c :  Ngram.simpleClauses(tree)) {
      switch (c.type) {
      case MC:
        if (c.children.size() == 1) { // the clause contains only a measure, add the operator
          final Ngram mea = c.children.stream().filter(cc -> cc.type.equals(Type.MEA) || cc.type.equals(Type.FACT)).findAny().get();
          if (mea.type.equals(Type.FACT)) { // FACT alone, infer COUNT
            c.setChildren(ImmutableList.of(Ngram.DUMMY_CNT, mea));
          } else { // measure alone, check if only an aggregation exists
            final Set<Entity> candidates = ImmutableSet.copyOf(mea2op.get(mea.mde().nameInTable()));
            if (candidates.size() > 1) { // more than one operator exist
              c.annotate(incInferId(), AnnotationType.MA, !lookup.containsKey(c) ? candidates : ImmutableSet.copyOf(Sets.intersection(candidates, lookup.get(c))));
            } else {// a single operator exists
              final Entity operator = candidates.stream().findAny().get();
              final Ngram agg = new Ngram(operator.nameInTable(), Type.AGG, operator);
              c.setChildren(ImmutableList.of(agg, mea));
            }
          }
        }
        break;
      case GC: // do nothing
        break;
      case SC:
        if (c.children.size() == 2) { // the clause contains both a level and a value
          final Ngram lev = c.children.stream().filter(cc -> cc.type.equals(Type.ATTR)).findAny().get();
          final Ngram val = c.children.stream().filter(cc -> cc.type.equals(Type.VAL)).findAny().get();
          c.setChildren(ImmutableList.of(lev, Ngram.DUMMY_EQ, val));
        } else if (c.children.size() == 1) { // the clause contains only a value
          // If you are here, you are either:
          // - a member NUMERIC/DATE member
          // - a member from a categorical attribute with the reference to the corresponding level
          final Ngram val = c.children.stream().filter(cc -> cc.type.equals(Type.VAL)).findAny().get();
          switch (val.typeInDB()) {
          case NUMERIC:
            final double value = Double.parseDouble(val.tokens);
            if (value >= 1900 && value <= 2155) { // you are a date (boundaries are compliant with year type in MySQL)
              final Entity levEntity = dateAttributes.stream().findAny().get(); // TODO an entity is picked randomly
              final Ngram lev = new Ngram(levEntity.nameInTable(), Type.ATTR, levEntity);
              c.setChildren(ImmutableList.of(lev, Ngram.DUMMY_EQ, val));
            } else {
              throw new IllegalArgumentException("What should I do with dangling value " + val + " ?");
            }
            break;
          case DATE:
            throw new IllegalArgumentException("What should I do with dangling date " + val + " ?");
          case STRING:
            final Set<Entity> candidates = ImmutableSet.copyOf(val2attr.get(val.mde().nameInTable()));
            if (candidates != null && !candidates.isEmpty()) {
              if (candidates.size() > 1) { // the member belongs to multiple attributes
                c.annotate(incInferId(), AnnotationType.AA, !lookup.containsKey(c) ? candidates : ImmutableSet.copyOf(Sets.intersection(candidates, lookup.get(c))));
              } else { // the member belongs to a single attribute
                final Entity levEntity = candidates.stream().findAny().get();
                final Ngram lev = new Ngram(levEntity.nameInTable(), Type.ATTR, levEntity);
                c.setChildren(ImmutableList.of(lev, Ngram.DUMMY_EQ, val));
              }
            }
            break;
          default:
            throw new NotImplementedException("This case is not handled: " + val);
          }
        }
        break;
      default:
        L.warn("Inference cannot be applied to " + c);
      }
    }
    return tree;
  }

  public static void automaticDisambiguate(final Mapping ambiguousMapping) {
    automaticDisambiguate(ambiguousMapping, Lists.newLinkedList());
  }

  public static void automaticDisambiguate(final Mapping ambiguousMapping, final List<Triple<AnnotationType, Ngram, Ngram>> log) {
    final List<Ngram> annotatedNgrams = ambiguousMapping.getAnnotatedNgrams();
    if (!annotatedNgrams.isEmpty()) {
      final Ngram n = annotatedNgrams.get(0);
      while (!n.getAnnotations().entrySet().isEmpty()) {
        final Entry<String, Pair<AnnotationType, Set<Entity>>> annotation = n.getAnnotations().entrySet().stream().findFirst().get();
        if (annotation.getValue().getKey().equals(AnnotationType.UP)) {
          ambiguousMapping.disambiguate(annotation.getKey(), "drop");
        } else {
          if (annotation.getValue().getKey().equals(AnnotationType.AVM) && annotation.getValue().getValue().isEmpty()) {
            ambiguousMapping.disambiguate(annotation.getKey(), "-1");
          } else {
            final Entity value = annotation.getValue().getRight().stream().min(Comparator.comparing(Entity::nameInTable)).get();
            ambiguousMapping.disambiguate(annotation.getKey(), value.nameInTable(), log);
          }
        }
      }
    }
  }

  /**
   * Get the SQL version of the query.
   * @param s mapping
   * @return SQL version of the query
   * @throws Exception in case of error
   */
  public static String getSQLQuery(final Cube cube, final Mapping s) throws Exception {
    if (s.bestNgram.type.equals(Type.GPSJ)) {
      return createQuery(cube, s.bestNgram.children);
    }
    throw new IllegalArgumentException("The query is not fully parsed: " + s.toString());
  }

  private static Entity getEntity(final Ngram m, Type... tt) {
    return getEntityWithType(m, tt).getLeft();
  }

  private static Pair<Entity, Type> getEntityWithType(final Ngram m, Type... tt) {
    ImmutableList<Ngram> children = m.children;
    return children.stream().map(c -> Pair.of(c.mde(), c.type)).filter(n -> {
      boolean found = false;
      for (int i = 0; i < tt.length && !found; i++) {
        found = n.getRight().equals(tt[i]);
      }
      return found;
    }).findAny().get();
  }

  private static String createQuery(final Cube cube, final List<Ngram> ngrams) throws Exception {
    Set<Ngram> attributes = Sets.newLinkedHashSet();
    String select = "";
    String from = "";
    String where = "";
    String groupby = "";

    for (final Ngram ngs : ngrams) {
      if (ngs.type.equals(Type.GC)) {
        Set<Ngram> ga = Ngram.leaves(ngs).stream().filter(n -> !n.type.equals(Type.BY)).collect(Collectors.toSet());
        final String attToString = ga.stream().map(nn -> QueryGenerator.getLevel(cube, nn.mde().nameInTable()).fullQualifier()).reduce((a, b) -> a + "," + b).orElse("");
        select += (select.isEmpty() ? "" : ",") + attToString;
        groupby = attToString;
        attributes.addAll(ga);
      } else if (ngs.type.equals(Type.SC)) {
        Set<Ngram> scset = Ngram.simpleClauses(ngs);
        attributes.addAll(scset.stream().flatMap(nn -> Ngram.leaves(nn).stream()).filter(n -> n.type.equals(Type.ATTR)).collect(Collectors.toSet()));
        where = scset.stream()
            .map(sc -> 
                QueryGenerator.getLevel(cube, getEntity(sc, Type.ATTR).nameInTable()).fullQualifier() +
                getEntity(sc, Type.COP).nameInTable() + 
                (getEntity(sc, Type.VAL).getTypeInDB().equals(DataType.STRING) ? "'" : "") + getEntity(sc, Type.VAL).nameInTable() + (getEntity(sc, Type.VAL).getTypeInDB().equals(DataType.STRING) ? "'" : ""))
            .reduce((a, b) -> a + " AND " + b).orElse("");
      } else if (ngs.type.equals(Type.MC)) {
        select += (select.isEmpty() ? "" : ",") + Ngram.simpleClauses(ngs).stream().map(nn -> {
          final Pair<Entity, Type> op = getEntityWithType(nn, Type.AGG, Type.COUNT);
          if (op.getRight().equals(Type.AGG)) {
            return op.getLeft().nameInTable() + "(" + getEntity(nn, Type.MEA).nameInTable() + ")";
          } else {
            return "count(*)";
          }
        }).reduce((a, b) -> a + "," + b).get();
      }
    }

    if (select.equals("")) {
      select = "count(*)";
    }

    Set<String> tabIns = Sets.newLinkedHashSet();
    Pair<String, String> ftdet = QueryGenerator.getFactTable(cube);
    from = " from " + ftdet.getRight() + " ft ";
    for (Ngram gba : attributes) {
      final String idT = gba.mde().refToOtherTable();
      if (!tabIns.contains(idT)) {
        Pair<String, String> detTab = QueryGenerator.getTabDetails(cube, ftdet.getLeft(), idT);
        from += " join " + detTab.getLeft() + " on " + detTab.getLeft() + "." + detTab.getRight() + " = ft." + detTab.getRight();
        tabIns.add(idT);
      }
    }

    return "select " + fixDeviation(cube, select) + " " + from + (where.isEmpty() ? "" : " where " + where) + (groupby.isEmpty() ? "" : " group by " + groupby);
  }

  /**
   * Fix the standard deviation function
   * @param cube cube
   * @param select select clause
   * @return fixed select clause
   */
  private static String fixDeviation(final Cube cube, final String select) {
    if (cube.getDbms().equals("mysql")) {
       return select.replace("stdev", "std");
    } else if (cube.getDbms().equals("oracle")) {
      return select.replace("stdev", "stddev");
    } else {
      return select;
    }
  }
}
