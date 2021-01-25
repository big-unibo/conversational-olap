// Generated from COOL.g4 by ANTLR 4.8

package it.unibo.antlr.gen;

import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link COOLParser}.
 */
public interface COOLListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by the {@code full}
	 * labeled alternative in {@link COOLParser#init}.
	 * @param ctx the parse tree
	 */
	void enterFull(COOLParser.FullContext ctx);
	/**
	 * Exit a parse tree produced by the {@code full}
	 * labeled alternative in {@link COOLParser#init}.
	 * @param ctx the parse tree
	 */
	void exitFull(COOLParser.FullContext ctx);
	/**
	 * Enter a parse tree produced by the {@code partial}
	 * labeled alternative in {@link COOLParser#init}.
	 * @param ctx the parse tree
	 */
	void enterPartial(COOLParser.PartialContext ctx);
	/**
	 * Exit a parse tree produced by the {@code partial}
	 * labeled alternative in {@link COOLParser#init}.
	 * @param ctx the parse tree
	 */
	void exitPartial(COOLParser.PartialContext ctx);
	/**
	 * Enter a parse tree produced by {@link COOLParser#unparsed}.
	 * @param ctx the parse tree
	 */
	void enterUnparsed(COOLParser.UnparsedContext ctx);
	/**
	 * Exit a parse tree produced by {@link COOLParser#unparsed}.
	 * @param ctx the parse tree
	 */
	void exitUnparsed(COOLParser.UnparsedContext ctx);
	/**
	 * Enter a parse tree produced by {@link COOLParser#gpsj}.
	 * @param ctx the parse tree
	 */
	void enterGpsj(COOLParser.GpsjContext ctx);
	/**
	 * Exit a parse tree produced by {@link COOLParser#gpsj}.
	 * @param ctx the parse tree
	 */
	void exitGpsj(COOLParser.GpsjContext ctx);
	/**
	 * Enter a parse tree produced by the {@code mea}
	 * labeled alternative in {@link COOLParser#mc}.
	 * @param ctx the parse tree
	 */
	void enterMea(COOLParser.MeaContext ctx);
	/**
	 * Exit a parse tree produced by the {@code mea}
	 * labeled alternative in {@link COOLParser#mc}.
	 * @param ctx the parse tree
	 */
	void exitMea(COOLParser.MeaContext ctx);
	/**
	 * Enter a parse tree produced by the {@code fact}
	 * labeled alternative in {@link COOLParser#mc}.
	 * @param ctx the parse tree
	 */
	void enterFact(COOLParser.FactContext ctx);
	/**
	 * Exit a parse tree produced by the {@code fact}
	 * labeled alternative in {@link COOLParser#mc}.
	 * @param ctx the parse tree
	 */
	void exitFact(COOLParser.FactContext ctx);
	/**
	 * Enter a parse tree produced by {@link COOLParser#sc}.
	 * @param ctx the parse tree
	 */
	void enterSc(COOLParser.ScContext ctx);
	/**
	 * Exit a parse tree produced by {@link COOLParser#sc}.
	 * @param ctx the parse tree
	 */
	void exitSc(COOLParser.ScContext ctx);
	/**
	 * Enter a parse tree produced by the {@code binary}
	 * labeled alternative in {@link COOLParser#ssc}.
	 * @param ctx the parse tree
	 */
	void enterBinary(COOLParser.BinaryContext ctx);
	/**
	 * Exit a parse tree produced by the {@code binary}
	 * labeled alternative in {@link COOLParser#ssc}.
	 * @param ctx the parse tree
	 */
	void exitBinary(COOLParser.BinaryContext ctx);
	/**
	 * Enter a parse tree produced by the {@code unary}
	 * labeled alternative in {@link COOLParser#ssc}.
	 * @param ctx the parse tree
	 */
	void enterUnary(COOLParser.UnaryContext ctx);
	/**
	 * Exit a parse tree produced by the {@code unary}
	 * labeled alternative in {@link COOLParser#ssc}.
	 * @param ctx the parse tree
	 */
	void exitUnary(COOLParser.UnaryContext ctx);
	/**
	 * Enter a parse tree produced by the {@code atom}
	 * labeled alternative in {@link COOLParser#ssc}.
	 * @param ctx the parse tree
	 */
	void enterAtom(COOLParser.AtomContext ctx);
	/**
	 * Exit a parse tree produced by the {@code atom}
	 * labeled alternative in {@link COOLParser#ssc}.
	 * @param ctx the parse tree
	 */
	void exitAtom(COOLParser.AtomContext ctx);
	/**
	 * Enter a parse tree produced by {@link COOLParser#gc}.
	 * @param ctx the parse tree
	 */
	void enterGc(COOLParser.GcContext ctx);
	/**
	 * Exit a parse tree produced by {@link COOLParser#gc}.
	 * @param ctx the parse tree
	 */
	void exitGc(COOLParser.GcContext ctx);
}