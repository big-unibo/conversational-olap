// Generated from COOL.g4 by ANTLR 4.5

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
	static { RuntimeMetaData.checkVersion("4.5", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, AGG=4, MEA=5, FACT=6, ATTR=7, VAL=8, BINARY=9, 
		NOT=10, COP=11, WS=12, ERRCHAR=13;
	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] ruleNames = {
		"T__0", "T__1", "T__2", "AGG", "MEA", "FACT", "ATTR", "VAL", "BINARY", 
		"NOT", "COP", "WS", "ERRCHAR"
	};

	private static final String[] _LITERAL_NAMES = {
		null, "'COUNT'", "'WHERE'", "'BY'", "'AGG'", "'MEA'", "'FACT'", "'ATTR'", 
		"'VAL'", null, "'NOT'", "'COP'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, null, null, null, "AGG", "MEA", "FACT", "ATTR", "VAL", "BINARY", 
		"NOT", "COP", "WS", "ERRCHAR"
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
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\2\17Z\b\1\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\3\2\3\2\3\2\3\2\3\2\3\2\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\4\3\4\3\4\3\5\3\5\3\5\3\5\3\6\3\6\3\6\3\6\3\7\3\7\3\7\3\7\3"+
		"\7\3\b\3\b\3\b\3\b\3\b\3\t\3\t\3\t\3\t\3\n\3\n\3\n\3\n\3\n\5\nH\n\n\3"+
		"\13\3\13\3\13\3\13\3\f\3\f\3\f\3\f\3\r\6\rS\n\r\r\r\16\rT\3\r\3\r\3\16"+
		"\3\16\2\2\17\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f\27\r\31\16"+
		"\33\17\3\2\3\5\2\13\f\17\17\"\"[\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2"+
		"\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2"+
		"\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\3\35\3\2\2\2"+
		"\5#\3\2\2\2\7)\3\2\2\2\t,\3\2\2\2\13\60\3\2\2\2\r\64\3\2\2\2\179\3\2\2"+
		"\2\21>\3\2\2\2\23G\3\2\2\2\25I\3\2\2\2\27M\3\2\2\2\31R\3\2\2\2\33X\3\2"+
		"\2\2\35\36\7E\2\2\36\37\7Q\2\2\37 \7W\2\2 !\7P\2\2!\"\7V\2\2\"\4\3\2\2"+
		"\2#$\7Y\2\2$%\7J\2\2%&\7G\2\2&\'\7T\2\2\'(\7G\2\2(\6\3\2\2\2)*\7D\2\2"+
		"*+\7[\2\2+\b\3\2\2\2,-\7C\2\2-.\7I\2\2./\7I\2\2/\n\3\2\2\2\60\61\7O\2"+
		"\2\61\62\7G\2\2\62\63\7C\2\2\63\f\3\2\2\2\64\65\7H\2\2\65\66\7C\2\2\66"+
		"\67\7E\2\2\678\7V\2\28\16\3\2\2\29:\7C\2\2:;\7V\2\2;<\7V\2\2<=\7T\2\2"+
		"=\20\3\2\2\2>?\7X\2\2?@\7C\2\2@A\7N\2\2A\22\3\2\2\2BC\7C\2\2CD\7P\2\2"+
		"DH\7F\2\2EF\7Q\2\2FH\7T\2\2GB\3\2\2\2GE\3\2\2\2H\24\3\2\2\2IJ\7P\2\2J"+
		"K\7Q\2\2KL\7V\2\2L\26\3\2\2\2MN\7E\2\2NO\7Q\2\2OP\7R\2\2P\30\3\2\2\2Q"+
		"S\t\2\2\2RQ\3\2\2\2ST\3\2\2\2TR\3\2\2\2TU\3\2\2\2UV\3\2\2\2VW\b\r\2\2"+
		"W\32\3\2\2\2XY\13\2\2\2Y\34\3\2\2\2\5\2GT\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}