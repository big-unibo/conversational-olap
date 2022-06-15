// Generated from OLAP.g4 by ANTLR 4.10.1

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
	static { RuntimeMetaData.checkVersion("4.10.1", RuntimeMetaData.VERSION); }

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
	private static String[] makeRuleNames() {
		return new String[] {
			"operator", "replace", "replace_spec", "remove", "remove_spec", "add", 
			"add_spec", "sad", "rollup", "drill", "init", "unparsed", "gpsj", "mc", 
			"sc", "ssc", "gc"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'REPLACE'", "'ACCESSORY'", "'DROP'", "'ADD'", "'SAD'", "'ROLLUP'", 
			"'DRILL'", "'COUNT'", "'WHERE'", "'BY'", "'AGG'", "'MEA'", "'FACT'", 
			"'ATTR'", "'VAL'", null, "'NOT'", "'COP'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, null, null, null, "AGG", 
			"MEA", "FACT", "ATTR", "VAL", "BINARY", "NOT", "COP", "WS", "ERRCHAR"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
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
			_errHandler.sync(this);
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
			_errHandler.sync(this);
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
			_errHandler.sync(this);
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
			_errHandler.sync(this);
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
			_errHandler.sync(this);
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
			_errHandler.sync(this);
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
			_errHandler.sync(this);
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
			_errHandler.sync(this);
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
			_errHandler.sync(this);
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
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case AGG:
			case MEA:
				_localctx = new MeaContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(183);
				_errHandler.sync(this);
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
			_errHandler.sync(this);
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
			_errHandler.sync(this);
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
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,26,_ctx) ) {
				case 1:
					{
					setState(198);
					((AtomContext)_localctx).attr1 = match(ATTR);
					setState(200);
					_errHandler.sync(this);
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
					_errHandler.sync(this);
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
		"\u0004\u0001\u0014\u00e4\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001"+
		"\u0002\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004"+
		"\u0002\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007\u0007\u0007"+
		"\u0002\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b\u0007\u000b"+
		"\u0002\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e\u0002\u000f\u0007"+
		"\u000f\u0002\u0010\u0007\u0010\u0001\u0000\u0001\u0000\u0001\u0000\u0001"+
		"\u0000\u0001\u0000\u0001\u0000\u0003\u0000)\b\u0000\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001"+
		"\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001"+
		"\u0002\u0003\u00029\b\u0002\u0001\u0003\u0001\u0003\u0001\u0003\u0001"+
		"\u0004\u0001\u0004\u0001\u0004\u0003\u0004A\b\u0004\u0001\u0005\u0001"+
		"\u0005\u0001\u0005\u0001\u0006\u0001\u0006\u0001\u0006\u0003\u0006I\b"+
		"\u0006\u0001\u0007\u0001\u0007\u0001\u0007\u0001\b\u0001\b\u0001\b\u0001"+
		"\b\u0003\bR\b\b\u0001\t\u0001\t\u0001\t\u0001\t\u0003\tX\b\t\u0001\n\u0001"+
		"\n\u0001\n\u0004\n]\b\n\u000b\n\f\n^\u0003\na\b\n\u0001\u000b\u0001\u000b"+
		"\u0001\u000b\u0003\u000bf\b\u000b\u0001\f\u0004\fi\b\f\u000b\f\f\fj\u0001"+
		"\f\u0001\f\u0001\f\u0001\f\u0004\fq\b\f\u000b\f\f\fr\u0001\f\u0001\f\u0001"+
		"\f\u0001\f\u0001\f\u0004\fz\b\f\u000b\f\f\f{\u0001\f\u0001\f\u0001\f\u0001"+
		"\f\u0001\f\u0004\f\u0083\b\f\u000b\f\f\f\u0084\u0001\f\u0001\f\u0004\f"+
		"\u0089\b\f\u000b\f\f\f\u008a\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0004"+
		"\f\u0092\b\f\u000b\f\f\f\u0093\u0001\f\u0004\f\u0097\b\f\u000b\f\f\f\u0098"+
		"\u0001\f\u0001\f\u0001\f\u0001\f\u0004\f\u009f\b\f\u000b\f\f\f\u00a0\u0001"+
		"\f\u0004\f\u00a4\b\f\u000b\f\f\f\u00a5\u0001\f\u0001\f\u0001\f\u0001\f"+
		"\u0004\f\u00ac\b\f\u000b\f\f\f\u00ad\u0001\f\u0004\f\u00b1\b\f\u000b\f"+
		"\f\f\u00b2\u0003\f\u00b5\b\f\u0001\r\u0003\r\u00b8\b\r\u0001\r\u0001\r"+
		"\u0001\r\u0003\r\u00bd\b\r\u0001\u000e\u0003\u000e\u00c0\b\u000e\u0001"+
		"\u000e\u0001\u000e\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0001"+
		"\u000f\u0003\u000f\u00c9\b\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0003"+
		"\u000f\u00ce\b\u000f\u0001\u000f\u0001\u000f\u0003\u000f\u00d2\b\u000f"+
		"\u0003\u000f\u00d4\b\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0005\u000f"+
		"\u00d9\b\u000f\n\u000f\f\u000f\u00dc\t\u000f\u0001\u0010\u0001\u0010\u0004"+
		"\u0010\u00e0\b\u0010\u000b\u0010\f\u0010\u00e1\u0001\u0010\u0000\u0001"+
		"\u001e\u0011\u0000\u0002\u0004\u0006\b\n\f\u000e\u0010\u0012\u0014\u0016"+
		"\u0018\u001a\u001c\u001e \u0000\u0000\u0102\u0000(\u0001\u0000\u0000\u0000"+
		"\u0002*\u0001\u0000\u0000\u0000\u00048\u0001\u0000\u0000\u0000\u0006:"+
		"\u0001\u0000\u0000\u0000\b@\u0001\u0000\u0000\u0000\nB\u0001\u0000\u0000"+
		"\u0000\fH\u0001\u0000\u0000\u0000\u000eJ\u0001\u0000\u0000\u0000\u0010"+
		"M\u0001\u0000\u0000\u0000\u0012S\u0001\u0000\u0000\u0000\u0014`\u0001"+
		"\u0000\u0000\u0000\u0016e\u0001\u0000\u0000\u0000\u0018\u00b4\u0001\u0000"+
		"\u0000\u0000\u001a\u00bc\u0001\u0000\u0000\u0000\u001c\u00bf\u0001\u0000"+
		"\u0000\u0000\u001e\u00d3\u0001\u0000\u0000\u0000 \u00dd\u0001\u0000\u0000"+
		"\u0000\")\u0003\u0012\t\u0000#)\u0003\u0010\b\u0000$)\u0003\u000e\u0007"+
		"\u0000%)\u0003\n\u0005\u0000&)\u0003\u0006\u0003\u0000\')\u0003\u0002"+
		"\u0001\u0000(\"\u0001\u0000\u0000\u0000(#\u0001\u0000\u0000\u0000($\u0001"+
		"\u0000\u0000\u0000(%\u0001\u0000\u0000\u0000(&\u0001\u0000\u0000\u0000"+
		"(\'\u0001\u0000\u0000\u0000)\u0001\u0001\u0000\u0000\u0000*+\u0005\u0001"+
		"\u0000\u0000+,\u0003\u0004\u0002\u0000,\u0003\u0001\u0000\u0000\u0000"+
		"-.\u0003\u001a\r\u0000./\u0005\u0002\u0000\u0000/0\u0003\u001a\r\u0000"+
		"09\u0001\u0000\u0000\u000012\u0005\u000e\u0000\u000023\u0005\u0002\u0000"+
		"\u000039\u0005\u000e\u0000\u000045\u0003\u001e\u000f\u000056\u0005\u0002"+
		"\u0000\u000067\u0003\u001e\u000f\u000079\u0001\u0000\u0000\u00008-\u0001"+
		"\u0000\u0000\u000081\u0001\u0000\u0000\u000084\u0001\u0000\u0000\u0000"+
		"9\u0005\u0001\u0000\u0000\u0000:;\u0005\u0003\u0000\u0000;<\u0003\b\u0004"+
		"\u0000<\u0007\u0001\u0000\u0000\u0000=A\u0003\u001a\r\u0000>A\u0005\u000e"+
		"\u0000\u0000?A\u0003\u001e\u000f\u0000@=\u0001\u0000\u0000\u0000@>\u0001"+
		"\u0000\u0000\u0000@?\u0001\u0000\u0000\u0000A\t\u0001\u0000\u0000\u0000"+
		"BC\u0005\u0004\u0000\u0000CD\u0003\f\u0006\u0000D\u000b\u0001\u0000\u0000"+
		"\u0000EI\u0003\u001a\r\u0000FI\u0005\u000e\u0000\u0000GI\u0003\u001e\u000f"+
		"\u0000HE\u0001\u0000\u0000\u0000HF\u0001\u0000\u0000\u0000HG\u0001\u0000"+
		"\u0000\u0000I\r\u0001\u0000\u0000\u0000JK\u0005\u0005\u0000\u0000KL\u0003"+
		"\u001e\u000f\u0000L\u000f\u0001\u0000\u0000\u0000MN\u0005\u0006\u0000"+
		"\u0000NQ\u0005\u000e\u0000\u0000OP\u0005\u0002\u0000\u0000PR\u0005\u000e"+
		"\u0000\u0000QO\u0001\u0000\u0000\u0000QR\u0001\u0000\u0000\u0000R\u0011"+
		"\u0001\u0000\u0000\u0000ST\u0005\u0007\u0000\u0000TW\u0005\u000e\u0000"+
		"\u0000UV\u0005\u0002\u0000\u0000VX\u0005\u000e\u0000\u0000WU\u0001\u0000"+
		"\u0000\u0000WX\u0001\u0000\u0000\u0000X\u0013\u0001\u0000\u0000\u0000"+
		"Ya\u0003\u0018\f\u0000Z\\\u0003\u0018\f\u0000[]\u0003\u0016\u000b\u0000"+
		"\\[\u0001\u0000\u0000\u0000]^\u0001\u0000\u0000\u0000^\\\u0001\u0000\u0000"+
		"\u0000^_\u0001\u0000\u0000\u0000_a\u0001\u0000\u0000\u0000`Y\u0001\u0000"+
		"\u0000\u0000`Z\u0001\u0000\u0000\u0000a\u0015\u0001\u0000\u0000\u0000"+
		"bf\u0003\u001e\u000f\u0000cf\u0003 \u0010\u0000df\u0003\u0018\f\u0000"+
		"eb\u0001\u0000\u0000\u0000ec\u0001\u0000\u0000\u0000ed\u0001\u0000\u0000"+
		"\u0000f\u0017\u0001\u0000\u0000\u0000gi\u0003\u001a\r\u0000hg\u0001\u0000"+
		"\u0000\u0000ij\u0001\u0000\u0000\u0000jh\u0001\u0000\u0000\u0000jk\u0001"+
		"\u0000\u0000\u0000kl\u0001\u0000\u0000\u0000lm\u0003\u001c\u000e\u0000"+
		"mn\u0003 \u0010\u0000n\u00b5\u0001\u0000\u0000\u0000oq\u0003\u001a\r\u0000"+
		"po\u0001\u0000\u0000\u0000qr\u0001\u0000\u0000\u0000rp\u0001\u0000\u0000"+
		"\u0000rs\u0001\u0000\u0000\u0000st\u0001\u0000\u0000\u0000tu\u0003 \u0010"+
		"\u0000uv\u0003\u001c\u000e\u0000v\u00b5\u0001\u0000\u0000\u0000wy\u0003"+
		"\u001c\u000e\u0000xz\u0003\u001a\r\u0000yx\u0001\u0000\u0000\u0000z{\u0001"+
		"\u0000\u0000\u0000{y\u0001\u0000\u0000\u0000{|\u0001\u0000\u0000\u0000"+
		"|}\u0001\u0000\u0000\u0000}~\u0003 \u0010\u0000~\u00b5\u0001\u0000\u0000"+
		"\u0000\u007f\u0080\u0003\u001c\u000e\u0000\u0080\u0082\u0003 \u0010\u0000"+
		"\u0081\u0083\u0003\u001a\r\u0000\u0082\u0081\u0001\u0000\u0000\u0000\u0083"+
		"\u0084\u0001\u0000\u0000\u0000\u0084\u0082\u0001\u0000\u0000\u0000\u0084"+
		"\u0085\u0001\u0000\u0000\u0000\u0085\u00b5\u0001\u0000\u0000\u0000\u0086"+
		"\u0088\u0003 \u0010\u0000\u0087\u0089\u0003\u001a\r\u0000\u0088\u0087"+
		"\u0001\u0000\u0000\u0000\u0089\u008a\u0001\u0000\u0000\u0000\u008a\u0088"+
		"\u0001\u0000\u0000\u0000\u008a\u008b\u0001\u0000\u0000\u0000\u008b\u008c"+
		"\u0001\u0000\u0000\u0000\u008c\u008d\u0003\u001c\u000e\u0000\u008d\u00b5"+
		"\u0001\u0000\u0000\u0000\u008e\u008f\u0003 \u0010\u0000\u008f\u0091\u0003"+
		"\u001c\u000e\u0000\u0090\u0092\u0003\u001a\r\u0000\u0091\u0090\u0001\u0000"+
		"\u0000\u0000\u0092\u0093\u0001\u0000\u0000\u0000\u0093\u0091\u0001\u0000"+
		"\u0000\u0000\u0093\u0094\u0001\u0000\u0000\u0000\u0094\u00b5\u0001\u0000"+
		"\u0000\u0000\u0095\u0097\u0003\u001a\r\u0000\u0096\u0095\u0001\u0000\u0000"+
		"\u0000\u0097\u0098\u0001\u0000\u0000\u0000\u0098\u0096\u0001\u0000\u0000"+
		"\u0000\u0098\u0099\u0001\u0000\u0000\u0000\u0099\u009a\u0001\u0000\u0000"+
		"\u0000\u009a\u009b\u0003 \u0010\u0000\u009b\u00b5\u0001\u0000\u0000\u0000"+
		"\u009c\u009e\u0003 \u0010\u0000\u009d\u009f\u0003\u001a\r\u0000\u009e"+
		"\u009d\u0001\u0000\u0000\u0000\u009f\u00a0\u0001\u0000\u0000\u0000\u00a0"+
		"\u009e\u0001\u0000\u0000\u0000\u00a0\u00a1\u0001\u0000\u0000\u0000\u00a1"+
		"\u00b5\u0001\u0000\u0000\u0000\u00a2\u00a4\u0003\u001a\r\u0000\u00a3\u00a2"+
		"\u0001\u0000\u0000\u0000\u00a4\u00a5\u0001\u0000\u0000\u0000\u00a5\u00a3"+
		"\u0001\u0000\u0000\u0000\u00a5\u00a6\u0001\u0000\u0000\u0000\u00a6\u00a7"+
		"\u0001\u0000\u0000\u0000\u00a7\u00a8\u0003\u001c\u000e\u0000\u00a8\u00b5"+
		"\u0001\u0000\u0000\u0000\u00a9\u00ab\u0003\u001c\u000e\u0000\u00aa\u00ac"+
		"\u0003\u001a\r\u0000\u00ab\u00aa\u0001\u0000\u0000\u0000\u00ac\u00ad\u0001"+
		"\u0000\u0000\u0000\u00ad\u00ab\u0001\u0000\u0000\u0000\u00ad\u00ae\u0001"+
		"\u0000\u0000\u0000\u00ae\u00b5\u0001\u0000\u0000\u0000\u00af\u00b1\u0003"+
		"\u001a\r\u0000\u00b0\u00af\u0001\u0000\u0000\u0000\u00b1\u00b2\u0001\u0000"+
		"\u0000\u0000\u00b2\u00b0\u0001\u0000\u0000\u0000\u00b2\u00b3\u0001\u0000"+
		"\u0000\u0000\u00b3\u00b5\u0001\u0000\u0000\u0000\u00b4h\u0001\u0000\u0000"+
		"\u0000\u00b4p\u0001\u0000\u0000\u0000\u00b4w\u0001\u0000\u0000\u0000\u00b4"+
		"\u007f\u0001\u0000\u0000\u0000\u00b4\u0086\u0001\u0000\u0000\u0000\u00b4"+
		"\u008e\u0001\u0000\u0000\u0000\u00b4\u0096\u0001\u0000\u0000\u0000\u00b4"+
		"\u009c\u0001\u0000\u0000\u0000\u00b4\u00a3\u0001\u0000\u0000\u0000\u00b4"+
		"\u00a9\u0001\u0000\u0000\u0000\u00b4\u00b0\u0001\u0000\u0000\u0000\u00b5"+
		"\u0019\u0001\u0000\u0000\u0000\u00b6\u00b8\u0005\u000b\u0000\u0000\u00b7"+
		"\u00b6\u0001\u0000\u0000\u0000\u00b7\u00b8\u0001\u0000\u0000\u0000\u00b8"+
		"\u00b9\u0001\u0000\u0000\u0000\u00b9\u00bd\u0005\f\u0000\u0000\u00ba\u00bb"+
		"\u0005\b\u0000\u0000\u00bb\u00bd\u0005\r\u0000\u0000\u00bc\u00b7\u0001"+
		"\u0000\u0000\u0000\u00bc\u00ba\u0001\u0000\u0000\u0000\u00bd\u001b\u0001"+
		"\u0000\u0000\u0000\u00be\u00c0\u0005\t\u0000\u0000\u00bf\u00be\u0001\u0000"+
		"\u0000\u0000\u00bf\u00c0\u0001\u0000\u0000\u0000\u00c0\u00c1\u0001\u0000"+
		"\u0000\u0000\u00c1\u00c2\u0003\u001e\u000f\u0000\u00c2\u001d\u0001\u0000"+
		"\u0000\u0000\u00c3\u00c4\u0006\u000f\uffff\uffff\u0000\u00c4\u00c5\u0005"+
		"\u0011\u0000\u0000\u00c5\u00d4\u0003\u001e\u000f\u0003\u00c6\u00c8\u0005"+
		"\u000e\u0000\u0000\u00c7\u00c9\u0005\u0012\u0000\u0000\u00c8\u00c7\u0001"+
		"\u0000\u0000\u0000\u00c8\u00c9\u0001\u0000\u0000\u0000\u00c9\u00ca\u0001"+
		"\u0000\u0000\u0000\u00ca\u00d2\u0005\u000f\u0000\u0000\u00cb\u00cd\u0005"+
		"\u000f\u0000\u0000\u00cc\u00ce\u0005\u0012\u0000\u0000\u00cd\u00cc\u0001"+
		"\u0000\u0000\u0000\u00cd\u00ce\u0001\u0000\u0000\u0000\u00ce\u00cf\u0001"+
		"\u0000\u0000\u0000\u00cf\u00d2\u0005\u000e\u0000\u0000\u00d0\u00d2\u0005"+
		"\u000f\u0000\u0000\u00d1\u00c6\u0001\u0000\u0000\u0000\u00d1\u00cb\u0001"+
		"\u0000\u0000\u0000\u00d1\u00d0\u0001\u0000\u0000\u0000\u00d2\u00d4\u0001"+
		"\u0000\u0000\u0000\u00d3\u00c3\u0001\u0000\u0000\u0000\u00d3\u00d1\u0001"+
		"\u0000\u0000\u0000\u00d4\u00da\u0001\u0000\u0000\u0000\u00d5\u00d6\n\u0002"+
		"\u0000\u0000\u00d6\u00d7\u0005\u0010\u0000\u0000\u00d7\u00d9\u0003\u001e"+
		"\u000f\u0003\u00d8\u00d5\u0001\u0000\u0000\u0000\u00d9\u00dc\u0001\u0000"+
		"\u0000\u0000\u00da\u00d8\u0001\u0000\u0000\u0000\u00da\u00db\u0001\u0000"+
		"\u0000\u0000\u00db\u001f\u0001\u0000\u0000\u0000\u00dc\u00da\u0001\u0000"+
		"\u0000\u0000\u00dd\u00df\u0005\n\u0000\u0000\u00de\u00e0\u0005\u000e\u0000"+
		"\u0000\u00df\u00de\u0001\u0000\u0000\u0000\u00e0\u00e1\u0001\u0000\u0000"+
		"\u0000\u00e1\u00df\u0001\u0000\u0000\u0000\u00e1\u00e2\u0001\u0000\u0000"+
		"\u0000\u00e2!\u0001\u0000\u0000\u0000\u001e(8@HQW^`ejr{\u0084\u008a\u0093"+
		"\u0098\u00a0\u00a5\u00ad\u00b2\u00b4\u00b7\u00bc\u00bf\u00c8\u00cd\u00d1"+
		"\u00d3\u00da\u00e1";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}