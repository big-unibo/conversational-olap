package it.unibo.conversational.olap;

import com.google.common.collect.Lists;
import it.unibo.antlr.gen.OLAPBaseVisitor;
import it.unibo.antlr.gen.OLAPParser.*;
import it.unibo.conversational.algorithms.Parser.Type;
import it.unibo.conversational.datatypes.Mapping;
import it.unibo.conversational.datatypes.Ngram;
import org.antlr.v4.runtime.tree.ErrorNode;

import java.util.List;


public class CustomOLAPVisitor extends OLAPBaseVisitor<Ngram> {
  final Mapping input;

//  @Override
//  public Operator visit(ParseTree tree) {
//    if (tree instanceof McContext) {
//      return visitMc((McContext) tree);
//    }
//    return super.visit(tree);
//  }
//  public Operator visitMc(McContext ctx) {
//    if (ctx instanceof MeaContext) {
//      visit
//    }
//    return null;
//  }

  public CustomOLAPVisitor(final Mapping input) {
    this.input = input;
  }

  @Override
  public Ngram visitRollup(RollupContext ctx) {
    if (ctx.toAtt != null) {
      final Ngram n = input.ngrams.get(ctx.toAtt.getTokenIndex());
      n.setNotRequiresExistence();
      return new Operator(Type.ROLLUP, input.ngrams.get(ctx.fromAtt.getTokenIndex()), n);
    } else {
      return new Operator(Type.ROLLUP, input.ngrams.get(ctx.fromAtt.getTokenIndex()));
    }
  }

  @Override
  public Ngram visitErrorNode(ErrorNode node) {
    System.out.println("Error");
    return null;
    // return super.visitErrorNode(node);
  }

  @Override
  public Ngram visitDrill(DrillContext ctx) {
    if (ctx.toAtt != null) {
      final Ngram n = input.ngrams.get(ctx.toAtt.getTokenIndex());
      n.setNotRequiresExistence();
      return new Operator(Type.DRILL, input.ngrams.get(ctx.fromAtt.getTokenIndex()), n);
    } else {
      return new Operator(Type.DRILL, input.ngrams.get(ctx.fromAtt.getTokenIndex()));
    }
  }

  @Override
  public Ngram visitSad(SadContext ctx) {
    final Ngram n = visit(ctx.sel);
    n.setNotRequiresExistence();
    return new Operator(Type.SAD, n);
  }

  @Override
  public Ngram visitRemoveMea(RemoveMeaContext ctx) {
    return new Operator(Type.DROP, visit(ctx.mea));
  }

  @Override
  public Ngram visitRemoveSSC(RemoveSSCContext ctx) {
    return new Operator(Type.DROP, visit(ctx.sel));
  }

  @Override
  public Ngram visitRemoveAttr(RemoveAttrContext ctx) {
    final Ngram n = input.ngrams.get(ctx.att.getTokenIndex());
    return new Operator(Type.DROP, n);
  }

  @Override
  public Ngram visitAddMea(AddMeaContext ctx) {
    final Ngram n = visit(ctx.mea);
    n.setNotRequiresExistence();
    return new Operator(Type.ADD, n);
  }

  @Override
  public Ngram visitAddSSC(AddSSCContext ctx) {
    final Ngram n = visit(ctx.sel);
    n.setNotRequiresExistence();
    return new Operator(Type.ADD, n);
  }

  @Override
  public Ngram visitAddAttr(AddAttrContext ctx) {
    final Ngram n = input.ngrams.get(ctx.att.getTokenIndex());
    n.setNotRequiresExistence();
    return new Operator(Type.ADD, n);
  }

  @Override
  public Ngram visitReplaceAttr(ReplaceAttrContext ctx) {
    final Ngram from = input.ngrams.get(ctx.fromAtt.getTokenIndex());
    final Ngram to = input.ngrams.get(ctx.toAtt.getTokenIndex());
    to.setNotRequiresExistence();
    return new Operator(Type.REPLACE, from, to);
  }

  @Override
  public Ngram visitReplaceMea(ReplaceMeaContext ctx) {
    final Ngram n = visit(ctx.toMea);
    n.setNotRequiresExistence();
    return new Operator(Type.REPLACE, visit(ctx.fromMea), n);
  }

  @Override
  public Operator visitReplaceSSC(ReplaceSSCContext ctx) {
    final Ngram n = visit(ctx.toSel);
    n.setNotRequiresExistence();
    return new Operator(Type.REPLACE, visit(ctx.fromSel), n);
  }

  @Override
  public Ngram visitFact(FactContext ctx) {
    final Ngram agg = ctx.cnt != null ? input.ngrams.get(ctx.cnt.getTokenIndex()) : null;
    if (ctx.fact == null) {
      throw new IllegalArgumentException("Invalid mapping:" + input);
    }
    final Ngram mea = input.ngrams.get(ctx.fact.getTokenIndex());
    return new Ngram(Type.MC, agg != null ? Lists.newArrayList(agg, mea) : Lists.newArrayList(mea));
  }

  @Override
  public Ngram visitMea(MeaContext ctx) {
    final Ngram agg = ctx.agg != null ? input.ngrams.get(ctx.agg.getTokenIndex()) : null;
    if (ctx.mea == null) {
      throw new IllegalArgumentException("Invalid mapping:" + input);
    }
    final Ngram mea = input.ngrams.get(ctx.mea.getTokenIndex());
    return new Ngram(Type.MC, agg != null ? Lists.newArrayList(agg, mea) : Lists.newArrayList(mea));
  }

  @Override
  public Ngram visitGc(GcContext ctx) {
    return ctx.attr//
        .stream()//
        .map(t -> input.ngrams.get(t.getTokenIndex()))//
        .reduce(//
            ctx.by != null ? input.ngrams.get(ctx.by.getTokenIndex()) : Ngram.DUMMY_BY, //
            (a, b) -> new Ngram(Type.GC, Lists.newArrayList(a, b))//
        );
  }

  @Override
  public Ngram visitSc(ScContext ctx) {
    return new Ngram(Type.SC, Lists.newArrayList(//
        ctx.where != null ? input.ngrams.get(ctx.where.getTokenIndex()) : Ngram.DUMMY_WHERE, //
        visit(ctx.SSC)//
    ));
  }

  @Override
  public Ngram visitUnary(UnaryContext ctx) {
    return new Ngram(Type.SC, Lists.newArrayList(input.ngrams.get(ctx.unary.getTokenIndex()), visit(ctx.left)));
  }

  @Override
  public Ngram visitBinary(BinaryContext ctx) {
    return new Ngram(Type.SC, Lists.newArrayList(visit(ctx.left), input.ngrams.get(ctx.binary.getTokenIndex()), visit(ctx.right)));
  }

  @Override
  public Ngram visitAtom(AtomContext ctx) {
    final List<Ngram> children = Lists.newArrayList();
    if (ctx.attr1 != null) {
      children.add(input.ngrams.get(ctx.attr1.getTokenIndex()));
      if (input.ngrams.get(ctx.attr1.getTokenIndex()).type != Type.ATTR) {
        throw new IllegalArgumentException("Should be attr");
      }
    }
    if (ctx.cop != null) {
      children.add(input.ngrams.get(ctx.cop.getTokenIndex()));
      if (input.ngrams.get(ctx.cop.getTokenIndex()).type != Type.COP) {
        throw new IllegalArgumentException("Should be COP");
      }
    }
    children.add(input.ngrams.get(ctx.val.getTokenIndex()));
    return new Ngram(Type.SC, children);
  }
}
