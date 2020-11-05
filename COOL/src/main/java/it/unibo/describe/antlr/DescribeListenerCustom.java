package it.unibo.describe.antlr;

import java.util.stream.Collectors;

import org.antlr.v4.runtime.tree.ErrorNode;
import org.apache.commons.lang3.tuple.Triple;

import com.google.common.base.Optional;

import it.unibo.antlr.gen.DescribeBaseListener;
import it.unibo.antlr.gen.DescribeParser.ConditionContext;
import it.unibo.antlr.gen.DescribeParser.DescribeContext;
import it.unibo.describe.Describe;

public class DescribeListenerCustom extends DescribeBaseListener {

  private final Describe describe;

  public DescribeListenerCustom(Describe d) {
    describe = new Describe(d);
  }

  public Describe getDescribe() {
    return describe;
  }

  @Override
  public void visitErrorNode(ErrorNode node) {
    throw new IllegalArgumentException("Invalid describe syntax");
  }

  @Override
  public void exitDescribe(DescribeContext ctx) {
    describe.setCube(ctx.cube.name);
    describe.setMeasures(ctx.mc.stream().map(m -> m.getText()).collect(Collectors.toList()));
    if (ctx.gc != null) {
      describe.setAttribute(Optional.of(ctx.gc.name));
    }
    if (ctx.models != null) {
      describe.setModels(ctx.models.stream().map(m -> m.getText()).collect(Collectors.toList()));
    }
    if (ctx.k != null) {
      describe.setK(Optional.of(Integer.parseInt(ctx.k.getText())));
    }
  }

  @Override
  public void exitCondition(ConditionContext ctx) {
    describe.addClause(Triple.of(ctx.attr.getText(), ctx.op == null? ctx.in.getText() : ctx.op.getText(), ctx.val.stream().map(c -> c.getText()).collect(Collectors.toList())));
  }
}
