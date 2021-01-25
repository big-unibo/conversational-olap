// Generated from COOL.g4 by ANTLR 4.8

package it.unibo.antlr.gen;

import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link COOLParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface COOLVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by the {@code full}
	 * labeled alternative in {@link COOLParser#init}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFull(COOLParser.FullContext ctx);
	/**
	 * Visit a parse tree produced by the {@code partial}
	 * labeled alternative in {@link COOLParser#init}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPartial(COOLParser.PartialContext ctx);
	/**
	 * Visit a parse tree produced by {@link COOLParser#unparsed}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnparsed(COOLParser.UnparsedContext ctx);
	/**
	 * Visit a parse tree produced by {@link COOLParser#gpsj}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGpsj(COOLParser.GpsjContext ctx);
	/**
	 * Visit a parse tree produced by the {@code mea}
	 * labeled alternative in {@link COOLParser#mc}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMea(COOLParser.MeaContext ctx);
	/**
	 * Visit a parse tree produced by the {@code fact}
	 * labeled alternative in {@link COOLParser#mc}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFact(COOLParser.FactContext ctx);
	/**
	 * Visit a parse tree produced by {@link COOLParser#sc}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSc(COOLParser.ScContext ctx);
	/**
	 * Visit a parse tree produced by the {@code binary}
	 * labeled alternative in {@link COOLParser#ssc}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBinary(COOLParser.BinaryContext ctx);
	/**
	 * Visit a parse tree produced by the {@code unary}
	 * labeled alternative in {@link COOLParser#ssc}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnary(COOLParser.UnaryContext ctx);
	/**
	 * Visit a parse tree produced by the {@code atom}
	 * labeled alternative in {@link COOLParser#ssc}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAtom(COOLParser.AtomContext ctx);
	/**
	 * Visit a parse tree produced by {@link COOLParser#gc}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGc(COOLParser.GcContext ctx);
}