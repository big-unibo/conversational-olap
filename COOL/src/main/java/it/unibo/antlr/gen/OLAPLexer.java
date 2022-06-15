// Generated from OLAP.g4 by ANTLR 4.10.1

package it.unibo.antlr.gen;

import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class OLAPLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.10.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, AGG=11, MEA=12, FACT=13, ATTR=14, VAL=15, BINARY=16, NOT=17, 
		COP=18, WS=19, ERRCHAR=20;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"T__0", "T__1", "T__2", "T__3", "T__4", "T__5", "T__6", "T__7", "T__8", 
			"T__9", "AGG", "MEA", "FACT", "ATTR", "VAL", "BINARY", "NOT", "COP", 
			"WS", "ERRCHAR"
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


	public OLAPLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "OLAP.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\u0004\u0000\u0014\u0092\u0006\uffff\uffff\u0002\u0000\u0007\u0000\u0002"+
		"\u0001\u0007\u0001\u0002\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002"+
		"\u0004\u0007\u0004\u0002\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002"+
		"\u0007\u0007\u0007\u0002\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002"+
		"\u000b\u0007\u000b\u0002\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e"+
		"\u0002\u000f\u0007\u000f\u0002\u0010\u0007\u0010\u0002\u0011\u0007\u0011"+
		"\u0002\u0012\u0007\u0012\u0002\u0013\u0007\u0013\u0001\u0000\u0001\u0000"+
		"\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0002\u0001\u0002"+
		"\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0003\u0001\u0003\u0001\u0003"+
		"\u0001\u0003\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0005"+
		"\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005"+
		"\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006"+
		"\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007"+
		"\u0001\b\u0001\b\u0001\b\u0001\b\u0001\b\u0001\b\u0001\t\u0001\t\u0001"+
		"\t\u0001\n\u0001\n\u0001\n\u0001\n\u0001\u000b\u0001\u000b\u0001\u000b"+
		"\u0001\u000b\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\r\u0001\r\u0001"+
		"\r\u0001\r\u0001\r\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0001"+
		"\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0003\u000f\u0080"+
		"\b\u000f\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0011\u0001"+
		"\u0011\u0001\u0011\u0001\u0011\u0001\u0012\u0004\u0012\u008b\b\u0012\u000b"+
		"\u0012\f\u0012\u008c\u0001\u0012\u0001\u0012\u0001\u0013\u0001\u0013\u0000"+
		"\u0000\u0014\u0001\u0001\u0003\u0002\u0005\u0003\u0007\u0004\t\u0005\u000b"+
		"\u0006\r\u0007\u000f\b\u0011\t\u0013\n\u0015\u000b\u0017\f\u0019\r\u001b"+
		"\u000e\u001d\u000f\u001f\u0010!\u0011#\u0012%\u0013\'\u0014\u0001\u0000"+
		"\u0001\u0003\u0000\t\n\r\r  \u0093\u0000\u0001\u0001\u0000\u0000\u0000"+
		"\u0000\u0003\u0001\u0000\u0000\u0000\u0000\u0005\u0001\u0000\u0000\u0000"+
		"\u0000\u0007\u0001\u0000\u0000\u0000\u0000\t\u0001\u0000\u0000\u0000\u0000"+
		"\u000b\u0001\u0000\u0000\u0000\u0000\r\u0001\u0000\u0000\u0000\u0000\u000f"+
		"\u0001\u0000\u0000\u0000\u0000\u0011\u0001\u0000\u0000\u0000\u0000\u0013"+
		"\u0001\u0000\u0000\u0000\u0000\u0015\u0001\u0000\u0000\u0000\u0000\u0017"+
		"\u0001\u0000\u0000\u0000\u0000\u0019\u0001\u0000\u0000\u0000\u0000\u001b"+
		"\u0001\u0000\u0000\u0000\u0000\u001d\u0001\u0000\u0000\u0000\u0000\u001f"+
		"\u0001\u0000\u0000\u0000\u0000!\u0001\u0000\u0000\u0000\u0000#\u0001\u0000"+
		"\u0000\u0000\u0000%\u0001\u0000\u0000\u0000\u0000\'\u0001\u0000\u0000"+
		"\u0000\u0001)\u0001\u0000\u0000\u0000\u00031\u0001\u0000\u0000\u0000\u0005"+
		";\u0001\u0000\u0000\u0000\u0007@\u0001\u0000\u0000\u0000\tD\u0001\u0000"+
		"\u0000\u0000\u000bH\u0001\u0000\u0000\u0000\rO\u0001\u0000\u0000\u0000"+
		"\u000fU\u0001\u0000\u0000\u0000\u0011[\u0001\u0000\u0000\u0000\u0013a"+
		"\u0001\u0000\u0000\u0000\u0015d\u0001\u0000\u0000\u0000\u0017h\u0001\u0000"+
		"\u0000\u0000\u0019l\u0001\u0000\u0000\u0000\u001bq\u0001\u0000\u0000\u0000"+
		"\u001dv\u0001\u0000\u0000\u0000\u001f\u007f\u0001\u0000\u0000\u0000!\u0081"+
		"\u0001\u0000\u0000\u0000#\u0085\u0001\u0000\u0000\u0000%\u008a\u0001\u0000"+
		"\u0000\u0000\'\u0090\u0001\u0000\u0000\u0000)*\u0005R\u0000\u0000*+\u0005"+
		"E\u0000\u0000+,\u0005P\u0000\u0000,-\u0005L\u0000\u0000-.\u0005A\u0000"+
		"\u0000./\u0005C\u0000\u0000/0\u0005E\u0000\u00000\u0002\u0001\u0000\u0000"+
		"\u000012\u0005A\u0000\u000023\u0005C\u0000\u000034\u0005C\u0000\u0000"+
		"45\u0005E\u0000\u000056\u0005S\u0000\u000067\u0005S\u0000\u000078\u0005"+
		"O\u0000\u000089\u0005R\u0000\u00009:\u0005Y\u0000\u0000:\u0004\u0001\u0000"+
		"\u0000\u0000;<\u0005D\u0000\u0000<=\u0005R\u0000\u0000=>\u0005O\u0000"+
		"\u0000>?\u0005P\u0000\u0000?\u0006\u0001\u0000\u0000\u0000@A\u0005A\u0000"+
		"\u0000AB\u0005D\u0000\u0000BC\u0005D\u0000\u0000C\b\u0001\u0000\u0000"+
		"\u0000DE\u0005S\u0000\u0000EF\u0005A\u0000\u0000FG\u0005D\u0000\u0000"+
		"G\n\u0001\u0000\u0000\u0000HI\u0005R\u0000\u0000IJ\u0005O\u0000\u0000"+
		"JK\u0005L\u0000\u0000KL\u0005L\u0000\u0000LM\u0005U\u0000\u0000MN\u0005"+
		"P\u0000\u0000N\f\u0001\u0000\u0000\u0000OP\u0005D\u0000\u0000PQ\u0005"+
		"R\u0000\u0000QR\u0005I\u0000\u0000RS\u0005L\u0000\u0000ST\u0005L\u0000"+
		"\u0000T\u000e\u0001\u0000\u0000\u0000UV\u0005C\u0000\u0000VW\u0005O\u0000"+
		"\u0000WX\u0005U\u0000\u0000XY\u0005N\u0000\u0000YZ\u0005T\u0000\u0000"+
		"Z\u0010\u0001\u0000\u0000\u0000[\\\u0005W\u0000\u0000\\]\u0005H\u0000"+
		"\u0000]^\u0005E\u0000\u0000^_\u0005R\u0000\u0000_`\u0005E\u0000\u0000"+
		"`\u0012\u0001\u0000\u0000\u0000ab\u0005B\u0000\u0000bc\u0005Y\u0000\u0000"+
		"c\u0014\u0001\u0000\u0000\u0000de\u0005A\u0000\u0000ef\u0005G\u0000\u0000"+
		"fg\u0005G\u0000\u0000g\u0016\u0001\u0000\u0000\u0000hi\u0005M\u0000\u0000"+
		"ij\u0005E\u0000\u0000jk\u0005A\u0000\u0000k\u0018\u0001\u0000\u0000\u0000"+
		"lm\u0005F\u0000\u0000mn\u0005A\u0000\u0000no\u0005C\u0000\u0000op\u0005"+
		"T\u0000\u0000p\u001a\u0001\u0000\u0000\u0000qr\u0005A\u0000\u0000rs\u0005"+
		"T\u0000\u0000st\u0005T\u0000\u0000tu\u0005R\u0000\u0000u\u001c\u0001\u0000"+
		"\u0000\u0000vw\u0005V\u0000\u0000wx\u0005A\u0000\u0000xy\u0005L\u0000"+
		"\u0000y\u001e\u0001\u0000\u0000\u0000z{\u0005A\u0000\u0000{|\u0005N\u0000"+
		"\u0000|\u0080\u0005D\u0000\u0000}~\u0005O\u0000\u0000~\u0080\u0005R\u0000"+
		"\u0000\u007fz\u0001\u0000\u0000\u0000\u007f}\u0001\u0000\u0000\u0000\u0080"+
		" \u0001\u0000\u0000\u0000\u0081\u0082\u0005N\u0000\u0000\u0082\u0083\u0005"+
		"O\u0000\u0000\u0083\u0084\u0005T\u0000\u0000\u0084\"\u0001\u0000\u0000"+
		"\u0000\u0085\u0086\u0005C\u0000\u0000\u0086\u0087\u0005O\u0000\u0000\u0087"+
		"\u0088\u0005P\u0000\u0000\u0088$\u0001\u0000\u0000\u0000\u0089\u008b\u0007"+
		"\u0000\u0000\u0000\u008a\u0089\u0001\u0000\u0000\u0000\u008b\u008c\u0001"+
		"\u0000\u0000\u0000\u008c\u008a\u0001\u0000\u0000\u0000\u008c\u008d\u0001"+
		"\u0000\u0000\u0000\u008d\u008e\u0001\u0000\u0000\u0000\u008e\u008f\u0006"+
		"\u0012\u0000\u0000\u008f&\u0001\u0000\u0000\u0000\u0090\u0091\t\u0000"+
		"\u0000\u0000\u0091(\u0001\u0000\u0000\u0000\u0003\u0000\u007f\u008c\u0001"+
		"\u0006\u0000\u0000";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}