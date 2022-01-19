package it.unibo.trummervocalization.main;

import it.unibo.trummervocalization.utils.CsvWriter;
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
                    System.out.println("Vocalization time: " + VolapSession.speechTime(output.getLeft()) + " ms");
                    System.out.println();
                } else {
                    // Comment to solve with variable refinements
                    System.out.println("Solving using base implementation");
                    Pair<String, Pair<Double, Double>> out1 = volapSession.executeQuery(input, false, true, Configuration.N_REFINEMENTS);
                    long time1 = VolapSession.speechTime(out1.getLeft());
                    System.out.println("Average relative error: " + out1.getRight().getLeft());
                    System.out.println("Mental model quality: " + out1.getRight().getRight());
                    System.out.println("Vocalization time: " + time1 + " ms");
                    System.out.println();
                    System.out.println("Solving using complete implementation");
                    Pair<String, Pair<Double, Double>> out2 = volapSession.executeQuery(input, true, true, Configuration.N_REFINEMENTS);
                    long time2 = VolapSession.speechTime(out2.getLeft());
                    System.out.println("Average relative error: " + out2.getRight().getLeft());
                    System.out.println("Mental model quality: " + out2.getRight().getRight());
                    System.out.println("Vocalization time: " + time2 + " ms");
                    System.out.println();
                    csvWriter.registerLine(Arrays.asList(input, out2.getRight().getLeft(), out2.getRight().getRight(), time2,
                            out1.getRight().getLeft(), out1.getRight().getRight(), time1));
                    // Uncomment to solve with variable refinements
                    /*System.out.println("Solving using 1 refinements");
                    Pair<String, Pair<Double, Double>> out1 = volapSession.executeQuery(input, true, true, 1);
                    long time1 = VolapSession.speechTime(out1.getLeft());
                    System.out.println("Average relative error: " + out1.getRight().getLeft());
                    System.out.println("Mental model quality: " + out1.getRight().getRight());
                    System.out.println("Vocalization time: " + time1 + " ms");
                    System.out.println();
                    System.out.println("Solving using 2 refinements");
                    Pair<String, Pair<Double, Double>> out2 = volapSession.executeQuery(input, true, true, 2);
                    long time2 = VolapSession.speechTime(out2.getLeft());
                    System.out.println("Average relative error: " + out2.getRight().getLeft());
                    System.out.println("Mental model quality: " + out2.getRight().getRight());
                    System.out.println("Vocalization time: " + time2 + " ms");
                    System.out.println();
                    System.out.println("Solving using 3 refinements");
                    Pair<String, Pair<Double, Double>> out3 = volapSession.executeQuery(input, true, true, 3);
                    long time3 = VolapSession.speechTime(out3.getLeft());
                    System.out.println("Average relative error: " + out3.getRight().getLeft());
                    System.out.println("Mental model quality: " + out3.getRight().getRight());
                    System.out.println("Vocalization time: " + time3 + " ms");
                    System.out.println();
                    System.out.println("Solving using 4 refinements");
                    Pair<String, Pair<Double, Double>> out4 = volapSession.executeQuery(input, true, true, 4);
                    long time4 = VolapSession.speechTime(out4.getLeft());
                    System.out.println("Average relative error: " + out4.getRight().getLeft());
                    System.out.println("Mental model quality: " + out4.getRight().getRight());
                    System.out.println("Vocalization time: " + time4 + " ms");
                    System.out.println();
                    csvWriter.registerLine(Arrays.asList(input,
                            out1.getRight().getLeft(), out1.getRight().getRight(), time1,
                            out2.getRight().getLeft(), out2.getRight().getRight(), time2,
                            out3.getRight().getLeft(), out3.getRight().getRight(), time3,
                            out4.getRight().getLeft(), out4.getRight().getRight(), time4));*/
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
