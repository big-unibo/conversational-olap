// Generated from Describe.g4 by ANTLR 4.5

package it.unibo.antlr.gen;

import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link DescribeParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface DescribeVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link DescribeParser#describe}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDescribe(DescribeParser.DescribeContext ctx);
	/**
	 * Visit a parse tree produced by {@link DescribeParser#id}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitId(DescribeParser.IdContext ctx);
	/**
	 * Visit a parse tree produced by {@link DescribeParser#clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClause(DescribeParser.ClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link DescribeParser#condition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCondition(DescribeParser.ConditionContext ctx);
	/**
	 * Visit a parse tree produced by {@link DescribeParser#value}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValue(DescribeParser.ValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link DescribeParser#comparator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComparator(DescribeParser.ComparatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link DescribeParser#binary}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBinary(DescribeParser.BinaryContext ctx);
	/**
	 * Visit a parse tree produced by {@link DescribeParser#bool}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBool(DescribeParser.BoolContext ctx);
}