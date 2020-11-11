package it.unibo.conversational.algorithms;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import it.unibo.conversational.Utils;
import org.jgrapht.alg.util.Pair;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * Match between two strings. Marriage problem.
 */
public final class MarriageProblem {
  private MarriageProblem() {
  }

  /**
   * @param token a token
   * @param tokens a list of tokens
   * @return list of tokens sorted by similarity to token
   */
  private static List<String> getListPreferences(final String token, final List<String> tokens) {
    return tokens.stream()
            .map(s -> Pair.of(s, Utils.tokenSimilarity(token, s)))
            .sorted((a , b) -> -Double.compare(a.getSecond(), b.getSecond()))
            .map(Pair::getFirst)
            .collect(Collectors.toList());
    // final List<String> tmp = Lists.newArrayList(tokens);
    // tmp.sort((String o1, String o2) -> -Double.compare(Utils.tokenSimilarity(token, o1), Utils.tokenSimilarity(token, o2)));
    // return tmp;
  }

  private static Map<String, String> match(final List<String> guys, final Map<String, List<String>> guyPrefers, final Map<String, List<String>> girlPrefers) {
    Map<String, String> engagedTo = new TreeMap<>();
    List<String> freeGuys = new LinkedList<>(guys);
    while (!freeGuys.isEmpty()) {
      String thisGuy = freeGuys.remove(0); // get a load of THIS guy
      List<String> thisGuyPrefers = guyPrefers.get(thisGuy);
      for (String girl : thisGuyPrefers) {
        if (engagedTo.get(girl) == null) { // girl is free
          engagedTo.put(girl, thisGuy); // awww
          break;
        } else {
          String otherGuy = engagedTo.get(girl);
          List<String> thisGirlPrefers = girlPrefers.get(girl);
          if (thisGirlPrefers.indexOf(thisGuy) < thisGirlPrefers.indexOf(otherGuy)) {
            // this girl prefers this guy to the guy she's engaged to
            engagedTo.put(girl, thisGuy);
            freeGuys.add(otherGuy);
            break;
          } // else no change...keep looking for this guy
        }
      }
    }
    return engagedTo;
  }

  /**
   * Risoluzione del matching problem tra due insiemi di stringhe.
   * @param first a string
   * @param second another string
   * @return match indexes
   */
  public static Map<Integer, Integer> getBestMatch(final List<String> first, final List<String> second) {
    final Map<Integer, Integer> res = Maps.newLinkedHashMap();
    final Map<String, List<String>> firstPreferences = Maps.newLinkedHashMap();
    final Map<String, List<String>> secondPreferences = Maps.newLinkedHashMap();
    first.forEach(s -> firstPreferences.put(s, getListPreferences(s, second)));
    second.forEach(s -> secondPreferences.put(s, getListPreferences(s, first)));
    final Map<String, String> matches = match(first, firstPreferences, secondPreferences);
    for (final String f : matches.keySet()) {
      res.put(first.indexOf(matches.get(f)), second.indexOf(f));
    }
    return res;
  }
}
