package it.unibo.conversational.vocalization.main;

import it.unibo.conversational.vocalization.utils.CsvWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.apache.commons.lang3.tuple.Pair;

public class Application {

    private static final boolean DEBUG_SESSION = false;

    public static void main(String[] args) throws Exception {
        long startMillis = System.currentTimeMillis();
        VolapSession volapSession = VolapSession.initialize(args[0], args[1], args[2], args[3], args[4]);
        printTimeLog("Initialized cache", startMillis);

        BufferedReader inputReader = new BufferedReader(new InputStreamReader(System.in));
        CsvWriter csvWriter = new CsvWriter();
        System.out.println("Insert input query");
        String input = inputReader.readLine().trim().toLowerCase();
        while (!input.equals("exit")) {
            try {
                startMillis = System.currentTimeMillis();
                if (!DEBUG_SESSION) {
                    Pair<String, Pair<Double, Double>> output = volapSession.executeQuery(input, true);
                    printTimeLog("Evaluated query", startMillis);
                    System.out.println("Average relative error: " + output.getRight().getLeft());
                    System.out.println("Mental model quality: " + output.getRight().getRight());
                    System.out.println();
                } else {
                    System.out.println("Solving using base implementation");
                    Pair<String, Pair<Double, Double>> out1 = volapSession.executeQuery(input, false, true);
                    printTimeLog("Evaluated query", startMillis);
                    System.out.println("Average relative error: " + out1.getRight().getLeft());
                    System.out.println("Mental model quality: " + out1.getRight().getRight());
                    System.out.println();
                    System.out.println("Solving using complete implementation");
                    Pair<String, Pair<Double, Double>> out2 = volapSession.executeQuery(input, true, true);
                    printTimeLog("Evaluated query", startMillis);
                    System.out.println("Average relative error: " + out2.getRight().getLeft());
                    System.out.println("Mental model quality: " + out2.getRight().getRight());
                    System.out.println();
                    csvWriter.registerLine(input, "", "", out1.getRight().getLeft().toString(),
                            out1.getRight().getRight().toString(), out2.getRight().getLeft().toString(), out2.getRight().getRight().toString());
                }
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
                System.out.println();
            }
            System.out.println("Insert input query");
            input = inputReader.readLine().toLowerCase();
        }

        if (DEBUG_SESSION) csvWriter.writeRegisteredLines();
    }

    private static void printTimeLog(String log, long startMillis) {
        long elapsedMillis = System.currentTimeMillis() - startMillis;
        long elapsedMinuts = elapsedMillis / 60000;
        elapsedMillis = elapsedMillis - elapsedMinuts * 60000;
        long elapsedSeconds = elapsedMillis / 1000;
        elapsedMillis = elapsedMillis - elapsedSeconds * 1000;
        System.out.println("[[" + elapsedMinuts + " min, " + elapsedSeconds + " s, " + elapsedMillis + " ms - " + log + "]]");
    }

}
