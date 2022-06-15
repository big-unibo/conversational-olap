// Generated from COOL.g4 by ANTLR 4.10.1

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
public class COOLLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.10.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, AGG=4, MEA=5, FACT=6, ATTR=7, VAL=8, BINARY=9, 
		NOT=10, COP=11, WS=12, ERRCHAR=13;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"T__0", "T__1", "T__2", "AGG", "MEA", "FACT", "ATTR", "VAL", "BINARY", 
			"NOT", "COP", "WS", "ERRCHAR"
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


	public COOLLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "COOL.g4"; }

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
		"\u0004\u0000\rX\u0006\uffff\uffff\u0002\u0000\u0007\u0000\u0002\u0001"+
		"\u0007\u0001\u0002\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004"+
		"\u0007\u0004\u0002\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007"+
		"\u0007\u0007\u0002\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b"+
		"\u0007\u000b\u0002\f\u0007\f\u0001\u0000\u0001\u0000\u0001\u0000\u0001"+
		"\u0000\u0001\u0000\u0001\u0000\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0002\u0001\u0002\u0001\u0002\u0001"+
		"\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0004\u0001\u0004\u0001"+
		"\u0004\u0001\u0004\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001"+
		"\u0005\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001"+
		"\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001\b\u0001\b\u0001\b\u0001"+
		"\b\u0001\b\u0003\bF\b\b\u0001\t\u0001\t\u0001\t\u0001\t\u0001\n\u0001"+
		"\n\u0001\n\u0001\n\u0001\u000b\u0004\u000bQ\b\u000b\u000b\u000b\f\u000b"+
		"R\u0001\u000b\u0001\u000b\u0001\f\u0001\f\u0000\u0000\r\u0001\u0001\u0003"+
		"\u0002\u0005\u0003\u0007\u0004\t\u0005\u000b\u0006\r\u0007\u000f\b\u0011"+
		"\t\u0013\n\u0015\u000b\u0017\f\u0019\r\u0001\u0000\u0001\u0003\u0000\t"+
		"\n\r\r  Y\u0000\u0001\u0001\u0000\u0000\u0000\u0000\u0003\u0001\u0000"+
		"\u0000\u0000\u0000\u0005\u0001\u0000\u0000\u0000\u0000\u0007\u0001\u0000"+
		"\u0000\u0000\u0000\t\u0001\u0000\u0000\u0000\u0000\u000b\u0001\u0000\u0000"+
		"\u0000\u0000\r\u0001\u0000\u0000\u0000\u0000\u000f\u0001\u0000\u0000\u0000"+
		"\u0000\u0011\u0001\u0000\u0000\u0000\u0000\u0013\u0001\u0000\u0000\u0000"+
		"\u0000\u0015\u0001\u0000\u0000\u0000\u0000\u0017\u0001\u0000\u0000\u0000"+
		"\u0000\u0019\u0001\u0000\u0000\u0000\u0001\u001b\u0001\u0000\u0000\u0000"+
		"\u0003!\u0001\u0000\u0000\u0000\u0005\'\u0001\u0000\u0000\u0000\u0007"+
		"*\u0001\u0000\u0000\u0000\t.\u0001\u0000\u0000\u0000\u000b2\u0001\u0000"+
		"\u0000\u0000\r7\u0001\u0000\u0000\u0000\u000f<\u0001\u0000\u0000\u0000"+
		"\u0011E\u0001\u0000\u0000\u0000\u0013G\u0001\u0000\u0000\u0000\u0015K"+
		"\u0001\u0000\u0000\u0000\u0017P\u0001\u0000\u0000\u0000\u0019V\u0001\u0000"+
		"\u0000\u0000\u001b\u001c\u0005C\u0000\u0000\u001c\u001d\u0005O\u0000\u0000"+
		"\u001d\u001e\u0005U\u0000\u0000\u001e\u001f\u0005N\u0000\u0000\u001f "+
		"\u0005T\u0000\u0000 \u0002\u0001\u0000\u0000\u0000!\"\u0005W\u0000\u0000"+
		"\"#\u0005H\u0000\u0000#$\u0005E\u0000\u0000$%\u0005R\u0000\u0000%&\u0005"+
		"E\u0000\u0000&\u0004\u0001\u0000\u0000\u0000\'(\u0005B\u0000\u0000()\u0005"+
		"Y\u0000\u0000)\u0006\u0001\u0000\u0000\u0000*+\u0005A\u0000\u0000+,\u0005"+
		"G\u0000\u0000,-\u0005G\u0000\u0000-\b\u0001\u0000\u0000\u0000./\u0005"+
		"M\u0000\u0000/0\u0005E\u0000\u000001\u0005A\u0000\u00001\n\u0001\u0000"+
		"\u0000\u000023\u0005F\u0000\u000034\u0005A\u0000\u000045\u0005C\u0000"+
		"\u000056\u0005T\u0000\u00006\f\u0001\u0000\u0000\u000078\u0005A\u0000"+
		"\u000089\u0005T\u0000\u00009:\u0005T\u0000\u0000:;\u0005R\u0000\u0000"+
		";\u000e\u0001\u0000\u0000\u0000<=\u0005V\u0000\u0000=>\u0005A\u0000\u0000"+
		">?\u0005L\u0000\u0000?\u0010\u0001\u0000\u0000\u0000@A\u0005A\u0000\u0000"+
		"AB\u0005N\u0000\u0000BF\u0005D\u0000\u0000CD\u0005O\u0000\u0000DF\u0005"+
		"R\u0000\u0000E@\u0001\u0000\u0000\u0000EC\u0001\u0000\u0000\u0000F\u0012"+
		"\u0001\u0000\u0000\u0000GH\u0005N\u0000\u0000HI\u0005O\u0000\u0000IJ\u0005"+
		"T\u0000\u0000J\u0014\u0001\u0000\u0000\u0000KL\u0005C\u0000\u0000LM\u0005"+
		"O\u0000\u0000MN\u0005P\u0000\u0000N\u0016\u0001\u0000\u0000\u0000OQ\u0007"+
		"\u0000\u0000\u0000PO\u0001\u0000\u0000\u0000QR\u0001\u0000\u0000\u0000"+
		"RP\u0001\u0000\u0000\u0000RS\u0001\u0000\u0000\u0000ST\u0001\u0000\u0000"+
		"\u0000TU\u0006\u000b\u0000\u0000U\u0018\u0001\u0000\u0000\u0000VW\t\u0000"+
		"\u0000\u0000W\u001a\u0001\u0000\u0000\u0000\u0003\u0000ER\u0001\u0006"+
		"\u0000\u0000";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}