// Generated from COOL.g4 by ANTLR 4.10.1

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
public class COOLParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.10.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, AGG=4, MEA=5, FACT=6, ATTR=7, VAL=8, BINARY=9, 
		NOT=10, COP=11, WS=12, ERRCHAR=13;
	public static final int
		RULE_init = 0, RULE_unparsed = 1, RULE_gpsj = 2, RULE_mc = 3, RULE_sc = 4, 
		RULE_ssc = 5, RULE_gc = 6;
	private static String[] makeRuleNames() {
		return new String[] {
			"init", "unparsed", "gpsj", "mc", "sc", "ssc", "gc"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'COUNT'", "'WHERE'", "'BY'", "'AGG'", "'MEA'", "'FACT'", "'ATTR'", 
			"'VAL'", null, "'NOT'", "'COP'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, "AGG", "MEA", "FACT", "ATTR", "VAL", "BINARY", 
			"NOT", "COP", "WS", "ERRCHAR"
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
	public String getGrammarFileName() { return "COOL.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public COOLParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
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
			if ( listener instanceof COOLListener ) ((COOLListener)listener).enterPartial(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof COOLListener ) ((COOLListener)listener).exitPartial(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof COOLVisitor ) return ((COOLVisitor<? extends T>)visitor).visitPartial(this);
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
			if ( listener instanceof COOLListener ) ((COOLListener)listener).enterFull(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof COOLListener ) ((COOLListener)listener).exitFull(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof COOLVisitor ) return ((COOLVisitor<? extends T>)visitor).visitFull(this);
			else return visitor.visitChildren(this);
		}
	}

	public final InitContext init() throws RecognitionException {
		InitContext _localctx = new InitContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_init);
		int _la;
		try {
			setState(21);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,1,_ctx) ) {
			case 1:
				_localctx = new FullContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(14);
				((FullContext)_localctx).GPSJ = gpsj();
				}
				break;
			case 2:
				_localctx = new PartialContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(15);
				((PartialContext)_localctx).GPSJ = gpsj();
				setState(17); 
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					setState(16);
					((PartialContext)_localctx).unparsed = unparsed();
					((PartialContext)_localctx).UP.add(((PartialContext)_localctx).unparsed);
					}
					}
					setState(19); 
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__1) | (1L << T__2) | (1L << AGG) | (1L << MEA) | (1L << ATTR) | (1L << VAL) | (1L << NOT))) != 0) );
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
			if ( listener instanceof COOLListener ) ((COOLListener)listener).enterUnparsed(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof COOLListener ) ((COOLListener)listener).exitUnparsed(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof COOLVisitor ) return ((COOLVisitor<? extends T>)visitor).visitUnparsed(this);
			else return visitor.visitChildren(this);
		}
	}

	public final UnparsedContext unparsed() throws RecognitionException {
		UnparsedContext _localctx = new UnparsedContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_unparsed);
		try {
			setState(26);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,2,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(23);
				ssc(0);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(24);
				gc();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(25);
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
			if ( listener instanceof COOLListener ) ((COOLListener)listener).enterGpsj(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof COOLListener ) ((COOLListener)listener).exitGpsj(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof COOLVisitor ) return ((COOLVisitor<? extends T>)visitor).visitGpsj(this);
			else return visitor.visitChildren(this);
		}
	}

	public final GpsjContext gpsj() throws RecognitionException {
		GpsjContext _localctx = new GpsjContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_gpsj);
		int _la;
		try {
			int _alt;
			setState(105);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,14,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(29); 
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					setState(28);
					((GpsjContext)_localctx).mc = mc();
					((GpsjContext)_localctx).MC.add(((GpsjContext)_localctx).mc);
					}
					}
					setState(31); 
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << AGG) | (1L << MEA))) != 0) );
				setState(33);
				((GpsjContext)_localctx).SC = sc();
				setState(34);
				((GpsjContext)_localctx).GC = gc();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(37); 
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					setState(36);
					((GpsjContext)_localctx).mc = mc();
					((GpsjContext)_localctx).MC.add(((GpsjContext)_localctx).mc);
					}
					}
					setState(39); 
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << AGG) | (1L << MEA))) != 0) );
				setState(41);
				((GpsjContext)_localctx).GC = gc();
				setState(42);
				((GpsjContext)_localctx).SC = sc();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(44);
				((GpsjContext)_localctx).SC = sc();
				setState(46); 
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					setState(45);
					((GpsjContext)_localctx).mc = mc();
					((GpsjContext)_localctx).MC.add(((GpsjContext)_localctx).mc);
					}
					}
					setState(48); 
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << AGG) | (1L << MEA))) != 0) );
				setState(50);
				((GpsjContext)_localctx).GC = gc();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(52);
				((GpsjContext)_localctx).SC = sc();
				setState(53);
				((GpsjContext)_localctx).GC = gc();
				setState(55); 
				_errHandler.sync(this);
				_alt = 1;
				do {
					switch (_alt) {
					case 1:
						{
						{
						setState(54);
						((GpsjContext)_localctx).mc = mc();
						((GpsjContext)_localctx).MC.add(((GpsjContext)_localctx).mc);
						}
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					setState(57); 
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,6,_ctx);
				} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(59);
				((GpsjContext)_localctx).GC = gc();
				setState(61); 
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					setState(60);
					((GpsjContext)_localctx).mc = mc();
					((GpsjContext)_localctx).MC.add(((GpsjContext)_localctx).mc);
					}
					}
					setState(63); 
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << AGG) | (1L << MEA))) != 0) );
				setState(65);
				((GpsjContext)_localctx).SC = sc();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(67);
				((GpsjContext)_localctx).GC = gc();
				setState(68);
				((GpsjContext)_localctx).SC = sc();
				setState(70); 
				_errHandler.sync(this);
				_alt = 1;
				do {
					switch (_alt) {
					case 1:
						{
						{
						setState(69);
						((GpsjContext)_localctx).mc = mc();
						((GpsjContext)_localctx).MC.add(((GpsjContext)_localctx).mc);
						}
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					setState(72); 
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,8,_ctx);
				} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(75); 
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					setState(74);
					((GpsjContext)_localctx).mc = mc();
					((GpsjContext)_localctx).MC.add(((GpsjContext)_localctx).mc);
					}
					}
					setState(77); 
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << AGG) | (1L << MEA))) != 0) );
				setState(79);
				((GpsjContext)_localctx).GC = gc();
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(81);
				((GpsjContext)_localctx).GC = gc();
				setState(83); 
				_errHandler.sync(this);
				_alt = 1;
				do {
					switch (_alt) {
					case 1:
						{
						{
						setState(82);
						((GpsjContext)_localctx).mc = mc();
						((GpsjContext)_localctx).MC.add(((GpsjContext)_localctx).mc);
						}
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					setState(85); 
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,10,_ctx);
				} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(88); 
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					setState(87);
					((GpsjContext)_localctx).mc = mc();
					((GpsjContext)_localctx).MC.add(((GpsjContext)_localctx).mc);
					}
					}
					setState(90); 
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << AGG) | (1L << MEA))) != 0) );
				setState(92);
				((GpsjContext)_localctx).SC = sc();
				}
				break;
			case 10:
				enterOuterAlt(_localctx, 10);
				{
				setState(94);
				((GpsjContext)_localctx).SC = sc();
				setState(96); 
				_errHandler.sync(this);
				_alt = 1;
				do {
					switch (_alt) {
					case 1:
						{
						{
						setState(95);
						((GpsjContext)_localctx).mc = mc();
						((GpsjContext)_localctx).MC.add(((GpsjContext)_localctx).mc);
						}
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					setState(98); 
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,12,_ctx);
				} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
				}
				break;
			case 11:
				enterOuterAlt(_localctx, 11);
				{
				setState(101); 
				_errHandler.sync(this);
				_alt = 1;
				do {
					switch (_alt) {
					case 1:
						{
						{
						setState(100);
						((GpsjContext)_localctx).mc = mc();
						((GpsjContext)_localctx).MC.add(((GpsjContext)_localctx).mc);
						}
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					setState(103); 
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,13,_ctx);
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
		public TerminalNode FACT() { return getToken(COOLParser.FACT, 0); }
		public FactContext(McContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof COOLListener ) ((COOLListener)listener).enterFact(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof COOLListener ) ((COOLListener)listener).exitFact(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof COOLVisitor ) return ((COOLVisitor<? extends T>)visitor).visitFact(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class MeaContext extends McContext {
		public Token agg;
		public Token mea;
		public TerminalNode MEA() { return getToken(COOLParser.MEA, 0); }
		public TerminalNode AGG() { return getToken(COOLParser.AGG, 0); }
		public MeaContext(McContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof COOLListener ) ((COOLListener)listener).enterMea(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof COOLListener ) ((COOLListener)listener).exitMea(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof COOLVisitor ) return ((COOLVisitor<? extends T>)visitor).visitMea(this);
			else return visitor.visitChildren(this);
		}
	}

	public final McContext mc() throws RecognitionException {
		McContext _localctx = new McContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_mc);
		int _la;
		try {
			setState(113);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case AGG:
			case MEA:
				_localctx = new MeaContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(108);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==AGG) {
					{
					setState(107);
					((MeaContext)_localctx).agg = match(AGG);
					}
				}

				setState(110);
				((MeaContext)_localctx).mea = match(MEA);
				}
				break;
			case T__0:
				_localctx = new FactContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(111);
				((FactContext)_localctx).cnt = match(T__0);
				setState(112);
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
			if ( listener instanceof COOLListener ) ((COOLListener)listener).enterSc(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof COOLListener ) ((COOLListener)listener).exitSc(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof COOLVisitor ) return ((COOLVisitor<? extends T>)visitor).visitSc(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ScContext sc() throws RecognitionException {
		ScContext _localctx = new ScContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_sc);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(116);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__1) {
				{
				setState(115);
				((ScContext)_localctx).where = match(T__1);
				}
			}

			setState(118);
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
		public TerminalNode BINARY() { return getToken(COOLParser.BINARY, 0); }
		public BinaryContext(SscContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof COOLListener ) ((COOLListener)listener).enterBinary(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof COOLListener ) ((COOLListener)listener).exitBinary(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof COOLVisitor ) return ((COOLVisitor<? extends T>)visitor).visitBinary(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class UnaryContext extends SscContext {
		public Token unary;
		public SscContext left;
		public TerminalNode NOT() { return getToken(COOLParser.NOT, 0); }
		public SscContext ssc() {
			return getRuleContext(SscContext.class,0);
		}
		public UnaryContext(SscContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof COOLListener ) ((COOLListener)listener).enterUnary(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof COOLListener ) ((COOLListener)listener).exitUnary(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof COOLVisitor ) return ((COOLVisitor<? extends T>)visitor).visitUnary(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class AtomContext extends SscContext {
		public Token attr1;
		public Token cop;
		public Token val;
		public TerminalNode ATTR() { return getToken(COOLParser.ATTR, 0); }
		public TerminalNode VAL() { return getToken(COOLParser.VAL, 0); }
		public TerminalNode COP() { return getToken(COOLParser.COP, 0); }
		public AtomContext(SscContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof COOLListener ) ((COOLListener)listener).enterAtom(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof COOLListener ) ((COOLListener)listener).exitAtom(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof COOLVisitor ) return ((COOLVisitor<? extends T>)visitor).visitAtom(this);
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
		int _startState = 10;
		enterRecursionRule(_localctx, 10, RULE_ssc, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(136);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case NOT:
				{
				_localctx = new UnaryContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(121);
				((UnaryContext)_localctx).unary = match(NOT);
				setState(122);
				((UnaryContext)_localctx).left = ssc(3);
				}
				break;
			case ATTR:
			case VAL:
				{
				_localctx = new AtomContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(134);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,20,_ctx) ) {
				case 1:
					{
					setState(123);
					((AtomContext)_localctx).attr1 = match(ATTR);
					setState(125);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==COP) {
						{
						setState(124);
						((AtomContext)_localctx).cop = match(COP);
						}
					}

					setState(127);
					((AtomContext)_localctx).val = match(VAL);
					}
					break;
				case 2:
					{
					setState(128);
					((AtomContext)_localctx).val = match(VAL);
					setState(130);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==COP) {
						{
						setState(129);
						((AtomContext)_localctx).cop = match(COP);
						}
					}

					setState(132);
					((AtomContext)_localctx).attr1 = match(ATTR);
					}
					break;
				case 3:
					{
					setState(133);
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
			setState(143);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,22,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					{
					_localctx = new BinaryContext(new SscContext(_parentctx, _parentState));
					((BinaryContext)_localctx).left = _prevctx;
					pushNewRecursionContext(_localctx, _startState, RULE_ssc);
					setState(138);
					if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
					setState(139);
					((BinaryContext)_localctx).binary = match(BINARY);
					setState(140);
					((BinaryContext)_localctx).right = ssc(3);
					}
					} 
				}
				setState(145);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,22,_ctx);
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
		public List<TerminalNode> ATTR() { return getTokens(COOLParser.ATTR); }
		public TerminalNode ATTR(int i) {
			return getToken(COOLParser.ATTR, i);
		}
		public GcContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_gc; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof COOLListener ) ((COOLListener)listener).enterGc(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof COOLListener ) ((COOLListener)listener).exitGc(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof COOLVisitor ) return ((COOLVisitor<? extends T>)visitor).visitGc(this);
			else return visitor.visitChildren(this);
		}
	}

	public final GcContext gc() throws RecognitionException {
		GcContext _localctx = new GcContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_gc);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(146);
			((GcContext)_localctx).by = match(T__2);
			setState(148); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(147);
					((GcContext)_localctx).ATTR = match(ATTR);
					((GcContext)_localctx).attr.add(((GcContext)_localctx).ATTR);
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(150); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,23,_ctx);
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
		case 5:
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
		"\u0004\u0001\r\u0099\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002"+
		"\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004\u0002"+
		"\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0001\u0000\u0001\u0000\u0001"+
		"\u0000\u0004\u0000\u0012\b\u0000\u000b\u0000\f\u0000\u0013\u0003\u0000"+
		"\u0016\b\u0000\u0001\u0001\u0001\u0001\u0001\u0001\u0003\u0001\u001b\b"+
		"\u0001\u0001\u0002\u0004\u0002\u001e\b\u0002\u000b\u0002\f\u0002\u001f"+
		"\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0004\u0002&\b\u0002"+
		"\u000b\u0002\f\u0002\'\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002"+
		"\u0001\u0002\u0004\u0002/\b\u0002\u000b\u0002\f\u00020\u0001\u0002\u0001"+
		"\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0004\u00028\b\u0002\u000b"+
		"\u0002\f\u00029\u0001\u0002\u0001\u0002\u0004\u0002>\b\u0002\u000b\u0002"+
		"\f\u0002?\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002"+
		"\u0004\u0002G\b\u0002\u000b\u0002\f\u0002H\u0001\u0002\u0004\u0002L\b"+
		"\u0002\u000b\u0002\f\u0002M\u0001\u0002\u0001\u0002\u0001\u0002\u0001"+
		"\u0002\u0004\u0002T\b\u0002\u000b\u0002\f\u0002U\u0001\u0002\u0004\u0002"+
		"Y\b\u0002\u000b\u0002\f\u0002Z\u0001\u0002\u0001\u0002\u0001\u0002\u0001"+
		"\u0002\u0004\u0002a\b\u0002\u000b\u0002\f\u0002b\u0001\u0002\u0004\u0002"+
		"f\b\u0002\u000b\u0002\f\u0002g\u0003\u0002j\b\u0002\u0001\u0003\u0003"+
		"\u0003m\b\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0003\u0003r\b\u0003"+
		"\u0001\u0004\u0003\u0004u\b\u0004\u0001\u0004\u0001\u0004\u0001\u0005"+
		"\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0003\u0005~\b\u0005"+
		"\u0001\u0005\u0001\u0005\u0001\u0005\u0003\u0005\u0083\b\u0005\u0001\u0005"+
		"\u0001\u0005\u0003\u0005\u0087\b\u0005\u0003\u0005\u0089\b\u0005\u0001"+
		"\u0005\u0001\u0005\u0001\u0005\u0005\u0005\u008e\b\u0005\n\u0005\f\u0005"+
		"\u0091\t\u0005\u0001\u0006\u0001\u0006\u0004\u0006\u0095\b\u0006\u000b"+
		"\u0006\f\u0006\u0096\u0001\u0006\u0000\u0001\n\u0007\u0000\u0002\u0004"+
		"\u0006\b\n\f\u0000\u0000\u00b4\u0000\u0015\u0001\u0000\u0000\u0000\u0002"+
		"\u001a\u0001\u0000\u0000\u0000\u0004i\u0001\u0000\u0000\u0000\u0006q\u0001"+
		"\u0000\u0000\u0000\bt\u0001\u0000\u0000\u0000\n\u0088\u0001\u0000\u0000"+
		"\u0000\f\u0092\u0001\u0000\u0000\u0000\u000e\u0016\u0003\u0004\u0002\u0000"+
		"\u000f\u0011\u0003\u0004\u0002\u0000\u0010\u0012\u0003\u0002\u0001\u0000"+
		"\u0011\u0010\u0001\u0000\u0000\u0000\u0012\u0013\u0001\u0000\u0000\u0000"+
		"\u0013\u0011\u0001\u0000\u0000\u0000\u0013\u0014\u0001\u0000\u0000\u0000"+
		"\u0014\u0016\u0001\u0000\u0000\u0000\u0015\u000e\u0001\u0000\u0000\u0000"+
		"\u0015\u000f\u0001\u0000\u0000\u0000\u0016\u0001\u0001\u0000\u0000\u0000"+
		"\u0017\u001b\u0003\n\u0005\u0000\u0018\u001b\u0003\f\u0006\u0000\u0019"+
		"\u001b\u0003\u0004\u0002\u0000\u001a\u0017\u0001\u0000\u0000\u0000\u001a"+
		"\u0018\u0001\u0000\u0000\u0000\u001a\u0019\u0001\u0000\u0000\u0000\u001b"+
		"\u0003\u0001\u0000\u0000\u0000\u001c\u001e\u0003\u0006\u0003\u0000\u001d"+
		"\u001c\u0001\u0000\u0000\u0000\u001e\u001f\u0001\u0000\u0000\u0000\u001f"+
		"\u001d\u0001\u0000\u0000\u0000\u001f \u0001\u0000\u0000\u0000 !\u0001"+
		"\u0000\u0000\u0000!\"\u0003\b\u0004\u0000\"#\u0003\f\u0006\u0000#j\u0001"+
		"\u0000\u0000\u0000$&\u0003\u0006\u0003\u0000%$\u0001\u0000\u0000\u0000"+
		"&\'\u0001\u0000\u0000\u0000\'%\u0001\u0000\u0000\u0000\'(\u0001\u0000"+
		"\u0000\u0000()\u0001\u0000\u0000\u0000)*\u0003\f\u0006\u0000*+\u0003\b"+
		"\u0004\u0000+j\u0001\u0000\u0000\u0000,.\u0003\b\u0004\u0000-/\u0003\u0006"+
		"\u0003\u0000.-\u0001\u0000\u0000\u0000/0\u0001\u0000\u0000\u00000.\u0001"+
		"\u0000\u0000\u000001\u0001\u0000\u0000\u000012\u0001\u0000\u0000\u0000"+
		"23\u0003\f\u0006\u00003j\u0001\u0000\u0000\u000045\u0003\b\u0004\u0000"+
		"57\u0003\f\u0006\u000068\u0003\u0006\u0003\u000076\u0001\u0000\u0000\u0000"+
		"89\u0001\u0000\u0000\u000097\u0001\u0000\u0000\u00009:\u0001\u0000\u0000"+
		"\u0000:j\u0001\u0000\u0000\u0000;=\u0003\f\u0006\u0000<>\u0003\u0006\u0003"+
		"\u0000=<\u0001\u0000\u0000\u0000>?\u0001\u0000\u0000\u0000?=\u0001\u0000"+
		"\u0000\u0000?@\u0001\u0000\u0000\u0000@A\u0001\u0000\u0000\u0000AB\u0003"+
		"\b\u0004\u0000Bj\u0001\u0000\u0000\u0000CD\u0003\f\u0006\u0000DF\u0003"+
		"\b\u0004\u0000EG\u0003\u0006\u0003\u0000FE\u0001\u0000\u0000\u0000GH\u0001"+
		"\u0000\u0000\u0000HF\u0001\u0000\u0000\u0000HI\u0001\u0000\u0000\u0000"+
		"Ij\u0001\u0000\u0000\u0000JL\u0003\u0006\u0003\u0000KJ\u0001\u0000\u0000"+
		"\u0000LM\u0001\u0000\u0000\u0000MK\u0001\u0000\u0000\u0000MN\u0001\u0000"+
		"\u0000\u0000NO\u0001\u0000\u0000\u0000OP\u0003\f\u0006\u0000Pj\u0001\u0000"+
		"\u0000\u0000QS\u0003\f\u0006\u0000RT\u0003\u0006\u0003\u0000SR\u0001\u0000"+
		"\u0000\u0000TU\u0001\u0000\u0000\u0000US\u0001\u0000\u0000\u0000UV\u0001"+
		"\u0000\u0000\u0000Vj\u0001\u0000\u0000\u0000WY\u0003\u0006\u0003\u0000"+
		"XW\u0001\u0000\u0000\u0000YZ\u0001\u0000\u0000\u0000ZX\u0001\u0000\u0000"+
		"\u0000Z[\u0001\u0000\u0000\u0000[\\\u0001\u0000\u0000\u0000\\]\u0003\b"+
		"\u0004\u0000]j\u0001\u0000\u0000\u0000^`\u0003\b\u0004\u0000_a\u0003\u0006"+
		"\u0003\u0000`_\u0001\u0000\u0000\u0000ab\u0001\u0000\u0000\u0000b`\u0001"+
		"\u0000\u0000\u0000bc\u0001\u0000\u0000\u0000cj\u0001\u0000\u0000\u0000"+
		"df\u0003\u0006\u0003\u0000ed\u0001\u0000\u0000\u0000fg\u0001\u0000\u0000"+
		"\u0000ge\u0001\u0000\u0000\u0000gh\u0001\u0000\u0000\u0000hj\u0001\u0000"+
		"\u0000\u0000i\u001d\u0001\u0000\u0000\u0000i%\u0001\u0000\u0000\u0000"+
		"i,\u0001\u0000\u0000\u0000i4\u0001\u0000\u0000\u0000i;\u0001\u0000\u0000"+
		"\u0000iC\u0001\u0000\u0000\u0000iK\u0001\u0000\u0000\u0000iQ\u0001\u0000"+
		"\u0000\u0000iX\u0001\u0000\u0000\u0000i^\u0001\u0000\u0000\u0000ie\u0001"+
		"\u0000\u0000\u0000j\u0005\u0001\u0000\u0000\u0000km\u0005\u0004\u0000"+
		"\u0000lk\u0001\u0000\u0000\u0000lm\u0001\u0000\u0000\u0000mn\u0001\u0000"+
		"\u0000\u0000nr\u0005\u0005\u0000\u0000op\u0005\u0001\u0000\u0000pr\u0005"+
		"\u0006\u0000\u0000ql\u0001\u0000\u0000\u0000qo\u0001\u0000\u0000\u0000"+
		"r\u0007\u0001\u0000\u0000\u0000su\u0005\u0002\u0000\u0000ts\u0001\u0000"+
		"\u0000\u0000tu\u0001\u0000\u0000\u0000uv\u0001\u0000\u0000\u0000vw\u0003"+
		"\n\u0005\u0000w\t\u0001\u0000\u0000\u0000xy\u0006\u0005\uffff\uffff\u0000"+
		"yz\u0005\n\u0000\u0000z\u0089\u0003\n\u0005\u0003{}\u0005\u0007\u0000"+
		"\u0000|~\u0005\u000b\u0000\u0000}|\u0001\u0000\u0000\u0000}~\u0001\u0000"+
		"\u0000\u0000~\u007f\u0001\u0000\u0000\u0000\u007f\u0087\u0005\b\u0000"+
		"\u0000\u0080\u0082\u0005\b\u0000\u0000\u0081\u0083\u0005\u000b\u0000\u0000"+
		"\u0082\u0081\u0001\u0000\u0000\u0000\u0082\u0083\u0001\u0000\u0000\u0000"+
		"\u0083\u0084\u0001\u0000\u0000\u0000\u0084\u0087\u0005\u0007\u0000\u0000"+
		"\u0085\u0087\u0005\b\u0000\u0000\u0086{\u0001\u0000\u0000\u0000\u0086"+
		"\u0080\u0001\u0000\u0000\u0000\u0086\u0085\u0001\u0000\u0000\u0000\u0087"+
		"\u0089\u0001\u0000\u0000\u0000\u0088x\u0001\u0000\u0000\u0000\u0088\u0086"+
		"\u0001\u0000\u0000\u0000\u0089\u008f\u0001\u0000\u0000\u0000\u008a\u008b"+
		"\n\u0002\u0000\u0000\u008b\u008c\u0005\t\u0000\u0000\u008c\u008e\u0003"+
		"\n\u0005\u0003\u008d\u008a\u0001\u0000\u0000\u0000\u008e\u0091\u0001\u0000"+
		"\u0000\u0000\u008f\u008d\u0001\u0000\u0000\u0000\u008f\u0090\u0001\u0000"+
		"\u0000\u0000\u0090\u000b\u0001\u0000\u0000\u0000\u0091\u008f\u0001\u0000"+
		"\u0000\u0000\u0092\u0094\u0005\u0003\u0000\u0000\u0093\u0095\u0005\u0007"+
		"\u0000\u0000\u0094\u0093\u0001\u0000\u0000\u0000\u0095\u0096\u0001\u0000"+
		"\u0000\u0000\u0096\u0094\u0001\u0000\u0000\u0000\u0096\u0097\u0001\u0000"+
		"\u0000\u0000\u0097\r\u0001\u0000\u0000\u0000\u0018\u0013\u0015\u001a\u001f"+
		"\'09?HMUZbgilqt}\u0082\u0086\u0088\u008f\u0096";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}