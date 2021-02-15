package it.unibo.conversational.vocalization.main;

import it.unibo.conversational.vocalization.utils.CsvWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import org.apache.commons.lang3.tuple.Pair;

public class Application {

    private static final boolean SAVE_RESULTS = false;

    public static void main(String[] args) throws Exception {
        VolapSession volapSession = VolapSession.getInstance();
        BufferedReader inputReader = new BufferedReader(new InputStreamReader(System.in));
        CsvWriter csvWriter = new CsvWriter(Configuration.OUTPUT_PATH);
        System.out.println("Insert input query");
        String input = inputReader.readLine().trim().toLowerCase();
        while (!input.equals("exit")) {
            try {
                if (!SAVE_RESULTS) {
                    Pair<String, Pair<Double, Double>> output = volapSession.executeQuery(input, true);
                    System.out.println("Average relative error: " + output.getRight().getLeft());
                    System.out.println("Mental model quality: " + output.getRight().getRight());
                    System.out.println();
                } else {
                    System.out.println("Solving using base implementation");
                    Pair<String, Pair<Double, Double>> out1 = volapSession.executeQuery(input, false, true);
                    System.out.println("Average relative error: " + out1.getRight().getLeft());
                    System.out.println("Mental model quality: " + out1.getRight().getRight());
                    System.out.println();
                    System.out.println("Solving using complete implementation");
                    Pair<String, Pair<Double, Double>> out2 = volapSession.executeQuery(input, true, true);
                    System.out.println("Average relative error: " + out2.getRight().getLeft());
                    System.out.println("Mental model quality: " + out2.getRight().getRight());
                    System.out.println();
                    csvWriter.registerLine(Arrays.asList(input, out2.getRight().getLeft(), out2.getRight().getRight(), out1.getRight().getLeft(), out1.getRight().getRight()));
                }
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
                System.out.println();
            }
            System.out.println("Insert input query");
            input = inputReader.readLine().toLowerCase();
        }
        if (SAVE_RESULTS) csvWriter.writeLines();
    }

}
