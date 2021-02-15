package it.unibo.conversational.vocalization.query;

import it.unibo.conversational.vocalization.data.Dimension;
import it.unibo.conversational.vocalization.data.Measure;
import it.unibo.conversational.vocalization.data.Member;
import it.unibo.conversational.vocalization.query.Aggregate.Function;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.regex.Pattern;

public class QueryValidator {

    public static Optional<Query> validateInput(String input, List<Measure> measures, List<Dimension> dimensions) {
        input = input.toLowerCase().trim();
        if (input.contains("from")) {
            int last = input.contains("where") ? input.indexOf("where") : (input.contains("group by") ? input.indexOf("group by") : input.length());
            input = input.substring(0, input.indexOf("from")) + input.substring(last);
        }
        if (!input.startsWith("select")) return Optional.empty();
        input = input.replaceFirst(Pattern.quote("select"), "").trim();
        Optional<Function> function = Optional.empty();
        Optional<Measure> measure = Optional.empty();
        while (!input.startsWith("where") && !input.startsWith("group by") && !input.isEmpty()) {
            String input1 = input;
            Optional<Function> found = Arrays.stream(Function.values()).filter(f -> input1.startsWith(f.name().toLowerCase())).findFirst();
            if (found.isPresent()) {
                function = found;
                input = input.replaceFirst(Pattern.quote(function.get().name().toLowerCase()), "").trim();
                String input2 = input;
                measure = measures.stream().filter(m -> input2.startsWith("(" + m.dbName.toLowerCase() + ")")).findFirst();
                if (measure.isPresent()) input = input.replaceFirst(Pattern.quote("(" + measure.get().dbName.toLowerCase() + ")"), "").trim();
            } else {
                input = input.substring(1);
            }
        }
        if (function.isEmpty() || measure.isEmpty()) return Optional.empty();
        Map<Dimension, Member> where = new HashMap<>();
        if (input.startsWith("where")) {
            input = input.replaceFirst(Pattern.quote("where"), "").trim();
            do {
                if (input.startsWith("and")) input = input.replaceFirst(Pattern.quote("and"), "").trim();
                String input1 = input;
                Optional<Dimension> dimension = dimensions.stream().filter(d -> input1.startsWith(d.dbName.toLowerCase() + ".")).findFirst();
                if (dimension.isEmpty()) return Optional.empty();
                input = input.replaceFirst(Pattern.quote(dimension.get().dbName.toLowerCase() + "."), "").trim();
                String input2 = input;
                Optional<String> level = dimension.get().levelDbNames.stream().filter(l -> input2.startsWith(l.toLowerCase())).findFirst();
                if (level.isEmpty()) return Optional.empty();
                input = input.replaceFirst(Pattern.quote(level.get().toLowerCase()), "").trim();
                if (!input.startsWith("=")) return Optional.empty();
                input = input.replaceFirst(Pattern.quote("="), "").trim();
                String input3 = input;
                int l = dimension.get().levelDbNames.indexOf(level.get()) + 1;
                Optional<Member> member = dimension.get().membersByLevelAndName.get(l).entrySet().stream()
                        .filter(e -> input3.startsWith("'" + e.getKey().toLowerCase() + "'")).findFirst().map(Entry::getValue);
                if (member.isEmpty()) return Optional.empty();
                input = input.replaceFirst(Pattern.quote("'" + member.get().dbName.toLowerCase() + "'"), "").trim();
                where.put(dimension.get(), member.get());
            } while (input.startsWith("and"));
        }
        Map<Dimension, Integer> groupBy = new HashMap<>();
        if (input.startsWith("group by")) {
            input = input.replaceFirst(Pattern.quote("group by"), "").trim();
            do {
                if (input.startsWith(",")) input = input.replaceFirst(Pattern.quote(","), "").trim();
                String input1 = input;
                Optional<Dimension> dimension = dimensions.stream().filter(d -> input1.startsWith(d.dbName.toLowerCase() + ".")).findFirst();
                if (dimension.isEmpty()) return Optional.empty();
                input = input.replaceFirst(Pattern.quote(dimension.get().dbName.toLowerCase() + "."), "").trim();
                String input2 = input;
                Optional<String> level = dimension.get().levelDbNames.stream().filter(l -> input2.startsWith(l.toLowerCase())).findFirst();
                if (level.isEmpty()) return Optional.empty();
                input = input.replaceFirst(Pattern.quote(level.get().toLowerCase()), "").trim();
                groupBy.put(dimension.get(), dimension.get().levelDbNames.indexOf(level.get()) + 1);
            } while (input.startsWith(","));
        }
        Query query = new Query(new Aggregate(function.get(), measure.get()), groupBy, where);
        return input.isEmpty() ? Optional.of(query) : Optional.empty();
    }

}
