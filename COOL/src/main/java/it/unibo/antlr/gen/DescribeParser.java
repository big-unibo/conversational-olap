// Generated from Describe.g4 by ANTLR 4.5

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
public class DescribeParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.5", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		IN=10, AND=11, NOT=12, TRUE=13, FALSE=14, GT=15, GE=16, LT=17, LE=18, 
		EQ=19, MODEL=20, DECIMAL=21, INT=22, ID=23, WS=24;
	public static final int
		RULE_describe = 0, RULE_id = 1, RULE_clause = 2, RULE_condition = 3, RULE_value = 4, 
		RULE_comparator = 5, RULE_binary = 6, RULE_bool = 7;
	public static final String[] ruleNames = {
		"describe", "id", "clause", "condition", "value", "comparator", "binary", 
		"bool"
	};

	private static final String[] _LITERAL_NAMES = {
		null, "'with'", "'describe'", "','", "'for'", "'by'", "'using'", "'size'", 
		"'('", "')'", null, null, null, null, null, "'>'", "'>='", "'<'", "'<='", 
		"'='"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, null, null, null, null, null, null, null, null, null, "IN", "AND", 
		"NOT", "TRUE", "FALSE", "GT", "GE", "LT", "LE", "EQ", "MODEL", "DECIMAL", 
		"INT", "ID", "WS"
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
	public String getGrammarFileName() { return "Describe.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public DescribeParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class DescribeContext extends ParserRuleContext {
		public IdContext cube;
		public IdContext id;
		public List<IdContext> mc = new ArrayList<IdContext>();
		public ClauseContext sc;
		public IdContext gc;
		public Token MODEL;
		public List<Token> models = new ArrayList<Token>();
		public Token k;
		public TerminalNode EOF() { return getToken(DescribeParser.EOF, 0); }
		public List<IdContext> id() {
			return getRuleContexts(IdContext.class);
		}
		public IdContext id(int i) {
			return getRuleContext(IdContext.class,i);
		}
		public ClauseContext clause() {
			return getRuleContext(ClauseContext.class,0);
		}
		public List<TerminalNode> MODEL() { return getTokens(DescribeParser.MODEL); }
		public TerminalNode MODEL(int i) {
			return getToken(DescribeParser.MODEL, i);
		}
		public TerminalNode INT() { return getToken(DescribeParser.INT, 0); }
		public DescribeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_describe; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DescribeListener ) ((DescribeListener)listener).enterDescribe(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DescribeListener ) ((DescribeListener)listener).exitDescribe(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DescribeVisitor ) return ((DescribeVisitor<? extends T>)visitor).visitDescribe(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DescribeContext describe() throws RecognitionException {
		DescribeContext _localctx = new DescribeContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_describe);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(16);
			match(T__0);
			setState(17);
			((DescribeContext)_localctx).cube = id();
			setState(18);
			match(T__1);
			setState(19);
			((DescribeContext)_localctx).id = id();
			((DescribeContext)_localctx).mc.add(((DescribeContext)_localctx).id);
			setState(24);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__2) {
				{
				{
				setState(20);
				match(T__2);
				setState(21);
				((DescribeContext)_localctx).id = id();
				((DescribeContext)_localctx).mc.add(((DescribeContext)_localctx).id);
				}
				}
				setState(26);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(29);
			_la = _input.LA(1);
			if (_la==T__3) {
				{
				setState(27);
				match(T__3);
				setState(28);
				((DescribeContext)_localctx).sc = clause();
				}
			}

			setState(33);
			_la = _input.LA(1);
			if (_la==T__4) {
				{
				setState(31);
				match(T__4);
				setState(32);
				((DescribeContext)_localctx).gc = id();
				}
			}

			setState(44);
			_la = _input.LA(1);
			if (_la==T__5) {
				{
				setState(35);
				match(T__5);
				setState(36);
				((DescribeContext)_localctx).MODEL = match(MODEL);
				((DescribeContext)_localctx).models.add(((DescribeContext)_localctx).MODEL);
				setState(41);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__2) {
					{
					{
					setState(37);
					match(T__2);
					setState(38);
					((DescribeContext)_localctx).MODEL = match(MODEL);
					((DescribeContext)_localctx).models.add(((DescribeContext)_localctx).MODEL);
					}
					}
					setState(43);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(48);
			_la = _input.LA(1);
			if (_la==T__6) {
				{
				setState(46);
				match(T__6);
				setState(47);
				((DescribeContext)_localctx).k = match(INT);
				}
			}

			setState(50);
			match(EOF);
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

	public static class IdContext extends ParserRuleContext {
		public String name;
		public Token ID;
		public TerminalNode ID() { return getToken(DescribeParser.ID, 0); }
		public IdContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_id; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DescribeListener ) ((DescribeListener)listener).enterId(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DescribeListener ) ((DescribeListener)listener).exitId(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DescribeVisitor ) return ((DescribeVisitor<? extends T>)visitor).visitId(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IdContext id() throws RecognitionException {
		IdContext _localctx = new IdContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_id);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(52);
			((IdContext)_localctx).ID = match(ID);
			 ((IdContext)_localctx).name =  (((IdContext)_localctx).ID!=null?((IdContext)_localctx).ID.getText():null); 
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

	public static class ClauseContext extends ParserRuleContext {
		public List<ConditionContext> condition() {
			return getRuleContexts(ConditionContext.class);
		}
		public ConditionContext condition(int i) {
			return getRuleContext(ConditionContext.class,i);
		}
		public List<BinaryContext> binary() {
			return getRuleContexts(BinaryContext.class);
		}
		public BinaryContext binary(int i) {
			return getRuleContext(BinaryContext.class,i);
		}
		public ClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_clause; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DescribeListener ) ((DescribeListener)listener).enterClause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DescribeListener ) ((DescribeListener)listener).exitClause(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DescribeVisitor ) return ((DescribeVisitor<? extends T>)visitor).visitClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ClauseContext clause() throws RecognitionException {
		ClauseContext _localctx = new ClauseContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_clause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(55);
			condition();
			setState(61);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==AND) {
				{
				{
				setState(56);
				binary();
				setState(57);
				condition();
				}
				}
				setState(63);
				_errHandler.sync(this);
				_la = _input.LA(1);
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

	public static class ConditionContext extends ParserRuleContext {
		public Token attr;
		public ComparatorContext op;
		public ValueContext value;
		public List<ValueContext> val = new ArrayList<ValueContext>();
		public Token in;
		public TerminalNode ID() { return getToken(DescribeParser.ID, 0); }
		public ComparatorContext comparator() {
			return getRuleContext(ComparatorContext.class,0);
		}
		public List<ValueContext> value() {
			return getRuleContexts(ValueContext.class);
		}
		public ValueContext value(int i) {
			return getRuleContext(ValueContext.class,i);
		}
		public TerminalNode IN() { return getToken(DescribeParser.IN, 0); }
		public ConditionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_condition; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DescribeListener ) ((DescribeListener)listener).enterCondition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DescribeListener ) ((DescribeListener)listener).exitCondition(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DescribeVisitor ) return ((DescribeVisitor<? extends T>)visitor).visitCondition(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ConditionContext condition() throws RecognitionException {
		ConditionContext _localctx = new ConditionContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_condition);
		int _la;
		try {
			setState(81);
			switch ( getInterpreter().adaptivePredict(_input,8,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(64);
				((ConditionContext)_localctx).attr = match(ID);
				setState(65);
				((ConditionContext)_localctx).op = comparator();
				setState(66);
				((ConditionContext)_localctx).value = value();
				((ConditionContext)_localctx).val.add(((ConditionContext)_localctx).value);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(68);
				((ConditionContext)_localctx).attr = match(ID);
				setState(69);
				((ConditionContext)_localctx).in = match(IN);
				setState(70);
				match(T__7);
				setState(71);
				((ConditionContext)_localctx).value = value();
				((ConditionContext)_localctx).val.add(((ConditionContext)_localctx).value);
				setState(76);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__2) {
					{
					{
					setState(72);
					match(T__2);
					setState(73);
					((ConditionContext)_localctx).value = value();
					((ConditionContext)_localctx).val.add(((ConditionContext)_localctx).value);
					}
					}
					setState(78);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(79);
				match(T__8);
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

	public static class ValueContext extends ParserRuleContext {
		public TerminalNode ID() { return getToken(DescribeParser.ID, 0); }
		public TerminalNode DECIMAL() { return getToken(DescribeParser.DECIMAL, 0); }
		public TerminalNode INT() { return getToken(DescribeParser.INT, 0); }
		public BoolContext bool() {
			return getRuleContext(BoolContext.class,0);
		}
		public ValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_value; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DescribeListener ) ((DescribeListener)listener).enterValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DescribeListener ) ((DescribeListener)listener).exitValue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DescribeVisitor ) return ((DescribeVisitor<? extends T>)visitor).visitValue(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ValueContext value() throws RecognitionException {
		ValueContext _localctx = new ValueContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_value);
		try {
			setState(87);
			switch (_input.LA(1)) {
			case ID:
				enterOuterAlt(_localctx, 1);
				{
				setState(83);
				match(ID);
				}
				break;
			case DECIMAL:
				enterOuterAlt(_localctx, 2);
				{
				setState(84);
				match(DECIMAL);
				}
				break;
			case INT:
				enterOuterAlt(_localctx, 3);
				{
				setState(85);
				match(INT);
				}
				break;
			case TRUE:
			case FALSE:
				enterOuterAlt(_localctx, 4);
				{
				setState(86);
				bool();
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

	public static class ComparatorContext extends ParserRuleContext {
		public TerminalNode GE() { return getToken(DescribeParser.GE, 0); }
		public TerminalNode LE() { return getToken(DescribeParser.LE, 0); }
		public TerminalNode EQ() { return getToken(DescribeParser.EQ, 0); }
		public TerminalNode GT() { return getToken(DescribeParser.GT, 0); }
		public TerminalNode LT() { return getToken(DescribeParser.LT, 0); }
		public ComparatorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_comparator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DescribeListener ) ((DescribeListener)listener).enterComparator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DescribeListener ) ((DescribeListener)listener).exitComparator(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DescribeVisitor ) return ((DescribeVisitor<? extends T>)visitor).visitComparator(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ComparatorContext comparator() throws RecognitionException {
		ComparatorContext _localctx = new ComparatorContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_comparator);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(89);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << GT) | (1L << GE) | (1L << LT) | (1L << LE) | (1L << EQ))) != 0)) ) {
			_errHandler.recoverInline(this);
			} else {
				consume();
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

	public static class BinaryContext extends ParserRuleContext {
		public TerminalNode AND() { return getToken(DescribeParser.AND, 0); }
		public BinaryContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_binary; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DescribeListener ) ((DescribeListener)listener).enterBinary(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DescribeListener ) ((DescribeListener)listener).exitBinary(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DescribeVisitor ) return ((DescribeVisitor<? extends T>)visitor).visitBinary(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BinaryContext binary() throws RecognitionException {
		BinaryContext _localctx = new BinaryContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_binary);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(91);
			match(AND);
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

	public static class BoolContext extends ParserRuleContext {
		public TerminalNode TRUE() { return getToken(DescribeParser.TRUE, 0); }
		public TerminalNode FALSE() { return getToken(DescribeParser.FALSE, 0); }
		public BoolContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_bool; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DescribeListener ) ((DescribeListener)listener).enterBool(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DescribeListener ) ((DescribeListener)listener).exitBool(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DescribeVisitor ) return ((DescribeVisitor<? extends T>)visitor).visitBool(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BoolContext bool() throws RecognitionException {
		BoolContext _localctx = new BoolContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_bool);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(93);
			_la = _input.LA(1);
			if ( !(_la==TRUE || _la==FALSE) ) {
			_errHandler.recoverInline(this);
			} else {
				consume();
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

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3\32b\4\2\t\2\4\3\t"+
		"\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\3\2\3\2\3\2\3\2\3\2"+
		"\3\2\7\2\31\n\2\f\2\16\2\34\13\2\3\2\3\2\5\2 \n\2\3\2\3\2\5\2$\n\2\3\2"+
		"\3\2\3\2\3\2\7\2*\n\2\f\2\16\2-\13\2\5\2/\n\2\3\2\3\2\5\2\63\n\2\3\2\3"+
		"\2\3\3\3\3\3\3\3\4\3\4\3\4\3\4\7\4>\n\4\f\4\16\4A\13\4\3\5\3\5\3\5\3\5"+
		"\3\5\3\5\3\5\3\5\3\5\3\5\7\5M\n\5\f\5\16\5P\13\5\3\5\3\5\5\5T\n\5\3\6"+
		"\3\6\3\6\3\6\5\6Z\n\6\3\7\3\7\3\b\3\b\3\t\3\t\3\t\2\2\n\2\4\6\b\n\f\16"+
		"\20\2\4\3\2\21\25\3\2\17\20e\2\22\3\2\2\2\4\66\3\2\2\2\69\3\2\2\2\bS\3"+
		"\2\2\2\nY\3\2\2\2\f[\3\2\2\2\16]\3\2\2\2\20_\3\2\2\2\22\23\7\3\2\2\23"+
		"\24\5\4\3\2\24\25\7\4\2\2\25\32\5\4\3\2\26\27\7\5\2\2\27\31\5\4\3\2\30"+
		"\26\3\2\2\2\31\34\3\2\2\2\32\30\3\2\2\2\32\33\3\2\2\2\33\37\3\2\2\2\34"+
		"\32\3\2\2\2\35\36\7\6\2\2\36 \5\6\4\2\37\35\3\2\2\2\37 \3\2\2\2 #\3\2"+
		"\2\2!\"\7\7\2\2\"$\5\4\3\2#!\3\2\2\2#$\3\2\2\2$.\3\2\2\2%&\7\b\2\2&+\7"+
		"\26\2\2\'(\7\5\2\2(*\7\26\2\2)\'\3\2\2\2*-\3\2\2\2+)\3\2\2\2+,\3\2\2\2"+
		",/\3\2\2\2-+\3\2\2\2.%\3\2\2\2./\3\2\2\2/\62\3\2\2\2\60\61\7\t\2\2\61"+
		"\63\7\30\2\2\62\60\3\2\2\2\62\63\3\2\2\2\63\64\3\2\2\2\64\65\7\2\2\3\65"+
		"\3\3\2\2\2\66\67\7\31\2\2\678\b\3\1\28\5\3\2\2\29?\5\b\5\2:;\5\16\b\2"+
		";<\5\b\5\2<>\3\2\2\2=:\3\2\2\2>A\3\2\2\2?=\3\2\2\2?@\3\2\2\2@\7\3\2\2"+
		"\2A?\3\2\2\2BC\7\31\2\2CD\5\f\7\2DE\5\n\6\2ET\3\2\2\2FG\7\31\2\2GH\7\f"+
		"\2\2HI\7\n\2\2IN\5\n\6\2JK\7\5\2\2KM\5\n\6\2LJ\3\2\2\2MP\3\2\2\2NL\3\2"+
		"\2\2NO\3\2\2\2OQ\3\2\2\2PN\3\2\2\2QR\7\13\2\2RT\3\2\2\2SB\3\2\2\2SF\3"+
		"\2\2\2T\t\3\2\2\2UZ\7\31\2\2VZ\7\27\2\2WZ\7\30\2\2XZ\5\20\t\2YU\3\2\2"+
		"\2YV\3\2\2\2YW\3\2\2\2YX\3\2\2\2Z\13\3\2\2\2[\\\t\2\2\2\\\r\3\2\2\2]^"+
		"\7\r\2\2^\17\3\2\2\2_`\t\3\2\2`\21\3\2\2\2\f\32\37#+.\62?NSY";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}