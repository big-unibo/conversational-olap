// Generated from OLAP.g4 by ANTLR 4.5

package it.unibo.antlr.gen;

import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class OLAPParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.5", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, AGG=11, MEA=12, FACT=13, ATTR=14, VAL=15, BINARY=16, NOT=17, 
		COP=18, WS=19, ERRCHAR=20;
	public static final int
		RULE_operator = 0, RULE_replace = 1, RULE_replace_spec = 2, RULE_remove = 3, 
		RULE_remove_spec = 4, RULE_add = 5, RULE_add_spec = 6, RULE_sad = 7, RULE_rollup = 8, 
		RULE_drill = 9, RULE_init = 10, RULE_unparsed = 11, RULE_gpsj = 12, RULE_mc = 13, 
		RULE_sc = 14, RULE_ssc = 15, RULE_gc = 16;
	public static final String[] ruleNames = {
		"operator", "replace", "replace_spec", "remove", "remove_spec", "add", 
		"add_spec", "sad", "rollup", "drill", "init", "unparsed", "gpsj", "mc", 
		"sc", "ssc", "gc"
	};

	private static final String[] _LITERAL_NAMES = {
		null, "'REPLACE'", "'ACCESSORY'", "'DROP'", "'ADD'", "'SAD'", "'ROLLUP'", 
		"'DRILL'", "'COUNT'", "'WHERE'", "'BY'", "'AGG'", "'MEA'", "'FACT'", "'ATTR'", 
		"'VAL'", null, "'NOT'", "'COP'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, null, null, null, null, null, null, null, null, null, null, "AGG", 
		"MEA", "FACT", "ATTR", "VAL", "BINARY", "NOT", "COP", "WS", "ERRCHAR"
	};
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "OLAP.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public OLAPParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class OperatorContext extends ParserRuleContext {
		public DrillContext drill() {
			return getRuleContext(DrillContext.class,0);
		}
		public RollupContext rollup() {
			return getRuleContext(RollupContext.class,0);
		}
		public SadContext sad() {
			return getRuleContext(SadContext.class,0);
		}
		public AddContext add() {
			return getRuleContext(AddContext.class,0);
		}
		public RemoveContext remove() {
			return getRuleContext(RemoveContext.class,0);
		}
		public ReplaceContext replace() {
			return getRuleContext(ReplaceContext.class,0);
		}
		public OperatorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_operator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OLAPListener ) ((OLAPListener)listener).enterOperator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OLAPListener ) ((OLAPListener)listener).exitOperator(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof OLAPVisitor ) return ((OLAPVisitor<? extends T>)visitor).visitOperator(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OperatorContext operator() throws RecognitionException {
		OperatorContext _localctx = new OperatorContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_operator);
		try {
			setState(40);
			switch (_input.LA(1)) {
			case T__6:
				enterOuterAlt(_localctx, 1);
				{
				setState(34);
				drill();
				}
				break;
			case T__5:
				enterOuterAlt(_localctx, 2);
				{
				setState(35);
				rollup();
				}
				break;
			case T__4:
				enterOuterAlt(_localctx, 3);
				{
				setState(36);
				sad();
				}
				break;
			case T__3:
				enterOuterAlt(_localctx, 4);
				{
				setState(37);
				add();
				}
				break;
			case T__2:
				enterOuterAlt(_localctx, 5);
				{
				setState(38);
				remove();
				}
				break;
			case T__0:
				enterOuterAlt(_localctx, 6);
				{
				setState(39);
				replace();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ReplaceContext extends ParserRuleContext {
		public Replace_specContext replace_spec() {
			return getRuleContext(Replace_specContext.class,0);
		}
		public ReplaceContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_replace; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OLAPListener ) ((OLAPListener)listener).enterReplace(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OLAPListener ) ((OLAPListener)listener).exitReplace(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof OLAPVisitor ) return ((OLAPVisitor<? extends T>)visitor).visitReplace(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ReplaceContext replace() throws RecognitionException {
		ReplaceContext _localctx = new ReplaceContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_replace);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(42);
			match(T__0);
			setState(43);
			replace_spec();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Replace_specContext extends ParserRuleContext {
		public Replace_specContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_replace_spec; }
	 
		public Replace_specContext() { }
		public void copyFrom(Replace_specContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class ReplaceMeaContext extends Replace_specContext {
		public McContext fromMea;
		public McContext toMea;
		public List<McContext> mc() {
			return getRuleContexts(McContext.class);
		}
		public McContext mc(int i) {
			return getRuleContext(McContext.class,i);
		}
		public ReplaceMeaContext(Replace_specContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OLAPListener ) ((OLAPListener)listener).enterReplaceMea(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OLAPListener ) ((OLAPListener)listener).exitReplaceMea(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof OLAPVisitor ) return ((OLAPVisitor<? extends T>)visitor).visitReplaceMea(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ReplaceAttrContext extends Replace_specContext {
		public Token fromAtt;
		public Token toAtt;
		public List<TerminalNode> ATTR() { return getTokens(OLAPParser.ATTR); }
		public TerminalNode ATTR(int i) {
			return getToken(OLAPParser.ATTR, i);
		}
		public ReplaceAttrContext(Replace_specContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OLAPListener ) ((OLAPListener)listener).enterReplaceAttr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OLAPListener ) ((OLAPListener)listener).exitReplaceAttr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof OLAPVisitor ) return ((OLAPVisitor<? extends T>)visitor).visitReplaceAttr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ReplaceSSCContext extends Replace_specContext {
		public SscContext fromSel;
		public SscContext toSel;
		public List<SscContext> ssc() {
			return getRuleContexts(SscContext.class);
		}
		public SscContext ssc(int i) {
			return getRuleContext(SscContext.class,i);
		}
		public ReplaceSSCContext(Replace_specContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OLAPListener ) ((OLAPListener)listener).enterReplaceSSC(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OLAPListener ) ((OLAPListener)listener).exitReplaceSSC(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof OLAPVisitor ) return ((OLAPVisitor<? extends T>)visitor).visitReplaceSSC(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Replace_specContext replace_spec() throws RecognitionException {
		Replace_specContext _localctx = new Replace_specContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_replace_spec);
		try {
			setState(56);
			switch ( getInterpreter().adaptivePredict(_input,1,_ctx) ) {
			case 1:
				_localctx = new ReplaceMeaContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(45);
				((ReplaceMeaContext)_localctx).fromMea = mc();
				setState(46);
				match(T__1);
				setState(47);
				((ReplaceMeaContext)_localctx).toMea = mc();
				}
				break;
			case 2:
				_localctx = new ReplaceAttrContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(49);
				((ReplaceAttrContext)_localctx).fromAtt = match(ATTR);
				setState(50);
				match(T__1);
				setState(51);
				((ReplaceAttrContext)_localctx).toAtt = match(ATTR);
				}
				break;
			case 3:
				_localctx = new ReplaceSSCContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(52);
				((ReplaceSSCContext)_localctx).fromSel = ssc(0);
				setState(53);
				match(T__1);
				setState(54);
				((ReplaceSSCContext)_localctx).toSel = ssc(0);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class RemoveContext extends ParserRuleContext {
		public Remove_specContext remove_spec() {
			return getRuleContext(Remove_specContext.class,0);
		}
		public RemoveContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_remove; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OLAPListener ) ((OLAPListener)listener).enterRemove(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OLAPListener ) ((OLAPListener)listener).exitRemove(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof OLAPVisitor ) return ((OLAPVisitor<? extends T>)visitor).visitRemove(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RemoveContext remove() throws RecognitionException {
		RemoveContext _localctx = new RemoveContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_remove);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(58);
			match(T__2);
			setState(59);
			remove_spec();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Remove_specContext extends ParserRuleContext {
		public Remove_specContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_remove_spec; }
	 
		public Remove_specContext() { }
		public void copyFrom(Remove_specContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class RemoveSSCContext extends Remove_specContext {
		public SscContext sel;
		public SscContext ssc() {
			return getRuleContext(SscContext.class,0);
		}
		public RemoveSSCContext(Remove_specContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OLAPListener ) ((OLAPListener)listener).enterRemoveSSC(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OLAPListener ) ((OLAPListener)listener).exitRemoveSSC(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof OLAPVisitor ) return ((OLAPVisitor<? extends T>)visitor).visitRemoveSSC(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class RemoveAttrContext extends Remove_specContext {
		public Token att;
		public TerminalNode ATTR() { return getToken(OLAPParser.ATTR, 0); }
		public RemoveAttrContext(Remove_specContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OLAPListener ) ((OLAPListener)listener).enterRemoveAttr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OLAPListener ) ((OLAPListener)listener).exitRemoveAttr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof OLAPVisitor ) return ((OLAPVisitor<? extends T>)visitor).visitRemoveAttr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class RemoveMeaContext extends Remove_specContext {
		public McContext mea;
		public McContext mc() {
			return getRuleContext(McContext.class,0);
		}
		public RemoveMeaContext(Remove_specContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OLAPListener ) ((OLAPListener)listener).enterRemoveMea(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OLAPListener ) ((OLAPListener)listener).exitRemoveMea(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof OLAPVisitor ) return ((OLAPVisitor<? extends T>)visitor).visitRemoveMea(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Remove_specContext remove_spec() throws RecognitionException {
		Remove_specContext _localctx = new Remove_specContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_remove_spec);
		try {
			setState(64);
			switch ( getInterpreter().adaptivePredict(_input,2,_ctx) ) {
			case 1:
				_localctx = new RemoveMeaContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(61);
				((RemoveMeaContext)_localctx).mea = mc();
				}
				break;
			case 2:
				_localctx = new RemoveAttrContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(62);
				((RemoveAttrContext)_localctx).att = match(ATTR);
				}
				break;
			case 3:
				_localctx = new RemoveSSCContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(63);
				((RemoveSSCContext)_localctx).sel = ssc(0);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AddContext extends ParserRuleContext {
		public Add_specContext add_spec() {
			return getRuleContext(Add_specContext.class,0);
		}
		public AddContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_add; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OLAPListener ) ((OLAPListener)listener).enterAdd(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OLAPListener ) ((OLAPListener)listener).exitAdd(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof OLAPVisitor ) return ((OLAPVisitor<? extends T>)visitor).visitAdd(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AddContext add() throws RecognitionException {
		AddContext _localctx = new AddContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_add);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(66);
			match(T__3);
			setState(67);
			add_spec();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Add_specContext extends ParserRuleContext {
		public Add_specContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_add_spec; }
	 
		public Add_specContext() { }
		public void copyFrom(Add_specContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class AddAttrContext extends Add_specContext {
		public Token att;
		public TerminalNode ATTR() { return getToken(OLAPParser.ATTR, 0); }
		public AddAttrContext(Add_specContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OLAPListener ) ((OLAPListener)listener).enterAddAttr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OLAPListener ) ((OLAPListener)listener).exitAddAttr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof OLAPVisitor ) return ((OLAPVisitor<? extends T>)visitor).visitAddAttr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class AddSSCContext extends Add_specContext {
		public SscContext sel;
		public SscContext ssc() {
			return getRuleContext(SscContext.class,0);
		}
		public AddSSCContext(Add_specContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OLAPListener ) ((OLAPListener)listener).enterAddSSC(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OLAPListener ) ((OLAPListener)listener).exitAddSSC(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof OLAPVisitor ) return ((OLAPVisitor<? extends T>)visitor).visitAddSSC(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class AddMeaContext extends Add_specContext {
		public McContext mea;
		public McContext mc() {
			return getRuleContext(McContext.class,0);
		}
		public AddMeaContext(Add_specContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OLAPListener ) ((OLAPListener)listener).enterAddMea(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OLAPListener ) ((OLAPListener)listener).exitAddMea(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof OLAPVisitor ) return ((OLAPVisitor<? extends T>)visitor).visitAddMea(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Add_specContext add_spec() throws RecognitionException {
		Add_specContext _localctx = new Add_specContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_add_spec);
		try {
			setState(72);
			switch ( getInterpreter().adaptivePredict(_input,3,_ctx) ) {
			case 1:
				_localctx = new AddMeaContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(69);
				((AddMeaContext)_localctx).mea = mc();
				}
				break;
			case 2:
				_localctx = new AddAttrContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(70);
				((AddAttrContext)_localctx).att = match(ATTR);
				}
				break;
			case 3:
				_localctx = new AddSSCContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(71);
				((AddSSCContext)_localctx).sel = ssc(0);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SadContext extends ParserRuleContext {
		public SscContext sel;
		public SscContext ssc() {
			return getRuleContext(SscContext.class,0);
		}
		public SadContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_sad; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OLAPListener ) ((OLAPListener)listener).enterSad(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OLAPListener ) ((OLAPListener)listener).exitSad(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof OLAPVisitor ) return ((OLAPVisitor<? extends T>)visitor).visitSad(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SadContext sad() throws RecognitionException {
		SadContext _localctx = new SadContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_sad);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(74);
			match(T__4);
			setState(75);
			((SadContext)_localctx).sel = ssc(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class RollupContext extends ParserRuleContext {
		public Token fromAtt;
		public Token toAtt;
		public List<TerminalNode> ATTR() { return getTokens(OLAPParser.ATTR); }
		public TerminalNode ATTR(int i) {
			return getToken(OLAPParser.ATTR, i);
		}
		public RollupContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_rollup; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OLAPListener ) ((OLAPListener)listener).enterRollup(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OLAPListener ) ((OLAPListener)listener).exitRollup(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof OLAPVisitor ) return ((OLAPVisitor<? extends T>)visitor).visitRollup(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RollupContext rollup() throws RecognitionException {
		RollupContext _localctx = new RollupContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_rollup);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(77);
			match(T__5);
			setState(78);
			((RollupContext)_localctx).fromAtt = match(ATTR);
			setState(81);
			_la = _input.LA(1);
			if (_la==T__1) {
				{
				setState(79);
				match(T__1);
				setState(80);
				((RollupContext)_localctx).toAtt = match(ATTR);
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class DrillContext extends ParserRuleContext {
		public Token fromAtt;
		public Token toAtt;
		public List<TerminalNode> ATTR() { return getTokens(OLAPParser.ATTR); }
		public TerminalNode ATTR(int i) {
			return getToken(OLAPParser.ATTR, i);
		}
		public DrillContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_drill; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OLAPListener ) ((OLAPListener)listener).enterDrill(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OLAPListener ) ((OLAPListener)listener).exitDrill(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof OLAPVisitor ) return ((OLAPVisitor<? extends T>)visitor).visitDrill(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DrillContext drill() throws RecognitionException {
		DrillContext _localctx = new DrillContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_drill);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(83);
			match(T__6);
			setState(84);
			((DrillContext)_localctx).fromAtt = match(ATTR);
			setState(87);
			_la = _input.LA(1);
			if (_la==T__1) {
				{
				setState(85);
				match(T__1);
				setState(86);
				((DrillContext)_localctx).toAtt = match(ATTR);
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class InitContext extends ParserRuleContext {
		public InitContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_init; }
	 
		public InitContext() { }
		public void copyFrom(InitContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class PartialContext extends InitContext {
		public GpsjContext GPSJ;
		public UnparsedContext unparsed;
		public List<UnparsedContext> UP = new ArrayList<UnparsedContext>();
		public GpsjContext gpsj() {
			return getRuleContext(GpsjContext.class,0);
		}
		public List<UnparsedContext> unparsed() {
			return getRuleContexts(UnparsedContext.class);
		}
		public UnparsedContext unparsed(int i) {
			return getRuleContext(UnparsedContext.class,i);
		}
		public PartialContext(InitContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OLAPListener ) ((OLAPListener)listener).enterPartial(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OLAPListener ) ((OLAPListener)listener).exitPartial(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof OLAPVisitor ) return ((OLAPVisitor<? extends T>)visitor).visitPartial(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class FullContext extends InitContext {
		public GpsjContext GPSJ;
		public GpsjContext gpsj() {
			return getRuleContext(GpsjContext.class,0);
		}
		public FullContext(InitContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OLAPListener ) ((OLAPListener)listener).enterFull(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OLAPListener ) ((OLAPListener)listener).exitFull(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof OLAPVisitor ) return ((OLAPVisitor<? extends T>)visitor).visitFull(this);
			else return visitor.visitChildren(this);
		}
	}

	public final InitContext init() throws RecognitionException {
		InitContext _localctx = new InitContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_init);
		int _la;
		try {
			setState(96);
			switch ( getInterpreter().adaptivePredict(_input,7,_ctx) ) {
			case 1:
				_localctx = new FullContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(89);
				((FullContext)_localctx).GPSJ = gpsj();
				}
				break;
			case 2:
				_localctx = new PartialContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(90);
				((PartialContext)_localctx).GPSJ = gpsj();
				setState(92); 
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					setState(91);
					((PartialContext)_localctx).unparsed = unparsed();
					((PartialContext)_localctx).UP.add(((PartialContext)_localctx).unparsed);
					}
					}
					setState(94); 
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__7) | (1L << T__8) | (1L << T__9) | (1L << AGG) | (1L << MEA) | (1L << ATTR) | (1L << VAL) | (1L << NOT))) != 0) );
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class UnparsedContext extends ParserRuleContext {
		public SscContext ssc() {
			return getRuleContext(SscContext.class,0);
		}
		public GcContext gc() {
			return getRuleContext(GcContext.class,0);
		}
		public GpsjContext gpsj() {
			return getRuleContext(GpsjContext.class,0);
		}
		public UnparsedContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_unparsed; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OLAPListener ) ((OLAPListener)listener).enterUnparsed(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OLAPListener ) ((OLAPListener)listener).exitUnparsed(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof OLAPVisitor ) return ((OLAPVisitor<? extends T>)visitor).visitUnparsed(this);
			else return visitor.visitChildren(this);
		}
	}

	public final UnparsedContext unparsed() throws RecognitionException {
		UnparsedContext _localctx = new UnparsedContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_unparsed);
		try {
			setState(101);
			switch ( getInterpreter().adaptivePredict(_input,8,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(98);
				ssc(0);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(99);
				gc();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(100);
				gpsj();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class GpsjContext extends ParserRuleContext {
		public McContext mc;
		public List<McContext> MC = new ArrayList<McContext>();
		public ScContext SC;
		public GcContext GC;
		public ScContext sc() {
			return getRuleContext(ScContext.class,0);
		}
		public GcContext gc() {
			return getRuleContext(GcContext.class,0);
		}
		public List<McContext> mc() {
			return getRuleContexts(McContext.class);
		}
		public McContext mc(int i) {
			return getRuleContext(McContext.class,i);
		}
		public GpsjContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_gpsj; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OLAPListener ) ((OLAPListener)listener).enterGpsj(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OLAPListener ) ((OLAPListener)listener).exitGpsj(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof OLAPVisitor ) return ((OLAPVisitor<? extends T>)visitor).visitGpsj(this);
			else return visitor.visitChildren(this);
		}
	}

	public final GpsjContext gpsj() throws RecognitionException {
		GpsjContext _localctx = new GpsjContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_gpsj);
		int _la;
		try {
			int _alt;
			setState(180);
			switch ( getInterpreter().adaptivePredict(_input,20,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(104); 
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					setState(103);
					((GpsjContext)_localctx).mc = mc();
					((GpsjContext)_localctx).MC.add(((GpsjContext)_localctx).mc);
					}
					}
					setState(106); 
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__7) | (1L << AGG) | (1L << MEA))) != 0) );
				setState(108);
				((GpsjContext)_localctx).SC = sc();
				setState(109);
				((GpsjContext)_localctx).GC = gc();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(112); 
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					setState(111);
					((GpsjContext)_localctx).mc = mc();
					((GpsjContext)_localctx).MC.add(((GpsjContext)_localctx).mc);
					}
					}
					setState(114); 
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__7) | (1L << AGG) | (1L << MEA))) != 0) );
				setState(116);
				((GpsjContext)_localctx).GC = gc();
				setState(117);
				((GpsjContext)_localctx).SC = sc();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(119);
				((GpsjContext)_localctx).SC = sc();
				setState(121); 
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					setState(120);
					((GpsjContext)_localctx).mc = mc();
					((GpsjContext)_localctx).MC.add(((GpsjContext)_localctx).mc);
					}
					}
					setState(123); 
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__7) | (1L << AGG) | (1L << MEA))) != 0) );
				setState(125);
				((GpsjContext)_localctx).GC = gc();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(127);
				((GpsjContext)_localctx).SC = sc();
				setState(128);
				((GpsjContext)_localctx).GC = gc();
				setState(130); 
				_errHandler.sync(this);
				_alt = 1;
				do {
					switch (_alt) {
					case 1:
						{
						{
						setState(129);
						((GpsjContext)_localctx).mc = mc();
						((GpsjContext)_localctx).MC.add(((GpsjContext)_localctx).mc);
						}
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					setState(132); 
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,12,_ctx);
				} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(134);
				((GpsjContext)_localctx).GC = gc();
				setState(136); 
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					setState(135);
					((GpsjContext)_localctx).mc = mc();
					((GpsjContext)_localctx).MC.add(((GpsjContext)_localctx).mc);
					}
					}
					setState(138); 
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__7) | (1L << AGG) | (1L << MEA))) != 0) );
				setState(140);
				((GpsjContext)_localctx).SC = sc();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(142);
				((GpsjContext)_localctx).GC = gc();
				setState(143);
				((GpsjContext)_localctx).SC = sc();
				setState(145); 
				_errHandler.sync(this);
				_alt = 1;
				do {
					switch (_alt) {
					case 1:
						{
						{
						setState(144);
						((GpsjContext)_localctx).mc = mc();
						((GpsjContext)_localctx).MC.add(((GpsjContext)_localctx).mc);
						}
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					setState(147); 
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,14,_ctx);
				} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(150); 
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					setState(149);
					((GpsjContext)_localctx).mc = mc();
					((GpsjContext)_localctx).MC.add(((GpsjContext)_localctx).mc);
					}
					}
					setState(152); 
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__7) | (1L << AGG) | (1L << MEA))) != 0) );
				setState(154);
				((GpsjContext)_localctx).GC = gc();
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(156);
				((GpsjContext)_localctx).GC = gc();
				setState(158); 
				_errHandler.sync(this);
				_alt = 1;
				do {
					switch (_alt) {
					case 1:
						{
						{
						setState(157);
						((GpsjContext)_localctx).mc = mc();
						((GpsjContext)_localctx).MC.add(((GpsjContext)_localctx).mc);
						}
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					setState(160); 
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,16,_ctx);
				} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(163); 
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					setState(162);
					((GpsjContext)_localctx).mc = mc();
					((GpsjContext)_localctx).MC.add(((GpsjContext)_localctx).mc);
					}
					}
					setState(165); 
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__7) | (1L << AGG) | (1L << MEA))) != 0) );
				setState(167);
				((GpsjContext)_localctx).SC = sc();
				}
				break;
			case 10:
				enterOuterAlt(_localctx, 10);
				{
				setState(169);
				((GpsjContext)_localctx).SC = sc();
				setState(171); 
				_errHandler.sync(this);
				_alt = 1;
				do {
					switch (_alt) {
					case 1:
						{
						{
						setState(170);
						((GpsjContext)_localctx).mc = mc();
						((GpsjContext)_localctx).MC.add(((GpsjContext)_localctx).mc);
						}
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					setState(173); 
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,18,_ctx);
				} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
				}
				break;
			case 11:
				enterOuterAlt(_localctx, 11);
				{
				setState(176); 
				_errHandler.sync(this);
				_alt = 1;
				do {
					switch (_alt) {
					case 1:
						{
						{
						setState(175);
						((GpsjContext)_localctx).mc = mc();
						((GpsjContext)_localctx).MC.add(((GpsjContext)_localctx).mc);
						}
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					setState(178); 
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,19,_ctx);
				} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class McContext extends ParserRuleContext {
		public McContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_mc; }
	 
		public McContext() { }
		public void copyFrom(McContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class FactContext extends McContext {
		public Token cnt;
		public Token fact;
		public TerminalNode FACT() { return getToken(OLAPParser.FACT, 0); }
		public FactContext(McContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OLAPListener ) ((OLAPListener)listener).enterFact(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OLAPListener ) ((OLAPListener)listener).exitFact(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof OLAPVisitor ) return ((OLAPVisitor<? extends T>)visitor).visitFact(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class MeaContext extends McContext {
		public Token agg;
		public Token mea;
		public TerminalNode MEA() { return getToken(OLAPParser.MEA, 0); }
		public TerminalNode AGG() { return getToken(OLAPParser.AGG, 0); }
		public MeaContext(McContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OLAPListener ) ((OLAPListener)listener).enterMea(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OLAPListener ) ((OLAPListener)listener).exitMea(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof OLAPVisitor ) return ((OLAPVisitor<? extends T>)visitor).visitMea(this);
			else return visitor.visitChildren(this);
		}
	}

	public final McContext mc() throws RecognitionException {
		McContext _localctx = new McContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_mc);
		int _la;
		try {
			setState(188);
			switch (_input.LA(1)) {
			case AGG:
			case MEA:
				_localctx = new MeaContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(183);
				_la = _input.LA(1);
				if (_la==AGG) {
					{
					setState(182);
					((MeaContext)_localctx).agg = match(AGG);
					}
				}

				setState(185);
				((MeaContext)_localctx).mea = match(MEA);
				}
				break;
			case T__7:
				_localctx = new FactContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(186);
				((FactContext)_localctx).cnt = match(T__7);
				setState(187);
				((FactContext)_localctx).fact = match(FACT);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ScContext extends ParserRuleContext {
		public Token where;
		public SscContext SSC;
		public SscContext ssc() {
			return getRuleContext(SscContext.class,0);
		}
		public ScContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_sc; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OLAPListener ) ((OLAPListener)listener).enterSc(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OLAPListener ) ((OLAPListener)listener).exitSc(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof OLAPVisitor ) return ((OLAPVisitor<? extends T>)visitor).visitSc(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ScContext sc() throws RecognitionException {
		ScContext _localctx = new ScContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_sc);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(191);
			_la = _input.LA(1);
			if (_la==T__8) {
				{
				setState(190);
				((ScContext)_localctx).where = match(T__8);
				}
			}

			setState(193);
			((ScContext)_localctx).SSC = ssc(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SscContext extends ParserRuleContext {
		public SscContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ssc; }
	 
		public SscContext() { }
		public void copyFrom(SscContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class BinaryContext extends SscContext {
		public SscContext left;
		public Token binary;
		public SscContext right;
		public List<SscContext> ssc() {
			return getRuleContexts(SscContext.class);
		}
		public SscContext ssc(int i) {
			return getRuleContext(SscContext.class,i);
		}
		public TerminalNode BINARY() { return getToken(OLAPParser.BINARY, 0); }
		public BinaryContext(SscContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OLAPListener ) ((OLAPListener)listener).enterBinary(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OLAPListener ) ((OLAPListener)listener).exitBinary(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof OLAPVisitor ) return ((OLAPVisitor<? extends T>)visitor).visitBinary(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class UnaryContext extends SscContext {
		public Token unary;
		public SscContext left;
		public TerminalNode NOT() { return getToken(OLAPParser.NOT, 0); }
		public SscContext ssc() {
			return getRuleContext(SscContext.class,0);
		}
		public UnaryContext(SscContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OLAPListener ) ((OLAPListener)listener).enterUnary(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OLAPListener ) ((OLAPListener)listener).exitUnary(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof OLAPVisitor ) return ((OLAPVisitor<? extends T>)visitor).visitUnary(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class AtomContext extends SscContext {
		public Token attr1;
		public Token cop;
		public Token val;
		public TerminalNode ATTR() { return getToken(OLAPParser.ATTR, 0); }
		public TerminalNode VAL() { return getToken(OLAPParser.VAL, 0); }
		public TerminalNode COP() { return getToken(OLAPParser.COP, 0); }
		public AtomContext(SscContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OLAPListener ) ((OLAPListener)listener).enterAtom(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OLAPListener ) ((OLAPListener)listener).exitAtom(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof OLAPVisitor ) return ((OLAPVisitor<? extends T>)visitor).visitAtom(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SscContext ssc() throws RecognitionException {
		return ssc(0);
	}

	private SscContext ssc(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		SscContext _localctx = new SscContext(_ctx, _parentState);
		SscContext _prevctx = _localctx;
		int _startState = 30;
		enterRecursionRule(_localctx, 30, RULE_ssc, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(211);
			switch (_input.LA(1)) {
			case NOT:
				{
				_localctx = new UnaryContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(196);
				((UnaryContext)_localctx).unary = match(NOT);
				setState(197);
				((UnaryContext)_localctx).left = ssc(3);
				}
				break;
			case ATTR:
			case VAL:
				{
				_localctx = new AtomContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(209);
				switch ( getInterpreter().adaptivePredict(_input,26,_ctx) ) {
				case 1:
					{
					setState(198);
					((AtomContext)_localctx).attr1 = match(ATTR);
					setState(200);
					_la = _input.LA(1);
					if (_la==COP) {
						{
						setState(199);
						((AtomContext)_localctx).cop = match(COP);
						}
					}

					setState(202);
					((AtomContext)_localctx).val = match(VAL);
					}
					break;
				case 2:
					{
					setState(203);
					((AtomContext)_localctx).val = match(VAL);
					setState(205);
					_la = _input.LA(1);
					if (_la==COP) {
						{
						setState(204);
						((AtomContext)_localctx).cop = match(COP);
						}
					}

					setState(207);
					((AtomContext)_localctx).attr1 = match(ATTR);
					}
					break;
				case 3:
					{
					setState(208);
					((AtomContext)_localctx).val = match(VAL);
					}
					break;
				}
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			_ctx.stop = _input.LT(-1);
			setState(218);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,28,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					{
					_localctx = new BinaryContext(new SscContext(_parentctx, _parentState));
					((BinaryContext)_localctx).left = _prevctx;
					pushNewRecursionContext(_localctx, _startState, RULE_ssc);
					setState(213);
					if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
					setState(214);
					((BinaryContext)_localctx).binary = match(BINARY);
					setState(215);
					((BinaryContext)_localctx).right = ssc(3);
					}
					} 
				}
				setState(220);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,28,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public static class GcContext extends ParserRuleContext {
		public Token by;
		public Token ATTR;
		public List<Token> attr = new ArrayList<Token>();
		public List<TerminalNode> ATTR() { return getTokens(OLAPParser.ATTR); }
		public TerminalNode ATTR(int i) {
			return getToken(OLAPParser.ATTR, i);
		}
		public GcContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_gc; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OLAPListener ) ((OLAPListener)listener).enterGc(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OLAPListener ) ((OLAPListener)listener).exitGc(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof OLAPVisitor ) return ((OLAPVisitor<? extends T>)visitor).visitGc(this);
			else return visitor.visitChildren(this);
		}
	}

	public final GcContext gc() throws RecognitionException {
		GcContext _localctx = new GcContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_gc);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(221);
			((GcContext)_localctx).by = match(T__9);
			setState(223); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(222);
					((GcContext)_localctx).ATTR = match(ATTR);
					((GcContext)_localctx).attr.add(((GcContext)_localctx).ATTR);
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(225); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,29,_ctx);
			} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 15:
			return ssc_sempred((SscContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean ssc_sempred(SscContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 2);
		}
		return true;
	}

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3\26\u00e6\4\2\t\2"+
		"\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\3\2\3\2\3\2\3\2\3\2\3\2\5\2+\n\2\3\3\3\3\3\3\3\4\3\4\3\4\3\4\3\4\3\4"+
		"\3\4\3\4\3\4\3\4\3\4\5\4;\n\4\3\5\3\5\3\5\3\6\3\6\3\6\5\6C\n\6\3\7\3\7"+
		"\3\7\3\b\3\b\3\b\5\bK\n\b\3\t\3\t\3\t\3\n\3\n\3\n\3\n\5\nT\n\n\3\13\3"+
		"\13\3\13\3\13\5\13Z\n\13\3\f\3\f\3\f\6\f_\n\f\r\f\16\f`\5\fc\n\f\3\r\3"+
		"\r\3\r\5\rh\n\r\3\16\6\16k\n\16\r\16\16\16l\3\16\3\16\3\16\3\16\6\16s"+
		"\n\16\r\16\16\16t\3\16\3\16\3\16\3\16\3\16\6\16|\n\16\r\16\16\16}\3\16"+
		"\3\16\3\16\3\16\3\16\6\16\u0085\n\16\r\16\16\16\u0086\3\16\3\16\6\16\u008b"+
		"\n\16\r\16\16\16\u008c\3\16\3\16\3\16\3\16\3\16\6\16\u0094\n\16\r\16\16"+
		"\16\u0095\3\16\6\16\u0099\n\16\r\16\16\16\u009a\3\16\3\16\3\16\3\16\6"+
		"\16\u00a1\n\16\r\16\16\16\u00a2\3\16\6\16\u00a6\n\16\r\16\16\16\u00a7"+
		"\3\16\3\16\3\16\3\16\6\16\u00ae\n\16\r\16\16\16\u00af\3\16\6\16\u00b3"+
		"\n\16\r\16\16\16\u00b4\5\16\u00b7\n\16\3\17\5\17\u00ba\n\17\3\17\3\17"+
		"\3\17\5\17\u00bf\n\17\3\20\5\20\u00c2\n\20\3\20\3\20\3\21\3\21\3\21\3"+
		"\21\3\21\5\21\u00cb\n\21\3\21\3\21\3\21\5\21\u00d0\n\21\3\21\3\21\5\21"+
		"\u00d4\n\21\5\21\u00d6\n\21\3\21\3\21\3\21\7\21\u00db\n\21\f\21\16\21"+
		"\u00de\13\21\3\22\3\22\6\22\u00e2\n\22\r\22\16\22\u00e3\3\22\2\3 \23\2"+
		"\4\6\b\n\f\16\20\22\24\26\30\32\34\36 \"\2\2\u0104\2*\3\2\2\2\4,\3\2\2"+
		"\2\6:\3\2\2\2\b<\3\2\2\2\nB\3\2\2\2\fD\3\2\2\2\16J\3\2\2\2\20L\3\2\2\2"+
		"\22O\3\2\2\2\24U\3\2\2\2\26b\3\2\2\2\30g\3\2\2\2\32\u00b6\3\2\2\2\34\u00be"+
		"\3\2\2\2\36\u00c1\3\2\2\2 \u00d5\3\2\2\2\"\u00df\3\2\2\2$+\5\24\13\2%"+
		"+\5\22\n\2&+\5\20\t\2\'+\5\f\7\2(+\5\b\5\2)+\5\4\3\2*$\3\2\2\2*%\3\2\2"+
		"\2*&\3\2\2\2*\'\3\2\2\2*(\3\2\2\2*)\3\2\2\2+\3\3\2\2\2,-\7\3\2\2-.\5\6"+
		"\4\2.\5\3\2\2\2/\60\5\34\17\2\60\61\7\4\2\2\61\62\5\34\17\2\62;\3\2\2"+
		"\2\63\64\7\20\2\2\64\65\7\4\2\2\65;\7\20\2\2\66\67\5 \21\2\678\7\4\2\2"+
		"89\5 \21\29;\3\2\2\2:/\3\2\2\2:\63\3\2\2\2:\66\3\2\2\2;\7\3\2\2\2<=\7"+
		"\5\2\2=>\5\n\6\2>\t\3\2\2\2?C\5\34\17\2@C\7\20\2\2AC\5 \21\2B?\3\2\2\2"+
		"B@\3\2\2\2BA\3\2\2\2C\13\3\2\2\2DE\7\6\2\2EF\5\16\b\2F\r\3\2\2\2GK\5\34"+
		"\17\2HK\7\20\2\2IK\5 \21\2JG\3\2\2\2JH\3\2\2\2JI\3\2\2\2K\17\3\2\2\2L"+
		"M\7\7\2\2MN\5 \21\2N\21\3\2\2\2OP\7\b\2\2PS\7\20\2\2QR\7\4\2\2RT\7\20"+
		"\2\2SQ\3\2\2\2ST\3\2\2\2T\23\3\2\2\2UV\7\t\2\2VY\7\20\2\2WX\7\4\2\2XZ"+
		"\7\20\2\2YW\3\2\2\2YZ\3\2\2\2Z\25\3\2\2\2[c\5\32\16\2\\^\5\32\16\2]_\5"+
		"\30\r\2^]\3\2\2\2_`\3\2\2\2`^\3\2\2\2`a\3\2\2\2ac\3\2\2\2b[\3\2\2\2b\\"+
		"\3\2\2\2c\27\3\2\2\2dh\5 \21\2eh\5\"\22\2fh\5\32\16\2gd\3\2\2\2ge\3\2"+
		"\2\2gf\3\2\2\2h\31\3\2\2\2ik\5\34\17\2ji\3\2\2\2kl\3\2\2\2lj\3\2\2\2l"+
		"m\3\2\2\2mn\3\2\2\2no\5\36\20\2op\5\"\22\2p\u00b7\3\2\2\2qs\5\34\17\2"+
		"rq\3\2\2\2st\3\2\2\2tr\3\2\2\2tu\3\2\2\2uv\3\2\2\2vw\5\"\22\2wx\5\36\20"+
		"\2x\u00b7\3\2\2\2y{\5\36\20\2z|\5\34\17\2{z\3\2\2\2|}\3\2\2\2}{\3\2\2"+
		"\2}~\3\2\2\2~\177\3\2\2\2\177\u0080\5\"\22\2\u0080\u00b7\3\2\2\2\u0081"+
		"\u0082\5\36\20\2\u0082\u0084\5\"\22\2\u0083\u0085\5\34\17\2\u0084\u0083"+
		"\3\2\2\2\u0085\u0086\3\2\2\2\u0086\u0084\3\2\2\2\u0086\u0087\3\2\2\2\u0087"+
		"\u00b7\3\2\2\2\u0088\u008a\5\"\22\2\u0089\u008b\5\34\17\2\u008a\u0089"+
		"\3\2\2\2\u008b\u008c\3\2\2\2\u008c\u008a\3\2\2\2\u008c\u008d\3\2\2\2\u008d"+
		"\u008e\3\2\2\2\u008e\u008f\5\36\20\2\u008f\u00b7\3\2\2\2\u0090\u0091\5"+
		"\"\22\2\u0091\u0093\5\36\20\2\u0092\u0094\5\34\17\2\u0093\u0092\3\2\2"+
		"\2\u0094\u0095\3\2\2\2\u0095\u0093\3\2\2\2\u0095\u0096\3\2\2\2\u0096\u00b7"+
		"\3\2\2\2\u0097\u0099\5\34\17\2\u0098\u0097\3\2\2\2\u0099\u009a\3\2\2\2"+
		"\u009a\u0098\3\2\2\2\u009a\u009b\3\2\2\2\u009b\u009c\3\2\2\2\u009c\u009d"+
		"\5\"\22\2\u009d\u00b7\3\2\2\2\u009e\u00a0\5\"\22\2\u009f\u00a1\5\34\17"+
		"\2\u00a0\u009f\3\2\2\2\u00a1\u00a2\3\2\2\2\u00a2\u00a0\3\2\2\2\u00a2\u00a3"+
		"\3\2\2\2\u00a3\u00b7\3\2\2\2\u00a4\u00a6\5\34\17\2\u00a5\u00a4\3\2\2\2"+
		"\u00a6\u00a7\3\2\2\2\u00a7\u00a5\3\2\2\2\u00a7\u00a8\3\2\2\2\u00a8\u00a9"+
		"\3\2\2\2\u00a9\u00aa\5\36\20\2\u00aa\u00b7\3\2\2\2\u00ab\u00ad\5\36\20"+
		"\2\u00ac\u00ae\5\34\17\2\u00ad\u00ac\3\2\2\2\u00ae\u00af\3\2\2\2\u00af"+
		"\u00ad\3\2\2\2\u00af\u00b0\3\2\2\2\u00b0\u00b7\3\2\2\2\u00b1\u00b3\5\34"+
		"\17\2\u00b2\u00b1\3\2\2\2\u00b3\u00b4\3\2\2\2\u00b4\u00b2\3\2\2\2\u00b4"+
		"\u00b5\3\2\2\2\u00b5\u00b7\3\2\2\2\u00b6j\3\2\2\2\u00b6r\3\2\2\2\u00b6"+
		"y\3\2\2\2\u00b6\u0081\3\2\2\2\u00b6\u0088\3\2\2\2\u00b6\u0090\3\2\2\2"+
		"\u00b6\u0098\3\2\2\2\u00b6\u009e\3\2\2\2\u00b6\u00a5\3\2\2\2\u00b6\u00ab"+
		"\3\2\2\2\u00b6\u00b2\3\2\2\2\u00b7\33\3\2\2\2\u00b8\u00ba\7\r\2\2\u00b9"+
		"\u00b8\3\2\2\2\u00b9\u00ba\3\2\2\2\u00ba\u00bb\3\2\2\2\u00bb\u00bf\7\16"+
		"\2\2\u00bc\u00bd\7\n\2\2\u00bd\u00bf\7\17\2\2\u00be\u00b9\3\2\2\2\u00be"+
		"\u00bc\3\2\2\2\u00bf\35\3\2\2\2\u00c0\u00c2\7\13\2\2\u00c1\u00c0\3\2\2"+
		"\2\u00c1\u00c2\3\2\2\2\u00c2\u00c3\3\2\2\2\u00c3\u00c4\5 \21\2\u00c4\37"+
		"\3\2\2\2\u00c5\u00c6\b\21\1\2\u00c6\u00c7\7\23\2\2\u00c7\u00d6\5 \21\5"+
		"\u00c8\u00ca\7\20\2\2\u00c9\u00cb\7\24\2\2\u00ca\u00c9\3\2\2\2\u00ca\u00cb"+
		"\3\2\2\2\u00cb\u00cc\3\2\2\2\u00cc\u00d4\7\21\2\2\u00cd\u00cf\7\21\2\2"+
		"\u00ce\u00d0\7\24\2\2\u00cf\u00ce\3\2\2\2\u00cf\u00d0\3\2\2\2\u00d0\u00d1"+
		"\3\2\2\2\u00d1\u00d4\7\20\2\2\u00d2\u00d4\7\21\2\2\u00d3\u00c8\3\2\2\2"+
		"\u00d3\u00cd\3\2\2\2\u00d3\u00d2\3\2\2\2\u00d4\u00d6\3\2\2\2\u00d5\u00c5"+
		"\3\2\2\2\u00d5\u00d3\3\2\2\2\u00d6\u00dc\3\2\2\2\u00d7\u00d8\f\4\2\2\u00d8"+
		"\u00d9\7\22\2\2\u00d9\u00db\5 \21\5\u00da\u00d7\3\2\2\2\u00db\u00de\3"+
		"\2\2\2\u00dc\u00da\3\2\2\2\u00dc\u00dd\3\2\2\2\u00dd!\3\2\2\2\u00de\u00dc"+
		"\3\2\2\2\u00df\u00e1\7\f\2\2\u00e0\u00e2\7\20\2\2\u00e1\u00e0\3\2\2\2"+
		"\u00e2\u00e3\3\2\2\2\u00e3\u00e1\3\2\2\2\u00e3\u00e4\3\2\2\2\u00e4#\3"+
		"\2\2\2 *:BJSY`bglt}\u0086\u008c\u0095\u009a\u00a2\u00a7\u00af\u00b4\u00b6"+
		"\u00b9\u00be\u00c1\u00ca\u00cf\u00d3\u00d5\u00dc\u00e3";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}