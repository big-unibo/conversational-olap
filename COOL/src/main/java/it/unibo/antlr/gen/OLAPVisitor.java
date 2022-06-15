// Generated from OLAP.g4 by ANTLR 4.10.1

package it.unibo.antlr.gen;

import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link OLAPParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface OLAPVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link OLAPParser#operator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOperator(OLAPParser.OperatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link OLAPParser#replace}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReplace(OLAPParser.ReplaceContext ctx);
	/**
	 * Visit a parse tree produced by the {@code replaceMea}
	 * labeled alternative in {@link OLAPParser#replace_spec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReplaceMea(OLAPParser.ReplaceMeaContext ctx);
	/**
	 * Visit a parse tree produced by the {@code replaceAttr}
	 * labeled alternative in {@link OLAPParser#replace_spec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReplaceAttr(OLAPParser.ReplaceAttrContext ctx);
	/**
	 * Visit a parse tree produced by the {@code replaceSSC}
	 * labeled alternative in {@link OLAPParser#replace_spec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReplaceSSC(OLAPParser.ReplaceSSCContext ctx);
	/**
	 * Visit a parse tree produced by {@link OLAPParser#remove}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRemove(OLAPParser.RemoveContext ctx);
	/**
	 * Visit a parse tree produced by the {@code removeMea}
	 * labeled alternative in {@link OLAPParser#remove_spec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRemoveMea(OLAPParser.RemoveMeaContext ctx);
	/**
	 * Visit a parse tree produced by the {@code removeAttr}
	 * labeled alternative in {@link OLAPParser#remove_spec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRemoveAttr(OLAPParser.RemoveAttrContext ctx);
	/**
	 * Visit a parse tree produced by the {@code removeSSC}
	 * labeled alternative in {@link OLAPParser#remove_spec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRemoveSSC(OLAPParser.RemoveSSCContext ctx);
	/**
	 * Visit a parse tree produced by {@link OLAPParser#add}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAdd(OLAPParser.AddContext ctx);
	/**
	 * Visit a parse tree produced by the {@code addMea}
	 * labeled alternative in {@link OLAPParser#add_spec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAddMea(OLAPParser.AddMeaContext ctx);
	/**
	 * Visit a parse tree produced by the {@code addAttr}
	 * labeled alternative in {@link OLAPParser#add_spec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAddAttr(OLAPParser.AddAttrContext ctx);
	/**
	 * Visit a parse tree produced by the {@code addSSC}
	 * labeled alternative in {@link OLAPParser#add_spec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAddSSC(OLAPParser.AddSSCContext ctx);
	/**
	 * Visit a parse tree produced by {@link OLAPParser#sad}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSad(OLAPParser.SadContext ctx);
	/**
	 * Visit a parse tree produced by {@link OLAPParser#rollup}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRollup(OLAPParser.RollupContext ctx);
	/**
	 * Visit a parse tree produced by {@link OLAPParser#drill}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDrill(OLAPParser.DrillContext ctx);
	/**
	 * Visit a parse tree produced by the {@code full}
	 * labeled alternative in {@link OLAPParser#init}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFull(OLAPParser.FullContext ctx);
	/**
	 * Visit a parse tree produced by the {@code partial}
	 * labeled alternative in {@link OLAPParser#init}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPartial(OLAPParser.PartialContext ctx);
	/**
	 * Visit a parse tree produced by {@link OLAPParser#unparsed}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnparsed(OLAPParser.UnparsedContext ctx);
	/**
	 * Visit a parse tree produced by {@link OLAPParser#gpsj}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGpsj(OLAPParser.GpsjContext ctx);
	/**
	 * Visit a parse tree produced by the {@code mea}
	 * labeled alternative in {@link OLAPParser#mc}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMea(OLAPParser.MeaContext ctx);
	/**
	 * Visit a parse tree produced by the {@code fact}
	 * labeled alternative in {@link OLAPParser#mc}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFact(OLAPParser.FactContext ctx);
	/**
	 * Visit a parse tree produced by {@link OLAPParser#sc}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSc(OLAPParser.ScContext ctx);
	/**
	 * Visit a parse tree produced by the {@code binary}
	 * labeled alternative in {@link OLAPParser#ssc}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBinary(OLAPParser.BinaryContext ctx);
	/**
	 * Visit a parse tree produced by the {@code unary}
	 * labeled alternative in {@link OLAPParser#ssc}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnary(OLAPParser.UnaryContext ctx);
	/**
	 * Visit a parse tree produced by the {@code atom}
	 * labeled alternative in {@link OLAPParser#ssc}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAtom(OLAPParser.AtomContext ctx);
	/**
	 * Visit a parse tree produced by {@link OLAPParser#gc}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGc(OLAPParser.GcContext ctx);
}