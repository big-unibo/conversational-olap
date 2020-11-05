// Generated from OLAP.g4 by ANTLR 4.5

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
	static { RuntimeMetaData.checkVersion("4.5", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, AGG=11, MEA=12, FACT=13, ATTR=14, VAL=15, BINARY=16, NOT=17, 
		COP=18, WS=19, ERRCHAR=20;
	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] ruleNames = {
		"T__0", "T__1", "T__2", "T__3", "T__4", "T__5", "T__6", "T__7", "T__8", 
		"T__9", "AGG", "MEA", "FACT", "ATTR", "VAL", "BINARY", "NOT", "COP", "WS", 
		"ERRCHAR"
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
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\2\26\u0094\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"+
		"\t\22\4\23\t\23\4\24\t\24\4\25\t\25\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\4\3\4\3\4\3\4\3\4\3\5\3\5\3\5"+
		"\3\5\3\6\3\6\3\6\3\6\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\b\3\b\3\b\3\b\3\b\3"+
		"\b\3\t\3\t\3\t\3\t\3\t\3\t\3\n\3\n\3\n\3\n\3\n\3\n\3\13\3\13\3\13\3\f"+
		"\3\f\3\f\3\f\3\r\3\r\3\r\3\r\3\16\3\16\3\16\3\16\3\16\3\17\3\17\3\17\3"+
		"\17\3\17\3\20\3\20\3\20\3\20\3\21\3\21\3\21\3\21\3\21\5\21\u0082\n\21"+
		"\3\22\3\22\3\22\3\22\3\23\3\23\3\23\3\23\3\24\6\24\u008d\n\24\r\24\16"+
		"\24\u008e\3\24\3\24\3\25\3\25\2\2\26\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21"+
		"\n\23\13\25\f\27\r\31\16\33\17\35\20\37\21!\22#\23%\24\'\25)\26\3\2\3"+
		"\5\2\13\f\17\17\"\"\u0095\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2"+
		"\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2"+
		"\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3"+
		"\2\2\2\2!\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2\2\2\2)\3\2\2\2\3+\3\2"+
		"\2\2\5\63\3\2\2\2\7=\3\2\2\2\tB\3\2\2\2\13F\3\2\2\2\rJ\3\2\2\2\17Q\3\2"+
		"\2\2\21W\3\2\2\2\23]\3\2\2\2\25c\3\2\2\2\27f\3\2\2\2\31j\3\2\2\2\33n\3"+
		"\2\2\2\35s\3\2\2\2\37x\3\2\2\2!\u0081\3\2\2\2#\u0083\3\2\2\2%\u0087\3"+
		"\2\2\2\'\u008c\3\2\2\2)\u0092\3\2\2\2+,\7T\2\2,-\7G\2\2-.\7R\2\2./\7N"+
		"\2\2/\60\7C\2\2\60\61\7E\2\2\61\62\7G\2\2\62\4\3\2\2\2\63\64\7C\2\2\64"+
		"\65\7E\2\2\65\66\7E\2\2\66\67\7G\2\2\678\7U\2\289\7U\2\29:\7Q\2\2:;\7"+
		"T\2\2;<\7[\2\2<\6\3\2\2\2=>\7F\2\2>?\7T\2\2?@\7Q\2\2@A\7R\2\2A\b\3\2\2"+
		"\2BC\7C\2\2CD\7F\2\2DE\7F\2\2E\n\3\2\2\2FG\7U\2\2GH\7C\2\2HI\7F\2\2I\f"+
		"\3\2\2\2JK\7T\2\2KL\7Q\2\2LM\7N\2\2MN\7N\2\2NO\7W\2\2OP\7R\2\2P\16\3\2"+
		"\2\2QR\7F\2\2RS\7T\2\2ST\7K\2\2TU\7N\2\2UV\7N\2\2V\20\3\2\2\2WX\7E\2\2"+
		"XY\7Q\2\2YZ\7W\2\2Z[\7P\2\2[\\\7V\2\2\\\22\3\2\2\2]^\7Y\2\2^_\7J\2\2_"+
		"`\7G\2\2`a\7T\2\2ab\7G\2\2b\24\3\2\2\2cd\7D\2\2de\7[\2\2e\26\3\2\2\2f"+
		"g\7C\2\2gh\7I\2\2hi\7I\2\2i\30\3\2\2\2jk\7O\2\2kl\7G\2\2lm\7C\2\2m\32"+
		"\3\2\2\2no\7H\2\2op\7C\2\2pq\7E\2\2qr\7V\2\2r\34\3\2\2\2st\7C\2\2tu\7"+
		"V\2\2uv\7V\2\2vw\7T\2\2w\36\3\2\2\2xy\7X\2\2yz\7C\2\2z{\7N\2\2{ \3\2\2"+
		"\2|}\7C\2\2}~\7P\2\2~\u0082\7F\2\2\177\u0080\7Q\2\2\u0080\u0082\7T\2\2"+
		"\u0081|\3\2\2\2\u0081\177\3\2\2\2\u0082\"\3\2\2\2\u0083\u0084\7P\2\2\u0084"+
		"\u0085\7Q\2\2\u0085\u0086\7V\2\2\u0086$\3\2\2\2\u0087\u0088\7E\2\2\u0088"+
		"\u0089\7Q\2\2\u0089\u008a\7R\2\2\u008a&\3\2\2\2\u008b\u008d\t\2\2\2\u008c"+
		"\u008b\3\2\2\2\u008d\u008e\3\2\2\2\u008e\u008c\3\2\2\2\u008e\u008f\3\2"+
		"\2\2\u008f\u0090\3\2\2\2\u0090\u0091\b\24\2\2\u0091(\3\2\2\2\u0092\u0093"+
		"\13\2\2\2\u0093*\3\2\2\2\5\2\u0081\u008e\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}