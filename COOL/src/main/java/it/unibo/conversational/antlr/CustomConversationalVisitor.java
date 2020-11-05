package it.unibo.conversational.antlr;

import com.google.common.collect.Lists;
import it.unibo.antlr.gen.COOLBaseVisitor;
import it.unibo.antlr.gen.COOLParser.*;
import it.unibo.conversational.algorithms.Parser.Type;
import it.unibo.conversational.datatypes.Mapping;
import it.unibo.conversational.datatypes.Ngram;

import java.util.List;

public class CustomConversationalVisitor extends COOLBaseVisitor<Ngram> {
  private Mapping mapping;

  public CustomConversationalVisitor(Mapping input) {
    this.mapping = input;
  }

  @Override
  public Ngram visitFull(FullContext ctx) {
    return new Ngram(Type.PARSEFOREST, Lists.newArrayList(visitGpsj(ctx.GPSJ)));
  }

  @Override
  public Ngram visitPartial(PartialContext ctx) {
    if (ctx.GPSJ.exception != null) {
      return null;
    }
    final List<Ngram> children = Lists.newArrayList(visitGpsj(ctx.GPSJ));
    for (UnparsedContext c: ctx.UP) {
      Ngram up = visit(c);
      if (c.exception != null) {
        return null;
      }
      if (up != null) {
        children.add(up);
      }
    }
    return new Ngram(Type.PARSEFOREST, children);
  }

  @Override
  public Ngram visitGpsj(GpsjContext ctx) {
    if (ctx.exception == null) {
      Ngram mc = null;
      for (final McContext c: ctx.MC) {
        final Ngram newmc = visit(c);
        if (newmc == null) {
          return null;
        }
        if (mc == null) {
          mc = newmc;
        } else {
          mc = new Ngram(Type.MC, Lists.newArrayList(mc, newmc));
        }
        
      }
      final List<Ngram> children = Lists.newArrayList(mc);
      if (ctx.GC != null)
        children.add(visitGc(ctx.GC));
      if (ctx.SC != null)
        children.add(visitSc(ctx.SC));
      return new Ngram(Type.GPSJ, children);
    } else {
      return null;
    }
  }

  @Override
  public Ngram visitFact(FactContext ctx) {
    final Ngram agg = ctx.cnt != null ? mapping.ngrams.get(ctx.cnt.getTokenIndex()) : null;
    final Ngram mea = mapping.ngrams.get(ctx.fact.getTokenIndex());
    return new Ngram(Type.MC, agg != null ? Lists.newArrayList(agg, mea) : Lists.newArrayList(mea));
  }

  @Override
  public Ngram visitMea(MeaContext ctx) {
    final Ngram agg = ctx.agg != null ? mapping.ngrams.get(ctx.agg.getTokenIndex()) : null;
    final Ngram mea = mapping.ngrams.get(ctx.mea.getTokenIndex());
    return new Ngram(Type.MC, agg != null ? Lists.newArrayList(agg, mea) : Lists.newArrayList(mea));
  }

  @Override
  public Ngram visitGc(GcContext ctx) {
    return ctx.attr//
        .stream()//
        .map(t -> mapping.ngrams.get(t.getTokenIndex()))//
        .reduce(//
            ctx.by != null ? mapping.ngrams.get(ctx.by.getTokenIndex()) : Ngram.DUMMY_BY, //
            (a, b) -> new Ngram(Type.GC, Lists.newArrayList(a, b))//
        );
  }

  @Override
  public Ngram visitSc(ScContext ctx) {
    return new Ngram(Type.SC, Lists.newArrayList(//
        ctx.where != null ? mapping.ngrams.get(ctx.where.getTokenIndex()) : Ngram.DUMMY_WHERE, //
        visit(ctx.SSC)//
    ));
  }

  @Override
  public Ngram visitUnary(UnaryContext ctx) {
    return new Ngram(Type.SC, Lists.newArrayList(mapping.ngrams.get(ctx.unary.getTokenIndex()), visit(ctx.left)));
  }

  @Override
  public Ngram visitBinary(BinaryContext ctx) {
    return new Ngram(Type.SC, Lists.newArrayList(visit(ctx.left), mapping.ngrams.get(ctx.binary.getTokenIndex()), visit(ctx.right)));
  }

  @Override
  public Ngram visitAtom(AtomContext ctx) {
    final List<Ngram> children = Lists.newArrayList();
    if (ctx.attr1 != null) {
      children.add(mapping.ngrams.get(ctx.attr1.getTokenIndex()));
      if (mapping.ngrams.get(ctx.attr1.getTokenIndex()).type != Type.ATTR) {
        throw new IllegalArgumentException("Should be attr");
      }
    }
    if (ctx.cop != null) {
      children.add(mapping.ngrams.get(ctx.cop.getTokenIndex()));
      if (mapping.ngrams.get(ctx.cop.getTokenIndex()).type != Type.COP) {
        throw new IllegalArgumentException("Should be COP");
      }
    }
    children.add(mapping.ngrams.get(ctx.val.getTokenIndex()));
    return new Ngram(Type.SC, children);
  }
}
