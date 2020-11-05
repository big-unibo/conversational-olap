// Generated from Describe.g4 by ANTLR 4.5

package it.unibo.antlr.gen;

import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link DescribeParser}.
 */
public interface DescribeListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link DescribeParser#describe}.
	 * @param ctx the parse tree
	 */
	void enterDescribe(DescribeParser.DescribeContext ctx);
	/**
	 * Exit a parse tree produced by {@link DescribeParser#describe}.
	 * @param ctx the parse tree
	 */
	void exitDescribe(DescribeParser.DescribeContext ctx);
	/**
	 * Enter a parse tree produced by {@link DescribeParser#id}.
	 * @param ctx the parse tree
	 */
	void enterId(DescribeParser.IdContext ctx);
	/**
	 * Exit a parse tree produced by {@link DescribeParser#id}.
	 * @param ctx the parse tree
	 */
	void exitId(DescribeParser.IdContext ctx);
	/**
	 * Enter a parse tree produced by {@link DescribeParser#clause}.
	 * @param ctx the parse tree
	 */
	void enterClause(DescribeParser.ClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link DescribeParser#clause}.
	 * @param ctx the parse tree
	 */
	void exitClause(DescribeParser.ClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link DescribeParser#condition}.
	 * @param ctx the parse tree
	 */
	void enterCondition(DescribeParser.ConditionContext ctx);
	/**
	 * Exit a parse tree produced by {@link DescribeParser#condition}.
	 * @param ctx the parse tree
	 */
	void exitCondition(DescribeParser.ConditionContext ctx);
	/**
	 * Enter a parse tree produced by {@link DescribeParser#value}.
	 * @param ctx the parse tree
	 */
	void enterValue(DescribeParser.ValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link DescribeParser#value}.
	 * @param ctx the parse tree
	 */
	void exitValue(DescribeParser.ValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link DescribeParser#comparator}.
	 * @param ctx the parse tree
	 */
	void enterComparator(DescribeParser.ComparatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link DescribeParser#comparator}.
	 * @param ctx the parse tree
	 */
	void exitComparator(DescribeParser.ComparatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link DescribeParser#binary}.
	 * @param ctx the parse tree
	 */
	void enterBinary(DescribeParser.BinaryContext ctx);
	/**
	 * Exit a parse tree produced by {@link DescribeParser#binary}.
	 * @param ctx the parse tree
	 */
	void exitBinary(DescribeParser.BinaryContext ctx);
	/**
	 * Enter a parse tree produced by {@link DescribeParser#bool}.
	 * @param ctx the parse tree
	 */
	void enterBool(DescribeParser.BoolContext ctx);
	/**
	 * Exit a parse tree produced by {@link DescribeParser#bool}.
	 * @param ctx the parse tree
	 */
	void exitBool(DescribeParser.BoolContext ctx);
}