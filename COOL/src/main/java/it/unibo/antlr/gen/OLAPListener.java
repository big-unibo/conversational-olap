// Generated from OLAP.g4 by ANTLR 4.10.1

package it.unibo.antlr.gen;

import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link OLAPParser}.
 */
public interface OLAPListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link OLAPParser#operator}.
	 * @param ctx the parse tree
	 */
	void enterOperator(OLAPParser.OperatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link OLAPParser#operator}.
	 * @param ctx the parse tree
	 */
	void exitOperator(OLAPParser.OperatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link OLAPParser#replace}.
	 * @param ctx the parse tree
	 */
	void enterReplace(OLAPParser.ReplaceContext ctx);
	/**
	 * Exit a parse tree produced by {@link OLAPParser#replace}.
	 * @param ctx the parse tree
	 */
	void exitReplace(OLAPParser.ReplaceContext ctx);
	/**
	 * Enter a parse tree produced by the {@code replaceMea}
	 * labeled alternative in {@link OLAPParser#replace_spec}.
	 * @param ctx the parse tree
	 */
	void enterReplaceMea(OLAPParser.ReplaceMeaContext ctx);
	/**
	 * Exit a parse tree produced by the {@code replaceMea}
	 * labeled alternative in {@link OLAPParser#replace_spec}.
	 * @param ctx the parse tree
	 */
	void exitReplaceMea(OLAPParser.ReplaceMeaContext ctx);
	/**
	 * Enter a parse tree produced by the {@code replaceAttr}
	 * labeled alternative in {@link OLAPParser#replace_spec}.
	 * @param ctx the parse tree
	 */
	void enterReplaceAttr(OLAPParser.ReplaceAttrContext ctx);
	/**
	 * Exit a parse tree produced by the {@code replaceAttr}
	 * labeled alternative in {@link OLAPParser#replace_spec}.
	 * @param ctx the parse tree
	 */
	void exitReplaceAttr(OLAPParser.ReplaceAttrContext ctx);
	/**
	 * Enter a parse tree produced by the {@code replaceSSC}
	 * labeled alternative in {@link OLAPParser#replace_spec}.
	 * @param ctx the parse tree
	 */
	void enterReplaceSSC(OLAPParser.ReplaceSSCContext ctx);
	/**
	 * Exit a parse tree produced by the {@code replaceSSC}
	 * labeled alternative in {@link OLAPParser#replace_spec}.
	 * @param ctx the parse tree
	 */
	void exitReplaceSSC(OLAPParser.ReplaceSSCContext ctx);
	/**
	 * Enter a parse tree produced by {@link OLAPParser#remove}.
	 * @param ctx the parse tree
	 */
	void enterRemove(OLAPParser.RemoveContext ctx);
	/**
	 * Exit a parse tree produced by {@link OLAPParser#remove}.
	 * @param ctx the parse tree
	 */
	void exitRemove(OLAPParser.RemoveContext ctx);
	/**
	 * Enter a parse tree produced by the {@code removeMea}
	 * labeled alternative in {@link OLAPParser#remove_spec}.
	 * @param ctx the parse tree
	 */
	void enterRemoveMea(OLAPParser.RemoveMeaContext ctx);
	/**
	 * Exit a parse tree produced by the {@code removeMea}
	 * labeled alternative in {@link OLAPParser#remove_spec}.
	 * @param ctx the parse tree
	 */
	void exitRemoveMea(OLAPParser.RemoveMeaContext ctx);
	/**
	 * Enter a parse tree produced by the {@code removeAttr}
	 * labeled alternative in {@link OLAPParser#remove_spec}.
	 * @param ctx the parse tree
	 */
	void enterRemoveAttr(OLAPParser.RemoveAttrContext ctx);
	/**
	 * Exit a parse tree produced by the {@code removeAttr}
	 * labeled alternative in {@link OLAPParser#remove_spec}.
	 * @param ctx the parse tree
	 */
	void exitRemoveAttr(OLAPParser.RemoveAttrContext ctx);
	/**
	 * Enter a parse tree produced by the {@code removeSSC}
	 * labeled alternative in {@link OLAPParser#remove_spec}.
	 * @param ctx the parse tree
	 */
	void enterRemoveSSC(OLAPParser.RemoveSSCContext ctx);
	/**
	 * Exit a parse tree produced by the {@code removeSSC}
	 * labeled alternative in {@link OLAPParser#remove_spec}.
	 * @param ctx the parse tree
	 */
	void exitRemoveSSC(OLAPParser.RemoveSSCContext ctx);
	/**
	 * Enter a parse tree produced by {@link OLAPParser#add}.
	 * @param ctx the parse tree
	 */
	void enterAdd(OLAPParser.AddContext ctx);
	/**
	 * Exit a parse tree produced by {@link OLAPParser#add}.
	 * @param ctx the parse tree
	 */
	void exitAdd(OLAPParser.AddContext ctx);
	/**
	 * Enter a parse tree produced by the {@code addMea}
	 * labeled alternative in {@link OLAPParser#add_spec}.
	 * @param ctx the parse tree
	 */
	void enterAddMea(OLAPParser.AddMeaContext ctx);
	/**
	 * Exit a parse tree produced by the {@code addMea}
	 * labeled alternative in {@link OLAPParser#add_spec}.
	 * @param ctx the parse tree
	 */
	void exitAddMea(OLAPParser.AddMeaContext ctx);
	/**
	 * Enter a parse tree produced by the {@code addAttr}
	 * labeled alternative in {@link OLAPParser#add_spec}.
	 * @param ctx the parse tree
	 */
	void enterAddAttr(OLAPParser.AddAttrContext ctx);
	/**
	 * Exit a parse tree produced by the {@code addAttr}
	 * labeled alternative in {@link OLAPParser#add_spec}.
	 * @param ctx the parse tree
	 */
	void exitAddAttr(OLAPParser.AddAttrContext ctx);
	/**
	 * Enter a parse tree produced by the {@code addSSC}
	 * labeled alternative in {@link OLAPParser#add_spec}.
	 * @param ctx the parse tree
	 */
	void enterAddSSC(OLAPParser.AddSSCContext ctx);
	/**
	 * Exit a parse tree produced by the {@code addSSC}
	 * labeled alternative in {@link OLAPParser#add_spec}.
	 * @param ctx the parse tree
	 */
	void exitAddSSC(OLAPParser.AddSSCContext ctx);
	/**
	 * Enter a parse tree produced by {@link OLAPParser#sad}.
	 * @param ctx the parse tree
	 */
	void enterSad(OLAPParser.SadContext ctx);
	/**
	 * Exit a parse tree produced by {@link OLAPParser#sad}.
	 * @param ctx the parse tree
	 */
	void exitSad(OLAPParser.SadContext ctx);
	/**
	 * Enter a parse tree produced by {@link OLAPParser#rollup}.
	 * @param ctx the parse tree
	 */
	void enterRollup(OLAPParser.RollupContext ctx);
	/**
	 * Exit a parse tree produced by {@link OLAPParser#rollup}.
	 * @param ctx the parse tree
	 */
	void exitRollup(OLAPParser.RollupContext ctx);
	/**
	 * Enter a parse tree produced by {@link OLAPParser#drill}.
	 * @param ctx the parse tree
	 */
	void enterDrill(OLAPParser.DrillContext ctx);
	/**
	 * Exit a parse tree produced by {@link OLAPParser#drill}.
	 * @param ctx the parse tree
	 */
	void exitDrill(OLAPParser.DrillContext ctx);
	/**
	 * Enter a parse tree produced by the {@code full}
	 * labeled alternative in {@link OLAPParser#init}.
	 * @param ctx the parse tree
	 */
	void enterFull(OLAPParser.FullContext ctx);
	/**
	 * Exit a parse tree produced by the {@code full}
	 * labeled alternative in {@link OLAPParser#init}.
	 * @param ctx the parse tree
	 */
	void exitFull(OLAPParser.FullContext ctx);
	/**
	 * Enter a parse tree produced by the {@code partial}
	 * labeled alternative in {@link OLAPParser#init}.
	 * @param ctx the parse tree
	 */
	void enterPartial(OLAPParser.PartialContext ctx);
	/**
	 * Exit a parse tree produced by the {@code partial}
	 * labeled alternative in {@link OLAPParser#init}.
	 * @param ctx the parse tree
	 */
	void exitPartial(OLAPParser.PartialContext ctx);
	/**
	 * Enter a parse tree produced by {@link OLAPParser#unparsed}.
	 * @param ctx the parse tree
	 */
	void enterUnparsed(OLAPParser.UnparsedContext ctx);
	/**
	 * Exit a parse tree produced by {@link OLAPParser#unparsed}.
	 * @param ctx the parse tree
	 */
	void exitUnparsed(OLAPParser.UnparsedContext ctx);
	/**
	 * Enter a parse tree produced by {@link OLAPParser#gpsj}.
	 * @param ctx the parse tree
	 */
	void enterGpsj(OLAPParser.GpsjContext ctx);
	/**
	 * Exit a parse tree produced by {@link OLAPParser#gpsj}.
	 * @param ctx the parse tree
	 */
	void exitGpsj(OLAPParser.GpsjContext ctx);
	/**
	 * Enter a parse tree produced by the {@code mea}
	 * labeled alternative in {@link OLAPParser#mc}.
	 * @param ctx the parse tree
	 */
	void enterMea(OLAPParser.MeaContext ctx);
	/**
	 * Exit a parse tree produced by the {@code mea}
	 * labeled alternative in {@link OLAPParser#mc}.
	 * @param ctx the parse tree
	 */
	void exitMea(OLAPParser.MeaContext ctx);
	/**
	 * Enter a parse tree produced by the {@code fact}
	 * labeled alternative in {@link OLAPParser#mc}.
	 * @param ctx the parse tree
	 */
	void enterFact(OLAPParser.FactContext ctx);
	/**
	 * Exit a parse tree produced by the {@code fact}
	 * labeled alternative in {@link OLAPParser#mc}.
	 * @param ctx the parse tree
	 */
	void exitFact(OLAPParser.FactContext ctx);
	/**
	 * Enter a parse tree produced by {@link OLAPParser#sc}.
	 * @param ctx the parse tree
	 */
	void enterSc(OLAPParser.ScContext ctx);
	/**
	 * Exit a parse tree produced by {@link OLAPParser#sc}.
	 * @param ctx the parse tree
	 */
	void exitSc(OLAPParser.ScContext ctx);
	/**
	 * Enter a parse tree produced by the {@code binary}
	 * labeled alternative in {@link OLAPParser#ssc}.
	 * @param ctx the parse tree
	 */
	void enterBinary(OLAPParser.BinaryContext ctx);
	/**
	 * Exit a parse tree produced by the {@code binary}
	 * labeled alternative in {@link OLAPParser#ssc}.
	 * @param ctx the parse tree
	 */
	void exitBinary(OLAPParser.BinaryContext ctx);
	/**
	 * Enter a parse tree produced by the {@code unary}
	 * labeled alternative in {@link OLAPParser#ssc}.
	 * @param ctx the parse tree
	 */
	void enterUnary(OLAPParser.UnaryContext ctx);
	/**
	 * Exit a parse tree produced by the {@code unary}
	 * labeled alternative in {@link OLAPParser#ssc}.
	 * @param ctx the parse tree
	 */
	void exitUnary(OLAPParser.UnaryContext ctx);
	/**
	 * Enter a parse tree produced by the {@code atom}
	 * labeled alternative in {@link OLAPParser#ssc}.
	 * @param ctx the parse tree
	 */
	void enterAtom(OLAPParser.AtomContext ctx);
	/**
	 * Exit a parse tree produced by the {@code atom}
	 * labeled alternative in {@link OLAPParser#ssc}.
	 * @param ctx the parse tree
	 */
	void exitAtom(OLAPParser.AtomContext ctx);
	/**
	 * Enter a parse tree produced by {@link OLAPParser#gc}.
	 * @param ctx the parse tree
	 */
	void enterGc(OLAPParser.GcContext ctx);
	/**
	 * Exit a parse tree produced by {@link OLAPParser#gc}.
	 * @param ctx the parse tree
	 */
	void exitGc(OLAPParser.GcContext ctx);
}