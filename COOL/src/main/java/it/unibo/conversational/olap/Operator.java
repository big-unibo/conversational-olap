package it.unibo.conversational.olap;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import it.unibo.antlr.gen.OLAPLexer;
import it.unibo.antlr.gen.OLAPParser;
import it.unibo.conversational.algorithms.Parser.Type;
import it.unibo.conversational.database.Cube;
import it.unibo.conversational.datatypes.Mapping;
import it.unibo.conversational.datatypes.Ngram;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.BailErrorStrategy;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ConsoleErrorListener;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.antlr.v4.runtime.tree.ParseTree;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Arrays;

public class Operator extends Ngram {

  /** serialVersionUID */
  private static final long serialVersionUID = -2563099056296168317L;

  /**
   * Parse a mapping.
   * @param gpsj mapping to be translated
   * @return parsing interpretations sorted by number of matched entities
   */
  public static Optional<Mapping> parse(final Cube cube, final Mapping input) {
    final OLAPLexer lexer = new OLAPLexer(new ANTLRInputStream(input.getMappedNgrams())); // new ANTLRInputStream(System.in);
    lexer.removeErrorListener(ConsoleErrorListener.INSTANCE);
    final CommonTokenStream tokens = new CommonTokenStream(lexer); // create a buffer of tokens pulled from the lexer
    try {
      final OLAPParser parser = new OLAPParser(tokens); // create a parser that feeds off the tokens buffer
      parser.setErrorHandler(new BailErrorStrategy());
      parser.removeErrorListener(ConsoleErrorListener.INSTANCE);
      final ParseTree tree = parser.operator(); // begin parsing at init rule
      final Operator answer = (Operator) new CustomOLAPVisitor(input).visit(tree);
      return answer == null ? Optional.absent() : Optional.of(new Mapping(cube, answer));
    } catch (ParseCancellationException e) {
      return Optional.absent();
    }
  }

  public Operator(final Type type, final Ngram... ngrams) {
    super(type, Arrays.asList(ngrams));
  }

  public void add(final Ngram gpsj, final Ngram n) {
    switch (n.type) {
    case MC:
      final Optional<Ngram> mc = Ngram.find(gpsj, (c, acc) -> c.type.equals(Type.MC));
      if (!mc.isPresent()) { // there was no MC before
        throw new IllegalArgumentException("There should always be a MC");
      } else { // there was a MC before
        Ngram.replaceNode(gpsj, mc.get(), new Ngram(Type.MC, Lists.newArrayList(mc.get(), n)));
      }
      break;
    case SC:
      final Optional<Ngram> sc = Ngram.find(gpsj, (c, acc) -> c.type.equals(Type.SC));
      if (!sc.isPresent()) { // there was no SC before
        Ngram.addNode(gpsj, new Ngram(Type.SC, Lists.newArrayList(DUMMY_WHERE, n)));
      } else { // there was a SC before
        final Ngram innersc = sc.get().children.stream().filter(c -> c.type.equals(Type.SC)).findAny().get();
        Ngram.replaceNode(gpsj, innersc, new Ngram(Type.SC, Lists.newArrayList(innersc, DUMMY_AND, n)));
      }
      break;
    case ATTR:
      final Optional<Ngram> gc = Ngram.find(gpsj, (c, acc) -> c.type.equals(Type.GC));
      if (!gc.isPresent()) { // there was no GC before
        Ngram.addNode(gpsj, new Ngram(Type.GC, Lists.newArrayList(DUMMY_BY, n)));
      } else { // there was a GC before
        final Optional<Ngram> innergc = Ngram.find(gc.get(), (c, acc) -> c.type.equals(Type.GC));
        Ngram.replaceNode(gpsj, innergc.get(), new Ngram(Type.GC, Lists.newArrayList(innergc.get(), n)));
      }
      break;
    default:
      throw new IllegalArgumentException("Unhandled " + n.type);
    }
  }

  public void apply(final Ngram gpsj) {
    switch (type) {
    case DRILL:
      if (children.get(0).mde().nameInTable().startsWith("all_")) {
        type = Type.ADD;
        add(gpsj, children.get(1));
        break;
      }
    case ROLLUP:
      if (children.get(1).mde().nameInTable().startsWith("all_")) {
        type = Type.DROP;
        remove(gpsj, children.get(0));
        break;
      }
    case REPLACE:
      replace(gpsj, children.get(0), children.get(1));
      break;
    case SAD:
    case ADD:
      add(gpsj, children.get(0));
      break;
    case DROP:
      remove(gpsj, children.get(0));
      break;
    default:
      throw new IllegalArgumentException("Unhandled " + type);
    }
  }

  public void remove(final Ngram gpsj, final Ngram n) {
    Ngram.removeNode(gpsj, n, true);
  }

  public void replace(final Ngram gpsj, final Ngram from, final Ngram to) {
    Ngram.replaceNode(gpsj, from, to, true);
  }

  public void sad(final Ngram gpsj, final Ngram n) {
    add(gpsj, n);
  }

  /**
   * @return serialized mapping
   * @throws IOException in case of error
   */
  public byte[] serialize() throws IOException {
    final ByteArrayOutputStream baos = new ByteArrayOutputStream();
    final ObjectOutputStream outStream = new ObjectOutputStream(baos);
    outStream.writeObject(this);
    return baos.toByteArray();
  }
}
